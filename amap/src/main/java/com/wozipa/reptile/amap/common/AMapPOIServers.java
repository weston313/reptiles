package com.wozipa.reptile.amap.common;

import javax.print.DocFlavor;


/**
 * 高德地图POI查询相关服务
 */
public class AMapPOIServers {

    // 查询服务所需要的KEY
    public static String POI_SERVER_PARAM_KEY = "key";

    public static class POLYGON {
        // 按照多边形查询
        public static String POLYGON_SERVER_URL = "https://restapi.amap.com/v3/place/polygon?parameters";
        public static String POLYGON_PARAM_POLYGON = "polygon";
        public static String POLYGON_PARAM_KEYWORDS = "keywords";
        public static String POLYGON_PARAM_TYPES = "types";
        public static String POLYGON_PARAM_OFFSET = "offset";
        public static String POLYGON_PARAM_PAGE = "page";
        public static String POLYGON_PARAM_EXTENSIONS = "extensions";
        public static String POLYGON_PARAM_SIG = "sig";
        public static String POLYGON_PARAM_OUTPUT = "output";
        public static String POLYGON_PARAM_CALLBACK = "callback";
    }





}
