package com.wozipa.reptile.amap.common;

/**
 * 高德地图POI查询相关服务
 */
public class AMapPOIServers {

    // 查询服务所需要的KEY
    public static String POI_SERVER_PARAM_KEY = "key";


    public static String[] POI_FIRST_LEVE_TYPES = new String[] {
            "180000", "010000",  "060000", "970000", "150000", "990000", "030000", "130000", "140000", "220000",
            "070000", "160000", "170000", "090000", "200000", "110000", "050000", "190000", "080000", "020000",
            "100000", "120000", "040000"
    };

    public static String GetFisrtTypeString(){
        return GetFisrtTypeString("", "" , ",");
    }

    public static String GetFisrtTypeString(String split){
        return GetFisrtTypeString("", "" , split);
    }

    public static String GetFisrtTypeString(String start, String end, String split){
        StringBuffer sb = new StringBuffer();
        sb.append(start);
        for(int index = 0; index < POI_FIRST_LEVE_TYPES.length; index ++ ){
            if(index > 0) sb.append(split);
            sb.append(POI_FIRST_LEVE_TYPES[index]);
        }
        return sb.toString();
    }

    public static class POLYGON {
        // 按照多边形查询
        public static String POLYGON_SERVER_URL = "https://restapi.amap.com/v3/place/polygon";
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
