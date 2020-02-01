package com.tradesystem;


public enum Assortments {

    M_12_FRESH("12M"),
    M_12_DRY("12M"),

    M_6_FRESH("6M"),
    M_6_DRY("6M"),

    M_5_FRESH("5M"),
    M_5_DRY("5M"),

    M_4_FRESH("4M"),
    M_4_DRY("4M"),

    M_3_FRESH("3M"),
    M_3_DRY("3M"),

    M_2_5_FRESH("2.5M"),
    M_2_5_DRY("2.5M");

    private String type;

    Assortments(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
