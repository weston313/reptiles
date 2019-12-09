package com.wozipa.reptile.amap.poi;

import com.alibaba.fastjson.JSONObject;
import com.wozipa.reptile.amap.common.AMapPOIServers;
import com.wozipa.reptile.common.Configuration;
import com.wozipa.reptile.common.http.HttpGetClient;
import com.wozipa.reptile.common.output.TextWriter;
import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 请输入左下和右上点的坐标值
 */
public final class AMapPOIReptile {

    public static Logger LOGGER = Logger.getLogger(AMapPOIReptile.class);
    private static GeometryFactory FACTORT = JTSFactoryFinder.getGeometryFactory();

    private Polygon polygon;
    private long reptileCount = 0;
    private int offset = Integer.parseInt(Configuration.GetInstance().get("amap.offset"));
    private String outputFilePath = System.getProperty("user.home") + "/reptiles/AMAP_REPTILE_%s.json";

    public AMapPOIReptile() {
        this(70,  0, 140, 60);
    }

    private AMapPOIReptile(double x1, double y1, double x2, double y2) {
        this(new Coordinate[]{
                new Coordinate(x1, y1),
                new Coordinate(x1, y2),
                new Coordinate(x2, y2),
                new Coordinate(x2, y1),
                new Coordinate(x1, y1)
        });
    }

    private AMapPOIReptile(Coordinate[] points) {
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

        // 开始进行数据爬取
        String polyServerUrl = createServerUrl();
        for(String type : AMapPOIServers.POI_FIRST_LEVE_TYPES){
            TextWriter writer = null;
            try {
                writer = new TextWriter(String.format(this.outputFilePath, type));
                doReptile(polyServerUrl, this.polygon, type, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Type" + type + " Total Count " + this.reptileCount);
            this.reptileCount = 0;
        }

    }

    /**
     * 创建URL字符串
     *
     * @return
     */
    private String createServerUrl() {
        Configuration configuration = Configuration.GetInstance();
        return AMapPOIServers.POLYGON.POLYGON_SERVER_URL + "?"
                + AMapPOIServers.POLYGON.POLYGON_PARAM_POLYGON + "=%s" + "&"
                + AMapPOIServers.POI_SERVER_PARAM_KEY + "=" + configuration.get("amap.key") + "&"
                + "types=%s" + "&"
                + "offset=" + offset;
    }


    private void doReptile(String serverUrl, Polygon polygon, String type, TextWriter writer) {
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

        HttpGetClient client = new HttpGetClient(polySerUrl);
        String content = null;
        if (client.sendRequest()) {
            content = client.getReponseContent();
        }

        // 检查content数据内容
        JSONObject root = JSONObject.parseObject(content);

        if(root.getInteger("status") == 0) {
            return;
        }

        long count = root.getLong("count");
        if (count >= offset) {
            // 进行空间切换，重新爬取数据
            Polygon[] children = splitPolygon(polygon);
            for(Polygon child : children){
                doReptile(serverUrl, child, type, writer);
            }
        }
        else if(count > 0){
            reptileCount = reptileCount + count;
            writer.append(content);
        }
    }

    /**
     * 爬取的结果写出到文件中
     * @param content
     */
    private void writeContent(String content, String filePath){
        System.out.println(content);
        File file = new File(filePath);
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
        DecimalFormat format = new DecimalFormat("#.000000");
        Envelope envelope = polygon.getEnvelopeInternal();

        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        double minY = envelope.getMinY();
        double maxY = envelope.getMaxY();
        double centerX = doubleCutSix((minX + maxX)/2);
        double centerY = doubleCutSix((minY + maxY)/2);

        // 进行空间多边形切割
        double surge = 0.000001;
        return new Polygon[] {
                intersect(polygon, new Envelope(minX - surge, centerX - surge, minY + surge, centerY + surge)),
                intersect(polygon, new Envelope(minX - surge, centerX- surge, centerY + surge, maxY + surge)),
                intersect(polygon, new Envelope(centerX- surge, maxX- surge, centerY + surge, maxY + surge)),
                intersect(polygon, new Envelope(centerX- surge, maxX- surge, minY + surge, centerY + surge)),
        };
    }

    private double doubleCutSix(double d){
        return new BigDecimal(d).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
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