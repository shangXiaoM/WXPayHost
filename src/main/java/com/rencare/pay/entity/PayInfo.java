package com.rencare.pay.entity;

import java.io.Serializable;

public class PayInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String mAppId;	// 应用ID
	private String mMchId;	// 商户号
	private String mTradeType;	// 交易类型
	private String mPrepayId;	// 预支付ID
	private String mBody;	// 商品描述
	private String mOutTradeNo;	// 商户订单号
	private String mTotalFee;	// 交易金额
	private String mBankType;	// 支付银行类型
	private String mTransactionId;	// 微信支付订单号
	private String mTimeEnd;	// 交易完成时间
	private String mCashFee;	// 支付金额
	private int mPayState;	// 支付状态：1支付成功，0未支付
	private int mNotifyState;	// 通知状态：1已通知，0未通知
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getmAppId() {
		return mAppId;
	}
	public void setmAppId(String mAppId) {
		this.mAppId = mAppId;
	}
	public String getmMchId() {
		return mMchId;
	}
	public void setmMchId(String mMchId) {
		this.mMchId = mMchId;
	}
	public String getmTradeType() {
		return mTradeType;
	}
	public void setmTradeType(String mTradeType) {
		this.mTradeType = mTradeType;
	}
	public String getmPrepayId() {
		return mPrepayId;
	}
	public void setmPrepayId(String mPrepayId) {
		this.mPrepayId = mPrepayId;
	}
	public String getmBody() {
		return mBody;
	}
	public void setmBody(String mBody) {
		this.mBody = mBody;
	}
	public String getmOutTradeNo() {
		return mOutTradeNo;
	}
	public void setmOutTradeNo(String mOutTradeNo) {
		this.mOutTradeNo = mOutTradeNo;
	}
	public String getmTotalFee() {
		return mTotalFee;
	}
	public void setmTotalFee(String mTotalFee) {
		this.mTotalFee = mTotalFee;
	}
	public String getmBankType() {
		return mBankType;
	}
	public void setmBankType(String mBankType) {
		this.mBankType = mBankType;
	}
	public String getmTransactionId() {
		return mTransactionId;
	}
	public void setmTransactionId(String mTransactionId) {
		this.mTransactionId = mTransactionId;
	}
	public String getmTimeEnd() {
		return mTimeEnd;
	}
	public void setmTimeEnd(String mTimeEnd) {
		this.mTimeEnd = mTimeEnd;
	}
	public String getmCashFee() {
		return mCashFee;
	}
	public void setmCashFee(String mCashFee) {
		this.mCashFee = mCashFee;
	}
	public int getmPayState() {
		return mPayState;
	}
	public void setmPayState(int mPayState) {
		this.mPayState = mPayState;
	}
	public int getmNotifyState() {
		return mNotifyState;
	}
	public void setmNotifyState(int mNotifyState) {
		this.mNotifyState = mNotifyState;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
