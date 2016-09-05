package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 * 单詞
 */
public class BmobWord {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String wordId;
    private String name;
    private String british_phonogram; //英式发音音标(多个用&分割)
    private String british_soundurl; //英式发音(下面同上)
    private String american_phonogram; //英式发音音标
    private String american_soundurl; //美式发音
    private String adjective; //表示形容词
    private String adjective_short = "adj.[a.](形容词)"; //(adj.[a.](形容词))
    private String adverb; //表示副词
    private String adverb_short = "adv.[ad.](副词)"; //(adv.[ad.](副词))
    private String preparation; //表示介词
    private String preparation_short = "prep.(介词)"; //(prep.(介词))
    private String noun; //表示名词
    private String noun_short = "n.(名词)"; //(n.(名词))
    private String transitive_verb; //表示
    private String transitive_verb_short = "vt.(及物动词)"; //(vt.(可及物动词))
    private String intransitive_verb; //表示不及物动词
    private String intransitive_verb_short = "vi.(不及物动词)"; //(vi.(不及物动词))
    private String verb; //表示动词
    private String verb_short = "v.(动词)"; //(v.(动词))
    private String auxiliary; //表示助动词
    private String auxiliary_short = "aux.(助动词)"; //(aux.(助动词))
    private String pronoun; //表示代词
    private String pronoun_short = "pron.(代词)"; //(pron.(代词))
    private String article; //冠詞
    private String article_short = "art.(冠詞)"; //(art.(冠詞))
    private String conjunction; //连接詞
    private String conjunction_short = "conj.(连接詞)"; //(conj.(连接詞))
    private String interjection; //感叹词
    private String interjection_short = "int.(/感叹词)"; //(int.(/感叹词))
    private String other; //其它词(不在上面出现的)
    private String correlation; //其它相关的（第三人称单数，复数....）
    private String remark; //备注


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBritish_phonogram() {
        return british_phonogram;
    }

    public void setBritish_phonogram(String british_phonogram) {
        this.british_phonogram = british_phonogram;
    }

    public String getBritish_soundurl() {
        return british_soundurl;
    }

    public void setBritish_soundurl(String british_soundurl) {
        this.british_soundurl = british_soundurl;
    }

    public String getAmerican_phonogram() {
        return american_phonogram;
    }

    public void setAmerican_phonogram(String american_phonogram) {
        this.american_phonogram = american_phonogram;
    }

    public String getAmerican_soundurl() {
        return american_soundurl;
    }

    public void setAmerican_soundurl(String american_soundurl) {
        this.american_soundurl = american_soundurl;
    }

    public String getAdjective() {
        return adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    public String getAdjective_short() {
        return adjective_short;
    }

    public void setAdjective_short(String adjective_short) {
        this.adjective_short = adjective_short;
    }

    public String getAdverb() {
        return adverb;
    }

    public void setAdverb(String adverb) {
        this.adverb = adverb;
    }

    public String getAdverb_short() {
        return adverb_short;
    }

    public void setAdverb_short(String adverb_short) {
        this.adverb_short = adverb_short;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getPreparation_short() {
        return preparation_short;
    }

    public void setPreparation_short(String preparation_short) {
        this.preparation_short = preparation_short;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getNoun_short() {
        return noun_short;
    }

    public void setNoun_short(String noun_short) {
        this.noun_short = noun_short;
    }

    public String getTransitive_verb() {
        return transitive_verb;
    }

    public void setTransitive_verb(String transitive_verb) {
        this.transitive_verb = transitive_verb;
    }

    public String getTransitive_verb_short() {
        return transitive_verb_short;
    }

    public void setTransitive_verb_short(String transitive_verb_short) {
        this.transitive_verb_short = transitive_verb_short;
    }

    public String getIntransitive_verb() {
        return intransitive_verb;
    }

    public void setIntransitive_verb(String intransitive_verb) {
        this.intransitive_verb = intransitive_verb;
    }

    public String getIntransitive_verb_short() {
        return intransitive_verb_short;
    }

    public void setIntransitive_verb_short(String intransitive_verb_short) {
        this.intransitive_verb_short = intransitive_verb_short;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getVerb_short() {
        return verb_short;
    }

    public void setVerb_short(String verb_short) {
        this.verb_short = verb_short;
    }

    public String getAuxiliary() {
        return auxiliary;
    }

    public void setAuxiliary(String auxiliary) {
        this.auxiliary = auxiliary;
    }

    public String getAuxiliary_short() {
        return auxiliary_short;
    }

    public void setAuxiliary_short(String auxiliary_short) {
        this.auxiliary_short = auxiliary_short;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getPronoun_short() {
        return pronoun_short;
    }

    public void setPronoun_short(String pronoun_short) {
        this.pronoun_short = pronoun_short;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getArticle_short() {
        return article_short;
    }

    public void setArticle_short(String article_short) {
        this.article_short = article_short;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    public String getConjunction_short() {
        return conjunction_short;
    }

    public void setConjunction_short(String conjunction_short) {
        this.conjunction_short = conjunction_short;
    }

    public String getInterjection() {
        return interjection;
    }

    public void setInterjection(String interjection) {
        this.interjection = interjection;
    }

    public String getInterjection_short() {
        return interjection_short;
    }

    public void setInterjection_short(String interjection_short) {
        this.interjection_short = interjection_short;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
