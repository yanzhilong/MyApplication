package com.englishlearn.myapplication.sentences;


import android.util.Log;

import com.englishlearn.myapplication.data.Sentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesSelectPresenter extends SentencesSelectContract.Presenter{

    private static final String TAG = SentencesSelectPresenter.class.getSimpleName();
    private boolean isEdit;
    private Map<String,Boolean> selects;
    private SentencesSelectContract.View view;
    private List<Sentence> selecteds;
    private boolean isAllSelect;

    public SentencesSelectPresenter(SentencesSelectContract.View view){
        this.view = view;
        this.view.setPresenter(this);
        selects = new HashMap<>();
        selecteds = new ArrayList<>();
    }

    @Override
    boolean isEdit() {
        return isEdit;
    }

    @Override
    void edit() {
        isEdit = true;
        view.notifyDataSetChanged();
        view.showSentencesEdit();
    }

    @Override
    void unedit() {
        selects.clear();
        selecteds.clear();
        view.hideSentencesEdit();
        view.hideAllSelect();
        view.showDeleteEnabled(false);
        view.showEditCount(0);
        view.notifyDataSetChanged();
        isEdit = false;
        isAllSelect = false;
    }

    @Override
    void onClick(Sentence sentence) {
        boolean select = selects.get(sentence.getSentenceId()) != null ? selects.get(sentence.getSentenceId()) : false;
        if(select){
            unselect(sentence);
            selects.put(sentence.getSentenceId(),false);
        }else{
            select(sentence);
            selects.put(sentence.getSentenceId(),true);
        }
        view.notifyDataSetChanged();
    }

    @Override
    void allSelectClick() {
        selecteds.clear();
        if(isAllSelect){
            isAllSelect = false;
        }else{
            isAllSelect = true;
            selecteds.addAll(view.getSentences());
        }
        Iterator<Sentence> iterator = view.getSentences().iterator();
        while (iterator.hasNext()){
            Sentence s = iterator.next();
            selects.put(s.getSentenceId(),isAllSelect);
        }
        showEditCount();
        view.notifyDataSetChanged();
    }

    @Override
    boolean isSelect(Sentence sentence) {
        return selects.get(sentence.getSentenceId()) != null ? selects.get(sentence.getSentenceId()) : false;
    }

    @Override
    void dataSetChanged() {
        Iterator<Sentence> iterator = selecteds.iterator();
        while (iterator.hasNext()){
            Sentence s = iterator.next();
            //删除少掉的
            Iterator<Sentence> ite = view.getSentences().iterator();
            boolean found = false;
            while (ite.hasNext()){
                Sentence sentence = ite.next();
                if(s.getSentenceId().equals(sentence.getSentenceId())){
                    found = true;
                    break;
                }
            }
            if(!found){
                iterator.remove();
                selects.remove(s.getSentenceId());
            }
        }
        //判断是全选就要改变
        showAllSelectStatus();
    }

    @Override
    List<Sentence> getSelects() {
        return selecteds;
    }

    private void select(Sentence sentence) {
        selecteds.add(sentence);
        Log.d(TAG,"增加一个，总:"+selects.size());
        showEditCount();
        showAllSelectStatus();
    }

    private void unselect(Sentence sentence) {
        Iterator<Sentence> iterator = selecteds.iterator();
        while (iterator.hasNext()){
            Sentence s = iterator.next();
            if(s.getSentenceId().equals(sentence.getSentenceId())){
                iterator.remove();
                break;
            }
        }
        Log.d(TAG,"减少一个，总:"+selects.size());
        showEditCount();
        showAllSelectStatus();
    }

    private void showEditCount(){
        if(selecteds.size() > 0){
            view.showDeleteEnabled(true);
            view.showEditCount(selecteds.size());
        }else{
            view.showDeleteEnabled(false);
            view.showEditCount(selecteds.size());
        }
    }

    private void showAllSelectStatus(){
        if(selecteds.size() == view.getSentences().size()){
            view.showAllSelect();
            isAllSelect = true;
        }else if(isAllSelect){
            view.hideAllSelect();
            isAllSelect = false;
        }
    }
}
