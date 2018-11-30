package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.wrap.CanClearEditText;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RealNameActivity extends BaseActivity {

    @BindView(R.id.ccet_name)
    CanClearEditText ccetName;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);
        ButterKnife.bind(this);
        initTitle();
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initView() {
        ccetName.setIsChinese(true);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 注 册");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RealNameActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入您的真实姓名");

    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String realName = ccetName.getPhone();
        if (TextUtils.isEmpty(realName)) {
            mlSpeak("请输入姓名");
            return;
        }
        startActivity(new Intent(this, SexActivity.class)
                .putExtra(REGISTER_REAL_NAME, realName)
                .putExtras(getIntent()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}
