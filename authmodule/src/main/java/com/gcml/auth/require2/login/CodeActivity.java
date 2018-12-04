package com.gcml.auth.require2.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.common.data.UserInfoBean;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.Handlers;
import com.gcml.lib_common.util.common.T;

import org.json.JSONException;
import org.json.JSONObject;

import static com.gcml.common.oldnet.NetworkApi.PASSWORD;


public class CodeActivity extends BaseActivity implements View.OnClickListener {


    private String phoneNumber;
    private String code = "";
    public static String DEFAULT_CODE = "8888";
    /**
     * 请输入手机158****1914收到的验证码
     */
    private TextView textView17;
    /**
     * 请输入验证码
     */
    private EditText etCode;
    /**
     * 发送验证码
     */
    private TextView tvSendCode;
    private RelativeLayout rlSendNumber;
    /**
     * 下一步
     */
    private TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        intTitle();
        initView();
        sendCode();
        ActivityHelper.addActivity(this);
    }

    private void initView() {
        textView17 = (TextView) findViewById(R.id.textView17);
        etCode = (EditText) findViewById(R.id.et_code);
        tvSendCode = (TextView) findViewById(R.id.tv_send_code);
        tvSendCode.setOnClickListener(this);
        rlSendNumber = (RelativeLayout) findViewById(R.id.rl_send_number);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);

        phoneNumber = getIntent().getStringExtra("phone");
        String phoneStar = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        textView17.setText("请输入手机" + phoneStar + "收到的验证码");
        tvSendCode.setSelected(true);
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 登 录");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CodeActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入手机验证码登录");
    }


    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_next) {
            next();

        } else if (i == R.id.tv_send_code) {
            sendCode();

        }
    }

    private void next() {
        final String phoneCode = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(phoneCode)) {
            mlSpeak("请输入手机验证码");
            return;
        }

        if (code.equals(phoneCode) /*|| DEFAULT_CODE.equals(phoneCode)*/) {
            login();
        } else {
            mlSpeak("验证码错误");
        }
    }

    private void login() {
        NetworkApi.login(phoneNumber, PASSWORD, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
//                new JpushAliasUtils(CodeActivity.this).setAlias("user_" + response.bid);
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).addAccount(response.bid, response.xfid);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
                hideLoadingDialog();
//                startActivity(new Intent(CodeActivity.this, InquiryAndFileActivity.class));
                finish();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                T.show("手机号或密码错误");
            }
        });
    }

    private void sendCode() {
        //请求验证码
        showLoadingDialog("");
        NetworkApi.getCode(phoneNumber, new NetworkManager.SuccessCallback<String>() {

            @Override
            public void onSuccess(String codeJson) {
                hideLoadingDialog();
                try {
                    JSONObject codeObj = new JSONObject(codeJson);
                    String code = codeObj.optString("code");
                    CodeActivity.this.code = code;
                    if (code != null) {
                        updateCountDownUi();
                        T.show("获取验证码成功");
                        mlSpeak("获取验证码成功");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    T.show("获取验证码失败");
                    mlSpeak("获取验证码失败");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                T.show("获取验证码失败");
                mlSpeak("获取验证码失败");
            }
        });

    }

    private void updateCountDownUi() {
        tvSendCode.setSelected(false);
        tvSendCode.setEnabled(false);
        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    tvSendCode.setSelected(true);
                    tvSendCode.setEnabled(true);
                    tvSendCode.setText("发送验证码");
                    count = 60;
                    return;
                }
                tvSendCode.setText(count + "秒重发");
                Handlers.ui().postDelayed(this, 1000);
            }
        }, 1000);
    }

    private int count = 60;

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
