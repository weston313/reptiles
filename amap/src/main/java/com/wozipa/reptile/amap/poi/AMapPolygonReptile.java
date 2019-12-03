package com.wozipa.reptile.amap.poi;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.coordinate.Polygon;
import sun.plugin.com.AmbientProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 请输入左下和右上点的坐标值
 */
public class AMapPolygonReptile {

    public static Logger LOGGER = Logger.getLogger(AMapPolygonReptile.class);

    private Coordinate[] points;

    public AMapPolygonReptile(){
        this(-180d, -90d, 180d, 90d);
    }

    public AMapPolygonReptile(double x1, double y1, double x2, double y2){
        this(new Coordinate[]{
                new Coordinate(x1, y1),
                new Coordinate(x1, y2),
                new Coordinate(x2, y2),
                new Coordinate(x2, y1),
                new Coordinate(x1, y1)
        });
    }

    public AMapPolygonReptile(Coordinate[] points){
        this.points = points;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for(Coordinate point: this.points){
            if(index > 0) sb.append("|");
            sb.append("(").append(point.getX()).append(",").append(point.getY());
        }
        return sb.toString();
    }
}
