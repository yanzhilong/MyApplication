package com.englishlearn.myapplication.data;

import android.os.Environment;
import android.speech.tts.TextToSpeech;

import com.englishlearn.myapplication.config.ApplicationConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

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
    public void play(boolean isUk) {
        if (dictEntry != null && dictEntry.isValid()
                && dictEntry.getHeadword().length() != 0) {

            String headWord = isUk ? dictEntry.getHeadword() + ApplicationConfig.MDDUK : dictEntry.getHeadword();
            if (!MiscUtils
                    .playAudioForWord(mdxDictBase, dictEntry.getDictId(), headWord)
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

    //保存当前声音文件
    public boolean saveWaveData(String folder){

        Map<String,byte[]> byteMap = MiscUtils.getWaveData(mdxDictBase, dictEntry.getDictId(), dictEntry.getHeadword());
        if(byteMap == null){
            return false;
        }
        String extendname = null;
        byte[] data = null;
        Iterator iterator = byteMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, byte[]> entry = (Map.Entry<String, byte[]>) iterator.next();
            extendname = entry.getKey();
            data = entry.getValue();
        }
        if(extendname == null || data == null){
            return false;
        }
        String mdictExternaltmp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taoge/" + folder;
        File filefolder = new File(mdictExternaltmp);
        if(!filefolder.exists()){
            filefolder.mkdir();
        }
        File file = new File(mdictExternaltmp + File.separator + word.getName() + "." + extendname);
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(data);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(bufferedOutputStream != null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    //保存当前声音文件
    public boolean saveUKWaveData(String folder){

        Map<String,byte[]> byteMap = MiscUtils.getWaveData(mdxDictBase, dictEntry.getDictId(), dictEntry.getHeadword() + "_UK");
        if(byteMap == null){
            return false;
        }
        String extendname = null;
        byte[] data = null;
        Iterator iterator = byteMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, byte[]> entry = (Map.Entry<String, byte[]>) iterator.next();
            extendname = entry.getKey();
            data = entry.getValue();
        }
        if(extendname == null || data == null){
            return false;
        }
        String mdictExternaltmp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taoge/" + folder;
        File filefolder = new File(mdictExternaltmp);
        if(!filefolder.exists()){
            filefolder.mkdir();
        }
        File file = new File(mdictExternaltmp + File.separator + word.getName() + "_UK" + "." + extendname);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
