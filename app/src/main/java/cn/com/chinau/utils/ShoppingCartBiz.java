package cn.com.chinau.utils;

import android.widget.ImageView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.ShopNameBean;

/**
 * 购物车的选择工具类
 * Created by Angle on 2016/9/12.
 */
public class ShoppingCartBiz {


    /**
     * 勾与不勾选中选项
     *
     * @param isSelect 原先状态
     * @return 是否勾上，之后状态
     */
    public static boolean checkItem(boolean isSelect, ImageView selected) {
        if (isSelect) {
            selected.setImageResource(R.mipmap.select_red);
        } else {
            selected.setImageResource(R.mipmap.select_white);
        }
        return isSelect;
    }

    /**
     * 选择父View整个一组的方法
     *
     * @param mList         传入的父View的集合
     * @param groupPosition 对应的角标
     * @return 是否全部选中
     */
    public static boolean selectGroup(List<ShopNameBean.SellerBean> mList, int groupPosition) {
        boolean isSelectAll;

        boolean isSelected = !(mList.get(groupPosition).isGroupSelected());
        //设置父View被选中
        mList.get(groupPosition).setGroupSelected(isSelected);
        //遍历改变子View的状态
        for (int i = 0; i < mList.get(groupPosition).getGoods_list().size(); i++) {
            mList.get(groupPosition).getGoods_list().get(i).setChildSelected(isSelected);
        }

        //这里是判断内部如果有没选中的就返回false,否则返回true(这个是处理全选的)
        isSelectAll = isSelectAllGroup(mList);
        return isSelectAll;

    }

    /**
     * 组内的所有组，是否都被选中，即全选
     *
     * @param list
     * @return
     */
    private static boolean isSelectAllGroup(List<ShopNameBean.SellerBean> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isSelectGroup = list.get(i).isGroupSelected();
            if (!isSelectGroup) {
                return false;
            }
        }
        return true;
    }


    /**
     * 增减数量，操作通用，数据不通用
     */
    public static void addOrReduceGoodsNum(List<ShopNameBean.SellerBean> mList, boolean isPlus, String flag) {

        //这个flag是标识,也就是子View的标识
        String[] ChildFlag = flag.split(",");
        //父view的角标,子View的角标
        String groupNum = ChildFlag[0];
        String childNum = ChildFlag[1];
        MyLog.LogShitou("父View和子view的标识" + groupNum + "_" + childNum);


        String currentNum = mList.get(Integer.valueOf(groupNum)).getGoods_list().get(Integer.valueOf(childNum)).getGoods_count();
        String num = "1";
        if (isPlus) {
            num = String.valueOf(Integer.parseInt(currentNum) + 1);
        } else {
            int i = Integer.parseInt(currentNum);
            if (i > 1) {
                num = String.valueOf(i - 1);
            } else {
                num = "1";
            }
        }

//        String productID = goods.getProductID();
        mList.get(Integer.valueOf(groupNum)).getGoods_list().get(Integer.valueOf(childNum)).setGoods_count(num);
//        tvNum.setText(mList.get(Integer.valueOf(groupNum)).getGoods_list().get(Integer.valueOf(childNum)).getGoods_count());
//        goods.setGoods_count(num);
//        updateGoodsNumber(productID, num);
    }


    /**
     * 单选一个，需要判断整个组的标志，整个组的标志，是否被全选，取消，则
     * 除了选择全部和选择单个可以单独设置背景色，其他都是通过改变值，然后notify；
     *
     * @param list
     * @param childPosition
     * @return 是否选择全部
     */
    public static boolean selectOne(List<ShopNameBean.SellerBean> list, int groupPosition, int childPosition) {
        //是否全选的标识
        boolean isSelectAll;
        //子View的状态取反
        boolean isSelectedOne = !(list.get(groupPosition).getGoods_list().get(childPosition).isChildSelected());
        //处理单个View的状态
        list.get(groupPosition).getGoods_list().get(childPosition).setChildSelected(isSelectedOne);
        //组内View是否全选的标识
        boolean isSelectCurrentGroup = isSelectAllChild(list.get(groupPosition).getGoods_list());
        //设置父View的标识
        list.get(groupPosition).setGroupSelected(isSelectCurrentGroup);

        isSelectAll = isSelectAllGroup(list);

        return isSelectAll;
    }


    /**
     * 组内所有子选项是否全部被选中
     *
     * @param list
     * @return
     */
    private static boolean isSelectAllChild(List<ShopNameBean.GoodsBean> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isSelectGroup = list.get(i).isChildSelected();
            if (!isSelectGroup) {
                return false;
            }
        }
        return true;
    }

    /**
     * 选择全部，点下全部按钮，改变所有商品选中状态
     */
    public static boolean selectAll(List<ShopNameBean.SellerBean> list, boolean isSelectAll, ImageView ivCheck) {
        isSelectAll = !isSelectAll;
        //改变所有选中状态
        ShoppingCartBiz.checkItem(isSelectAll, ivCheck);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setGroupSelected(isSelectAll);
            for (int j = 0; j < list.get(i).getGoods_list().size(); j++) {
                list.get(i).getGoods_list().get(j).setChildSelected(isSelectAll);
            }
        }
        return isSelectAll;
    }

    /**
     * 获取结算信息，肯定需要获取总价和数量，但是数据结构改变了，这里处理也要变；
     *
     * @return 0=选中的商品数量；1=选中的商品总价
     */
    public static String[] getShoppingCount(List<ShopNameBean.SellerBean> listGoods) {
        String[] info = new String[2];
        int selectedCount = 0;
        double selectedMoney = 0;
        for (int i = 0; i < listGoods.size(); i++) {
            double childMoney = 0;
            int childCount = 0;
            for (int j = 0; j < listGoods.get(i).getGoods_list().size(); j++) {
                boolean isSelected = listGoods.get(i).getGoods_list().get(j).isChildSelected();
                if (isSelected) {//选中的商品
                    //价格,数量
                    String price = listGoods.get(i).getGoods_list().get(j).getGoods_price();
                    String num = listGoods.get(i).getGoods_list().get(j).getGoods_count();

//                    MyLog.LogShitou("ShoppingCartBiz选中的商品数据",price+"--"+num);
                    //子View的总价格,数量
                    childMoney += Double.valueOf(price) * Integer.valueOf(num);
                    childCount++;
//                    String countMoney = DecimalUtil.multiply(price, num);
//                    selectedMoney = DecimalUtil.add(selectedMoney, countMoney);
//                    selectedCount = DecimalUtil.add(selectedCount, "1");
                }
            }
            //累加循环
            selectedCount += childCount;
            selectedMoney += childMoney;
        }

        info[0] = String.valueOf(selectedCount);
        info[1] = String.valueOf(selectedMoney);
//        MyLog.LogShitou("ShoppingCartBiz赋值后的",info[0] + "-" + info[1]);
        return info;
    }
}
