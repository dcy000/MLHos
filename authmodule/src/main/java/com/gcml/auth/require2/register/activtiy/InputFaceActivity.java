package com.gcml.auth.require2.register.activtiy;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.gcml.auth.R;
import com.gcml.auth.require2.bean.GetUserXFInfoBean;
import com.gcml.auth.require2.dialog.AffirmHeadDialog;
import com.gcml.common.data.UserInfoBean;
import com.gcml.common.oldnet.NetworkApi;
import com.gcml.common.oldnet.NetworkManager;
import com.gcml.common.utils.localdata.LocalShared;
import com.gcml.lib_common.base.old.BaseActivity;
import com.gcml.lib_common.util.business.Utils;
import com.gcml.lib_common.util.common.ActivityHelper;
import com.gcml.lib_common.util.common.Handlers;
import com.gcml.lib_common.util.common.T;
import com.google.gson.Gson;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class InputFaceActivity extends BaseActivity implements AffirmHeadDialog.ClickListener {
    private String registerIdCardNumber;
    private String registerPhoneNumber;
    private String registeRrealName;
    private String registerSex;
    private String registerAddress;

    SurfaceView surfaceView;
    ImageView ivCircle;

    private Camera mCamera;
    private SurfaceHolder holder;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    ;

    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private static int PREVIEW_WIDTH = 1280;
    private static int PREVIEW_HEIGHT = 720;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_face);
        initTitle();
        initData();
        ActivityHelper.addActivity(this);
    }

    private byte[] faceData;
    private Camera.PreviewCallback cb = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.e("测试该方法调用的次数", "onPreviewFrame: ");
//            dealFaceData(data, camera);
            showHeadDialog(data, camera);
        }
    };

    private void showHeadDialog(byte[] data, Camera camera) {
        try {
            Bitmap b3 = decodeToBitMap(data, camera);
            if (b3 != null) {
                stream.reset();
                Bitmap bitmap = Utils.centerSquareScaleBitmap(b3, 300);
                //可根据流量及网络状况对图片进行压缩
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                faceData = bytes;
            }

            AffirmHeadDialog dialog = new AffirmHeadDialog(faceData);
            dialog.setListener(this);
            dialog.show(getFragmentManager(), "headDialog");
        } catch (Exception e) {

        }
    }

    @Override
    public void onConfirm() {
//上传头像 注册
        uploadHeadToSelf();
    }

    @Override
    public void onCancel() {
        MLVoiceSynthetize.startSynthesize(InputFaceActivity.this,
                "让我为您拍个照，3，2，1，茄子。",
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        try {
                            if (mCamera != null) {
                                mCamera.reconnect();
                                mCamera.setOneShotPreviewCallback(cb);
                                mCamera.startPreview();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                },
                false);
    }

    private UploadManager uploadManager = new UploadManager();

    private void uploadHeadToSelf() {
        NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Date date = new Date();
                SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                StringBuilder str = new StringBuilder();//定义变长字符串
                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    str.append(random.nextInt(10));
                }
                //将字符串转换为数字并输出
                String key = simple.format(date) + str + ".jpg";

                uploadManager.put(faceData, key, response, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            String stringExtra = getIntent().getStringExtra(OVERHEAD_INFORMATION);
                            //录入头像跳转页面的逻辑
                            if (!TextUtils.isEmpty(stringExtra)) {
                                toOtherPages(stringExtra, imageUrl);
                                finish();
                                return;
                            }
                            //注册的逻辑
                            signUp(imageUrl);
                        }
                    }
                }, null);
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    private void toOtherPages(String stringExtra, String url) {
      /*  Intent intent = new Intent(this, DetectActivity.class);
        //====附加的头像url===开始===//
        intent.putExtra("detectHeadIcon", url);
        //====附加的头像url===结束===//
        switch (stringExtra) {
            case "healthDetect":
                intent.putExtra("type", "wendu");
                intent.putExtra("isDetect", true);
                intent.putExtra("detectCategory", "detectHealth");
                startActivity(intent);
                break;
            case "hypertensionFollowUp":
                intent.putExtra("type", "xueya");
                intent.putExtra("isDetect", true);
                intent.putExtra("detectCategory", "detectPressure");
                startActivity(intent);
                break;
            case "hyperglycemiaFollowUp":
                intent.putExtra("type", "xueya");
                intent.putExtra("isDetect", true);
                intent.putExtra("detectCategory", "detectSugar");
                startActivity(intent);
                break;
            case "zhongYiTiZhi":
                startActivity(new Intent(this, OlderHealthManagementSerciveActivity.class).
                        putExtra("detectHeadIcon", url));
                break;
        }*/

    }


    private String TAG = "InputFaceActivity";
    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
//            mCamera = getCameraInstance();
//            if (mCamera == null) {
//                T.show("打开摄像机失败");
//                return;
//            }
//            try {
//                mCamera.setPreviewDisplay(holder);
//                mCamera.startPreview();
//            } catch (IOException e) {
//                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
//            }
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
//                        setCameraDisplayOrientation(InputFaceActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
                        mCamera.setPreviewDisplay(surfaceView.getHolder());
                        mCamera.startPreview();
                        mCamera.cancelAutoFocus();//对焦

                    } catch (Exception e) {
                        runOnUiThreadWithOpenCameraFail();
                    }
                }
            });


        }


        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
