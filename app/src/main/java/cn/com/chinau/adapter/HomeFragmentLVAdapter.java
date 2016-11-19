package cn.com.chinau.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.HomeImageH5Activity;
import cn.com.chinau.bean.HomeBean;
import cn.com.chinau.bitmap.BitmapHelper;

/**
 * 首页listView的适配器
 * Created by Angle on 2016/11/19.
 */

public class HomeFragmentLVAdapter extends BaseAdapter implements View.OnClickListener {

    private List<HomeBean.Group> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int TYPE = 3;
    public BitmapUtils mBitmap;


    private interface ViewType {
        // 必须从0开始，因为ListView内部是用一个数组维护ViewType的。
        int ONE = 0;
        int TWO = 1;
        int THREE = 2;
    }

    public HomeFragmentLVAdapter(Context context, List<HomeBean.Group> list) {
        this.mList = list;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mBitmap = BitmapHelper.getBitmapUtils();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getTemplate().equals("A-yellow")) {
            return ViewType.ONE;
        } else if (mList.get(position).getTemplate().equals("B-blue")) {
            return ViewType.TWO;
        } else {
            return ViewType.THREE;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ViewType.ONE) {
            YellowHolder yellowHolder;
            if (convertView == null) {
                yellowHolder = new YellowHolder();
                convertView = mInflater.inflate(R.layout.home_chinese_zodiac, parent, false);
                yellowHolder.FirstTitle = (TextView) convertView.findViewById(R.id.home_first_title);
                //第一个小布局的图片
                yellowHolder.FirstImage1 = (ImageView) convertView.findViewById(R.id.home_chinese_image1);
                yellowHolder.FirstImage2 = (ImageView) convertView.findViewById(R.id.home_chinese_image2);
                yellowHolder.FirstImage3 = (ImageView) convertView.findViewById(R.id.home_chinese_image3);
                yellowHolder.FirstImage4 = (ImageView) convertView.findViewById(R.id.home_chinese_image4);
                convertView.setTag(yellowHolder);
            } else {
                yellowHolder = (YellowHolder) convertView.getTag();
            }
            yellowHolder.FirstImage1.setTag(position);
            yellowHolder.FirstImage1.setOnClickListener(this);

            yellowHolder.FirstImage2.setTag(position);
            yellowHolder.FirstImage2.setOnClickListener(this);

            yellowHolder.FirstImage3.setTag(position);
            yellowHolder.FirstImage3.setOnClickListener(this);

            yellowHolder.FirstImage4.setTag(position);
            yellowHolder.FirstImage4.setOnClickListener(this);

            mBitmap.display(yellowHolder.FirstImage1, mList.get(position).getChild_list().get(0).getImg_url());
            mBitmap.display(yellowHolder.FirstImage2, mList.get(position).getChild_list().get(1).getImg_url());
            mBitmap.display(yellowHolder.FirstImage3, mList.get(position).getChild_list().get(2).getImg_url());
            mBitmap.display(yellowHolder.FirstImage4, mList.get(position).getChild_list().get(3).getImg_url());

            yellowHolder.FirstTitle.setText(mList.get(position).getGroup_name());

        } else if (getItemViewType(position) == ViewType.TWO) {

            BlueHolder blueHolder;
            if (convertView == null) {
                blueHolder = new BlueHolder();
                convertView = mInflater.inflate(R.layout.home_classical_literture, parent, false);
                blueHolder.mSecondTitle = (TextView) convertView.findViewById(R.id.home_second_title);
                //第一个小布局的图片
                //第二个小布局的图片
                blueHolder.mSecondImage1 = (ImageView) convertView.findViewById(R.id.home_classical_image1);
                blueHolder.mSecondImage2 = (ImageView) convertView.findViewById(R.id.home_classical_image2);
                blueHolder.mSecondImage3 = (ImageView) convertView.findViewById(R.id.home_classical_image3);
                blueHolder.mSecondImage4 = (ImageView) convertView.findViewById(R.id.home_classical_image4);
                convertView.setTag(blueHolder);
            } else {
                blueHolder = (BlueHolder) convertView.getTag();
            }
            blueHolder.mSecondImage1.setTag(position);
            blueHolder.mSecondImage1.setOnClickListener(this);

            blueHolder.mSecondImage2.setTag(position);
            blueHolder.mSecondImage2.setOnClickListener(this);

            blueHolder.mSecondImage3.setTag(position);
            blueHolder.mSecondImage3.setOnClickListener(this);

            blueHolder.mSecondImage4.setTag(position);
            blueHolder.mSecondImage4.setOnClickListener(this);


            mBitmap.display(blueHolder.mSecondImage1, mList.get(position).getChild_list().get(0).getImg_url());
            mBitmap.display(blueHolder.mSecondImage2, mList.get(position).getChild_list().get(1).getImg_url());
            mBitmap.display(blueHolder.mSecondImage3, mList.get(position).getChild_list().get(2).getImg_url());
            mBitmap.display(blueHolder.mSecondImage4, mList.get(position).getChild_list().get(3).getImg_url());

            blueHolder.mSecondTitle.setText(mList.get(position).getGroup_name());

        } else {
            RedHolder redHolder;
            if (convertView == null) {
                redHolder = new RedHolder();
                convertView = mInflater.inflate(R.layout.home_folk_art, parent, false);
                redHolder.mThreeTitle = (TextView) convertView.findViewById(R.id.home_three_title);
                //第一个小布局的图片
                //第二个小布局的图片
                //第三个小布局的图片
                redHolder.mThreeImage1 = (ImageView) convertView.findViewById(R.id.home_folk_image1);
                redHolder.mThreeImage2 = (ImageView) convertView.findViewById(R.id.home_folk_image2);
                redHolder.mThreeImage3 = (ImageView) convertView.findViewById(R.id.home_folk_image3);
                redHolder.mThreeImage4 = (ImageView) convertView.findViewById(R.id.home_folk_image4);
                redHolder.mThreeImage5 = (ImageView) convertView.findViewById(R.id.home_folk_image5);
                convertView.setTag(redHolder);
            } else {
                redHolder = (RedHolder) convertView.getTag();
            }
            redHolder.mThreeImage1.setTag(position);
            redHolder.mThreeImage1.setOnClickListener(this);

            redHolder.mThreeImage2.setTag(position);
            redHolder.mThreeImage2.setOnClickListener(this);

            redHolder.mThreeImage3.setTag(position);
            redHolder.mThreeImage3.setOnClickListener(this);

            redHolder.mThreeImage4.setTag(position);
            redHolder.mThreeImage4.setOnClickListener(this);

            redHolder.mThreeImage5.setTag(position);
            redHolder.mThreeImage5.setOnClickListener(this);

            mBitmap.display(redHolder.mThreeImage1, mList.get(position).getChild_list().get(0).getImg_url());
            mBitmap.display(redHolder.mThreeImage2, mList.get(position).getChild_list().get(1).getImg_url());
            mBitmap.display(redHolder.mThreeImage3, mList.get(position).getChild_list().get(2).getImg_url());
            mBitmap.display(redHolder.mThreeImage4, mList.get(position).getChild_list().get(3).getImg_url());
            mBitmap.display(redHolder.mThreeImage5, mList.get(position).getChild_list().get(4).getImg_url());

            redHolder.mThreeTitle.setText(mList.get(position).getGroup_name());
        }

