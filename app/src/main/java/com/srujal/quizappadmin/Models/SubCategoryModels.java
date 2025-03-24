package com.srujal.quizappadmin.Models;

public class SubCategoryModels {
    private String categoryName, key;

    public SubCategoryModels() {
    }

    public SubCategoryModels(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
