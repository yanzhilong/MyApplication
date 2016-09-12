package com.englishlearn.myapplication.grammars;


import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarsSelectPresenter extends GrammarsSelectContract.Presenter{

    private static final String TAG = GrammarsSelectPresenter.class.getSimpleName();
    private boolean isEdit;
    private Map<String,Boolean> selects;
    private GrammarsSelectContract.View view;
    private List<Grammar> selecteds;
    private boolean isAllSelect;

    public GrammarsSelectPresenter(GrammarsSelectContract.View view){
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
        view.showGrammarsEdit();
    }

    @Override
    void unedit() {

        selects.clear();
        selecteds.clear();
        view.hideGrammarsEdit();
        view.hideAllSelect();
        view.showDeleteEnabled(false);
        view.showEditCount(0);
        view.notifyDataSetChanged();
        isEdit = false;
        isAllSelect = false;
    }

    @Override
    void onClick(Grammar grammar) {
        boolean select = selects.get(grammar.getGrammarId()) != null ? selects.get(grammar.getGrammarId()) : false;
        if(select){
            unselect(grammar);
            selects.put(grammar.getGrammarId(),false);
        }else{
            select(grammar);
            selects.put(grammar.getGrammarId(),true);
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
            selecteds.addAll(view.getGrammars());
        }
        Iterator<Grammar> iterator = view.getGrammars().iterator();
        while (iterator.hasNext()){
            Grammar g = iterator.next();
            selects.put(g.getGrammarId(),isAllSelect);
        }
        showEditCount();
        view.notifyDataSetChanged();
    }

    @Override
    boolean isSelect(Grammar grammar) {
        return selects.get(grammar.getGrammarId()) != null ? selects.get(grammar.getGrammarId()) : false;
    }

    @Override
    void dataSetChanged() {
        Iterator<Grammar> iterator = selecteds.iterator();
        while (iterator.hasNext()){
            Grammar s = iterator.next();
            //删除少掉的
            Iterator<Grammar> ite = view.getGrammars().iterator();
            boolean found = false;
            while (ite.hasNext()){
                Grammar grammar = ite.next();
                if(s.getGrammarId().equals(grammar.getGrammarId())){
                    found = true;
                    break;
                }
            }
            if(!found){
                iterator.remove();
                selects.remove(s.getGrammarId());
            }
        }
    }

    @Override
    List<Grammar> getSelects() {
        return selecteds;
    }

    private void select(Grammar grammar) {
        selecteds.add(grammar);
        Log.d(TAG,"增加一个，总:"+selects.size());
        showEditCount();
        showAllSelectStatus();
    }

    private void unselect(Grammar grammar) {
        Iterator<Grammar> iterator = selecteds.iterator();
        while (iterator.hasNext()){
            Grammar g = iterator.next();
            if(g.getGrammarId().equals(grammar.getGrammarId())){
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
        if(selecteds.size() == view.getGrammars().size()){
            view.showAllSelect();
            isAllSelect = true;
        }else if(isAllSelect){
            view.hideAllSelect();
            isAllSelect = false;
        }
    }
}