        return convertView;
    }

    private class YellowHolder {
        private TextView FirstTitle;
        //第一个小布局的图片
        private ImageView FirstImage1;
        private ImageView FirstImage2;
        private ImageView FirstImage3;
        private ImageView FirstImage4;
    }

    private class BlueHolder {
        private TextView mSecondTitle;//第二个标题
        private ImageView mSecondImage1;
        private ImageView mSecondImage2;
        private ImageView mSecondImage3;
        private ImageView mSecondImage4;
    }

    private class RedHolder {
        private TextView mThreeTitle;//第三个标题
        //第三个小布局的图片
        private ImageView mThreeImage1;
        private ImageView mThreeImage2;
        private ImageView mThreeImage3;
        private ImageView mThreeImage4;
        private ImageView mThreeImage5;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //第一个View的
            case R.id.home_chinese_image1:
                int position = (int) v.getTag();
                List<HomeBean.Child> child_list = mList.get(position).getChild_list();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, child_list.get(0).getTitle());// 标题
                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_chinese_image2:
                int position1 = (int) v.getTag();
                List<HomeBean.Child> child_list1 = mList.get(position1).getChild_list();
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticField.HOMETITLE, child_list1.get(1).getTitle());// 标题
                bundle1.putString(StaticField.HOMEURL, child_list1.get(1).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle1);
                break;
            case R.id.home_chinese_image3:
                int position3 = (int) v.getTag();
                List<HomeBean.Child> child_list3 = mList.get(position3).getChild_list();
                Bundle bundle3 = new Bundle();
                bundle3.putString(StaticField.HOMETITLE, child_list3.get(2).getTitle());// 标题
                bundle3.putString(StaticField.HOMEURL, child_list3.get(2).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle3);
                break;
            case R.id.home_chinese_image4:
                int position4 = (int) v.getTag();
                List<HomeBean.Child> child_list4 = mList.get(position4).getChild_list();
                Bundle bundle4 = new Bundle();
                bundle4.putString(StaticField.HOMETITLE, child_list4.get(3).getTitle());// 标题
                bundle4.putString(StaticField.HOMEURL, child_list4.get(3).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle4);
                break;

            //第二个View
            case R.id.home_classical_image1:
                int position5 = (int) v.getTag();
                List<HomeBean.Child> child_list5 = mList.get(position5).getChild_list();
                Bundle bundle5 = new Bundle();
                bundle5.putString(StaticField.HOMETITLE, child_list5.get(0).getTitle());// 标题
                bundle5.putString(StaticField.HOMEURL, child_list5.get(0).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle5);
                break;
            case R.id.home_classical_image2:
                int position6 = (int) v.getTag();
                List<HomeBean.Child> child_list6 = mList.get(position6).getChild_list();
                Bundle bundle6 = new Bundle();
                bundle6.putString(StaticField.HOMETITLE, child_list6.get(1).getTitle());// 标题
                bundle6.putString(StaticField.HOMEURL, child_list6.get(1).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle6);
                break;
            case R.id.home_classical_image3:
                int position7 = (int) v.getTag();
                List<HomeBean.Child> child_list7 = mList.get(position7).getChild_list();
                Bundle bundle7 = new Bundle();
                bundle7.putString(StaticField.HOMETITLE, child_list7.get(2).getTitle());// 标题
                bundle7.putString(StaticField.HOMEURL, child_list7.get(2).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle7);
                break;
            case R.id.home_classical_image4:
                int position8 = (int) v.getTag();
                List<HomeBean.Child> child_list8 = mList.get(position8).getChild_list();
                Bundle bundle8 = new Bundle();
                bundle8.putString(StaticField.HOMETITLE, child_list8.get(3).getTitle());// 标题
                bundle8.putString(StaticField.HOMEURL, child_list8.get(3).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle8);
                break;

            //第三个
            case R.id.home_folk_image1:
                int position9 = (int) v.getTag();
                List<HomeBean.Child> child_list9 = mList.get(position9).getChild_list();
                Bundle bundle9 = new Bundle();
                bundle9.putString(StaticField.HOMETITLE, child_list9.get(0).getTitle());// 标题
                bundle9.putString(StaticField.HOMEURL, child_list9.get(0).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle9);
                break;
            case R.id.home_folk_image2:
                int position10 = (int) v.getTag();
                List<HomeBean.Child> child_list10 = mList.get(position10).getChild_list();
                Bundle bundle10 = new Bundle();
                bundle10.putString(StaticField.HOMETITLE, child_list10.get(1).getTitle());// 标题
                bundle10.putString(StaticField.HOMEURL, child_list10.get(1).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle10);
                break;
            case R.id.home_folk_image3:
                int position11 = (int) v.getTag();
                List<HomeBean.Child> child_list11 = mList.get(position11).getChild_list();
                Bundle bundle11 = new Bundle();
                bundle11.putString(StaticField.HOMETITLE, child_list11.get(2).getTitle());// 标题
                bundle11.putString(StaticField.HOMEURL, child_list11.get(2).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle11);
                break;
            case R.id.home_folk_image4:
                int position12 = (int) v.getTag();
                List<HomeBean.Child> child_list12 = mList.get(position12).getChild_list();
                Bundle bundle12 = new Bundle();
                bundle12.putString(StaticField.HOMETITLE, child_list12.get(3).getTitle());// 标题
                bundle12.putString(StaticField.HOMEURL, child_list12.get(3).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle12);
                break;
            case R.id.home_folk_image5:
                int position13 = (int) v.getTag();
                List<HomeBean.Child> child_list13 = mList.get(position13).getChild_list();
                Bundle bundle13 = new Bundle();
                bundle13.putString(StaticField.HOMETITLE, child_list13.get(4).getTitle());// 标题
                bundle13.putString(StaticField.HOMEURL, child_list13.get(4).getH5_url()); // 显示页面的url
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle13);
                break;
        }
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据,带动画
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivityWitchAnimation(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        mContext.startActivity(intent);
        //跳转动画
//        (MainActivity) mContext.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

}
