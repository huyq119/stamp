package cn.com.chinau.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.DesignerListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.DesignerBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.IndexListView;

/**
 * 设计家页面
 */
public class DesignerActivity extends BaseActivity {
    private View mDesignerContent;
    private View mDesignerTitle;
    private IndexListView mDesignerLV;
    private List<DesignerBean.Designer> mList;
    private ImageView mBack;//返回按钮
    private int num = 0; // 初始步长
    private static final int OFFSETNUM = 1000; // 首次请求条目数
    private PullToRefreshScrollView mDesignerSV;
    private ScrollView scrollView;
    private Button mTopBtn;

    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};

    @Override
    public View CreateTitle() {
        mDesignerTitle = View.inflate(this, R.layout.base_back_title, null);
        return mDesignerTitle;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    DesignerBean designerBean = gson.fromJson((String) msg.obj, DesignerBean.class);
                    initAdapter(designerBean);
                    break;
            }
        }
    };

    @Override
    public View CreateSuccess() {
        mDesignerContent = View.inflate(this, R.layout.activity_designer_content, null);
        initView();
        initData();
        initListener();
        return mDesignerContent;
    }

    private void initView() {
        mBack = (ImageView) mDesignerTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mDesignerTitle.findViewById(R.id.base_title);
        mTitle.setText("设计家");
        mDesignerLV = (IndexListView) mDesignerContent.findViewById(R.id.designer_lv);
        mTopBtn = (Button) mDesignerContent.findViewById(R.id.base_top_btn);// 置顶
    }

    private void initAdapter(DesignerBean designerBean) {
        mList = new ArrayList<>();
        mList = designerBean.getDesigner_list();

        mList.remove(0);
        mList.remove(1);
        mList.remove(2);
        mList.remove(3);
        mList.remove(4);
//        mList.remove(5);
//        mList.remove(6);
//        mList.remove(7);
//        mList.remove(8);

        DesignerListViewAdapter mLVAdapter = new DesignerListViewAdapter(this, mBitmap, mList);
        mDesignerLV.setAdapter(mLVAdapter, positionListener);
    }

    private void initListener() {
//        scrollView =mDesignerSV.getRefreshableView();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mDesignerLV.setOnItemClickListener(new IndexListView.onItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 跳转设计家详情页面
                String Designer_sn = mList.get(i).getDesigner_sn();// 设计家编号
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DESIGNERSN, Designer_sn);
                openActivityWitchAnimation(DesignerDetailActivity.class, bundle);
            }
        });
    }

    private IndexListView.AlphabetPositionListener positionListener = new IndexListView.AlphabetPositionListener() {
        @Override
        public int getPosition(String letter) {
//            mList.get(i).getEnglish_name().charAt(0);
            // 先注释
            for (int i = 0, count = mList.size(); i < count; i++) {
//                Character firstLetter = mList.get(i).getChinese_name().charAt(0);
                String name = mList.get(i).getChinese_name();
                MyLog.LogShitou("====name000=========这是啥", name);

//                if (!name.equals("——") && !name.equals("fljme--kejnf") && !name.equals("hsl") && !name.equals("zgxkls-lm")){
//                    String mName = getSpells(name);
//                    MyLog.LogShitou("====111111111111111========这是啥", mName);
//                        Character firstLetter = mName.charAt(0);
//                        if (firstLetter.equals("")) {
//                            if (firstLetter.toString().equals(letter)) {
//                                return i;
//                            }
//                        }
//                }
            }
            return UNKNOW;
        }
    };

    @Override
    public void AgainRequest() {
        initData();
    }

    /**
     * 设计家list网络请求
     */
    private void initData() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.DESIGNERLIST);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num));
                params.put(StaticField.OFFSET, String.valueOf(OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                MyLog.LogShitou("设计家List", "===" + result);
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {
            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
// 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {
        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n'
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

}