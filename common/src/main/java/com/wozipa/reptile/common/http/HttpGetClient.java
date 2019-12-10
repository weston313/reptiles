package com.wozipa.reptile.common.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HttpGetClient {

    public static Logger LOGGER = Logger.getLogger(HttpGetClient.class);

    private String url;

    private CloseableHttpResponse response = null;

    private CloseableHttpClient client = HttpClients.createDefault();


    public HttpGetClient() {
        this(null);
    }

    public HttpGetClient(String url) {
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public boolean sendRequest(){
        return sendRequest(1);
    }

    public boolean sendRequest(int times) {
        if(url == null || url.equals(""))  {
            LOGGER.info("URL is empty and cannot send request.");
            return false;
        }

        int time = 0;
        boolean needSend = true;
        boolean success = false;

        // 用户设置重试次数进行网络重试
        while(time < times && needSend){
            try {
                HttpGet httpGet = new HttpGet(url);
                this.response = client.execute(httpGet);
                needSend =  false;
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public String getReponseContent(){
        if(this.response == null) return null;

        // 获取并进行提交
        HttpEntity entity = this.response.getEntity();
        String entityContent = null;
        try {
            entityContent = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return entityContent;
        }
    }


}
