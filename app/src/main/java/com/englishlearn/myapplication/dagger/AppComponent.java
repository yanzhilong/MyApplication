package com.englishlearn.myapplication.dagger;

import com.englishlearn.myapplication.domain.AddGrammars;
import com.englishlearn.myapplication.domain.AddSentences;
import com.englishlearn.myapplication.domain.GetGrammar;
import com.englishlearn.myapplication.domain.GetGrammars;
import com.englishlearn.myapplication.domain.GetSentence;
import com.englishlearn.myapplication.domain.GetSentences;

import dagger.Component;

/**
 * Created by yanzl on 16-6-24.
 */
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(GetSentences getSentences);

    void inject(AddSentences addSentences);

    void inject(GetGrammars getGrammars);

    void inject(AddGrammars addGrammars);

    void inject(GetSentence getSentenceById);

    void inject(GetGrammar getGrammarById);


}
