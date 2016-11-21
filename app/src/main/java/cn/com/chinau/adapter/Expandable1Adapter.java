package cn.com.chinau.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Hashtable;

import cn.com.chinau.R;
import cn.com.chinau.bean.ShopNameBean;

/**
 * 购物车的适配器
 * Created by Administrator on 2016/8/15.
 */

public class Expandable1Adapter extends BaseExpandableListAdapter {

    private Context context;
    private BitmapUtils bitmap;

    private Hashtable<ShopNameBean.SellerBean, ArrayList<ShopNameBean.SellerBean.GoodsBean>> groupSet;

    // 选中的商品卖家账号集合
//    private static Hashtable<String, String> selectedNOtable;
//    ShopNameBean shopNameBean;
    ShopNameBean mShopNameBean;

    public ArrayList<ShopNameBean.SellerBean> mSellerList;//卖家列表
    public ArrayList<ShopNameBean.SellerBean> sellerlist;//卖家列表


    public Expandable1Adapter(Context context, BitmapUtils bitmap,
                              Hashtable<ShopNameBean.SellerBean,
                                      ArrayList<ShopNameBean.SellerBean.GoodsBean>> groupSet
            ,ShopNameBean shopNameBean) {
        this.context = context;
        this.bitmap = bitmap;
//        this.shopNameBean =shopNameBean;
        mShopNameBean = new ShopNameBean();
        mShopNameBean.setGoods_total_amount(shopNameBean.getGoods_total_amount());


        //父控件的存放集合
        this.groupSet =  groupSet;

        sellerlist = new ArrayList<ShopNameBean.SellerBean>();
        for (Hashtable.Entry<ShopNameBean.SellerBean, ArrayList<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {
            sellerlist.add(entry.getKey());
        }

        mSellerList = new ArrayList<ShopNameBean.SellerBean>();

        for (ShopNameBean.SellerBean bean:shopNameBean.getSeller_list()
             ) {
            ShopNameBean.SellerBean bean1 = isIn(bean,sellerlist);
            if (bean1 != null){
                bean.setGoods_list(groupSet.get(bean1));
                mSellerList.add(bean);
            }
        }

        mShopNameBean.setSeller_list(mSellerList);


    }

    private ShopNameBean.SellerBean  isIn(ShopNameBean.SellerBean bean ,ArrayList<ShopNameBean.SellerBean> list){
        for (ShopNameBean.SellerBean sellerbean: list
             ) {
            if (isSellerBeanEqu(sellerbean,bean)){
                return  sellerbean;
            }

        }

        return null;

    }



    private boolean isSellerBeanEqu(ShopNameBean.SellerBean bean1,ShopNameBean.SellerBean bean2){
        if (TextUtils.equals(bean1.getSeller_no(),bean2.getSeller_no())){
            return true;
        }
        return  false;
    }



    @Override
    public int getGroupCount() {
//        return shopNameBean.getSeller_list().ize();
        return mSellerList.size();
    }

    @Override
    public int getChildrenCount(int i) {
//        return shopNameBean.getSeller_list().get(i).getGoods_list().size();
        return mShopNameBean.getSeller_list().get(i).getGoods_list().size();
    }

    @Override
    public Object getGroup(int i) {
        return mShopNameBean.getSeller_list().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mShopNameBean.getSeller_list().get(i).getGoods_list().get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //外部展示的布局
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (view == null) {
            groupHolder = new GroupHolder();
            view = View.inflate(context, R.layout.shop_expandable_item_group, null);
            groupHolder.mName = (TextView) view.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) view.findViewById(R.id.group_type);
            groupHolder.selected = (ImageView) view.findViewById(R.id.image_selected);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }

        groupHolder.selected.setVisibility(View.GONE);

        //父View的实体类
        ShopNameBean.SellerBean sellerBean = mShopNameBean.getSeller_list().get(i);
        String mName = sellerBean.getSeller_name(); //卖家名称
        groupHolder.mName.setText(mName);
        String mTypes = sellerBean.getSeller_type(); // 卖家类型
        if (mTypes.equals("SC_ZY")) {
            groupHolder.mType.setText("自营商城");
        } else if (mTypes.equals("SC_DSF")) {
            groupHolder.mType.setText("第三方商家");
        } else if (mTypes.equals("YS")) {
            groupHolder.mType.setText("邮市");
        }

        return view;
    }

    //内部展示的布局
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ItemHolder itemHolder;
        if (view == null) {
            itemHolder = new ItemHolder();
            view = View.inflate(context, R.layout.shop_expandable_item_child, null);
            itemHolder.mCommon = (LinearLayout) view.findViewById(R.id.shop_mode);
            itemHolder.mEdit = (LinearLayout) view.findViewById(R.id.shop_editMode);

            itemHolder.mCount = (TextView) view.findViewById(R.id.child_count);
            itemHolder.mName = (TextView) view.findViewById(R.id.child_name);
            itemHolder.mPrice = (TextView) view.findViewById(R.id.child_price);
            itemHolder.mEditPrice = (TextView) view.findViewById(R.id.tv_price);
            itemHolder.mImg = (ImageView) view.findViewById(R.id.child_icon);
            itemHolder.mNum = (TextView) view.findViewById(R.id.tv_num);
            itemHolder.child_selected = (ImageView) view.findViewById(R.id.child_image);
            itemHolder.mAdd = (TextView) view.findViewById(R.id.tv_add);
            itemHolder.mSub = (TextView) view.findViewById(R.id.tv_reduce);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        itemHolder.mEdit.setVisibility(View.GONE);
        itemHolder.child_selected.setVisibility(View.GONE);
        itemHolder.mAdd.setVisibility(View.GONE);
        itemHolder.mSub.setVisibility(View.GONE);

        ShopNameBean.SellerBean.GoodsBean goodsBean = mShopNameBean.getSeller_list().get(i).getGoods_list().get(i1);

        String mGoods_name = goodsBean.getGoods_name();// 名称
        itemHolder.mName.setText(mGoods_name);
        String mGoods_price = goodsBean.getGoods_price(); // 价格
        itemHolder.mPrice.setText("￥" + mGoods_price);

        String Goods_count = goodsBean.getGoods_count();// 数量
        itemHolder.mCount.setText("x" + Goods_count);

        String mGoodes_img = goodsBean.getGoods_img();// 图片
        bitmap.display(itemHolder.mImg, mGoodes_img);

        //这两个是编辑模式的数据
        itemHolder.mEditPrice.setText("￥" + goodsBean.getGoods_price());
        itemHolder.mNum.setText(goodsBean.getGoods_count());


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class GroupHolder {
        /**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         */
        public TextView mName, mType;//商城名称,类型
        public ImageView selected;
    }

    public class ItemHolder {
        public TextView mName, mPrice, mEditPrice, mCount, mNum, mAdd, mSub;//名称,单价,编辑状态下价格,个数,编辑状态下个数,数量,加好,减号
        public ImageView mImg;//图片
        public ImageView child_selected;//子View的选择状态
        public LinearLayout mCommon, mEdit;//普通模式,编辑模式

    }












}
