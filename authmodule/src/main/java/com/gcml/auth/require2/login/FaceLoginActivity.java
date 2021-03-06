package com.gcml.auth.require2.login;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.gcml.auth.R;
import com.gcml.auth.require2.bean.EquipmentXFInfoBean;
import com.gcml.auth.require2.bean.PersonInfoResultBean;
import com.gcml.common.data.UserInfoBean;
import com.gcml.common.oldface.CreateGroupListener;
import com.gcml.common.oldface.FaceAuthenticationUtils;
import com.gcml.common.oldface.JoinGroupListener;
import com.gcml.common.oldface.VertifyFaceListener;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.lib_common.app.BaseApp;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.business.Utils;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.Handlers;
import com.gcml.lib_common.util.common.T;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import timber.log.Timber;


public class FaceLoginActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_face_login);
//    }

    private SurfaceView mPreviewSurface;
    private Camera mCamera;
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    private byte[] mImageData = null;
    private String orderid;
    private String fromString;//标识从哪个页面过来的
    private String fromType;
    private Button mTiaoguo;
    private ByteArrayOutputStream baos;
    private String mAuthid;
    private FaceLoginActivity.MyHandler myHandler;
    private ArrayList<UserInfoBean> mDataList;
    private boolean isInclude_PassPerson = false;
    private int authenticationNum = 0;
    private TextView tvTips;
    private boolean isTest;
    private LottieAnimationView lottAnimation;
    private Animation rotateAnim;
    //    private byte[] cacheCamera;
    private static final int TO_FACE_AUTHENTICATION = 1;
    private static final int TO_CAMERA_PRE_RESOLVE = 2;
    private boolean openOrcloseAnimation = true;
    private boolean isOnPause = false;
    private SurfaceHolder holder;
    private EquipmentXFInfoBean bean;

    class MyHandler extends Handler {
        private WeakReference<FaceLoginActivity> weakReference;

        public MyHandler(FaceLoginActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_FACE_AUTHENTICATION://开始验证头像
                    findViewById(R.id.iv_circle).startAnimation(rotateAnim);
                    if (bean != null) {
                        if (bean.tag) {
                            if (bean.data == null || bean.data.size() == 0) {
                                T.show("您不是VIP用户");
                                finishActivity();
                                return;
                            } else {
                                int size = bean.data.size();
                                for (int i = 0; i < size; i++) {
                                    FaceRecognition(bean.data.get(i).groupId);
                                }
                            }
                        } else {
                            T.show("网络服务繁忙");
                        }

                        return;
                    }


                    NetworkApi.getEquipmentXunFeiInfo(Utils.getDeviceId(), new StringCallback() {

                        @Override
                        public void onSuccess(Response<String> response) {
                            String body = response.body();
                            bean = new Gson().fromJson(body, EquipmentXFInfoBean.class);
                            if (bean != null) {
                                if (bean.tag) {
                                    if (bean.data == null || bean.data.size() == 0) {
                                        T.show("您不是VIP用户");
                                        finishActivity();
                                        return;
                                    } else {
                                        int size = bean.data.size();
                                        for (int i = 0; i < size; i++) {
                                            FaceRecognition(bean.data.get(i).groupId);
                                        }
                                    }
                                } else {
                                    T.show("网络服务繁忙");
                                }

                                return;
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            T.show("请求服务器失败,请稍后重试");
                            finishActivity();
                        }
                    });


                    break;
                case TO_CAMERA_PRE_RESOLVE://解析图像
                    if (mCamera != null) {
                        mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data, Camera camera) {
                                Bitmap sourceBitmap = decodeToBitMap(data, mCamera);
                                if (sourceBitmap != null) {
                                    Bitmap bitmap = Utils.centerSquareScaleBitmap(sourceBitmap, 300);
                                    if (bitmap != null) {
                                        if (baos != null)
                                            baos.reset();
                                        if (baos != null)
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        if (baos != null)
                                            mImageData = baos.toByteArray();
//                                        FaceDetector detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(),3);
//                                        FaceDetector.Face[] faces = new FaceDetector.Face[3];
//                                        int detectorResult=detector.findFaces(bitmap, faces);
//                                        Log.e("人脸检测结果返回情况",detectorResult+"");
                                        myHandler.sendEmptyMessage(TO_FACE_AUTHENTICATION);
                                    }
                                }
                            }
                        });
                    }
                    break;
            }
        }

        //"4125405030"
        private void FaceRecognition(final String groupId) {
            FaceAuthenticationUtils.getInstance(weakReference.get()).verificationFace(mImageData, groupId);
            FaceAuthenticationUtils.getInstance(weakReference.get()).setOnVertifyFaceListener(new VertifyFaceListener() {
                @Override
                public void onResult(IdentityResult result, boolean islast) {
                    if (null == result) {
                        myHandler.sendEmptyMessage(TO_FACE_AUTHENTICATION);
                        return;
                    }
                    try {
                        String resultStr = result.getResultString();
                        JSONObject resultJson = new JSONObject(resultStr);
                        if (ErrorCode.SUCCESS == resultJson.getInt("ret")) {//此处检验百分比
                            JSONArray scoreList = resultJson.getJSONObject("ifv_result").getJSONArray("candidates");
                            Timber.e(scoreList.toString());
                            String scoreFirstXfid = scoreList.getJSONObject(0).optString("user");
                            Timber.e("最高分数的讯飞id" + scoreFirstXfid);
                            final double firstScore = scoreList.getJSONObject(0).optDouble("score");
                            if (firstScore > 80) {
                                if ("Test".equals(fromString) || "Welcome".equals(fromString)) {
                                    authenticationSuccessForTest$Welcome(scoreFirstXfid, groupId);
                                } else if ("Pay".equals(fromString)) {
                                    if (mAuthid.equals(scoreFirstXfid) && !isOnPause) {
                                        paySuccess();
                                    } else {
                                        payFail();
                                    }
                                }

                            } else {
                                if (firstScore > 30) {
                                    authenticationNum = 0;
                                    ToastUtils.showShort("请将您的面孔靠近摄像头，再试一次");
                                    myHandler.sendEmptyMessageDelayed(TO_CAMERA_PRE_RESOLVE, 1000);
                                } else {
                                    ToastUtils.showLong("匹配度" + String.format("%.2f", firstScore) + "%,验证不通过!");
                                    finishActivity();
                                }
                            }
                        } else {
                            ToastUtils.showShort("识别失败");
                            finishActivity();
                        }
                    } catch (JSONException e) {
                        Timber.e(e, "验证失败");
                    }
                }

                @Override
                public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                    tvTips.setText("努力验证中...");
                }

                @Override
                public void onError(SpeechError error) {
                    Timber.e(error, "验证出错");
                    if (authenticationNum < 5) {
                        authenticationNum++;
                        ToastUtils.showShort("第" + Utils.getChineseNumber(authenticationNum) + "次验证失败");
//                                    myHandler.sendEmptyMessage(2);
//                                    myHandler.sendEmptyMessageDelayed(1, 2000);
                        myHandler.sendEmptyMessageDelayed(TO_CAMERA_PRE_RESOLVE, 1000);
                    } else {
                        finishActivity();
                    }
                }
            });
        }
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("人 脸 识 别");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(FaceLoginActivity.this, WifiConnectActivity.class));
            }
        });
        ActivityHelper.addActivity(this);

    }


    /**
     * 通过验证
     *
     * @param scoreFirstXfid
     */
    private void authenticationSuccessForTest$Welcome(String scoreFirstXfid, String groupId) {
        String[] split = scoreFirstXfid.split("_");
        LocalShared.getInstance(this).setUserId(split[1]);
        LocalShared.getInstance(BaseApp.app).setUserId(split[1]);

//        new JpushAliasUtils(this).setAlias("user_" + split[1]);

        NetworkApi.loginByXunFei(scoreFirstXfid, groupId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (response != null) {
                        Gson gson = new Gson();
                        PersonInfoResultBean bean = gson.fromJson(response.body(), PersonInfoResultBean.class);
                        if (bean != null) {
                            if (bean.tag) {
//                                startActivity(new Intent(FaceLoginActivity.this, InquiryAndFileActivity.class));
                                finish();
                            } else {
                                T.show("网络繁忙请稍后重试");
                            }

                        }
                    }

                } catch (Exception e) {

                }

            }
        });

    }

    /**
     * 支付成功
     */
    private void paySuccess() {
        NetworkApi.pay_status(LocalShared.getInstance(BaseApp.app).getUserId(), Utils.getDeviceId(), orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                setResult(RESULT_OK);
                finishActivity();
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }

    private void payFail() {
        NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                mlSpeak("主人，验证不通过，请重新验证");
                ToastUtils.showShort("验证不通过");
                finish();

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);
        //工厂测试专用
        intTitle();
        isTest = getIntent().getBooleanExtra("isTest", false);
        init();

        if (!"Pay".equals(fromString)) {
//            mTiaoguo.performClick();
//            return;
        }
        openCameraPreview();
        if (isTest) {
            openAnimation();
        } else {
//            joinGroup();
        }
        setClick();
        openAnimation();
//        getAllUsersInfo();
    }

    private void joinGroup() {
        String groupid = LocalShared.getInstance(this).getGroupId();
        String firstXfid = LocalShared.getInstance(this).getGroupFirstXfid();
        final String currentXfid = LocalShared.getInstance(this).getXunfeiId();
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, currentXfid);
        FaceAuthenticationUtils.getInstance(FaceLoginActivity.this).setOnJoinGroupListener(new JoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                tvTips.setText("请将人脸对准识别框");
                openAnimation();
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                    createGroup(currentXfid);
                } else {
                    openAnimation();
                }

            }
        });
    }

    private void createGroup(final String xfid) {
        FaceAuthenticationUtils.getInstance(this).createGroup(xfid);
        FaceAuthenticationUtils.getInstance(this).setOnCreateGroupListener(new CreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    LocalShared.getInstance(FaceLoginActivity.this).setGroupId(groupId);
                    LocalShared.getInstance(FaceLoginActivity.this).setGroupFirstXfid(xfid);
                    joinGroup();
                    FaceAuthenticationUtils.getInstance(FaceLoginActivity.this).updateGroupInformation(groupId, xfid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "创建组失败");
            }
        });
    }

    private void openAnimation() {
        lottAnimation.playAnimation();
    }

    private void closeAnimation() {
        lottAnimation.reverseAnimation();
        findViewById(R.id.iv_circle).clearAnimation();
    }

    private void getAllUsersInfo() {
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                String[] accounts = LocalShared.getInstance(FaceLoginActivity.this).getAccounts();
                if (accounts == null) {
                    return;
                }
                StringBuilder mAccountIdBuilder = new StringBuilder();
                for (String item : accounts) {
                    mAccountIdBuilder.append(item.split(",")[0]).append(",");
                }
                NetworkApi.getAllUsers(mAccountIdBuilder.substring(0, mAccountIdBuilder.length() - 1),
                        new NetworkManager.SuccessCallback<ArrayList<UserInfoBean>>() {
                            @Override
                            public void onSuccess(ArrayList<UserInfoBean> response) {
                                if (response == null) {
                                    return;
                                }
                                mDataList = response;
                            }
                        });
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        fromString = intent.getStringExtra("from");
        fromType = intent.getStringExtra("fromType");
        mAuthid = LocalShared.getInstance(this).getXfId();
        mTiaoguo = (Button) findViewById(R.id.tiao_guos);
        if ("Pay".equals(fromString) || "Welcome".equals(fromString)) {//支付过来
            mTiaoguo.setVisibility(View.GONE);
        }
        if ("Test".equals(fromString)) {
            mTiaoguo.setVisibility(View.VISIBLE);
        }
        mPreviewSurface = findViewById(R.id.sfv_preview);
        rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_face_check);
        baos = new ByteArrayOutputStream();
        myHandler = new FaceLoginActivity.MyHandler(this);
        tvTips = findViewById(R.id.tv_tip);
        lottAnimation = findViewById(R.id.lott_animation);
        lottAnimation.setImageAssetsFolder("lav_imgs/");
        lottAnimation.setAnimation("camera_pre.json");
    }


    private void openCameraPreview() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        mPreviewSurface.setLayoutParams(params);
        holder = mPreviewSurface.getHolder();
        holder.addCallback(callback);


    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Timber.e("getHolder().addCallback所在线程");
            Handlers.bg().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                        if (mCamera == null) {
                            runOnUiThreadWithOpenCameraFail();
                            return;
                        }
                        Camera.Parameters params = mCamera.getParameters();
                        params.setPreviewFormat(ImageFormat.NV21);
                        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
                        mCamera.setParameters(params);
                        setCameraDisplayOrientation(FaceLoginActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
                        mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
                        mCamera.startPreview();
                        mCamera.cancelAutoFocus();//对焦

                    } catch (Exception e) {
                        runOnUiThreadWithOpenCameraFail();
                    }
                }
            });

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            finishActivity();
        }
    };

    private void runOnUiThreadWithOpenCameraFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort("启动相机失败");
                finishActivity();
            }
        });
    }

    private void setClick() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        mTiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
        lottAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束
                if (openOrcloseAnimation) {
                    myHandler.sendEmptyMessage(TO_CAMERA_PRE_RESOLVE);
                    openOrcloseAnimation = false;
                }
                Timber.e("动画结束");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 设置预览图像的方向
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(Activity activity,
                                             int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private int result;

    /**
     * NV21格式(所有相机都支持的格式)转换为bitmap
     */
    public Bitmap decodeToBitMap(byte[] data, Camera mCamera) {
        if (mCamera == null) {
            return null;
        }
        try {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                baos.reset();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
                Bitmap bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
                Matrix matrix = new Matrix();
                switch (result) {
                    case 90:
                        matrix.postRotate(270);
                        break;
                    case 270:
                        matrix.postRotate(90);
                        break;
                    default:
                        matrix.postRotate(result);
                        break;
                }
//                matrix.postRotate(0);
                Bitmap nbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                return nbmp;
            }
        } catch (Exception ex) {
            Log.e("tag", "Error:" + ex.getMessage());
        }
        return null;
    }

    private void finishActivity() {
        mImageData = null;
        myHandler.removeCallbacksAndMessages(null);
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                if (null != mCamera) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    if (holder != null) {
                        holder.removeCallback(callback);
                    }
                }
                if (baos != null) {
                    try {
                        baos.close();
                        baos = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
    }

    private long currentTimeWithLong;

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        currentTimeWithLong = System.currentTimeMillis();
        closeAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.e("onDestroy");
        Handlers.bg().removeCallbacksAndMessages(null);
        if (lottAnimation != null)
            lottAnimation.cancelAnimation();
        Log.e("从onPause到onDestroy时间", System.currentTimeMillis() - currentTimeWithLong + "");
    }

}
