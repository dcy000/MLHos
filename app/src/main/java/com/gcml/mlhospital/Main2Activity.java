package com.gcml.mlhospital;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 首页
     */
    private TextView tvHome;
    /**
     * 语音播报
     */
    private TextView coreTvVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();

    }

    private void initView() {
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        coreTvVoice = (TextView) findViewById(R.id.core_tv_voice);
        coreTvVoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.core_tv_voice:
                MLVoiceSynthetize.startSynthesize(this, "绑定公共卫生服务管理 V3.0账号", false);
                break;
        }
    }
}
