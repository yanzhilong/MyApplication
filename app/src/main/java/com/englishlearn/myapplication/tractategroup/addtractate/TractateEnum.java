package com.englishlearn.myapplication.tractategroup.addtractate;

/**
 * Created by yanzl on 16-11-7.
 */
public enum TractateEnum {

    UNKNOW("文件不合法"),
    SPLITEXISTSBC("存在全角｜分割符"),//存在全角的｜的分割符
    SPLITENUMERROR("分割符|数量为奇数"),//分割符数量为单数
    ENGLISHTITLEERROR("英文标题包含中文"),//
    PUNCTUATIONNOSPAN("标点符号后面没有空格"),//
    ENGLISHCONTENTCN("英文中包含中文"),//
    PARAGRAPHEERROR("段落数不一样");//

    private String message;

    TractateEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
