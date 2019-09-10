package com.whx.taotao.commons.kafkaTopics;

import com.whx.taotao.pojo.TbItem;

public class MessageItem {
    private int action;

    public static final int ACTION_ADD=0;
    public static final int ACTION_DEL=1;
    public static final int ACTION_UPDATE=2;

    private TbItem tbItem;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public static int getActionAdd() {
        return ACTION_ADD;
    }

    public static int getActionDel() {
        return ACTION_DEL;
    }

    public static int getActionUpdate() {
        return ACTION_UPDATE;
    }

    public TbItem getTbItem() {
        return tbItem;
    }

    public void setTbItem(TbItem tbItem) {
        this.tbItem = tbItem;
    }
}
