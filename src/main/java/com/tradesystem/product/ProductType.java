package com.tradesystem.product;


public enum ProductType {

    M_12_FRESH("12M-Świeże"),
    M_12_DRY("12M-Suche"),

    M_6_FRESH("6M-Świeże"),
    M_6_DRY("6M-Suche"),

    M_5_FRESH("5M-Świeże"),
    M_5_DRY("5M-Suche"),

    M_4_FRESH("4M-Świeże"),
    M_4_DRY("4M-Suche"),

    M_3_FRESH("3M-Świeże"),
    M_3_DRY("3M-Suche"),

    M_2_5_FRESH("2.5M-Świeże"),
    M_2_5_DRY("2.5M-Suche");

    private String type;

    ProductType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
