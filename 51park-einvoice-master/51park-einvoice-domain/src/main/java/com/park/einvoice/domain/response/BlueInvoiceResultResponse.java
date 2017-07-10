package com.park.einvoice.domain.response;

import java.math.BigDecimal;

/**
 * 无忧应用查询蓝票是否开具成功时返回数据的对象
 * @author WangYuefei
 *
 */
public class BlueInvoiceResultResponse {
	private String tradeNo,invoiceStatus,invoiceReqSerialNo,securityCode,
		qrCode,invoiceCode,invoiceNo,invoicePdf,downloadUrl;
	private BigDecimal invoiceAmount;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceReqSerialNo() {
		return invoiceReqSerialNo;
	}

	public void setInvoiceReqSerialNo(String invoiceReqSerialNo) {
		this.invoiceReqSerialNo = invoiceReqSerialNo;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoicePdf() {
		return invoicePdf;
	}

	public void setInvoicePdf(String invoicePdf) {
		this.invoicePdf = invoicePdf;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
