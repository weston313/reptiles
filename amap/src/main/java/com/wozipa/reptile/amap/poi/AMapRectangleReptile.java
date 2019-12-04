package com.wozipa.reptile.amap.poi;

import com.alibaba.fastjson.JSONObject;
import com.wozipa.reptile.amap.common.AMapPOIServers;
import com.wozipa.reptile.common.Configuration;
import com.wozipa.reptile.common.http.HttpGetClient;
import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import sun.security.krb5.Config;

import javax.security.jacc.PolicyConfiguration;

/**
 * 请输入左下和右上点的坐标值
 */
public class AMapRectangleReptile {

    public static Logger LOGGER = Logger.getLogger(AMapRectangleReptile.class);

    private Polygon polygon;

    public AMapRectangleReptile(){
        this(-180d, -90d, 180d, 90d);
    }

    public AMapRectangleReptile(double x1, double y1, double x2, double y2){
        this(new Coordinate[]{
                new Coordinate(x1, y1),
                new Coordinate(x1, y2),
                new Coordinate(x2, y2),
                new Coordinate(x2, y1),
                new Coordinate(x1, y1)
        });
    }

    public AMapRectangleReptile(Coordinate[] points){
        GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
        this.polygon = factory.createPolygon(points);
    }

    @Override
    public String toString() {
        return polygonText(this.polygon);
    }

    /**
     * 将Polygon转换成字符串类型
     */
    private String polygonText(Polygon polygon){
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for(Coordinate point: this.polygon.getCoordinates()){
            if(index > 0) sb.append("|");
            sb.append(point.getX()).append(",").append(point.getY());
            index ++ ;
        }
        return sb.toString();
    }

    public void reptile(){
        System.out.println("Replation AMap " + toString());

        // 开始进行数据爬取
        Configuration configuration = Configuration.GetInstance();
        String polyServerUrl = AMapPOIServers.POLYGON.POLYGON_SERVER_URL + "?"
                + AMapPOIServers.POLYGON.POLYGON_PARAM_POLYGON + "=%s"
                + AMapPOIServers.POI_SERVER_PARAM_KEY + "=" + configuration.get("amap.key");

        doReptile(polyServerUrl, this.polygon);
        // String polyStr = polygonText(this.polygon);
    }


    private void doReptile(String serverUrl, Polygon polygon){
        // 判断服务连接为空
        if(serverUrl == null || serverUrl.equals("")) {
            System.out.println("Polygon Server Url is empty.");
            return ;
        }

        // 判断多边形是否为空
        if(polygon == null && polygon.isEmpty()) {
            System.out.println("Polygon is Empty.");
            return ;
        }

        //获取多边形字符串，然后进行字符串替换
        String polyStr = polygonText(polygon);
        String polySerUrl = String.format(serverUrl, polyStr);

        HttpGetClient client = new HttpGetClient(polySerUrl);
        String content = null;
        if(client.sendRequest()) {
            content = client.getReponseContent();
        }

        // 将数据进行写出

        // 检查content数据内容
        JSONObject root = JSONObject.parseObject(content);
        long count = root.getLong("count");
        if(count >= 1000) {
            // 进行空间切换，重新爬取数据

        }
    }
//    private Polygon[] cutPlolygon(Polygon polygon){
//
//    }

}
