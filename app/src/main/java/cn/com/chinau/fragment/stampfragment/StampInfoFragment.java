package cn.com.chinau.fragment.stampfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import cn.com.chinau.R;
import cn.com.chinau.utils.MyLog;

/**
 * 邮票信息页面
 */
public class StampInfoFragment extends Fragment {

    public WebView mVB;
    private View mStampInfo;
    private String mDetail;

    public StampInfoFragment(String mDetail) {
        this.mDetail = mDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStampInfo = View.inflate(getActivity(), R.layout.fragment_stamp_info, null);
        mVB = (WebView) mStampInfo.findViewById(R.id.StampInfo_wb);
        setInfoContent(mDetail);
        return mStampInfo;
    }

    public void setInfoContent(String url) {
        if (url != null)
        mVB.loadUrl(url);
        MyLog.e("邮票信息:" + mDetail + "-" + "mVB是否为空" + (mVB == null));
    }
}
