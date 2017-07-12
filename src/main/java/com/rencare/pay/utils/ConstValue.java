package com.rencare.pay.utils;

public class ConstValue {
	
	// 支付回调URL路径
	public static final String NOTIFY_URL = "https://www.andy.co/wxpay/order/pay/notify.shtml";
    public static final String CUSTODY_TYPE_LONG = "1";
    public static final String CUSTODY_TYPE_REALTIME_LONG = "2";
    // 支付成功
    public static final String PAY_SUCCESS = "SUCCESS";
    // 支付失败
    public static final String PAY_FAIL = "FAIL";
    
    public static final String ORDER_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 统一下单

    public static final String ORDER_PAY_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery"; // 支付订单查询

    public static final String ORDER_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund"; // 申请退款

    public static final String ORDER_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery"; // 申请退款查询

    public static final String APP_ID = ConfigUtil.getProperty("wx.appid"); // 微信支付APP_ID

    public static final String MCH_ID = ConfigUtil.getProperty("wx.mchid"); // 微信支付商户号

    public static final String API_SECRET = ConfigUtil.getProperty("wx.api.secret"); // 密钥
    
    
	/**
	 * 没有商品
	 */
	public static final int RESPONSE_CODE_NO_THINGS = -501;
	/**
	 * 统一下单失败
	 */
	public static final int RESPONSE_CODE_GET_PAYID_FAIL = -102;
	/**
	 * 统一下单成功
	 */
	public static final int RESPONSE_CODE_GET_PAYID_SUCCESS = 101;
	
	/**
	 * 订单号为空
	 */
	public static final int RESPONSE_CODE_EMPTY_PAYID = -502;
	/**
	 * 查询订单成功
	 */
	public static final int RESPONSE_CODE_QUERY_SUCCESS = 201;
	/**
	 * 查询订单失败
	 */
	public static final int RESPONSE_CODE_QUERY_FAIL = 202;
	/**
	 * 查询订单支付成功
	 */
	public static final int RESPONSE_CODE_QUERY_PAID_SUCCESS = 203;
	/**
	 * 查询订单支付失败
	 */
	public static final int RESPONSE_CODE_QUERY_PAID_FAIL = 204;
}
