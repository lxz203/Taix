package com.example.takeataxiproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.takeataxiproject.adapter.MessageAdapter;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.litepal.OrderBean;
import com.example.takeataxiproject.login.LoginActivity;

import org.litepal.LitePal;

import java.util.List;
//消息页面
public class MessageActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView message_rv = findViewById(R.id.message_rv);
        message_rv.setLayoutManager(new LinearLayoutManager(this));
        List<MessageBean> messageBeanList ;
        //查询当前用户的消息，然后展示
        if(LoginActivity.isPassengers){
            messageBeanList =LitePal
                    .where("passengerAccountNumber=?",
                            LoginActivity.currPhone)
                    .find(MessageBean.class);
        }else{
            messageBeanList =LitePal
                    .where("driverAccountNumber=?",
                            LoginActivity.currPhone)
                    .find(MessageBean.class);
        }
        LogUtils.d("messageBeanList====",messageBeanList.size());
        MessageAdapter messageAdapter = new MessageAdapter(R.layout.item_message,messageBeanList);
        message_rv.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, MessageDetailActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        });
    }
}
