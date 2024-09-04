package com.github.sniconmc.oblivion.config;

public class OblivionData {

    private String function;
    private String page;

    public String getFunction() {
        if (function == null) {
            return "";
        }
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPage() {
        if (page == null) {
            return "";
        }
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

