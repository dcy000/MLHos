package com.gcml.consitutionmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.consitutionmodule.adapter.FragAdapter;
import com.gcml.consitutionmodule.bean.HealthManagementAnwserBean;
import com.gcml.consitutionmodule.bean.HealthManagementResultBean;
import com.gcml.consitutionmodule.bean.OlderHealthManagementBean;
import com.gcml.consitutionmodule.fragment.HealthItemFragment;
import com.gcml.consitutionmodule.wrap.MonitorViewPager;
import com.gcml.lib_common.app.BaseApp;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.T;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ConsitutionActivity extends BaseActivity implements View.OnClickListener {

    private List<OlderHealthManagementBean.DataBean.QuestionListBean> questionList = new ArrayList<>();
    private int count;
    private String hmQuestionnaireId = "";
    private MonitorViewPager vp;
    /**
     * 上一题
     */
    private TextView tvPreviousItem;
    /**
     * 1/3
     */
    private TextView tvCurrentItem;
    /**
     * 下一题
     */
    private TextView tvNextItem;
    private LinearLayout llOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_health_management_sercive);
        bindView();
        initView();
        initTitle();
        initData();
    }

    private void bindView() {
        vp = (MonitorViewPager) findViewById(R.id.vp);
        tvPreviousItem = (TextView) findViewById(R.id.tv_previous_item);
        tvPreviousItem.setOnClickListener(this);
        tvCurrentItem = (TextView) findViewById(R.id.tv_current_item);
        tvNextItem = (TextView) findViewById(R.id.tv_next_item);
        tvNextItem.setOnClickListener(this);
        llOperator = (LinearLayout) findViewById(R.id.ll_operator);
    }

    private void initData() {
        showLoadingDialog("正在加载中...");
        NetworkApi.getHealthManagementForOlder(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                OlderHealthManagementBean bean = new Gson().fromJson(body, OlderHealthManagementBean.class);
                if (bean != null && bean.tag) {
                    OlderHealthManagementBean.DataBean data = bean.data;
                    ConsitutionActivity.this.hmQuestionnaireId = data.hmQuestionnaireId;
                    if (data != null) {
                        questionList = data.questionList;
                        if (questionList != null && questionList.size() != 0) {
                            count = questionList.size();
                            llOperator.setVisibility(View.VISIBLE);
                            initView();
                        }
                    }

                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });
    }

    private ArrayList<Fragment> fragments;
    private int index;

    private void initView() {
        tvCurrentItem.setText(1 + "/" + count);

        fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fragments.add(HealthItemFragment.getInstance((i + 1) + "", questionList.get(i)));
        }
        vp.setOffscreenPageLimit(count - 1);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ConsitutionActivity.this.index = position;
                tvCurrentItem.setText((index + 1) + "/" + count);

                if (isLastPager()) {
                    tvNextItem.setText("提交");
                } else {
                    tvNextItem.setText("下一题");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质自查");
    }


    private void submit() {
        showLoadingDialog("正在提交...");
        HealthManagementAnwserBean anwserBean = new HealthManagementAnwserBean();
        anwserBean.equipmentId = LocalShared.getInstance(this).getEqID();
        anwserBean.userId = LocalShared.getInstance(BaseApp.app).getUserId();
        anwserBean.hmQuestionnaireId = this.hmQuestionnaireId;
        anwserBean.answerList = new ArrayList<>();

        if (questionList != null && questionList.size() != 0) {

            for (int i = 0; i < questionList.size(); i++) {
                HealthManagementAnwserBean.AnswerListBean anwser = new HealthManagementAnwserBean.AnswerListBean();
                anwser.answerScore = questionList.get(i).answerScore;
                anwser.hmAnswerId = questionList.get(i).hmAnswerId;
                anwser.hmQuestionId = questionList.get(i).hmQuestionId;
                anwser.questionSeq = questionList.get(i).questionSeq;

                anwserBean.answerList.add(anwser);

            }
        }

        String anwserJson = new Gson().toJson(anwserBean);
        NetworkApi.postHealthManagementAnwser(anwserJson, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                HealthManagementResultBean resultBean = new Gson().fromJson(response.body(), HealthManagementResultBean.class);
                if (resultBean != null && resultBean.tag) {
                    T.show("提交成功");
                    gotoResultPage(resultBean.data);
                } else {
                    T.show(resultBean.message);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });


    }

    /**
     * 跳转到结果页面
     *
     * @param data
     */
    private void gotoResultPage(List<HealthManagementResultBean.DataBean> data) {
        startActivity(new Intent(this, ConsitutionActivity.class).putExtra("result_data", (Serializable) data));
        finish();
    }

    private boolean isLastPager() {
        return index == count - 1;
    }

    public void nextCurrentPage() {
        vp.setCurrentItem(index + 1, true);
    }

    public void preCurrentPage() {
        vp.setCurrentItem(index - 1, true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_previous_item) {
            preCurrentPage();

        } else if (i == R.id.tv_next_item) {
            if (questionList == null) {
                return;
            }
            if (!questionList.get(index).isSelected) {
                T.show("请选择答案");
                return;
            }

            if (isLastPager()) {
                submit();
                return;
            }
            nextCurrentPage();


        } else {
        }
    }
}
