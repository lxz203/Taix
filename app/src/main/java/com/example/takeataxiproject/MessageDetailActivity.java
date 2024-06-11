package com.example.takeataxiproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.example.takeataxiproject.adapter.MessageDetailAdapter;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.litepal.MessageDetailBean;
import com.example.takeataxiproject.login.LoginActivity;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//消息聊天页面
public class MessageDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Button send_message_btn = findViewById(R.id.send_message_btn);
        EditText input_message_et = findViewById(R.id.input_message_et);
        List<MessageBean> messageBeanList;
        //根据当前账号，查询所有消息对话
        if (LoginActivity.isPassengers) {
            messageBeanList = LitePal
                    .where("passengerAccountNumber=?",
                            LoginActivity.currPhone)
                    .find(MessageBean.class);
        } else {
            messageBeanList = LitePal
                    .where("driverAccountNumber=?",
                            LoginActivity.currPhone)
                    .find(MessageBean.class);
        }
        // 获取传递过来的Bundle对象
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position", -1); // -1为默认值，如果没有获取到isPassengers
        String messageList = messageBeanList.get(position).getMessageList();
        Type type = new TypeToken<List<MessageDetailBean>>(){}.getType();
        List<MessageDetailBean> list1  =  GsonUtils.fromJson(messageList,type);
        MessageDetailAdapter messageDetailAdapter = new MessageDetailAdapter(R.layout.item_message_detail,
                list1);

        send_message_btn.setOnClickListener(view -> {
            String inputMessage = input_message_et.getText().toString();
            //发送消息
            if (TextUtils.isEmpty(inputMessage)) {
                Toast.makeText(this, "请输入需要发送的消息", Toast.LENGTH_LONG).show();
            } else {
                if (LoginActivity.isPassengers) {
                    list1.add(new MessageDetailBean(1, 1, inputMessage));
                } else {
                    list1.add(new MessageDetailBean(1, 2, inputMessage));
                }
                MessageBean messageBean = messageBeanList.get(position);
                messageBean.setMessageList(GsonUtils.toJson(list1));
                messageBean.save();
                input_message_et.setText("");
                messageDetailAdapter.notifyDataSetChanged();
            }
        });
        RecyclerView message_item_detail_rv = findViewById(R.id.message_item_detail_rv);
        message_item_detail_rv.setLayoutManager(new LinearLayoutManager(this));
        message_item_detail_rv.setAdapter(messageDetailAdapter);

    }
}
