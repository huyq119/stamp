package cn.com.chinau.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.bean.AddressBean;
import cn.com.chinau.widget.OnWheelChangedListener;
import cn.com.chinau.widget.WheelView;
import cn.com.chinau.widget.adapters.ArrayWheelAdapter;

import static cn.com.chinau.R.id.province;

/**
 * 省市县三级联动的PopupWindow的页面
 * Created by Administrator on 2016/8/22.
 */
public class AddressPopupWindow extends PopupWindow implements OnWheelChangedListener {

    private View mAddressView;
    private AddressBean mAddress;
    private Context context;
    /**
     * 省的WheelView控件
     */
    private WheelView mProvinceView;
    /**
     * 市的WheelView控件
     */
    private WheelView mCityView;
    /**
     * 区的WheelView控件
     */
    private WheelView mAreaView;
    /**
     * 所有省
     */
    private String[] mProvince;
    private AddressBean.Province[] Province;
    /**
     * key - 省 value - 市
     */
//    protected Map<String, String[]> mCityMap = new HashMap<>();
    protected Map<String, AddressBean.City[]> CityMap = new HashMap<>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, AddressBean.Area[]> AreaMap = new HashMap<>();
    /**
     * 区县的集合
     */
//    protected Map<String, String> mAreaCode = new HashMap<>();

    public String mCurrentProvince;//当前省份的名字
    public String mCurrentCity;//当前城市的名字
    public String mCurrentArea;//当前区的名字
    private TextView mFinish;//完成按钮
    private TextView mCancel;//取消按钮

    public String mCurrentProvinceCode;
    public String mCurrentCityCode;
    public String mCurrentAreaCode;


    /**
     * @param context         上下文
     * @param mAddress        地址信息实体类
     * @param onClickListener 监听
     */
    public AddressPopupWindow(Context context, AddressBean mAddress, View.OnClickListener onClickListener) {
        super(context);
        this.mAddress = mAddress;
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAddressView = inflater.inflate(R.layout.popupwindow_address, null);


        setView();
        initView();
        initData();
        initAdapter();
        initListener(onClickListener);
//        initListener(onClickListener);
    }


    /**
     * 设置显示参数
     */
    private void setView() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mAddressView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        mProvinceView = (WheelView) mAddressView.findViewById(province);
        mCityView = (WheelView) mAddressView.findViewById(R.id.city);
        mAreaView = (WheelView) mAddressView.findViewById(R.id.district);
        mCancel = (TextView) mAddressView.findViewById(R.id.address_cancel);
        mFinish = (TextView) mAddressView.findViewById(R.id.address_finish);
    }


    /**
     * 设置省市县的数据
     */
    private void initData() {
        //获取省份的集合数据
        List<AddressBean.Province> province_list = mAddress.getProvince_list();

        //这里应该创建一个对象去接收

        mProvince = new String[province_list.size()];
        Province = new AddressBean.Province[province_list.size()];

        for (int i = 0; i < province_list.size(); i++) {
            // 遍历所有省的数据
            mProvince[i] = province_list.get(i).getProvince_name();
            Province[i] = new AddressBean.Province(province_list.get(i).getProvince_code(), province_list.get(i).getProvince_name());


            //获取当前省对应的城市信息
            List<AddressBean.City> cityList = province_list.get(i).getCity_list();
            //创建城市数组
            String[] cityNames = new String[cityList.size()];
            AddressBean.City[] cityName = new AddressBean.City[cityList.size()];

            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityName[j] = new AddressBean.City(cityList.get(j).getCity_code(), cityList.get(j).getCity_name());
                cityNames[j] = cityList.get(j).getCity_name();


                //获取当前市对应的地区信息
                List<AddressBean.Area> AreaList = cityList.get(j).getArea_list();
                //用于记录区/县的数组
                String[] AreaArray = new String[AreaList.size()];
                //区/县的邮编,这里应该记录一下,应该创建一个实体类记录一下
                AddressBean.Area[] AreaCode = new AddressBean.Area[AreaList.size()];
                for (int k = 0; k < AreaList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    AreaCode[k] = new AddressBean.Area(AreaList.get(k).getArea_name(), AreaList.get(k).getArea_code());
//                    // 区/县对于的邮编，保存到mAreaCode集合中
//                    mAreaCode.put(AreaList.get(k).getArea_name(), AreaList.get(k).getArea_code());
//                    AreaCode[k] = area;
//                    AreaArray[k] = area.getArea_name();
                }
                // 市-区/县的数据，保存到mAreaMap集合中
                AreaMap.put(cityNames[j], AreaCode);
            }
            // 省-市的数据，保存到mCityMap集合中
            CityMap.put(province_list.get(i).getProvince_name(), cityName);
        }
    }


    private void initAdapter() {
        //这里传递集合的时候必须是String[] 类型的
        mProvinceView.setViewAdapter(new ArrayWheelAdapter<>(context, mProvince));
        mProvinceView.setVisibleItems(7);
        mCityView.setVisibleItems(7);
        mAreaView.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    private void initListener(View.OnClickListener onClickListener) {
        // 添加change事件
        mProvinceView.addChangingListener(this);
        // 添加change事件
        mCityView.addChangingListener(this);
        // 添加change事件
        mAreaView.addChangingListener(this);

        mCancel.setOnClickListener(onClickListener);
        mFinish.setOnClickListener(onClickListener);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mProvinceView) {
            updateCities();
        } else if (wheel == mCityView) {
            updateAreas();
        } else if (wheel == mAreaView) {
            //这个是获取地区的信息
            AddressBean.Area area = AreaMap.get(mCurrentCity)[newValue];

            mCurrentArea = area.getArea_name();
            mCurrentAreaCode = area.getArea_code();

//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }


    /**
     * 更新城市信息
     */
    private void updateCities() {
        int pCurrent = mProvinceView.getCurrentItem();
//        mCurrentProvince = mProvince[pCurrent];
        //省份的数据
        mCurrentProvince = Province[pCurrent].getProvince_name();
        //省份的id
        mCurrentProvinceCode = Province[pCurrent].getProvince_code();
//
//        String[] cities = mCityMap.get(mCurrentProvince);
        AddressBean.City[] cities = CityMap.get(mCurrentProvince);

        String[] citie = new String[cities.length];
        //这里应该是新来一个数组吧
        for (int i = 0; i < cities.length; i++) {
            citie[i] = cities[i].getCity_name();
        }

        if (citie == null) {
            citie = new String[]{""};

        }
        mCityView.setViewAdapter(new ArrayWheelAdapter<>(context, citie));
        mCityView.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 更新地区的信息
     */
    private void updateAreas() {
        int pCurrent = mCityView.getCurrentItem();

//        mCurrentCity = mCityMap.get(mCurrentProvince)[pCurrent];
        AddressBean.City city = CityMap.get(mCurrentProvince)[pCurrent];
        mCurrentCity = city.getCity_name();
        mCurrentCityCode = city.getCity_code();


        AddressBean.Area[] area = AreaMap.get(mCurrentCity);
        String[] areas = new String[area.length];
        for (int i = 0; i < area.length; i++) {
            areas[i] = area[i].getArea_name();
        }

//        String[] areas = mAreaMap.get(mCurrentCity);

        if (areas == null) {
            areas = new String[]{""};
        }

        mAreaView.setViewAdapter(new ArrayWheelAdapter<>(context, areas));
        mAreaView.setCurrentItem(0);
        mCurrentArea = areas[0];
        mCurrentAreaCode = area[0].getArea_code();
    }
}
