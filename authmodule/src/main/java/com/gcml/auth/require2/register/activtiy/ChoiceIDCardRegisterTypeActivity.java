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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceIDCardRegisterTypeActivity extends BaseActivity {


    @BindView(R.id.im_idcard_login)
    ImageView imIdcardLogin;
    @BindView(R.id.im_idcard_number_login)
    ImageView imIdcardNumberLogin;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.textView16)
    TextView textView16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_idcard_register_type);
        ButterKnife.bind(this);
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
                startActivity(new Intent(ChoiceIDCardRegisterTypeActivity.this, WifiConnectActivity.class));
            }
        });

        MLVoiceSynthetize.startSynthesize(this, "请选择注册方式", false);
    }


    @OnClick({R.id.im_idcard_login, R.id.im_idcard_number_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_idcard_login:
                //刷身份证
                startActivity(new Intent(this, RegisterByIdCardActivity.class));
                break;
            case R.id.im_idcard_number_login:
                startActivity(new Intent(this, IDCardNumberRegisterActivity.class));
                break;
        }
    }


}
