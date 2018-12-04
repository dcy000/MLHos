package com.gcml.auth.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.auth.R;
import com.gcml.auth.require2.wrap.CanClearEditText;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import static com.gcml.auth.require2.register.activtiy.InputFaceActivity.REGISTER_ADDRESS;


public class AddressActivity extends BaseActivity implements View.OnClickListener {


    CityPickerView cityPicker = new CityPickerView();
    private ImageView imageView2;
    /**
     * 您的户籍地址
     */
    private TextView tvAddress;
    private TextView tvProvinceInfo;
    private TextView tvCityInfo;
    private TextView tvBlockInfo;
    /**
     * 省
     */
    private TextView tvProvince;
    /**
     * 市
     */
    private TextView tvCity;
    /**
     * 区/县
     */
    private TextView tvCity2;
    /**
     * TextView
     */
    private TextView textView19;
    private CanClearEditText canClearEditText;
    /**
     * 下一步
     */
    private TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initTile();
        initCityPicker();
        ActivityHelper.addActivity(this);

    }

    private void initCityPicker() {
        cityPicker.init(this);
        //添加默认的配置，不需要自己定义
        CityConfig cityConfig = new CityConfig.Builder()
                .title("选择城市").title("选择城市")//标题
                .titleTextSize(26)//标题文字大小
                .titleTextColor("#585858")//标题文字颜  色
                .titleBackgroundColor("#E9E9E9")//标题栏背景色

                .confirTextColor("#585858")//确认按钮文字颜色
                .confirmText("确定")//确认按钮文字
                .confirmTextSize(20)//确认按钮文字大小

                .cancelTextColor("#585858")//取消按钮文字颜色
                .cancelText("取消")//取消按钮文字
                .cancelTextSize(20)//取消按钮文字大小

                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .visibleItemsCount(5)//显示item的数量
                .province("浙江省")//默认显示的省份
                .city("杭州市")//默认显示省份下面的城市
                .district("滨江区")//默认显示省市下面的区县数据
                .setLineColor("#03a9f4")//中间横线的颜色
                .setLineHeigh(2)//中间横线的高度
                .setShowGAT(true)//是否显示港澳台数据，默认不显示
                .build();
        cityPicker.setConfig(cityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //省份
                if (province != null) {
                    tvProvinceInfo.setText(province.getName());
                }

                //城市
                if (city != null) {
                    tvCityInfo.setText(city.getName());
                }

                //地区
                if (district != null) {
                    tvBlockInfo.setText(district.getName());
                }

            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void initTile() {
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
//                startActivity(new Intent(AddressActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入您的户籍地址");

        canClearEditText.setIsChinese(true);
        canClearEditText.setHintText("请输入详细地址");
    }

    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_province_info || i == R.id.tv_city_info || i == R.id.tv_block_info) {
            cityPicker.showCityPicker();

        } else if (i == R.id.tv_next) {
            if (TextUtils.isEmpty(tvProvinceInfo.getText().toString().trim())) {
                mlSpeak("请输入您的户籍地址 ");
                return;
            }

            if (TextUtils.isEmpty(tvCityInfo.getText().toString().trim())) {
                mlSpeak("请输入您的户籍地址 ");
                return;
            }
            if (TextUtils.isEmpty(tvBlockInfo.getText().toString().trim())) {
                mlSpeak("请输入您的户籍地址 ");
                return;
            }


            String detailAddress = canClearEditText.getPhone();
            if (TextUtils.isEmpty(detailAddress)) {
                mlSpeak("请输入详细地址");
                return;
            }
            String address = tvProvinceInfo.getText().toString() + tvCity.getText().toString() + tvBlockInfo.getText().toString() + detailAddress;
            startActivity(new Intent(this, InputFaceActivity.class)
                    .putExtras(getIntent())
                    .putExtra(REGISTER_ADDRESS, address));

        }
    }

    private void initView() {
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvProvinceInfo = (TextView) findViewById(R.id.tv_province_info);
        tvProvinceInfo.setOnClickListener(this);
        tvCityInfo = (TextView) findViewById(R.id.tv_city_info);
        tvCityInfo.setOnClickListener(this);
        tvBlockInfo = (TextView) findViewById(R.id.tv_block_info);
        tvBlockInfo.setOnClickListener(this);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvCity2 = (TextView) findViewById(R.id.tv_city2);
        textView19 = (TextView) findViewById(R.id.textView19);
        canClearEditText = (CanClearEditText) findViewById(R.id.canClearEditText);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }
}
