package com.transing.crawl.integration.bo;

public class Style {
    private long id;
    private String styleCode;
    private String styleName;
    private String stylePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStylePath() {
        return stylePath;
    }

    public void setStylePath(String stylePath) {
        this.stylePath = stylePath;
    }
}