package com.yricky.powertrans.pojo.youdao;

import java.util.List;

public class Data    {
    private List<Entries> entries;
    private String query;
    private String language;
    private String type;
    public void setEntries(List<Entries> entries) {
        this.entries = entries;
    }
    public List<Entries> getEntries() {
        return entries;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    public String getQuery() {
        return query;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLanguage() {
        return language;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
