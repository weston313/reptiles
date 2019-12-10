package com.wozipa.reptile.bdmap.poi;

import com.wozipa.reptile.bdmap.common.BDMapServers;
import com.wozipa.reptile.common.Configuration;
import com.wozipa.reptile.common.geo.Rectangle;

public class BDMapPOIReptile {

    private Configuration configuration = Configuration.GetInstance();

    private Rectangle rectangle;

    public BDMapPOIReptile(){
        this(70,  0, 140, 60);
    }

    private BDMapPOIReptile(double x1, double y1, double x2, double y2){
        this.rectangle = new Rectangle(x1, y1, x2, y2);
    }

    public void reptole(){

        StringBuffer sb = new StringBuffer();
        sb.append(BDMapServers.SERVER_URL).append("?")
                .append(BDMapServers.COMMON_PARAM_AK).append("=").append(configuration.get("bdmap.ak"))
                .append("&").append(BDMapServers.Bounds.RECT_PARAM_BOUNDS).append("=%s")
                .append("&").append(BDMapServers.COMMON_PARAM_QUERY).append("=%s")
                .append("&").append(BDMapServers.COMMON_PARAM_QUERY)
        ;
    }

    private void doReptime(String url, Rectangle rect){
        // 检查参数
        if(rect == null) return;
        if(url == null || url.equals("")) return;

        // 组装服务URL

        String serverUrl = String.format(url, rectangleToString(rect));
    }

    private String rectangleToString(Rectangle rect){
        StringBuffer sb = new StringBuffer();
        sb.append(rect.getMinX()).append(",")
                .append(rect.getMinY()).append(",")
                .append(rect.getMaxX()).append(",")
                .append(rect.getMaxY());

        return sb.toString();
    }

}
