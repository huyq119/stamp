package cn.com.chinau.fragment.popfragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.SelfMallPanStampGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.SellMallPanStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 淘邮票筛选（竞拍）Fragment
 */
public class AuctionFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.SelfMallItemClick{

    private View mAuctionView;
    private int Current = -1;//当前选择的年代角标
    private SellMallPanStampGridViewOnItemClickListener mGoodsListener, mMetalListener,mItemsListener,mBrandListener;
    private View mSelfMall;
    private String mData;
    private TextView mNewChinese,mQingMin,mWaiguo,mALLclass;
    private SharedPreferences sp;
    private ArrayList<CategoryBean.Category.SubCategory> subCategory1;
    private String[] mChinese, mQingMins, mWaiguos, mAllClasss, mChineseValue, mQingminValue, mWaiGuoValue, mAllClassValue;


    public AuctionFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuctionView = View.inflate(getActivity(), R.layout.fragment_auction_filter_dialog, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, getActivity().MODE_PRIVATE);
        initView();
        GetCategoryData();
        //设置GridView的数据
        setPopupWindowData();
//        RequestNet();
        return mAuctionView;
    }

    private void initView() {
        mNewChinese = (TextView) mAuctionView.findViewById(R.id.auction_pop_newchinese_title);
        mQingMin = (TextView) mAuctionView.findViewById(R.id.auction_pop_qingmin_title);
        mWaiguo = (TextView) mAuctionView.findViewById(R.id.auction_pop_waiguo_title);
        mALLclass = (TextView) mAuctionView.findViewById(R.id.auction_pop_allClass_title);
    }

    // 获取保存在本地邮市类别数据
    private void GetCategoryData() {
        String category6 = sp.getString("Category6", "");
        MyLog.LogShitou("获取本地竞拍类别数据", category6);
        if (category6 != null) {
            Gson gson = new Gson();
            CategoryBean mCategoryBean = gson.fromJson(category6, CategoryBean.class);
            ArrayList<CategoryBean.Category> mCategory = mCategoryBean.getCategory();// 一级标题list
            CategoryBean.Category category = mCategory.get(0);
            subCategory1 = category.getSubCategory();// 二级标题list

            int sub = subCategory1.size();// 获取subCategory1的个数
            MyLog.LogShitou("-==============sub", "sub==" + sub);

            String[] mArrTitle = new String[sub];// 一级分类
            //二级分类
            ArrayList<String[]> mArrList = new ArrayList<>();
            ArrayList<String[]> mArrListValue = new ArrayList<>();

            // 循环出一级分类的名字
            for (int i = 0; i <subCategory1.size(); i++) {
                mArrTitle[i] = subCategory1.get(i).getName();

                MyLog.LogShitou("===============竞拍一级类别0001", mArrTitle[i]);

                ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                String[] mArr = new String[subCategory.size()];
                String[] mArrValue = new String[subCategory.size()];
                // 循环出二级分类名字
                for (int j = 0; j < subCategory.size(); j++) {
                    mArr[j] = subCategory.get(j).getName();
                    mArrValue[j] = subCategory.get(j).getValue();
                    MyLog.LogShitou("竞拍二级类别0", mArr[j] + "===" + mArrValue[j]);
                }
                mArrList.add(mArr);
                mArrListValue.add(mArrValue);
            }
            // 获取单个的一级标题Title
            String title0 = mArrTitle[0];
            mNewChinese.setText(title0);
            String title1 = mArrTitle[1];
            mQingMin.setText(title1);
            String title2 = mArrTitle[2];
            mWaiguo.setText(title2);
            String title3 = mArrTitle[3];
            mALLclass.setText(title3);
            MyLog.LogShitou("竞拍一级类别----->:", title0 + "--" + title1 + "--" + title2 + "--" + title3);

            // 获取二级分类name
            mChinese = mArrList.get(0);
            mQingMins = mArrList.get(1);
            mWaiguos = mArrList.get(2);
            mAllClasss = mArrList.get(3);
            // 获取二级分类value值
            mChineseValue = mArrListValue.get(0);
            mQingminValue = mArrListValue.get(1);
            mWaiGuoValue = mArrListValue.get(2);
            mAllClassValue = mArrListValue.get(3);

        }
    }




    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //新中国的GridView
        NoScrollGridView mNewChinesGV = (NoScrollGridView) mAuctionView.findViewById(R.id.auction_pop_newchinese);
        //清民区邮票的GridView
        NoScrollGridView mQingMinGV = (NoScrollGridView) mAuctionView.findViewById(R.id.auction_pop_qingmin);
        //外国的GridView
        NoScrollGridView mWaiGuoGV = (NoScrollGridView) mAuctionView.findViewById(R.id.auction_pop_waiguo);
        //的GridView
        NoScrollGridView mAllClassGV = (NoScrollGridView) mAuctionView.findViewById(R.id.auction_pop_allClass);

        //创建适配器
        SelfMallPanStampGridViewAdapter mNewChineseAdapter =  new SelfMallPanStampGridViewAdapter(getActivity(), mChinese);
        SelfMallPanStampGridViewAdapter mQingMinAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mQingMins);
        SelfMallPanStampGridViewAdapter mWaiGuoAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mWaiguos);
        SelfMallPanStampGridViewAdapter mAllClassAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mAllClasss);

        //设置适配器
        mNewChinesGV.setAdapter(mNewChineseAdapter);
        mQingMinGV.setAdapter(mQingMinAdapter);
        mWaiGuoGV.setAdapter(mWaiGuoAdapter);
        mAllClassGV.setAdapter(mAllClassAdapter);

        //设置监听
        //竞品邮集的监听
        mGoodsListener = new SellMallPanStampGridViewOnItemClickListener(Current, mNewChineseAdapter);
        mGoodsListener.setSelfMallItemClick(this);
        mNewChinesGV.setOnItemClickListener(mGoodsListener);
        //贵金属邮票的监听
        mMetalListener = new SellMallPanStampGridViewOnItemClickListener(Current, mQingMinAdapter);
        mMetalListener.setSelfMallItemClick(this);
        mQingMinGV.setOnItemClickListener(mMetalListener);
        //邮品的监听
        mItemsListener = new SellMallPanStampGridViewOnItemClickListener(Current, mWaiGuoAdapter);
        mItemsListener.setSelfMallItemClick(this);
        mWaiGuoGV.setOnItemClickListener(mItemsListener);
        //品牌的监听
        mBrandListener = new SellMallPanStampGridViewOnItemClickListener(Current, mAllClassAdapter);
        mBrandListener.setSelfMallItemClick(this);
        mAllClassGV.setOnItemClickListener(mBrandListener);

        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                MyLog.e("ThreeMallFilterFragment-->");
                mGoodsListener.setPosition();
                mMetalListener.setPosition();
                mItemsListener.setPosition();
                mBrandListener.setPosition();
            }
        });
    }

    @Override
    public void GetClickItem() {
        int GoodsNum = mGoodsListener.getPosition();
        int MetalNum = mMetalListener.getPosition();
        int ItemsNum = mItemsListener.getPosition();
        int BrandNum = mBrandListener.getPosition();
        MyLog.e(GoodsNum + "-" + MetalNum + "-" + ItemsNum+"-"+BrandNum);


        String Goods = (GoodsNum == -1) ? "" : mChineseValue[GoodsNum];
        String Metal = (MetalNum == -1) ? "" : mQingminValue[MetalNum];
        String Items = (ItemsNum == -1) ? "" : mWaiGuoValue[ItemsNum];
        String Brand = (BrandNum == -1) ? "" : mAllClassValue[BrandNum];

        mData = Goods + "," + Metal + "," + Items+","+Brand;
        setData(mData);
        MyLog.e("点击了啥002--->"+mData);

    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

}
