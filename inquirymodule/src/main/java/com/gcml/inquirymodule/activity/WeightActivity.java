package com.gcml.inquirymodule.activity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class WeightActivity extends HeightActivity {
    public List<String> mStrings;

    @Override
    protected void initTitle() {
        super.initTitle();
        tvUnit.setText("kg");
    }

    @Override
    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 35; i < 150; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @Override
    public void onTvGoForwardClicked() {
        String weight = mStrings.get(selectedPosition);
        Intent intent = new Intent(this, AddressActivity.class);
        startActivity(intent);
    }
}
