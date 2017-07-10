package com.park.einvoice.service.eih5.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.park.einvoice.common.DataChangeTools;
import com.park.einvoice.common.ResultTools;
import com.park.einvoice.common.constants.CodeConstants;
import com.park.einvoice.dao.eih5.OrderInvoiceDao;
import com.park.einvoice.service.eih5.OrderInvoiceService;

@Repository(value="orderInvoiceServiceImpl")
public class OrderInvoiceServiceImpl implements OrderInvoiceService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="orderInvoiceDao")
	private OrderInvoiceDao orderInvoiceDao;

	@Override
	public String getOrderInvoiceStatus(String requestStr) {
		try {
			//从请求中获取主订单号
			Map<String, String> requestMap = DataChangeTools.json2Map(requestStr);
			String tradeNo = requestMap.get("tradeNo");
			logger.info("** 获取订单发票信息请求的主订单号：" + tradeNo);
			//校验订单号
			if(tradeNo == null || tradeNo.equals("")){
				return ResultTools.setResponse(CodeConstants.参数为空.getCode());
			}
			//检验企业是否注册
			int isReg = orderInvoiceDao.selectIsRegEnterprise(tradeNo);
			if(isReg == 0){
				return ResultTools.setResponse(CodeConstants.企业未注册.getCode());
			}
			//根据主订单号获取需要返回的数据
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap = orderInvoiceDao.selectOrderInvoiceInfo(tradeNo);
			//通过总金额和已开票金额计算未开票金额，并放入结果集
			BigDecimal costAfter = new BigDecimal(resultMap.get("costAfter"));
			String hasInvoiceStr = resultMap.get("hasInvoice");
			//校验hasInvoice是否为null
			if(hasInvoiceStr == null){
				resultMap.put("hasInvoice", new BigDecimal(0).toString());
				resultMap.put("noInvoice", costAfter.toString());
			}else{
				BigDecimal hasInvoice = new BigDecimal(hasInvoiceStr);
				resultMap.put("noInvoice", costAfter.subtract(hasInvoice).toString());
			}
			//返回数据
			return ResultTools.setObjectResponse(CodeConstants.成功.getCode(), resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("** 获取订单发票信息异常：" + e.getMessage());
			return ResultTools.setResponse(CodeConstants.服务器异常.getCode());
		}
	}
	
}
