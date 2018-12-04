package com.gcml.auth.require2.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.auth.R;

/**
 * 常用提示的dialog
 * Created by lenovo on 2018/7/11.
 */

@SuppressLint("ValidFragment")
public class SomeCommonDialog extends DialogFragment implements View.OnClickListener {

    private final DialogTypeEnum type;
    private OnDialogClickListener listener;
    private View view;
    private TextView tvTitle;
    /**
     * 确定
     */
    private TextView confirm;
    /**
     * 取消
     */
    private TextView cancel;

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.confirm) {
            clickConfirm();
        } else if (i == R.id.cancel) {
            clickCancel();
        }
    }


    public interface OnDialogClickListener {
        void onClickConfirm(DialogTypeEnum type);

//        void onClickCancel();
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ValidFragment")
    public SomeCommonDialog(DialogTypeEnum type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.some_common_dialog, container, false);
        initView(view);
        return view;
    }

    private void clickConfirm() {
        if (listener == null) {
            return;
        }
        listener.onClickConfirm(type);
        dismiss();
    }

    private void clickCancel() {
//        if (listener==null){
//            return;
//        }
//        listener.onClickCancel();

        dismiss();
    }

}
