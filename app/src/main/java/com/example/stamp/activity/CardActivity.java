package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 银行卡页面
 *
 */
public class CardActivity extends BaseActivity implements View.OnClickListener {


    private View mAddCardTitle;
    private View mAddCardContent;
    private ImageView mBack;
    private TextView mAddCard;
    private LinearLayout mNoSetCard;//没有设置银行卡View
    private LinearLayout mSetCard;//设置银行卡View
    private TextView mUnBundling;//解绑
    private TextView mRevise;//修改

    private boolean flag = true;//只作为一个标记,之后要删除的

    @Override
    public View CreateTitle() {
        mAddCardTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAddCardTitle;
    }

    @Override
    public View CreateSuccess() {
        //这个是银行卡页面,这里是把修改和绑定设置在一个页面根据进来的时候的一个标记判断
        mAddCardContent = View.inflate(this, R.layout.activity_card, null);
        initView();
        initData();
        initListener();
        return mAddCardContent;
    }




    private void initView() {
        TextView Title = (TextView) mAddCardTitle.findViewById(R.id.base_title);
        Title.setText("添加银行卡");
        mSetCard = (LinearLayout) mAddCardContent.findViewById(R.id.card_SetCard);
        mNoSetCard = (LinearLayout) mAddCardContent.findViewById(R.id.card_noSetCard);
        mBack = (ImageView) mAddCardTitle.findViewById(R.id.base_title_back);
        mAddCard = (TextView) mAddCardContent.findViewById(R.id.addCard_add);
        mUnBundling = (TextView) mAddCardContent.findViewById(R.id.card_UnBundling);
        mRevise = (TextView) mAddCardContent.findViewById(R.id.card_revise);
    }

    private void initData() {
        //判断显示那个页面
        mSetCard.setVisibility(isSetCard()?View.VISIBLE: View.GONE);
        mNoSetCard.setVisibility(isSetCard()?View.GONE: View.VISIBLE);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mAddCard.setOnClickListener(this);
        mUnBundling.setOnClickListener(this);
        mRevise.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCard_add://添加银行卡
                //这里要判断是否设置了初始密码
                if(flag){

                }else{
                    openActivityWitchAnimation(ReviseCardActivity.class);
                }
                break;
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.card_UnBundling://解绑银行卡
                //解绑银行卡页面
                openActivityWitchAnimation(UnBundlingActivity.class);
                break;
            case R.id.card_revise://修改银行卡
                //修改银行卡页面
                openActivityWitchAnimation(ReviseCardActivity.class);
                break;
        }
    }

    /**
     * 是否设置银行卡
     * @return true设置了银行卡,false没有设置银行卡
     */
    public boolean isSetCard(){
        return flag;
    }
}
