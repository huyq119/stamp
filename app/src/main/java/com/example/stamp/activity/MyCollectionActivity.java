package com.example.stamp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.CollectionListViewAdapter;
import com.example.stamp.adapter.MyCollectionListViewEditerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.CollectionBean;
import com.example.stamp.dialog.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏页面
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    private View mCollectionTitle;
    private View mCollectionContent;
    private ListView mCollection_lv;
    private List<CollectionBean.Collection> mList;
    private ImageView Back;
    private TextView mEdit;
    private int checkNum; // 记录选中的条目数量
    private CheckBox mSelect;
    private TextView mDelete;
    private CollectionListViewAdapter adapter;
    private LinearLayout cocollection_edit;
    private MyCollectionListViewEditerAdapter mListAdapter;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel;
    private Button dialog_button_ok;
    private boolean isDel = true;
    @Override
    public View CreateTitle() {
        mCollectionTitle = View.inflate(this, R.layout.base_font_title, null);
        return mCollectionTitle;
    }
    @Override
    public View CreateSuccess() {
        mCollectionContent = View.inflate(this, R.layout.activity_mycollection_content, null);
        initView();
        initData();
        initAdapter();
        initListener();
        return mCollectionContent;
    }
    private void initView() {
        Back = (ImageView) mCollectionTitle.findViewById(R.id.base_title_back);
        TextView Title = (TextView) mCollectionTitle.findViewById(R.id.base_title);
        Title.setText("收藏夹");
        mEdit = (TextView) mCollectionTitle.findViewById(R.id.base_edit);
        mEdit.setText("编辑");
        mCollection_lv = (ListView) mCollectionContent.findViewById(R.id.collection_listView);
        mSelect = (CheckBox) mCollectionContent.findViewById(R.id.checkbox_select);

        mDelete = (TextView)mCollectionContent.findViewById(R.id.delete);
        cocollection_edit = (LinearLayout)mCollectionContent.findViewById(R.id.collection_edit);
    }
    private void initData(){
        mList = new ArrayList<>();
        CollectionBean.Collection collection;
        collection = new CollectionBean.Collection("庚申年", "YH", "YS", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "WH", "YS", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "JPZ", "JP", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "WKS", "JP", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "YJS", "JP", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "YH", "SC", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "WH", "SC", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
    }
    private void initAdapter() {
        adapter = new CollectionListViewAdapter(this, mBitmap, mList);
        mCollection_lv.setAdapter(adapter);

    }
    private void initListener() {
        Back.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mSelect.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_edit:
                if (mEdit != null && isDel) {
                    isDel = false;
                    mEdit.setText("完成");
                    mListAdapter = new MyCollectionListViewEditerAdapter(this, mBitmap, mList);
                    adapter.notifyDataSetChanged();
                    mCollection_lv.setAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();
                    cocollection_edit.setVisibility(View.VISIBLE);
                } else if (!isDel && mEdit != null) {
                    isDel = true;
                    mEdit.setText("编辑");
                    adapter = new CollectionListViewAdapter(this, mBitmap, mList);
                    mCollection_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    cocollection_edit.setVisibility(View.GONE);
                }
                break;
            case R.id.checkbox_select:// 反选按钮的回调接口
                if(mSelect.isClickable()==true){
                    // 遍历list的长度，将已选的设为未选，未选的设为已选
                    for (int i = 0; i < mList.size(); i++) {
                        if (MyCollectionListViewEditerAdapter.getIsSelected().get(i)) {
                            MyCollectionListViewEditerAdapter.getIsSelected().put(i, false);
                            checkNum--;
                        } else {
                            MyCollectionListViewEditerAdapter.getIsSelected().put(i, true);
                            checkNum++;
                        }
                    }
                    // 刷新listview和TextView的显示
                    dataChanged();
                }
                break;
            case R.id.delete:
                DeleteDialog();//删除弹出框
                break;
            default:
                break;
        }
    }
    /**
     * 删除弹出框
     */
    private void DeleteDialog() {
        prodialog = new ProgressDialog(this);
        prodialog.show();
        Title = (TextView) prodialog.findViewById(R.id.title_tv);
        Title.setText("确定要删除吗？");
        // 取消
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);// 取消
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);
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
                ArrayList<CollectionBean.Collection> deleteList = new ArrayList<CollectionBean.Collection>();
                for (int i = 0; i < mList.size(); i++){
                    CollectionBean.Collection group = mList.get(i);
                    if (group.isChoosed()){
                        deleteList.add(group);
                    }
                    mList.removeAll(deleteList);
                    mListAdapter.notifyDataSetChanged();
                }
                prodialog.dismiss();// 关闭Dialog
            }
        });
    }
    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        mListAdapter.notifyDataSetChanged();
    }
    @Override
    public void AgainRequest(){
    }


}
