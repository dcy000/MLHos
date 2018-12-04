package com.gcml.auth.require2.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.lib_widget.CircleImageView;


/**
 * 头像确认的dialog
 * Created by Administrator on 2018/7/14.
 */

@SuppressLint("ValidFragment")
public class AffirmHeadDialog extends DialogFragment implements View.OnClickListener {
    private final byte[] imageData;
    public CircleImageView ivHead;
    public TextView confirm;
    public TextView cancel;

    @SuppressLint("ValidFragment")
    public AffirmHeadDialog(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_affirm_head, container, false);
        ivHead = view.findViewById(R.id.iv_head);
        confirm = view.findViewById(R.id.confirm);
        cancel = view.findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        updateHead();
        return view;
    }

    public void updateHead() {

        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ivHead.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.confirm) {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();

        } else if (i == R.id.cancel) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();

        }

    }

    public interface ClickListener {
        void onConfirm();

        void onCancel();
    }

    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
