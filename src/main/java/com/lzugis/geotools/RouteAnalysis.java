package com.lzugis.geotools;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
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
import org.geotools.graph.structure.*;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RouteAnalysis {
    private static final long serialVersionUID = 1L;

    private static String rootPath = System.getProperty("user.dir");
    private Graph networkGraph;
    private MemoryFeatureCollection stops;
    private MemoryFeatureCollection route;
    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    String shpPath = rootPath + "/data/shp/rail_segment.shp";

    public RouteAnalysis() throws IOException, SchemaException {
        super();
        File file = new File(shpPath);

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        stops = new MemoryFeatureCollection(DataUtilities.createType("Stops",
                "the_geom:Point:srid=4326," + // <- the geometry attribute: Point type
                        "name:String"
        ));
        route = new MemoryFeatureCollection(DataUtilities.createType("Route",
                "the_geom:LineString:srid=4326," + // <- the geometry attribute: Point type
                        "name:String"
        ));

        FeatureGraphGenerator featureGen = new FeatureGraphGenerator(
                new DirectedLineStringGraphGenerator()
        );
        FeatureCollection<?, ?> networkFC = featureSource.getFeatures();
        FeatureIterator<?> iter = networkFC.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iter.next();
                featureGen.add(feature);
            }
            System.out.println("---end build graph---");
        } finally {
            iter.close();
        }

        networkGraph = featureGen.getGraph();
    }

    protected Map searchRoute() throws Exception {
        Map map = new HashMap();

        Iterator<SimpleFeature> itr = stops.iterator();
        Point destinationPoint = (Point) itr.next().getDefaultGeometry();
        Point originPoint = (Point) itr.next().getDefaultGeometry();

        Node source = getNearestGraphNode(originPoint);
        Node destination = getNearestGraphNode(destinationPoint);

        AStarFunctions asfunc = new AStarFunctions(destination) {
            @Override
            public double cost(AStarNode n1, AStarNode n2) {
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

        AStarShortestPathFinder finder = new AStarShortestPathFinder(networkGraph, source, destination, asfunc);
        finder.calculate();
        Path path = finder.getPath();

        Geometry routeGeo = null;
        Node previous = null, node = null;
        for (Iterator<?> ritr = path.riterator(); ritr.hasNext(); ) {
            node = (Node) ritr.next();
            if (previous != null) {
                // Adds the resulting edge into the vector
                Edge eg = node.getEdge(previous);
                SimpleFeature feature = (SimpleFeature) eg.getObject();
                int id = (int) feature.getAttribute("id");
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

        map.put("route", routeGeo);
        return map;
    }

    private Node getNearestGraphNode(Point pointy) {
        double dist = 0;
        Node nearestNode = null;
        for (Object o : networkGraph.getNodes()) {
            Node n = (Node) o;
            Point p = ((Point) n.getObject());
            double newdist = p.distance(pointy);

            if (nearestNode == null || newdist < dist) {
                dist = newdist;
                nearestNode = n;
            }
        }
        return nearestNode;
    }

    private Map getRoute(double[][] stopsPt) {
        stops.clear();

        for (int i = 0; i < stopsPt.length; i++) {
            double[] stopPt = stopsPt[i];
            SimpleFeatureBuilder stopBuilder = new SimpleFeatureBuilder(stops.getSchema());
            stopBuilder.add(geometryFactory.createPoint(
                    new Coordinate(stopPt[0], stopPt[1])));
            stopBuilder.add("hello");
            stops.add(stopBuilder.buildFeature(null));
        }
        try {
            return searchRoute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        double[][] stopsPt = new double[][]{
                {111.00932312011719, 35.05805969238281},
                {110.72885131835938, 34.55958938598633}
        };
        try {
            RouteAnalysis route = new RouteAnalysis();
            Map map = route.getRoute(stopsPt);
            System.out.println(map.get("route"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
