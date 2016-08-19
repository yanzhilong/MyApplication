package com.englishlearn.myapplication.search;


import android.widget.Filter;

import com.englishlearn.myapplication.data.Sentence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yanzl on 16-7-20.
 */
public class SearchPresenter extends SearchContract.Presenter{

    private SearchContract.View mView;
    private SentencesFilter sentencesFilter;

    @Inject
    public SearchPresenter(SearchContract.View vew){
        mView = vew;
        mView.setPresenter(this);
        sentencesFilter = new SentencesFilter();
    }

    @Override
    void filterSentences(CharSequence constraint) {
        sentencesFilter.filter(constraint);
    }

    @Override
    void setQuery(String query) {
        mView.setQuery(query);
    }


    private class SentencesFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if (constraint != null || constraint.length() > 0){
                List<Sentence> list = new ArrayList<>();
                /*for(Sentence sentence:mSentences){
                    if(sentence.getContent().contains(constraint) || sentence.getTranslation().contains(constraint)){
                        list.add(sentence);
                    }
                }*/
                filterResults.count = list.size();
                filterResults.values = list;
            }else {
                /*filterResults.count = mSentences.size();
                filterResults.values = mSentences;*/
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           /* mFilterList = (List<Sentence>) results.values;
            mainView.showSentences(mFilterList);*/
        }
    }
}
