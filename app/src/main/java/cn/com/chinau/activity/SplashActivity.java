package cn.com.chinau.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import cn.com.chinau.MainActivity;
import cn.com.chinau.R;
import cn.com.chinau.utils.MyHandler;


public class SplashActivity extends Activity {


    private static final int MAIN = 0;
    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(MAIN, 2000);
    }

}
