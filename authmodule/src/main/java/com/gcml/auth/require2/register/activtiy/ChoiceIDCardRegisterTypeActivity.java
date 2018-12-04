package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class ChoiceIDCardRegisterTypeActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 身份证扫描注册
     */
    private ImageView imIdcardLogin;
    private ImageView imIdcardNumberLogin;
    /**
     * 身份证扫描注册
     */
    private TextView textView15;
    /**
     * 身份证号码注册
     */
    private TextView textView16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_idcard_register_type);
        initView();
        initTitle();
        ActivityHelper.addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("选 择 注 册 方 式");


        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChoiceIDCardRegisterTypeActivity.this, WifiConnectActivity.class));
            }
        });

        MLVoiceSynthetize.startSynthesize(this, "请选择注册方式", false);
    }


    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.im_idcard_login) {//刷身份证
            startActivity(new Intent(this, RegisterByIdCardActivity.class));

        } else if (i == R.id.im_idcard_number_login) {
            startActivity(new Intent(this, IDCardNumberRegisterActivity.class));

        }
    }


    private void initView() {
        imIdcardLogin = (ImageView) findViewById(R.id.im_idcard_login);
        imIdcardLogin.setOnClickListener(this);
        imIdcardNumberLogin = (ImageView) findViewById(R.id.im_idcard_number_login);
        imIdcardNumberLogin.setOnClickListener(this);
        textView15 = (TextView) findViewById(R.id.textView15);
        textView16 = (TextView) findViewById(R.id.textView16);
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
