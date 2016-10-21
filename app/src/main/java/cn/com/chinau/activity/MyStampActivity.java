package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.MyStampViewPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.MyStampGridViewBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 我的邮集页面
 */
public class MyStampActivity extends BaseActivity implements View.OnClickListener {

    private View mMyStampTitle, mMyStampContent;
    private ImageView mBack, mMore;
    private GridView mStampGV;
    private ArrayList<MyStampGridViewBean.StampList> mList;
    private View MorePopView;
    private PopupWindow mMorePop;
    private TextView mEdit, mShared;// 编辑，分享
    //这个标记是编辑状态下的标记
    private boolean flag;

    // popup的背景
    private ColorDrawable mColorBg = new ColorDrawable(00000000);
    //viewpager的对象
    private ViewPager myStampViewPager;
    //数据集合
    private List<View> mViewPagerList;
    private int pageSize; //每页的数量
    private int pageCount;//总共的页数
    private LayoutInflater layoutInflater;
    private SharedPreferences sp;
    private String mToken,mUser_id,result;
    private int num = 0;//初始索引
    private Handler mHandler = new Handler() {

        private String mTotal;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 我的邮集
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    MyStampGridViewBean mOrderSweepBean = gson.fromJson(msge, MyStampGridViewBean.class);
                    String mRsp_code = mOrderSweepBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mOrderSweepBean.getStamp_list();
                        MyLog.LogShitou("我的邮集有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            // 总价值
                            mTotal = mOrderSweepBean.getTotal_amount();
                            mContentTotal.setText("￥"+mTotal);
                            //初始化GridView的集合
                            mViewPagerList = new ArrayList<>();
                            //获取Activity的LayoutInflater
                            layoutInflater = getLayoutInflater();

                            //计算每页最大显示个数，2行4列
                            pageSize = getResources().getInteger(R.integer.PagerCount) * 2;
                            //总共的页数 总数/每页数量，并取整。
                            pageCount = (int) Math.ceil(mList.size() * 1.0 / pageSize);

                            //使用For循环创建页面,并添加到集合中去
                            for (int i = 0; i < pageCount; i++) {
                                //每个页面都是inflate出一个新实例
                                //这里面传的是GridView的布局
                                GridView grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
                                //给GridView设置Adapter，传入index
                                grid.setAdapter(new GridViewAdapter(MyStampActivity.this, mList, i, mBitmap, flag));
                                //加入到ViewPager的View数据集中
                                mViewPagerList.add(grid);
                            }

                            initAdapter();

                        }
                    }

                    break;

            }
        }
    };
    private TextView mContentTotal;

    @Override
    public View CreateTitle() {
        mMyStampTitle = View.inflate(this, R.layout.activity_mystamp_title, null);
        return mMyStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mMyStampContent = View.inflate(this, R.layout.activity_mystamp_content, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initData();
//        initAdapter();
        initListener();
        return mMyStampContent;
    }


    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mBack = (ImageView) mMyStampTitle.findViewById(R.id.base_title_back);
        mMore = (ImageView) mMyStampTitle.findViewById(R.id.base_more);
        //初始化ViewPager
        myStampViewPager = (ViewPager) mMyStampContent.findViewById(R.id.my_content_Viewpager);
        mContentTotal = (TextView) mMyStampContent.findViewById(R.id.my_content_total);

    }

    private void initData() {
        GetInitNet(num);// 我的邮集列表网络请求方法
//        mStampGV = (GridView) mMyStampContent.findViewById(R.id.myStamp_gv);
        //初始化数据的集合，一共20条
//        mList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            mList.add(new MyStampGridViewBean.StampList("庚申年", "" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
//        }

//        //初始化GridView的集合
//        mViewPagerList = new ArrayList<>();
//        //获取Activity的LayoutInflater
//        layoutInflater = getLayoutInflater();
//
//        //计算每页最大显示个数，2行4列
//        pageSize = getResources().getInteger(R.integer.PagerCount) * 2;
//        //总共的页数 总数/每页数量，并取整。
//        pageCount = (int) Math.ceil(mList.size() * 1.0 / pageSize);
//
//        //使用For循环创建页面,并添加到集合中去
//        for (int i = 0; i < pageCount; i++) {
//            //每个页面都是inflate出一个新实例
//            //这里面传的是GridView的布局
//            GridView grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
//            //给GridView设置Adapter，传入index
//            grid.setAdapter(new GridViewAdapter(this, mList, i, mBitmap, flag));
//
//            //加入到ViewPager的View数据集中
//            mViewPagerList.add(grid);
//        }



    }

    private void initAdapter() {

        //设置翻页效果
                myStampViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position <= 0) {
                    page.setPivotX(page.getMeasuredWidth());
                    page.setPivotY(0);
                    page.setScaleX(1 + position);
                } else if (position <= 1) {
                    page.setPivotX(0);
                    page.setPivotY(0);
                    page.setScaleX(1 - position);
                }
            }
        });
        myStampViewPager.setAdapter(new MyStampViewPagerAdapter(mViewPagerList));
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_more://更多按钮
                MorePopupWindow();
                break;
        }
    }

    /**
     * 点击更多
     */
    private void MorePopupWindow() {
        // 判断选择性别弹出的View是否为空
        if (MorePopView == null) {
            MorePopView = View.inflate(this, R.layout.mystamp_dialog, null);
        }

        if (mMorePop == null) {
            mMorePop = new PopupWindow(MorePopView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        // 为了响应返回键和界面外的其他界面
        mMorePop.setBackgroundDrawable(mColorBg);
        //把他改为屏幕中间了
        mMorePop.showAsDropDown(mMore, mMore.getWidth() / 2, 0);
        mMorePop.setOutsideTouchable(true);

        mEdit = (TextView) MorePopView.findViewById(R.id.mystamp_edit);
        mShared = (TextView) MorePopView.findViewById(R.id.mystamp_shared);
        // 编辑
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditClickDispose();
            }
        });
        // 分享
        mShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(SharedActivity.class);
                mMorePop.dismiss();
            }
        });
    }

    /**
     * 编辑按钮的处理方法
     */
    private void EditClickDispose() {
        mMorePop.dismiss();
        flag = !flag;
        if (flag) {//编辑状态
            mEdit.setText("完成");
        } else {//非编辑状态
            mEdit.setText("编辑");
        }
        mViewPagerList.clear();
        //使用For循环创建页面,并添加到集合中去
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            //这里面传的是GridView的布局
            GridView grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
            //给GridView设置Adapter，传入index
            grid.setAdapter(new GridViewAdapter(this, mList, i, mBitmap, flag));
            //加入到ViewPager的View数据集中
            mViewPagerList.add(grid);
        }
        myStampViewPager.setAdapter(new MyStampViewPagerAdapter(mViewPagerList));
    }


    /**
     * 我的邮集列表网络请求
     * @param num 初始化索引
     */
    private void GetInitNet(final int num){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ALBUMLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("我的邮集List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        });
    }

    /**
     * 修改邮集网络请求
     */
    private void UpDateGetInitNet(final String stamp_count,final String op_type){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.MODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.STAMP_SN, "");//  邮票编号 （暂时为空）
                params.put(StaticField.STAMP_COUNT, stamp_count);//  邮票数量
                params.put(StaticField.OP_TYPE, op_type);//  操作类型：SC：删除；JR加入；XG修改
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("我的邮集List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
//                Message msg = mHandler.obtainMessage();
//                msg.what = StaticField.SUCCESS;
//                msg.obj = result;
//                mHandler.sendMessage(msg);

            }
        });
    }

}
