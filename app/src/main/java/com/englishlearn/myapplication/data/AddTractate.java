package com.englishlearn.myapplication.data;

/**
 * 用于从文件输入检测输入合法性并创建文章，
 * Created by yanzl on 16-10-14.
 */
public class AddTractate {

    private boolean checkout;//是否合法
    private Tractate tractate;//文章
    private String checkoutResult;//检测报告
    private String tractateResult;//文章字符串

    public boolean isCheckout() {
        return checkout;
    }

    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }

    public Tractate getTractate() {
        return tractate;
    }

    public void setTractate(Tractate tractate) {
        this.tractate = tractate;
    }

    public String getCheckoutResult() {
        return checkoutResult;
    }

    public void setCheckoutResult(String checkoutResult) {
        this.checkoutResult = checkoutResult;
    }

    public String getTractateResult() {
        return tractateResult;
    }

    public void setTractateResult(String tractateResult) {
        this.tractateResult = tractateResult;
    }
}
