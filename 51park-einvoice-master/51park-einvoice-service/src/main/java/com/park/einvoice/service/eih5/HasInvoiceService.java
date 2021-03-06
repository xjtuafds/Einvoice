package com.park.einvoice.service.eih5;

import org.springframework.stereotype.Repository;

@Repository(value="hasInvoiceService")
public interface HasInvoiceService {

	/**
	 * 获取已经开票的发票列表
	 * @param requestStr 请求字符串，包含主订单号
	 * @return 返回需要的数据 JSON字符串
	 */
	String getHasInvoiceList(String requestStr);

	/**
	 * 获取已经开票的某个发票详情
	 * @param requestStr 请求字符串，包含发票请求流水号
	 * @return 返回需要的数据 JSON字符串
	 */
	String getHasInvoiceDetail(String requestStr);
	
}
