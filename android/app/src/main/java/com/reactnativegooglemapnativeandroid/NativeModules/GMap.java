package com.reactnativegooglemapnativeandroid.NativeModules;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class GMap extends SimpleViewManager<MapView> implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ClusterManager mClusterManager;
    private ThemedReactContext context;

    // Send event to JavaScript so that we can add listeners
    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Nonnull
    @Override
    public String getName() {
        return "GMap";
    }

    @Nonnull
    @Override
    protected MapView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        context = reactContext;
        MapView view = new MapView(reactContext);
        view.onCreate(null);
        view.onResume();
        view.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        googleMap = gmap;
        mClusterManager = new ClusterManager<MyClusterItem>(context, googleMap);
        sendEvent(context, "onMapReady", null);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener() {
            @Override
            public boolean onClusterClick(Cluster cluster) {
                LatLng clusterPos = new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude);
                float newZoom = googleMap.getCameraPosition().zoom + 2;
                if (newZoom > googleMap.getMaxZoomLevel())
                    newZoom = googleMap.getMaxZoomLevel();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clusterPos, newZoom));
                return true;
            }
        });
    }

    public void addMarker(@Nullable final ReadableArray args) {
        MyClusterItem ci = new MyClusterItem(
                args.getString(0),
                new LatLng(args.getDouble(1),
                        args.getDouble(2)),
                args.getString(3),
                args.getString(4),
                args.getString(5)
                );
        mClusterManager.addItem(ci);
        mClusterManager.cluster();
    }

    // Receives commands from JavaScript using UIManager.dispatchViewManagerCommand
    @Override
    public void receiveCommand(MapView view, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(view, commandId, args);
        switch (commandId) {
            case 0:
                addMarker(args);
                break;
        }

    }
}
