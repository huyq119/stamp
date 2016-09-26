package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.listener.ShopListenerFace;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ShoppingCartBiz;

/**
 * 购物车的适配器
 * Created by Administrator on 2016/8/15.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private ShopNameBean shopNameBean;
    private Context context;
    private BitmapUtils bitmap;
    private boolean isEditing;//是否处于编辑状态
    private ShopListenerFace mChangeListener;
    private boolean isSelectAll;//父控件是否处于选中状态默认状态为不显示

    public ExpandableAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
    }

    /**
     * 暴露接口的方法
     *
     * @param changeListener
     */
    public void setChangeListener(ShopListenerFace changeListener) {
        mChangeListener = changeListener;
    }

    @Override
    public int getGroupCount() {
        return shopNameBean.getSeller_list().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return shopNameBean.getSeller_list().get(i).getGoods_list().size();
    }

    @Override
    public Object getGroup(int i) {
        return shopNameBean.getSeller_list().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return shopNameBean.getSeller_list().get(i).getGoods_list().get(i);
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
//            view = LayoutInflater.from(context).inflate(R.layout.shop_expandable_item_group, viewGroup, false);
            view = View.inflate(context, R.layout.shop_expandable_item_group, null);
            groupHolder.mName = (TextView) view.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) view.findViewById(R.id.group_type);
            groupHolder.selected = (ImageView) view.findViewById(R.id.image_selected);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }

        //父View的实体类
        ShopNameBean.SellerBean sellerBean = shopNameBean.getSeller_list().get(i);
        groupHolder.mName.setText(sellerBean.getSeller_name());
        groupHolder.mType.setText(sellerBean.getSeller_type());

        //这里设置父View是否被选中
        ShoppingCartBiz.checkItem(sellerBean.isGroupSelected(), groupHolder.selected);

        //记录选择的位置(设置标记),设置父View选中的标记
        groupHolder.selected.setTag(i);
        //父控件的圆圈
        groupHolder.selected.setOnClickListener(listener);

        return view;
    }

    //内部展示的布局
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        if (isEditing) {
//            ItemHolder itemHolder;
//            if (view == null) {
//                itemHolder = new ItemHolder();
//                view = View.inflate(context, R.layout.shop_expandable_item_child, null);
//                itemHolder.mCount = (TextView) view.findViewById(R.id.child_count);
//                itemHolder.mName = (TextView) view.findViewById(R.id.child_name);
//                itemHolder.mPrice = (TextView) view.findViewById(R.id.child_price);
//                itemHolder.mImg = (ImageView) view.findViewById(R.id.child_icon);
//                itemHolder.child_selected = (ImageView) view.findViewById(R.id.child_image);
//                view.setTag(itemHolder);
//            } else {
//                itemHolder = (ItemHolder) view.getTag();
//            }
////        ArrayList<ShopNameBean.GoodsBean> goods_list = shopNameBean.getSeller_list().get(i).getGoods_list();
//////        itemHolder.mCount.setText(goods_list.get(i1).getGoods_count() );
////        itemHolder.mName.setText(goods_list.get(i1).getGoods_name());
////        itemHolder.mPrice.setText(goods_list.get(i1).getGoods_price());
////        bitmap.display(itemHolder.mImg, goods_list.get(i1).getGoods_img());
//
//           //是否选中
//            boolean isChildSelected = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1).isChildSelected();
//
//            ShopNameBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1);
//            itemHolder.mName.setText(goodsBean.getGoods_name());
//            itemHolder.mPrice.setText(goodsBean.getGoods_price());
//            itemHolder.mCount.setText(goodsBean.getGoods_count());
//            bitmap.display(itemHolder.mImg, goodsBean.getGoods_img());
//
//            //子View的checkBox
//            itemHolder.child_selected.setTag(i + "," + i1);
//            //子View的点击事件
//            itemHolder.child_selected.setOnClickListener(listener);
//
//            //设置子View的选择状态
//            ShoppingCartBiz.checkItem(isChildSelected, itemHolder.child_selected);
//
//        } else {//编辑的显示
//            MyLog.e("是否执行");
//            ItemHolderSelected itemHolderSelected = null;
//            if (view == null) {
//                itemHolderSelected = new ItemHolderSelected();
//                view = View.inflate(context, R.layout.shop_expandable_item_childselected, null);
//                itemHolderSelected.mChild_selected = (ImageView) view.findViewById(R.id.checkbox_shopCart);
//                itemHolderSelected.mImageUrl = (ImageView) view.findViewById(R.id.iv_adapter_list_pic);
//                itemHolderSelected.mName = (TextView) view.findViewById(R.id.tv_intro);
//                itemHolderSelected.mAdd = (TextView) view.findViewById(R.id.tv_add);
//                itemHolderSelected.mNum = (TextView) view.findViewById(R.id.tv_num);
//                itemHolderSelected.mSub = (TextView) view.findViewById(R.id.tv_reduce);
//                itemHolderSelected.mPrice = (TextView) view.findViewById(R.id.tv_price);
//                view.setTag(itemHolderSelected);
//            } else {
//                itemHolderSelected = (ItemHolderSelected) view.getTag();
//            }
//
//            ShopNameBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1);
//            itemHolderSelected.mName.setText(goodsBean.getGoods_name());
//            itemHolderSelected.mPrice.setText(goodsBean.getGoods_price());
//            itemHolderSelected.mNum.setText(goodsBean.getGoods_count());
//            bitmap.display(itemHolderSelected.mImageUrl, goodsBean.getGoods_img());
//
//            //是否选中
//            boolean isChildSelected = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1).isChildSelected();
//            //设置子View的选择状态
//            ShoppingCartBiz.checkItem(isChildSelected, itemHolderSelected.mChild_selected);
//
//            //增加的按钮监听,这里应该把相应的值传过去
//            itemHolderSelected.mAdd.setTag(i + "," + i1);
//            itemHolderSelected.mAdd.setOnClickListener(listener);
//
//            //减少的按钮监听
//            itemHolderSelected.mSub.setTag(i + "," + i1);//减少按钮
//            itemHolderSelected.mSub.setOnClickListener(listener);
//
//            //子View的checkBox
//            itemHolderSelected.mChild_selected.setTag(i + "," + i1);
//            itemHolderSelected.mChild_selected.setOnClickListener(listener);
//        }
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

        //各种状态的显示
        if (isEditing) {//编辑状态
            itemHolder.mCommon.setVisibility(View.GONE);
            itemHolder.mEdit.setVisibility(View.VISIBLE);
        } else {
            itemHolder.mCommon.setVisibility(View.VISIBLE);
            itemHolder.mEdit.setVisibility(View.GONE);
        }

        //是否选中
        boolean isChildSelected = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1).isChildSelected();

        ShopNameBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1);
        itemHolder.mName.setText(goodsBean.getGoods_name());
        itemHolder.mPrice.setText(goodsBean.getGoods_price());
        itemHolder.mCount.setText("x" + goodsBean.getGoods_count());
        bitmap.display(itemHolder.mImg, goodsBean.getGoods_img());
        //这两个是编辑模式的数据
        itemHolder.mEditPrice.setText(goodsBean.getGoods_price());
        itemHolder.mNum.setText(goodsBean.getGoods_count());
        //子View的checkBox
        itemHolder.child_selected.setTag(i + "," + i1);
        //子View的点击事件
        itemHolder.child_selected.setOnClickListener(listener);

        //设置子View的选择状态
        ShoppingCartBiz.checkItem(isChildSelected, itemHolder.child_selected);

        //增加的按钮监听,这里应该把相应的值传过去
        itemHolder.mAdd.setTag(i + "," + i1);
        itemHolder.mAdd.setOnClickListener(listener);

        //减少的按钮监听
        itemHolder.mSub.setTag(i + "," + i1);//减少按钮
        itemHolder.mSub.setOnClickListener(listener);

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

