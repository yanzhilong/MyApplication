package com.englishlearn.myapplication.data.source.remote;

/**
 * Created by yanzl on 16-9-18.
 */
public class RemoteCode {

    public enum CREATEUSER{

        DEFAULT(0,"未知错误"),
        CREATEUSER_NAME_REPEAT(202,"用户名已经存在"),
        CREATEUSER_EMAIL_REPEAT(203,"邮箱已经存在");

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
    }
}
