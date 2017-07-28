package com.rencare.pay.utils;

public class ConstValue {
	
    public static final String CUSTODY_TYPE_LONG = "1";
    public static final String CUSTODY_TYPE_REALTIME_LONG = "2";
    // 支付成功
    public static final String PAY_SUCCESS = "SUCCESS";
    // 支付失败
    public static final String PAY_FAIL = "FAIL";
    
    //微信支付成功后通知地址 必须要求80端口并且地址不能带参数
  	public static final String NOTIFY_URL = "http://yewei8.natappfree.cc/rencare/pay/notify";
  	
  	public static final String SANDBOX_SIGN = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey"; // 仿真系统获取签名的URL
    
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
	public static final int RESPONSE_CODE_NO_THINGS = -100;
	/**
	 * 统一下单失败
	 */
	public static final int RESPONSE_CODE_GET_PAYID_FAIL = -101;
	/**
	 * 统一下单成功
	 */
	public static final int RESPONSE_CODE_GET_PAYID_SUCCESS = 100;
	
	/**
	 * 订单号为空
	 */
	public static final int RESPONSE_CODE_EMPTY_PAYID = -200;
	/**
	 * 查询订单成功
	 */
	public static final int RESPONSE_CODE_QUERY_SUCCESS = 200;
	/**
	 * 查询订单失败
	 */
	public static final int RESPONSE_CODE_QUERY_FAIL = -201;
	/**
	 * 查询订单支付成功
	 */
	public static final int RESPONSE_CODE_QUERY_PAID_SUCCESS = 201;
	/**
	 * 查询订单支付失败
	 */
	public static final int RESPONSE_CODE_QUERY_PAID_FAIL = -202;
}
