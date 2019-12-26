package com.wozipa.reptile.bdmap.poi;

import com.alibaba.fastjson.JSONObject;
import com.wozipa.reptile.bdmap.common.BDMapServers;
import com.wozipa.reptile.common.Configuration;
import com.wozipa.reptile.common.geo.Rectangle;
import com.wozipa.reptile.common.http.HttpGetClient;
import com.wozipa.reptile.common.output.TextWriter;
import com.wozipa.reptile.common.output.Writer;

public class BDMapPOIReptile {

    private Configuration configuration = Configuration.GetInstance();

    private Rectangle rectangle;
    private long reptileCount = 0;
    private String outputFilePath = System.getProperty("user.home") + "/reptiles/BDMAP_REPTILE_%s.json";

    public BDMapPOIReptile(){
        this(70,  0, 140, 60);
    }

    private BDMapPOIReptile(double x1, double y1, double x2, double y2){
        this.rectangle = new Rectangle(x1, y1, x2, y2);
    }

    public void reptile(){

        StringBuffer sb = new StringBuffer();
        sb.append(BDMapServers.SERVER_URL).append("?")
                .append(BDMapServers.COMMON_PARAM_AK).append("=").append(configuration.get("bdmap.ak"))
                .append("&").append(BDMapServers.Bounds.RECT_PARAM_BOUNDS).append("=%s")
                .append("&").append(BDMapServers.COMMON_PARAM_QUERY).append("=%s")
                .append("&").append(BDMapServers.RECT_PARAM_PAGE_SIZE).append("=20")
                .append("&output=json");
        String serverUrl = sb.toString();

        Writer writer = null;
        try {
            for(String poiType : BDMapServers.POT_TYPES){
                writer = new TextWriter(String.format(this.outputFilePath, poiType));
                doReptile(serverUrl, this.rectangle, poiType, writer);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(this.reptileCount);
    }

    private void doReptile(String url, Rectangle rect, String poiType, Writer writer){
        // 检查参数
        if(rect == null) return;
        if(url == null || url.equals("")) return;

        // 组装服务URL
        String rectBounds = rectangleToString(rect);
        System.out.println(rectBounds);
        String serverUrl = String.format(url, rectBounds, poiType);
        System.out.println("Reptile " + serverUrl);

        // 进行参数访问
        String content = null;
        HttpGetClient client = new HttpGetClient(serverUrl);
        if(client.sendRequest(3)) {
            content = client.getReponseContent();
        }

        //
        if(content == null || content.equals("")) return;
        JSONObject object = JSONObject.parseObject(content);
        if(object.containsKey("total")){
            if(object.getInteger("total") < 20){
                reptileCount = reptileCount + object.getInteger("total");
                writer.append(content);
            }
            else {
                Rectangle[] rectangles = rect.split(rect.getCenterX(), rect.getCenterY());
                for (Rectangle rectangle : rectangles) {
                    doReptile(url, rectangle, poiType, writer);
                }
            }
        }
        else return;
    }



    private String rectangleToString(Rectangle rect){
        StringBuffer sb = new StringBuffer();
        sb.append(rect.getMinY()).append(",")
                .append(rect.getMinX()).append(",")
                .append(rect.getMaxY()).append(",")
                .append(rect.getMaxX());

        return sb.toString();
    }

}
