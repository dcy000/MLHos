package com.gcml.lib_common.base.old;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.lib_common.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Resources mResources;
    private ProgressDialog mDialog;
    protected LayoutInflater mInflater;
    private boolean enableListeningLoop = false;
    private LinearLayout rootView;
    private View mTitleView;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected RelativeLayout mToolbar;
    protected LinearLayout mllBack;
    public SharedPreferences mIatPreferences;




    public void mlSpeak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = getResources();
        mInflater = LayoutInflater.from(this);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);

        rootView.addView(mTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (70 * mResources.getDisplayMetrics().density)));
        initToolbar();
    }


    private long lastTimeMillis = -1;
    private static final long DURATION = 500L;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastTimeMillis != -1) {
                long elapsedTime = currentTimeMillis - lastTimeMillis;
                if (elapsedTime < DURATION) {
                    lastTimeMillis = currentTimeMillis;
                    return true;
                }
            }
            lastTimeMillis = currentTimeMillis;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void initToolbar() {
        mllBack = (LinearLayout) mTitleView.findViewById(R.id.ll_back);
        mToolbar = (RelativeLayout) mTitleView.findViewById(R.id.toolbar);
        mTitleText = (TextView) mTitleView.findViewById(R.id.tv_top_title);
        mLeftText = (TextView) mTitleView.findViewById(R.id.tv_top_left);
        mRightText = (TextView) mTitleView.findViewById(R.id.tv_top_right);
        mLeftView = (ImageView) mTitleView.findViewById(R.id.iv_top_left);
        mRightView = (ImageView) mTitleView.findViewById(R.id.iv_top_right);
        mllBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastActivity();
            }
        });
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainActivity();
            }
        });
    }

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
    }

    @Override
    public void setContentView(int layoutResID) {
        mInflater.inflate(layoutResID, rootView);
        super.setContentView(rootView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void showLoadingDialog(String message) {
        try {
            if (mDialog == null) {
                mDialog = new ProgressDialog(mContext);
//            mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.setIndeterminate(true);
                mDialog.setMessage(message);
            }
            mDialog.show();
        } catch (Exception e) {

        }
    }


    public void hideLoadingDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.dismiss();
    }

}