package com.svea.app.beans;

public class WatchListBean {

    private String heading;
    private String subHeading;
    private String price;
    private int icon;

    public WatchListBean(String heading, String subHeading, String price, int icon) {
        this.heading = heading;
        this.subHeading = subHeading;
        this.price = price;
        this.icon = icon;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

