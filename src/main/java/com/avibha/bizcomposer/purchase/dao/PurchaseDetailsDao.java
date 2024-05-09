/*
 * Author : Avibha IT Solutions Copyright 2006 Avibha IT Solutions. All rights
 * reserved. AVIBHA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * www.avibha.com
 */

package com.avibha.bizcomposer.purchase.dao;

import com.avibha.bizcomposer.employee.dao.Title;
import com.avibha.bizcomposer.purchase.forms.PrintLabelDto;
import com.avibha.bizcomposer.purchase.forms.VendorDto;
import com.avibha.bizcomposer.sales.dao.CustomerInfo;
import com.avibha.bizcomposer.sales.dao.InvoiceInfo;
import com.avibha.bizcomposer.sales.dao.TrHistoryLookUp;
import com.avibha.common.log.Loger;
import com.avibha.common.utility.CountryState;
import com.nxsol.bizcomposer.common.ConstValue;
import org.apache.struts.action.ActionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseDetailsDao {

	@Autowired
	private CountryState cs;

	@Autowired
	private PurchaseInfoDao purchaseInfoDao;

	@Autowired
	private PurchaseInfo purchaseInfo;

	@Autowired
	private InvoiceInfo invoiceInfo;
	@Autowired
	private Title title;

	@Autowired
	private Term term;

	@Autowired
	private VendorCategory vendorCategory;

	@Autowired
	private Rep rep;

	@Autowired
	private PayMethod payMethod;

	@Autowired
	private Shipping shipping;

	@Autowired
	private CreditCard creditCard;

	/*
	 * The method gets the list of all vendor with their ids and names & with their
	 * services.
	 */
	public List<VendorDto> getVendors(HttpServletRequest request) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		String sortByNo = request.getParameter("SortBy");
		String action = request.getParameter("tabid");
		String sortBy = null;
//	//	// PurchaseInfo vendor = new // PurchaseInfo();
		if (sortByNo != null) {
			int sortById = Integer.parseInt(sortByNo);
			if (sortById == 1) {
				sortBy = "Name";
			} else if (sortById == 2) {
				sortBy = "FirstName";
			} else if (sortById == 3) {
				sortBy = "LastName";
			}
		}
		List<VendorDto> VendorDetails = purchaseInfo.getVendorsBySort(compId, sortBy);
		if (action.equalsIgnoreCase("VONODO")) {
//			InvoiceInfo invoiceInfo = new InvoiceInfo();
			List<TrHistoryLookUp> hlookupList = invoiceInfo.searchHistory(request, "ShowAll", "", null, null);
			for (VendorDto cust : VendorDetails) {
				double balance = 0.0;
				long invoiceID = 0;
				for (TrHistoryLookUp hlookup : hlookupList) {
					if (cust.getClientVendorID().equals(hlookup.getClientVendorID())) {
						balance = balance + Double.parseDouble(hlookup.getBalance());
						if (invoiceID == 0 || invoiceID < Integer.parseInt(hlookup.getInvoiceId())) {
							cust.setLastOrderDate(hlookup.getDateAdded());
						}
						invoiceID = Integer.parseInt(hlookup.getInvoiceId());
					}
				}
				cust.setTotalOverdueAmt(balance);
			}
		}
		request.setAttribute("VendorDetails", VendorDetails);
		request.setAttribute("VendorList", VendorDetails);
		// Loger.log("Size of VendorDetails=" + VendorDetails.size());
		return VendorDetails;
	}

	public List<VendorDto> searchVendors(HttpServletRequest request) {
		String compId = (String) request.getSession().getAttribute("CID");
		String venderText = request.getParameter("venderText");
		// // PurchaseInfo vendor = new // PurchaseInfo();
		List<VendorDto> VendorDetails = purchaseInfo.searchVendors(compId, venderText);
		request.setAttribute("VendorDetails", VendorDetails);
		return VendorDetails;
	}

	public void getCountry(HttpServletRequest request) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		// // PurchaseInfo vendor = new // PurchaseInfo();
		List<VendorDto> VendorDetails = new ArrayList<>();
		VendorDetails = purchaseInfo.getVendorsBySort(compId, null);
		request.setAttribute("VendorDetails", VendorDetails);
		Loger.log("Size of VendorDetails=" + VendorDetails.size());
	}

	public void getState(HttpServletRequest request) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		// // PurchaseInfo vendor = new // PurchaseInfo();
		List<VendorDto> VendorDetails = new ArrayList<>();
		VendorDetails = purchaseInfo.getVendorsBySort(compId, null);
		request.setAttribute("VendorDetails", VendorDetails);
		Loger.log("Size of VendorDetails=" + VendorDetails.size());
	}

	public void getTitle(HttpServletRequest request) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		// // PurchaseInfo vendor = new // PurchaseInfo();
		List<VendorDto> VendorDetails = new ArrayList<>();
		VendorDetails = purchaseInfo.getVendorsBySort(compId, null);
		request.setAttribute("VendorDetails", VendorDetails);
		Loger.log("Size of VendorDetails=" + VendorDetails.size());
	}

	/*
	 * Add new vendor with its related information to the database.
	 */
	public void AddVendor(HttpServletRequest request, VendorDto form, String compId) {
		// PurchaseInfoDao purchase = new // PurchaseInfoDao();
		String action = request.getParameter("tabid");
		String actionMsg="Vendor Information is Successfully Added !";
		form.setTaxAble("on".equalsIgnoreCase(request.getParameter("isTaxable")) ? "1" : "0");
		form.setIsclient("on".equalsIgnoreCase(request.getParameter("isAlsoClient")) ? "1" : "3"); // 1:
																									// Customer+Vendor,
																									// 2: Customer,
																									// 3: Vendor
																									// 8:
																									// ServiceProvider

		if (action.equalsIgnoreCase("billingcompany")) 
		{
			form.setIsclient("8"); // making CVTypeID 8 for ServiceProviders as for Billing Company
			actionMsg="Billing Company Information is Successfully Added !";
		}
		form.setFsUseIndividual("on".equalsIgnoreCase(request.getParameter("UseIndividualFinanceCharges")) ? "1" : "0");
		form.setFsAssessFinanceCharge("on".equalsIgnoreCase(request.getParameter("AssessFinanceChk")) ? "1" : "0");
		form.setFsMarkFinanceCharge("on".equalsIgnoreCase(request.getParameter("FChargeInvoiceChk")) ? "1" : "0");
		form.setStateName(request.getParameter("stateName"));
		try {
			boolean isAdded = purchaseInfoDao.insertVendor(form, compId);
			if (isAdded) {
				request.setAttribute("SaveStatus", new ActionMessage(actionMsg));
				request.setAttribute("Added", "true");
				request.getSession().setAttribute("actionMsg", actionMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("Status", new ActionMessage("Information is Not Inserted !"));
		}
	}
	
	public void addBillingVendor(HttpServletRequest request, VendorDto form, String compId) {
		
		try {
			System.out.println("............Purchase Details  ................"+compId);
			
			boolean isAdded = purchaseInfoDao.insertBillingVendor(form, compId);
			if (isAdded) {
				request.setAttribute("SaveStatus", new ActionMessage(" Billing Vendor Information is Successfully Added !"));
				request.setAttribute("Added", "true");
				request.getSession().setAttribute("actionMsg", "Billing Vendor Information is Successfully Added!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("Status", new ActionMessage("Billing Vendor Information is Not Insert !"));
		}
	}
	

	/*
	 * Get all general information related to the vendor.
	 */
	public void getAllList(HttpServletRequest request) {
//		CountryState cs = new CountryState();
		HttpSession sess = request.getSession();
		String cid = (String) sess.getAttribute("CID");
		String countryID = ConstValue.countryID;
		String stateID = ConstValue.stateID;
		String action = request.getParameter("tabid");
		if (action.equalsIgnoreCase("editVendor")) {
			VendorDto customer = (VendorDto) request.getAttribute("vendorDetails1");
			countryID = customer.getCountry();
			stateID = customer.getState();
			request.setAttribute("stateList2", cs.getStateList(customer.getBscountry()));
			request.setAttribute("cityList2", cs.getCityList(customer.getBsstate()));
			request.setAttribute("stateList3", cs.getStateList(customer.getShcountry()));
			request.setAttribute("cityList3", cs.getCityList(customer.getShstate()));
		}
		// country List
		request.setAttribute("cList", cs.getCountry());
		request.setAttribute("countryList", cs.getCountryList());
		request.setAttribute("stateList", cs.getStateList(countryID));
		request.setAttribute("cityList", cs.getCityList(stateID));

		// Title List
//		Title t = new Title();
		request.setAttribute("titleList", title.getTitleList(cid));

		// Term List
//		Term tr = new Term();
		request.setAttribute("TermList", term.getTermList(cid));

		// Rep List
//		Rep rap = new Rep();
		request.setAttribute("RepList", rep.getRepList(cid));

		// PayMethod List
//		PayMethod pmethod = new PayMethod();

		request.setAttribute("PaymentList", payMethod.getPaymentTypeList(cid));

		// ShipCarrier List
//		Shipping ship = new Shipping();
		request.setAttribute("ShipCarrierList", shipping.getShipCarrierList(cid));

		// CreditCard List
//		CreditCard cc = new CreditCard();
		request.setAttribute("CreditCardList", creditCard.getCCTypeList(cid));

		// CreditCard List
//		VendorCategory cv = new VendorCategory();
		request.setAttribute("VendorCategoryList", vendorCategory.getCVCategoryList(cid));

	}

	/*
	 * Search the vendor from his id provided. And provide his related information.
	 */
	public void searchVendor(String cvId, HttpServletRequest request, VendorDto form) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		// PurchaseInfoDao purchase = new // PurchaseInfoDao();
		purchaseInfoDao.SearchVendor(compId, cvId, request, form);
		purchaseInfoDao.getServices(request, compId, cvId);
		// request.setAttribute("CustomerDetails",CustomerDetails);
	}

	/*
	 * Update the existing vendor & his related information.
	 */
	public void UpdateVendor(HttpServletRequest request, VendorDto form) {
		HttpSession sess = request.getSession();
		String compId = (String) sess.getAttribute("CID");
		// PurchaseInfo purchase =new // PurchaseInfo();
//		CustomerInfo customer = new CustomerInfo();
		String cvId = form.getClientVendorID(); // (String) sess.getAttribute("editedCVID");

		// customer.UpdateCustomer(compId, cvId);

		String istaxable = request.getParameter("isTaxable");
		String isAlsoClient = request.getParameter("isAlsoClient");
		String UseIndividualFinanceCharges = request.getParameter("UseIndividualFinanceCharges");
		String AssessFinanceChk = request.getParameter("AssessFinanceChk");
		String FChargeInvoiceChk = request.getParameter("FChargeInvoiceChk");

		int istax = (istaxable != null && istaxable.equalsIgnoreCase("on")) ? 1 : 0;
		int isclient = (isAlsoClient != null && isAlsoClient.equalsIgnoreCase("on")) ? 1 : 3; // 1: Customer+Vendor, 2:
																								// Customer, 3: Vendor
		int indCharge = (UseIndividualFinanceCharges != null && UseIndividualFinanceCharges.equalsIgnoreCase("on")) ? 1
				: 0;
		int aFCharge = (AssessFinanceChk != null && AssessFinanceChk.equalsIgnoreCase("on")) ? 1 : 0;
		int fICharge = (FChargeInvoiceChk != null && FChargeInvoiceChk.equalsIgnoreCase("on")) ? 1 : 0;

		form.setAnnualIntrestRate(request.getParameter("AnualRate"));
		form.setMinFCharges(request.getParameter("MinFinance"));
		form.setGracePrd(request.getParameter("GracePeriod"));
		boolean updated = purchaseInfo.updateInsertVendor(cvId, form, compId, istax, isclient, indCharge, aFCharge,
				fICharge, "U");

		if (updated) {
			request.setAttribute("SaveStatus", new ActionMessage("Vendor information is successfully updated."));
			request.getSession().setAttribute("actionMsg", "Vendor Information is Successfully updated!");
		} else {
			request.setAttribute("SaveStatus", new ActionMessage("Vendor information is not updated."));
		}

		if (form.getDispay_info() == null) {
			request.setAttribute("RadioVal", "1");
		} else if (form.getDispay_info().equals("ShowAll")) {
			request.setAttribute("RadioVal", "1");
		} else {
			request.setAttribute("RadioVal", "2");
		}
		if (request.getParameter("Flag").equals("1")) {
			request.setAttribute("ClientID", cvId);
		}
	}

	/*
	 * Provides the information required for the print label such as vendor list
	 * with his/her services,total no. of pages, etc.
	 * 
	 */
	public void getPrintLabelInfo(HttpServletRequest request, PrintLabelDto pForm) {

//		// PurchaseInfoDao purchase = new // PurchaseInfoDao();
		String compId = (String) request.getSession().getAttribute("CID");
		Pagination page = new Pagination();
		int total = page.getPages(Long.parseLong(compId));
		int start = pForm.getStartPage();

		int limit = 5; // Limit to no. of records display
		int pageCount = 3; // No. of pages to display on page

		request.setAttribute("PrintList", purchaseInfoDao.getPrintLabelInfo(request, compId, start, limit));
		pForm.setTotalPages(total);
		request.setAttribute("PageValue", String.valueOf(start));
		int[] pages = new int[pageCount];
		int count = 0;
		if (start == total) {
			start--;
			for (int cnt = 0; cnt < pageCount; cnt++) {
				pages[cnt] = start;
				start++;
				count++;
				if (start > total) {
					break;
				}
			}
		} else if (start < total) {
			for (int cnt = 0; cnt < pageCount; cnt++) {
				pages[cnt] = start;
				start++;
				count++;
				if (start > total) {
					break;
				}
			}
		}

		int[] record = new int[count];
		for (int cnt = 0; cnt < count; cnt++) {
			record[cnt] = pages[cnt];
		}

		request.setAttribute("TotalPages", String.valueOf(pForm.getTotalPages()));
		request.setAttribute("Total", record);

		if (request.getParameter("VendorName") != null) {
			request.setAttribute("VendorList", request.getParameter("VendorName"));
		}

	}

	/*
	 * Provides the information related the label of the the given label id.
	 */
	public void getLabel(HttpServletRequest request, PrintLabelDto vform) {
		// PurchaseInfoDao customer = new // PurchaseInfoDao();
		int labelId = Integer.parseInt(request.getParameter("lblId"));
		purchaseInfoDao.getLabel(labelId, vform);
	}

	/*
	 * Saves or updates the label & related information.
	 * 
	 */
	public boolean saveLabel(HttpServletRequest request, PrintLabelDto cfrm) {
		boolean result = false;
		// PurchaseInfoDao purchase = new // PurchaseInfoDao();
		int labelID = Integer.parseInt(request.getParameter("LabelID"));
		if (labelID == 0) {
			purchaseInfoDao.saveLabel(cfrm);
			result = true;
		} else {
			purchaseInfoDao.updateLabel(labelID, cfrm);
			result = false;
		}
		return result;
	}

	public void addNewLabel(PrintLabelDto cform) {
		cform.setLabelName("");
		cform.setTopMargin("0.0");
		cform.setLeftMargin("0.0");
		cform.setHorizon("0.0");
		cform.setVertical("0.0");
		cform.setLabelHeight("0.0");
		cform.setLabelWidth("0.0");
	}

	public void deleteLabel(HttpServletRequest request, PrintLabelDto form) {
		// PurchaseInfoDao purchase = new // PurchaseInfoDao();
		int labelID = Integer.parseInt(request.getParameter("LabelID"));
		Loger.log("LABEL   " + labelID);
		purchaseInfoDao.deleteLabel(labelID, form);
	}
}
