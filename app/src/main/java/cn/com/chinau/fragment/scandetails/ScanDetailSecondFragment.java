package cn.com.chinau.fragment.scandetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import cn.com.chinau.R;
import cn.com.chinau.bean.ScanBean;
import cn.com.chinau.view.CustWebView;

/**
 * 扫码回购底部的View
 * Created by Administrator on 2016/8/17.
 */
@SuppressLint("ValidFragment")
public class ScanDetailSecondFragment extends Fragment {

    private View mScanDetailView;
    private String result;
    private CustWebView mWebview;
    private boolean hasInited = false;
    public ScanDetailSecondFragment(String result) {
        this.result = result;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScanDetailView = View.inflate(getActivity(), R.layout.fragment_scandetail_second, null);

        mWebview = (CustWebView) mScanDetailView.findViewById(R.id.market_detail_vb);
        return mScanDetailView;
    }

    public void initView() {
        if (null != mWebview && !hasInited) {
            hasInited = true;
            Gson gson = new Gson();
            ScanBean scanBean = gson.fromJson(result, ScanBean.class);
            Log.e("是否执行", scanBean.getGoods_detail());
            mWebview.loadUrl(scanBean.getGoods_detail());
        }
    }
}
