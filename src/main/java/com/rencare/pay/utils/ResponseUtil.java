package com.rencare.pay.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rencare.pay.Application;


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
				String sing = restmap.get("sign"); // 返回的签名
				restmap.remove("sign");
				try {
					String signnow = PayUtil.getSign(restmap, ConstValue.API_SECRET);
					if (signnow.equals(sing)) { // 签名成功
						flag = true;
					} else {
						LOG.info("返回的签名错误！");
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
