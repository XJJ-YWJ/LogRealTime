package com.realtime.publish.Bean;

import java.io.Serializable;

public class OrderLog implements Serializable {
    private String area;
    private String consignee;
    private String orderComment;
    private String consigneeTel;
    private String operateTime;
    private String orderStatus;
    private String paymentWay;
    private String userId;
    private String imgUrl;
    private Double totalAmount;
    private String expireTime;
    private String deliveryAddress;
    private String createTime;
    private String trackingNo;
    private String parentOrderId;
    private String outTradeNo;
    private String id;
    private String tradeBody;
    private String createDate;
    private Integer createHour;
    private String createHourMinute;

    public OrderLog() {
    }

    public OrderLog(String area, String consignee, String orderComment, String consigneeTel, String operateTime, String orderStatus, String paymentWay, String userId, String imgUrl, Double totalAmount, String expireTime, String deliveryAddress, String createTime, String trackingNo, String parentOrderId, String outTradeNo, String id, String tradeBody, String createDate, Integer createHour, String createHourMinute) {
        this.area = area;
        this.consignee = consignee;
        this.orderComment = orderComment;
        this.consigneeTel = consigneeTel;
        this.operateTime = operateTime;
        this.orderStatus = orderStatus;
        this.paymentWay = paymentWay;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.totalAmount = totalAmount;
        this.expireTime = expireTime;
        this.deliveryAddress = deliveryAddress;
        this.createTime = createTime;
        this.trackingNo = trackingNo;
        this.parentOrderId = parentOrderId;
        this.outTradeNo = outTradeNo;
        this.id = id;
        this.tradeBody = tradeBody;
        this.createDate = createDate;
        this.createHour = createHour;
        this.createHourMinute = createHourMinute;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeBody() {
        return tradeBody;
    }

    public void setTradeBody(String tradeBody) {
        this.tradeBody = tradeBody;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateHour() {
        return createHour;
    }

    public void setCreateHour(Integer createHour) {
        this.createHour = createHour;
    }

    public String getCreateHourMinute() {
        return createHourMinute;
    }

    public void setCreateHourMinute(String createHourMinute) {
        this.createHourMinute = createHourMinute;
    }
}
