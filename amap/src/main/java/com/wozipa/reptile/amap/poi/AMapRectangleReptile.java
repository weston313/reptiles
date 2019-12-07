package com.wozipa.reptile.amap.poi;

import com.alibaba.fastjson.JSONObject;
import com.wozipa.reptile.amap.common.AMapPOIServers;
import com.wozipa.reptile.common.Configuration;
import com.wozipa.reptile.common.http.HttpGetClient;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 请输入左下和右上点的坐标值
 */
public class AMapRectangleReptile {

    public static Logger LOGGER = Logger.getLogger(AMapRectangleReptile.class);
    private static GeometryFactory FACTORT = JTSFactoryFinder.getGeometryFactory();

    private Polygon polygon;
    private int offset = Integer.parseInt(Configuration.GetInstance().get("amap.offset"));
    private String outputFilePath = System.getProperty("user.home")
            + "/reptiles/" + "AMAP_REPTILE_"
            + new SimpleDateFormat("yyyyMMdd").format(new Date())
            + ".json";

    public AMapRectangleReptile() {
        this(0d, 0d, 180d, 90d);
    }

    public AMapRectangleReptile(double x1, double y1, double x2, double y2) {
        this(new Coordinate[]{
                new Coordinate(x1, y1),
                new Coordinate(x1, y2),
                new Coordinate(x2, y2),
                new Coordinate(x2, y1),
                new Coordinate(x1, y1)
        });
    }

    public AMapRectangleReptile(Coordinate[] points) {
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
    private String polygonText(Polygon polygon) {
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for (Coordinate point : polygon.getCoordinates()) {
            if (index > 0) sb.append("%7C");
            sb.append(point.getX()).append(",").append(point.getY());
            index++;
        }
        return sb.toString();
    }

    public void reptile() {
        System.out.println("Replation AMap " + toString());

        // 开始进行数据爬取
        Configuration configuration = Configuration.GetInstance();
        String polyServerUrl = AMapPOIServers.POLYGON.POLYGON_SERVER_URL + "?"
                + AMapPOIServers.POLYGON.POLYGON_PARAM_POLYGON + "=%s" + "&"
                + AMapPOIServers.POI_SERVER_PARAM_KEY + "=" + configuration.get("amap.key") + "&"
                + "types=%s" + "&"
                + "offset=" + offset;

        for(String type : AMapPOIServers.POI_FIRST_LEVE_TYPES){
            doReptile(polyServerUrl, this.polygon, type);
        }
    }


    private void doReptile(String serverUrl, Polygon polygon, String type) {
        // 判断服务连接为空
        if (serverUrl == null || serverUrl.equals("")) {
            System.out.println("Polygon Server Url is empty.");
            return;
        }

        // 判断多边形是否为空
        if (polygon == null && polygon.isEmpty()) {
            System.out.println("Polygon is Empty.");
            return;
        }

        // 考虑到个人账号查询次数有限，所以进行3秒休息
        try {
            Thread.sleep(Long.parseLong(Configuration.GetInstance().get("amap.sleep")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取多边形字符串，然后进行字符串替换
        String polyStr = polygonText(polygon);
        String polySerUrl = String.format(serverUrl, polyStr,  type);

        System.out.println("Reptile " + polySerUrl);
        HttpGetClient client = new HttpGetClient(polySerUrl);
        String content = null;
        if (client.sendRequest()) {
            content = client.getReponseContent();
        }

        // 检查content数据内容
        JSONObject root = JSONObject.parseObject(content);
        long count = root.getLong("count");
        if (count >= offset) {
            // 进行空间切换，重新爬取数据
            Polygon[] children = splitPolygon(polygon);
            for(Polygon child : children){
                doReptile(serverUrl, child, type);
            }
        }
        else if(count > 0){
            writeContent(content);
        }
    }

    /**
     * 爬取的结果写出到文件中
     * @param content
     */
    private void writeContent(String content){
        System.out.println(content);
        File file = new File(this.outputFilePath);
        BufferedWriter writer = null;
        try {
            if(!file.exists()) file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Polygon[] splitPolygon(Polygon polygon){
        Envelope envelope = polygon.getEnvelopeInternal();

        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        double minY = envelope.getMinY();
        double maxY = envelope.getMaxY();
        double centerX = envelope.centre().getX();
        double centerY = envelope.centre().getY();

        // 进行空间多边形切割
        return new Polygon[] {
                intersect(polygon, new Envelope(minX, centerX, minY, centerY)),
                intersect(polygon, new Envelope(minX, centerX, centerY, maxY)),
                intersect(polygon, new Envelope(centerX, maxX, centerY, maxY)),
                intersect(polygon, new Envelope(centerX, maxX, minY, centerY)),
        };
    }

    private Polygon intersect(Polygon polygon, Envelope envelope){
        Polygon ep = convertEnvelopToPolygon(envelope);
        Geometry geometry = polygon.intersection(ep);
        return FACTORT.createPolygon(geometry.getCoordinates());
    }

    private Polygon convertEnvelopToPolygon(Envelope envelope){
        if(envelope == null) return null;
        return FACTORT.createPolygon(new Coordinate[]{
                new Coordinate(envelope.getMinX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMinY())
        });
    }
}