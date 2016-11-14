package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.MyStampGridViewBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

import static java.lang.String.valueOf;

/**
 * 我的邮集中GridView的适配器
 * Created by Angle on 2016/9/25.
 */
public class GridViewAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private int num = 0;//初始索引
    private ArrayList<MyStampGridViewBean.StampList> mList;
    private int index;
    private BitmapUtils bitmap;
    //这个是编辑按钮的标记
    private boolean flag;

    //每页显示的最大的数量
    private int PagerNum;
    private String mToken, mUser_id;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel, dialog_button_ok;
    private String mStamp_sn;
    private DataList mDataList;
    private static int offsetnum = 1000; // 显示邮集条数 步长(item条目数)

    public GridViewAdapter(Context context, ArrayList<MyStampGridViewBean.StampList> mList, int index, BitmapUtils bitmap, boolean flag) {
        this.mList = mList;
        this.index = index;
        this.bitmap = bitmap;
        this.flag = flag;
        MyLog.e("传入的" + flag);
        this.mLayoutInflater = LayoutInflater.from(context);
        PagerNum = context.getResources().getInteger(R.integer.PagerCount) * 2;
    }

    @Override
    public int getCount() {
        //这里判断数量的时候应该注意看是否是大于最大值了
        return mList.size() > (index + 1) * PagerNum ? PagerNum : (mList.size() - index * PagerNum);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position + index * PagerNum);
    }

    @Override
    public long getItemId(int position) {
        return position + index * PagerNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.mystamp_gridview_item, parent, false);
            holder.mImg = (ImageView) convertView.findViewById(R.id.mystamp_item_img);
            holder.mName = (TextView) convertView.findViewById(R.id.mystamp_item_name);
            holder.mCount = (TextView) convertView.findViewById(R.id.mystamp_item_count);
            holder.mNotCount = (TextView) convertView.findViewById(R.id.mystamp_item_nocCunt);
            holder.mEdit = (LinearLayout) convertView.findViewById(R.id.stamp_Edit);
            holder.mNotEdit = (LinearLayout) convertView.findViewById(R.id.stamp_NoEdit);
            holder.mAdd = (TextView) convertView.findViewById(R.id.mystamp_item_add);
            holder.mSubtract = (TextView) convertView.findViewById(R.id.mystamp_item_subtract);
            holder.mDelete = (ImageView) convertView.findViewById(R.id.mystamp_item_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //这里根据传入的flag进行判断是否进入编辑模式
        MyLog.e(flag + "");
        if (!flag) {//true代表非编辑模式
            holder.mEdit.setVisibility(View.GONE);
            holder.mNotEdit.setVisibility(View.VISIBLE);
            holder.mDelete.setVisibility(View.GONE);
        } else {//false代表编辑模式
            holder.mEdit.setVisibility(View.VISIBLE);
            holder.mNotEdit.setVisibility(View.GONE);
            holder.mDelete.setVisibility(View.VISIBLE);
        }


        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        int pos = position + index * PagerNum;
        //标题
        holder.mName.setText(mList.get(pos).getStamp_name());
        holder.mCount.setText(mList.get(pos).getStamp_count());// 编辑显示邮集数量
        holder.mNotCount.setText(mList.get(pos).getStamp_count()); // 不编辑邮集显示的数量
        bitmap.display(holder.mImg, mList.get(pos).getStamp_img());

        holder.mAdd.setTag(pos);
        holder.mAdd.setOnClickListener(this);
        holder.mSubtract.setTag(pos);
        holder.mSubtract.setOnClickListener(this);

        String mStampSn = mList.get(pos).getStamp_sn();
        MyLog.LogShitou("==000000000=====获取邮集编号", mStampSn);
        holder.mDelete.setTag(mStampSn);
//        holder.mDelete.setTag(pos);
        holder.mDelete.setOnClickListener(this);

        return convertView;
    }


    public class ViewHolder {
        public ImageView mImg, mDelete;//图片
        public TextView mName, mCount, mNotCount, mAdd, mSubtract;//名称,数量,非编辑状态下数量，添加按钮，减少按钮
        public LinearLayout mEdit, mNotEdit;
    }

    // 获取标识，用户ID
    private void GetTokenUserID() {
        SharedPreferences sp = mLayoutInflater.getContext().getSharedPreferences(StaticField.NAME, mLayoutInflater.getContext().MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mystamp_item_add://添加
//                addFlag = 1;
                MyStampGridViewBean.StampList stampAdd = mList.get((int) v.getTag());

                String mStampSn = stampAdd.getStamp_sn(); // 获取邮集编号

                String addCount = stampAdd.getStamp_count();
                String newAddCount = valueOf(Integer.valueOf(addCount) + 1);
                MyLog.LogShitou("==========增加的数量", "==增加的数量=" + newAddCount + "/==/" + mStampSn);
                mList.get((int) v.getTag()).setStamp_count(newAddCount);
//                notifyDataSetChanged();
                GetTokenUserID(); // 获取标识，用户ID
                UpDateGetInitNet(newAddCount, StaticField.XG, mStampSn); //修改邮集网络请求
                break;
            case R.id.mystamp_item_subtract://减少
//                addFlag = 2;
                MyStampGridViewBean.StampList stampSubtract = mList.get((int) v.getTag());

                String mStampSns = stampSubtract.getStamp_sn(); // 获取邮集编号

                String subtractCount = stampSubtract.getStamp_count();
                int newNum = Integer.valueOf(subtractCount) - 1;
                if (newNum < 1)
                    return;
                String newSubtractCount = valueOf(newNum);
                MyLog.LogShitou("==========减少的数量", "==减少的数量===" + newSubtractCount + "/==/" + mStampSns);
                mList.get((int) v.getTag()).setStamp_count(newSubtractCount);
//                notifyDataSetChanged();
                GetTokenUserID(); // 获取标识，用户ID
                UpDateGetInitNet(newSubtractCount, StaticField.XG, mStampSns); //修改邮集网络请求
                break;
            case R.id.mystamp_item_delete://删除
                mStamp_sn = String.valueOf(v.getTag());
                DeleteDialog();
                break;
        }
    }


    /**
     * 修改邮集网络请求
     *
     * @param stamp_count 邮集数量
     * @param op_type     操作类型
     * @param stampsn     邮票编号
     */
    private void UpDateGetInitNet(final String stamp_count, final String op_type, final String stampsn) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.MODIFY);// 修改邮集接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.STAMP_SN, stampsn);//  邮票编号
                params.put(StaticField.OP_TYPE, op_type);//  操作类型：SC：删除；JR加入；XG修改

                if (!op_type.equals("SC")) {
                    params.put(StaticField.STAMP_COUNT, stamp_count);//  邮票数量
                    MyLog.LogShitou(op_type + "/======修改邮集数量", stamp_count);
                }
                MyLog.LogShitou("===邮集编号", "===stampsn=" + stampsn);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(op_type + "/======修改邮集", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (op_type.equals("SC")) {// 删除

                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else { // 修改
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.XG_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS://删除邮集
                    Gson gson = new Gson();
                    BaseBean mBaseBean = gson.fromJson((String) msg.obj, BaseBean.class);
                    String code = mBaseBean.getRsp_code();
                    String msg1 = mBaseBean.getRsp_msg();
                    if (code.equals("0000")) {


                        GetInitNet(num); // 我的邮集列表网络请求
                    } else if (code.equals("1002")) {
                        MyToast.showShort(mLayoutInflater.getContext(), msg1);
                    }
                    break;
                case StaticField.CG_SUCCESS://邮集list
                    String msge = (String) msg.obj;
                    Gson gson1 = new Gson();
                    MyStampGridViewBean mOrderSweepBean = gson1.fromJson(msge, MyStampGridViewBean.class);
                    String mRsp_code = mOrderSweepBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mOrderSweepBean.getStamp_list();
                        String mTotalPrice = mOrderSweepBean.getTotal_amount();// 总资产
                        MyLog.LogShitou("/=====邮集条数", mList.size() + "");
                        if (mList != null && mList.size() != 0 && mTotalPrice != null) {
                            mDataList.GetDataList(mList, mTotalPrice);// 定义接口调用
                        }
                    }
                    break;

                case StaticField.XG_SUCCESS: // 修改
                    Gson gson2 = new Gson();
                    BaseBean mBaseBean1 = gson2.fromJson((String) msg.obj, BaseBean.class);
                    String code1 = mBaseBean1.getRsp_code();
                    String msg2 = mBaseBean1.getRsp_msg();
                    if (code1.equals("0000")) {
                        notifyDataSetChanged();// 刷新适配器
                        // 修改成功后，再次请求邮集列表数据，以便刷新邮集总资产
                        GetInitNet(num); // 我的邮集列表网络请求
//                        MyToast.showShort(mLayoutInflater.getContext(), msg2);
                    } else if (code1.equals("1002")) {
                        MyToast.showShort(mLayoutInflater.getContext(), msg2);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 删除弹出框
     */

    private void DeleteDialog() {
        prodialog = new ProgressDialog(mLayoutInflater.getContext());
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
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTokenUserID();   // 获取标识，用户ID
                UpDateGetInitNet(null, StaticField.SC, mStamp_sn); //修改邮集网络请求
                prodialog.dismiss();
            }
        });
    }


    // 定义一个接口给Fragment,删除成功，通知list刷新
    public interface DataList {
        void GetDataList(ArrayList<MyStampGridViewBean.StampList> mDataList, String str);
    }

    public void SetDataList(DataList dataList) {
        this.mDataList = dataList;
    }


    /**
     * 我的邮集列表网络请求
     *
     * @param num 初始化索引
     */
    private void GetInitNet(final int num) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ALBUMLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(offsetnum)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("适配器我的邮集", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.CG_SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        });
    }
}
