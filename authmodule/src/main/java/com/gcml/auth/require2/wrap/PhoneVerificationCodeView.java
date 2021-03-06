package com.gcml.auth.require2.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.lib_common.util.business.Utils;

/**
 * Created by lenovo on 2018/7/11.
 */

public class PhoneVerificationCodeView extends LinearLayout implements View.OnClickListener {

    public EditText tvPhone;
    public ImageView ivDelete;
    public EditText etCode;
    public TextView tvSendCode;
    private Context context;

    public PhoneVerificationCodeView(Context context) {
        this(context, null);
    }

    public PhoneVerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.phone_verification_view, null);
        tvPhone = view.findViewById(R.id.tv_phone);
        ivDelete = view.findViewById(R.id.iv_delete);
        tvSendCode = view.findViewById(R.id.tv_send_code);
        ivDelete.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        initEvent();
        addView(view);
    }

    /**
     * 联动逻辑处理
     */
    private void initEvent() {
        tvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(tvPhone.getText().toString())) {
                    ivDelete.setVisibility(GONE);
                } else {
                    ivDelete.setVisibility(VISIBLE);
                }

                if (Utils.isValidPhone(s.toString())) {
                    tvSendCode.setSelected(true);
                } else {
                    tvSendCode.setSelected(false);
                }

            }
        });
    }

    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_delete) {
            clearPhoneNumber();

        } else if (i == R.id.tv_send_code) {
            sendCode();

        }
    }

    private void sendCode() {
        if (listener == null) {
            return;
        }

        String phone = tvPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        if (!tvSendCode.isSelected()) {
            return;
        }
        listener.onSendCode(phone);
    }

    public void countDown() {
        tvSendCode.setSelected(false);
        tvSendCode.setEnabled(false);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    tvSendCode.setSelected(true);
                    tvSendCode.setEnabled(true);
                    tvSendCode.setText("发送验证码");
                    count = TIME;
                    return;
                }
                tvSendCode.setText(count + "秒重发");
                postDelayed(this, 1000);
            }
        }, 1000);
    }

    private int count = TIME;
    private static final int TIME = 60;


    private void clearPhoneNumber() {
        tvPhone.setText("");
    }


    private OnSendClickListener listener;

    public String getPhone() {
        return tvPhone.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }


    public interface OnSendClickListener {
        void onSendCode(String phone);
    }


    public String getCode() {
        return etCode.getText().toString().trim();
    }

    public void setListener(OnSendClickListener listener) {
        this.listener = listener;
    }


}
