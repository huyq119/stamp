package cn.com.chinau.fragment.stampdetailfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import cn.com.chinau.R;
import cn.com.chinau.utils.MyLog;

/**
 * 邮市详情页Fragment 邮票信息
 */
public class StampDetailInfoFragment extends Fragment {

    private Context context;
    private String H5url;
    private WebView mWebView;

    public StampDetailInfoFragment(Context context,String H5url) {
        this.context = context;
        this.H5url = H5url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mStampInfo = View.inflate(getActivity(), R.layout.fragment_stampdetailinfo, null);
       mWebView = (WebView) mStampInfo.findViewById(R.id.Web_View);
        mWebView.loadUrl("http://test.chinau.com.cn:8090/app/dealHtmlData.do?type=DESIGNEREXTEND&sn=151");
//        SetWebView(H5url);
       MyLog.LogShitou("前面传过来的URL001-->:",H5url);
        return mStampInfo;
    }

    private void SetWebView(String H5url){
        if (mWebView != null){
//            mWebView.loadUrl(H5url);
            mWebView.loadUrl("http://test.chinau.com.cn:8090/app/dealHtmlData.do?type=DESIGNERSTAMPDESC&sn=26038086970");
         Log.e("传过来的URL001-->:",H5url);
        }
    }

}
