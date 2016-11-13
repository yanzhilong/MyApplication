package com.englishlearn.myapplication.dagger;

import com.englishlearn.myapplication.addeditgrammar.AddEditGrammarPresenter;
import com.englishlearn.myapplication.addeditsentence.AddEditSentencePresenter;
import com.englishlearn.myapplication.dialog.WordDetailDialog;
import com.englishlearn.myapplication.domain.DeleteGrammars;
import com.englishlearn.myapplication.domain.DeleteSentences;
import com.englishlearn.myapplication.grammar.GrammarPresenter;
import com.englishlearn.myapplication.grammardetail.GrammarDetailPresenter;
import com.englishlearn.myapplication.grammars.GrammarsActivity;
import com.englishlearn.myapplication.grammars.GrammarsPresenter;
import com.englishlearn.myapplication.main.MainActivity;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail.PhoneticsDetailPresenter;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail.PhoneticsSymbolsDetailActivity;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsSymbolsActivity;
import com.englishlearn.myapplication.phoneticssymbols.PhoneticsSymbolsPresenter;
import com.englishlearn.myapplication.registeruser.RegisterUserPresenter;
import com.englishlearn.myapplication.searchsentences.SearchSentencesPresenter;
import com.englishlearn.myapplication.sentence.SentencesPresenter;
import com.englishlearn.myapplication.sentencedetail.SentenceDetailPresenter;
import com.englishlearn.myapplication.sentencegroups.MyCollectSentenceGroupsFragment;
import com.englishlearn.myapplication.sentencegroups.MyCreateSentenceGroupsFragment;
import com.englishlearn.myapplication.sentencegroups.SentenceGroupsTopFragmentFragment;
import com.englishlearn.myapplication.sentencegroups.sentences.SentencesFragment;
import com.englishlearn.myapplication.sentencegroups.sentences.sentencecollect.CreateSentenceActivity;
import com.englishlearn.myapplication.tractategroup.MyCollectTractateGroupsFragment;
import com.englishlearn.myapplication.tractategroup.MyCreateTractateGroupFragment;
import com.englishlearn.myapplication.tractategroup.TractateTypesFragment;
import com.englishlearn.myapplication.tractategroup.addtractate.AddTractateActivity;
import com.englishlearn.myapplication.tractategroup.tractates.TractatesFragment;
import com.englishlearn.myapplication.tractategroup.tractatestop.TractateTopFragment;
import com.englishlearn.myapplication.tractategroup.tractatestop.TractatrGroupTopFragment;
import com.englishlearn.myapplication.wordgroup.WordGroupsFragment;
import com.englishlearn.myapplication.wordgroup.WordsActivity;
import com.englishlearn.myapplication.wordgroups.MyCollectWordGroupsFragment;
import com.englishlearn.myapplication.wordgroups.MyCreateWordGroupsFragment;
import com.englishlearn.myapplication.wordgroups.WordGroupsTopFragment;
import com.englishlearn.myapplication.wordgroups.words.WordsFragment;
import com.englishlearn.myapplication.wordgroups.words.wordcollect.WordCollectActivity;

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

    void inject(RegisterUserPresenter registerUserPresenter);

    void inject(PhoneticsSymbolsPresenter phoneticsSymbolsPresenter);

    void inject(PhoneticsDetailPresenter phoneticsDetailPresenter);

    void inject(GrammarPresenter grammarPresenter);

    void inject(WordGroupsFragment wordGroupsFragment);

    void inject(WordsActivity wordsActivity);

    void inject(PhoneticsSymbolsActivity phoneticsSymbolsActivity);

    void inject(PhoneticsSymbolsDetailActivity phoneticsSymbolsDetailActivity);

    void inject(GrammarsActivity grammarsActivity);

    void inject(WordGroupsTopFragment wordGroupsTopFragment);

    void inject(MyCreateWordGroupsFragment myWordGroupsFragment);

    void inject(MyCollectWordGroupsFragment myCollectWordGroupsFragment);

    void inject(WordsFragment wordsFragment);

    void inject(MainActivity mainActivity);

    void inject(MyCollectSentenceGroupsFragment myCollectSentenceGroupsFragment);

    void inject(MyCreateSentenceGroupsFragment myCreateSentenceGroupsFragment);

    void inject(SentenceGroupsTopFragmentFragment sentenceGroupsTopFragmentFragment);

    void inject(SentencesFragment sentencesFragment);

    void inject(TractateTypesFragment tractateTypesFragment);

    void inject(TractateTopFragment tractatesFragment);

    void inject(MyCollectTractateGroupsFragment myCollectTractateGroupsFragment);

    void inject(WordDetailDialog wordDetailDialog);

    void inject(TractatrGroupTopFragment tractatrGroupTopFragment);

    void inject(MyCreateTractateGroupFragment myCreateTractateGroupFragment);

    void inject(TractatesFragment tractatesFragment);

    void inject(AddTractateActivity addTractateActivity);

    void inject(WordCollectActivity wordCollectActivity);

    void inject(CreateSentenceActivity sentenceCollectActivity);



}
