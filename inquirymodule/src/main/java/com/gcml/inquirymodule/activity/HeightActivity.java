package com.gcml.inquirymodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.inquirymodule.R;
import com.gcml.inquirymodule.adapter.SelectAdapter;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.T;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class HeightActivity extends BaseActivity implements View.OnClickListener {
    protected SelectAdapter adapter;
    public ArrayList<String> mStrings;
    /**
     * 您的身高
     */
    public TextView tvSignUpHeight;
    public RecyclerView rvContent;
    public TextView tvUnit;
    /**
     * 上一步
     */
    public TextView tvSignUpGoBack;
    /**
     * 下一步
     */
    public TextView tvSignUpGoForward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        initView();
        initTitle();
        ActivityHelper.addActivity(this);
    }

    protected void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }

    protected int selectedPosition = 20;

    protected void initView() {
        tvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        rvContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvUnit = (TextView) findViewById(R.id.tv_sign_up_unit);

        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);

        tvUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                T.show((mStrings == null ? String.valueOf(position) : mStrings.get(position)));
            }
        });
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvContent.smoothScrollToPosition(position);
            }
        });
        rvContent.setAdapter(adapter);

    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        Intent intent = new Intent(this, WeightActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                break;
            case R.id.tv_sign_up_go_forward:
                break;
        }
    }
}
