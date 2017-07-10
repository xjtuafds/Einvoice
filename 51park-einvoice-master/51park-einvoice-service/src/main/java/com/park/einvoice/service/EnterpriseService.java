package com.park.einvoice.service;

import org.springframework.stereotype.Repository;

import com.park.einvoice.domain.Enterprise;

@Repository(value="enterpriceService")
public interface EnterpriseService {
	
	/**
	 * 从invoice对象中读取所有参数
	 */
	public void getInvoiceInfo();
	
	/**
	 * 企业注册
	 * @param enterprice 企业对象
	 * @return 注册是否成功 true-成功 false-失败
	 */
	public boolean enterpriseRegister(Enterprise enterprise);
	
	/**
	 * 企业注册后，接收企业推送内容并进行处理
	 * @param content 注册企业推送消息的字符串
	 * @return 返回回应消息
	 */
	public String dealRegisterPush(String enterpriseRegPush);
}
