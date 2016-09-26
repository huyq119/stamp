package cn.com.chinau.fragment.stampdetailfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import cn.com.chinau.R;

/**
 * 邮市详情页Fragment 邮票信息
 */
public class StampDetailInfoFragment extends Fragment {

    private Context context;
    private String H5url;
    private WebView mWebView;

//    public StampDetailInfoFragment(Context context,String H5url) {
//        this.context = context;
//        this.H5url = H5url;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mStampInfo = View.inflate(getActivity(), R.layout.fragment_stampdetailinfo, null);
//       mWebView = (WebView) mStampInfo.findViewById(R.id.Web_View);
//        SetWebView(H5url);
        return mStampInfo;
    }

    private void SetWebView(String H5url){
        if (mWebView != null){
            mWebView.loadUrl(H5url);
        }
    }

}
