package com.e1858.building.bean;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable {
	public static final long	serialVersionUID	= 1L;
	public String				orderID;					//抢单信息ID
	public String				serviceType;				//抢单信息服务类型
	public String				serviceTime;				//抢单信息服务时间
	public String				orderTime;					//抢单信息下单时间
	public String				orderAddress;				//抢单信息服务地址
	public String				buyerName;					//服务对象名字（收货人）
	public String				buyerMobile;				//服务对象手机号
	public String				orderFullAddress;			//服务对象详细地址
	public List<GoodsInfo>		goodsInfos;				//商品实体类
	public String				logisticsName;				//物流公司
	public int					allGoodsNum;				//货物数量
	public String				takesAddress;				//提货地址
	public String				origin;					//订单来源
	public int					orderStatus;				//订单状态
	public boolean				isUploadPicture;			//是否上传完成效果图
	public String				ID;
	public String				orderSN;

	public boolean				isArrive;					//是否上门签到
	public boolean				isAccept;					//是否提货签到

	public boolean				isConfirmGoods;			//是否商品验货
	public float				servicePrice;				//服务报价

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerMobile() {
		return buyerMobile;
	}

	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}

	public String getOrderFullAddress() {
		return orderFullAddress;
	}

	public void setOrderFullAddress(String orderFullAddress) {
		this.orderFullAddress = orderFullAddress;
	}

	public List<GoodsInfo> getGoodsInfos() {
		return goodsInfos;
	}

	public void setGoodsInfos(List<GoodsInfo> goodsInfos) {
		this.goodsInfos = goodsInfos;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public int getAllGoodsNum() {
		return allGoodsNum;
	}

	public void setAllGoodsNum(int allGoodsNum) {
		this.allGoodsNum = allGoodsNum;
	}

	public String getTakesAddress() {
		return takesAddress;
	}

	public void setTakesAddress(String takesAddress) {
		this.takesAddress = takesAddress;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public boolean isUploadPicture() {
		return isUploadPicture;
	}

	public void setUploadPicture(boolean isUploadPicture) {
		this.isUploadPicture = isUploadPicture;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOrderSN() {
		return orderSN;
	}

	public void setOrderSN(String orderSN) {
		this.orderSN = orderSN;
	}

	public boolean isArrive() {
		return isArrive;
	}

	public void setArrive(boolean isArrive) {
		this.isArrive = isArrive;
	}

	public boolean isAccept() {
		return isAccept;
	}

	public void setAccept(boolean isAccept) {
		this.isAccept = isAccept;
	}

	public boolean isConfirmGoods() {
		return isConfirmGoods;
	}

	public void setConfirmGoods(boolean isConfirmGoods) {
		this.isConfirmGoods = isConfirmGoods;
	}

	public float getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(float servicePrice) {
		this.servicePrice = servicePrice;
	}
}
