package com.srujal.quizappadmin.Models;

public class categoryModels {
    private String categoryName, categoryImage, key;
    private int setNum;

    public categoryModels() {
    }

    public categoryModels(String categoryName, String categoryImage, String key, int setNum) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.key = key;
        this.setNum = setNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}
