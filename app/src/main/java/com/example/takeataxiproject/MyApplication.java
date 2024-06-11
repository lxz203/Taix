package com.example.takeataxiproject;

import static com.amap.api.location.AMapLocationClient.updatePrivacyAgree;
import static com.amap.api.location.AMapLocationClient.updatePrivacyShow;
import static com.amap.api.maps2d.MapsInitializer.setApiKey;

import android.app.Application;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.core.ServiceSettings;

import org.litepal.LitePal;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//定位隐私政策同意
        AMapLocationClient.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        //搜索隐私政策同意
        ServiceSettings.updatePrivacyShow(this,true,true);
        ServiceSettings.updatePrivacyAgree(this,true);

        LitePal.initialize(this);
    }
}
