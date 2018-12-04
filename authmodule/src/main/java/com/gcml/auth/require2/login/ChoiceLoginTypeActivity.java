package com.gcml.auth.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.register.activtiy.ChoiceIDCardRegisterTypeActivity;
import com.gcml.auth.require2.register.activtiy.RegisterByIdCardActivity;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.T;

public class ChoiceLoginTypeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imLoginByIdcard;
    private ImageView imLoginByFinger;
    private ImageView imLoginByFace;
    /**
     * 指纹识别
     */
    private TextView textView26;
    /**
     * 身份证扫描
     */
    private TextView textView30;
    /**
     * VIP人脸登录
     */
    private TextView textView29;
    private ImageView imLoginByIdNumber;
    /**
     * 身份证号登录
     */
    private TextView textView28;
    private ImageView imageView4;
    /**
     * 立即注册
     */
    private TextView tvToRegister;
    /**
     * 没有账号?
     */
    private TextView tvNoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_idcard_login_type);
        initView();
        initTitle();
        ActivityHelper.addActivity(this);
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("选 择 登 录 方 式");

        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChoiceLoginTypeActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请选择登录方式");
    }

    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.im_login_by_idcard) {//刷身份证 登录,注册公用界面 跳转刷身份证登录界面 intent传login
            startActivity(new Intent(this, RegisterByIdCardActivity.class)
                    .putExtra("login", true));

        } else if (i == R.id.im_login_by_finger) {
            T.show("敬请期待");

        } else if (i == R.id.im_login_by_face) {
            startActivity(new Intent(this, FaceLoginActivity.class).putExtra("from", "Welcome"));

        } else if (i == R.id.im_login_by_id_number) {
            startActivity(new Intent(this, LoginByIDCardNuberActivity.class));


        } else if (i == R.id.tv_to_register) {
            startActivity(new Intent(this, ChoiceIDCardRegisterTypeActivity.class));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        get3BindInfo();
    }

    private void initView() {
        imLoginByIdcard = (ImageView) findViewById(R.id.im_login_by_idcard);
        imLoginByIdcard.setOnClickListener(this);
        imLoginByFinger = (ImageView) findViewById(R.id.im_login_by_finger);
        imLoginByFinger.setOnClickListener(this);
        imLoginByFace = (ImageView) findViewById(R.id.im_login_by_face);
        imLoginByFace.setOnClickListener(this);
        textView26 = (TextView) findViewById(R.id.textView26);
        textView30 = (TextView) findViewById(R.id.textView30);
        textView29 = (TextView) findViewById(R.id.textView29);
        imLoginByIdNumber = (ImageView) findViewById(R.id.im_login_by_id_number);
        imLoginByIdNumber.setOnClickListener(this);
        textView28 = (TextView) findViewById(R.id.textView28);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        tvToRegister = (TextView) findViewById(R.id.tv_to_register);
        tvToRegister.setOnClickListener(this);
        tvNoCount = (TextView) findViewById(R.id.tv_no_count);
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
