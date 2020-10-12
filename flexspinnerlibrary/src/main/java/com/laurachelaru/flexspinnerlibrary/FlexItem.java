package com.laurachelaru.flexspinnerlibrary;

public class FlexItem {

    private String text;
    private Integer intId;
    private String stringId;
    private boolean isSelected;

    public FlexItem(String text, Integer intId, boolean isSelected) {
        this.text = text;
        this.intId = intId;
        this.isSelected = isSelected;
    }

    public FlexItem(String text, String stringId, boolean isSelected) {
        this.text = text;
        this.stringId = stringId;
        this.isSelected = isSelected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public String getStringId() {
        return stringId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
