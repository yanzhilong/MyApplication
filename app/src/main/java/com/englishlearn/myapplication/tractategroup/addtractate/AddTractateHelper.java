package com.englishlearn.myapplication.tractategroup.addtractate;

import android.content.Context;

import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzl on 16-11-7.
 */
public class AddTractateHelper {

    public static String SPLITTAG = "\\|";
    public static String MARK = "|";
    private Context context;

    public AddTractateHelper(Context context) {
        this.context = context;
    }

    public Tractate getTractateByRaw(int resId) throws TractateLegalException {

        String tractteString = AndroidUtils.newInstance(context).getStringByResource(resId);
        return getTractateByString(tractteString);
    }

    public Tractate getTractateByFilePath(String filePath) throws TractateLegalException, IOException {

        String tractteString = AndroidUtils.newInstance(context).getStringByFilePath(filePath);
        return getTractateByString(tractteString);
    }

    public Tractate getTractateByString(String tractateStr) throws TractateLegalException {

        List<TractateEnum> enumlist = new ArrayList<>();
        Tractate tractate = new Tractate();

        List<String> english_paragraph = new ArrayList<>();//英文段落数
        List<String> chinese_paragraph = new ArrayList<>();//中文段落数

        StringBuffer english_stringBuffer = new StringBuffer();
        StringBuffer chinese_stringBuffer = new StringBuffer();

        if(tractateStr.equals("")){
            throw new TractateLegalException(TractateEnum.UNKNOW.getMessage());
        }
        //分段
        String[] tractateArray = tractateStr.split(System.getProperty("line.separator"));

        String englishtitle = tractateArray[0];
        String chinesetitle = tractateArray[1];
        String remark = tractateArray[2];

        //判断英文标题，中文标题，备注合法性
        Pattern cnPattern = Pattern.compile("[\\u4e00-\\u9fa5]");

        Matcher titlematcher = cnPattern.matcher(englishtitle);

        if(titlematcher.find()){
            //标题出错
            enumlist.add(TractateEnum.ENGLISHTITLEERROR);
        }

        String[] newTractateArray = Arrays.copyOfRange(tractateArray,3,tractateArray.length);

        //测试段落数是否是一样的
        if(newTractateArray.length % 2 == 0){
            enumlist.add(TractateEnum.PARAGRAPHEERROR);
        }

        //检测段落是否一样
        for (int i = 0; i < newTractateArray.length; i++){
            if(i % 2 == 0 && (i / 2) % 2 == 0){
                //英文行
                if(cnPattern.matcher(newTractateArray[i]).find()){
                    enumlist.add(TractateEnum.ENGLISHCONTENTCN);
                }
                if(newTractateArray[i].equals("")){
                    enumlist.add(TractateEnum.UNKNOW);
                }
                english_paragraph.add(newTractateArray[i]);
                english_stringBuffer.append(newTractateArray[i] + System.getProperty("line.separator"));
                english_stringBuffer.append(System.getProperty("line.separator"));
            }else if(i % 2 == 0 && (i / 2) % 2 == 1){
                //中文行
                chinese_paragraph.add(newTractateArray[i]);
                chinese_stringBuffer.append(newTractateArray[i] + System.getProperty("line.separator"));
                if(i < newTractateArray.length - 1){
                    chinese_stringBuffer.append(System.getProperty("line.separator"));
                }
                if(newTractateArray[i].equals("")){
                    enumlist.add(TractateEnum.UNKNOW);
                }
            }else{
                if(!newTractateArray[i].equals("")){
                    enumlist.add(TractateEnum.UNKNOW);
                }
            }


            /*if(i % 2 == 1){
                isEnglish = !isEnglish;
            }else{
                if(isEnglish){
                    english_stringBuffer.append(System.getProperty("line.separator"));
                }else{
                    chinese_stringBuffer.append(System.getProperty("line.separator"));
                }
                continue;
            }
            String line = tractateArray[i];

            if(isEnglish){
                english_paragraph.add(line);
                english_stringBuffer.append(line + System.getProperty("line.separator"));
            }else{
                chinese_paragraph.add(line);
                chinese_stringBuffer.append(line + System.getProperty("line.separator"));
            }*/
        }

        //检测段落数量
        if(english_paragraph.size() != chinese_paragraph.size()){
            //段落数量不一样
            enumlist.add(TractateEnum.PARAGRAPHEERROR);
        }else{
            //段落一样，栓测每一段|符号是否一样
            for(int i =0; i < english_paragraph.size(); i++){

                String englishsencce = english_paragraph.get(i);
                //检测半角的分割符是否是偶数
                int countenglish = 0;
                int tmpenglishindexof = 0;
                while((tmpenglishindexof =englishsencce.indexOf("|",tmpenglishindexof)) != -1){
                    countenglish ++;
                    tmpenglishindexof++;
                    if(tmpenglishindexof >= englishsencce.length()){
                        break;
                    }
                }

                String chinesesencce = chinese_paragraph.get(i);
                int countchinese = 0;
                int tmpchineseindexof = 0;
                while((tmpchineseindexof =chinesesencce.indexOf("|",tmpchineseindexof)) != -1){
                    countchinese ++;
                    tmpchineseindexof++;
                    if(tmpchineseindexof >= chinesesencce.length()){
                        break;
                    }
                }
                if(countenglish != countchinese){
                    enumlist.add(TractateEnum.SPLITENUMERROR);
                }
            }
        }

        //检测是否包含全角的｜
        int countchinese = 0;
        int tmpchineseindexof = 0;
        while((tmpchineseindexof = tractateStr.indexOf("｜",tmpchineseindexof)) != -1){
            countchinese ++;
            tmpchineseindexof++;
            if(tmpchineseindexof >= tractateStr.length()){
                break;
            }
        }
        if(countchinese > 0){
            enumlist.add(TractateEnum.SPLITEXISTSBC);
        }

        //栓查.后面是否有直接跟字母，这样会影响取词，会将两个词取成一个词,排除跟大写字母　，这个是人名
        if(Pattern.compile("\\w+\\.[^A-Z\\s]").matcher(tractateStr).find()){
            enumlist.add(TractateEnum.PUNCTUATIONNOSPAN);
        }


        if(enumlist.size() != 0){
            StringBuffer exceptionmessage = new StringBuffer();
            for(TractateEnum tractateEnum:enumlist){
                exceptionmessage.append(tractateEnum.getMessage() + ";");
            }
            throw new TractateLegalException(exceptionmessage.toString());
        }

        tractate.setTitle(englishtitle + MARK + chinesetitle);
        tractate.setRemark(remark);
        tractate.setContent(english_stringBuffer.toString());
        tractate.setTranslation(chinese_stringBuffer.toString());
        return tractate;

    }



}
