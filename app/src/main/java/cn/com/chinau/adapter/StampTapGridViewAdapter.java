package cn.com.chinau.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.LoginActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampTapBean;
import cn.com.chinau.dialog.AddStampDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;


/**
 * 邮票目录的GridView的适配器
 * Created by Administrator on 2016/7/28.
 */
public class StampTapGridViewAdapter extends BaseAdapter {

    private static final int DELDIALOG = 1;
    private ArrayList<StampTapBean.StampList> mList;//填入的集合
    private Context context;
    private BitmapUtils bitmap;
    private AddStampDialog mAdd;

    private String mToken,mUser_id,mStampSn;


    public StampTapGridViewAdapter(Context context, ArrayList<StampTapBean.StampList> mList, BitmapUtils bitmap) {
        this.mList = mList;
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.stamptap_gridview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.StampTap_item_icon);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.StampTap_item_money);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.StampTap_item_title);
            viewHolder.mAdd = (ImageView) view.findViewById(R.id.StampTap_item_add);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        StampTapBean.StampList stampList = mList.get(i);
        mStampSn = stampList.getStamp_sn();// 邮票编号
        viewHolder.mTitle.setText(stampList.getStamp_name());
        viewHolder.mMoney.setText("￥"+stampList.getCurrent_price());
        viewHolder.mAdd.setTag(mStampSn);
        //设置图片
        bitmap.display(viewHolder.mIcon, stampList.getStamp_img());
        //添加邮集点击按钮
        viewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTokenUserID();// 获取标识，用户ID
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)){

                    AddStampGetInitNet("1",StaticField.JR,String.valueOf(view.getTag())); //加入邮集网络请求
//                    MyLog.LogShitou("==11111====邮票编号",mStampSn);
                }else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("WithDraw","StampTap");
                    context.startActivity(intent);
                    //跳转动画
                    ((Activity)context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

            }
        });

        return view;
    }

    public class ViewHolder {
        public ImageView mIcon, mAdd;//图片,添加按钮
        public TextView mTitle, mMoney;//名称,钱数
    }

    // 获取标识，用户ID
    private void GetTokenUserID(){
         SharedPreferences sp = context.getSharedPreferences(StaticField.NAME,Context.MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
    }

    /**
     * 加入邮集网络请求
     * @param stamp_count　添加数量
     * @param op_type 操作类型：SC：删除；JR加入；XG修改
     */

    private void AddStampGetInitNet(final String stamp_count,final String op_type,final String stampsn){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.MODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.STAMP_SN, stampsn);//  邮票编号
                params.put(StaticField.STAMP_COUNT, stamp_count);//  邮票数量
                params.put(StaticField.OP_TYPE, op_type);//  操作类型：SC：删除；JR加入；XG修改

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                MyLog.LogShitou("===邮集编号", stampsn);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("===result加入邮集", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case  StaticField.SUCCESS://加入邮集
                    Gson gson = new Gson();
                    BaseBean mBaseBean = gson.fromJson((String) msg.obj, BaseBean.class);
                   String code = mBaseBean.getRsp_code();
                    if (code.equals("0000")){
                        mAdd = new AddStampDialog(context);
                        mAdd.show();
                        mHandler.sendEmptyMessageDelayed(DELDIALOG,1000);
                    }
                    break;
                case  DELDIALOG://加入邮集
                    if (mAdd != null){
                        mAdd.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
