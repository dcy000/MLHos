package com.gcml.common.oldnet;

import android.text.TextUtils;

import com.gcml.common.data.UserInfoBean;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.lib_common.BuildConfig;
import com.gcml.lib_common.app.BaseApp;
import com.gcml.lib_common.util.business.Utils;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

;

public class NetworkApi {
    //public static final String BasicUrl = "http://118.31.73.176:8080";
    //public static final String BasicUrl = "http://192.168.200.117:8080";
    //public static final String BasicUrl = "http://192.168.200.120:8080";
    //public static final String BasicUrl = "http://192.168.200.157:8080";

    public static final String BasicUrl = BuildConfig.SERVER_ADDRESS;
    public static final String PASSWORD = "123456";

    /**
     * 查询3.0用户
     */
    public static final String QUERY_3_ACCOUNT = BasicUrl + "/ZZB/api/health/inquiry/sanitation/";

    public static void Query3Account(String account, String passWord, StringCallback callback) {
        OkGo.<String>post(QUERY_3_ACCOUNT)
                .headers("equipmentId", Utils.getDeviceId())
                .params("userName", account)
                .params("password", passWord)
                .execute(callback);
    }

    public static final String IS_PHONE_REGISTERED = BasicUrl + "/ZZB/login/tel_isClod";

    public static void canRegister(
            String phone,
            String state,
            NetworkManager.SuccessCallback<Object> successCallback,
            NetworkManager.FailedCallback failedCallback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("tel", phone);
        params.put("state", state);
        NetworkManager.getInstance().postResultClass(IS_PHONE_REGISTERED, params, Object.class, successCallback, failedCallback);
    }

    public static final String GET_CODE = BasicUrl + "/ZZB/br/GainCode";

    public static void getCode(
            String phone,
            NetworkManager.SuccessCallback<String> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        NetworkManager.getInstance().getResultString(GET_CODE, params, successCallback, failedCallback);
    }

    public static final String TOKEN_URL = BasicUrl + "/ZZB/br/seltoken";

    public static void get_token(NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {

        Map<String, String> paramsMap = new HashMap<>();

        NetworkManager.getInstance().postResultString(TOKEN_URL, paramsMap, listener, failedCallback);
    }

    public static String USER_XUN_FEI_URL = BasicUrl + "/ZZB/api/user/xunfei/";

    public static void getUserXunFeiInfo(String userId, StringCallback callback) {
        OkGo.<String>get(USER_XUN_FEI_URL + userId + "/")
                .headers("equipmentId", Utils.getDeviceId())
                .params("userId", userId)
                .params("equipmentId", Utils.getDeviceId())
                .execute(callback);
    }

    public static final String RegisterUrl = BasicUrl + "/ZZB/br/appadd";

    public static void register(String registeRrealName,
                                String registerSex,
                                String registerAddress,
                                String registerIdCardNumber,
                                String registerPhoneNumber,
                                String user_photo,
                                NetworkManager.SuccessCallback<UserInfoBean> successCallback,
                                NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bname", registeRrealName);
        paramsMap.put("sex", registerSex);
        paramsMap.put("eqid", Utils.getDeviceId());
        paramsMap.put("tel", registerPhoneNumber);
        paramsMap.put("dz", registerAddress);
        paramsMap.put("sfz", registerIdCardNumber);
        paramsMap.put("user_photo", user_photo);
        paramsMap.put("pwd", PASSWORD);
        NetworkManager.getInstance().postResultClass(RegisterUrl, paramsMap, UserInfoBean.class, successCallback, failedCallback);

    }

    public static final String AUTH_IS_REGISTERED_ID_CARD = BasicUrl + "/ZZB/login/user_sfz_login";

    public static void isRegisteredByIdCard(String idCard, NetworkManager.SuccessCallback<UserInfoBean> successCallback,
                                            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("sfz", idCard);
        NetworkManager.getInstance().postResultClass(AUTH_IS_REGISTERED_ID_CARD, params, UserInfoBean.class, successCallback, failedCallback);
    }

    public static String EQIPMENT_ID_XUN_FEI_URL = BasicUrl + "/ZZB/api/user/xunfei/equipment/";

    public static void getEquipmentXunFeiInfo(String equipmentId, StringCallback callback) {
        OkGo.<String>get(EQIPMENT_ID_XUN_FEI_URL + equipmentId + "/")
                .headers("equipmentId", Utils.getDeviceId())
                .params("equipmentId", equipmentId)
                .execute(callback);
    }

    public static final String ADD_GROUP = BasicUrl + "/ZZB/xf/insert_group_record";

    /**
     * 添加组到后台
     *
     * @param callback
     * @param failedCallback
     */
    public static void recordGroup(String gid, String xfid, NetworkManager.SuccessCallback<String> callback, NetworkManager.FailedCallback failedCallback) {

        if (TextUtils.isEmpty(LocalShared.getInstance(BaseApp.app).getUserId())) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", LocalShared.getInstance(BaseApp.app).getUserId());
        paramsMap.put("gid", gid);
        paramsMap.put("xfid", xfid);
        NetworkManager.getInstance().postResultString(ADD_GROUP, paramsMap, callback, failedCallback);
    }

    public static final String LOGIN_BY_XUNFEI_URL = BasicUrl + "/ZZB/login/xunfei";

    public static void loginByXunFei(String xfId, String groupId, StringCallback callback) {
        OkGo.<String>post(LOGIN_BY_XUNFEI_URL)
                .headers("equipmentId", Utils.getDeviceId())
                .params("equipmentId", Utils.getDeviceId())
                .params("xunfeiId", xfId)
                .params("groupId", groupId)
                .execute(callback);
    }

    public static final String PAY_STATUS = BasicUrl + "/ZZB/order/pay_pro";

    public static void pay_status(String userid, String eqid, String orderid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("eqid", eqid);
        paramsMap.put("orderid", orderid);
        NetworkManager.getInstance().postResultString(PAY_STATUS, paramsMap, listener, failedCallback);
    }

    public static final String PAY_CANCEL = BasicUrl + "/ZZB/order/delivery_del";

    public static void pay_cancel(String pay_state, String delivery_state, String display_state, String orderid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pay_state", pay_state);
        paramsMap.put("delivery_state", delivery_state);
        paramsMap.put("display_state", display_state);
        paramsMap.put("orderid", orderid);
        NetworkManager.getInstance().postResultString(PAY_CANCEL, paramsMap, listener, failedCallback);
    }

    public static final String GETINFO_URL = BasicUrl + "/ZZB/br/selMoreUser";

    public static void getAllUsers(String accounts, NetworkManager.SuccessCallback<ArrayList<UserInfoBean>> callback) {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("p", accounts);
        NetworkManager.getInstance().getResultClass(GETINFO_URL, paramsMap, new TypeToken<ArrayList<UserInfoBean>>() {
        }.getType(), callback);
    }



    public static final String LOGIN_URL = BasicUrl + "/ZZB/login/applogin";

    public static void login(String phoneNum, String pwd, NetworkManager.SuccessCallback<UserInfoBean> listener, NetworkManager.FailedCallback failedCallback) {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", phoneNum);
        paramsMap.put("password", pwd);
        NetworkManager.getInstance().postResultClass(LOGIN_URL, paramsMap, UserInfoBean.class, listener, failedCallback);
    }


}
