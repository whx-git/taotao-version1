package com.whx.taotao.order.domain;

public class OrderStaus {
    //未支付
    public static final int WZF=0;
    //已支付
    public static final int YZF=1;
    //未发货
    public static final int WFH=2;
    //已发货
    public static final int YFH=3;
    //已送达
    public static final int YSD=4;
    //交易成功
    public static final int JYCG=5;
    //取消订单
    public static final int QXDD=6;

    public static int getWZF() {
        return WZF;
    }

    public static int getYZF() {
        return YZF;
    }

    public static int getWFH() {
        return WFH;
    }

    public static int getYFH() {
        return YFH;
    }

    public static int getYSD() {
        return YSD;
    }

    public static int getJYCG() {
        return JYCG;
    }

    public static int getQXDD() {
        return QXDD;
    }
}
