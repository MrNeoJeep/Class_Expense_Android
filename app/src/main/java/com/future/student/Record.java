package com.future.student;

import java.time.LocalDateTime;

public class Record {
    private String id;

    /**
     * 记录名
     */
    private String recordName;

    /**
     * 购买日期
     */
    private LocalDateTime recordDate;

    /**
     * 金额
     */
    private Double recordMoney;

    /**
     * 实物照片存储路径
     */
    private String photos;

    /**
     * 小票路径
     */
    private String receipt;

    /**
     * 验收人ID
     */
    private String checkId;

    /**
     * 状态，0未解决，1解决
     */
    private Integer state;

    public Record(String id, String recordName, LocalDateTime recordDate,
                  Double recordMoney, String photos, String receipt,
                  String checkId, Integer state) {
        this.id = id;
        this.recordName = recordName;
        this.recordDate = recordDate;
        this.recordMoney = recordMoney;
        this.photos = photos;
        this.receipt = receipt;
        this.checkId = checkId;
        this.state = state;
    }

    public Record() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public Double getRecordMoney() {
        return recordMoney;
    }

    public void setRecordMoney(Double recordMoney) {
        this.recordMoney = recordMoney;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPhotoFileName() {
        return "IMG_"+ getPhotos() +"_" + getId().toString() + ".jpg";
    }

    public String getReceiptFileName() {
        return "IMG_" + getReceipt() +"_" + getId().toString() + ".jpg";
    }
}
