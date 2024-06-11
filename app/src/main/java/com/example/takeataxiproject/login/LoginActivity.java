package com.example.takeataxiproject.login;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.takeataxiproject.BaseActivity;
import com.example.takeataxiproject.R;
import com.example.takeataxiproject.litepal.User;
import com.example.takeataxiproject.util.ToastHelper;
import com.example.takeataxiproject.util.view.ClearEditText;
import com.permissionx.guolindev.PermissionX;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化布局
        setupViews();
    }

    private ClearEditText etPhone;
    //当前登录的账号
    public static String currPhone = "";
    //当前登录的用户名
    public static String currName = "";
    //当前是否是乘客登录，为false是司机端登录
    public static Boolean isPassengers = true;


    private void setupViews() {
        //账号输入框
        etPhone = findViewById(R.id.et_login_phone);
        ClearEditText etPassword = findViewById(R.id.et_login_pwd);
        TextView tvSwitchRole = findViewById(R.id.tv_login_switch);
        TextView btnRegister = findViewById(R.id.btn_login_forget_pwd);

        findViewById(R.id.btn_login_login).setOnClickListener(view -> {
            //点击登录后查询当前账号是否存在
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            int userType = isPassengers ? 1 : 0;
            User user = LitePal.where("account=? and passoword=? and isPassengers=?", phone, password, String.valueOf(userType))
                    .findFirst(User.class);
            if (user != null) {
                //存在表示账号验证通过
                currName = user.getUserName();
                performLogin();
            } else {
                ToastHelper.showShort("账号或者密码错误");
            }
        });

        tvSwitchRole.setOnClickListener(view -> {
            isPassengers = !isPassengers;
            tvSwitchRole.setText(isPassengers ? "乘客登录" : "司机登录");
        });

        btnRegister.setOnClickListener(view -> {
            int userType = isPassengers ? 1 : 0;
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("isPassengers", userType);
            startActivity(intent);
        });
    }

    private void performLogin() {
        List<String> permissionList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList = Arrays.asList(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE);
        }
        if (!permissionList.isEmpty()) {
            //地图需要定位权限，先弹出权限提示框
            PermissionX.init(this)
                    .permissions(permissionList)
                    .onExplainRequestReason((scope, deniedList) ->
                            scope.showRequestReasonDialog(deniedList, "PermissionX需要您同意以下权限才能正常使用", "Allow", "Deny"))
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            //进入首页
                            currPhone = etPhone.getText().toString().trim();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
