package cn.com.chinau.fragment.stampfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.chinau.R;

/**
 * 价格行情
 */
public class StampPriceFragment extends Fragment {


    public StampPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mStampPrice = View.inflate(getActivity(), R.layout.fragment_price, null);
        return mStampPrice;
    }

}
