package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.wrap.PhoneVerificationCodeView;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.T;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gcml.auth.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_PHONE_NUMBER;


public class PhoneAndCodeActivity extends BaseActivity implements PhoneVerificationCodeView.OnSendClickListener, View.OnClickListener {
    public static final String FROM_WHERE = "from_where";
    public static final String FROM_REGISTER_BY_IDCARD = "register_by_idCard";
    public static final String FROM_REGISTER_BY_IDCARD_NUMBER = "register_by_idCard_number";
    public static final String DEFAULT_CODE = "8888";

    boolean receiveCodeFirst = false;
    /**
     * 发手机号的验证码
     */
    private String phone = "";
    private String fromWhere;
    private PhoneVerificationCodeView phoneView;
    /**
     * 下一步
     */
    private TextView tvNext;
    private ImageView imageView2;
    /**
     * 没有手机号?请点击
     */
    private TextView tvCodePhoneAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_and_code);
        initView();
        intTitle();
        initEvent();
        mlSpeak("请输入手机号码,点击发送验证码");
        ActivityHelper.addActivity(this);
    }

    private void intTitle() {
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
//                startActivity(new Intent(PhoneAndCodeActivity.this, WifiConnectActivity.class));
            }
        });

        fromWhere = getIntent().getStringExtra(FROM_WHERE);
        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            mTitleText.setText("身 份 证 扫 描 注 册");
        }
    }

    private void initEvent() {
        phoneView.setListener(this);
    }

    public void onViewClicked() {
        if (TextUtils.isEmpty(phoneView.getPhone())) {
            mlSpeak("请输入手机号码");
            return;
        }

        String code = phoneView.getCode();
        if (TextUtils.isEmpty(code)) {
            mlSpeak("请输入验证码");
            return;
        }

        if (!isSendedCode) {
            mlSpeak("请点击按钮发送验证码");
            return;
        }

        if (!this.phone.equals(phoneView.getPhone())) {
            mlSpeak("验证码错误");
            return;
        }

        if (phoneView.tvSendCode.isEnabled() && receiveCodeFirst) {
            mlSpeak("验证码已过期请重新获取");
            return;
        }


        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            if (code.equals(this.code)/*|| DEFAULT_CODE.equals(code)*/) {
                startActivity(new Intent(this, InputFaceActivity.class)
                        .putExtras(getIntent())
                        .putExtra(REGISTER_PHONE_NUMBER, phone));
            } else {
                mlSpeak("验证码错误");
            }
        } else if (fromWhere.equals(FROM_REGISTER_BY_IDCARD_NUMBER)) {
            if (code.equals(this.code)/*||DEFAULT_CODE.equals(code)*/) {
                startActivity(new Intent(PhoneAndCodeActivity.this, RealNameActivity.class)
                        .putExtra(REGISTER_PHONE_NUMBER, phone)
                        .putExtras(getIntent()));
            } else {
                mlSpeak("验证码错误");
            }
        }


    }

    private String code = "";
    private boolean isSendedCode = false;

    @Override
    public void onSendCode(final String phone) {
        isSendedCode = true;
        this.phone = phone;
        showLoadingDialog("正在获取验证码...");
        NetworkApi.canRegister(phone, "3", new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                hideLoadingDialog();
                NetworkApi.getCode(phone, new NetworkManager.SuccessCallback<String>() {

                    @Override
                    public void onSuccess(String codeJson) {
                        try {
                            receiveCodeFirst = true;
                            JSONObject codeObj = new JSONObject(codeJson);
                            String code = codeObj.getString("code");
                            if (code != null) {
                                PhoneAndCodeActivity.this.code = code;
                                T.show("获取验证码成功");
//                                mlSpeak("获取验证码成功");
                                phoneView.countDown();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            T.show("获取验证码失败");
//                            mlSpeak("获取验证码失败");
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        T.show("获取验证码失败");
//                        mlSpeak("获取验证码失败");
                    }
                });

            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                ToastUtils.showLong("该手机号码已使用,请确认后重新使用");
            }
        });

    }


    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.stop();
    }

    private void initView() {
        phoneView = (PhoneVerificationCodeView) findViewById(R.id.phone_view);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        tvCodePhoneAddContact = (TextView) findViewById(R.id.tv_code_phone_add_contact);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_next) {
            onViewClicked();
        }
    }
}
