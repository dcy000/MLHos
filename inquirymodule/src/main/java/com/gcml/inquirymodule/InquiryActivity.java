package com.gcml.inquirymodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.inquirymodule.activity.HeightActivity;

public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 问诊界面
     */
    private TextView inquiryTvHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        initView();
    }

    private void initView() {
        inquiryTvHome = (TextView) findViewById(R.id.inquiry_tv_home);
        inquiryTvHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.inquiry_tv_home:
                startActivity(new Intent(this, HeightActivity.class));
                break;
        }
    }
}
