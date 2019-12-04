package com.wozipa.reptile.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Configuration {

    private static volatile Configuration INSTANCE = null;

    public static Configuration GetInstance(){
        if(INSTANCE == null) {
            synchronized (Configuration.class) {
                if(INSTANCE == null)
                    INSTANCE = new Configuration();
            }
        }
        return INSTANCE;
    }

    private Configuration(){

    }

    private Config config = ConfigFactory.load("reptiles.conf");

    public String get(String key){
        return config.getString(key);
    }
}
