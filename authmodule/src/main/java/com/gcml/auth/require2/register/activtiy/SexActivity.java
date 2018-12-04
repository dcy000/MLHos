package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;

import static com.gcml.auth.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_SEX;


public class SexActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 您的性别
     */
    private TextView tvSex;
    /**
     * 男
     */
    private TextView tvSexMan;
    /**
     * 女
     */
    private TextView tvSexWomen;
    /**
     * 下一步
     */
    private TextView tvNext;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        initTitle();
        initView();
        ActivityHelper.addActivity(this);
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
//                startActivity(new Intent(SexActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入您的性别");
    }


    private void clickMan(boolean man) {
        tvSexMan.setSelected(man);
        tvSexWomen.setSelected(!man);
    }

    private void initView() {
        clickMan(true);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvSexMan = (TextView) findViewById(R.id.tv_sex_man);
        tvSexMan.setOnClickListener(this);
        tvSexWomen = (TextView) findViewById(R.id.tv_sex_women);
        tvSexWomen.setOnClickListener(this);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
    }

    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_sex_man) {
            clickMan(true);

        } else if (i == R.id.tv_sex_women) {
            clickMan(false);

        } else if (i == R.id.tv_next) {
            String sex = tvSexMan.isSelected() ? tvSexMan.getTag().toString() : tvSexWomen.getTag().toString();
            startActivity(new Intent(this, AddressActivity.class)
                    .putExtra(REGISTER_SEX, sex)
                    .putExtras(getIntent()));

        }
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
