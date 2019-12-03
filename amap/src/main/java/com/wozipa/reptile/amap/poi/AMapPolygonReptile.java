package com.wozipa.reptile.amap.poi;

import org.apache.log4j.Logger;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.coordinate.Polygon;
import sun.plugin.com.AmbientProperty;

public class AMapPolygonReptile {

    public static Logger LOGGER = Logger.getLogger(AMapPolygonReptile.class);

    public AMapPolygonReptile(){
        this(0d, 0d, 0d, 0d);
    }

    public AMapPolygonReptile(double x1, double y1, double x2, double y2){
    }

    public AMapPolygonReptile(Point p1, Point p2){

    }

    public AMapPolygonReptile(Polygon polygon){

    }

 }
