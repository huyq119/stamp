package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.AuctionRegulationsAdapter;
import cn.com.chinau.bean.SysParamQueryBean;


/**
 * Created by Administrator on 2016/8/31.
 * 竞拍出价规则与协议Dialog
 */
public class AuctionRegulationsAgreementDialog extends Dialog{
    private SharedPreferences sp;
    private ListView mListView;
    private Context context;
    private ArrayList<SysParamQueryBean.Sys_param_value.Auction_rule> mList;

    public AuctionRegulationsAgreementDialog(Context context) {
        super(context, R.style.exitdialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_agreement);
        sp = getContext().getSharedPreferences(StaticField.NAME,Context.MODE_PRIVATE);
        initView();
        // 设置点击外部窗口不关闭Dialog
        this.setCanceledOnTouchOutside(false);
    }

    private void initView(){
        String mData = sp.getString("System","");

        Gson gsons = new Gson();
        SysParamQueryBean paramQueryBean = gsons.fromJson(mData, SysParamQueryBean.class);
        SysParamQueryBean.Sys_param_value sys_param_value = paramQueryBean.getSys_param_value();
        // 竞拍规则
        mList = sys_param_value.getAuction_rule();
        mListView = (ListView) findViewById(R.id.regulations_listview);
        mListView.setEnabled(false);// ListView不可点击
        initAdapter();

    }

    private void initAdapter(){
        AuctionRegulationsAdapter mAdapter = new AuctionRegulationsAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