//            int rotation = getDisplayOrientation(); //获取当前窗口方向
//            mCamera.setDisplayOrientation(rotation); //设定相机显示方向
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//            if (mCamera == null || holder == null) {
//                return;
//            }
//            holder.removeCallback(callback);
//            mCamera.setPreviewCallback(null);
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
            runOnUiThreadWithOpenCameraFail();
        }
    };

    private void runOnUiThreadWithOpenCameraFail() {
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {

                try {
                    if (null != mCamera) {
                        mCamera.setPreviewCallback(null);
                        mCamera.stopPreview();
                        mCamera.release();
                        mCamera = null;
                        if (holder != null) {
                            holder.removeCallback(callback);
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }


    public static final String REGISTER_IDCARD_NUMBER = "registerIdCardNumber";
    public static final String REGISTER_PHONE_NUMBER = "registerPhoneNumber";
    public static final String REGISTER_REAL_NAME = "registeRrealName";
    public static final String REGISTER_SEX = "registerSex";
    public static final String REGISTER_ADDRESS = "registerAddress";
    public static final String OVERHEAD_INFORMATION = "overHeadInformation";

    private void initData() {
        registerIdCardNumber = getIntent().getStringExtra(REGISTER_IDCARD_NUMBER);
        registerPhoneNumber = getIntent().getStringExtra(REGISTER_PHONE_NUMBER);
        registeRrealName = getIntent().getStringExtra(REGISTER_REAL_NAME);
        registerSex = getIntent().getStringExtra(REGISTER_SEX);
        registerAddress = getIntent().getStringExtra(REGISTER_ADDRESS);
    }

    private void initTitle() {
        surfaceView = findViewById(R.id.surface_view);
        ivCircle = findViewById(R.id.iv_circle);

        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("人 脸 录 入");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InputFaceActivity.this, WifiConnectActivity.class));
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onResume() {
        super.onResume();
        try {
            holder = surfaceView.getHolder();
            holder.addCallback(callback);
        } catch (Exception e) {

        }

        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                MLVoiceSynthetize.startSynthesize(InputFaceActivity.this,
                        "让我为您拍个照，3，2，1，茄子。",
                        new MLSynthesizerListener() {
                            @Override
                            public void onCompleted(SpeechError speechError) {
                                try {
                                    if (mCamera != null) {
                                        mCamera.reconnect();
                                        mCamera.setOneShotPreviewCallback(cb);
                                        mCamera.startPreview();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        false);

            }
        }, 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {

        }
    }


    // 获取camera实例
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d("TAG", "camera is not available");
        }
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            params.setPreviewFormat(ImageFormat.NV21);
            params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            camera.setParameters(params);
        }
        return camera;

    }

    // 刷新相机
    private void refreshCamera() {
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    public Bitmap decodeToBitMap(byte[] data, Camera mCamera) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        Bitmap bmp = null;
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                stream.reset();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                Matrix matrix = new Matrix();
                matrix.postRotate(0);
                //上面得到的图片旋转了270度,下面将图片旋转回来,前置摄像头270度,后置只需要90度
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                return bmp;
            }
        } catch (Exception ex) {
            Log.e("tag", "Error:" + ex.getMessage());
            return null;
        }
        return bmp;
    }

    private void signUp(String url) {
        showLoadingDialog("");
        final LocalShared shared = LocalShared.getInstance(this);
        NetworkApi.register(
                registeRrealName,
                registerSex,
                registerAddress,
                registerIdCardNumber,
                registerPhoneNumber,
                url,
                new NetworkManager.SuccessCallback<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        try {
                            initXFInfo(response.bid, faceData);
                        } catch (Exception e) {
                        }
                        shared.setUserInfo(response);
                        LocalShared.getInstance(mContext).setSex(response.sex);
                        LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                        LocalShared.getInstance(mContext).setUserAge(response.age);
                        LocalShared.getInstance(mContext).setUserHeight(response.height);
//                        new JpushAliasUtils(InputFaceActivity.this).setAlias("user_" + response.bid);
//                        startActivity(new Intent(InputFaceActivity.this, InquiryAndFileActivity.class)
//                                .putExtra("isRegister", true)
//                        );
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        T.show(message);
                        mlSpeak("主人," + message);
                        finish();
                    }
                }
        );
    }

    private void initXFInfo(String userId, final byte[] faceData) {
        NetworkApi.getUserXunFeiInfo(userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                GetUserXFInfoBean bean = new Gson().fromJson(body, GetUserXFInfoBean.class);
                if (bean != null && bean.tag && bean.data != null && bean.data.xunfeiId != null) {
//                    //vip
//                    if ("1".equals(bean.data.vipState)) {
//                        if (TextUtils.isEmpty(bean.data.currentGroup)) {
//                            addXFId2Group(bean.data.xunfeiId);
//                        }
//                    } else {
//                        //非vip
//                    }
                    registerXFInfo(bean.data.xunfeiId, faceData);

                }
            }
        });
    }

    private void registerXFInfo(String xunfeiId, byte[] data) {
        if (data == null) return;
        FaceRequest request = new FaceRequest(this);
        request.setParameter(SpeechConstant.WFR_SST, "reg");
        request.setParameter(SpeechConstant.AUTH_ID, xunfeiId);
        request.sendRequest(data, new RequestListener() {
            @Override
            public void onEvent(int i, Bundle bundle) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }
        });
    }
}
