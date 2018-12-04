package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.wrap.CanClearEditText;
import com.gcml.common.data.UserInfoBean;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.business.Utils;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.T;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class IDCardNumberRegisterActivity extends BaseActivity implements CanClearEditText.OnTextChangeListener, View.OnClickListener {


    /**
     * 身份证号码注册
     */
    private TextView textView17;
    /**
     * (身份证号将与您的居民健康档案绑定，请确认是否填写正确)
     */
    private TextView textView18;
    private CanClearEditText ccetPhone;
    /**
     * 下一步
     */
    private TextView tvNext;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_number_register);
        initView();
        initTitle();
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
//                startActivity(new Intent(IDCardNumberRegisterActivity.this, WifiConnectActivity.class));
            }
        });

        speak("请输入您的身份证号码");
        ccetPhone.setListener(this);
    }

    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }


    /**
     * 网络检测身份证是否注册
     */
    private void neworkCheckIdCard(final String idCardNumber) {

        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                T.show("您输入的身份证号码已注册,请重新注册");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                startActivity(new Intent(IDCardNumberRegisterActivity.this, PhoneAndCodeActivity.class)
                        .putExtra(PhoneAndCodeActivity.FROM_WHERE, PhoneAndCodeActivity.FROM_REGISTER_BY_IDCARD_NUMBER).putExtra(REGISTER_IDCARD_NUMBER, idCardNumber));
//
            }
        });
    }


    public static final String REGISTER_IDCARD_NUMBER = "registerIdCardNumber";
    public static final String REGISTER_PHONE_NUMBER = "registerPhoneNumber";
    public static final String REGISTER_REAL_NAME = "registeRrealName";
    public static final String REGISTER_SEX = "registerSex";
    public static final String REGISTER_ADDRESS = "registerAddress";

    @Override
    public void onTextChange(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }

    private void initView() {
        textView17 = (TextView) findViewById(R.id.textView17);
        textView18 = (TextView) findViewById(R.id.textView18);
        ccetPhone = (CanClearEditText) findViewById(R.id.ccet_phone);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_next) {
            String phone = ccetPhone.getPhone();
            if (TextUtils.isEmpty(phone)) {
                speak("请输入您的身份证号码");
                return;
            }

            if (!Utils.checkIdCard1(phone)) {
                speak("请输入正确的身份证号码");
                return;
            }

            neworkCheckIdCard(phone);
        }
    }
}
