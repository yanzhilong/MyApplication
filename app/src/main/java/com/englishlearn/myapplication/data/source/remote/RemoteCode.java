package com.englishlearn.myapplication.data.source.remote;

/**
 * Created by yanzl on 16-9-18.
 */
public class RemoteCode {

    /**
     * 创建用户
     */
    public static enum CREATEUSER{

        DEFAULT(0,"未知错误"),
        CREATEUSER_NAME_REPEAT(202,"用户名已经存在"),
        CREATEUSER_EMAIL_REPEAT(203,"邮箱已经存在"),
        CREATEUSER_SMSCODE_ERROR(207,"验证码不正确"),
        CREATEUSER_MOBILE_REPEAT(209,"手机号已经被占用");

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

        public String getMessage() {
            return message;
        }
    }

    /**
     * 登陆用户
     */
    public enum LOGINUSER{

        DEFAULT(0,"未知错误"),
        CREATEUSER_NAME_REPEAT(101,"用户名或密码不正确");

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
     * 修改密码
     */
    public enum PASSWORDRESET{

        DEFAULT(0,"未知错误"),
        NOT_FIND_EMAIL(205,"没有找到当前邮箱地址");

        private int code;
        private String message;

        PASSWORDRESET(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static PASSWORDRESET getErrorMessage(int code){
            for (PASSWORDRESET passwordreset : PASSWORDRESET.values()) {
                if (code == passwordreset.code)
                    return passwordreset;
            }
            return DEFAULT;
        }
    }
}
