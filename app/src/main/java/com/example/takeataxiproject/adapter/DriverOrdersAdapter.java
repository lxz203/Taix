package com.example.takeataxiproject.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.OrderBean;

import java.util.List;

public class DriverOrdersAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    public DriverOrdersAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.take_orders_btn);
    }



    @Override
    protected void convert(BaseViewHolder holder, OrderBean orderBean) {
        LogUtils.d("OrderBean=====", orderBean.toString());
       TextView start_location_tv =  holder.getView(R.id.start_location_tv);
        start_location_tv.setText(orderBean.getStartPointName()+"---->");
        holder.setText(R.id.end_location_tv,orderBean.getEndPointName());
        holder.setText(R.id.price_tv,orderBean.getPrice());
        if(!TextUtils.isEmpty(orderBean.getDriverAccountNumber())){
            holder.setText(R.id.take_orders_btn,"已接单");
        }
    }

}
