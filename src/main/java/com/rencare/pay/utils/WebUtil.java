package com.rencare.pay.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {

	public static Object getSessionAttribute(HttpServletRequest req, String key) {
		Object ret = null;
		try {
			ret = req.getSession(false).getAttribute(key);
		} catch (Exception e) {
		}
		return ret;
	}

	public static void response(HttpServletResponse response, String result) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String packJsonp(String callback, String json) {
		if (json == null) {
			json = "";
		}
		if (callback == null || callback.isEmpty()) {
			return json;
		}
		return callback + "&&" + callback + '(' + json + ')';
	}

}
