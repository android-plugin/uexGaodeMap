package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import java.io.Serializable;

public class CustomBubbleVO implements Serializable{
    private static final long serialVersionUID = 472047525757453544L;
    private int type;
    private BubbleLayoutBaseVO data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BubbleLayoutBaseVO getData() {
        return data;
    }

    public void setData(BubbleLayoutBaseVO data) {
        this.data = data;
    }
}
