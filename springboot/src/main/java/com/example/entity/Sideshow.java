package com.example.entity;

public class Sideshow {
    private Integer id;
    private String img;
    private Integer propagateId;

    private String propagateTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getPropagateId() {
        return propagateId;
    }

    public void setPropagateId(Integer propagateId) {
        this.propagateId = propagateId;
    }

    public String getPropagateTitle() {
        return propagateTitle;
    }

    public void setPropagateTitle(String propagateTitle) {
        this.propagateTitle = propagateTitle;
    }
}
