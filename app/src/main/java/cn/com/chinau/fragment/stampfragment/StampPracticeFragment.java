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
 * 邮票故事
 */
public class StampPracticeFragment extends Fragment {

    private WebView mSWB;
    private String mStory;

    public StampPracticeFragment(String mStory) {
      this.mStory = mStory;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mStampPractice = View.inflate(getActivity(), R.layout.fragment_stamppractice, null);
        mSWB = (WebView) mStampPractice.findViewById(R.id.StampPractice_wb);
        setInfoContent(mStory);
        return mStampPractice;
    }
    public void setInfoContent(String url) {
        MyLog.e("邮票故事:" + mStory);
        mSWB.loadUrl(url);
    }

}
