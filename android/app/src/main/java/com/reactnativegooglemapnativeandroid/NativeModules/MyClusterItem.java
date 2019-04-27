package com.reactnativegooglemapnativeandroid.NativeModules;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyClusterItem implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private String population;
    public String tag;

    public MyClusterItem(String g, LatLng pos, String t, String s, String p){
        tag = g;
        position =pos;
        title = t;
        snippet = s + "\n" + p;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getTag(){
        return tag;
    }
}
