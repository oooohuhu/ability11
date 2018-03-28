package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.ustcinfo.mobile.platform.ability.jsbridge.BridgeWebView;
import com.ustcinfo.mobile.platform.ability.jsbridge.JsMethodAdapter;
import com.ustcinfo.mobile.platform.ability.qrcode.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements INaviInfoCallback, View.OnClickListener {
    private BridgeWebView webView;
    private Button go;
    private TextView startpoint;
    private TextView endpoint;
    private Button routesearchbutton;
    private LatLonPoint startLatpoint;
    private LatLonPoint endLatpoint;
    private Double startlat;
    private Double startlong;
    private Double endlat;
    private Double endlong;
    private Button signame;
    private static final int STARTPOINT = 0x201;
    private static final int ENDPOINT = 0x202;
private  static final int CAMERA_OK=0x301;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        webView = (BridgeWebView) findViewById(com.ustcinfo.mobile.platform.ability.R.id.web_view);
        webView.loadUrl("file:///android_asset/demo/index.html");
        JsMethodAdapter.register(webView);

    }

    private void initView() {
        startpoint = (TextView) findViewById(R.id.startpoint);
        endpoint = (TextView) findViewById(R.id.endpoint);
        go = (Button) findViewById(R.id.go);
        signame = (Button) findViewById(R.id.signame);
        startpoint.setOnClickListener(this);
        endpoint.setOnClickListener(this);
        go.setOnClickListener(this);
        signame.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case STARTPOINT:
                    Tip tip1 = data.getExtras().getParcelable("Tip");
                    if (tip1 == null) {
                        String address = data.getExtras().getString("address");
                        Double latitude = data.getExtras().getDouble("latitude");
                        Double longitude = data.getExtras().getDouble("longitude");
                        startpoint.setText(address);
                        startlat = latitude;
                        startlong = longitude;
                    } else {
                        startpoint.setText(tip1.getName());
                        startLatpoint = tip1.getPoint();
                        startlat = startLatpoint.getLatitude();
                        startlong = startLatpoint.getLongitude();
                    }
                    break;
                case ENDPOINT:
                    Tip tip2 = data.getExtras().getParcelable("Tip");
                    if (tip2 == null) {
                        String address = data.getExtras().getString("address");
                        Double latitude = data.getExtras().getDouble("latitude");
                        Double longitude = data.getExtras().getDouble("longitude");
                        endpoint.setText(address);
                        endlat = latitude;
                        endlong = longitude;
                    } else {
                        endpoint.setText(tip2.getName());
                        endLatpoint = tip2.getPoint();
                        endlat = endLatpoint.getLatitude();
                        endlong = endLatpoint.getLongitude();
                    }

                    break;
            }
        }
        JsMethodAdapter.getmInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JsMethodAdapter.getmInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
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
    public void onDestroy() {
        super.onDestroy();
        JsMethodAdapter.unRegister();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startpoint:
                Intent intent = new Intent(MainActivity.this, PoiSearchActivity.class);
                startActivityForResult(intent, STARTPOINT);
                break;
            case R.id.endpoint:
                Intent intent1 = new Intent(MainActivity.this, PoiSearchActivity.class);
                startActivityForResult(intent1, ENDPOINT);
                break;
            case R.id.go:
                String startsrc = startpoint.getText().toString().trim();
                if (startsrc == null || startsrc.length() == 0) {
                    Toast.makeText(this, "起始点不可为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String endstr = endpoint.getText().toString().trim();
                if (endstr == null || endstr.length() == 0) {
                    Toast.makeText(this, "终点点不可为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(MainActivity.this, GMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("startlatitude", startlat);
                bundle.putDouble("startlongitude", startlong);
                bundle.putDouble("endlatitude", endlat);
                bundle.putDouble("endlongitude", endlong);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.signame:
                Intent intent3 = new Intent(MainActivity.this, SignNameActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
