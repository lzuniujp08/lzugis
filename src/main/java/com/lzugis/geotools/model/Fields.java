package com.lzugis.geotools.model;

public class Fields {

    public String getFieldmame() {
        return fieldmame;
    }

    public void setFieldmame(String fieldmame) {
        this.fieldmame = fieldmame;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    private String fieldmame, fieldtype;

    public Fields(String fieldname, String fieldtype){
        this.fieldmame = fieldname;
        this.fieldtype = fieldtype;
    }

}
