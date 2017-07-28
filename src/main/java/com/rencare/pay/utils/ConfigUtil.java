package com.rencare.pay.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 
 * @author "shangM"
 *
 */
public class ConfigUtil {

	private static final Logger LOG = Logger.getLogger(ConfigUtil.class);

	private static Properties config = null;

	/**
	 * 解析config.properties，获取需要的配置信息，如APP_ID，商户号以及密钥
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		if (config == null) {
			synchronized (ConfigUtil.class) {
				if (null == config) {
					try {
						Resource resource = new ClassPathResource("config.properties");
						config = PropertiesLoaderUtils.loadProperties(resource);
					} catch (IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		return config.getProperty(key);
	}
}
