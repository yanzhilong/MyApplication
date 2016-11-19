package com.englishlearn.myapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.englishlearn.myapplication.activityforresult.multiple.MultipleActivity;
import com.englishlearn.myapplication.dialog.CreateAndUpdateItemFragment;
import com.englishlearn.myapplication.dialog.DeleteConfirmFragment;
import com.englishlearn.myapplication.dialog.ItemsSelectFragment;
import com.englishlearn.myapplication.dialog.OperateConfirmFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-11-19.
 * 句子单记事多选操作类
 */


/**
 *
 * @param <T> 分级名称
 * @param <O> //列表类型
 */
public abstract class MultipleAdapter<T,O> {

    public static final int REQUESTCODE = 0;//多选请求码
    public static final int SELETCREQUESTCODE = 1;//单选选请求码

    private Fragment fragment;
    private Activity activity;
    private int tag;
    private ItemsSelectFragment itemsSelectFragment;

    public MultipleAdapter(Fragment fragment, Activity activity) {
        this.fragment = fragment;
        this.activity = activity;
    }

    protected abstract T getListGroup();//获得当前列表的分组

    protected abstract void operationListGroup(T t,int flag);//操作列表分组

    protected abstract List<O> getList();//获得列表

    protected abstract void operationList(List<O> list, T group, int tag);//操作列表

    protected abstract boolean selectCreate(int flag);//创建数据项

    protected abstract boolean selectResult(int position,int flag);//选择返回

    protected abstract String getMultipleItemName(O o);

    protected abstract void createComplete(String name,int createflag);//创建新我名称


    private int createflag;
    /**
     * 创建新的 名称
     */
    public void createAndUpdate(String title,String oldName,int flag){

        createflag = flag;
        CreateAndUpdateItemFragment createItemFragment = new CreateAndUpdateItemFragment();

        if(!title.isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString(CreateAndUpdateItemFragment.TITLE,title);
            bundle.putString(CreateAndUpdateItemFragment.OLDNAME,oldName);
            createItemFragment.setArguments(bundle);
        }
        createItemFragment.setCreateItemListener(new CreateAndUpdateItemFragment.CreateItemListener() {
            @Override
            public void onComplete(String name) {
                if(!name.isEmpty()){
                    createComplete(name,createflag);
                }
            }
        });
        createItemFragment.show(fragment.getFragmentManager(),"create");
    }



    public void notifyDataSetChanged(String[] arrays){
        itemsSelectFragment.notifyDataSetChanged(arrays);
    }

    //选择分组
    public void selectItems(int tag){

        itemsSelectFragment = new ItemsSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemsSelectFragment.FLAG,tag);
        itemsSelectFragment.setTargetFragment(fragment, SELETCREQUESTCODE);
        itemsSelectFragment.setArguments(bundle);
        itemsSelectFragment.show(fragment.getFragmentManager(),"sentencecollectgroup");
    }


    //操作列表
    public void operationList(int tag){
        this.tag = tag;
        showMultipleSelect();//显示多选界面
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {

            case REQUESTCODE:
                if(resultCode == Activity.RESULT_OK){
                    List<Integer> checkedlist = data.getIntegerArrayListExtra(MultipleActivity.CHECKEDARRAY);
                    List<O> checkeds = new ArrayList<>();
                    for(int i = 0; i < getList().size(); i++){
                        if(checkedlist.contains(i)){
                            checkeds.add(getList().get(i));
                        }
                    }
                    if(checkeds.size() > 0){
                        operationList(checkeds, getListGroup(),tag);//操作选择回来的数据项
                    }
                }
                break;
            case SELETCREQUESTCODE:

                int position = data.getIntExtra(ItemsSelectFragment.POSITION,-1);
                boolean create = data.getBooleanExtra(ItemsSelectFragment.CREATE,false);
                int flag = data.getIntExtra(ItemsSelectFragment.FLAG,-1);
                if(create){
                    selectCreate(flag);//创建
                }else{
                    selectResult(position,flag);//选择
                }
                break;
        }
    }

    //操作分组
    public void operationListGroup(int flag){

        operationListGroup(getListGroup(),flag);

    }




    //显示多选界面
    private void showMultipleSelect(){

        String[] strings = new String[getList().size()];
        for(int i = 0; i < getList().size(); i++){
            strings[i] = getMultipleItemName(getList().get(i));
        }
        Intent intent = new Intent(fragment.getContext(),MultipleActivity.class);
        intent.putExtra(MultipleActivity.STRINGARRAY,strings);
        fragment.startActivityForResult(intent,REQUESTCODE);
    }

    protected abstract void confirm(int confirmflag);


    int confirmflag;
    public void showConfirmDialog(final int confirmflag, String title){

        this.confirmflag = confirmflag;
        OperateConfirmFragment operateConfirmFragment = new OperateConfirmFragment();
        if(!title.isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString(DeleteConfirmFragment.TITLE,title);
            operateConfirmFragment.setArguments(bundle);
        }

        operateConfirmFragment.setOperateConfirmListener(new OperateConfirmFragment.OperateConfirmListener() {
            @Override
            public void oncomplete() {
                confirm(confirmflag);
            }
        });
        operateConfirmFragment.show(fragment.getFragmentManager(),"operate");
    }





}
