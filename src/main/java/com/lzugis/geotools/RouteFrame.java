package com.lzugis.geotools;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.graph.build.feature.FeatureGraphGenerator;
import org.geotools.graph.build.line.DirectedLineStringGraphGenerator;
import org.geotools.graph.path.AStarShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.AStarIterator.AStarFunctions;
import org.geotools.graph.traverse.standard.AStarIterator.AStarNode;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class RouteFrame extends JMapFrame{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static String rootPath = System.getProperty("user.dir");
	private Graph networkGraph;
	private MemoryFeatureCollection stops;
	private MemoryFeatureCollection route;
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	private MapContent map;

	public RouteFrame() throws IOException, SchemaException {
		super();

//		File file = JFileDataStoreChooser.showOpenFile("shp", null);
//        if (file == null) {
//            return;
//        }
		String shpPath = rootPath + "/data/shp/rail_segment.shp";
		File file = new File(shpPath);

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        map = new MapContent();
        map.setTitle("Route");
        
        Style style = SLD.createSimpleStyle(featureSource.getSchema(), Color.GRAY);
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        
        stops = new MemoryFeatureCollection(DataUtilities.createType("Stops",
                "the_geom:Point:srid=4326," + // <- the geometry attribute: Point type
                "name:String"
        ));
        
        map.addLayer(new FeatureLayer(stops
        		, SLD.createSimpleStyle(stops.getSchema(), Color.red)));
        
        route = new MemoryFeatureCollection(DataUtilities.createType("Route",
                "the_geom:LineString:srid=4326," + // <- the geometry attribute: Point type
                "name:String"
        ));
        
        map.addLayer(new FeatureLayer(route
        		, SLD.createSimpleStyle(route.getSchema(), Color.red)));
        
        this.setMapContent(map);
        enableStatusBar(true);
        enableToolBar(true);
        
        FeatureGraphGenerator featureGen = new FeatureGraphGenerator(
			new DirectedLineStringGraphGenerator()
		);
		FeatureCollection<?, ?> networkFC = featureSource.getFeatures();
		FeatureIterator<?> iter = networkFC.features();
		try {
			System.out.println("build graph:");
			while (iter.hasNext()) {
				SimpleFeature feature = (SimpleFeature)iter.next();
				int id = (int) feature.getAttribute("id");
//				System.out.println(id);
				featureGen.add(feature);
			}
		} finally {
			iter.close();
		}
		
		networkGraph = featureGen.getGraph();
		System.out.println("graph:");
		for(Object i:networkGraph.getEdges()){
			DirectedEdge e=(DirectedEdge) i;
			SimpleFeature aLineString = (SimpleFeature) e.getObject();
			int id = (int) aLineString.getAttribute("id");
//			System.out.println(id+": ["+e.getInNode()+" ,"+e.getOutNode()+"]");
		}
		
		this.getMapPane().addMouseListener(new MapMouseAdapter() {
						
			@Override
			public void onMouseClicked(MapMouseEvent arg0) {
				if(stops.size()==2)
					stops.clear();
				
				SimpleFeatureBuilder stopBuilder = new SimpleFeatureBuilder(stops.getSchema());
				stopBuilder.add(geometryFactory.createPoint(
						new Coordinate(arg0.getWorldPos().x, arg0.getWorldPos().y)));
				stopBuilder.add("hello");
				stops.add(stopBuilder.buildFeature(null));

				if(stops.size()==2)
					try{
						searchRoute();
					}catch(Throwable e){
						e.printStackTrace();
					}
				
				RouteFrame.this.getMapPane().setDisplayArea(RouteFrame.this.getMapPane().getDisplayArea());
			}
		});
	}

	protected void searchRoute() throws Exception {
		Iterator<SimpleFeature> itr = stops.iterator();
		Point destinationPoint = (Point) itr.next().getDefaultGeometry();
		Point originPoint = (Point) itr.next().getDefaultGeometry();
		
		Node source = getNearestGraphNode(originPoint);		
		Node destination = getNearestGraphNode(destinationPoint);
		
		AStarFunctions asfunc=new AStarFunctions(destination){
			@Override
			public double cost(AStarNode n1, AStarNode n2) {
				DirectedNode nd1 = (DirectedNode) n1.getNode();
				DirectedNode nd2 = (DirectedNode) n2.getNode();
				DirectedEdge e = (DirectedEdge) nd1.getEdge(nd2);
				if(e!=null){
					SimpleFeature feature = (SimpleFeature) e.getObject();
					Integer id = (Integer) feature.getAttribute("id");
					Double type = (Double) feature.getAttribute("type");//1 means A~B;-1 means B~A;0 means not one-way road
					boolean t=e.getInNode().equals(nd1) && e.getOutNode().equals(nd2);
					
//					System.out.println(id+"-"+type+": ["+e.getInNode()+" ,"+e.getOutNode()+"]");
//					System.out.println(nd1+"~"+nd2);
//					System.out.println(type==1 && !t);
					
					if(type==1 && !t)
						return Double.POSITIVE_INFINITY;

					if(type==-1 && t)
						return Double.POSITIVE_INFINITY;
				}
				return ((Point) n1.getNode().getObject())
						.distance((Point) n2.getNode()
								.getObject());
			}

			@Override
			public double h(Node n) {
				return ((Point) n.getObject())
						.distance((Point) this.getDest().getObject());
			}
			
		};
		
		System.out.println("find:");
		AStarShortestPathFinder finder = new AStarShortestPathFinder(networkGraph, source, destination, asfunc);
		finder.calculate();
		Path path = finder.getPath();
		
//		EdgeWeighter weighter = new EdgeWeighter() {
//			public double getWeight(org.geotools.graph.structure.Edge e) {
//				SimpleFeature aLineString = (SimpleFeature) e.getObject();
//				Geometry geom = (Geometry) aLineString.getDefaultGeometry();
//				return 1.0/geom.getLength();
//			}
//		};
//		DijkstraShortestPathFinder dspf = new DijkstraShortestPathFinder(networkGraph, source, weighter);
//		dspf.calculate();	
//		Path path = dspf.getPath(destination);
		
		Geometry routeGeo = null;
		System.out.println("route:");
		Node previous = null, node = null;
		for (Iterator<?> ritr = path.riterator(); ritr.hasNext();) {
			node = (Node) ritr.next();
			if (previous != null) {
				// Adds the resulting edge into the vector
				Edge eg = node.getEdge(previous);
				SimpleFeature feature = (SimpleFeature) eg.getObject();
				int id = (int) feature.getAttribute("id");
				System.out.println(id);
				Geometry geom = (Geometry) feature.getDefaultGeometry();
				if (routeGeo == null) {
					routeGeo = geom;
				} else {
					routeGeo = routeGeo.union(geom);
				}
			}
			previous = node;
		}
		
		route.clear();
		
		SimpleFeatureBuilder routeBuilder = new SimpleFeatureBuilder(route.getSchema());
		routeBuilder.add(routeGeo);
		routeBuilder.add("hello");
		route.add(routeBuilder.buildFeature(null));
		
		System.out.println(routeGeo);
	}

	private Node getNearestGraphNode(Point pointy) {
		double dist = 0;
		Node nearestNode = null;
		for (Object o : networkGraph.getNodes()) {
			Node n = (Node) o;
			Point p = ((Point) n.getObject());
			double newdist = p.distance(pointy);
			
			if(nearestNode == null || newdist < dist){
				dist = newdist;
				nearestNode = n;
			}
		}
		return nearestNode;
	}

	public static void main(String[] args) throws IOException {
		System.setProperty("org.geotools.referencing.forceXY", "true");		

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				try {
					RouteFrame frm = new RouteFrame();
	                frm.pack();
	                frm.setSize(800,600);
	                frm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

            }
        });
	}	
}
