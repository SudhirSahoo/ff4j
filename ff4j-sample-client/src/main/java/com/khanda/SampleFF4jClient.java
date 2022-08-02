package com.khanda;

import java.util.Map;

import org.ff4j.FF4j;
import org.ff4j.cache.FF4jCacheProxy;
import org.ff4j.cache.InMemoryCacheManager;
import org.ff4j.core.Feature;
import org.ff4j.web.jersey2.store.FeatureStoreHttp;
import org.ff4j.web.jersey2.store.PropertyStoreHttp;


public class SampleFF4jClient {

	private static FF4j ff4j;

	public static void main(String[] args) {

        String ff4jApiEndPoint = "http://localhost:8085/api/ff4j";
        FeatureStoreHttp  fStore = new FeatureStoreHttp(ff4jApiEndPoint);
        PropertyStoreHttp pStore = new PropertyStoreHttp(ff4jApiEndPoint);
        /* 
         * Maybe we don't want to do an http call each time we test a feature
         * as there is latency. Local in memory cache with TTL 10min can help.
         */
        FF4jCacheProxy cc = new FF4jCacheProxy(fStore, pStore, new InMemoryCacheManager(600));

        ff4j = new FF4j();
        ff4j.setFeatureStore(cc);
        ff4j.setPropertiesStore(cc);
        ff4j.audit(false); //(auditing at server level)
        ff4j.autoCreate(true);

        System.out.println("-----------------------------");
        System.out.println("--     FF4J FEATURES       --");
        System.out.println("-----------------------------");
        Map<String, Feature> allFeatures = ff4j.getFeatureStore().readAll();
        for(Feature f : allFeatures.values()) {
            System.out.println("- Feature " + f.getUid() + "=>" + f.toJson());
        }
        
        if (ff4j.check("showUserName")) {
            System.out.println("'showUserName' is ON");
        } else {
            System.out.println("'showUserName' is OFF");
        }
        
        Object s = ff4j.getProperty("username").getValue();
        System.out.println(s);

	}

}

