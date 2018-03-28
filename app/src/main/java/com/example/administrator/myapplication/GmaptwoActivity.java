package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.ustcinfo.mobile.platform.ability.jsbridge.BridgeWebView;
import com.ustcinfo.mobile.platform.ability.jsbridge.JsMethodAdapter;

/**
 * Created by Administrator on 2018\3\25 0025.
 */

public class GmaptwoActivity extends AppCompatActivity implements INaviInfoCallback {

    private Double startlatitude;
    private Double startlongitude;
    private Double endlatitude;
    private Double endlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            startlatitude = bundle.getDouble("startlatitude");
            startlongitude = bundle.getDouble("startlongitude");
            endlatitude = bundle.getDouble("endlatitude");
            endlongitude = bundle.getDouble("endlongitude");
        }
        LatLng p4 = new LatLng(endlatitude, endlongitude);
        LatLng p5 = new LatLng(startlatitude, startlongitude);
        Poi start = new Poi("合肥", p5, "");//起点
        Poi end = new Poi("北京", p4, "");//终点
        AmapNaviParams amapNaviParams = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), amapNaviParams, GmaptwoActivity.this);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }
}
