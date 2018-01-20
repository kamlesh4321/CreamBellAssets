package info.kamleshgupta.myapplicationassets.model;

import java.io.Serializable;


public class OrderModel implements Serializable {

    private String partyName;
    private String orderNo;
    private String orderQty;
    private String orderDate;
    private String despQty;
    private String deliveryDate;
    private String isApprove;
    private String OrdNo;
    String OrdStatus;
    public OrderModel(String partyName, String orderNo, String orderQty, String orderDate, String despQty, String deliveryDate,String isApprove,String OrdNo,String OrdStatus) {
        this.partyName = partyName;
        this.orderNo = orderNo;
        this.orderQty = orderQty;
        this.orderDate = orderDate;
        this.despQty = despQty;
        this.deliveryDate = deliveryDate;
        this.isApprove = isApprove;
        this.OrdNo=OrdNo;
        this.OrdStatus=OrdStatus;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDespQty() {
        return despQty;
    }

    public void setDespQty(String despQty) {
        this.despQty = despQty;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getIsApp() {
        return isApprove;
    }

    public void setIsApp(String isApprove) {
        this.isApprove = isApprove;
    }

    public String getOrdNo() {
        return OrdNo;
    }

    public void setOrdNo(String OrdNo) {
        this.OrdNo = OrdNo;
    }

    public String getOrdStatus() {
        return OrdStatus;
    }

    public void setOrderStatus(String OrdStatus) {
        this.OrdStatus = OrdStatus;
    }
}
