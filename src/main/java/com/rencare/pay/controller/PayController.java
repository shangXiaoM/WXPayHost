package com.rencare.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.rencare.pay.model.JsonResult;
import com.rencare.pay.model.ResponseData;
import com.rencare.pay.utils.CollectionUtil;
import com.rencare.pay.utils.ConstValue;
import com.rencare.pay.utils.FileUtil;
import com.rencare.pay.utils.HttpUtils;
import com.rencare.pay.utils.PayUtil;
import com.rencare.pay.utils.ResponseUtil;
import com.rencare.pay.utils.SerializerFeatureUtil;
import com.rencare.pay.utils.StringUtil;
import com.rencare.pay.utils.WebUtil;
import com.rencare.pay.utils.XmlUtil;

@RestController
@RequestMapping("/rencare")
public class PayController {

	private static final Logger LOG = Logger.getLogger(PayController.class);

	/**
	 * 统一下单API
	 * 
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param cashnum
	 *            金额
	 * @param mercid
	 *            商品ID
	 * @param callback
	 */
	@RequestMapping(value = "/pay", method = { RequestMethod.POST, RequestMethod.GET })
	public void orderPay(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "0") Double cashnum, String mercid, String callback) {
		LOG.info("[/rencare/pay]");
		if (!ConstValue.CUSTODY_TYPE_LONG.equals(mercid) && !ConstValue.CUSTODY_TYPE_REALTIME_LONG.equals(mercid)) {
			String json = JSON.toJSONString(
					new JsonResult(ConstValue.RESPONSE_CODE_NO_THINGS, "商品不存在", new ResponseData()),
					SerializerFeatureUtil.FEATURES);
			WebUtil.response(response, WebUtil.packJsonp(callback, json));
			return;
		}

		Map<String, String> restmap = null;
		boolean flag = false; // 是否订单创建成功
		try {
			String total_fee = BigDecimal.valueOf(cashnum).multiply(BigDecimal.valueOf(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", ConstValue.APP_ID);
			parm.put("mch_id", ConstValue.MCH_ID);
			parm.put("device_info", "WEB"); // 设备号，默认为“WEB”
			parm.put("nonce_str", PayUtil.getNonceStr()); // 随机数
			parm.put("body", "QQ游戏-充值"); // 商品描述
			parm.put("attach", "XXXX公司"); // 附加信息
			parm.put("out_trade_no", PayUtil.getTradeNo()); // 商户内部管控的订单号
			parm.put("total_fee", total_fee); // 总费用：单位-分
			parm.put("spbill_create_ip", PayUtil.getRemoteAddrIp(request)); // 获取客户端的IP地址
			parm.put("notify_url", ConstValue.NOTIFY_URL); // 微信服务器异步通知支付结果地址
			parm.put("trade_type", "APP"); // 支付类型：APP支付
			parm.put("sign", PayUtil.getSign(parm, ConstValue.API_SECRET)); // 签名

			String restxml = HttpUtils.post(ConstValue.ORDER_PAY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		}

		Map<String, String> payMap = new HashMap<String, String>();
		if (ResponseUtil.isResponseSuccess(restmap)) {
			if (ConstValue.PAY_SUCCESS.equals(restmap.get("result_code"))) {
				flag = true;
				// 下单成功
				payMap.put("appid", ConstValue.APP_ID);
				payMap.put("partnerid", ConstValue.MCH_ID);
				payMap.put("prepayid", restmap.get("prepay_id"));
				payMap.put("package", "Sign=WXPay");
				payMap.put("noncestr", PayUtil.getNonceStr());
				payMap.put("timestamp", PayUtil.payTimestamp());
				try {
					payMap.put("sign", PayUtil.getSign(payMap, ConstValue.API_SECRET));
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			} else {
				// 下单失败
				LOG.info("订单创建失败：" + restmap.get("err_code"));
			}
		}
		String json = null;
		if (flag) {
			json = JSON.toJSONString(new JsonResult(ConstValue.RESPONSE_CODE_GET_PAYID_SUCCESS, "订单获取成功",
					new ResponseData(null, payMap)), SerializerFeatureUtil.FEATURES);
		} else {
			json = JSON.toJSONString(
					new JsonResult(ConstValue.RESPONSE_CODE_GET_PAYID_FAIL, "订单获取失败", new ResponseData()),
					SerializerFeatureUtil.FEATURES);
		}
		WebUtil.response(response, WebUtil.packJsonp(callback, json));
	}

	/**
	 * 查询支付结果：商户后台主动查询微信支付服务器，该订单的支付情况（用于商户服务器异常，没有收到微信支付服务器的支付回调时）
	 * 
	 * @param request
	 * @param response
	 * @param tradeid
	 *            微信交易订单号
	 * @param tradeno
	 *            商品订单号(商户内部维护)
	 * @param callback
	 */
	@RequestMapping(value = "/pay/query", method = { RequestMethod.POST, RequestMethod.GET })
	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeid, String tradeno,
			String callback) {
		LOG.info("[/rencare/pay/query]");
		if (StringUtil.isEmpty(tradeno) && StringUtil.isEmpty(tradeid)) {
			String json = JSON.toJSONString(
					new JsonResult(ConstValue.RESPONSE_CODE_EMPTY_PAYID, "订单号不能为空", new ResponseData()),
					SerializerFeatureUtil.FEATURES);
			WebUtil.response(response, WebUtil.packJsonp(callback, json));
			return;
		}

		String json = null;
		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", ConstValue.APP_ID);
			parm.put("mch_id", ConstValue.MCH_ID);
			parm.put("transaction_id", tradeid); // 微信订单号（优先使用）
			parm.put("out_trade_no", tradeno); // 商户订单号（但没有传transaction_id时，使用该订单号）
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("sign", PayUtil.getSign(parm, ConstValue.API_SECRET));

			String restxml = HttpUtils.post(ConstValue.ORDER_PAY_QUERY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		if (ResponseUtil.isResponseSuccess(restmap)) {
			if (ConstValue.PAY_SUCCESS.equals(restmap.get("result_code"))) {
				// 订单状态：支付成功
				json = JSON.toJSONString(
						new JsonResult(ConstValue.RESPONSE_CODE_QUERY_PAID_SUCCESS, "订单支付成功", new ResponseData()),
						SerializerFeatureUtil.FEATURES);
			} else {
				// 订单状态：支付失败
				json = JSON.toJSONString(
						new JsonResult(ConstValue.RESPONSE_CODE_QUERY_PAID_FAIL, "订单支付失败", new ResponseData()),
						SerializerFeatureUtil.FEATURES);
			}
			// 处理业务逻辑，更新对应的数据库信息等
		} else {
			json = JSON.toJSONString(new JsonResult(ConstValue.RESPONSE_CODE_QUERY_FAIL, "订单查询失败", new ResponseData()),
					SerializerFeatureUtil.FEATURES);
		}
		WebUtil.response(response, WebUtil.packJsonp(callback, json));
	}

	/**
	 * 订单支付微信服务器异步通知
	 * 
	 * @param request
	 *            微信支付服务器通知
	 * @param response
	 *            通知应答
	 */
	@RequestMapping("/pay/notify")
	public void orderPayNotify(HttpServletRequest request, HttpServletResponse response) {
		boolean flag = false;
		LOG.info("[/rencare/pay/notify]");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		String return_msg = null;
		try {
			ServletInputStream in = request.getInputStream();
			String resxml = FileUtil.readInputStream2String(in);
			Map<String, String> restmap = XmlUtil.xmlParse(resxml);
			LOG.info("支付结果通知：" + restmap);
			if (ResponseUtil.isResponseSuccess(restmap)) {
				flag = true;
				String out_trade_no = restmap.get("out_trade_no"); // 商户订单号
				// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果则进行订单业务相关的处理
				if (!false) {
					if (ConstValue.PAY_SUCCESS.equals(restmap.get("result_code"))) {
						// 订单状态：支付成功
					} else {
						// 订单状态：支付失败
						LOG.info("订单支付通知：支付失败，" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
					}
					// 处理业务逻辑，更新对应的数据库信息等
				}
			} else {
				return_msg = "签名失败，或return_code为FAIL!";
			}
		} catch (Exception e) {
			return_msg = e.getMessage();
			LOG.error(e.getMessage(), e);
		}
		Map<String, String> parm = new HashMap<String, String>();
		if (flag) {
			parm.put("return_code", ConstValue.PAY_SUCCESS);
		} else {
			parm.put("return_code", ConstValue.PAY_FAIL);
		}
		parm.put("return_msg", return_msg);
		String repoxml = XmlUtil.xmlFormat(parm, true);
		WebUtil.response(response, repoxml);
	}

	/**
	 * 订单退款 需要双向证书验证
	 * 
	 * @param request
	 * @param response
	 * @param tradeno
	 *            微信订单号
	 * @param orderno
	 *            商家订单号
	 * @param callback
	 */
	@RequestMapping(value = "/pay/refund", method = { RequestMethod.POST, RequestMethod.GET })
	public void orderPayRefund(HttpServletRequest request, HttpServletResponse response, String tradeno, String orderno,
			String callback) {
		LOG.info("[rencare/pay/refund]");
		if (StringUtil.isEmpty(tradeno) && StringUtil.isEmpty(orderno)) {
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单号不能为空", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}

		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", ConstValue.APP_ID);
			parm.put("mch_id", ConstValue.MCH_ID);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("transaction_id", tradeno);
			parm.put("out_trade_no", orderno);// 订单号
			parm.put("out_refund_no", PayUtil.getRefundNo()); // 退款单号
			parm.put("total_fee", "10"); // 订单总金额 从业务逻辑获取
			parm.put("refund_fee", "10"); // 退款金额
			parm.put("op_user_id", ConstValue.MCH_ID);
			parm.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");// 退款方式
			parm.put("sign", PayUtil.getSign(parm, ConstValue.API_SECRET));
			String restxml = HttpUtils.posts(ConstValue.ORDER_REFUND, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		Map<String, String> refundMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			refundMap.put("transaction_id", restmap.get("transaction_id"));
			refundMap.put("out_trade_no", restmap.get("out_trade_no"));
			refundMap.put("refund_id", restmap.get("refund_id"));
			refundMap.put("out_refund_no", restmap.get("out_refund_no"));
			LOG.info("订单退款：订单" + restmap.get("out_trade_no") + "退款成功，商户退款单号" + restmap.get("out_refund_no") + "，微信退款单号"
					+ restmap.get("refund_id"));
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON.toJSONString(
					new JsonResult(1, "订单获取成功", new ResponseData(null, refundMap)), SerializerFeatureUtil.FEATURES)));
		} else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单退款失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单退款失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
	}

	/**
	 * 订单退款查询
	 * 
	 * @param request
	 * @param response
	 * @param tradeid
	 *            微信订单号
	 * @param tradeno
	 *            商户订单号
	 * @param refundid
	 *            微信退款号
	 * @param refundno
	 *            商家退款号
	 * @param callback
	 */
	@RequestMapping(value = "/pay/refund/query", method = { RequestMethod.POST, RequestMethod.GET })
	public void orderPayRefundQuery(HttpServletRequest request, HttpServletResponse response, String refundid,
			String refundno, String tradeid, String tradeno, String callback) {
		LOG.info("[rencare/pay/refund/query]");
		if (StringUtil.isEmpty(tradeid) && StringUtil.isEmpty(tradeno) && StringUtil.isEmpty(refundno)
				&& StringUtil.isEmpty(refundid)) {
			WebUtil.response(response,
					WebUtil.packJsonp(callback, JSON.toJSONString(new JsonResult(-1, "退单号或订单号不能为空", new ResponseData()),
							SerializerFeatureUtil.FEATURES)));
		}

		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", ConstValue.APP_ID);
			parm.put("mch_id", ConstValue.MCH_ID);
			parm.put("transaction_id", tradeid);
			parm.put("out_trade_no", tradeno);
			parm.put("refund_id", refundid);
			parm.put("out_refund_no", refundno);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("sign", PayUtil.getSign(parm, ConstValue.API_SECRET));

			String restxml = HttpUtils.post(ConstValue.ORDER_REFUND_QUERY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		Map<String, String> refundMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))
				&& "SUCCESS".equals(restmap.get("result_code"))) {
			// 订单退款查询成功 处理业务逻辑
			LOG.info("退款订单查询：订单" + restmap.get("out_trade_no") + "退款成功，退款状态" + restmap.get("refund_status_0"));
			refundMap.put("transaction_id", restmap.get("transaction_id"));
			refundMap.put("out_trade_no", restmap.get("out_trade_no"));
			refundMap.put("refund_id", restmap.get("refund_id_0"));
			refundMap.put("refund_no", restmap.get("out_refund_no_0"));
			refundMap.put("refund_status", restmap.get("refund_status_0"));
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON.toJSONString(
					new JsonResult(1, "订单退款成功", new ResponseData(null, refundMap)), SerializerFeatureUtil.FEATURES)));
		} else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单退款失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单退款失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
	}

}
