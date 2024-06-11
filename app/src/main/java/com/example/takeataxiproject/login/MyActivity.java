package com.example.takeataxiproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.takeataxiproject.AboutMeActivity;
import com.example.takeataxiproject.BaseActivity;
import com.example.takeataxiproject.MessageActivity;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.User;
import com.example.takeataxiproject.util.AppManager;
import com.example.takeataxiproject.util.ToastHelper;

import org.litepal.LitePal;
//个人信息页面
public class MyActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        TextView me_name_tv = findViewById(R.id.me_name_tv);
        ConstraintLayout view_messages_cl = findViewById(R.id.view_messages_cl);
        ConstraintLayout about_us_cl = findViewById(R.id.about_us_cl);
        ConstraintLayout exit_login_cl = findViewById(R.id.exit_login_cl);
        ConstraintLayout write_off_cl = findViewById(R.id.write_off_cl);
        me_name_tv.setText("HI," + LoginActivity.currName);

        view_messages_cl.setOnClickListener(view -> {
            startActivity(new Intent(this, MessageActivity.class));
        });
        about_us_cl.setOnClickListener(view -> {
            //关于我们
            startActivity(new Intent(this, AboutMeActivity.class));
        });
        exit_login_cl.setOnClickListener(view -> {
            //退出登录，返回登录页面
            AppManager.getAppManager().killAll();
            startActivity(new Intent(this, LoginActivity.class));

        });
        write_off_cl.setOnClickListener(view -> {
            if (LoginActivity.isPassengers) {
                //删除账号信息
                User user = LitePal
                        .where("account=?and isPassengers=?", LoginActivity.currPhone, 1 + "")
                        .findFirst(User.class);
                user.delete();
            } else {
                //删除账号信息
                User user = LitePal
                        .where("account=?and isPassengers=?", LoginActivity.currPhone, 0 + "")
                        .findFirst(User.class);
                user.delete();
            }
            ToastHelper.showShort("注销成功");
            AppManager.getAppManager().killAll();
            //关于我们
            startActivity(new Intent(this, LoginActivity.class));

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView total_amount_of_account_tv = findViewById(R.id.total_amount_of_account_tv);
        ConstraintLayout wallet_cl = findViewById(R.id.wallet_cl);

        if (!LoginActivity.isPassengers) {
            //查询当前账户多少钱
            wallet_cl.setVisibility(View.VISIBLE);
            User user = LitePal
                    .where("account=? and isPassengers=?", LoginActivity.currPhone, 0 + "")
                    .findFirst(User.class);
            total_amount_of_account_tv.setText(user.getMoney() + "元");
        } else {
            wallet_cl.setVisibility(View.GONE);
        }


    }
}
