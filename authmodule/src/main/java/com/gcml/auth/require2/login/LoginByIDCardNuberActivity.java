package com.gcml.auth.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.dialog.DialogTypeEnum;
import com.gcml.auth.require2.dialog.SomeCommonDialog;
import com.gcml.auth.require2.register.activtiy.ChoiceIDCardRegisterTypeActivity;
import com.gcml.auth.require2.wrap.CanClearEditText;
import com.gcml.common.data.UserInfoBean;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.business.Utils;
import com.gcml.lib_common.util.common.ActivityHelper;

public class LoginByIDCardNuberActivity extends BaseActivity implements SomeCommonDialog.OnDialogClickListener, CanClearEditText.OnTextChangeListener, View.OnClickListener {

    TextView tvNext;
    TextView textView17;
    private CanClearEditText ccetIdNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_idcard_nuber);
        initView();
        intTitle();
        ActivityHelper.addActivity(this);
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
//                startActivity(new Intent(LoginByIDCardNuberActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入身份证号码登录");
        ccetIdNumber.setListener(this);
    }


    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_next) {
            String IdCardNumber = ccetIdNumber.getPhone();
            if (!Utils.checkIdCard1(IdCardNumber)) {
                mlSpeak("请输入正确的身份证号码");
                return;
            }
            neworkCheckIdCard(IdCardNumber);

        }
    }

    /**
     * 网络检测身份证是否注册
     */
    private void neworkCheckIdCard(final String idCardNumber) {

        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                //保存信息
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
//                new JpushAliasUtils(LoginByIDCardNuberActivity.this).setAlias("user_" + response.bid);

                startActivity(new Intent(LoginByIDCardNuberActivity.this, CodeActivity.class).putExtra("phone", response.tel));
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                registerNoticeDialog();
            }
        });
    }

    private void registerNoticeDialog() {
        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.idCardUnregistered);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClickConfirm(DialogTypeEnum type) {
        startActivity(new Intent(LoginByIDCardNuberActivity.this, ChoiceIDCardRegisterTypeActivity.class));
    }

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
        ccetIdNumber = (CanClearEditText) findViewById(R.id.tv_phone_number_notice);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
