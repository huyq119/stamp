package cn.com.chinau.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.ShopNameBean;

/**
 * 操作HashTab的工具类
 * Created by Angle on 2016/11/20.
 */

public class HashTabUtils {

    public List<AddShopCartBean> TabtoString(Hashtable<ShopNameBean.SellerBean, Set<ShopNameBean.SellerBean.GoodsBean>> tab) {
        List<AddShopCartBean> list = new ArrayList<>();

        Enumeration<Set<ShopNameBean.SellerBean.GoodsBean>> elements = tab.elements();
        while (elements.hasMoreElements()) {
            Set<ShopNameBean.SellerBean.GoodsBean> goodsBeen = elements.nextElement();
//            for (int i = 0; i < goodsBeen.size(); i++) {
                Iterator<ShopNameBean.SellerBean.GoodsBean> it = goodsBeen.iterator();
                while (it.hasNext()) {
                    ShopNameBean.SellerBean.GoodsBean next = it.next();
                    String goods_count = next.getGoods_count();
                    String goods_sn = next.getGoods_sn();
                    AddShopCartBean bean = new AddShopCartBean(goods_sn, goods_count);
                    list.add(bean);
//                }
            }
        }
        return list;
    }
}
