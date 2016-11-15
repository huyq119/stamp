package cn.com.chinau.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.FirmOrderActivity;
import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.ShopListenerFace;
import cn.com.chinau.ui.MyApplication;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ShoppingCartBiz;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 购物车的适配器
 * Created by Administrator on 2016/8/15.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private SharedPreferences sp;
    private ShopNameBean shopNameBean;
    private Context context;
    private BitmapUtils bitmap;
    private boolean isEditing;//是否处于编辑状态
    private ShopListenerFace mChangeListener;
    private boolean isSelectAll;//父控件是否处于选中状态默认状态为不显示
    private LinearLayout layout;
    private TextView tv1, tv2;
    private ProgressDialog prodialog;
    private TextView Title, title;
    private Button dialog_button_cancel, dialog_button_ok;
    private ArrayList<ShopNameBean.SellerBean> mGoodsList;
    private int temp;

    private HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet;
    private Set<ShopNameBean.SellerBean.GoodsBean> childSet;//相当于一个Map结合被

    private ArrayList<AddShopCartBean> info_list;
    private String mToken, mUser_id;
    private boolean isChildSelected;
    private String goods_sn;
    private String goods_count;
    private boolean isChild;
    private boolean isChooseAllFlage = true;
    private String price;
    private String count;
    private SellerList mSellerList;
    private String mSellerLists;

    public ExpandableAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean
            , LinearLayout layout, TextView tv1, TextView tv2) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
        this.layout = layout;
        this.tv1 = tv1;
        this.tv2 = tv2;
        info_list = new ArrayList<>();

        sp = context.getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        //子控件的存放集合
        childSet = new HashSet<>();
        //父控件的存放集合
        groupSet = new HashMap<>();
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
        ShopNameBean.SellerBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1);

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
        //子View的checkBox
        String mGoods_sn = goodsBean.getGoods_sn();// 商品编号
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


    private Integer key;
    private Set<ShopNameBean.SellerBean.GoodsBean> value;

    //内部的监听
    View.OnClickListener listener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            ArrayList<ShopNameBean.SellerBean> seller_list = shopNameBean.getSeller_list();
            switch (v.getId()) {
                case R.id.image_selected://父控件的选择按钮
                    String SellerList1 = sp.getString("SellerList", "");
                    if (!SellerList1.equals("")) {
                        sp.edit().putString("SellerList", "").commit();
                    }
                    //获取当前点击View的标识,并强转成数字标识
                    int groupPosition = Integer.parseInt(String.valueOf(v.getTag()));

                    //变更数据的显示,内部有取反的操作
                    isSelectAll = ShoppingCartBiz.selectGroup(seller_list, groupPosition);

                    //子控件的存放集合
                    childSet = new HashSet<>();

                    //这里获取的是选择父控件的内容,如果为真的话应该把数据存起来，否则的话应该把数据删除
                    ShopNameBean.SellerBean sellerBean = shopNameBean.getSeller_list().get(groupPosition);
                    if (sellerBean.isGroupSelected) {//如果为真的情况下是选中
                        String seller_name = sellerBean.getSeller_name();
                        String seller_type = sellerBean.getSeller_type();
                        MyLog.LogShitou("父控件内容", groupPosition + "--" + seller_name + "--" + seller_type);

                        //选中的所有子空间
                        ArrayList<ShopNameBean.SellerBean.GoodsBean> goods_list = sellerBean.getGoods_list();

                        //这里遍历所有子控件并且把所有内容都存起来
                        for (int i = 0; i < goods_list.size(); i++) {
                            ShopNameBean.SellerBean.GoodsBean goodsBean = goods_list.get(i);
                            childSet.add(goodsBean);
                        }
                        groupSet.put(groupPosition, childSet);


                    } else {
                        groupSet.remove(groupPosition);
                    }

                    // 遍历groupSet获取商品编号和数量
                    for (HashMap.Entry<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {
                        Set<ShopNameBean.SellerBean.GoodsBean> value = entry.getValue(); // 拿到循环后的value值
                        for (int i = 0; i < value.size(); i++) {
                            Iterator<ShopNameBean.SellerBean.GoodsBean> iterator = value.iterator();
                            ShopNameBean.SellerBean.GoodsBean next = iterator.next();
                            goods_sn = next.getGoods_sn();// 商品编号
                            goods_count = next.getGoods_count();// 商品数量
                            isChild = next.isChildSelected();
                            MyLog.LogShitou("---1---编号+数量Json", goods_sn + "--" + goods_count);


                        }
                        // 添加数据到AddShopCartBean生成Json
                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                        mAddShopCartBean.setGoods_sn(goods_sn);
                        mAddShopCartBean.setGoods_count(goods_count);
                        info_list.add(mAddShopCartBean); // 添加到list
                        MyLog.LogShitou("==2===最后需要的编号+数量Json", info_list.toString());
                    }


                    selectAll(); // 选择所有，全选回调
                    setSettleInfo();  // 获取总价和总商品数量
                    notifyDataSetChanged(); // 刷新适配器

                    MyLog.LogShitou("父控件添的数据", groupSet.toString() + "========条数" + groupSet.size());
                    // 判断删除是否可点击
//                    mSellerLists = sp.getString("SellerList", "");
//                    if (!(count != null && price != null || !mSellerLists.equals(""))) {
//                        tv1.setBackgroundColor(context.getResources().getColor(R.color.red_font));
//                        tv1.setEnabled(true);
//                    }
                    break;

                case R.id.tv_add://增加的监听
//                    UpDataShopCart(final String Goods_info,final String op_type)

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
                    String SellerList2 = sp.getString("SellerList", "");
                    if (!SellerList2.equals("")) {
                        sp.edit().putString("SellerList", "").commit();
                    }
                    String tagEdit = String.valueOf(v.getTag());
                    MyLog.LogShitou("子控件1", tagEdit);
                    if (tagEdit.contains(",")) {
                        String s[] = tagEdit.split(",");
                        //父View的角标和子View的角标
                        int group = Integer.parseInt(s[0]);
                        int child = Integer.parseInt(s[1]);

                        isSelectAll = ShoppingCartBiz.selectOne(seller_list, group, child);
                        //这里获取的是选择父控件的内容,如果为真的话应该把数据存起来，否则的话应该把数据删除
                        ShopNameBean.SellerBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(group).getGoods_list().get(child);

                        String name = shopNameBean.getSeller_list().get(group).getSeller_name();
                        String type = shopNameBean.getSeller_list().get(group).getSeller_type();

                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(name);
                        arrayList.add(type);

                        MyLog.LogShitou("选中子控件的父控件的name+type", name + "--" + type);

                        MyLog.LogShitou(goodsBean.isChildSelected + "");
                        //取出集合
                        Set<ShopNameBean.SellerBean.GoodsBean> goodsBeen = groupSet.get(group);
                        if (goodsBean.isChildSelected) {
                            if (goodsBeen == null) {
                                goodsBeen = new HashSet<>();
                            }
                            goodsBeen.add(goodsBean);
                            groupSet.put(group, goodsBeen);
                            MyLog.LogShitou("子控件总共添加的数据", groupSet + "");

                        } else {

                            goodsBeen.remove(goodsBean);
                            MyLog.LogShitou("-----子控件反选的数据goodsBeen", goodsBeen.toString());
                            groupSet.put(group, goodsBeen);
                            MyLog.LogShitou("子反选后总添加的数据goodsBean", goodsBeen.toString() + "=====条数" + groupSet.size());

                        }
                    }
                    // 遍历获取商品编号和数量
                    for (HashMap.Entry<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {
                        Set<ShopNameBean.SellerBean.GoodsBean> value = entry.getValue(); // 拿到循环后的value值
                        for (int i = 0; i < value.size(); i++) {
                            Iterator<ShopNameBean.SellerBean.GoodsBean> iterator = value.iterator();
                            ShopNameBean.SellerBean.GoodsBean next = iterator.next();
                            goods_sn = next.getGoods_sn();// 商品编号
                            goods_count = next.getGoods_count();// 商品数量
                            isChild = next.isChildSelected();
                            MyLog.LogShitou("---1---编号+数量Json", goods_sn + "--" + goods_count);


                        }
                        // 添加数据到AddShopCartBean生成Json
                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                        mAddShopCartBean.setGoods_sn(goods_sn);
                        mAddShopCartBean.setGoods_count(goods_count);
                        info_list.add(mAddShopCartBean); // 添加到list
                        MyLog.LogShitou("==2===最后需要的编号+数量Json", info_list.toString());
                    }

                    selectAll(); // 选择所有
                    setSettleInfo();  // 获取总价和总商品数量
                    notifyDataSetChanged(); // 刷新适配器

                    // 判断删除是否可点击
//                    mSellerLists = sp.getString("SellerList", "");
//                    if (!(count != null && price != null || !mSellerLists.equals(""))) {
//                        tv1.setBackgroundColor(context.getResources().getColor(R.color.red_font));
//                        tv1.setEnabled(true);
//                    }


                    MyLog.LogShitou("***************" + temp + "*****************child" + groupSet.toString());
                    break;
                case R.id.shop_all://全选按钮

                    MyLog.LogShitou("shopNameBean==========", shopNameBean + "");
                    isSelectAll = ShoppingCartBiz.selectAll(seller_list, isSelectAll, (ImageView) v);

                    MyLog.LogShitou("全选是否选中", isSelectAll + "");

                    //这里获取的是全选控件的内容,如果为真的话应该把数据存起来，否则的话应该把数据删除
                    ArrayList<ShopNameBean.SellerBean> seller_lists = shopNameBean.getSeller_list();


                    if (isChooseAllFlage) {//如果为真的情况下是选中
                        sp = context.getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
                        sp.edit().putString("SellerList", seller_lists.toString()).commit();
                        isChooseAllFlage = false;
                        MyLog.LogShitou("保存后拿取得=====11======", sp.getString("SellerList", ""));
                    } else {
                        sp.edit().putString("SellerList", "").commit();
                        isChooseAllFlage = true;
                        MyLog.LogShitou("保存后拿取得====22====", sp.getString("SellerList", ""));
                    }

                    MyLog.LogShitou("最后全选内容是seller_lists", sp.getString("SellerList", ""));


                    setSettleInfo(); // 获取总价和总商品数量
                    notifyDataSetChanged();
                    break;
                case R.id.base_search://编辑按钮
                    TextView mEditText = (TextView) v;
                    isEditing = !isEditing;
//                    MyLog.LogShitou("isEditing这是啥值==11==",""+isEditing);
                    if (isEditing) {
                        mEditText.setText("完成");
                        layout.setVisibility(View.INVISIBLE);// 隐藏合计Ll
                        tv1.setVisibility(View.VISIBLE); // 显示删除按钮
//                        tv1.setBackgroundColor(context.getResources().getColor(R.color.gary));
//                        tv1.setEnabled(false);
                        tv2.setVisibility(View.GONE); // 隐藏结算按钮
//                        MyLog.LogShitou("isEditing这是啥值==22==",""+isEditing);
                    } else {
                        mEditText.setText("编辑");
                        layout.setVisibility(View.VISIBLE);
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.VISIBLE);
//                        MyLog.LogShitou("isEditing这是啥值==33==",""+isEditing);
                    }
                    notifyDataSetChanged();
                    break;

                case R.id.Shop_delete://删除
                    // 判断删除是否可点击
                    mSellerLists = sp.getString("SellerList", "");
                    MyLog.LogShitou("===点击删除", mSellerLists + "--" + count + "--" + price);
                    if (count != null && price != null || !mSellerLists.equals("")) {
                        DeleteDialog();// 删除弹出框
                    } else {
                        MyToast.showShort(context, "请选择要删除的商品");
                    }
                    break;
                case R.id.Shop_pay://去结算
                    mSellerLists = sp.getString("SellerList", "");
                    MyLog.LogShitou("点击结算", mSellerLists + "--" + count + "--" + price);

                    if (count != null && !count.equals("0")) {
//                        if (count != null && price != null || !mSellerLists.equals("")) {
                            Intent intent = new Intent(context, FirmOrderActivity.class);
                            intent.putExtra("Count", count);// 传数量
                            intent.putExtra("Price", price);// 传总价钱
                            intent.putExtra("FirmOrder", "ExpandableAdapter");// 适配器的标识
                            //这里只要是把集合遍历一下传过去就行了
                            MyApplication.setGroupSet(groupSet);
                            MyLog.LogShitou("去结算传的数据", "" + groupSet);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                        } else {
//
//                            MyToast.showShort(context, "您还没有选择商品哦");
//                        }
                    } else {

                        MyToast.showShort(context, "您还没有选择商品哦");
                    }

                    break;


            }
        }
    };


    // 获取总价和总商品数量
    private void setSettleInfo() {
        String[] info = ShoppingCartBiz.getShoppingCount(shopNameBean.getSeller_list());
        count = info[0];
        price = info[1];
        MyLog.LogShitou("获取的数量和价格", count + "--" + price);
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && info != null) {
            mChangeListener.onDataChange(count, price);

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

    /**
     * 删除弹出框
     */

    private void DeleteDialog() {
        prodialog = new ProgressDialog(context);
        prodialog.show();
        Title = (TextView) prodialog.findViewById(R.id.title_tv);
        Title.setText("确定要删除吗？");
        // 取消
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);// 取消
        // 取消
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog.dismiss();
            }
        });
        // 确定
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpDataShopCart(info_list.toString(), StaticField.SC); // 修改购物车网络请求
                prodialog.dismiss();
            }
        });
    }

    /**
     * 修改购物车网络请求
     *
     * @param Goods_info 商品信息：所有商品的json字符串
     * @param op_type    操作类型： SC删除；JR加入；XG修改
     */
    private void UpDataShopCart(final String Goods_info, final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTMODIFY);// 接口名称

                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODESINFO, Goods_info);//  商品信息：所有商品的json字符串
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result加入购物车", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (op_type.equals(StaticField.SC)) {// 删除
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DeleteSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (op_type.equals(StaticField.XG)) { // 修改
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }

            }
        });
    }

    private String mSellerList1;
    private ArrayList<ShopNameBean.SellerBean> seller_list;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticField.DeleteSUCCESS:// 删除
                    Gson gsones = new Gson();
                    BaseBean mBasebean = gsones.fromJson((String) msg.obj, BaseBean.class);
                    if (mBasebean.getRsp_code().equals("0000")) {
                        ShopRequestNet(); // 购物车List网络请求
                        MyToast.showShort(context, "刪除成功");

                    } else {
                        MyToast.showShort(context, mBasebean.getRsp_msg());
                    }
                    break;
                case StaticField.SUCCESS:// 修改
                    Gson gson = new Gson();
                    BaseBean mBasebeans = gson.fromJson((String) msg.obj, BaseBean.class);
                    if (mBasebeans.getRsp_code().equals("0000")) {

                        MyToast.showShort(context, "修改成功");
                    }
                    break;
                case StaticField.QB_SUCCESS:// 请求购物车
                    Gson gsons = new Gson();
                    ShopNameBean shopNameBean = gsons.fromJson((String) msg.obj, ShopNameBean.class);
                    String code = shopNameBean.getRsp_code();
                    if (code.equals("0000")) {
                        seller_list = shopNameBean.getSeller_list();
                        mSellerList.GetSellerList(seller_list);// 接口调用
                    }
                    break;
            }


        }
    };

    /**
     * 购物车List网络请求
     */
    private void ShopRequestNet() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTQUERY);// 接口名称
                params.put(StaticField.TOKEN, mToken); // 登录标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result购物车list", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.QB_SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }


    // 定义一个接口给Fragment
    public interface SellerList {
        void GetSellerList(ArrayList<ShopNameBean.SellerBean> seller_list);
    }


    public void setSellerList(SellerList mSellerList) {
        this.mSellerList = mSellerList;
    }
}
