package cn.com.chinau.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.adapter.LogisticsDetailsListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.LogisticsBean;

/**
 * 物流详情页面
 */
public class LogisticsDetailsActivity extends BaseActivity {

    private View mLogisticsDetailsTitle;
    private View mLogisticsDetailsContent;
    private ListView mLogisticsListView;//物流展示信息的ListView
    private List<LogisticsBean.Express> mList;//物流信息的集合


    @Override
    public View CreateTitle() {
        mLogisticsDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLogisticsDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mLogisticsDetailsContent = View.inflate(this, R.layout.activity_logisticsdetails, null);
        initView();
        initAdapter();
        return mLogisticsDetailsContent;
    }


    private void initView() {
        ImageView mBack = (ImageView) mLogisticsDetailsTitle.findViewById(R.id.base_title_back);
        TextView  mTitle = (TextView) mLogisticsDetailsTitle.findViewById(R.id.base_title);
        mTitle.setText("物流详情");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mLogisticsListView = (ListView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_LV);
    }

    private void initAdapter() {
        mList = new ArrayList<>();
        mList.add(new LogisticsBean.Express("15253157943   2016-03-20 08:30:43", "【北京市】北京市朝阳区望京派件员：王大锤17235674987正在为您派件"));
        mList.add(new LogisticsBean.Express("2016-03-19 18:11:23", "货物正在配送"));
        mList.add(new LogisticsBean.Express("2016-03-18 16:42:41", "货物已到达天津转运中心"));
        mList.add(new LogisticsBean.Express("2016-03-17 14:32:13", "货品已到济南货运站"));
        mList.add(new LogisticsBean.Express("2016-03-16 11:12:42", "货物已送达济南高新区站点"));


        LogisticsDetailsListViewAdapter Adapter = new LogisticsDetailsListViewAdapter(this, mList);
        mLogisticsListView.setAdapter(Adapter);
    }

    @Override
    public void AgainRequest() {

    }


}
