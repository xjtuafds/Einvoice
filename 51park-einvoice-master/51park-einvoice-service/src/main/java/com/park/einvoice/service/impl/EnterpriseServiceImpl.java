package com.park.einvoice.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.park.einvoice.common.Base64Tools;
import com.park.einvoice.common.DataChangeTools;
import com.park.einvoice.common.HttpTools;
import com.park.einvoice.common.OpenApiTools;
import com.park.einvoice.common.RSATools;
import com.park.einvoice.common.ResultTools;
import com.park.einvoice.common.constants.CodeConstants;
import com.park.einvoice.common.properties.Invoice;
import com.park.einvoice.common.service.ImageService;
import com.park.einvoice.dao.EnterpriseDao;
import com.park.einvoice.dao.EnterpriseRegisterDao;
import com.park.einvoice.domain.Enterprise;
import com.park.einvoice.domain.EnterpriseRegister;
import com.park.einvoice.domain.request.EnterpriseRegPushReq;
import com.park.einvoice.domain.request.EnterpriseRegPushReqContent;
import com.park.einvoice.domain.response.EnterpriseRegPTResp;
import com.park.einvoice.service.EnterpriseService;

@Repository(value="enterpriseServiceImpl")
public class EnterpriseServiceImpl implements EnterpriseService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Invoice invoice;
	
	@Resource(name="enterpriseDao")
	private EnterpriseDao enterpriseDao;
	@Resource(name="enterpriseRegisterDao")
	private EnterpriseRegisterDao enterpriseRegisterDao;
	@Resource(name="imageService")
	private ImageService image;
	
	private String url4Reg;
	private String privateKey;
	private String ptPublicKey;
	private String password;
	private String prefix;
	private String platformCode;
	
	
	
	public void getInvoiceInfo(){
		url4Reg = invoice.getUrl4Reg();
		privateKey = invoice.getPrivateKey();
		ptPublicKey = invoice.getPtPublicKey();
		password = invoice.getPassword();
		prefix = invoice.getPrefix();
		platformCode = invoice.getPlatformCode();
	}
	
	@Override
	public boolean enterpriseRegister(Enterprise enterprise) {
		getInvoiceInfo();
		//校验入参是否为空
		if(enterprise == null){
			return false;
		}
		//创建map并添加参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("taxpayerNum", enterprise.getTaxpayer_num());
        map.put("enterpriseName", enterprise.getEnterprise_name());
        map.put("legalPersonName", enterprise.getLegal_person_name());
        map.put("contactsName", enterprise.getContact_name());
        map.put("contactsEmail", enterprise.getContact_email());
        map.put("contactsPhone", enterprise.getContact_phone());
        map.put("regionCode", enterprise.getRegion_code());
        map.put("cityName", enterprise.getCity_name());
        map.put("enterpriseAddress", enterprise.getAddress());
        //获取oss图片可访问的地址
        InputStream inputStream= image.getOSS2InputStream(enterprise.getTax_reg_cer());
        String fileString = Base64Tools.encode2FileUrl(inputStream);
        map.put("taxRegistrationCertificate", fileString);
        map.put("isPermitPaperInvoice", enterprise.getCan_paper_invoice() == 1 ? "true" : "false");
        map.put("operationType", enterprise.getOperate_type() == 2 ? "1" : "0");
        try {
        	//将map转换为json
			String content = DataChangeTools.bean2gson(map);
			logger.info("** 注册/更新企业请求：[企业名称：" + enterprise.getEnterprise_name()
				 + "，纳税识别号：" + enterprise.getTaxpayer_num() + "]");
			//将json数据与签名串等信息结合进行签名
			String buildRequest = new OpenApiTools(password, platformCode, prefix, privateKey).buildRequest(content);
			//发送请求并接收返回值
			String response = HttpTools.postJson(url4Reg, buildRequest);
			if(response.contains("<!DOCTYPE")){
				logger.error("** 注册/更新企业的发票方接口出现异常,请稍后重试！");
				return false;
			}
			//将返回值解析得到返回的数据
			String disponseResponse = new OpenApiTools(password, platformCode, prefix, privateKey).disposeResponse(response, ptPublicKey);
			//将返回的数据封装为对象
			EnterpriseRegPTResp enterpriseRegPTResp = DataChangeTools.gson2bean(disponseResponse, EnterpriseRegPTResp.class);
			String ptCode = enterpriseRegPTResp.getCode();
			String ptMsg = enterpriseRegPTResp.getMsg();
			logger.info("** 注册/更新企业回应：[code:" + ptCode + "，msg:" + ptMsg + "]");
			//判断返回的错误码，如果不成功，返回false
			if(!enterpriseRegPTResp.getCode().equals("0000")){
				logger.error("** 企业注册/更新失败，错误码：" + ptCode);
				logger.error("** 企业注册/更新失败，错误原因：" + ptMsg);
				return false;
			}
			logger.info("** 企业注册/更新成功！");
			return true;
		} catch (Exception e) {
			logger.error("** 企业注册/更新失败，发生异常：" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public String dealRegisterPush(String enterpriseRegPush) {
		getInvoiceInfo();
		//声明要返回的json串和交易流水号
		String jsonResult = null;
		String serialNo = null;
		try {
			//对请求进行验签------
			Map<String, String> paramMap = DataChangeTools.json2Map(enterpriseRegPush);
			//校验入参是否为空
			if(paramMap == null || paramMap.isEmpty()){
				return ResultTools.setResponse(CodeConstants.签名验证失败.getCode());
			}
			String sign = paramMap.remove("sign");
			boolean verify = RSATools.verify(RSATools.getSignatureContent(paramMap), sign, ptPublicKey);
			if(!verify){
				return ResultTools.setResponse(CodeConstants.签名验证失败.getCode());
			}
			//验签结束----------
			
			//解析请求
			//	enterpriseRegPushStr：企业注册推送json字符串
			String enterpriseRegPushStr = new OpenApiTools(password, platformCode, prefix, privateKey).disposeResponse(enterpriseRegPush, ptPublicKey);
			//将请求转化为对象
			//	EnterpriseRegPushReq:企业注册推送请求对象
			EnterpriseRegPushReq enterpriseRegPushReq = DataChangeTools.gson2bean(enterpriseRegPushStr, EnterpriseRegPushReq.class);
			//	EnterpriseRegPushReqContent：企业注册推送请求内容体对象
			EnterpriseRegPushReqContent content = enterpriseRegPushReq.getContent();
			logger.info("** 注册企业推送报文：" + DataChangeTools.bean2gson(content));
			//检测推送是否有效
			String taxpayerNum = content.getTaxpayerNum();
			String enterpriseName = content.getEnterpriseName();
			Integer enterpriseRegisterId = enterpriseRegisterDao.getId(taxpayerNum);
			//开始对请求进行处理(封装注册企业对象 -> 将信息写入数据库)
			EnterpriseRegister enterpriseRegister = new EnterpriseRegister();
			enterpriseRegister.setId(enterpriseRegisterId);
			enterpriseRegister.setTaxpayer_num(content.getTaxpayerNum());
			enterpriseRegister.setPlatform_code(content.getPlatformCode());
			enterpriseRegister.setRegistration_code(content.getRegistrationCode());
			enterpriseRegister.setAuthorization_code(content.getAuthorizationCode());
			enterpriseRegister.setCreate_time(new Date());
			enterpriseRegisterDao.update(enterpriseRegister);
			Map<String, Object> daoParamMap = new HashMap<String, Object>();
			daoParamMap.put("taxpayerNum", taxpayerNum);
			daoParamMap.put("operationType", 2);
			enterpriseDao.updateOperateType(daoParamMap);
			//封装返回信息为json
			Map<String, String> contentMap = new HashMap<String, String>();
			contentMap.put("taxpayerNum", taxpayerNum);
			contentMap.put("enterpriseName", enterpriseName);
			contentMap.put("platformCode", content.getPlatformCode());
			contentMap.put("registrationCode", content.getRegistrationCode());
			contentMap.put("authorizationCode", content.getAuthorizationCode());
			String respContent = DataChangeTools.bean2gson(contentMap);
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("code", CodeConstants.电子发票成功.getCode());
			resultMap.put("msg", CodeConstants.电子发票成功.getContent());
			resultMap.put("serialNo", serialNo);
			resultMap.put("content", respContent);
			resultMap.put("sign", RSATools.sign(RSATools.getSignatureContent(resultMap), privateKey));
			jsonResult = DataChangeTools.bean2gson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("code", CodeConstants.电子发票失败.getCode());
			resultMap.put("msg", CodeConstants.电子发票失败.getContent());
			resultMap.put("serialNo", serialNo);
			logger.info("** 注册企业推送回应报文：" + DataChangeTools.bean2gson(resultMap));
			resultMap.put("sign", RSATools.sign(RSATools.getSignatureContent(resultMap), privateKey));
			jsonResult = DataChangeTools.bean2gson(resultMap);
		}
		return jsonResult;
	}
}
