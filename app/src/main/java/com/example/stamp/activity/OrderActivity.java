package com.example.stamp.activity;

import android.view.View;
import android.widget.Button;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.utils.MyLog;

import java.util.Random;

/**
 * 回购订单页面
 */
public class OrderActivity extends BaseActivity {

    private View mOrderTitle;
    private View mOrderContent;


    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order, null);
        initView();
        return mOrderContent;
    }

    private void initView() {
        Button btn = (Button) mOrderContent.findViewById(R.id.order);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int ran = random.nextInt(5);
                MyLog.e(ran + "");
                if (ran == 0) {
                    openActivityWitchAnimation(OrderAuditingActivity.class);
                } else if (ran == 1) {
                    openActivityWitchAnimation(OrderFinishActivity.class);
                } else if (ran == 2) {
                    openActivityWitchAnimation(OrderCloseActivity.class);
                } else if (ran == 3) {
                    openActivityWitchAnimation(OrderRejectActivity.class);
                } else {
                    openActivityWitchAnimation(OrderWaitActivity.class);
                }
            }
        });

        Button btn1 = (Button) mOrderContent.findViewById(R.id.order_logisticsDetails);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityWitchAnimation(OrderDetailsActivity.class);
            }
        });
    }

    @Override
    public void AgainRequest() {

    }
}
