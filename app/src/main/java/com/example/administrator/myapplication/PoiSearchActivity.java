package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\3\24 0024.
 */

public class PoiSearchActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener, View.OnClickListener {
    private CommonAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Tip> tips = new ArrayList<>();
    private String city = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            // TODO Auto-generated method stub
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double locationType = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    //获取纬度
                    latitude = amapLocation.getLatitude();
                    //获取经度
                    longitude = amapLocation.getLongitude();
                    address = amapLocation.getPoiName();
                    Log.e("Amap==经度：纬度", "locationType:" + locationType + ",latitude:" + latitude);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    private AMapLocationClientOption mLocationOption;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);


        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        /**
         * 获取一次定位
         */
        //该方法默认为false，true表示只定位一次
        mLocationOption.setOnceLocation(true);

        EditText mKeywordText = (EditText) findViewById(R.id.input_edittext);
        TextView mylocation = (TextView) findViewById(R.id.mylocation);
        mylocation.setOnClickListener(this);
        mKeywordText.addTextChangedListener(this);
        findViewById(R.id.rl_tv_map_pick).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.ll_rv_inputlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private CommonAdapter getAdapter() {
        return new CommonAdapter<Tip>(this, R.layout.item_layout, tips) {

            @Override
            protected void convert(ViewHolder holder, final Tip tip, int position) {

                holder.setText(R.id.poi_field_id, tip.getName());
                holder.setText(R.id.poi_value_id, tip.getDistrict());
                holder.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Tip", tip);
                        intent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            }
        };
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, "");
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mylocation:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                bundle.putString("address", address);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (tips.size() > 0)
                tips.clear();
            for (Tip tip : list) {
                if (tip.getPoint() != null) {
                    tips.add(tip);
                }
            }
//            tips.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