//    public class ItemHolderSelected {
//        public ImageView mChild_selected, mImageUrl;//子View的选择框,子View的图片
//        public TextView mName, mAdd, mSub, mPrice, mNum;//名字,增加,减少,价格
//    }


    //内部的监听
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ArrayList<ShopNameBean.SellerBean> seller_list = shopNameBean.getSeller_list();

            switch (v.getId()) {
                case R.id.image_selected://父控件的选择按钮
                    //获取当前点击View的标识,并强转成数字标识
                    int groupPosition = Integer.parseInt(String.valueOf(v.getTag()));
                    //变更数据的显示,内部有取反的操作
                    isSelectAll = ShoppingCartBiz.selectGroup(seller_list, groupPosition);
                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;

                case R.id.tv_add://增加的监听
                    ShoppingCartBiz.addOrReduceGoodsNum(seller_list, true, (String) v.getTag());
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;

                case R.id.tv_reduce://减少的监听
                    ShoppingCartBiz.addOrReduceGoodsNum(seller_list, false, (String) v.getTag());
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;

                case R.id.child_image://子View的CheckBox点击事件
                    String tagEdit = String.valueOf(v.getTag());
                    MyLog.e(tagEdit);
                    if (tagEdit.contains(",")) {
                        String s[] = tagEdit.split(",");
                        //父View的角标和子View的角标
                        int group = Integer.parseInt(s[0]);
                        int child = Integer.parseInt(s[1]);
                        isSelectAll = ShoppingCartBiz.selectOne(seller_list, group, child);
                        selectAll();
                        setSettleInfo();
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.shop_all://全选按钮
                    isSelectAll = ShoppingCartBiz.selectAll(seller_list, isSelectAll, (ImageView) v);
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                case R.id.base_search://编辑按钮
                    TextView mEditText = (TextView) v;
                    isEditing = !isEditing;
                    if (isEditing) {
                        mEditText.setText("完成");
                    } else {
                        mEditText.setText("编辑");
                    }
                    notifyDataSetChanged();
                    break;


            }
        }
    };

    private void setSettleInfo() {
        String[] info = ShoppingCartBiz.getShoppingCount(shopNameBean.getSeller_list());
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && info != null) {
            mChangeListener.onDataChange(info[0], info[1]);
        }
    }

    /**
     * 对外暴露内部的监听
     *
     * @return
     */
    public View.OnClickListener getAdapterListener() {
        return listener;
    }

    /**
     * 选择所有
     */
    private void selectAll() {
        if (mChangeListener != null) {
            mChangeListener.onSelectItem(isSelectAll);
        }
    }


}
