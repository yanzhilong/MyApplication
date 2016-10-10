package com.englishlearn.myapplication.data.source.remote;

/**
 * Created by yanzl on 16-9-18.
 */
public class RemoteCode {

    /**
     * 创建用户
     */
    public enum COMMON{

        DEFAULT(0,"未知错误"),
        Common_NOTFOUND(101,"未查询到相关信息"),
        Common_RESERVED(105,"存在保留字段"),
        Common_UNIQUE(401,"唯一鍵不能重复"),
        MOBILE_ERROR(301,"参数不合法（字段名不能大于20...）");

        private int code;
        private String message;

        COMMON(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static COMMON getErrorMessage(int code){
            for (COMMON common : COMMON.values()) {
                if (code == common.code)
                    return common;
            }
            return DEFAULT;
        }

        public static COMMON getDefauleError(){
            return DEFAULT;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 创建用户
     */
    public static enum CREATEUSER{

        DEFAULT(0,"未知错误"),
        CREATEUSER_NAME_MISSING(201,"用户名不能为空"),
        CREATEUSER_NAME_REPEAT(202,"用户名已经存在"),
        CREATEUSER_EMAIL_REPEAT(203,"邮箱已经存在"),
        CREATEUSER_SMSCODE_ERROR(207,"验证码不正确"),
        CREATEUSER_MOBILE_REPEAT(209,"手机号已经被占用"),
        MOBILE_ERROR(301,"手机号码不合法");

        private int code;
        private String message;

        CREATEUSER(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static CREATEUSER getErrorMessage(int code){
            for (CREATEUSER createuser : CREATEUSER.values()) {
                if (code == createuser.code)
                    return createuser;
            }
            return DEFAULT;
        }

        public static CREATEUSER getDefauleError(){
            return DEFAULT;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 登陆用户
     */
    public enum LOGINUSER{

        DEFAULT(0,"未知错误"),
        LOGIN_NAMEORPWDERROR(101,"用户名或密码不正确"),
        LOGIN_NAMEORPWDEEMPTY(109,"请提交正确的参数");

        private int code;
        private String message;

        LOGINUSER(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static LOGINUSER getErrorMessage(int code){
            for (LOGINUSER loginuser : LOGINUSER.values()) {
                if (code == loginuser.code)
                    return loginuser;
            }
            return DEFAULT;
        }
    }


    /**
     * 修改用户信息
     */
    public enum UPDATEUSER{

        DEFAULT(0,"未知错误"),
        UPDATEUSER_DONTUPDATE(105,"预留字段不允许更改"),
        UPDATEUSER_NAME_REPEAT(202,"用户名已经存在"),
        UPDATEUSER_EMAIL_REPEAT(203,"邮箱已经存在"),
        UPDATEUSER_MOBILE_REPEAT(209,"手机号已经被占用"),
        MOBILE_ERROR(301,"手机号码不合法");
        private int code;
        private String message;

        UPDATEUSER(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static UPDATEUSER getErrorMessage(int code){
            for (UPDATEUSER updateuser : UPDATEUSER.values()) {
                if (code == updateuser.code)
                    return updateuser;
            }
            return DEFAULT;
        }
    }

    /**
     * 修改密码
     */
    public enum PASSWORDRESET{

        DEFAULT(0,"未知错误"),
        EMAIL_EMPTY(204,"邮箱地址不能为空"),
        NOT_FIND_EMAIL(205,"没有找到当前邮箱地址"),
        SESSIONTOKEN_ERROR(206,"使用旧密码修改需要先登陆"),
        SMSCODE_ERROR(207,"验证码不正确"),
        OLDPWD_ERROR(210,"旧密码不正确");


        private int code;
        private String message;

        PASSWORDRESET(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static PASSWORDRESET getDefauleError(){
            return DEFAULT;
        }

        public static PASSWORDRESET getErrorMessage(int code){
            for (PASSWORDRESET passwordreset : PASSWORDRESET.values()) {
                if (code == passwordreset.code)
                    return passwordreset;
            }
            return DEFAULT;
        }
    }

    /**
     * 验证邮箱
     */
    public enum EMAILVERIFY{

        DEFAULT(0,"未知错误"),
        EMAILVERIFY(205,"没有找到当前邮箱地址");


        private int code;
        private String message;

        EMAILVERIFY(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static EMAILVERIFY getDefauleError(){
            return DEFAULT;
        }

        public static EMAILVERIFY getErrorMessage(int code){
            for (EMAILVERIFY emailverify : EMAILVERIFY.values()) {
                if (code == emailverify.code)
                    return emailverify;
            }
            return DEFAULT;
        }
    }

    /**
     * 验证短信验证码
     */
    public enum SMSCODEVERIFY{

        DEFAULT(0,"未知错误"),
        SMSCODE_ERROR(207,"验证码不正确"),
        MOBILE_ERROR(301,"手机号码不合法");


        private int code;
        private String message;

        SMSCODEVERIFY(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static SMSCODEVERIFY getDefauleError(){
            return DEFAULT;
        }

        public static SMSCODEVERIFY getErrorMessage(int code){
            for (SMSCODEVERIFY smscodeverify : SMSCODEVERIFY.values()) {
                if (code == smscodeverify.code)
                    return smscodeverify;
            }
            return DEFAULT;
        }
    }

    /**
     * 请求短信验证码requestSmsCode
     */
    public enum REQUESTSMSCODE{

        DEFAULT(0,"未知错误"),
        MOBILE_ERROR(301,"手机号码不合法"),
        MOBILE_LIMITED(10010,"当前手机号码验证被限制");

        private int code;
        private String message;

        REQUESTSMSCODE(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static REQUESTSMSCODE getDefauleError(){
            return DEFAULT;
        }

        public static REQUESTSMSCODE getErrorMessage(int code){
            for (REQUESTSMSCODE requestsmscode : REQUESTSMSCODE.values()) {
                if (code == requestsmscode.code)
                    return requestsmscode;
            }
            return DEFAULT;
        }
    }
}
