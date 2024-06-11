package com.example.takeataxiproject.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.litepal.MessageDetailBean;
import com.example.takeataxiproject.litepal.OrderBean;
import com.example.takeataxiproject.litepal.User;
import com.example.takeataxiproject.login.LoginActivity;
import com.example.takeataxiproject.util.ToastHelper;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable List<MessageBean> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.take_orders_btn);
    }



    @Override
    protected void convert(BaseViewHolder holder, MessageBean orderBean) {
        LogUtils.d("orderBeanGetMessageList====",orderBean.getMessageList());
        Type type = new TypeToken<List<MessageDetailBean>>(){}.getType();
        List<MessageDetailBean> list1  =  GsonUtils.fromJson(orderBean.getMessageList(),type);
        if(list1.size()>0){
            holder.setText(R.id.message_tv,list1.get(list1.size()-1).getMessage());
        }else{
            holder.setText(R.id.message_tv,"来了");
        }

        orderBean.getDriverAccountNumber();

        if(LoginActivity.isPassengers){
            holder.setText(R.id.car_name_tv,orderBean.getDriverName());
        }else{
            User user = LitePal
                    .where("account=? and isPassengers=?", orderBean.getPassengerAccountNumber(), 1 + "")
                    .findFirst(User.class);
            if (user != null) {
               String currName = user.getUserName();
                holder.setText(R.id.car_name_tv,currName);
            } else{
                holder.setText(R.id.car_name_tv,orderBean.getDriverName());
            }
        }

        holder.setText(R.id.message_time_tv,orderBean.getTime());
    }

}
