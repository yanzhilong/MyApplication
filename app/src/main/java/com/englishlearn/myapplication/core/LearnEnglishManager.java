package com.englishlearn.myapplication.core;

import android.content.Context;

/**
 * Created by yanzl on 16-10-11.
 */
public class LearnEnglishManager {

    private static LearnEnglishManager learnEnglishManager;
    private Context context;

    public LearnEnglishManager(Context context) {
        this.context = context;
    }

    public static synchronized LearnEnglishManager newInstance(Context context) {
        if(learnEnglishManager == null){
            learnEnglishManager = new LearnEnglishManager(context);
        }
        return learnEnglishManager;
    }

}
