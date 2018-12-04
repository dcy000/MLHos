package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.wrap.CanClearEditText;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;

import static com.gcml.auth.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_REAL_NAME;


public class RealNameActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 实名认证
     */
    private TextView textView17;
    /**
     * (姓名将与您的居民健康档案绑定，请确认是否填写正确)
     */
    private TextView textView18;
    private CanClearEditText ccetName;
    /**
     * 下一步
     */
    private TextView tvNext;
    private ImageView imageView2;
/*

    public CanClearEditText ccetName;
    public TextView tvNext;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);
        initTitle();
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initView() {
        textView17 = (TextView) findViewById(R.id.textView17);
        textView18 = (TextView) findViewById(R.id.textView18);
        ccetName = (CanClearEditText) findViewById(R.id.ccet_name);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
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
//                startActivity(new Intent(RealNameActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入您的真实姓名");

    }

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
    public void onClick(View v) {
        onViewClicked();
    }
}
