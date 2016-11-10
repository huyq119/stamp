package cn.com.chinau.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import java.util.ArrayList;
import java.util.HashMap;
import cn.com.chinau.R;
import cn.com.chinau.bean.ShopNameBean;

/**
 * 购物车的适配器
 * Created by Administrator on 2016/8/15.
 */
public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter {
    private ShopNameBean shopNameBean;
    private Context context;
    private BitmapUtils bitmap;
    private ModifyCountInterface modifyCountInterface;//增加 减少接口
    //private CheckInterface checkInterface;
    private ArrayList<ShopNameBean.SellerBean.GoodsBean> goods_list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    public ShopcartExpandableListViewAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < shopNameBean.getSeller_list().size(); i++) {
            getIsSelected().put(i, false);
        }
    }
    /*public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }*/
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
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
        return 0;
    }
    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //外部展示的布局
    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (view == null) {
            groupHolder = new GroupHolder();
            view = View.inflate(context, R.layout.shop_expandable_item_group, null);
            groupHolder.mName = (TextView) view.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) view.findViewById(R.id.group_type);
            groupHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }
       /* final ShopNameBean.SellerBean group = (ShopNameBean.SellerBean) getGroup(i);
        if (group !=null){*/
        groupHolder.mName.setText(shopNameBean.getSeller_list().get(i).getSeller_name());
        groupHolder.mType.setText(shopNameBean.getSeller_list().get(i).getSeller_type());
        groupHolder.checkBox.setChecked(getIsSelected().get(i));
           /* groupHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group.setChoosed(((CheckBox) view).isChecked());
                    checkInterface.checkGroup(i, ((CheckBox) view).isChecked());// 暴露组选接口
                }
            });
            groupHolder.checkBox.setChecked(group.isChoosed());
        // 根据isSelected来设置checkbox的选中状况
    }*/
        return view;
    }
    //内部展示的布局
    @Override
    public View getChildView(final int i, final int i1, boolean isLastChild, View view, ViewGroup parent) {
        final ItemHolder itemHolder;
        if (view == null) {
            itemHolder = new ItemHolder();
            view = View.inflate(context, R.layout.item_shopcart_product, null);
            itemHolder.checkBox = (CheckBox)view.findViewById(R.id.checkbox_shopcart);
            itemHolder.mShopname = (TextView)view.findViewById(R.id.tv_intro);
            itemHolder.mPrice = (TextView)view.findViewById(R.id.tv_price);
            itemHolder.mAdd = (TextView) view.findViewById(R.id.tv_add);
            itemHolder.mReduce = (TextView) view.findViewById(R.id.tv_reduce);
            itemHolder.mCount = (TextView) view.findViewById(R.id.tv_num);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }
        final ShopNameBean.SellerBean.GoodsBean product = (ShopNameBean.SellerBean.GoodsBean) getChild(i, i1);

        goods_list = shopNameBean.seller_list.get(i1).getGoods_list();

        if (product !=null){
            itemHolder.mCount.setText(goods_list.get(i1).getGoods_count()+"");
            itemHolder.mShopname.setText(goods_list.get(i1).getGoods_name());//名字
            itemHolder.mPrice.setText("￥"+ goods_list.get(i1).getGoods_price()+"");//价格
            //itemHolder.checkBox.setChecked(goods_list.get(i1).isChoosed());
                /*itemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goods_list.get(i1).setChoosed(((CheckBox) view).isChecked());
                        itemHolder.checkBox.setChecked(((CheckBox) view).isChecked());
                        checkInterface.checkChild(i, i1, ((CheckBox) view).isChecked());// 暴露子选接口
                    }
                });*/
            itemHolder.mAdd.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    modifyCountInterface.doIncrease(i, i1,itemHolder.mCount, itemHolder.checkBox.isChecked(),goods_list);// 暴露增加接口
                }
            });
            itemHolder.mReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modifyCountInterface.doDecrease(i, i1, itemHolder.mCount, itemHolder.checkBox.isChecked());// 暴露删减接口
                }
            });
            // 根据isSelected来设置checkbox的选中状况
            itemHolder.checkBox.setChecked(getIsSelected().get(i1));
            bitmap.display(itemHolder.mImg, goods_list.get(i1).getGoods_img());
        }
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected){
        ShopcartExpandableListViewAdapter.isSelected = isSelected;
    }
    public class GroupHolder {
        /**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         */
        public TextView mName, mType;//商城名称,类型
        public CheckBox checkBox;
    }
    public class ItemHolder {
        public TextView mShopname, mPrice,mReduce, mCount,mAdd;//名称,单价,减少，数量，增加
        public ImageView mImg;//图片
        public CheckBox checkBox;
    }
    /**
     * 复选框接口
     *
     *
     */
/*    public interface CheckInterface {
         *//**
     *  组选框状态改变触发的事件
     * @param position
     * @param isChecked
     *//*
        public void checkGroup(int position, boolean isChecked);
        *//***
     * 子选框状态改变时触发的事件
     * @param position
     * @param i1
     * @param isChecked
     *//*
        public void checkChild(int position, int i1, boolean isChecked);
    }*/
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         *
         * 增加操作
         * @param position
         * @param i1
         * @param showCountView
         * @param isChecked
         * 子元素选中与否
         */
        public void doIncrease(int position, int i1, View showCountView, boolean isChecked, ArrayList<ShopNameBean.SellerBean.GoodsBean> goods_list);
        /***
         * 减少操作
         * @param position
         * @param i1
         * @param showCountView
         * @param isChecked
         * 子元素选中与否
         */
        public void doDecrease(int position, int i1, View showCountView, boolean isChecked);
    }
}