package com.rencare.pay.dao;

import org.apache.ibatis.annotations.Mapper;

import com.rencare.pay.entity.PayInfo;

@Mapper
public interface PayMapper {
	/**
	 * 统一下单成功后，插入预支付信息
	 * 
	 * @param payInfo
	 * @return
	 */
	int insertPayInfo(PayInfo payInfo);

	/**
	 * 通过商户订单号，主动查询支付结果
	 * 
	 * @param outTradeNo
	 *            商户订单号
	 * @return
	 */
	PayInfo queryByOTN(String outTradeNo);

	/**
	 * 根据支付结果通知，更新支付信息
	 * 
	 * @param payInfo
	 *            支付信息
	 */
	void updatePayInfo(PayInfo payInfo);

	/**
	 * 通过预支付id，获取支付信息
	 * 
	 * @param prepayId
	 *            预支付id
	 * @return
	 */
	PayInfo queryByPreId(String prepayId);
	
}
