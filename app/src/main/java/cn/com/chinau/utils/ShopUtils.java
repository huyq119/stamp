package cn.com.chinau.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.StaticField;
import cn.com.chinau.bean.ShopNameBean;

/**
 * Created by Angle on 2016/11/18.
 */

public class ShopUtils {
    /**
     * 设置数据的数据集合
     *
     * @param sellerListBean 这里是所有添加同步集合的方法
     */
    public static void SynData(Context context, ShopNameBean.SellerBean sellerListBean) {
        //拿出之前的集合判断是否为空，为空直接添加
//        List<ShopBean.SellerListBean> seller_list = ShopBean.getInstance().getSeller_list();
        String shopJson = (String) SPUtils.get(context, StaticField.SHOPJSON, "");
        ShopNameBean shopBean = new Gson().fromJson(shopJson, ShopNameBean.class);

        if (shopBean != null){
            //        List<ShopBean.SellerListBean> seller_list = shopBean.getSeller_list();
            ArrayList<ShopNameBean.SellerBean> seller_list = shopBean.getSeller_list();
            if (seller_list.size() == 0) {
                MyLog.LogShitou("0");
                //这里直接添加就可以了
                seller_list.add(sellerListBean);
            } else {
                MyLog.LogShitou("!0");
                //这里要判断之前是否有这个如果有的话就更改数量
                for (int i = 0; i < seller_list.size(); i++) {
                    //获取保存的对象
                    ShopNameBean.SellerBean sellerListBeanSave = seller_list.get(i);
                    MyLog.LogShitou("sellerListBeanSave" + sellerListBeanSave.toString());
                    MyLog.LogShitou("seller_list" + seller_list.toString());
                    if (sellerListBeanSave.getSeller_no().equals(sellerListBean.getSeller_no())) {
                        //这里说明的是已经包含了外层对象，这里还要判断内层对象
                        MyLog.LogShitou("1");
                        //内层集合
                        ArrayList<ShopNameBean.SellerBean.GoodsBean> goods_listSave = sellerListBeanSave.getGoods_list();
                        for (int j = 0; j < goods_listSave.size(); j++) {
                            MyLog.LogShitou("这里是是上司" + goods_listSave.get(j).getGoods_sn());
                            if (goods_listSave.get(j).getGoods_sn().equals(sellerListBean.getGoods_list().get(0).getGoods_sn())) {
                                MyLog.LogShitou("2");
                                //这里是说明内层包含了相同的对象，这里有个逻辑内层相同外层一定相同了
                                String goods_count = goods_listSave.get(j).getGoods_count();
                                int goodsCount = Integer.valueOf(goods_count);
                                goodsCount++;
                                seller_list.get(i).getGoods_list().get(j).setGoods_count(String.valueOf(goodsCount));
                            } else {
                                MyLog.LogShitou("执行了阿门");
                                goods_listSave.add(sellerListBean.getGoods_list().get(0));
                                break;
                            }
                        }
                    } else {
                        //说明不包含外层对象
                        seller_list.add(sellerListBean);
                        break;
                    }
                }
            }

//        ShopBean.getInstance().getSeller_list().add(sellerListBean);
//        MyLog.LogShitou("实体类", "--->" + ShopBean.getInstance().toString());

            //把这个对象保存成Json保存进去
            SPUtils.put(context, StaticField.SHOPJSON, new Gson().toJson(shopBean));

            String sg = (String) SPUtils.get(context, StaticField.SHOPJSON, "");
            MyLog.LogShitou("点击执行完成的时候执行的" + sg);
        }else {

            ShopNameBean shopNameBean= new ShopNameBean();

            ArrayList<ShopNameBean.SellerBean> seller_list = new ArrayList<>();
            seller_list.add(sellerListBean);

            shopNameBean.setSeller_list(seller_list);

            SPUtils.put(context, StaticField.SHOPJSON, new Gson().toJson(shopNameBean));

        }

    }
}
