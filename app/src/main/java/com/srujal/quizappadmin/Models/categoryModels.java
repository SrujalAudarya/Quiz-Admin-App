package com.srujal.quizappadmin.Models;

public class categoryModels {

    private String categoryName, categoryImage;
    private int setNum;

    public categoryModels(String categoryName, String categoryImage, int setNum) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.setNum = setNum;
    }

    public categoryModels() {
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

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}