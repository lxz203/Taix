package com.example.takeataxiproject.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.litepal.MessageDetailBean;
import com.example.takeataxiproject.login.LoginActivity;

import java.util.List;

public class MessageDetailAdapter extends BaseQuickAdapter<MessageDetailBean, BaseViewHolder> {
    public MessageDetailAdapter(int layoutResId, @Nullable List<MessageDetailBean> data) {
        super(layoutResId, data);

    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder holder, MessageDetailBean orderBean) {
        if(LoginActivity.isPassengers){
            if (orderBean.getIspassenger() == 1) {
                holder.setText(R.id.message_tv, orderBean.getMessage());
                LinearLayout first_ll = holder.getView(R.id.first_ll);
                first_ll.setBackgroundResource(R.drawable.blue_shape_10);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) first_ll.getLayoutParams();
                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                first_ll.setLayoutParams(layoutParams);
                TextView message_tv = holder.getView(R.id.message_tv);
                message_tv.setTextColor(R.color.white);
            } else {
                holder.setText(R.id.message_tv, orderBean.getMessage());
                LinearLayout first_ll = holder.getView(R.id.first_ll);
                first_ll.setBackgroundResource(R.drawable.white_shape_10);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) first_ll.getLayoutParams();
                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                layoutParams.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                first_ll.setLayoutParams(layoutParams);
                TextView message_tv = holder.getView(R.id.message_tv);
                message_tv.setTextColor(R.color.black);
            }
        }else{
            if (orderBean.getIspassenger() == 1) {
                holder.setText(R.id.message_tv, orderBean.getMessage());
                LinearLayout first_ll = holder.getView(R.id.first_ll);
                first_ll.setBackgroundResource(R.drawable.white_shape_10);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) first_ll.getLayoutParams();
                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                layoutParams.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                first_ll.setLayoutParams(layoutParams);
                TextView message_tv = holder.getView(R.id.message_tv);
                message_tv.setTextColor(R.color.black);
            } else {
                holder.setText(R.id.message_tv, orderBean.getMessage());
                LinearLayout first_ll = holder.getView(R.id.first_ll);
                first_ll.setBackgroundResource(R.drawable.blue_shape_10);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) first_ll.getLayoutParams();
                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                first_ll.setLayoutParams(layoutParams);
                TextView message_tv = holder.getView(R.id.message_tv);
                message_tv.setTextColor(R.color.white);
            }
        }


    }

}
