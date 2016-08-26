package com.englishlearn.myapplication.dagger;

import com.englishlearn.myapplication.addeditgrammar.AddEditGrammarPresenter;
import com.englishlearn.myapplication.addeditsentence.AddEditSentencePresenter;
import com.englishlearn.myapplication.domain.DeleteGrammars;
import com.englishlearn.myapplication.domain.DeleteSentences;
import com.englishlearn.myapplication.grammardetail.GrammarDetailPresenter;
import com.englishlearn.myapplication.grammars.GrammarsPresenter;
import com.englishlearn.myapplication.searchsentences.SearchSentencesPresenter;
import com.englishlearn.myapplication.sentencedetail.SentenceDetailPresenter;
import com.englishlearn.myapplication.sentences.SentencesPresenter;

import dagger.Component;

/**
 * Created by yanzl on 16-6-24.
 */
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(DeleteSentences deleteSentences);

    void inject(DeleteGrammars deleteGrammars);

    void inject(SentenceDetailPresenter sentenceDetailPresenter);

    void inject(GrammarDetailPresenter grammarDetailPresenter);

    void inject(AddEditSentencePresenter addEditSentencePresenter);

    void inject(AddEditGrammarPresenter addEditGrammarPresenter);

    void inject(SentencesPresenter sentencesPresenter);

    void inject(GrammarsPresenter grammarsPresenter);

    void inject(SearchSentencesPresenter searchSentencesPresenter);

}
