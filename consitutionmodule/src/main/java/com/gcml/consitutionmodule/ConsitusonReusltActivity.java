package com.gcml.consitutionmodule;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.consitutionmodule.bean.HealthManagementResultBean;
import com.gcml.lib_common.base.old.BaseActivity;

import java.util.List;


public class ConsitusonReusltActivity extends BaseActivity  {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_consituson_reuslt);
//    }


    private List<HealthManagementResultBean.DataBean> data;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consituson_reuslt);
        initTitle();
        initView();
    }

    private void initView() {
        container = (LinearLayout) findViewById(R.id.container);
        data = (List<HealthManagementResultBean.DataBean>) getIntent().getSerializableExtra("result_data");
        if (data != null & data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                HealthManagementResultBean.DataBean itemBean = data.get(i);
                if (!"否".equals(itemBean.result)) {
                    TextView item = new TextView(this);
                    initTextViewParams(item);
                    item.setText("体质类型:" + itemBean.constitutionName + "       得分:" + itemBean.score + "       " + itemBean.result);
                    container.addView(item);
                }
            }
        }
    }

    private void initTextViewParams(TextView item) {
        item.setTextSize(32);
        item.setPadding(20, 20, 20, 20);

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质自查报告");
        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);
    }


}
