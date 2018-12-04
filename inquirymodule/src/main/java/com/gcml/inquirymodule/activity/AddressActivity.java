package com.gcml.inquirymodule.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.inquirymodule.R;
import com.gcml.lib_common.base.old.BaseActivity;

public class AddressActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 您的现住址
     */
    private TextView tvSignUpAddress;
    private TextView spProvince;
    /**
     * 省
     */
    private TextView tvSignUpProvince;
    private TextView spCity;
    /**
     * 市
     */
    private TextView tvSignUpCity;
    private TextView spCounty;
    /**
     * 区
     */
    private TextView tvSignUpCounty;
    /**
     * 请输入详细地址
     */
    private EditText etSignUpAddress;
    /**
     * 上一步
     */
    private TextView tvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView tvSignUpGoForward;
    private ConstraintLayout clSignUpRootAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initTitle();
    }

    private void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }

    private void initView() {
        tvSignUpAddress = (TextView) findViewById(R.id.tv_sign_up_address);
        spProvince = (TextView) findViewById(R.id.sp_province);
        tvSignUpProvince = (TextView) findViewById(R.id.tv_sign_up_province);
        spCity = (TextView) findViewById(R.id.sp_city);
        tvSignUpCity = (TextView) findViewById(R.id.tv_sign_up_city);
        spCounty = (TextView) findViewById(R.id.sp_county);
        tvSignUpCounty = (TextView) findViewById(R.id.tv_sign_up_county);
        etSignUpAddress = (EditText) findViewById(R.id.et_sign_up_address);
        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        clSignUpRootAddress = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                break;
            case R.id.tv_sign_up_go_forward:
                break;
        }
    }



   /* CityPickerView cityPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initLocation();
        initTitle();
        initCityPicker();
    }

    private void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
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
                    spProvince.setText(province.getName());
                }

                //城市
                if (city != null) {
                    spCity.setText(city.getName());
                }

                //地区
                if (district != null) {
                    spCounty.setText(district.getName());
                }

            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        mLocationClient.setLocOption(locOption);
    }

    private LocationClient mLocationClient;

    private String finalDetailAddress = "";

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            stopLocation();

            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String county = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String streetNumber = bdLocation.getStreetNumber();

            finalDetailAddress = street + streetNumber;
            spProvince.setText(province);
            spCity.setText(city);
            spCounty.setText(county);
            etAddress.setText(finalDetailAddress);
            etAddress.setSelection(finalDetailAddress.length());
        }
    };

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up3_address_tip);
        startLocation();
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onPause() {
        stopLocation();
        super.onPause();
    }

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
    }

    @OnClick(R.id.cl_sign_up_root_address)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            T.show(R.string.sign_up3_address_tip);
            speak(R.string.sign_up3_address_tip);
            return;
        }

//        Intent intent = SignUp7HeightActivity.newIntent(this);
//        startActivity(intent);

        if ("女".equals(LocalShared.getInstance(this).getSex())) {
            startActivity(new Intent(this, PregnancyWenActivity.class));
        } else {
            startActivity(new Intent(this, DrinkInfoActivity.class));
        }
    }

    private String getAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(spProvince.getText().toString().trim())
                .append(spCity.getText().toString().trim())
                .append(spCounty.getText().toString().trim())
                .append(etAddress.getText().toString().trim());
        return builder.toString();
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sp_province:
            case R.id.sp_city:
            case R.id.sp_county:
                cityPicker.showCityPicker();
                break;
        }
    }*/
}
