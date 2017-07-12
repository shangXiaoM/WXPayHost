package com.rencare.pay.utils;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 创建时间：2015年10月23日 下午5:12:46
 * 
 * @author andy
 * @version 2.2
 */

public class SerializerFeatureUtil {

	public static final SerializerFeature[] FEATURES = {
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.QuoteFieldNames,
			SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteNullBooleanAsFalse,
			SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.DisableCircularReferenceDetect,
			SerializerFeature.IgnoreErrorGetter
	};
}
