package com.example.takeataxiproject.login;

import static java.lang.Math.round;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.takeataxiproject.BaseActivity;
import com.example.takeataxiproject.MessageActivity;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.adapter.DriverOrdersAdapter;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.litepal.MessageDetailBean;
import com.example.takeataxiproject.litepal.OrderBean;
import com.example.takeataxiproject.litepal.User;
import com.example.takeataxiproject.map.MapSearchActivity;
import com.example.takeataxiproject.util.KeyboardsUtils;
import com.example.takeataxiproject.util.ToastHelper;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements LocationSource, AMapLocationListener {
    //初始化定位
    AMapLocationClient mLocationClient = null;
    MapView home_map;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    // 创建线路搜索对象
    RouteSearch mRouteSearch = null;
    //地图控制器
    private AMap aMap = null;
    String endLocation;
    //位置更改监听
    private OnLocationChangedListener mListener;
    LatLng selectLatLng, startLatLng;

    int price;
    TextView taxi_price_tv, expensive_taxi_price_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView menu_iv = findViewById(R.id.menu_iv);
        Button go_car_btn = findViewById(R.id.go_car_btn);
        ImageView message_iv = findViewById(R.id.message_iv);
        ConstraintLayout go_car_cl = findViewById(R.id.go_car_cl);
        TextView go_search_tv = findViewById(R.id.go_search_tv);
        ConstraintLayout takeATaxi_title_cl = findViewById(R.id.takeATaxi_title_cl);
        Button cancel_order_btn = findViewById(R.id.cancel_order_btn);
        Button taxi_end_btn = findViewById(R.id.taxi_end_btn);
        Button cancel_end_btn = findViewById(R.id.cancel_end_btn);
        RecyclerView driver_order_page_rv = findViewById(R.id.driver_order_page_rv);
        taxi_price_tv = findViewById(R.id.taxi_price_tv);
        expensive_taxi_price_tv = findViewById(R.id.expensive_taxi_price_tv);
        home_map = findViewById(R.id.home_map);
        home_map.onCreate(savedInstanceState);
        //初始化地图相关配置，参考高德文档
        initLocation();
        initMap();


        ConstraintLayout scheduled_success_prompt_cl = findViewById(R.id.scheduled_success_prompt_cl);
        ConstraintLayout scheduled_driver_success_prompt_cl = findViewById(R.id.scheduled_driver_success_prompt_cl);
        if (LoginActivity.isPassengers) {
            //乘客页面展示
            OrderBean currUser = LitePal
                    .where("passengerAccountNumber=? and orderStatus=?", LoginActivity.currPhone, "1")
                    .findFirst(OrderBean.class);
            OrderBean ordersReceived = LitePal
                    .where("passengerAccountNumber=? and orderStatus=?", LoginActivity.currPhone, "2")
                    .findFirst(OrderBean.class);
            //默认进来的状态
            if (currUser != null) {
                if (currUser.getOrderStatus() == 1) {
                    go_car_cl.setVisibility(View.GONE);
                    scheduled_success_prompt_cl.setVisibility(View.VISIBLE);
                    scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
                } else if (currUser.getOrderStatus() == 2) {
                    go_car_cl.setVisibility(View.GONE);
                    scheduled_success_prompt_cl.setVisibility(View.GONE);
                    scheduled_driver_success_prompt_cl.setVisibility(View.VISIBLE);
                } else {
                    go_car_cl.setVisibility(View.VISIBLE);
                    scheduled_success_prompt_cl.setVisibility(View.GONE);
                    scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
                }
            } else if (ordersReceived != null) {
                //已有订单被接了
                go_car_cl.setVisibility(View.GONE);
                scheduled_success_prompt_cl.setVisibility(View.GONE);
                scheduled_driver_success_prompt_cl.setVisibility(View.VISIBLE);
                aMap.clear();
//                116.382589,39.988908
                //116.381207,39.994262
                LatLng car1 = new LatLng(39.988908, 116.382589);
                startLatLng = car1;
                LatLng carEnd = new LatLng(39.994262, 116.382543);
                selectLatLng = carEnd;
                navigation(getBaseContext(), startLatLng.latitude, startLatLng.longitude, selectLatLng.latitude,
                        selectLatLng.longitude);
                taxi_end_btn.setOnClickListener(view -> {
                    //打车流程结束
                    aMap.clear();
                    initMap();
                    ordersReceived.setOrderStatus(4);
                    //司机账户
                    User user = LitePal
                            .where("account=? and isPassengers=?", ordersReceived.getDriverAccountNumber(),  0 + "")
                            .findFirst(User.class);

                    user.setMoney(user.getMoney() + Integer.parseInt(ordersReceived.getPrice()));
                    user.save();
                    ordersReceived.save();
                    go_car_cl.setVisibility(View.VISIBLE);
                    scheduled_success_prompt_cl.setVisibility(View.GONE);
                    scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "打车完成，欢迎下次使用", Toast.LENGTH_LONG).show();


                });
                cancel_end_btn.setOnClickListener(view -> {
                    //打车流程结束
                    aMap.clear();
                    initMap();
                    ordersReceived.setOrderStatus(3);

                    ordersReceived.save();
                    go_car_cl.setVisibility(View.VISIBLE);
                    scheduled_success_prompt_cl.setVisibility(View.GONE);
                    scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "取消订单", Toast.LENGTH_LONG).show();

                });


            } else {
                go_car_cl.setVisibility(View.VISIBLE);
                scheduled_success_prompt_cl.setVisibility(View.GONE);
                scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
            }
            ConstraintLayout economy_car_cl = findViewById(R.id.economy_car_cl);
            ConstraintLayout expanded_car_cl = findViewById(R.id.expanded_car_cl);
            economy_car_cl.setOnClickListener(view -> {
                economy_car_cl.setBackground(getDrawable(R.drawable.blue_border));
                expanded_car_cl.setBackground(getDrawable(R.drawable.white_border));
            });
            expanded_car_cl.setOnClickListener(view -> {
                economy_car_cl.setBackground(getDrawable(R.drawable.white_border));
                expanded_car_cl.setBackground(getDrawable(R.drawable.blue_border));


            });
        } else {
            driver_order_page_rv.setVisibility(View.VISIBLE);
            //司机页面展示
            go_car_cl.setVisibility(View.GONE);
            scheduled_success_prompt_cl.setVisibility(View.GONE);
            scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
            takeATaxi_title_cl.setVisibility(View.GONE);
            driver_order_page_rv.setLayoutManager(new LinearLayoutManager(this));
            List<OrderBean> driverHasReceivedTheOrder = LitePal
                    .where("orderStatus=? and driverAccountNumber=?", "2", LoginActivity.currPhone)
                    .find(OrderBean.class);

            List<OrderBean> orderBeanList = LitePal
                    .where("orderStatus=?", "1")
                    .find(OrderBean.class);
            DriverOrdersAdapter driverOrderAdapter;

            if (driverHasReceivedTheOrder != null && driverHasReceivedTheOrder.size() > 0) {
                driverOrderAdapter = new DriverOrdersAdapter(R.layout.item_driver_order,
                        driverHasReceivedTheOrder);
                aMap.clear();
                LatLng car1 = new LatLng(39.988908, 116.382589);
                startLatLng = car1;
                LatLng carEnd = new LatLng(39.994262, 116.382543);
                selectLatLng = carEnd;
                navigation(getBaseContext(), startLatLng.latitude, startLatLng.longitude, selectLatLng.latitude,
                        selectLatLng.longitude);


                Toast.makeText(getBaseContext(), "已接单，请尽快去接乘客", Toast.LENGTH_LONG).show();
            } else {
                driverOrderAdapter = new DriverOrdersAdapter(R.layout.item_driver_order,
                        orderBeanList);
            }

            driverOrderAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    if (view.getId() == R.id.take_orders_btn) {
                        LogUtils.d("接单=========");
                        OrderBean user = LitePal
                                .where("passengerAccountNumber=? and orderStatus=?",
                                        orderBeanList.get(position).getPassengerAccountNumber(), "1")
                                .findFirst(OrderBean.class);
                        user.setOrderStatus(2);
                        user.setDriverAccountNumber(LoginActivity.currPhone);
                        user.save();
                        aMap.clear();
                        LatLng car1 = new LatLng(39.988908, 116.382589);
                        startLatLng = car1;
                        LatLng carEnd = new LatLng(39.994262, 116.382543);
                        selectLatLng = carEnd;
                        navigation(getBaseContext(), startLatLng.latitude, startLatLng.longitude, selectLatLng.latitude,
                                selectLatLng.longitude);
                        driver_order_page_rv.setVisibility(View.GONE);

                        MessageBean messageBean = new MessageBean();
                        messageBean.setPassengerAccountNumber(user.getPassengerAccountNumber());
                        messageBean.setDriverAccountNumber(user.getDriverAccountNumber());
                        messageBean.setDriverName(LoginActivity.currName);
                        List<MessageDetailBean>list = new ArrayList<>();
                        list.add(new MessageDetailBean(1,2, "我马上来接您"));
                        list.add(new MessageDetailBean(1,1, "好的"));
                        String messageJson = GsonUtils.toJson(list);
                        messageBean.setMessageList(messageJson);
                        messageBean.setMessageStatus(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String currentTime = sdf.format(new Date());
                        messageBean.setTime(currentTime);
                        messageBean.save();
                        Toast.makeText(MainActivity.this, "已成功接单，请尽快去接乘客", Toast.LENGTH_LONG).show();
                    }
                }
            });

            driver_order_page_rv.setAdapter(driverOrderAdapter);
        }


        menu_iv.setOnClickListener(view -> {
            startActivity(new Intent(this, MyActivity.class));
        });
        message_iv.setOnClickListener(view -> {
            startActivity(new Intent(this, MessageActivity.class));
        });
        go_car_btn.setOnClickListener(view -> {
            OrderBean user = LitePal
                    .where("passengerAccountNumber=? and orderStatus=?", LoginActivity.currPhone, "1")
                    .findFirst(OrderBean.class);


            if (user != null) {
                ToastHelper.showShort("当前已有未完成的订单，请先取消订单");
            } else {
                OrderBean order = new OrderBean();
                order.setPassengerAccountNumber(LoginActivity.currPhone);
                order.setDriverOrder(false);
                order.setPrice(price + "");
                order.setStartPointName("北京西");
                order.setEndPointName(endLocation);
                order.setOrderStatus(1);
                order.save();
                ToastHelper.showShort("订单已生成，请等待司机接单");
                go_car_cl.setVisibility(View.GONE);
                scheduled_success_prompt_cl.setVisibility(View.VISIBLE);
                scheduled_driver_success_prompt_cl.setVisibility(View.GONE);
            }

        });
        go_search_tv.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, MapSearchActivity.class), 1);
        });

        cancel_order_btn.setOnClickListener(view -> {
            OrderBean order = LitePal
                    .where("passengerAccountNumber=? and orderStatus=?", LoginActivity.currPhone, "1")
                    .findFirst(OrderBean.class);
            if (order != null) {
                order.setPassengerAccountNumber(LoginActivity.currPhone);
                order.setDriverOrder(false);
                order.setPrice(price + "");
                order.setOrderStatus(3);
                order.save();
                ToastHelper.showShort("订单取消成功");
                go_car_cl.setVisibility(View.VISIBLE);
                scheduled_success_prompt_cl.setVisibility(View.GONE);
                scheduled_driver_success_prompt_cl.setVisibility(View.GONE);

            }

        });

    }

    /**
     * 初始化地图
     *
     * @param
     */
    private void initMap() {
        aMap = home_map.getMap();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
//        aMap.moveCamera(newLatLngZoom(new LatLng(200, 200), 16));

        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        startLatLng = new LatLng(39.988908, 116.382543);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(startLatLng, 18, 30, 0));
        aMap.addMarker(new MarkerOptions().position(startLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.starting_point_pic)));
        aMap.moveCamera(mCameraUpdate);
        //司机坐标经纬度
        LatLng car1 = new LatLng(39.994262, 116.382589);
        LatLng car2 = new LatLng(29.046083, 111.656163);
        LatLng car3 = new LatLng(29.041609, 111.68119);
        LatLng car4 = new LatLng(29.046609, 111.680988);
        LatLng car5 = new LatLng(29.054645, 111.672645);
        LatLng car6 = new LatLng(29.048645, 111.675632);
        LatLng car7 = new LatLng(29.051909, 111.667488);
        LatLng car8 = new LatLng(29.046158, 111.67204);


        //司机添加地图标记图标，可以看见司机小车位置，当前是随便写的地址，如需展示到固定位置，需要调整经纬度
        aMap.addMarker(new MarkerOptions().position(car1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car4).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car5).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car6).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car7).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));
        aMap.addMarker(new MarkerOptions().position(car8).icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pic)));

    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //此行代码打开打开表示获取当前手机定位
