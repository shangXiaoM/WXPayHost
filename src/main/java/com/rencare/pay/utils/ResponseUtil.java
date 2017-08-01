package com.rencare.pay.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rencare.pay.Application;
import com.rencare.pay.controller.PayController;

public class ResponseUtil {
	private static final Logger LOG = Logger.getLogger(Application.class);

	private ResponseUtil() {
		super();
	}

	/**
	 * 
	 * @param restmap
	 * @return	
	 */
	public static boolean isResponseSuccess(Map<String, String> restmap) {
		boolean flag = false;
		if (CollectionUtil.isNotEmpty(restmap)) { // 应答不为null
			if (ConstValue.PAY_SUCCESS.equals(restmap.get("return_code"))) {
				try {
					String sign = restmap.get("sign"); // 返回的签名
					restmap.remove("sign");
					String signnow = PayUtil.getSign(restmap, PayController.API_KEY);
					if (signnow.equals(sign)) { // 签名成功
						flag = true;
					} else {
						LOG.info("返回的签名错误：返回签名" + sign + "计算签名：" + signnow + "API-KEY:" + PayController.API_KEY);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					LOG.info(e.getMessage());
				}
			} else {
				LOG.info(restmap.get("return_msg"));
			}
		}
		return flag;
	}
}
