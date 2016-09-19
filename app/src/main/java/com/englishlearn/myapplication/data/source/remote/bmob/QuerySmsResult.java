package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-19.
 * sms_state是发送状态，有值: SENDING-发送中，FAIL-发送失败 SUCCESS-发送成功
 * verify_state是验证码是否验证状态， 有值: true-已验证 false-未验证
 */
public class QuerySmsResult {

    private String state;
    private String verify_state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVerify_state() {
        return verify_state;
    }

    public void setVerify_state(String verify_state) {
        this.verify_state = verify_state;
    }

    public SENDSTATUS getStatus(){
        if(this.state.equals("SENDING")){
            return SENDSTATUS.SENDING;
        }else if(this.state.equals("FAIL")){
            return SENDSTATUS.FAIL;
        }else if(this.state.equals("SUCCESS")){
            return SENDSTATUS.SUCCESS;
        }
        return SENDSTATUS.QUERYFAIL;
    }

    public boolean isVerify(){
        return this.verify_state.equals("true");
    }

    public enum SENDSTATUS{

        QUERYFAIL,
        SENDING,
        FAIL,
        SUCCESS
    }

}
