package com.hsj.bean;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class Tag {
    private int id;
    private String name;
    private int dimension;
    private String dimensionName;
    private int tagCode;
    private int deleted;
    private int status;
    private int orderWeight;
    private long createTime;
    private long updateTime;
    private String imageUrl;
    private boolean isCheck = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public int getTagCode() {
        return tagCode;
    }

    public void setTagCode(int tagCode) {
        this.tagCode = tagCode;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(int orderWeight) {
        this.orderWeight = orderWeight;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dimension=" + dimension +
                ", dimensionName='" + dimensionName + '\'' +
                ", tagCode=" + tagCode +
                ", deleted=" + deleted +
                ", status=" + status +
                ", orderWeight=" + orderWeight +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", imageUrl='" + imageUrl + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
