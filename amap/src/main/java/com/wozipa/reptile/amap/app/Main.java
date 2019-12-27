package com.wozipa.reptile.amap.app;

import com.wozipa.reptile.amap.common.DataTypeEnum;
import com.wozipa.reptile.amap.poi.AMapPOIReptile;

public class Main {

    public static void main(String[] args){
        String dataType = args[0];

        switch (DataTypeEnum.valueOf(dataType.toUpperCase())) {
            case POI:
                new AMapPOIReptile().reptile();
                break;
        }
    }
}
