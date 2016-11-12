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

import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.MyStampGridViewBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 我的邮集中GridView的适配器
 * Created by Angle on 2016/9/25.
 */
public class GridViewAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private int num = 0;//初始索引
    private List<MyStampGridViewBean.StampList> mList;
    private int index;
    private BitmapUtils bitmap;
    //这个是编辑按钮的标记
    private boolean flag;

    //每页显示的最大的数量
    private int PagerNum;
    private String mToken,mUser_id;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel,dialog_button_ok;

    public GridViewAdapter(Context context, List<MyStampGridViewBean.StampList> mList, int index, BitmapUtils bitmap, boolean flag) {
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
        if (!flag) {//true代表编辑模式
            holder.mEdit.setVisibility(View.GONE);
            holder.mNotEdit.setVisibility(View.VISIBLE);
        } else {//false代表非编辑模式
            holder.mEdit.setVisibility(View.VISIBLE);
            holder.mNotEdit.setVisibility(View.GONE);
        }


        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        int pos = position + index * PagerNum;
        //标题
        holder.mName.setText(mList.get(pos).getStamp_name());
        holder.mCount.setText(mList.get(pos).getStamp_count());
        holder.mNotCount.setText(mList.get(pos).getStamp_count());
        bitmap.display(holder.mImg, mList.get(pos).getStamp_img());

        holder.mAdd.setTag(pos);
        holder.mAdd.setOnClickListener(this);
        holder.mSubtract.setTag(pos);
        holder.mSubtract.setOnClickListener(this);
        String mStampSn = mList.get(pos).getStamp_sn();
        holder.mDelete.setTag(mStampSn);
        holder.mDelete.setTag(pos);
        holder.mDelete.setOnClickListener(this);
        return convertView;
    }


    public class ViewHolder {
        public ImageView mImg,mDelete;//图片
        public TextView mName, mCount, mNotCount, mAdd, mSubtract;//名称,数量,非编辑状态下数量，添加按钮，减少按钮
        public LinearLayout mEdit, mNotEdit;
    }

    // 获取标识，用户ID
    private void GetTokenUserID(){
        SharedPreferences sp = context.getSharedPreferences(StaticField.NAME,Context.MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mystamp_item_add://添加
                MyStampGridViewBean.StampList stampAdd = mList.get((int) v.getTag());
                String addCount = stampAdd.getStamp_count();
                String newAddCount = String.valueOf(Integer.valueOf(addCount) + 1);
                mList.get((int) v.getTag()).setStamp_count(newAddCount);
                notifyDataSetChanged();
                break;
            case R.id.mystamp_item_subtract://减少
                MyStampGridViewBean.StampList stampSubtract= mList.get((int) v.getTag());
                String subtractCount = stampSubtract.getStamp_count();
                int newNum = Integer.valueOf(subtractCount) - 1;
                if (newNum<0)
                    return;
                String newSubtractCount = String.valueOf(newNum);
                mList.get((int) v.getTag()).setStamp_count(newSubtractCount);
                notifyDataSetChanged();
                break;
            case R.id.mystamp_item_delete://删除
//                DeleteDialog();
                break;
        }
    }


    /**
     * 修改邮集网络请求
     */
    private void UpDateGetInitNet(final String stamp_count, final String op_type,final String stampsn) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.MODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.STAMP_SN, stampsn);//  邮票编号
                params.put(StaticField.OP_TYPE, op_type);//  操作类型：SC：删除；JR加入；XG修改
                if(op_type.equals("SC")){
                    params.put(StaticField.STAMP_COUNT, stamp_count);//  邮票数量
                }
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

               String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou(op_type+"/======修改邮集", result);

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
                case  StaticField.SUCCESS://修改邮集
                    Gson gson = new Gson();
                    BaseBean mBaseBean = gson.fromJson((String) msg.obj, BaseBean.class);
                    String code = mBaseBean.getRsp_code();
                    if (code.equals("0000")){

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
        prodialog = new ProgressDialog(context);
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
                GetTokenUserID();
                UpDateGetInitNet(null,StaticField.SC,String.valueOf(view.getTag())); //加入邮集网络请求
                prodialog.dismiss();
            }
        });
    }
}
