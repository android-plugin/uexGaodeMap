package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import org.zywx.wbpalmstar.base.BUtility;

import java.io.Serializable;

public class BubbleLayoutBaseVO implements Serializable{
    private static final long serialVersionUID = -2631859283618194851L;
    private float width;//(必选)气泡宽度
    private String bgImg;//(必选)背景图
    private String bgColor;//(可选)背景色
    private float shadowOffsetX;//(可选)x轴偏移量,向右为正，单位;px
    private float shadowOffsetY;//(可选)y轴偏移量,向下为正，单位;px
    private String shadowColor;//(可选)阴影颜色
    private String titleContent;//(必选)标题内容
    private int titleFontSize;//(必选)标题字号
    private String titleColor;//(必选)标题颜色
    private String textContent;//(必选)文本内容
    private int textFontSize;//(必选)文本字号
    private String textColor;//(必选)文本颜色


    public int getWidth() {
        return (int) width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getBgColor() {
        return BUtility.parseColor(bgColor);
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getShadowOffsetX() {
        return (int) shadowOffsetX;
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
    }

    public int getShadowOffsetY() {
        return (int) shadowOffsetY;
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
    }

    public int getShadowColor() {
        return BUtility.parseColor(shadowColor);
    }

    public void setShadowColor(String shadowColor) {
        this.shadowColor = shadowColor;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getTitleColor() {
        return BUtility.parseColor(titleColor);
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getTextFontSize() {
        return textFontSize;
    }

    public void setTextFontSize(int textFontSize) {
        this.textFontSize = textFontSize;
    }

    public int getTextColor() {
        return BUtility.parseColor(textColor);
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }
}