//            mLocationClient.startLocation();


        }
    }
    //点击任意位置关闭键盘输入法弹框
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (KeyboardsUtils.isShouldHideKeyBord(view, ev)) {
                KeyboardsUtils.hintKeyBoards(view);
            }
        }
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient != null) {
//            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getAddress();
                if (mListener != null) {
                    //定位后获取初始位置经纬度
                    mListener.onLocationChanged(aMapLocation);
                    startLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtils.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        home_map.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
        home_map.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if(data!=null){
                //通过输入框输入的地址，回调拿到要去的地方经纬度，然后计算距离
                Double latitude = data.getDoubleExtra("latitude", 0.0f);
                Double longitude = data.getDoubleExtra("longitude", 0.0);
                selectLatLng = new LatLng(latitude, longitude);
                LatLng endLatLng = new LatLng(selectLatLng.latitude, selectLatLng.longitude);
                int distance = calculatedDistanceM(startLatLng, endLatLng);

                new MaterialDialog.Builder(MainActivity.this)
                        .title("当前距离")
                        .content("当前距离"+distance+"米")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .onPositive((dialog, which) -> {
                            endLocation = data.getStringExtra("endLocation");
                            LogUtils.d("onActivityResult=====", latitude, longitude);
                            selectLatLng = new LatLng(latitude, longitude);
                            aMap.clear();
                            //规划地图路线
                            navigation(this, startLatLng.latitude, startLatLng.longitude, selectLatLng.latitude,
                                    selectLatLng.longitude);
                        })
                        .show();



            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 路线规划
     *
     * @param slat 起点纬度
     * @param slon 起点经度
     * @param dlat 终点纬度
     * @param dlon 终点经度
     */
    public void navigation(Context context, double slat, double slon, double dlat, double dlon) {

        LatLng endLatLng = new LatLng(dlat, dlon);

        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(startLatLng);
        latLngs.add(endLatLng);
        try {
            // 对象进行初始化
            mRouteSearch = new RouteSearch(this);
            // 搜索监听
            mRouteSearch.setRouteSearchListener(mRouteSearchListener);

        } catch (Exception e) {

        }

        // 清除界面上之前绘制的线路
        clearAllLineMarker();
        // 所有线路需要将开始的线路经纬度放到这个集合中
        for (int j = 0; j < latLngs.size() - 1; j++) { // 遍历当前集合进行搜索线路
            // 传入当前线路的起点和终点坐标，然后进行绘制
            searchRouteResult(new LatLonPoint(latLngs.get(j).latitude, latLngs.get(j).longitude), new LatLonPoint(latLngs.get(j + 1).latitude, latLngs.get(j + 1).longitude));
        }
        //直线距离
        int distance = calculatedDistance(startLatLng, endLatLng);




        //根据距离动态设置车辆价钱
        price = distance * 20 + 10;
        taxi_price_tv.setText("价格：" + price);
        expensive_taxi_price_tv.setText("价格：" + price * 1.5);
    }

    private void SurfacePolyDaysLine() {

        // update map zoom
//        setMapBounds(list);
    }

    public void clearAllLineMarker() {

    }

    /**
     * start search line
     */
    public void searchRouteResult(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        // 传入起点坐标、终点坐标
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        // DriveRouteQuery 类型是驾车的路线搜索  DRIVING_SINGLE_SHORTEST 类型表示返回单个路线最短的数据集
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        // 开始异步搜索处理
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    /**
     * search line listener
     */
    RouteSearch.OnRouteSearchListener mRouteSearchListener = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            // bus
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            // driving
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        DriveRouteResult mDriveRouteResult = result;
                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                        List<DriveStep> driveSteps = mDriveRouteResult.getPaths().get(0).getSteps();
                        if (drivePath == null) { // 表示返回的线路是空的情况，就不管
                            return;
                        }
                        List<LatLng> latLngs = new ArrayList();
                        //通过遍历，将DriveStep 里面的集合坐标点放入latLngs 内
                        for (DriveStep driveStep : driveSteps) {
                            List<LatLonPoint> latLonPoints = driveStep.getPolyline();
                            for (LatLonPoint latLonPoint : latLonPoints) {
                                LatLng latLng1 = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                                latLngs.add(latLng1);
                            }
                        }
                        //判断集合不为空，进行路线的绘制
                        if (latLngs.size() > 0) {
                            aMap.addPolyline(new PolylineOptions().
                                    addAll(latLngs).width(10).color(Color.argb(255, 71, 204, 160)));
                        }
                        //绘制起始图片
                        if (latLngs.size() > 1) {
                            aMap.addMarker(new MarkerOptions().position(latLngs.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.starting_point_pic)));
                            aMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_coordinate_pic)));
                        }

                    } else if (result != null && result.getPaths() == null) {
                    }
                } else {
                }
            } else {
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
            // walk
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            // ride
        }
    };
    //计算距离，千米单位
    public int calculatedDistance(LatLng latLng2, LatLng latLng) {
        float distance = AMapUtils.calculateLineDistance(latLng, latLng2);
        int qianmifload = (int) distance / 1000;
        return qianmifload;
    }
    //计算距离，米单位
    public int calculatedDistanceM(LatLng latLng2, LatLng latLng) {
        float distance = AMapUtils.calculateLineDistance(latLng, latLng2);
        return (int) distance;
    }
}
