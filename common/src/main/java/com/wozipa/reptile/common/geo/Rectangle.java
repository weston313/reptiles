package com.wozipa.reptile.common.geo;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.w3c.dom.css.Rect;

import java.math.BigDecimal;

public class Rectangle {

    private GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
    private double minX, minY, maxX, maxY;

    public Rectangle(double x1, double y1, double x2, double y2){
        this.minX = x1;
        this.minY = y1;
        this.maxX = x2;
        this.maxY = y2;
    }

    public Rectangle(Rectangle rectangle){
        this(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
    }

    public Rectangle(Envelope envelope){
        this(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY());
    }

    public Rectangle(Geometry geometry){
        this(geometry.getEnvelopeInternal());
    }

    public double getMinX() {
        return getMinX(6);
    }

    public double getMinX(int decimal) {
        return convertDecimal(this.minX, decimal);
    }

    public double getMinY() {
        return getMinY(6);
    }

    public double getMinY(int decimal){
        return convertDecimal(this.minY, decimal);
    }

    public double getMaxX() {
        return getMaxX(6);
    }

    public double getMaxX(int decimal) {
        return convertDecimal(this.maxX, decimal);
    }

    public double getMaxY() {
        return getMaxY(6);
    }

    public double getMaxY(int decimal) {
        return convertDecimal(this.maxY, decimal);
    }

    public double getCenterX(){
        return getCenterX(6);
    }

    public double getCenterX(int decimal){
        return convertDecimal(
                (this.minX + this.maxX) /2
                , decimal
        );
    }

    public double getCenterY(){
        return getCenterY(6);
    }

    public double getCenterY(int decimal) {
        return convertDecimal(
                (this.minY + this.maxY) /2
                , decimal
        );
    }

    private double convertDecimal(double value, int decimal){
        BigDecimal bg = new BigDecimal(value);
        return bg.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Envelope convertToEnvelope(){
        return new Envelope(this.minX, this.maxX, this.minY, this.maxY);
    }

    public Geometry convertToPolygon(Rectangle rect){
        Coordinate[] points = new Coordinate[] {
                new Coordinate(rect.getMinX(), rect.getMinY()),
                new Coordinate(rect.getMinX(), rect.getMaxY()),
                new Coordinate(rect.getMaxX(), rect.getMaxY()),
                new Coordinate(rect.getMaxX(), rect.getMinY()),
                new Coordinate(rect.getMinX(), rect.getMinY())
        };
        return factory.createPolygon(points);
    }

    public Rectangle[] split(double x, double y){
        return new Rectangle[] {
                new Rectangle(minX, minY, x, y),
                new Rectangle(minX, y, x, maxY),
                new Rectangle(x, y, maxX, maxY),
                new Rectangle(x, minY, maxY, y)
        };
    }
    
}
