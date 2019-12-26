package com.wozipa.reptile.bdmap.common;

public class BDMapServers {

    public static String SERVER_URL = "http://api.map.baidu.com/place/v2/search";

    public static String COMMON_PARAM_QUERY = "query";
    public static String COMMON_PARAM_AK = "ak";
    public static String COMMON_PARAM_SN = "sn";
    public static String COMMON_PARAM_SN_TIMESTAMP = "timestamp";
    public static String COMMON_PARAM_OUTPUT = "output";
    public static String COMMON_PARAM_SCOPE = "scope";
    public static String COMMON_PARAM_FILTER = "filter";
    public static String COMMON_PARAM_TAG = "tag";
    public static String RECT_PARAM_PAGE_SIZE = "page_size";
    public static String RECT_PARAM_PAGE_NUM = "page_num";
    public static String RECT_PARAM_COOR_TYPE = "coord_type";
    public static String RECT_PARAM_RET_COOR_TYPE = "ret_coordtype";

    public static String[] POT_TYPES = new String[] {
            "美食", "酒店", "购物", "生活服务", "丽人", "旅游景点", "休闲娱乐", "运动健身",
            "教育培训", "文化传媒", "医疗", "汽车服务", "交通设施", "金融", "房地产",
            "公司企业", "政府机构", "出入口", "自然地物"
    };

    public static class Bounds {
        public static String RECT_PARAM_BOUNDS = "bounds";
    }
}
