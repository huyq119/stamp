package cn.com.chinau.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.ScanActivity;
import cn.com.chinau.activity.ScanDetailsActivity;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.zxing.MessageIDs;
import cn.com.chinau.zxing.camera.CameraManager;
import cn.com.chinau.zxing.decoding.CaptureActivityHandler;
import cn.com.chinau.zxing.decoding.InactivityTimer;
import cn.com.chinau.zxing.view.ViewfinderView;

/**
 * 立即扫码页面
 */
public class CaptureActivity extends Activity implements Callback, View.OnClickListener {
    public static final String QR_RESULT = "RESULT";

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    CameraManager cameraManager;
    private ImageView mBack;// 返回按钮
    private TextView mTitle;
    private SharedPreferences sp;
    private String mHomeFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//          this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		  WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new
                ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));

        this.surfaceView = new SurfaceView(this);
        this.surfaceView
                .setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));
        layout.addView(this.surfaceView);
        this.viewfinderView = new ViewfinderView(this);
        this.viewfinderView.setBackgroundColor(0x00000000);
        this.viewfinderView.setLayoutParams(new
                ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        layout.addView(this.viewfinderView);

        TextView status = new TextView(this);
        RelativeLayout.LayoutParams
                params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        status.setLayoutParams(params);
        status.setBackgroundColor(0x00000000);
        status.setTextColor(0xFFFFFFFF);
        status.setText("请将条码置于取景框内扫描。");
        status.setTextSize(14.0f);

        layout.addView(status);
        setContentView(layout);

        setContentView(R.layout.activity_capture);

        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);


        mBack = (ImageView) findViewById(R.id.base_title_back);
        mBack.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.base_title);
        mTitle.setText("立即扫码");
        Bundle bundle1 = getIntent().getExtras();
        mHomeFragment = bundle1.getString("SanFragment", "");
        MyLog.LogShitou("扫码传过来的标志", mHomeFragment);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.base_title_back) {
//            finish();
//            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

            if (mHomeFragment!= null && mHomeFragment.equals("HomeFragment")) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            } else if (mHomeFragment!= null &&mHomeFragment.equals("PanStampFragment")) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            } else if (mHomeFragment!= null &&mHomeFragment.equals("StampTapFragment")) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            } else if (mHomeFragment!= null &&mHomeFragment.equals("ScanActivity")){
                Intent intent = new Intent(CaptureActivity.this, ScanActivity.class);
                startActivity(intent);
                //跳转动画
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // CameraManager.init(getApplication());
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            // CameraManager.get().openDriver(surfaceHolder);
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        showResult(obj, barcode);
    }

    private void showResult(final Result rawResult, Bitmap barcode) {

        // 获取二维码的信息,并且把数据传到商品详情界面
//         ShowDialog.showTextDialog(this, "扫码成功");
        Intent intent = new Intent(this, ScanDetailsActivity.class); // 跳转扫码详情页
        sp.edit().putString("result", rawResult.getText()).commit();

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

//        //用Bundle携带数据
//        Bundle bundle=new Bundle();
//        //传递name参数为tinyphp
//        bundle.putString("result", rawResult.getText());
//        intent.putExtras(bundle);

        MyLog.e("二维码地址-->", rawResult.toString());

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        Drawable drawable = new BitmapDrawable(barcode);
//        builder.setIcon(drawable);
//
//        builder.setTitle("类型:" + rawResult.getBarcodeFormat() + "\n 结果：" +
//                rawResult.getText());
//
//        MyLog.e("类型-->", rawResult.getBarcodeFormat().toString());
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialog.dismiss();
//                Intent intent = new Intent();
//                intent.putExtra("result", rawResult.getText());
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//        builder.setNegativeButton("重新扫描", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//              dialog.dismiss();
//                restartPreviewAfterDelay(0L);
//            }
//        });
//
//        builder.setCancelable(false);
//        builder.show();

        // Intent intent = new Intent();
        // intent.putExtra(QR_RESULT, rawResult.getText());
        // setResult(RESULT_OK, intent);
        // finish();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            try {
                AssetFileDescriptor fileDescriptor = getAssets().openFd("qrbeep.ogg");
                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                this.mediaPlayer.setVolume(0.1F, 0.1F);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}