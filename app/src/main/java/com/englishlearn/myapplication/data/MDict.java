package com.englishlearn.myapplication.data;

import android.speech.tts.TextToSpeech;

import java.io.Serializable;

import cn.mdict.mdx.DictEntry;
import cn.mdict.mdx.MdxDictBase;
import cn.mdict.utils.MiscUtils;

/**
 * Created by yanzl on 16-11-29.
 * 词典播放实体类
 */

public class MDict implements Serializable,Cloneable {

    private DictEntry dictEntry;
    private MdxDictBase mdxDictBase;
    private Word word;//当前单词
    private TextToSpeech ttsEngine = null;//tts播放引擎
    private boolean useTTS;//如果没有音频则使用tts播放

    public DictEntry getDictEntry() {
        return dictEntry;
    }

    public void setDictEntry(DictEntry dictEntry) {
        this.dictEntry = dictEntry;
    }

    public MdxDictBase getMdxDictBase() {
        return mdxDictBase;
    }

    public void setMdxDictBase(MdxDictBase mdxDictBase) {
        this.mdxDictBase = mdxDictBase;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public TextToSpeech getTtsEngine() {
        return ttsEngine;
    }

    public void setTtsEngine(TextToSpeech ttsEngine) {
        this.ttsEngine = ttsEngine;
    }

    public boolean isUseTTS() {
        return useTTS;
    }

    public void setUseTTS(boolean useTTS) {
        this.useTTS = useTTS;
    }

    //播放声音
    public void play() {
        if (dictEntry != null && dictEntry.isValid()
                && dictEntry.getHeadword().length() != 0) {
            if (!MiscUtils
                    .playAudioForWord(mdxDictBase, dictEntry.getDictId(), dictEntry.getHeadword())
                    && ttsEngine != null && useTTS) {
                String headword = dictEntry.getHeadword().trim();
                StringBuilder hw = new StringBuilder(dictEntry.getHeadword()
                        .length());
                char c;
                for (int i = 0; i < headword.length(); ++i) {
                    c = headword.charAt(i);
                    if (c == ' ' || c >= '1')
                        hw.append(c);
                }
                if (hw.length() > 0)
                    ttsEngine.speak(hw.toString(), TextToSpeech.QUEUE_FLUSH,
                            null);
                else
                    ttsEngine.speak(headword, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}
