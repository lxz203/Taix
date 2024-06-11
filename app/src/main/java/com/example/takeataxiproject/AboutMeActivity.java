package com.example.takeataxiproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takeataxiproject.adapter.MessageAdapter;
import com.example.takeataxiproject.litepal.MessageBean;
import com.example.takeataxiproject.login.LoginActivity;

import org.litepal.LitePal;

import java.util.List;

public class AboutMeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}
