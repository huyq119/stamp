package cn.com.chinau.listener;

/**
 * 商品的接口用来改变数据的
 * Created by Angle on 2016/9/13.
 */
public interface ShopListenerFace {

    /**
     * 选择条目发生改变的时候
     * @param isSelectedAll 是否全选
     */
    void onSelectItem(boolean isSelectedAll);

    /**
     * 内容发生改变是回调的方法
     * @param selectCount
     * @param selectMoney
     */
    void onDataChange(String selectCount, String selectMoney);
}
