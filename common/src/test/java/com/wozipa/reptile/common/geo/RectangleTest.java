package com.wozipa.reptile.common.geo;

import org.junit.Test;
import org.w3c.dom.css.Rect;

import static org.junit.Assert.*;

public class RectangleTest {

    @Test
    public void testSplit(){
        Rectangle rect = new Rectangle(-180,  -90, 180, 90);
        Rectangle[] rects = rect.split(rect.getCenterX(), rect.getCenterY());

        for(Rectangle r : rects){
            System.out.println(r.toString());
        }
    }

}