package com.park.einvoice.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.einvoice.common.GetRequestJsonTools;
import com.park.einvoice.service.EnterpriseService;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {
	
	@Resource(name="enterpriseServiceImpl")
	private EnterpriseService enterpriseService;
	
	@ResponseBody
	@RequestMapping(value="/enterpriseregpush", produces = "text/html;charset=UTF-8")
	public String enterpriseRegPush(HttpServletRequest request, HttpServletResponse response){
		String requestJsonStr = null;
		try {
			requestJsonStr = GetRequestJsonTools.getRequestJsonString(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return enterpriseService.dealRegisterPush(requestJsonStr);
	}
	
}
