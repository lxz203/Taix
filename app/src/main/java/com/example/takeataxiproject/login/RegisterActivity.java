package com.example.takeataxiproject.login;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.takeataxiproject.BaseActivity;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.User;

import org.litepal.LitePal;

//注册页面
public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        Button submit_registration_btn = findViewById(R.id.submit_registration_btn);
        EditText account_et = findViewById(R.id.account_et);
        EditText passowrd_et = findViewById(R.id.passowrd_et);
        EditText passowrd2_et = findViewById(R.id.passowrd2_et);
        EditText user_name_cl_et = findViewById(R.id.user_name_cl_et);
        TextView title_tv = findViewById(R.id.title_tv);
        ImageView message_iv = findViewById(R.id.message_iv);
        ImageView menu_iv = findViewById(R.id.menu_iv);
        message_iv.setVisibility(View.GONE);
        menu_iv.setVisibility(View.GONE);
        //通过传递过来的是乘客还是司机，注册不同端的账号
        int isPassengers;
        // 获取传递过来的Bundle对象
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // 通过键名获取参数值
            isPassengers = extras.getInt("isPassengers", -1); // -1为默认值，如果没有获取到isPassengers
            // 根据isPassengers的值进行后续操作
            if(isPassengers==1){
                title_tv.setText("乘客注册");
            }else{
                title_tv.setText("司机注册");
            }
        } else {
            isPassengers = -1;
        }


        submit_registration_btn.setOnClickListener(view -> {
            String phone = account_et.getText().toString();
            String passowrd = passowrd_et.getText().toString();
            String passowrd2 = passowrd2_et.getText().toString();
            String userNameStr = user_name_cl_et.getText().toString();
            if (passowrd.equals(passowrd2)) {
                //输入的是一致
                //根据指定条件查询
                User user = LitePal
                        .where("account=?", phone)
                        .findFirst(User.class);
                //当前不存在此账号，可注册
                if (user == null) {
                    //可以注册
                    User user1 = new User();
                    user1.setAccount(phone);
                    user1.setPassoword(passowrd);
                    user1.setUserName(userNameStr);
                    if (isPassengers == 1) {
                        user1.setPassengers(true);
                    } else {
                        user1.setPassengers(false);
                    }
                    user1.save();
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "当前用户已经注册过", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
