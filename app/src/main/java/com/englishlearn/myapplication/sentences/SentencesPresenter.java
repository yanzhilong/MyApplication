package com.englishlearn.myapplication.sentences;


import android.widget.Filter;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.DeleteSentences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesPresenter extends SentencesContract.Presenter{

    private SentencesContract.View mainView;
    private int page = 0;
    private final int PAGESIZE = 10;
    private List<Sentence> mSentences;
    private List<Sentence> mFilterList;
    private SentencesFilter sentencesFilter;
    @Inject
    Repository repository;

    DeleteSentences deleteSentences;
    public SentencesPresenter(SentencesContract.View vew){
        mainView = vew;
        MyApplication.instance.getAppComponent().inject(this);
        deleteSentences = new DeleteSentences();
        mSentences = new ArrayList<>();
        sentencesFilter = new SentencesFilter();
        mainView.setPresenter(this);
    }

    @Override
    void getSentences() {
        getSentences(null);
    }

    @Override
    void filterSentences(CharSequence constraint) {
        sentencesFilter.filter(constraint);
    }

    @Override
    void getSentencesNextPage() {
        repository.getSentencesRx(++page,PAGESIZE)
        .subscribe(new Subscriber<List<Sentence>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Sentence> sentences) {
                mainView.addSentences(sentences);
            }
        });
    }

    @Override
    void getSentences(String searchword) {
        if(searchword == null){
            Subscription subscription = repository.getSentencesRx()
                    .subscribe(new Subscriber<List<Sentence>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mainView.emptySentences();
                        }

                        @Override
                        public void onNext(List<Sentence> sentences) {
                            if(sentences != null && sentences.size() > 0){
                                mSentences.clear();
                                mSentences.addAll(sentences);
                                mainView.showSentences(mSentences);
                            }else{
                                mainView.emptySentences();
                            }
                        }
                    });
            add(subscription);
        }
    }


    @Override
    void addSentence() {
        mainView.showaddSentence();
    }

    @Override
    void deleteSentences(final List<Sentence> sentences) {
        Subscription subscription = deleteSentences.excuteIo(new DeleteSentences.DeleteSentencesParame(sentences))
              .subscribe(new Subscriber<DeleteSentences.DeleteSentensResult>() {
                  @Override
                  public void onCompleted() {

                  }

                  @Override
                  public void onError(Throwable e) {
                      mainView.showDeleteResult(0,0);
                  }

                  @Override
                  public void onNext(DeleteSentences.DeleteSentensResult deleteSentensResult) {
                      mainView.showDeleteResult(deleteSentensResult.getSuccessCount(),deleteSentensResult.getFailCount());
                  }
              });
        add(subscription);
    }

    private class SentencesFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if (constraint != null || constraint.length() > 0){
                List<Sentence> list = new ArrayList<>();
                for(Sentence sentence:mSentences){
                    if(sentence.getContent().contains(constraint) || sentence.getTranslation().contains(constraint)){
                        list.add(sentence);
                    }
                }
                filterResults.count = list.size();
                filterResults.values = list;
            }else {
                filterResults.count = mSentences.size();
                filterResults.values = mSentences;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilterList = (List<Sentence>) results.values;
            mainView.showSentences(mFilterList);
        }
    }

}
