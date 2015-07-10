package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import java.io.Serializable;

public class UpdateResultVO implements Serializable{
    private static final long serialVersionUID = -6836956837125725510L;
    private String name;
    private int result;

    public UpdateResultVO(String name, int result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
