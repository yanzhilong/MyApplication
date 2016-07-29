package com.englishlearn.myapplication.dagger;

import com.englishlearn.myapplication.domain.AddGrammar;
import com.englishlearn.myapplication.domain.AddSentence;
import com.englishlearn.myapplication.domain.GetGrammar;
import com.englishlearn.myapplication.domain.GetGrammars;
import com.englishlearn.myapplication.domain.GetSentence;
import com.englishlearn.myapplication.domain.GetSentences;
import com.englishlearn.myapplication.domain.UpdateGrammar;
import com.englishlearn.myapplication.domain.UpdateSentence;

import dagger.Component;

/**
 * Created by yanzl on 16-6-24.
 */
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(GetSentences getSentences);

    void inject(AddSentence addSentences);

    void inject(GetGrammars getGrammars);

    void inject(AddGrammar addGrammars);

    void inject(GetSentence getSentenceById);

    void inject(GetGrammar getGrammarById);

    void inject(UpdateSentence updateSentence);

    void inject(UpdateGrammar updateGrammar);


}
