package cn.com.chinau;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.com.chinau.adapter.MainViewPagerAdapter;
import cn.com.chinau.fragment.HomeFragment;
import cn.com.chinau.fragment.MyFragment;
import cn.com.chinau.fragment.PanStampFragment;
import cn.com.chinau.fragment.ShopFragment;
import cn.com.chinau.fragment.StampTapFragment;
import cn.com.chinau.view.LazyViewPager;
import cn.com.chinau.view.NoScrollViewpager;

/**
 * 主页面的显示
 */
public class MainActivity extends FragmentActivity implements LazyViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {


    private NoScrollViewpager mViewPager; // 不能滑动的ViewPager
    private RadioGroup mRadioGroup;
    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initAdapter();
        initListener();
        setCurrent();// 设置开始显示的页面
    }


    /**
     * 初始化布局
     */
    private void initView() {
        mViewPager = (NoScrollViewpager) findViewById(R.id.Main_ViewPager);
        mRadioGroup = (RadioGroup) findViewById(R.id.Main_RadioGroup);
        //初始化当前显示的tab
        mViewPager.setCurrentItem(0);
        mRadioGroup.check(R.id.Radio_home);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        mList.add(homeFragment);
        StampTapFragment stampTapFragment = new StampTapFragment();
        mList.add(stampTapFragment);
        PanStampFragment panStampFragment = new PanStampFragment();
        mList.add(panStampFragment);
        ShopFragment shopFragment = new ShopFragment();
        mList.add(shopFragment);
        MyFragment myFragment = new MyFragment();
        mList.add(myFragment);
    }

    /**
     * 设置适配器
     */
    private void initAdapter() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(adapter);
    }


    /**
     * 监听
     */
    public void initListener() {
        mViewPager.setOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mViewPager.setOffscreenPageLimit(0);
    }

    /**
     * 设置显示的页面
     */
    private void setCurrent() {
        Intent intent = getIntent();
        String Login = intent.getStringExtra("Login");
        if (Login != null) {
            if (Login.equals("login")) {
                mViewPager.setCurrentItem(4);
                mRadioGroup.check(R.id.Radio_my);

            } else if (Login.equals("SelfMallDetail") ) {
                mViewPager.setCurrentItem(3);
                mRadioGroup.check(R.id.Radio_shop);
            } else if (Login.equals("StampDetail")) {
                mViewPager.setCurrentItem(3);
                mRadioGroup.check(R.id.Radio_shop);
            }
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.Radio_home);
                break;
            case 1:
                mRadioGroup.check(R.id.Radio_stamptap);
                break;
            case 2:
                mRadioGroup.check(R.id.Radio_panstamp);
                break;
            case 3:
                mRadioGroup.check(R.id.Radio_shop);
                break;
            case 4:
                mRadioGroup.check(R.id.Radio_my);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.Radio_home:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.Radio_stamptap:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.Radio_panstamp:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.Radio_shop:
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.Radio_my:
                mViewPager.setCurrentItem(4, false);
                break;
            default:
                break;
        }
    }
}
