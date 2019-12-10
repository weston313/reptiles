package com.wozipa.reptile.common.geo;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

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
                (this.minX + this.maxY) /2
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
        BigDecimal bg = new BigDecimal(this.minX);
        return bg.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Envelope convertToEnvelope(){
        return new Envelope(this.minX, this.maxX, this.minY, this.maxY);
    }

    public Geometry convertToPolygon(){
        Coordinate[] points = new Coordinate[] {
                new Coordinate(this.minX, this.minY),
                new Coordinate(this.minX, this.maxY),
                new Coordinate(this.maxX, this.maxY),
                new Coordinate(this.maxX, this.minY),
                new Coordinate(this.minX, this.minY)
        };
        return factory.createPolygon(points);
    }
    
}
