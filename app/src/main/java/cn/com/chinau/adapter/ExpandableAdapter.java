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

    private HashMap<Integer, Set<ShopNameBean.GoodsBean>> groupSet;
    private Set<ShopNameBean.GoodsBean> childSet;//相当于一个Map结合被

    private ArrayList<AddShopCartBean> info_list;
    private String mToken, mUser_id;
    private boolean isChildSelected;
    private String goods_sn;
    private String goods_count;
    private boolean isChild;

    public ExpandableAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean
            , LinearLayout layout, TextView tv1, TextView tv2) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
        this.layout = layout;
        this.tv1 = tv1;
        this.tv2 = tv2;
        info_list = new ArrayList<>();

        SharedPreferences sp = context.getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
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

        ShopNameBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1);

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
    private Set<ShopNameBean.GoodsBean> value;

    //内部的监听
    View.OnClickListener listener = new View.OnClickListener() {

        private String sn;
        private String[] array;

        @Override
        public void onClick(View v) {
            ArrayList<ShopNameBean.SellerBean> seller_list = shopNameBean.getSeller_list();
            switch (v.getId()) {
                case R.id.image_selected://父控件的选择按钮
                    //获取当前点击View的标识,并强转成数字标识
                    int groupPosition = Integer.parseInt(String.valueOf(v.getTag()));
//                    String mGourp = String.valueOf(v.getTag());
//                    MyLog.LogShitou("父控件1", groupPosition);
//                    if (mGourp.contains(",")) {
//                        String s[] = mGourp.split(",");
                    //父View的角标和子View的角标
//                        group = Integer.parseInt(s[0]);
//                        String name = s[1];
//                        String mTypes = s[2];

//                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
//                        mAddShopCartBean.setGoods_count(goods_count);
//                        mAddShopCartBean.setGoods_sn(goods_sn);
//
//                        info_list.add(mAddShopCartBean); // 添加到list
//                        MyLog.LogShitou("添加的list数据",info_list.toString());

//                    }

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
                        ArrayList<ShopNameBean.GoodsBean> goods_list = sellerBean.getGoods_list();

                        //这里遍历所有子控件并且把所有内容都存起来
                        for (int i = 0; i < goods_list.size(); i++) {
                            ShopNameBean.GoodsBean goodsBean = goods_list.get(i);
                            childSet.add(goodsBean);
                        }
                        groupSet.put(groupPosition, childSet);

                    } else {
                        groupSet.remove(groupPosition);
                    }
                    //这个是全选回调
                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();

                    MyLog.LogShitou("父控件添加的数据", groupSet.toString());
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
                    String tagEdit = String.valueOf(v.getTag());
                    MyLog.LogShitou("子控件1", tagEdit);
                    if (tagEdit.contains(",")) {
                        String s[] = tagEdit.split(",");
                        //父View的角标和子View的角标
                        int group = Integer.parseInt(s[0]);
                        int child = Integer.parseInt(s[1]);

                        isSelectAll = ShoppingCartBiz.selectOne(seller_list, group, child);
                        //这里获取的是选择父控件的内容,如果为真的话应该把数据存起来，否则的话应该把数据删除
                        ShopNameBean.GoodsBean goodsBean = shopNameBean.getSeller_list().get(group).getGoods_list().get(child);

                        String name = shopNameBean.getSeller_list().get(group).getSeller_name();
                        String type = shopNameBean.getSeller_list().get(group).getSeller_type();

                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(name);
                        arrayList.add(type);

                        MyLog.LogShitou("选中子控件的父控件的name+type", name + "--" + type);

                        MyLog.LogShitou(goodsBean.isChildSelected + "");
                        //取出集合
                        Set<ShopNameBean.GoodsBean> goodsBeen = groupSet.get(group);
                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                        if (goodsBean.isChildSelected) {
                            if (goodsBeen == null) {
                                goodsBeen = new HashSet<>();
                            }
                            goodsBeen.add(goodsBean);
//                            MyLog.LogShitou("-----子控件单个添加的数据", goodsBeen.toString());
                            groupSet.put(group, goodsBeen);
                            MyLog.LogShitou("子控件总共添加的数据", groupSet + "");


                            for (HashMap.Entry<Integer, Set<ShopNameBean.GoodsBean>> entry : groupSet.entrySet()) {
                                key = entry.getKey();
                                value = entry.getValue();

                                for (int i = 0; i < value.size(); i++) {
                                    Iterator<ShopNameBean.GoodsBean> iterator = value.iterator();
                                    ShopNameBean.GoodsBean next = iterator.next();
                                    goods_sn = next.getGoods_sn();
                                    goods_count = next.getGoods_count();
                                    isChild = next.isChildSelected();
//                                    MyLog.LogShitou("-----子选中的编号+数量", price + "--" + goods_count);
                                }
                            }

                            mAddShopCartBean.setGoods_count(goods_count);
                            mAddShopCartBean.setGoods_sn(goods_sn);
                            info_list.add(mAddShopCartBean); // 添加到list
                            MyLog.LogShitou("添加的list数据", info_list.toString());

//                            MyLog.LogShitou("循环出的key", key.toString());
//                            MyLog.LogShitou("循环出的values", value.toString());

                        } else {

                            goodsBeen.remove(goodsBean);
                            MyLog.LogShitou("-----子控件反选的数据goodsBeen", goodsBeen.toString());

                            info_list.remove(mAddShopCartBean); // 删除

                            MyLog.LogShitou("-----子反选的数据后info_list", info_list.toString());
                            groupSet.put(group, goodsBeen);
                            MyLog.LogShitou("子反选后总添加的数据groupSet", groupSet.toString());
                        }
                    }

                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();


                    MyLog.e("***************" + temp + "*****************child" + groupSet.toString());
                    break;
                case R.id.shop_all://全选按钮
                    isSelectAll = ShoppingCartBiz.selectAll(seller_list, isSelectAll, (ImageView) v);
                    MyLog.LogShitou("全选是否选中", isSelectAll + "");

                    mGoodsList = shopNameBean.getSeller_list(); // list数据
                    MyLog.LogShitou("全选的数据11", mGoodsList.toString());

                    if (!isSelectAll) {
                        mGoodsList.remove(shopNameBean);
                    }
                    MyLog.LogShitou("全选的数据22", mGoodsList.toString());

                    String mTotalAount = shopNameBean.getGoods_total_amount();// 总价
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                case R.id.base_search://编辑按钮
                    TextView mEditText = (TextView) v;
                    isEditing = !isEditing;
                    if (isEditing) {
                        mEditText.setText("完成");
                        layout.setVisibility(View.INVISIBLE);// 隐藏合计Ll
                        tv1.setVisibility(View.VISIBLE); // 显示删除按钮
                        tv2.setVisibility(View.GONE); // 隐藏结算按钮
                    } else {
                        mEditText.setText("编辑");
                        layout.setVisibility(View.VISIBLE);
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                    break;

                case R.id.Shop_delete://删除
                    DeleteDialog();// 删除弹出框
                    break;
                case R.id.Shop_pay://去结算

//                    Intent intent = new Intent(context, FirmOrderActivity.class);
                    //这里只要是把集合遍历一下传过去就行了
//                    MyApplication.setGroupSet(groupSet);
//                    MyLog.LogShitou("那到值了吗", "" + groupSet);
//                    context.startActivity(intent);
//                    ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anm.slide_left_out);
                    if (groupSet.size()!=0){
                        Intent intent = new Intent(context, FirmOrderActivity.class);
                        intent.putExtra("ShopGoodsJson", info_list.toString());
                        intent.putExtra("GroupSet", groupSet.toString());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    }else{
                        MyToast.showShort(context,"你还未选择要结算的商品");
                    }




                    break;


            }
        }
    };


    private void setSettleInfo() {
        String[] info = ShoppingCartBiz.getShoppingCount(shopNameBean.getSeller_list());
//        MyLog.LogShitou("这里有数据吗", info[0] + "--" + info[1]);
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
//                UpDataShopCart(info_list.toString(), StaticField.SC); // 修改购物车网络请求
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticField.DeleteSUCCESS:// 删除
                    Gson gsones = new Gson();
                    BaseBean mBasebean = gsones.fromJson((String) msg.obj, BaseBean.class);
                    if (mBasebean.getRsp_code().equals("0000")) {
                        notifyDataSetChanged();
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
            }


        }
    };


}
