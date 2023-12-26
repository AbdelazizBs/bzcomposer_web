/*
 * Author : Avibha IT Solutions Copyright 2006 Avibha IT Solutions. All rights
 * reserved. AVIBHA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * www.avibha.com
 */

package com.avibha.bizcomposer.rma.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import javax.servlet.http.HttpServletRequest;
@NoArgsConstructor
public class RMADto {
	
	private static final long serialVersionUID = 0;

	private int startPage;
	
	private int totalPages;
	
	private String Rma;

	private String fname;

	private String lname;

	private String order;

	private String orderDate;

	private String sentDate;

	private String cusName;

	private String itemCode;

	private String itemDesc;

	private String Qty;

	private String Reason;

	private String unitPrice;

	private String unitWeight;

	private String cartID;

	public String getRma() {
		return Rma;
	}

	public void setRma(String rma) {
		Rma = rma;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(String unitWeight) {
		this.unitWeight = unitWeight;
	}

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

	/*public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

		Rma = null;

		fname = null;

		lname = null;

		order = null;

		orderDate = null;

		sentDate = null;

		cusName = null;

		itemCode = null;

		itemDesc = null;

		Qty = null;

		Reason = null;

		unitPrice = null;

		unitWeight = null;

		cartID = null;

	}*/

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public RMADto(String firstName, String lastName, Integer orderNum, String dateAdded, String dateConfirmed) {
		this.fname = firstName;
		this.lname = lastName;
		this.Rma = Integer.toString(orderNum);
		this.orderDate = dateAdded;
		this.sentDate = dateConfirmed;
	}
	public RMADto(Integer rmaNo, String firstName , String lastName ,String inventoryCode, String inventoryName, String rmaReason 
			, Integer rmaQty ,Double unitPrice , Double unitWeight , OffsetDateTime dateAdded ) {
		this.Rma=Integer.toString(rmaNo);
		this.fname=firstName;
		this.lname=lastName;
		this.itemCode=inventoryCode;
		this.itemDesc=inventoryName;
		this.Reason=rmaReason;
		this.Qty=Double.toString(rmaQty);
		this.unitPrice=Double.toString(unitPrice);
		this.unitWeight=Double.toString(unitWeight);
		this.sentDate=dateAdded.toString();
	}
	public RMADto(Integer rmaNo, String firstName , String lastName ,String inventoryCode, String inventoryName, String rmaReason 
			, Integer rmaQty ,Double unitPrice , Double unitWeight , String dateAdded, Integer orderNum ) {
		this.Rma=Integer.toString(rmaNo);
		this.fname=firstName;
		this.lname=lastName;
		this.itemCode=inventoryCode;
		this.itemDesc=inventoryName;
		this.Reason=rmaReason;
		this.Qty=Double.toString(rmaQty);
		this.unitPrice=Double.toString(unitPrice);
		this.unitWeight=Double.toString(unitWeight);
		this.sentDate=dateAdded;
		this.order=orderNum.toString();
	}
	
	
}
