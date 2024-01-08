/*
 * Author : Avibha IT Solutions Copyright 2007 Avibha IT Solutions. All rights
 * reserved. AVIBHA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * www.avibha.com
 */
package com.avibha.bizcomposer.sales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avibha.bizcomposer.employee.dao.Title;
import com.avibha.bizcomposer.purchase.dao.PurchaseInfo;
import com.avibha.bizcomposer.purchase.dao.VendorCategory;
import com.avibha.bizcomposer.sales.forms.CustomerDto;
import com.avibha.bizcomposer.sales.forms.EstimationDto;
import com.avibha.bizcomposer.sales.forms.UpdateInvoiceDto;
import com.avibha.common.db.SQLExecutor;
import com.avibha.common.log.Loger;
import com.avibha.common.utility.CountryState;
import com.avibha.common.utility.DateInfo;
import com.nxsol.bizcomposer.common.ConstValue;
import com.nxsol.bizcomposer.common.JProjectUtil;
import com.nxsol.bizcomposer.global.clientvendor.ClientVendor;
import com.nxsol.bzcomposer.company.domain.BcaAccount;
import com.nxsol.bzcomposer.company.domain.BcaClientvendor;
import com.nxsol.bzcomposer.company.domain.BcaClientvendorservice;
import com.nxsol.bzcomposer.company.domain.BcaCompany;
import com.nxsol.bzcomposer.company.domain.BcaCreditcardtype;
import com.nxsol.bzcomposer.company.domain.BcaCvcreditcard;
import com.nxsol.bzcomposer.company.domain.BcaCvtype;
import com.nxsol.bzcomposer.company.domain.BcaInvoicestyle;
import com.nxsol.bzcomposer.company.domain.BcaLabel;
import com.nxsol.bzcomposer.company.domain.BcaPaymenttype;
import com.nxsol.bzcomposer.company.domain.BcaSalesrep;
import com.nxsol.bzcomposer.company.domain.BcaSalestax;
import com.nxsol.bzcomposer.company.domain.BcaServicetype;
import com.nxsol.bzcomposer.company.domain.BcaShipcarrier;
import com.nxsol.bzcomposer.company.domain.BcaTerm;
import com.nxsol.bzcomposer.company.domain.SmdCvinfo;
import com.nxsol.bzcomposer.company.domain.StorageClientvendor;
import com.nxsol.bzcomposer.company.repos.BcaClientvendorRepository;
import com.nxsol.bzcomposer.company.repos.BcaCompanyRepository;
import com.nxsol.bzcomposer.company.repos.BcaCreditcardtypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaCvcreditcardRepository;
import com.nxsol.bzcomposer.company.repos.BcaCvtypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaInvoicestyleRepository;
import com.nxsol.bzcomposer.company.repos.BcaIteminventoryRepository;
import com.nxsol.bzcomposer.company.repos.BcaLabelRepository;
import com.nxsol.bzcomposer.company.repos.BcaPaymenttypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaSalesrepRepository;
import com.nxsol.bzcomposer.company.repos.BcaSalestaxRepository;
import com.nxsol.bzcomposer.company.repos.BcaServicetypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaShipcarrierRepository;
import com.nxsol.bzcomposer.company.repos.BcaTermRepository;
import com.nxsol.bzcomposer.company.repos.SmdCvinfoRepository;
import com.nxsol.bzcomposer.company.repos.StorageClientvendorRepository;
import com.nxsol.bzcomposer.company.service.BcaClientvendorService;
import com.nxsol.bzcomposer.company.utils.DateHelper;
import com.nxsol.bzcomposer.company.utils.JpaHelper;
import com.pritesh.bizcomposer.accounting.bean.TblAccount;
import com.pritesh.bizcomposer.accounting.bean.TblBSAddress2;

/*
 * 
 */
@Service
public class CustomerInfoDao {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private BcaLabelRepository bcaLabelRepository;

	@Autowired
	private BcaClientvendorRepository bcaClientvendorRepository;

	@Autowired
	private BcaIteminventoryRepository bcaIteminventoryRepository;

	@Autowired
	private BcaServicetypeRepository bcaServicetypeRepository;

	@Autowired
	private BcaInvoicestyleRepository bcaInvoicestyleRepository;

	@Autowired
	private BcaCompanyRepository bcaCompanyRepository;

	@Autowired
	private BcaTermRepository bcaTermRepository;

	@Autowired
	private BcaShipcarrierRepository bcaShipcarrierRepository;

	@Autowired
	private BcaSalesrepRepository bcaSalesrepRepository;

	@Autowired
	private BcaPaymenttypeRepository bcaPaymenttypeRepository;

	@Autowired
	private BcaSalestaxRepository bcaSalestaxRepository;

	@Autowired
	private BcaCreditcardtypeRepository bcaCreditcardtypeRepository;

	@Autowired
	private BcaCvtypeRepository bcaCvtypeRepository;

	@Autowired
	private StorageClientvendorRepository storageClientvendorRepository;

	@Autowired
	private CountryState countryState;
	@Autowired
	private VendorCategory vendorCategory;

	@Autowired
	private PurchaseInfo purchaseInfo;

	@Autowired
	private SmdCvinfoRepository smdCvinfoRepository;

	public ArrayList<CustomerDto> customerDetails(String compId) {
		ArrayList<CustomerDto> objList = new ArrayList<>();
		Title title = new Title();
		try {
			@SuppressWarnings("unchecked")
			ArrayList<LabelValueBean> titleList = title.getTitleList(compId);

//			String sqlString = "SELECT distinct c.ClientVendorID,c.Name,c.CustomerTitle,c.FirstName,c.LastName,c.Address1,c.Address2,c.City,c.State,c.ZipCode,c.Country,"
//					+ "c.Email,c.Phone,c.CellPhone,c.Fax,date_format(c.DateAdded,'%m-%d-%Y') as DateAdded,i.IsPaymentCompleted,c.CVCategoryName,"
//					+ "ct.Name AS CityName, st.Name AS StateName, cn.Name AS CountryName, c.DBAName "
//					+ "FROM bca_clientvendor AS c LEFT JOIN bca_countries as cn ON cn.ID=c.Country LEFT JOIN bca_states as st ON st.ID=c.State LEFT JOIN bca_cities as ct ON ct.ID=c.City "
//					+ "LEFT JOIN bca_invoice as i ON i.ClientVendorID=c.ClientVendorID AND NOT (i.invoiceStatus=1) AND i.IsPaymentCompleted = 0 AND i.InvoiceTypeID IN (1,13,17) "
//					+ "WHERE c.CompanyID = " + compId
//					+ " AND CVTypeID IN (1, 2) AND c.Status IN ('U', 'N') AND c.Deleted = 0 AND c.Active=1 ORDER BY c.Name";

//			String query = " select distinct c.clientVendorId , c.name ,c.customerTitle, c.firstName , c.lastName , c.address1, c.address2, c.city, c.state , c.zipCode , c.country , "
//					+ "c.email , c.phone , c.cellPhone , c.fax ,date_format(c.dateAdded , '%m-%d-%Y') as dateAdded , i.isPaymentCompleted , c.cvcategoryName, ct.name as cityName , st.name as stateName , cn.name as countryName "
//					+ ", c.DBAName  from BcaClientvendor as c left join BcaCountries as cn on cn.Id =c.country left join BcaStates as st on st.Id =c.state left join BcaCities as ct on ct.Id = c.city"
//					+ " left join BcaInvoice as i on i.clientVendor.clientVendorId = c.clientVendorId and not (i.invoiceStatus =1) and i.isPaymentCompleted = 0 and i.invoiceType.invoiceTypeId in (1,13,17) "
//					+ " where c.company.companyId =  :companyId and c.cvtypeId in (1,2) and c.status in ('U' , 'N' ) and c.deleted = 0 and c.active =1 order by c.name ";

			List<String> status = Arrays.asList("U", "N");
			List<Integer> cvTypeId = Arrays.asList(1, 2);

			List<BcaClientvendor> cvs = bcaClientvendorRepository
					.findDistinctByCompany_CompanyIdAndStatusInAndCvtypeIdInAndDeletedAndActiveOrderByName(
							Long.valueOf(compId), status, cvTypeId, 0, 1);

			DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

			for (BcaClientvendor cv : cvs) {
				CustomerDto customer = new CustomerDto();
				customer.setClientVendorID(cv.getClientVendorId().toString());
				customer.setCompanyName(cv.getName());
				customer.setCname(cv.getName() + "(" + cv.getFirstName() + " " + cv.getLastName() + ")");
				customer.setTitle(cv.getCustomerTitle());
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setZipCode(cv.getZipCode());
				customer.setCity(cv.getCity());
				customer.setStateName(cv.getState());
				String countryName = cv.getCountry();
				if (countryName != null && countryName.contains("United States")) {
					countryName = "USA";
				}
				customer.setCountry(countryName);
				customer.setEmail(cv.getEmail());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setDateAdded(cv.getDateAdded() != null ? outputFormat.format(cv.getDateAdded()) : "");
//				customer.setDateAdded(outputFormat.format(cv.getDateAdded()));
				String fullName= cv.getFirstName() + " " + cv.getLastName();
				customer.setFullName(fullName);
				customer.setBillTo(fullName);
				
//				boolean paymentUnpaid = (cv.getString("IsPaymentCompleted") != null
//						&& cv.getString("IsPaymentCompleted").equals("0")) ? true : false;
//				customer.setPaymentUnpaid(paymentUnpaid);
				
				customer.setType(cv.getCvcategoryName());
				customer.setDbaName(cv.getDbaname());
				for (LabelValueBean lvBean : titleList) {
					if (lvBean.getValue().equalsIgnoreCase(customer.getTitle())) {
						customer.setTitle(lvBean.getLabel());
						break;
					}
				}
				objList.add(customer);
			}

//			List<CustomerDto> listOfCustomer = bcaClientvendorRepository.findCustomerInfo(new Long(compId));
//			for (CustomerDto customer : listOfCustomer) {
//				for (LabelValueBean lvBean : titleList) {
//					if (lvBean.getValue().equalsIgnoreCase(customer.getTitle())) {
//						customer.setTitle(lvBean.getLabel());
//						break;
//					}
//				}
//				objList.add(customer);
//			}

//			pstmt = con.prepareStatement(sqlString);
			// Loger.log(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CustomerDto customer = new CustomerDto();
//				customer.setClientVendorID(rs.getString("ClientVendorID"));
//				customer.setCompanyName(rs.getString("Name"));
//				customer.setCname(
//						rs.getString("Name") + "(" + rs.getString("FirstName") + " " + rs.getString("LastName") + ")");
//				customer.setTitle(rs.getString("CustomerTitle"));
//				customer.setFirstName(rs.getString("FirstName"));
//				customer.setLastName(rs.getString("LastName"));
//				customer.setAddress1(rs.getString("Address1"));
//				customer.setAddress2(rs.getString("Address2"));
//				customer.setZipCode(rs.getString("ZipCode"));
//				customer.setCity(rs.getString("CityName") != null ? rs.getString("CityName") : rs.getString("City"));
//				customer.setStateName(
//						rs.getString("StateName") != null ? rs.getString("StateName") : rs.getString("State"));
//				String countryName = rs.getString("CountryName") != null ? rs.getString("CountryName")
//						: rs.getString("Country");
//				if (countryName != null && countryName.contains("United States")) {
//					countryName = "USA";
//				}
//				customer.setCountry(countryName);
//				customer.setEmail(rs.getString("Email"));
//				customer.setPhone(rs.getString("Phone"));
//				customer.setCellPhone(rs.getString("CellPhone"));
//				customer.setFax(rs.getString("Fax"));
//				customer.setDateAdded(rs.getString("DateAdded"));
//
//				customer.setFullName(rs.getString("FirstName") + " " + rs.getString("LastName"));
//				customer.setBillTo(rs.getString("FirstName") + rs.getString("LastName"));
//				boolean paymentUnpaid = (rs.getString("IsPaymentCompleted") != null
//						&& rs.getString("IsPaymentCompleted").equals("0")) ? true : false;
//				customer.setPaymentUnpaid(paymentUnpaid);
//				customer.setType(rs.getString("CVCategoryName"));
//				customer.setDbaName(rs.getString("DBAName"));
//				for (LabelValueBean lvBean : titleList) {
//					if (lvBean.getValue().equalsIgnoreCase(customer.getTitle())) {
//						customer.setTitle(lvBean.getLabel());
//						break;
//					}
//				}
//				objList.add(customer);
//			}
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class CustomerInfo and  method -customerDetails " + " " + ee.toString());

		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return objList;
	}

	public ArrayList<CustomerDto> customerDetailsDELETE(String compId) {

//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
//		ResultSet rs = null;
//		CountryState cs = new CountryState();
		Title title = new Title();
		try {
//			con = db.getConnection();
			@SuppressWarnings("unchecked")
			ArrayList<LabelValueBean> titleList = title.getTitleList(compId);

//			String sqlString = "SELECT distinct c.ClientVendorID,c.Name,c.CustomerTitle,c.FirstName,c.LastName,c.Address1,c.Address2,c.City,c.State,c.ZipCode,c.Country,"
//					+ "c.Email,c.Phone,c.CellPhone,c.Fax,date_format(c.DateAdded,'%m-%d-%Y') as DateAdded,i.IsPaymentCompleted,c.CVCategoryName,"
//					+ "ct.Name AS CityName, st.Name AS StateName, cn.Name AS CountryName, c.DBAName "
//					+ "FROM bca_clientvendor AS c LEFT JOIN bca_countries as cn ON cn.ID=c.Country LEFT JOIN bca_states as st ON st.ID=c.State LEFT JOIN bca_cities as ct ON ct.ID=c.City "
//					+ "LEFT JOIN bca_invoice as i ON i.ClientVendorID=c.ClientVendorID AND NOT (i.invoiceStatus=1) AND i.IsPaymentCompleted = 0 AND i.InvoiceTypeID IN (1,13,17) "
//					+ "WHERE c.CompanyID = " + compId
//					+ " AND CVTypeID IN (1, 2) AND c.Status IN ('U', 'N') AND c.Deleted = 0 AND c.Active=1 ORDER BY c.Name";

//			String query = " select distinct c.clientVendorId , c.name ,c.customerTitle, c.firstName , c.lastName , c.address1, c.address2, c.city, c.state , c.zipCode , c.country , "
//					+ "c.email , c.phone , c.cellPhone , c.fax ,date_format(c.dateAdded , '%m-%d-%Y') as dateAdded , i.isPaymentCompleted , c.cvcategoryName, ct.name as cityName , st.name as stateName , cn.name as countryName "
//					+ ", c.DBAName  from BcaClientvendor as c left join BcaCountries as cn on cn.Id =c.country left join BcaStates as st on st.Id =c.state left join BcaCities as ct on ct.Id = c.city"
//					+ " left join BcaInvoice as i on i.clientVendor.clientVendorId = c.clientVendorId and not (i.invoiceStatus =1) and i.isPaymentCompleted = 0 and i.invoiceType.invoiceTypeId in (1,13,17) "
//					+ " where c.company.companyId =  :companyId and c.cvtypeId in (1,2) and c.status in ('U' , 'N' ) and c.deleted = 0 and c.active =1 order by c.name ";
//			
//			TypedQuery<?> typedQuery = (TypedQuery<?>) this.entityManager.createQuery(query);
//			JpaHelper.addParameter(typedQuery, query, "companyId", new Long(compId));
//			List<?> list=typedQuery.getResultList();
//			int size =list.size();
//			

			List<CustomerDto> listOfCustomer = bcaClientvendorRepository.findCustomerInfo(new Long(compId));
			for (CustomerDto customer : listOfCustomer) {
				for (LabelValueBean lvBean : titleList) {
					if (lvBean.getValue().equalsIgnoreCase(customer.getTitle())) {
						customer.setTitle(lvBean.getLabel());
						break;
					}
				}
				objList.add(customer);
			}

//			pstmt = con.prepareStatement(sqlString);
			// Loger.log(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CustomerDto customer = new CustomerDto();
//				customer.setClientVendorID(rs.getString("ClientVendorID"));
//				customer.setCompanyName(rs.getString("Name"));
//				customer.setCname(
//						rs.getString("Name") + "(" + rs.getString("FirstName") + " " + rs.getString("LastName") + ")");
//				customer.setTitle(rs.getString("CustomerTitle"));
//				customer.setFirstName(rs.getString("FirstName"));
//				customer.setLastName(rs.getString("LastName"));
//				customer.setAddress1(rs.getString("Address1"));
//				customer.setAddress2(rs.getString("Address2"));
//				customer.setZipCode(rs.getString("ZipCode"));
//				customer.setCity(rs.getString("CityName") != null ? rs.getString("CityName") : rs.getString("City"));
//				customer.setStateName(
//						rs.getString("StateName") != null ? rs.getString("StateName") : rs.getString("State"));
//				String countryName = rs.getString("CountryName") != null ? rs.getString("CountryName")
//						: rs.getString("Country");
//				if (countryName != null && countryName.contains("United States")) {
//					countryName = "USA";
//				}
//				customer.setCountry(countryName);
//				customer.setEmail(rs.getString("Email"));
//				customer.setPhone(rs.getString("Phone"));
//				customer.setCellPhone(rs.getString("CellPhone"));
//				customer.setFax(rs.getString("Fax"));
//				customer.setDateAdded(rs.getString("DateAdded"));
//
//				customer.setFullName(rs.getString("FirstName") + " " + rs.getString("LastName"));
//				customer.setBillTo(rs.getString("FirstName") + rs.getString("LastName"));
//				boolean paymentUnpaid = (rs.getString("IsPaymentCompleted") != null
//						&& rs.getString("IsPaymentCompleted").equals("0")) ? true : false;
//				customer.setPaymentUnpaid(paymentUnpaid);
//				customer.setType(rs.getString("CVCategoryName"));
//				customer.setDbaName(rs.getString("DBAName"));
//				for (LabelValueBean lvBean : titleList) {
//					if (lvBean.getValue().equalsIgnoreCase(customer.getTitle())) {
//						customer.setTitle(lvBean.getLabel());
//						break;
//					}
//				}
//				objList.add(customer);
//			}
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class CustomerInfo and  method -customerDetails " + " " + ee.toString());

		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return objList;
	}

	public ArrayList getTransactionList(String datesCombo, String fromDate, String toDate, String sortBy,
			String companyID, HttpServletRequest request, CustomerDto cForm) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		con = db.getConnection();
		ArrayList<CustomerDto> objList = new ArrayList<>();
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		CustomerInfoDao cInfo = new CustomerInfoDao();
		DateInfo dInfo = new DateInfo();
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					cForm.setFromDate(cInfo.date2String(selectedRange.get(0)));
					cForm.setToDate(cInfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND cv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND cv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate))
						+ "')";
			}
		}
		try {

			String sql = "" + "SELECT cv.NAME, " + "       cv.firstname, " + "       cv.lastname, "
					+ "       date_format(cv.DateAdded,'%m-%d-%Y') as DateAdded, " + "       cv.detail AS Details, "
					+ "       cv.customeropendebit, " + "       cv.clientvendorid, " + "       inv.dateadded, "
					+ "       inv.ordernum, " + "       inv.memo, " + "       inv.total "
					+ " FROM   bca_clientvendor AS cv " + "       INNER JOIN bca_invoice AS inv "
					+ "               ON cv.clientvendorid = inv.clientvendorid " + " WHERE  cvtypeid IN ( 1, 2 ) "
					+ "       AND ( status = 'U' " + "              OR status = 'N' ) "
					+ "       AND inv.invoicetypeid NOT IN ( 10, 15, 7 ) " + "       AND deleted = 0 "
					+ "       AND cv.companyid = '" + companyID + "'" + dateBetween + " ORDER  BY cv.dateadded, "
					+ "          cv.NAME";

			pstmt = con.prepareStatement(sql);
			Loger.log(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CustomerDto iForm = new CustomerDto();
				iForm.setCname(rs.getString(1));
				iForm.setDate(rs.getString(4));
				iForm.setOrderNo(rs.getString(9));
				iForm.setMemo(rs.getString(10));
				iForm.setTotal(rs.getDouble(11));
				objList.add(iForm);

			}

		} catch (Exception e) {
			Loger.log(2, "SQL error in getTransactionList" + " " + e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return objList;
	}

	public ArrayList getBalanceSummaryList(String datesCombo, String fromDate, String toDate, String sortBy,
			String companyID, HttpServletRequest request, CustomerDto cForm) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		con = db.getConnection();
		StringBuffer sb = new StringBuffer();
		ArrayList<CustomerDto> objList = new ArrayList();
		ArrayList<Date> selectedRange = new ArrayList<>();
		CustomerInfoDao cInfo = new CustomerInfoDao();
		DateInfo dInfo = new DateInfo();
		String dateBetween = "";
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					cForm.setFromDate(cInfo.date2String(selectedRange.get(0)));
					cForm.setToDate(cInfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND cv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND cv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate))
						+ "')";
			}
		}
		sb.append("SELECT " + " cv.Name," + " cv.FirstName," + // by ss
				" cv.LastName," + // by ss
				" cv.ClientVendorID" + " FROM " + " bca_clientvendor AS cv " + " WHERE "
				+ " (cv.Status = 'U' OR cv.Status = 'N') AND cv.Deleted = 0  " +
				// " AND cv.CVTypeID IN (1,2) " +
				" AND CVTypeID IN (" + getCustomerTypeId(ConstValue.CUSTOMER) + ","
				+ getCustomerTypeId(ConstValue.DEALER) + "," + getCustomerTypeId(ConstValue.CustVenBoth) + ","
				+ getCustomerTypeId(ConstValue.DealerVenBoth) + ")" + " AND cv.CompanyID = '" + companyID + "'");
		sb.append(dateBetween);
		sb.append(" ORDER By cv.Name");

		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CustomerDto form = new CustomerDto();
				form.setFullName(rs.getString(2) + " " + rs.getString(3) + "(" + rs.getString(1) + ")");
				form.setTotal(Double.parseDouble(getBalance(rs.getInt(4), Integer.parseInt(companyID))));

				objList.add(form);
			}

		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return objList;
	}

	public ArrayList getBalanceDetail(String datesCombo, String fromDate, String toDate, String sortBy,
			String companyID, HttpServletRequest request, CustomerDto cForm) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		ArrayList<CustomerDto> form = new ArrayList<>();
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		CustomerInfoDao cInfo = new CustomerInfoDao();
		DateInfo dInfo = new DateInfo();
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					cForm.setFromDate(cInfo.date2String(selectedRange.get(0)));
					cForm.setToDate(cInfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND cv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND cv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate))
						+ "')";
			}
		}

		sb.append(
				"SELECT cv.ClientVendorID,cv.Name,cv.FirstName,cv.LastName,date_format(cv.dateadded,'%m-%d-%Y') AS Dateadded, "
						+ "cv.CustomerOpenDebit, cv.ClientVendorID,"
						+ " inv.InvoiceID,inv.DateAdded, inv.OrderNum, inv.AdjustedTotal, inv.Balance,inv.InvoiceTypeID"
						+ " FROM bca_clientvendor AS cv INNER JOIN bca_invoice AS inv"
						+ " ON cv.ClientVendorID = inv.ClientVendorID" + " WHERE CVTypeID IN (1,2,4,5) "
						+ " AND (Status = 'U' OR Status = 'N')  " + " AND Deleted = 0 "
						+ " AND InvoiceTypeID IN (1,11) " + " AND inv.invoiceStatus IN (0,2) " + " AND inv.CompanyID="
						+ companyID);
		sb.append(dateBetween);
		sb.append(" ORDER BY cv.dateadded DESC, cv.Name ");

		con = db.getConnection();
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CustomerDto f = new CustomerDto();
				f.setCname(rs.getString(2));
				f.setDateAdded(rs.getString(5));
				f.setOrderNo(rs.getString(10));
				f.setTotal(rs.getDouble(11));
				f.setBalance(rs.getDouble(12));
				form.add(f);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return form;
	}

	public String getBalance(int cvId, int companyId) {
		String balance = "";
		StringBuffer sb = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rst = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = null;
		con = db.getConnection();

		sb.append("SELECT SUM(cv.Balance) FROM " + " bca_invoice AS cv" + " WHERE cv.CompanyID = " + companyId
				+ " AND cv.ClientVendorID=" + cvId + " AND InvoiceTypeID IN(1,11,17,13) AND invoiceStatus NOT IN(1,2)");

		try {
			ps = con.prepareStatement(sb.toString());
			rst = ps.executeQuery();

			while (rst.next()) {
				balance = new DecimalFormat("#0.00").format(rst.getDouble(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Loger.log(e.toString());
		} finally {
			try {
				if (rst != null) {
					db.close(rst);
				}
				if (ps != null) {
					db.close(ps);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return balance;
	}

	public int getCustomerTypeId(String customerType) {
		int cvTypeId = 0;
//		Statement stmt = null;
//		ResultSet rst = null;
//		SQLExecutor db = new SQLExecutor();
//		Connection con = null;
//		con = db.getConnection();

//		try {
//			String strSql = "SELECT CVTypeID from bca_cvtype " + "WHERE name='" + customerType + "'";
//			stmt = con.createStatement();
//			rst = stmt.executeQuery(strSql);
		List<Integer> CVTypeIDs = bcaCvtypeRepository.findByName(customerType);
//			if (rst.next()) {
//				cvTypeId = rst.getInt("CVTypeID");
//			}

		if (CVTypeIDs.size() > 0) {
			cvTypeId = CVTypeIDs.get(0);
		}

//		} catch (Exception e) {
//
//			Loger.log(e.toString());
//		} finally {
//			try {
//				if (rst != null) {
//					db.close(rst);
//				}
//				if (stmt != null) {
//					db.close(stmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return cvTypeId;
	}

	public ArrayList getSalesByCustomerSummary(String datesCombo, String fromDate, String toDate, String sortBy,
			String companyID, HttpServletRequest request, CustomerDto cForm) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		ArrayList<CustomerDto> objList = new ArrayList<>();
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		CustomerInfoDao cInfo = new CustomerInfoDao();
		DateInfo dInfo = new DateInfo();
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					cForm.setFromDate(cInfo.date2String(selectedRange.get(0)));
					cForm.setToDate(cInfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND cv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND cv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate))
						+ "')";
			}
		}

		sb.append("SELECT cv.ClientVendorID, cv.Name,cv.FirstName,cv.LastName FROM ");
		sb.append("bca_clientvendor AS cv ");
		sb.append("WHERE cv.ClientVendorID IN (SELECT ClientVendorID FROM bca_invoice)");
		sb.append(" AND (cv.Status = 'U' OR cv.Status = 'N') " + " AND cv.Deleted = 0 "
				+ " AND cv.CVTypeID IN (1,2,4,5) " + " AND cv.CompanyID = '" + companyID + "'");
		sb.append(dateBetween);
		sb.append(" ORDER BY cv.NAME");
		Loger.log(sb.toString());
		try {
			con = db.getConnection();
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CustomerDto f = new CustomerDto();
				f.setCname(rs.getString(2));
				f.setTotal(getBalanceForSalesCustomerSummary(rs.getInt(1), companyID));
				objList.add(f);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return objList;
	}

	public double getBalanceForSalesCustomerSummary(int cvId, String compId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		double bal = 0.00;

		String sql = "SELECT SUM(inv.Balance) AS Bal " + "FROM bca_invoice AS inv "
				+ "WHERE NOT (inv.invoiceStatus = 1) " + "AND inv.ClientVendorID = " + cvId + " "
				+ "AND inv.InvoiceTypeID NOT IN (15,7) " + "AND inv.CompanyID = '" + compId + "'";
		try {
			con = db.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bal = rs.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return bal;
	}

	public ArrayList getIncomeByCustomerSymmary(String datesCombo, String fromDate, String toDate, String sortBy,
			String companyID, HttpServletRequest request, CustomerDto cForm) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		ArrayList<CustomerDto> objlist = new ArrayList<>();
		Long cvId = 0L;
		double balance = 0.00;
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		CustomerInfoDao cInfo = new CustomerInfoDao();
		DateInfo dInfo = new DateInfo();
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					cForm.setFromDate(cInfo.date2String(selectedRange.get(0)));
					cForm.setToDate(cInfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND cv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND cv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND cv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cInfo.string2date(toDate))
						+ "')";
			}
		}
		String sql = "SELECT cv.ClientVendorID, CustomerOpenDebit, " + " cv.DateAdded as cvDateAdded"
				+ " FROM bca_clientvendor AS cv" + " WHERE Status IN ('U','N')" + " AND CompanyID = '" + companyID + "'"
				+ " AND CVTypeID IN (1,2,4,5)" + dateBetween + " ORDER BY Name";
		try {
			con = db.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Loger.log(sql);
			while (rs.next()) {
				CustomerDto f = new CustomerDto();
				cvId = rs.getLong(1);
				ClientVendor cv = getCv(cvId, companyID);
				f.setCname(cv.getName().equals("") ? cv.getFirstName() + " " + cv.getLastName() : cv.getName());
				double amount = 0.0;
				amount = rs.getDouble(2);
				balance = amount;
				Date date = rs.getDate(3);
				balance += getInvoiceAmount(cvId, companyID);
				balance = balance - getCostOfGoodsSold(cvId, companyID);
				balance = balance + getCustomerOpeningBal(cvId, companyID);
				if (balance >= 0) {
					f.setBalance(balance);
				} else {
					f.setBalance(0.00);
				}
				objlist.add(f);
			}
		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return objlist;
	}

	public double getCustomerOpeningBal(long cvId, String compId) {
		Statement stmt = null;
		ResultSet rs = null;
		double amt = 0.0;
		SQLExecutor db = new SQLExecutor();
		Connection con = null;

		String sql = "SELECT CustomerOpenDebit as total " + " FROM bca_clientvendor" + " WHERE Status IN ('U','N')"
				+ " AND CompanyID = '" + compId + "'" + " AND ClientVendorID = " + cvId + " AND CVTypeID IN (1,2,4,5)";

		try {
			con = db.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				amt = rs.getDouble(1);
			}

		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return amt;
	}

	public double getCostOfGoodsSold(long cvId, String compId) {
		Statement stmt = null;
		double amount = 0.0;
		String sql = "";
		Connection con = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;

		sql = "SELECT  (cart.Qty * inventory.PurchasePrice) as total "
				+ " FROM (bca_cart AS cart INNER JOIN bca_invoice AS inv ON cart.InvoiceID = inv.InvoiceID) "
				+ " INNER JOIN bca_iteminventory AS inventory ON cart.InventoryID = inventory.InventoryID "
				+ " WHERE  inv.InvoiceTypeID IN (1,11) " + " AND inv.invoiceStatus=0 " + " AND inv.CompanyID='" + compId
				+ "'" + " AND inv.ClientVendorID=" + cvId;
		try {
			con = db.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				amount += rs.getDouble(1);
			}
		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return amount;
	}

	public double getInvoiceAmount(long cvId, String compId) {
		Statement stmt = null, stmt1 = null;
		SQLExecutor db = new SQLExecutor();
		double amount = 0.0;
		double UpFrontamount = 0.0;
		String sql = "";
		String Sql = "";
		ResultSet rs = null;
		Connection con = null;

		sql = " SELECT SUM(AdjustedTotal) " + " FROM bca_invoice  " + " WHERE InvoiceTypeID IN (1,11) "
				+ " AND CompanyID='" + compId + "'" + " AND ClientVendorID=" + cvId;
		try {
			con = db.getConnection();
			stmt = con.createStatement();
			stmt1 = con.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);
			if (resultSet.next()) {
				amount = resultSet.getDouble(1);
			}

			Sql = " Select sum(UpfrontAmount) as ufAmt " + " FROM bca_invoice  " + " WHERE InvoiceTypeID IN (1) "
					+ " AND CompanyID='" + compId + "'" + " AND ClientVendorID=" + cvId;

			rs = stmt1.executeQuery(Sql);
			if (rs.next()) {
				UpFrontamount = rs.getDouble(1);
			}
			amount += UpFrontamount;
		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return amount;
	}

	public ClientVendor getCv(Long cvID, String compId) {
		Statement stmt = null;
		Connection con = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		ClientVendor cv = null;

		sql.append("SELECT FirstName,LastName,Name,Email,State,City,Province,ZipCode,Email,Address1,ClientVendorID "
				+ " FROM bca_clientvendor" + " WHERE  ClientVendorID =" + cvID + " AND Status='N'" + " AND CompanyID= '"
				+ compId + "'");
		try {
			con = db.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				cv = new ClientVendor();
				String name = rs.getString("Name");
				cv.setName(name.equals("") ? name : name.trim());
				cv.setFirstName(rs.getString("FirstName"));
				cv.setLastName(rs.getString("LastName"));
				cv.setCity(rs.getString("City"));
				cv.setState(rs.getString("State"));
				cv.setProvince(rs.getString("Province"));
				cv.setZipCode(rs.getString("ZipCode"));
				cv.setEmail(rs.getString("Email"));
				cv.setAddress1(rs.getString("Address1"));
				cv.setCvID(rs.getInt("ClientVendorID"));
			}
		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return cv;
	}

	public void getLabel(int lblId, CustomerDto label) {
//		Connection con = null;
//		PreparedStatement pstmt_lbl = null;
//		SQLExecutor db = new SQLExecutor();
//		ResultSet rs = null;
//		con = db.getConnection();

		try {

			Optional<BcaLabel> bcaLabel = bcaLabelRepository.findById(lblId);
			if (bcaLabel.isPresent()) {
				BcaLabel bca_label = bcaLabel.get();
				label.setLabelType(bca_label.getId());
				label.setLabelName(bca_label.getLabelType());
				label.setTopMargin(String.valueOf(bca_label.getMarTop()));
				label.setLeftMargin(String.valueOf(bca_label.getMarLeft()));
				label.setLabelWidth(String.valueOf(bca_label.getSizeWidth()));
				label.setLabelHeight(String.valueOf(bca_label.getSizeHeight()));
				label.setVertical(String.valueOf(bca_label.getSpacingVert()));
				label.setHorizon(String.valueOf(bca_label.getSpacingHor()));
			}
//			pstmt_lbl = con.prepareStatement(
//					"select ID,LabelType,Mar_Top,Mar_Left,Size_Width,Size_Height,Spacing_Hor,Spacing_Vert from bca_label where ID=?");
//			pstmt_lbl.setInt(1, lblId);
//			rs = pstmt_lbl.executeQuery();
//			if (rs.next()) {
//
//				label.setLabelType(rs.getInt("ID"));
//				label.setLabelName(rs.getString("LabelType"));
//				label.setTopMargin(rs.getString("Mar_Top"));
//				label.setLeftMargin(rs.getString("Mar_Left"));
//				label.setLabelWidth(rs.getString("Size_Width"));
//				label.setLabelHeight(rs.getString("Size_Height"));
//				label.setVertical(rs.getString("Spacing_Vert"));
//				label.setHorizon(rs.getString("Spacing_Hor"));
//
//			}

		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + " " + ee.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt_lbl != null) {
//					db.close(pstmt_lbl);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
	}

	public ArrayList labelTypeDetails() {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
//		ResultSet rs = null;
//		con = db.getConnection();

		try {
			List<BcaLabel> bcaLabel = bcaLabelRepository.findAllByOrderByLabelType();
			for (BcaLabel bca_label : bcaLabel) {
				CustomerDto label = new CustomerDto();
				label.setLabelType(bca_label.getId());
				label.setLabelName(bca_label.getLabelType());
				label.setTopMargin(String.valueOf(bca_label.getMarTop()));
				label.setLeftMargin(String.valueOf(bca_label.getMarLeft()));
				label.setLabelWidth(String.valueOf(bca_label.getSizeWidth()));
				label.setLabelHeight(String.valueOf(bca_label.getSizeHeight()));
				label.setVertical(String.valueOf(bca_label.getSpacingVert()));
				label.setHorizon(String.valueOf(bca_label.getSpacingHor()));
				objList.add(label);
			}
//			pstmt = con.prepareStatement(
//					"select ID,LabelType,Mar_Top,Mar_Left,Size_Width,Size_Height,Spacing_Hor,Spacing_Vert from bca_label order by LabelType");
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CustomerDto label = new CustomerDto();
//				label.setLabelType(rs.getInt("ID"));
//				label.setLabelName(rs.getString("LabelType"));
//				label.setTopMargin(rs.getString("Mar_Top"));
//				label.setLeftMargin(rs.getString("Mar_Left"));
//				label.setLabelWidth(rs.getString("Size_Width"));
//				label.setLabelHeight(rs.getString("Size_Height"));
//				label.setVertical(rs.getString("Spacing_Vert"));
//				label.setHorizon(rs.getString("Spacing_Hor"));
//				objList.add(label);
//
//			}

		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + " " + ee.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return objList;
	}

	public void saveLabel(CustomerDto form) {

//		Connection con = null;
//		PreparedStatement pstmt = null, pstmt1;
//		SQLExecutor db = new SQLExecutor();
//		ResultSet rs = null;
//		con = db.getConnection();

		try {
//			int labelID = 0;
//			pstmt1 = con.prepareStatement("select max(ID)+1 from bca_label");
//			rs = pstmt1.executeQuery();
//			if (rs.next()) {
//				labelID = rs.getInt(1);
//			}

			BcaLabel bcaLabel = new BcaLabel();
			bcaLabel.setLabelType(form.getLabelName());
			bcaLabel.setMarTop(Double.parseDouble(form.getTopMargin()));
			bcaLabel.setMarLeft(Double.parseDouble(form.getLeftMargin()));
			bcaLabel.setSizeWidth(Double.parseDouble(form.getLabelWidth()));
			bcaLabel.setSizeHeight(Double.parseDouble(form.getLabelHeight()));
			bcaLabel.setSpacingHor(Double.parseDouble(form.getHorizon()));
			bcaLabel.setSpacingVert(Double.parseDouble(form.getVertical()));

			bcaLabelRepository.save(bcaLabel);
//			pstmt = con.prepareStatement("insert into bca_label values(?,\"" + form.getLabelName() + "\",?,?,?,?,?,?)");
//			pstmt.setInt(1, labelID);
//			pstmt.setString(2, form.getTopMargin());
//			pstmt.setString(3, form.getLeftMargin());
//			pstmt.setString(4, form.getLabelWidth());
//			pstmt.setString(5, form.getLabelHeight());
//			pstmt.setString(6, form.getHorizon());
//			pstmt.setString(7, form.getVertical());
//			pstmt.executeUpdate();

		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + " " + ee.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
	}

	public void deleteLabel(int lblId, CustomerDto form) {
		Connection con = null;
		PreparedStatement pstmt_delete = null, pstmt_id = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();
		ResultSet rs_id = null;
		try {
			pstmt_delete = con.prepareStatement("delete from bca_label where ID=?");
			pstmt_delete.setInt(1, lblId);
			int del = pstmt_delete.executeUpdate();
			if (del > 0) {
				pstmt_id = con.prepareStatement("select ID from bca_label where ID >? order by ID asc");
				pstmt_id.setInt(1, lblId);
				rs_id = pstmt_id.executeQuery();
				if (rs_id.next())
					getLabel(rs_id.getInt("ID"), form);
				else {
					pstmt_id = con.prepareStatement("select ID from bca_label");
					rs_id = pstmt_id.executeQuery();
					if (rs_id.next()) {
						getLabel(rs_id.getInt("ID"), form);
					}

				}
			}
		} catch (SQLException ee) {
			Loger.log(2, "  SQL Error in Class CustomerInfo and  method -deleteLabel" + " " + ee.toString());
		} finally {
			try {
				if (rs_id != null) {
					db.close(rs_id);
				}
				if (pstmt_delete != null) {
					db.close(pstmt_delete);
				}
				if (pstmt_id != null) {
					db.close(pstmt_id);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ee) {
				Loger.log(2, " SQL Error in Class CustomerInfo and  method -deleteLabel and in finally " + " "
						+ ee.toString());
			}
		}
	}

	public void updateLabel(int labelID, CustomerDto form) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
//		con = db.getConnection();

		try {
			if (form.getTopMargin().equals("")) {
				form.setTopMargin("0");
			}
			if (form.getLeftMargin().equals("")) {
				form.setLeftMargin("0");
			}
			if (form.getLabelWidth().equals("")) {
				form.setLabelWidth("0");
			}
			if (form.getLabelHeight().equals("")) {
				form.setLabelHeight("0");
			}
			if (form.getHorizon().equals("")) {
				form.setHorizon("0");
			}
			if (form.getVertical().equals("")) {
				form.setTopMargin("0");
			}
			Loger.log("TOP_____________" + form.getTopMargin());
			Loger.log("NAME_____________" + form.getLabelName());
			Loger.log("HERTICAL_____________" + form.getHorizon());

			Optional<BcaLabel> bca_label = bcaLabelRepository.findById(labelID);
			if (bca_label.isPresent()) {
				BcaLabel bcaLabel = bca_label.get();
				bcaLabel.setLabelType(form.getLabelName());
				bcaLabel.setMarTop(Double.parseDouble(form.getTopMargin()));
				bcaLabel.setMarLeft(Double.parseDouble(form.getLeftMargin()));
				bcaLabel.setSizeWidth(Double.parseDouble(form.getLabelWidth()));
				bcaLabel.setSizeHeight(Double.parseDouble(form.getLabelHeight()));
				bcaLabel.setSpacingHor(Double.parseDouble(form.getHorizon()));
				bcaLabel.setSpacingVert(Double.parseDouble(form.getVertical()));
				bcaLabelRepository.save(bcaLabel);
			}

//			pstmt = con.prepareStatement("update bca_label set LabelType=\"" + form.getLabelName()
//					+ "\",Mar_Top=?,Mar_Left=?,Size_Width=?,Size_Height=?,Spacing_Hor=?,Spacing_Vert=? where ID=?");
//			pstmt.setString(1, form.getTopMargin());
//			pstmt.setString(2, form.getLeftMargin());
//			pstmt.setString(3, form.getLabelWidth());
//			pstmt.setString(4, form.getLabelHeight());
//			pstmt.setString(5, form.getHorizon());
//			pstmt.setString(6, form.getVertical());
//			pstmt.setInt(7, labelID);
//			pstmt.executeUpdate();

		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + " " + ee.toString());
		}
//		finally {
//			try {
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
	}

	public ArrayList SearchCustomer(String compId, String cvId, CustomerDto customer) {
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
		ResultSet rs = null;
		ResultSet rs1 = null;
		con = db.getConnection();
		try {
			StringBuffer sqlString = new StringBuffer();
			sqlString.append(
					" SELECT distinct cv.ClientVendorID,cv.Name,cv.FirstName, cv.LastName,cv.Address1, cv.Address2,cv.City,");
			sqlString.append(
					"cv.State, cv.Province, cv.Country,cv.ZipCode, cv.Phone, cv.CellPhone,cv.Fax, cv.Email,cv.HomePage,");
			sqlString.append(
					"cv.CustomerTitle,cv.ResellerTaxID,cv.VendorOpenDebit,cv.VendorAllowedCredit,cv.Detail,cv.Taxable,cv.CVTypeID,max(cv.DateAdded),");
			sqlString.append(
					"cc.CardNumber ,cc.CardExpMonth,cc.CardCW2 ,cc.CardHolderName,cc.CardBillingAddress,cc.CardBillingZipCode,");
			sqlString.append(
					"ad1.Name,ad1.FirstName,ad1.LastName,ad1.Address1,ad1.Address2,ad1.City,ad1.ZipCode,ad1.Country,ad1.State,ad1.Province,ad1.AddressType,");
			sqlString.append(
					"cvf.UseIndividual ,cvf.AnnualInterestRate ,cvf.MinimumFinanceCharge,cvf.GracePeriod ,cvf.AssessFinanceCharge ,cvf.MarkFinanceCharge ");
			sqlString.append(
					"FROM bca_clientvendor cv left join ( bca_cvcreditcard cc ,bca_bsaddress ad1 ,bca_clientvendorfinancecharges cvf )");
			sqlString.append(" on (cc.ClientVendorID= cv.ClientVendorID and ad1.ClientVendorID= ");
			sqlString.append("cv.ClientVendorID and cvf.ClientVendorID= cv.ClientVendorID )");
			sqlString.append(" WHERE cv.Status IN ('N', 'U') and cv.CVTypeID IN ('1', '2') and cv.Deleted = '0' ");
			sqlString.append(" and CompanyID='" + compId + "' and cv.ClientVendorID ='" + cvId
					+ "' group by ( cv.ClientVendorID )");
			sqlString.append(" order by cv.ClientVendorID ");

			pstmt = con.prepareStatement(sqlString.toString());
			Loger.log(sqlString);
			rs = pstmt.executeQuery();
			String addresstype = "";
			if (rs.next()) {
				customer.setClientVendorID(rs.getString(1));
				customer.setCname(rs.getString(2));
				customer.setFirstName(rs.getString(3));
				customer.setLastName(rs.getString(4));
				customer.setAddress1(rs.getString(5));
				customer.setAddress2(rs.getString(6));
				customer.setCity(rs.getString(7));
				customer.setState(rs.getString(8));
				customer.setProvince(rs.getString(9));
				customer.setCountry(rs.getString(10));
				customer.setZipCode(rs.getString(11));
				customer.setPhone(rs.getString(12));
				customer.setCellPhone(rs.getString(13));
				customer.setFax(rs.getString(14));
				customer.setEmail(rs.getString(15));
				customer.setHomePage(rs.getString(16));
				customer.setTitle(rs.getString(17));
				customer.setTexID(rs.getString(18));
				customer.setOpeningUB(rs.getString(19));
				customer.setExtCredit(rs.getString(20));
				customer.setMemo(rs.getString(21));
				customer.setTaxAble(rs.getString(22));
				customer.setIsclient(rs.getString(23));
				customer.setDateAdded(rs.getString(24));
				customer.setCardNo(rs.getString(25));
				customer.setExpDate(rs.getString(26));
				customer.setCw2(rs.getString(27));
				customer.setCardHolderName(rs.getString(28));
				customer.setCardBillAddress(rs.getString(29));
				customer.setCardZip(rs.getString(30));

				if ("0".equalsIgnoreCase(rs.getString(41))) {
					addresstype = "0";
					customer.setBscname(rs.getString(31));
					customer.setBsfirstName(rs.getString(32));
					customer.setBslastName(rs.getString(33));
					customer.setBsaddress1(rs.getString(34));
					customer.setBsaddress2(rs.getString(35));
					customer.setBscity(rs.getString(36));
					customer.setBszipCode(rs.getString(37));
					customer.setBscountry(rs.getString(38));
					customer.setBsstate(rs.getString(39));
					customer.setBsprovince(rs.getString(40));
				} else if ("1".equalsIgnoreCase(rs.getString(41))) {
					addresstype = "1";
					customer.setShcname(rs.getString(31));
					customer.setShfirstName(rs.getString(32));
					customer.setShlastName(rs.getString(33));
					customer.setShaddress1(rs.getString(34));
					customer.setShaddress2(rs.getString(35));
					customer.setShcity(rs.getString(36));
					customer.setShzipCode(rs.getString(37));
					customer.setShcountry(rs.getString(38));
					customer.setShstate(rs.getString(39));
					customer.setShprovince(rs.getString(40));
				}
				customer.setFsUseIndividual(rs.getString(42));
				customer.setAnnualIntrestRate(rs.getString(43));
				customer.setMinFCharges(rs.getString(44));
				customer.setGracePrd(rs.getString(45));
				String str1 = rs.getString(46);
				String str2 = rs.getString(47);

				customer.setFsAssessFinanceCharge(str1);
				customer.setFsMarkFinanceCharge(str2);

				if ("1".equalsIgnoreCase(addresstype)) {
					String sqlString1 = "SELECT Name,FirstName,LastName,Address1,Address2,City,ZipCode,Country,State,Province "
							+ " FROM bca_bsaddress WHERE ClientVendorID=" + cvId
							+ " and AddressType='0' and Status in ('N','U')";
					Loger.log(sqlString1);
					pstmt1 = con.prepareStatement(sqlString1.toString());
					rs1 = pstmt1.executeQuery();
					while (rs1.next()) {
						customer.setBscname(rs1.getString(1));
						customer.setBsfirstName(rs1.getString(2));
						customer.setBslastName(rs1.getString(3));
						customer.setBsaddress1(rs1.getString(4));
						customer.setBsaddress2(rs1.getString(5));
						customer.setBscity(rs1.getString(6));
						customer.setBszipCode(rs1.getString(7));
						customer.setBscountry(rs1.getString(8));
						customer.setBsstate(rs1.getString(9));
						customer.setBsprovince(rs1.getString(10));
					}
				} else if ("0".equalsIgnoreCase(addresstype)) {
					String sqlString1 = " SELECT Name,FirstName,LastName,Address1,Address2,City,ZipCode,Country,State,Province "
							+ " FROM bca_bsaddress WHERE ClientVendorID=" + cvId
							+ " and AddressType='1' and Status in ('N','U')";

					Loger.log(sqlString1);
					pstmt1 = con.prepareStatement(sqlString1.toString());
					rs1 = pstmt1.executeQuery();
					while (rs1.next()) {
						customer.setShcname(rs1.getString(1));
						customer.setShfirstName(rs1.getString(2));
						customer.setShlastName(rs1.getString(3));
						customer.setShaddress1(rs1.getString(4));
						customer.setShaddress2(rs1.getString(5));
						customer.setShcity(rs1.getString(6));
						customer.setShzipCode(rs1.getString(7));
						customer.setShcountry(rs1.getString(8));
						customer.setShstate(rs1.getString(9));
						customer.setShprovince(rs1.getString(10));
					}
				}
				objList.add(customer);
			}
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());

		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (rs1 != null) {
					db.close(rs1);
				}
				if (pstmt1 != null) {
					db.close(pstmt1);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return objList;
	}

	private BcaCvcreditcardRepository bcaCvcreditcardRepository;

	public boolean makeCustomerCardDefault(String cvID, String cardID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		Statement stmt = null;
//		PreparedStatement pstmt = null, pstmt2 = null;
//		ResultSet rs = null;
		boolean valid = false;
//		try {
//			String sqlString = "select * from bca_cvcreditcard where DEFAULTCard=1 AND ClientVendorID=" + cvID;
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sqlString);
		List<BcaCvcreditcard> bcaCvcreditcards = bcaCvcreditcardRepository.findByDefaultCardAndClientVendorId(1,
				Integer.parseInt(cvID));
//			if (rs.next()) {
//				long ccID = rs.getInt("CreditCardID");
//				sqlString = "update bca_cvcreditcard set DEFAULTCard=0 where CreditCardID = " + ccID;
//				pstmt = con.prepareStatement(sqlString);
//				int count = pstmt.executeUpdate(sqlString);
//			}
		for (BcaCvcreditcard bcaCvcreditcard : bcaCvcreditcards) {
			long ccID = bcaCvcreditcard.getCreditCardId();
//				sqlString = "update bca_cvcreditcard set DEFAULTCard=0 where CreditCardID = " + ccID;
//				pstmt = con.prepareStatement(sqlString);

//				int count = pstmt.executeUpdate(sqlString);
			int count = bcaCvcreditcardRepository.updateByCreditCardId(0, ccID);
		}
//			sqlString = "update bca_cvcreditcard set DEFAULTCard=1 where CreditCardID = " + cardID;
//			pstmt2 = con.prepareStatement(sqlString);
//			int count = pstmt2.executeUpdate(sqlString);
		int count = bcaCvcreditcardRepository.updateByCreditCardId(1, Long.valueOf(cardID));// JPA Check
		if (count > 0) {
			valid = true;
		}

//		} catch (SQLException ee) {
//			Loger.log(2, " SQL Error in Class CustomerInfo and  method -makeCustomerCardDefault " + ee.toString());
//
//		} finally {
//			try {
//				if (stmt != null) {
//					db.close(stmt);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (pstmt2 != null) {
//					db.close(pstmt2);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return valid;
	}

	public boolean UpdateCustomer(String compId, String cvId) {
		Connection con = null;
		SQLExecutor db = new SQLExecutor();
		boolean valid = false;
		// ResultSet rs = null;
		con = db.getConnection();
		Statement stmt = null;

		try {
			stmt = con.createStatement();
			StringBuffer sqlString = new StringBuffer();
			sqlString.append("update bca_clientvendor set status='O' where ClientVendorID like '" + cvId
					+ "' and ( status like 'N' or status like 'U') ");
			Loger.log(sqlString);
			int count = stmt.executeUpdate(sqlString.toString());
			if (count > 0) {
				valid = true;
				Loger.log("status updated successfully");
			}
			// Loger.log("!!!!!!!!!!!!!!!!!!!!!!!!");

		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class CustomerInfo and  method -UpdateCustomer " + ee.toString());
		} finally {
			try {

				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return valid;
	}

//	Insert Customer into storage_clientvendor
	public boolean insertCustomerStorage(CustomerDto c, String compID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		PreparedStatement ps = null, pstmt = null;
		boolean ret = false;
		try {
			String oBal = "0.0";
			String exCredit = "0.0";
			String status = "N";
//			PurchaseInfo pinfo = new PurchaseInfo();
			int cvID = purchaseInfo.getLastClientVendorID();

			if (c.getOpeningUB() != null && c.getOpeningUB().trim().length() > 0)
				oBal = c.getOpeningUB();

			if (c.getExtCredit() != null && c.getExtCredit().trim().length() > 0)
				exCredit = c.getExtCredit();

//			VendorCategory vc = new VendorCategory();
			String vcName = vendorCategory.CVCategory(c.getType());

//			String sqlString = "insert into storage_clientvendor(ClientVendorID, Name,DateAdded, CustomerTitle, FirstName, LastName, Address1, Address2,"
//					+ " City, State, Province, Country, ZipCode, Phone, CellPhone,Fax,HomePage, Email, CompanyID,ResellerTaxID,VendorOpenDebit,"
//					+ " VendorAllowedCredit,Detail,Taxable,CVTypeID,CVCategoryID,CVCategoryName,Active,Deleted,Status,isPhoneMobileNumber,isMobilePhoneNumber,"
//					+ " MiddleName,DateInput,DateTerminated,isTerminated,DBAName, TermID,SalesRepID,ShipCarrierID,PaymentTypeID,CCTypeID, CustomerGroupID) "
//					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			StorageClientvendor scv = new StorageClientvendor();
			Optional<BcaClientvendor> bcaClientVendor = bcaClientvendorRepository.findById(cvID);
			if (bcaClientVendor.isPresent())
				scv.setClientVendor(bcaClientVendor.get());
			scv.setName(c.getCname());
			Date dateAdded = (c.getDateAdded() == null || c.getDateAdded().equals("")) ? string2date(" now() ")
					: string2date(c.getDateAdded());
			scv.setDateAdded(DateHelper.convertDateToOffsetDateTime(dateAdded));
			scv.setCustomerTitle(c.getTitle());
			scv.setFirstName(c.getFirstName());
			scv.setLastName(c.getLastName());
			scv.setAddress1(c.getAddress1());
			scv.setAddress2(c.getAddress2());
			scv.setCity(c.getCity());
			scv.setState(c.getState());
			scv.setProvince(c.getProvince());
			scv.setCountry(c.getCountry());
			scv.setZipCode(c.getZipCode());
			scv.setPhone(c.getPhone());
			scv.setCellPhone(c.getCellPhone());
			scv.setFax(c.getFax());
			scv.setHomePage(c.getHomePage());
			scv.setEmail(c.getEmail());
			Optional<BcaCompany> company = bcaCompanyRepository.findById(Long.parseLong(compID));
			if (company.isPresent())
				scv.setCompany(company.get());

			scv.setResellerTaxId(c.getTexID());
			scv.setVendorOpenDebit(Double.parseDouble(oBal));
			scv.setVendorAllowedCredit(Double.parseDouble(exCredit));
			scv.setDetail(c.getMemo());
			if (null != c.getTaxAble())

				scv.setTaxable(Long.parseLong(c.getTaxAble()));

			Optional<BcaCvtype> cvType = bcaCvtypeRepository.findById(Integer.parseInt(c.getIsclient()));
			if (cvType.isPresent()) {
				scv.setCvtype(cvType.get());
			}
			scv.setCvcategory(null);
			scv.setCvcategoryName(vcName);
			scv.setActive(1);
			scv.setDeleted(0);
			scv.setStatus(status);
			scv.setIsPhoneMobileNumber(c.isIsPhoneMobileNumber());
			scv.setIsMobilePhoneNumber(c.isIsMobilePhoneNumber());
			scv.setMiddleName(c.getMiddleName());
			Date dateInput = (c.getDateInput() == null || c.getDateInput().trim().equals("")) ? null
					: string2date(c.getDateInput());
			scv.setDateInput(DateHelper.convertDateToOffsetDateTime(dateInput));
			Date dateTerminated = (c.getTerminatedDate() == null || c.getTerminatedDate().trim().equals("")) ? null
					: string2date(c.getTerminatedDate());
			scv.setDateTerminated(DateHelper.convertDateToOffsetDateTime(dateTerminated));

			scv.setIsTerminated(c.isTerminated());
			scv.setDbaname(c.getDbaName());

			int termId = (c.getTerm() == null || c.getTerm().trim().equals("")) ? 0 : Integer.parseInt(c.getTerm());
			Optional<BcaTerm> term = bcaTermRepository.findById(termId);
			if (term.isPresent())
				scv.setTerm(term.get());
			int salesRepId = (c.getRep() == null || c.getRep().trim().equals("")) ? 0 : Integer.parseInt(c.getRep());
			Optional<BcaSalesrep> salesRep = bcaSalesrepRepository.findById(salesRepId);
			if (salesRep.isPresent())
				scv.setSalesRep(salesRep.get());

			Optional<BcaShipcarrier> shipCarrier = bcaShipcarrierRepository.findById(Integer.parseInt(c.getShipping()));
			if (shipCarrier.isPresent())
				scv.setShipCarrier(shipCarrier.get());
			int paymentTypeId = (c.getPaymentType() == null || c.getPaymentType().trim().equals("")) ? 0
					: Integer.parseInt(c.getPaymentType());
			Optional<BcaPaymenttype> paymentType = bcaPaymenttypeRepository.findById(paymentTypeId);
			if (paymentType.isPresent())
				scv.setPaymentType(paymentType.get());
			if (null != c.getCcType()) {
				Optional<BcaCreditcardtype> creditcardtype = bcaCreditcardtypeRepository
						.findById(Integer.parseInt(c.getCcType()));
				if (creditcardtype.isPresent())
					scv.setCctype(creditcardtype.get());
			}
			if (null != c.getCustomerGroup())
				scv.setCustomerGroupId(Integer.parseInt(c.getCustomerGroup()));
			scv.setCustomerGroupId(Integer.parseInt(c.getCustomerGroup()));

			StorageClientvendor save = storageClientvendorRepository.save(scv);
			if (null != save) {
				ret = true;
			}
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, cvID);
//			pstmt.setString(2, c.getCname());
//			pstmt.setDate(3, (c.getDateAdded() == null || c.getDateAdded().equals("")) ? string2date(" now() ")
//					: string2date(c.getDateAdded()));
//			pstmt.setString(4, c.getTitle());
//			pstmt.setString(5, c.getFirstName());
//			pstmt.setString(6, c.getLastName());
//			pstmt.setString(7, c.getAddress1());
//			pstmt.setString(8, c.getAddress2());
//			pstmt.setString(9, c.getCity());
//			pstmt.setString(10, c.getState());
//			pstmt.setString(11, c.getProvince());
//			pstmt.setString(12, c.getCountry());
//			pstmt.setString(13, c.getZipCode());
//			pstmt.setString(14, c.getPhone());
//			pstmt.setString(15, c.getCellPhone());
//			pstmt.setString(16, c.getFax());
//			pstmt.setString(17, c.getHomePage());
//			pstmt.setString(18, c.getEmail());
//			pstmt.setString(19, compID);
//			pstmt.setString(20, c.getTexID());
//			pstmt.setDouble(21, Double.parseDouble(oBal));
//			pstmt.setDouble(22, Double.parseDouble(exCredit));
//			pstmt.setString(23, c.getMemo());
//			pstmt.setString(24, c.getTaxAble());
//			pstmt.setString(25, c.getIsclient());
//			pstmt.setString(26, c.getType());
//			pstmt.setString(27, vcName);
//			pstmt.setString(28, "1");
//			pstmt.setString(29, "0");
//			pstmt.setString(30, status);
//			pstmt.setBoolean(31, c.isIsPhoneMobileNumber());
//			pstmt.setBoolean(32, c.isIsMobilePhoneNumber());
//			pstmt.setString(33, c.getMiddleName());
//			pstmt.setDate(34, (c.getDateInput() == null || c.getDateInput().trim().equals("")) ? null
//					: string2date(c.getDateInput()));
//			pstmt.setDate(35, (c.getTerminatedDate() == null || c.getTerminatedDate().trim().equals("")) ? null
//					: string2date(c.getTerminatedDate()));
//			pstmt.setBoolean(36, c.isTerminated());
//			pstmt.setString(37, c.getDbaName());
//			pstmt.setInt(38,
//					(c.getTerm() == null || c.getTerm().trim().equals("")) ? 0 : Integer.parseInt(c.getTerm())); // c.getTerm()
//			pstmt.setString(39, (c.getRep() == null || c.getRep().trim().equals("")) ? null : c.getRep()); // c.getRep()
//			pstmt.setString(40, c.getShipping());
//			pstmt.setString(41,
//					(c.getPaymentType() == null || c.getPaymentType().trim().equals("")) ? null : c.getPaymentType()); // c.getPaymentType()
//			pstmt.setString(42, "".equalsIgnoreCase(c.getCcType()) ? null : c.getCcType());
//			pstmt.setString(43, c.getCustomerGroup());
//			Loger.log(sqlString);
//			int num = pstmt.executeUpdate();
//			if (num > 0) {
//				ret = true;
//			}

			// -------------------Code to save services---END-----------------------
		} catch (Exception ee) {
			Loger.log(2, "SQLException in Class CustomerInfo,  method -insertCustomerStorage " + ee.toString());

		}
//		finally {
//			try {
//				if (ps != null) {
//					db.close(ps);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		return ret;
	}

//	Insert Customer
	public boolean insertCustomer(CustomerDto c, String compID) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement ps = null, pstmt = null;
		boolean ret = false;
		try {
			String oBal = "0.0";
			String exCredit = "0.0";
			String status = "N";
//			PurchaseInfo pinfo = new PurchaseInfo();
			int cvID = purchaseInfo.getLastClientVendorID() + 1;

			if (c.getOpeningUB() != null && c.getOpeningUB().trim().length() > 0)
				oBal = c.getOpeningUB();

			if (c.getExtCredit() != null && c.getExtCredit().trim().length() > 0)
				exCredit = c.getExtCredit();

			VendorCategory vc = new VendorCategory();
			String vcName = vc.CVCategory(c.getType());

			BcaClientvendor bcv = new BcaClientvendor();
			bcv.setClientVendorId(cvID);
			bcv.setName(c.getCname());

			Date dateAdded = (c.getDateAdded() == null || c.getDateAdded().equals("")) ? string2date(" now() ")
					: string2date(c.getDateAdded());
			bcv.setDateAdded(DateHelper.convertDateToOffsetDateTime(dateAdded));
			bcv.setCustomerTitle(c.getTitle());
			bcv.setFirstName(c.getFirstName());
			bcv.setLastName(c.getLastName());
			bcv.setAddress1(c.getAddress1());
			bcv.setAddress2(c.getAddress2());
			bcv.setCity(c.getCity());
			bcv.setState(c.getState());
			bcv.setProvince(c.getProvince());
			bcv.setCountry(c.getCountry());
			bcv.setZipCode(c.getZipCode());
			bcv.setPhone(c.getPhone());
			bcv.setCellPhone(c.getPhone());
			bcv.setFax(c.getFax());
			bcv.setHomePage(c.getHomePage());
			bcv.setEmail(c.getEmail());
			Optional<BcaCompany> company = bcaCompanyRepository.findById(Long.parseLong(compID));
			if (company.isPresent()) {
				bcv.setCompany(company.get());
			}

			bcv.setResellerTaxId(c.getTexID());
			bcv.setVendorOpenDebit(Double.parseDouble(oBal));
			bcv.setVendorAllowedCredit(Double.parseDouble(exCredit));
			bcv.setDetail(c.getMemo());
			if (null != c.getTaxAble())
				bcv.setTaxable(Long.parseLong(c.getTaxAble()));
			if (null != c.getIsclient())
				bcv.setCvtypeId(Integer.parseInt(c.getIsclient()));
			if (null != c.getType())
				bcv.setCvcategoryId(Integer.parseInt(c.getType()));
			bcv.setCvcategoryName(vcName);
			bcv.setActive(1);
			bcv.setDeleted(0);
			bcv.setStatus(status);
			bcv.setIsPhoneMobileNumber(c.isIsPhoneMobileNumber());
			bcv.setIsMobilePhoneNumber(c.isIsMobilePhoneNumber());
			bcv.setMiddleName(c.getMiddleName());
			Date dateInput = (c.getDateInput() == null || c.getDateInput().trim().equals("")) ? null
					: string2date(c.getDateInput());
			bcv.setDateInput(DateHelper.convertDateToOffsetDateTime(dateInput));
			Date dateTerminated = (c.getTerminatedDate() == null || c.getTerminatedDate().trim().equals("")) ? null
					: string2date(c.getTerminatedDate());
			bcv.setDateTerminated(DateHelper.convertDateToOffsetDateTime(dateTerminated));
			bcv.setIsTerminated(c.isTerminated());
			int termId = c.getTerm() == null || c.getTerm().trim().equals("") ? 0 : Integer.parseInt(c.getTerm());
			bcv.setDbaname(c.getDbaName());
			Optional<BcaTerm> term = bcaTermRepository.findById(termId);
			if (term.isPresent())
				bcv.setTerm(term.get());
			int salesRepId = (c.getRep() == null || c.getRep().trim().equals("")) ? 0 : Integer.parseInt(c.getRep());
			Optional<BcaSalesrep> salesRep = bcaSalesrepRepository.findById(salesRepId);
			if (salesRep.isPresent())
				bcv.setSalesRep(salesRep.get());

			Optional<BcaShipcarrier> shipCarrier = bcaShipcarrierRepository.findById(Integer.parseInt(c.getShipping()));
			if (shipCarrier.isPresent())
				bcv.setShipCarrier(shipCarrier.get());
			int paymentTypeId = (c.getPaymentType() == null || c.getPaymentType().trim().equals("")) ? 0
					: Integer.parseInt(c.getPaymentType());
			Optional<BcaPaymenttype> paymentType = bcaPaymenttypeRepository.findById(paymentTypeId);
			if (paymentType.isPresent())
				bcv.setPaymentType(paymentType.get());
			if (null != c.getCcType() && !c.getCcType().trim().isEmpty())
				bcv.setCctypeId(Integer.parseInt(c.getCcType()));
			if (null != c.getCustomerGroup())
				bcv.setCustomerGroupId(Integer.parseInt(c.getCustomerGroup()));

			BcaClientvendor save = bcaClientvendorRepository.save(bcv);
			if (null != save) {
				ret = true;
				insertCustomerStorage(c, compID);
			}
//
//			String sqlString = "insert into bca_clientvendor(ClientVendorID, Name,DateAdded, CustomerTitle, FirstName, LastName, Address1, Address2,"
//					+ " City, State, Province, Country, ZipCode, Phone, CellPhone,Fax,HomePage, Email, CompanyID,ResellerTaxID,VendorOpenDebit,"
//					+ " VendorAllowedCredit,Detail,Taxable,CVTypeID,CVCategoryID,CVCategoryName,Active,Deleted,Status,isPhoneMobileNumber,isMobilePhoneNumber,"
//					+ " MiddleName,DateInput,DateTerminated,isTerminated,DBAName, TermID,SalesRepID,ShipCarrierID,PaymentTypeID,CCTypeID, CustomerGroupID) "
//					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, cvID);
//			pstmt.setString(2, c.getCname());
//			pstmt.setDate(3, (c.getDateAdded() == null || c.getDateAdded().equals("")) ? string2date(" now() ")
//					: string2date(c.getDateAdded()));
//			pstmt.setString(4, c.getTitle());
//			pstmt.setString(5, c.getFirstName());
//			pstmt.setString(6, c.getLastName());
//			pstmt.setString(7, c.getAddress1());
//			pstmt.setString(8, c.getAddress2());
//			pstmt.setString(9, c.getCity());
//			pstmt.setString(10, c.getState());
//			pstmt.setString(11, c.getProvince());
//			pstmt.setString(12, c.getCountry());
//			pstmt.setString(13, c.getZipCode());
//			pstmt.setString(14, c.getPhone());
//			pstmt.setString(15, c.getCellPhone());
//			pstmt.setString(16, c.getFax());
//			pstmt.setString(17, c.getHomePage());
//			pstmt.setString(18, c.getEmail());
//			pstmt.setString(19, compID);
//			pstmt.setString(20, c.getTexID());
//			pstmt.setDouble(21, Double.parseDouble(oBal));
//			pstmt.setDouble(22, Double.parseDouble(exCredit));
//			pstmt.setString(23, c.getMemo());
//			pstmt.setString(24, c.getTaxAble());
//			pstmt.setString(25, c.getIsclient());
//			pstmt.setString(26, c.getType());
//			pstmt.setString(27, vcName);
//			pstmt.setString(28, "1");
//			pstmt.setString(29, "0");
//			pstmt.setString(30, status);
//			pstmt.setBoolean(31, c.isIsPhoneMobileNumber());
//			pstmt.setBoolean(32, c.isIsMobilePhoneNumber());
//			pstmt.setString(33, c.getMiddleName());
//			pstmt.setDate(34, (c.getDateInput() == null || c.getDateInput().trim().equals("")) ? null
//					: string2date(c.getDateInput()));
//			pstmt.setDate(35, (c.getTerminatedDate() == null || c.getTerminatedDate().trim().equals("")) ? null
//					: string2date(c.getTerminatedDate()));
//			pstmt.setBoolean(36, c.isTerminated());
//			pstmt.setString(37, c.getDbaName());
//			pstmt.setInt(38,
//					(c.getTerm() == null || c.getTerm().trim().equals("")) ? 0 : Integer.parseInt(c.getTerm())); // c.getTerm()
//			pstmt.setString(39, (c.getRep() == null || c.getRep().trim().equals("")) ? null : c.getRep()); // c.getRep()
//			pstmt.setString(40, c.getShipping());
//			pstmt.setString(41,
//					(c.getPaymentType() == null || c.getPaymentType().trim().equals("")) ? null : c.getPaymentType()); // c.getPaymentType()
//			pstmt.setString(42, "".equalsIgnoreCase(c.getCcType()) ? null : c.getCcType());
//			pstmt.setString(43, c.getCustomerGroup());
//			Loger.log(sqlString);
//			int num = pstmt.executeUpdate();
//			if (num > 0) {
//				ret = true;
//				insertCustomerStorage(c, compID);
//			}

			purchaseInfo.insertVendorCreditCard(cvID, c.getCcType(), c.getCardNo(), c.getExpDate(), c.getCw2(),
					c.getCardHolderName(), c.getCardBillAddress(), c.getCardZip());
			int bsAddID = purchaseInfo.getLastBsAdd() + 1;
			TblBSAddress2 address = new TblBSAddress2();
			if ("0".equals(c.getSetdefaultbs())) {
				purchaseInfo.insertVendorBSAddress(cvID, bsAddID, c.getBscname(), c.getBsdbaName(), c.getBsfirstName(),
						c.getBslastName(), c.getBsaddress1(), c.getBsaddress2(), c.getBscity(), c.getBsstate(),
						c.getBsprovince(), c.getBscountry(), c.getBszipCode(), "1");

				purchaseInfo.insertVendorBSAddress(cvID, bsAddID, c.getShcname(), c.getShdbaName(), c.getShfirstName(),
						c.getShlastName(), c.getShaddress1(), c.getShaddress2(), c.getShcity(), c.getShstate(),
						c.getShprovince(), c.getShcountry(), c.getShzipCode(), "0");

				address.setAddressWithCustomerDtoBilling(c, cvID);
				purchaseInfo.insertBillingShippingAddress(address, 1, true);
				address.setAddressWithCustomerDtoShipping(c, cvID);
				purchaseInfo.insertBillingShippingAddress(address, 0, true);
			} else {
				purchaseInfo.insertVendorBSAddress(cvID, bsAddID, c.getCname(), c.getDbaName(), c.getFirstName(),
						c.getLastName(), c.getAddress1(), c.getAddress2(), c.getCity(), c.getState(), c.getProvince(),
						c.getCountry(), c.getZipCode(), "1");

				purchaseInfo.insertVendorBSAddress(cvID, bsAddID, c.getCname(), c.getDbaName(), c.getFirstName(),
						c.getLastName(), c.getAddress1(), c.getAddress2(), c.getCity(), c.getState(), c.getProvince(),
						c.getCountry(), c.getZipCode(), "0");
				address.setAddressWithCustomerDto(c, cvID);
				purchaseInfo.insertBillingShippingAddress(address, 1, true);
				purchaseInfo.insertBillingShippingAddress(address, 0, true);
			}

			if (c.getCvTypeID() != 3) {
				insertClientInfo(c, cvID, compID);
			}

			insertClientVendorAccount(c, cvID);
//		    bca_account			done
//		    smd_cvinfo			done
//		    bca_billingaddress
//		    bca_shippingaddress
//		    storage_billingaddress
//		    storage_shippingaddress

			int useIndividual = "1".equals(c.getFsUseIndividual()) ? 1 : 0;
			int assFCharge = "1".equals(c.getFsAssessFinanceCharge()) ? 1 : 0;
			int markFCharge = "1".equals(c.getFsMarkFinanceCharge()) ? 1 : 0;
			purchaseInfo.insertVFCharge(cvID, useIndividual, c.getAnnualIntrestRate(), c.getMinFCharges(),
					c.getGracePrd(), assFCharge, markFCharge);

			// -----------------------code to save services---START------------------------
			int i;
			String sql;
			String serviceID = c.getTable_serID();
			String serviceBal = c.getTable_bal();
			String defaultser = c.getTable_defaultVal();
			String invStyleID = c.getTable_invId();
			// Loger.log("SERVICE----------------------->");

			String temp[] = null, temp2[] = null, temp3[] = null;
			if ((serviceID != "" && serviceID != null)
					&& (invStyleID != "" && invStyleID != null) & (serviceBal != "" && serviceBal != null)) {
				temp = serviceID.split(";"); // serviceID is in form like
				// 3;6;8;
				temp2 = invStyleID.split(";");
				temp3 = serviceBal.split(";");
			}

			if ((temp != null) || (temp2 != null) || (temp3 != null)) {
				java.sql.Date d = new java.sql.Date(new Date().getTime());

				for (i = 0; i < temp.length; i++) {
//					BcaClientvendorservice bcvs=new BcaClientvendorservice();
//					bcvs.setClientVendor(save);
//					bcvs.setDateAdded(null);
//					bcvs.setCompany(null);
//					bcvs.setInvoiceStyleId(null);
//					bcvs.setDefaultService(null);
//					bcvs.setServiceId(null);
//					bcvs.setSalePrice(null);

					sql = "insert into bca_clientvendorservice values (?,?,?,?,?,?,?)";
					ps = con.prepareStatement(sql);
					ps.setInt(1, cvID);
					ps.setDate(2, d);
					ps.setInt(3, Integer.parseInt(compID));
					ps.setInt(4, Integer.parseInt(temp2[i]));
					ps.setFloat(5, Float.parseFloat(temp3[i]));
					if (Integer.parseInt(temp[i]) == Integer.parseInt(defaultser))
						ps.setInt(6, 1);
					else
						ps.setInt(6, 0);
					ps.setInt(7, Integer.parseInt(temp[i]));

					ps.executeUpdate();
				}
			}
			// -------------------Code to save services---END-----------------------
		} catch (Exception ee) {
			Loger.log(2, "SQLException in Class CustomerInfo,  method -insertCustomer " + ee.toString());

		} finally {
			try {
				if (ps != null) {
					db.close(ps);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return ret;
	}

	/**
	 * This method is needed because of SMC-BCA merging feature, this method is
	 * needed for SMC to show the customer which is added from BCA should shows in
	 * SMC
	 *
	 * @param cv
	 */
	public boolean insertClientInfo(CustomerDto c, int cvId, String compID) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		Statement stmt = null;
		boolean ret = false;
//		SmdCvinfo smdCvinfo = new SmdCvinfo();
//		Optional<BcaClientvendor> bcaClientvendor = bcaClientvendorRepository.findById(cvId);
//		if (bcaClientvendor.isPresent())
//			smdCvinfo.setClientVendor(bcaClientvendor.get());
//		
//			smdCvinfo.setCompany(ConstValue.companyName );
//		smdCvinfo.setPassword(" ");
//		smdCvinfo.setPasswordHint(" ");
//		smdCvinfo.setPasswordAnswer(" ");
//		smdCvinfo.setNewsletter("Y");
//		smdCvinfo.setSubscribe(" ");
//		smdCvinfo.setIsChecked(" ");
//		smdCvinfo.setStatus("Approved");
//		smdCvinfo.setHomePage(" ");
//		smdCvinfo.setResellerTaxId(" ");
//		smdCvinfo.setTaxable(c.getTaxAble());
//		smdCvinfo.setFid(" ");
//		smdCvinfo.setCustomerGroupId(1);
//		smdCvinfo.setBillingAddressId(0);
//		smdCvinfo.setShippingAddressId(0);
//		smdCvinfo.setAllowMultipleAddress(" ");
//		smdCvinfo.setWww(" ");
//		smdCvinfo.setSourceInfo(0);
//		smdCvinfo.setBusinessType(" ");
//		smdCvinfo.setUserPhoto(" ");
//		smdCvinfo.setIsPhotoPrivate(0);
//		smdCvinfoRepository.save(smdCvinfo);

		String sql2 = " INSERT INTO smd_cvinfo (ClientVendorID,Company,Password," + "PasswordHint, "
				+ "PasswordAnswer, Newsletter, Subscribe, IsChecked,Status,HomePage,ResellerTaxID,Taxable,"
				+ "FID,CustomerGroupID,BillingAddressID,ShippingAddressID,"
				+ "AllowMultipleAddress,WWW,SourceInfo,BusinessType,userPhoto,isPhotoPrivate)" + " VALUES ( " + cvId
				+ "," + // ClientVendorID,
				"'" + ConstValue.companyName + "'" + "," + // CompanyID,
				"' '" + "," + // Password
				"' '" + "," + // PasswordHint
				"' '" + "," + // PasswordAnswer
				"'Y'" + "," + // Newsletter
				"' '" + "," + // Subscribe
				"' '" + "," + // IsChecked
				"'Approved'" + "," + // Status
				"' '" + "," + // HomePage
				"' '" + "," + // ResellerTaxID
				"'" + c.getTaxAble() + "'" + "," + // Taxable
				"' '" + "," + // FID
				1 + "," + // CustomerGroupID
				"0" + "," + // BillingAddressID
				"0" + "," + // ShippingAddressID
				"' '" + "," + // AllowMultipleAddress
				"' '" + "," + // WWW
				"0" + "," + // SourceInfo
				"' '" + "," + // BusinessType
				"' '" + "," + // userPhoto
				"0" + ")"; // isPhotoPrivate
		try {
			stmt = con.createStatement();
			Loger.log("sql query=>" + sql2);
			int num = stmt.executeUpdate(sql2);
			if (num > 0) {
				ret = true;
			}
		} catch (SQLException ee) {
			Loger.log(2, "SQLException in Class CustomerInfo,  method -insertCustomer " + ee.toString());

		} finally {
			try {
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}

		}

		return ret;
	}

	private void insertClientVendorAccount(CustomerDto c, int cvId) {

		String oBal = "0.00";
		int cvTypeID = c.getCvTypeID();
		if (c.getOpeningUB() != null && c.getOpeningUB().trim().length() > 0)
			oBal = c.getOpeningUB();
		Double openingBalance = Double.parseDouble(oBal);

		Statement stmt = null;
		TblAccount account = new TblAccount();
		account.setParentID(0);
		account.setIsCategory(false);
		account.setName(c.getLastName() + ", " + c.getFirstName() + " - " + c.getCname());
		account.setAccountTypeID(3);
		account.setAccountCategoryID(2);
		account.setCvID(cvId);
		account.setDepositPaymentID(0);
		// 1:both 2: customer 3 :vendor
		if (cvTypeID == 1) {
			account.setCustomerStartingBalance(openingBalance);
			account.setCustomerCurrentBalance(openingBalance);
			account.setVendorStartingBalance(openingBalance);
			account.setVendorCurrentBalance(openingBalance);
			account.setDescription("ClientVendor account.");
			if (account.getCustomerStartingBalance() == 2.2E-306) {
				account.setCustomerStartingBalance(0.0);
			}
			if (account.getCustomerCurrentBalance() == 2.2E-306) {
				account.setCustomerCurrentBalance(0.0);
			}
			if (account.getVendorCurrentBalance() == 2.2E-306) {
				account.setVendorCurrentBalance(0.0);
			}
			if (account.getVendorStartingBalance() == 2.2E-306) {
				account.setVendorStartingBalance(0.0);
			}
		} else if (cvTypeID == 2) {
			account.setCustomerStartingBalance(openingBalance);
			account.setCustomerCurrentBalance(openingBalance);
			account.setDescription("Client account.");
			if (account.getCustomerStartingBalance() == 2.2E-306) {
				account.setCustomerStartingBalance(0.0);
			}
			if (account.getCustomerCurrentBalance() == 2.2E-306) {
				account.setCustomerCurrentBalance(0.0);
			}
		} else if (cvTypeID == 3) {
			account.setVendorStartingBalance(openingBalance);
			account.setVendorCurrentBalance(openingBalance);
			account.setDescription("Vendor account.");
			if (account.getVendorStartingBalance() == 2.2E-306) {
				account.setVendorStartingBalance(0.0);
			}
			if (account.getVendorCurrentBalance() == 2.2E-306) {
				account.setVendorCurrentBalance(0.0);
			}
		}
		try {
			int accountId = -1;
			accountId = insertClientVendorAccount(account);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static int insertClientVendorAccount(TblAccount account) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();

		int accountId = -1;
//		BcaAccount bcaAccount = new BcaAccount();
//		bcaAccount.setParentId( account.getParentID());
//		bcaAccount.setIsCategory(account.isIsCategory());
//		bcaAccount.setName(account.getName().replaceAll("'", "''"));
//		bcaAccount.setDescription(account.getDescription().replaceAll("'", "''"));
//		
//		bcaAccount.setAcctType(null);
//		bcaAccount.setAcctCategory(null);
//		
//		bcaAccount.setCompany(null);
//		bcaAccount.setClientVendor(null);
//		bcaAccount.setDepositPaymentId(account.getDepositPaymentID() );
//		bcaAccount.setCustomerStartingBalance(account.getCustomerStartingBalance() );
//		bcaAccount.setCustomerCurrentBalance(account.getCustomerCurrentBalance());
//		bcaAccount.setVendorStartingBalance(account.getVendorStartingBalance());
//		bcaAccount.setVendorCurrentBalance(account.getVendorCurrentBalance() );
//		bcaAccount.setActive(1);
//		bcaAccount.setDateAdded(DateHelper.StringToOffsetDateTime(JProjectUtil.getDateFormater().format(new java.util.Date())));
		String sql = "INSERT INTO bca_account (ParentID,isCategory,Name,"
				+ "Description,AcctTypeID,AcctCategoryID,CompanyID,"
				+ "ClientVendorID,DepositPaymentID,CustomerStartingBalance,"
				+ "CustomerCurrentBalance,VendorStartingBalance," + "VendorCurrentBalance,Active,DateAdded) VALUES ("
				+ account.getParentID() + "," + // ParentID
				(account.isIsCategory() == true ? 1 : 0) + "," + // isCategory,
				"'" + account.getName().replaceAll("'", "''") + "'" + "," + // Name,
				"'" + account.getDescription().replaceAll("'", "''") + "'" + "," + // Description,
				"3" + "," + // account.getAccountTypeID() + "," + //AcctTypeID,
				account.getAccountCategoryID() + "," + // AcctCategoryID,
				ConstValue.companyId + "," + // CompanyID," +
				account.getCvID() + "," + // "ClientVendorID,
				account.getDepositPaymentID() + "," + // DepositPaymentID,
				account.getCustomerStartingBalance() + "," + // CustomerStartingBalance,
				account.getCustomerCurrentBalance() + "," + // CustomerCurrentBalance,
				account.getVendorStartingBalance() + "," + // VendorStartingBalance," +
				account.getVendorCurrentBalance() + "," + // "VendorCurrentBalance,
				"1" + "," + // Active,
				"'" + (JProjectUtil.getDateFormater().format(new java.util.Date())) + "'" + // DateAdded
				")";

		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			// select IDENTITY_VAL_LOCAL
			// rs = stmt.executeQuery("select IDENTITY_VAL_LOCAL() AS LastID");
			rs = stmt.executeQuery(
					"SELECT Max(AccountID) AS LastID from " + "bca_account where companyid=" + ConstValue.companyId); // stmt.executeQuery("SELECT
																														// @@IDENTITY
																														// AS
																														// LastID");
			if (rs.next()) {
				accountId = rs.getInt("LastID");
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return accountId;
	}

	public boolean updateInsertCustomer(String cvId, CustomerDto c, String compID, int istaxable, int isAlsoClient,
			int useIndividualFinanceCharges, int AssessFinanceChk, String status) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement ps = null, pstmt = null;
		boolean ret = false;
		try {
			String oBal = "0";
			String exCredit = "0";
			PurchaseInfo pinfo = new PurchaseInfo();
			int cvID = Integer.parseInt(cvId);
			if (c.getOpeningUB() != null && c.getOpeningUB().trim().length() > 0)
				oBal = c.getOpeningUB();

			if (c.getExtCredit() != null && c.getExtCredit().trim().length() > 0)
				exCredit = c.getExtCredit();

			if (c.getType() == null || c.getType().equals(""))
				c.setType("0");

			VendorCategory vc = new VendorCategory();
			String vcName = vc.CVCategory(c.getType());

			String sqlString = "UPDATE bca_clientvendor SET Name=?,DateAdded=?,CustomerTitle=?, FirstName=?,LastName=?,Address1=?,Address2=?,"
					+ " City=?,State=?,Province=?,Country=?,ZipCode=?,Phone=?,CellPhone=?, Fax=?,HomePage=?,Email=?,CompanyID=?,"
					+ " ResellerTaxID=?,VendorOpenDebit=?,VendorAllowedCredit=?,Detail=?, Taxable=?,CVTypeID=?, "
					+ " CVCategoryID=?,CVCategoryName=?,Active=1,Deleted=0,Status=?,CCTypeID=?,isMobilePhoneNumber=?, "
					+ " MiddleName=?, DateInput=?, DateTerminated=?, isTerminated=?, DBAName=? "
					+ " WHERE ClientVendorID=" + cvID;

			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, c.getCname());
			pstmt.setDate(2, ((c.getDateAdded() == null || c.getDateAdded().equals("")) ? string2date("now()")
					: string2date(c.getDateAdded())));
			pstmt.setString(3, c.getTitle());
			pstmt.setString(4, c.getFirstName());
			pstmt.setString(5, c.getLastName());
			pstmt.setString(6, c.getAddress1());
			pstmt.setString(7, c.getAddress2());
			pstmt.setString(8, c.getCity());
			pstmt.setString(9, c.getState());
			pstmt.setString(10, c.getProvince());
			pstmt.setString(11, c.getCountry());
			pstmt.setString(12, c.getZipCode());
			pstmt.setString(13, c.getPhone());
			pstmt.setString(14, c.getCellPhone());
			pstmt.setString(15, c.getFax());
			pstmt.setString(16, c.getHomePage());
			pstmt.setString(17, c.getEmail());
			pstmt.setString(18, compID);
			pstmt.setString(19, c.getTexID());
			pstmt.setString(20, oBal);
			pstmt.setString(21, exCredit);
			pstmt.setString(22, c.getMemo()); // detail
			pstmt.setInt(23, istaxable); // taxable
			pstmt.setInt(24, isAlsoClient); // cvtypeid
			pstmt.setInt(25, Integer.parseInt(c.getType())); // cvcategoryid
			pstmt.setString(26, vcName); // CVCategoryName
			pstmt.setString(27, status); // may be {N, U, 0(zero)}
			int cct = (c.getCcType() == null || c.getCcType().equals("") ? 0 : Integer.parseInt(c.getCcType()));
			pstmt.setInt(28, cct); // credit card type
			pstmt.setBoolean(29, c.isIsMobilePhoneNumber());
			pstmt.setString(30, c.getMiddleName());
			pstmt.setDate(31, (c.getDateInput() == null || c.getDateInput().trim().equals("")) ? null
					: string2date(c.getDateInput()));
			pstmt.setDate(32, (c.getTerminatedDate() == null || c.getTerminatedDate().trim().equals("")) ? null
					: string2date(c.getTerminatedDate()));
			pstmt.setBoolean(33, c.isTerminated());
			pstmt.setString(34, c.getDbaName());

			Loger.log(sqlString);
			int num = pstmt.executeUpdate();
			if (num > 0) {
				System.out.println(num + " Record updated!!!");
				ret = true;
			}
			if (c.getShipping() != null && c.getShipping().trim().length() > 0)
				pinfo.updateClientVendor("ShipCarrierID", c.getShipping(), cvID);

			if (c.getPaymentType() != null && c.getPaymentType().trim().length() > 0)
				pinfo.updateClientVendor("PaymentTypeID", c.getPaymentType(), cvID);

			if (c.getRep() != null && c.getRep().trim().length() > 0)
				pinfo.updateClientVendor("SalesRepID", c.getRep(), cvID);

			if (c.getTerm() != null && c.getTerm().trim().length() > 0)
				pinfo.updateClientVendor("TermID", c.getTerm(), cvID);

			// change status of old record...........
			pstmt = con.prepareStatement(
					"update bca_bsaddress set status='0' where clientvendorid=? and status in ('N','U')");
			pstmt.setInt(1, cvID);
			pstmt.executeUpdate();
			pstmt.close();
			// ......................status change finished.........

			int bsAddID = pinfo.getLastBsAdd() + 1;
			pinfo.insertVendorBSAddress(cvID, bsAddID, c.getBscname(), c.getBsdbaName(), c.getBsfirstName(),
					c.getBslastName(), c.getBsaddress1(), c.getBsaddress2(), c.getBscity(), c.getBsstate(),
					c.getBsprovince(), c.getBscountry(), c.getBszipCode(), "1");

			System.out.println("bsAddressID: " + bsAddID);

			pinfo.insertVendorBSAddress(cvID, bsAddID, c.getShcname(), c.getShdbaName(), c.getShfirstName(),
					c.getShlastName(), c.getShaddress1(), c.getShaddress2(), c.getShcity(), c.getShstate(),
					c.getShprovince(), c.getShcountry(), c.getShzipCode(), "0");

			pinfo.insertVFCharge(cvID, useIndividualFinanceCharges, c.getAnnualIntrestRate(), c.getMinFCharges(),
					c.getGracePrd(), AssessFinanceChk, 0);

			// billing-shipping------------------------------------------END------

			// --------code to save services--------------------------START---
			int i;
			String sql;
			String serviceID = c.getTable_serID();
			String serviceBal = c.getTable_bal();
			String defaultser = c.getTable_defaultVal();
			String invStyleID = c.getTable_invId();

			sql = "delete from bca_clientvendorservice where ClientVendorID = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, cvID);
			ps.executeUpdate();

			if (serviceID != null && !(serviceID.equals("") || invStyleID.equals("") || serviceBal.equals(""))) {
				String temp[] = null, temp2[] = null, temp3[] = null;
				if ((serviceID != "" && serviceID != null)
						&& (invStyleID != "" && invStyleID != null) & (serviceBal != "" && serviceBal != null)) {
					temp = serviceID.split(";"); // serviceID is in form like // 3;6;8;
					temp2 = invStyleID.split(";");
					temp3 = serviceBal.split(";");
				}
				java.sql.Date d = new java.sql.Date(new Date().getTime());

				for (i = 0; i < temp.length; i++) {
					/*
					 * commented on 13-05-2020 sql =
					 * "insert into bca_clientvendorservice values (?,?,?,?,?,?,?)";
					 */
					sql = "insert into bca_clientvendorservice (ClientVendorID,DateAdded,CompanyID,InvoiceStyleID,ServiceBalance,DEFAULTService) "
							+ "values (?,?,?,?,?,?)";
					ps = con.prepareStatement(sql);
					ps.setInt(1, cvID);
					ps.setDate(2, d);
					ps.setInt(3, Integer.parseInt(compID));
					ps.setInt(4, Integer.parseInt(temp2[i]));
					ps.setFloat(5, Float.parseFloat(temp3[i]));
					if (Integer.parseInt(temp[i]) == Integer.parseInt(defaultser)) {
						ps.setInt(6, 1);
					} else
						ps.setInt(6, 0);
					ps.setInt(7, Integer.parseInt(temp[i]));
					ps.executeUpdate();
				}
			}
			// --------------------------code to save services
			// -------------------------------------END-------

		} catch (SQLException ee) {
			Loger.log(2, "SQLException in Class CustomerInfo," + "method -updateInsertCustomer " + ee.toString());

		} finally {
			try {
				if (ps != null) {
					db.close(ps);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return ret;
	}

	public boolean deleteCustomer(String cvID, String compId) {
		/*
		 * Function to delete the particular Customer Do not actualy DELETE the record;
		 * just UPDATE the value of deleted attribute to 1 deleted=0 means the record is
		 * not deleted, deleted=1 means deleted user can see only undeleted records
		 */

		boolean ret = false;

		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = null;
		// int rec=0;
		String sqlString = null;

		try {
			db = new SQLExecutor();
			con = db.getConnection();

			// update bca_clientvendor.....
			sqlString = "update bca_clientvendor set active=0, status='0', deleted=1 " + " where clientvendorId=?"
					+ " and status in ('U','N') and CompanyID=?";

			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, cvID);
			pstmt.setString(2, compId);
			pstmt.executeUpdate();
			pstmt.close();

			// update bca_bsaddress....
			sqlString = "update bca_bsaddress set status='0' where clientvendorid=?" + " and status in ('N','U')";
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, cvID);
			pstmt.executeUpdate();
			pstmt.close();

			// update bca_creditcard....
//			sqlString = "update bca_cvcreditcard set bca_creditcard.active=0 where clientvendorid=?";
//
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setString(1, cvID);
//			pstmt.executeUpdate();
//			pstmt.close();
			bcaCvcreditcardRepository.updateByActiveAndClientVendorId(Integer.valueOf(cvID)); // JPA Check

			// set flag to indicate success & return value...
			ret = true;
		} catch (Exception e) {
			Loger.log(2, "Exception... CustomerInfo.deleteCustomer(). --->" + e.getMessage());
			ret = false;
		} finally {
			try {
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return ret;
	}

	public void getServices(HttpServletRequest request, String compId) {

		ArrayList<UpdateInvoiceDto> serviceList = new ArrayList<>();
		ArrayList<UpdateInvoiceDto> invoiceName = new ArrayList<>();
		// ArrayList balenceDetails = new ArrayList();
//		ResultSet rs = null;
//		ResultSet rs1 = null;
//		Connection con = null;
//		SQLExecutor db = new SQLExecutor();
//		PreparedStatement pstmt = null;
//		PreparedStatement pstmt1 = null;
//		con = db.getConnection();

//		String sqlString = "select * from bca_servicetype";
//		String sqlString1 = "select  * from bca_invoicestyle where Active=1";

		try {
			List<BcaServicetype> serivceTypeList = bcaServicetypeRepository.findAll();
			for (BcaServicetype servicetype : serivceTypeList) {
				UpdateInvoiceDto uform = new UpdateInvoiceDto();
				uform.setServiceID(servicetype.getServiceId());
				uform.setServiceName(servicetype.getServiceName());
				if (null != servicetype.getInvoiceStyle())
					uform.setInvoiceStyleId(servicetype.getInvoiceStyle().getInvoiceStyleId());
				serviceList.add(uform);
			}
//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				UpdateInvoiceDto uform = new UpdateInvoiceDto();
//				uform.setServiceID(rs.getInt(1));
//				uform.setServiceName(rs.getString(2));
//				uform.setInvoiceStyleId(rs.getInt(3));
//				serviceList.add(uform);
//			}
		} catch (Exception e) {
			Loger.log(e.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		request.setAttribute("ServiceList", serviceList);

		try {
			List<BcaInvoicestyle> invoiceStyles = bcaInvoicestyleRepository.findByActive(1);
			for (BcaInvoicestyle invoicestyle : invoiceStyles) {
				UpdateInvoiceDto uform = new UpdateInvoiceDto();
				// Loger.log("The Incoice style id is " + rs1.getString(1));
				uform.setInvoiceStyleId(invoicestyle.getInvoiceStyleId());
				// Loger.log("The Invoice Style name is " + rs1.getString(2));
				uform.setInvoiceStyle(invoicestyle.getName());
				invoiceName.add(uform);
			}

//			con = db.getConnection();
//			pstmt1 = con.prepareStatement(sqlString1);
//			rs1 = pstmt1.executeQuery();
//			while (rs1.next()) {
//				UpdateInvoiceDto uform = new UpdateInvoiceDto();
//				// Loger.log("The Incoice style id is " + rs1.getString(1));
//				uform.setInvoiceStyleId(rs1.getInt(1));
//				// Loger.log("The Invoice Style name is " + rs1.getString(2));
//				uform.setInvoiceStyle(rs1.getString(2));
//				invoiceName.add(uform);
//			}
		} catch (Exception e) {
			Loger.log(e.toString());
		}
//		finally {
//			try {
//				if (rs1 != null) {
//					db.close(rs1);
//				}
//				if (pstmt1 != null) {
//					db.close(pstmt1);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
	}

	public java.sql.Date string2date(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

		Date d1 = null;
		try {

			d1 = sdf.parse(d);

		} catch (ParseException e) {
			Loger.log(2, "ParseException" + e.getMessage());

		}

		return (d1 != null ? new java.sql.Date(d1.getTime()) : new java.sql.Date(new Date().getTime()));

	}

	public String date2String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateString = sdf.format(date);
		return dateString;
	}

	public String getStatesName(String sid) {
		String sname = "";
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			String sqlString = "select StateName  from state where StateID = ? ";
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, sid);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				sname = rs.getString(1);
			}

		} catch (SQLException ee) {
			Loger.log("Error in State Name Selection" + ee);
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return sname;
	}

	public ArrayList getAccountPayableReport(String cId, HttpServletRequest request, String datesCombo, String fromDate,
			String toDate, String sortBy, CustomerDto form) {
		Connection con = null;
		Statement stmt = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();
		ArrayList<CustomerDto> objList = new ArrayList<>();
		ResultSet rs = null;
		double totalBalance = 0.00;
		String sql = "";
		CustomerInfoDao cinfo = new CustomerInfoDao();
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		DateInfo dInfo = new DateInfo();

		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					form.setFromDate(cinfo.date2String(selectedRange.get(0)));
					form.setToDate(cinfo.date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND inv.DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND inv.DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cinfo.string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND inv.DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cinfo.string2date(toDate) + "')");
			} else {
				dateBetween = " AND inv.DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(cinfo.string2date(fromDate))
						+ "') AND Timestamp ('" + JProjectUtil.getDateFormaterCommon().format(cinfo.string2date(toDate))
						+ "')";
			}
		}

		try {
			sql += "SELECT inv.invoiceid, " + "       inv.ordernum, " + "       inv.ponum, "
					+ "       date_format(inv.dateadded,'%m-%d-%Y')AS DateAdded, " + "       inv.clientvendorid, "
					+ "       inv.adjustedtotal, " + "       inv.balance, " + "       cv.NAME  AS cvName, "
					+ "       cv.firstname, " + "       cv.lastname, " + "       rep.NAME AS repName "
					+ "FROM   ( bca_clientvendor AS cv " + "         INNER JOIN bca_invoice AS inv "
					+ "                 ON inv.clientvendorid = cv.clientvendorid ) "
					+ "       LEFT JOIN bca_salesrep AS rep " + "              ON rep.salesrepid = inv.salesrepid "
					+ "WHERE  inv.companyid = '" + cId + "'" + "       AND inv.invoicetypeid IN ( 2 ) "
					+ "       AND ( cv.status = 'U' " + "              OR cv.status = 'N' ) "
					+ "       AND cv.deleted = 0 " + dateBetween + "ORDER  BY inv.dateadded DESC";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				CustomerDto c = new CustomerDto();
				c.setPoNum(rs.getInt(3));
				c.setInvoiceId(rs.getInt(1));
				c.setDateAdded(rs.getString(4));
				String name = rs.getString(8);
				String firstName = rs.getString(9);
				String lastName = rs.getString(10);
				c.setFullName(name.equals("") ? firstName + " " + lastName : name);
				c.setTotal(rs.getDouble(6));
				c.setBalance(rs.getDouble(7));
				totalBalance += rs.getDouble(7);
				objList.add(c);
			}

		} catch (Exception e) {
			// TODO: handle exception
			Loger.log(e.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (stmt != null) {
					db.close(stmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		request.setAttribute("totalBalance", totalBalance);
		return objList;
	}

	public void getProfitLossDetailReport(String datesCombo, String fromDate, String toDate, String sortBy, String cId,
			HttpServletRequest request, CustomerDto form) {
		Connection con = null;
		Statement stmt1 = null, stmt2 = null, stmt3 = null;
		ResultSet rs1 = null, rs2 = null, rs3 = null;
		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> AccountReceivable = new ArrayList<>();
		ArrayList<CustomerDto> AccountPayable = new ArrayList<>();
		ArrayList<CustomerDto> temp = new ArrayList<>();
		ArrayList<CustomerDto> billPayable = new ArrayList<>();
		con = db.getConnection();
		DateInfo dInfo = new DateInfo();
		String dateBetween = "";
		ArrayList<Date> selectedRange = new ArrayList<>();
		String sql1 = "";
		double totalUncategorisedIncome = 0D;
		double totalCOGS = 0D;
		double grossProfit = 0D;
		double totalBillAmount = 0D;
		double totalVendorOpBal = 0D;
		double netIncome = 0D;
		double amt = 0D;
		if (datesCombo != null && !datesCombo.equals("8")) {
			if (datesCombo != null && !datesCombo.equals("")) {
				selectedRange = dInfo.selectedIndex(Integer.parseInt(datesCombo));
				if (!selectedRange.isEmpty() && selectedRange != null) {
					form.setFromDate(date2String(selectedRange.get(0)));
					form.setToDate(date2String(selectedRange.get(1)));
				}
				if (selectedRange != null && !selectedRange.isEmpty()) {
					dateBetween = " AND DateAdded BETWEEN Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(0)) + "') AND Timestamp ('"
							+ JProjectUtil.getDateFormaterCommon().format(selectedRange.get(1)) + "')";
				}
			}
		} else if (datesCombo != null && datesCombo.equals("8")) {
			if (fromDate.equals("") && toDate.equals("")) {
				dateBetween = "";
			} else if (!fromDate.equals("") && toDate.equals("")) {
				dateBetween = " AND DateAdded >= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(string2date(fromDate) + "')");
			} else if (fromDate.equals("") && !toDate.equals("")) {
				dateBetween = " AND DateAdded <= Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(string2date(toDate) + "')");
			} else {
				dateBetween = " AND DateAdded BETWEEN Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(string2date(fromDate)) + "') AND Timestamp ('"
						+ JProjectUtil.getDateFormaterCommon().format(string2date(toDate)) + "')";
			}
		}
		try {
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();

			sql1 += "SELECT clientvendorid, " + "       NAME, " + "       customeropendebit, "
					+ "       vendoropendebit, " + "       cvtypeid " + "FROM   bca_clientvendor "
					+ "WHERE  status = 'N' " + "       AND active = 1 " + "       AND companyid = '" + cId + "'"
					+ dateBetween + "ORDER  BY dateadded";

			rs1 = stmt1.executeQuery(sql1);
			while (rs1.next()) {
				CustomerDto c = new CustomerDto();
				c.setCvTypeID(rs1.getInt("CVTypeID"));
				if (c.getCvTypeID() == 2) {
					amt = rs1.getDouble("CustomerOpenDebit");
					if (amt == 0) {
						continue;
					}
					c.setClientVendorID(rs1.getString("ClientVendorID"));
					c.setFirstName(rs1.getString("Name"));
					c.setTotal(amt);
					c.setType("Customer");
					c.setMemo("Opening Balance");
					totalUncategorisedIncome += amt;
					AccountReceivable.add(c);
				} else {
					amt = rs1.getDouble("VendorOpenDebit");
					if (amt == 0) {
						continue;
					}
					CustomerDto c1 = new CustomerDto();
					c1.setClientVendorID(rs1.getString("ClientVendorID"));
					c1.setFirstName(rs1.getString("Name"));
					c1.setTotal(amt);
					c1.setType("Vendor");
					c1.setMemo("Opening Balance");
					totalVendorOpBal += amt;
					/* temp.add(c1); */
					/* AccountReceivable.add(c1); */
				}
			}

			String sql2 = "SELECT DISTINCT( ordernum ) AS ID," + "               inv.dateadded, "
					+ "               cv.NAME                           AS NAME, "
					+ "               ( item.purchaseprice * cart.qty ) AS PP, "
					+ "               ( item.saleprice * cart.qty )     AS SP " + "FROM   bca_iteminventory AS item "
					+ "       INNER JOIN (bca_clientvendor AS cv " + "                   INNER JOIN (bca_cart AS cart "
					+ "                               INNER JOIN bca_invoice AS inv "
					+ "                                       ON cart.invoiceid = inv.invoiceid) "
					+ "                           ON cv.clientvendorid = inv.clientvendorid) "
					+ "               ON item.inventoryid = cart.inventoryid " + "WHERE  inv.invoicestatus <> 1 "
					+ dateBetween.replaceAll("DateAdded", "inv.DateAdded") + "       AND invoicetypeid = 1 "
					+ "       AND inv.companyid = '" + cId + "'" + "       AND item.itemtypeid <> 6 "
					+ "       AND status = 'N' " + "ORDER  BY inv.dateadded";

			rs2 = stmt2.executeQuery(sql2);
			while (rs2.next()) {
				CustomerDto c = new CustomerDto();
				CustomerDto pPrice = new CustomerDto();
				pPrice.setNumber(rs2.getString("ID"));
				c.setNumber(rs2.getString("ID"));
				c.setFirstName(rs2.getString("Name"));
				pPrice.setFirstName(rs2.getString("Name"));
				c.setTotal(rs2.getDouble("SP"));
				pPrice.setTotal(rs2.getDouble("PP"));
				c.setType("Invoice");
				pPrice.setType("Invoice");
				c.setMemo("Account Receivable");
				pPrice.setMemo("Account Payable");
				totalUncategorisedIncome += c.getTotal();
				totalCOGS += pPrice.getTotal();
				AccountPayable.add(pPrice);
				AccountReceivable.add(c);
			}
			String sql3 = "SELECT b.billnum        AS ID, " + "       c.NAME           AS NAME, "
					+ "       bd.expenseamount AS AMOUNT " + "FROM   bca_category AS c "
					+ "       INNER JOIN (bca_bill AS b " + "                   INNER JOIN bca_billdetail AS bd "
					+ "                           ON b.billnum = bd.billnum) "
					+ "               ON c.categoryid = bd.expenseacctid " + "WHERE  bd.expenseamount <> 0 "
					+ "       AND bd.inventoryid = 0 " + "       AND bd.companyid = '" + cId + "'"
					+ dateBetween.replaceAll("DateAdded", "b.DateAdded");
			rs3 = stmt3.executeQuery(sql3);
			while (rs3.next()) {
				CustomerDto f = new CustomerDto();
				f.setNumber(rs3.getString("ID"));
				f.setFirstName(rs3.getString("NAME"));
				f.setTotal(rs3.getDouble("AMOUNT"));
				f.setType("Bill");
				f.setMemo("Account Payable");
				totalBillAmount += f.getTotal();
				billPayable.add(f);
			}
			grossProfit = totalUncategorisedIncome - totalCOGS;
		} catch (Exception e) {
			// TODO: handle exception
			Loger.log(e.toString());
		} finally {
			try {
				if (rs1 != null) {
					db.close(rs1);
				}
				if (stmt1 != null) {
					db.close(stmt1);
				}
				if (rs2 != null) {
					db.close(rs2);
				}
				if (stmt2 != null) {
					db.close(stmt2);
				}
				if (rs3 != null) {
					db.close(rs3);
				}
				if (stmt3 != null) {
					db.close(stmt3);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		request.setAttribute("AccountReceivable", AccountReceivable);
		request.setAttribute("AccountPayable", AccountPayable);
		request.setAttribute("GrossProfit", grossProfit);
		request.setAttribute("Total_COGS", totalCOGS);
		request.setAttribute("totalUncategorisedIncome", totalUncategorisedIncome);
		request.setAttribute("BillPayable", billPayable);
	}

	public ArrayList customerDetailsSortByFirstName(String compId) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
//		ResultSet rs = null;
//		con = db.getConnection();
//		CountryState cs = new CountryState();
		try {
			String query = "select distinct bcv from BcaClientvendor bcv where (bcv.status like 'N' or bcv.status like 'U') and (bcv.cvtypeId = 1 or bcv.cvtypeId = 2) and (bcv.deleted = 0 )"
					+ " and bcv.company.companyId = :companyId  order by bcv.firstName";

			TypedQuery<BcaClientvendor> typedQuery = this.entityManager.createNamedQuery(query, BcaClientvendor.class);
			JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));

			List<BcaClientvendor> clientvendors = typedQuery.getResultList();
			for (BcaClientvendor cv : clientvendors) {
				CustomerDto customer = new CustomerDto();
				customer.setClientVendorID(String.valueOf(cv.getClientVendorId()));
				customer.setCname(cv.getName() + "(" + cv.getFirstName() + " " + cv.getLastName() + ")");
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setCity(cv.getCity());
				customer.setStateName(countryState.getStatesName(cv.getState()));
				customer.setZipCode(cv.getZipCode());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setEmail(cv.getEmail());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

				customer.setDateAdded(cv.getDateAdded().format(formatter));
				customer.setCountry(countryState.getCountryName(cv.getCountry()));
				customer.setFullName(cv.getFirstName() + " " + cv.getLastName()); // changed by pritesh 10-09-2018
				customer.setBillTo(cv.getFirstName() + cv.getLastName()); // changed by jay 05-11-2018
				objList.add(customer);

			}

//			String sqlString = "select distinct ClientVendorID,Name,FirstName,LastName, Address1,Address2,"
//					+ "City,State,ZipCode, Phone,CellPhone,Fax,Email,date_format(DateAdded,'%m-%d-%Y') as DateAdded,Country from bca_clientvendor  "
//					+ "where  (Status like 'N' or Status like 'U')  and  (CVTypeID = '1' or CVTypeID = '2') "
//					+ "and ( Deleted = '0') and CompanyID='" + compId + "'   order by FirstName ";
//
//			pstmt = con.prepareStatement(sqlString);
//			Loger.log(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CustomerDto customer = new CustomerDto();
//				customer.setClientVendorID(rs.getString(1));
//				customer.setCname(rs.getString(2) + "(" + rs.getString(3) + " " + rs.getString(4) + ")");
//				customer.setFirstName(rs.getString(3));
//				customer.setLastName(rs.getString(4));
//				customer.setAddress1(rs.getString(5));
//				customer.setAddress2(rs.getString(6));
//				customer.setCity(rs.getString(7));
//				customer.setStateName(cs.getStatesName(rs.getString(8)));
//				customer.setZipCode(rs.getString(9));
//				customer.setPhone(rs.getString(10));
//				customer.setCellPhone(rs.getString(11));
//				customer.setFax(rs.getString(12));
//				customer.setEmail(rs.getString(13));
//				customer.setDateAdded(rs.getString(14));
//				customer.setCountry(cs.getCountryName(rs.getString(15)));
//				customer.setFullName(rs.getString(3) + " " + rs.getString(4)); // changed by pritesh 10-09-2018
//				customer.setBillTo(rs.getString(3) + rs.getString(4)); // changed by jay 05-11-2018
//				objList.add(customer);
//			}

		} catch (Exception ee) {
			Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByFirstName " + ee.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception ex) {
//				Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByFirstName " + ex.toString());
//
//			}
//		}
		return objList;
	}

	public ArrayList<CustomerDto> customerDetailsSortByLastName(String compId) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
//		ResultSet rs = null;
//		con = db.getConnection();
//		CountryState cs = new CountryState();
		try {
			String query = "select distinct bcv from BcaClientvendor bcv where (bcv.status like 'N' or bcv.status like 'U') and (bcv.cvtypeId = 1 or bcv.cvtypeId = 2) and (bcv.deleted = 0 )"
					+ " and bcv.company.companyId = :companyId  order by bcv.lastName";
			TypedQuery<BcaClientvendor> typedQuery = this.entityManager.createNamedQuery(query, BcaClientvendor.class);
			JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));

			List<BcaClientvendor> clientvendors = typedQuery.getResultList();
			for (BcaClientvendor cv : clientvendors) {
				CustomerDto customer = new CustomerDto();
				customer.setClientVendorID(String.valueOf(cv.getClientVendorId()));
				customer.setCname(cv.getName() + "(" + cv.getFirstName() + " " + cv.getLastName() + ")");
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setCity(cv.getCity());
				customer.setStateName(countryState.getStatesName(cv.getState()));
				customer.setZipCode(cv.getZipCode());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setEmail(cv.getEmail());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

				customer.setDateAdded(cv.getDateAdded().format(formatter));
				customer.setCountry(countryState.getCountryName(cv.getCountry()));
				customer.setFullName(cv.getFirstName() + " " + cv.getLastName()); // changed by pritesh 10-09-2018
				customer.setBillTo(cv.getFirstName() + cv.getLastName()); // changed by jay 05-11-2018
				objList.add(customer);
			}

//			String sqlString = "select distinct ClientVendorID,Name,FirstName,LastName, Address1,Address2,"
//					+ "City,State,ZipCode, Phone,CellPhone,Fax,Email,date_format(DateAdded,'%m-%d-%Y') as DateAdded,Country from bca_clientvendor  "
//					+ "where  (Status like 'N' or Status like 'U')  and  (CVTypeID = '1' or CVTypeID = '2') "
//					+ "and ( Deleted = '0') and CompanyID='" + compId + "'   order by LastName ";
//
//			
//			
//			pstmt = con.prepareStatement(sqlString);
//			// Loger.log(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CustomerDto customer = new CustomerDto();
//				customer.setClientVendorID(rs.getString(1));
//				customer.setCname(rs.getString(2) + "(" + rs.getString(3) + " " + rs.getString(4) + ")");
//				customer.setFirstName(rs.getString(3));
//				customer.setLastName(rs.getString(4));
//				customer.setAddress1(rs.getString(5));
//				customer.setAddress2(rs.getString(6));
//				customer.setCity(rs.getString(7));
//				customer.setStateName(countryState.getStatesName(rs.getString(8)));
//				customer.setZipCode(rs.getString(9));
//				customer.setPhone(rs.getString(10));
//				customer.setCellPhone(rs.getString(11));
//				customer.setFax(rs.getString(12));
//				customer.setEmail(rs.getString(13));
//				customer.setDateAdded(rs.getString(14));
//				customer.setCountry(countryState.getCountryName(rs.getString(15)));
//				customer.setFullName(rs.getString(3) + " " + rs.getString(4)); // changed by pritesh 10-09-2018
//				customer.setBillTo(rs.getString(3) + rs.getString(4)); // changed by jay 05-11-2018
//				objList.add(customer);
//			}

		} catch (Exception ee) {
			Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByLastName " + ee.toString());
		}
//		finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception ex) {
//				Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByLastName " + ex.toString());
//
//			}
//		}
		return objList;
	}

	public ArrayList<CustomerDto> customerDetailsSort(String compId, String sort) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<CustomerDto>();
		ResultSet rs = null;
		con = db.getConnection();
		CountryState cs = new CountryState();
		try {
			String sqlString = "select distinct ClientVendorID,Name,FirstName,LastName, Address1,Address2,"
					+ "City,State,ZipCode, Phone,CellPhone,Fax,Email,date_format(DateAdded,'%m-%d-%Y') as DateAdded,Country from bca_clientvendor  "
					+ "where CVTypeID IN (1, 2) AND Status IN ('N', 'U') and Deleted = '0' and active =1 and CompanyID='"
					+ compId + "' order by " + sort;

			pstmt = con.prepareStatement(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CustomerDto customer = new CustomerDto();
				customer.setClientVendorID(rs.getString(1));
				customer.setCname(rs.getString(2));
				customer.setFirstName(rs.getString(3));
				customer.setLastName(rs.getString(4));
				customer.setAddress1(rs.getString(5));
				customer.setAddress2(rs.getString(6));
				customer.setCity(rs.getString(7));
				customer.setStateName(cs.getStatesName(rs.getString(8)));
				customer.setZipCode(rs.getString(9));
				customer.setPhone(rs.getString(10));
				customer.setCellPhone(rs.getString(11));
				customer.setFax(rs.getString(12));
				customer.setEmail(rs.getString(13));
				customer.setDateAdded(rs.getString(14));
				customer.setCountry(cs.getCountryName(rs.getString(15)));
				customer.setFullName(rs.getString(3) + " " + rs.getString(4)); // changed by pritesh 10-09-2018
				customer.setBillTo(rs.getString(3) + rs.getString(4)); // changed by jay 05-11-2018
				objList.add(customer);
			}
		} catch (SQLException ee) {
			Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSort " + ee.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ex) {
				Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByLastName " + ex.toString());
			}
		}
		return objList;
	}

	public ArrayList<CustomerDto> searchCustomers(String compId, String venderText) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ArrayList<CustomerDto> objList = new ArrayList<>();
		ResultSet rs = null;
		con = db.getConnection();
		CountryState cs = new CountryState();
		try {
			String sqlString = "select distinct ClientVendorID,Name,FirstName,LastName, Address1,Address2,City,State,ZipCode, "
					+ " Phone,CellPhone,Fax,Email,date_format(DateAdded,'%m-%d-%Y') as DateAdded,Country from bca_clientvendor "
					+ " WHERE CVTypeID IN (1, 2) AND Status IN ('N', 'U') and Deleted = '0' and CompanyID=" + compId
					+ " AND (Name LIKE '%" + venderText + "%' OR FirstName LIKE '%" + venderText
					+ "%' OR LastName LIKE '%" + venderText + "%') order by Name";

			pstmt = con.prepareStatement(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CustomerDto customer = new CustomerDto();
				customer.setClientVendorID(rs.getString(1));
				customer.setCname(rs.getString(2));
				customer.setFirstName(rs.getString(3));
				customer.setLastName(rs.getString(4));
				customer.setAddress1(rs.getString(5));
				customer.setAddress2(rs.getString(6));
				customer.setCity(rs.getString(7));
				customer.setStateName(cs.getStatesName(rs.getString(8)));
				customer.setZipCode(rs.getString(9));
				customer.setPhone(rs.getString(10));
				customer.setCellPhone(rs.getString(11));
				customer.setFax(rs.getString(12));
				customer.setEmail(rs.getString(13));
				customer.setDateAdded(rs.getString(14));
				customer.setCountry(cs.getCountryName(rs.getString(15)));
				customer.setFullName(rs.getString(3) + " " + rs.getString(4)); // changed by pritesh 10-09-2018
				customer.setBillTo(rs.getString(3) + rs.getString(4)); // changed by jay 05-11-2018
				objList.add(customer);
			}
		} catch (SQLException ee) {
			Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSort " + ee.toString());

		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ex) {
				Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByLastName " + ex.toString());
			}
		}
		return objList;
	}

	public ArrayList<EstimationDto> sortCustomer(String compId, String sort) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ArrayList<EstimationDto> objList = new ArrayList<>();
		ResultSet rs = null;
		con = db.getConnection();
		CountryState cs = new CountryState();
		try {
			String sqlString = "select distinct ClientVendorID,Name,FirstName,LastName, Address1,Address2,"
					+ "City,State,ZipCode, Phone,CellPhone,Fax,Email,date_format(DateAdded,'%m-%d-%Y') as DateAdded,Country from bca_clientvendor  "
					+ "where  (Status like 'N' or Status like 'U')" + "and ( Deleted = '0') and CompanyID='" + compId
					+ "' order by " + sort;

			pstmt = con.prepareStatement(sqlString);
			// Loger.log(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EstimationDto customer = new EstimationDto();
				customer.setClientVendorID(rs.getString(1));
				customer.setCname(rs.getString(2) + "(" + rs.getString(3) + " " + rs.getString(4) + ")");
				customer.setFirstName(rs.getString(3));
				customer.setLastName(rs.getString(4));
				customer.setAddress1(rs.getString(5));
				customer.setAddress2(rs.getString(6));
				customer.setCity(rs.getString(7));
				customer.setStateName(cs.getStatesName(rs.getString(8)));
				customer.setZipCode(rs.getString(9));
				customer.setPhone(rs.getString(10));
				customer.setCellPhone(rs.getString(11));
				customer.setFax(rs.getString(12));
				customer.setEmail(rs.getString(13));
				customer.setDateAdded(rs.getString(14));
				customer.setCountry(cs.getCountryName(rs.getString(15)));
				customer.setFullName(rs.getString(3) + " " + rs.getString(4)); // changed by pritesh 10-09-2018
				customer.setBillTo(rs.getString(3) + rs.getString(4)); // changed by jay 05-11-2018
				objList.add(customer);
			}

		} catch (SQLException ee) {
			Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSort " + ee.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ex) {
				Loger.log(2, "SQL Error in Class TaxInfo and  method -customerDetailsSortByLastName " + ex.toString());

			}
		}
		return objList;
	}

	public void setNewUnitPrice(String companyID, int itemId, double price) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
//		con = db.getConnection();
		try {

//			String sqlString = "update bca_iteminventory set SalePrice=? where InventoryID =? and CompanyID=?";
//			pstmt = con.prepareStatement(sqlString);

			price = Double.parseDouble(new DecimalFormat("##.##").format(price));
			int count = bcaIteminventoryRepository.updateSalesPriceByInventoryIdAndCompanyId(price, itemId,
					Long.parseLong(companyID));

//			pstmt.setDouble(1, price);
//			pstmt.setInt(2, itemId);
//			pstmt.setString(3, companyID);
//			int count = pstmt.executeUpdate();
			if (count > 0) {
				System.out.println(count + " Row updated...");
			}
		} catch (Exception ee) {
			Loger.log(2, "SQL Error in Class CustomerInfo and  method -setNewUnitPrice " + ee.toString());
		}

//		finally {
//			try {
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception ex) {
//				Loger.log(2, "SQL Error in Class CustomerInfo and method -setNewUnitPrice " + ex.toString());
//
//			}
//		}
	}

	public void setNewitemName(String companyID, int itemId, String itemName) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
//		con = db.getConnection();
		try {
			/*
			 * String sqlString =
			 * "update bca_inventory set InventoryName=?,InventoryDescription=? where InventoryID =? and CompanyID=?"
			 * ;
			 */
			int count = bcaIteminventoryRepository.updateInventoryNameByInventoryIdAndCompanyId(itemName, itemId,
					Long.parseLong(companyID));
//			String sqlString = "update bca_iteminventory set InventoryName=? where InventoryID =? and CompanyID=?";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setString(1, itemName);
//			/* pstmt.setString(2,itemName); */
//			pstmt.setInt(2, itemId);
//			pstmt.setString(3, companyID);
//			int count = pstmt.executeUpdate();
			if (count > 0) {
				System.out.println(count + " Row updated...");
			}
		} catch (Exception ee) {
			Loger.log(2, "SQL Error in Class CustomerInfo and  method -setNewitemName " + ee.toString());
		}
//		finally {
//			try {
//				if (pstmt != null) {
//					db.close(pstmt);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception ex) {
//				Loger.log(2, "SQL Error in Class CustomerInfo and method -setNewitemName " + ex.toString());
//
//			}
//		}
	}

	public void setNewitemNameEstimation(String companyID, int itemId, String itemName) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();
		try {
			/*
			 * String sqlString =
			 * "update bca_inventory set InventoryName=?,InventoryDescription=? where InventoryID =? and CompanyID=?"
			 * ;
			 */
			String sqlString = "update bca_iteminventory set InventoryName=? where InventoryID =? and CompanyID=?";
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, itemName);
			/* pstmt.setString(2,itemName); */
			pstmt.setInt(2, itemId);
			pstmt.setString(3, companyID);
			int count = pstmt.executeUpdate();
			if (count > 0) {
				System.out.println(count + " Row updated...");
			}
		} catch (SQLException ee) {
			Loger.log(2, "SQL Error in Class CustomerInfo and  method -setNewitemNameEstimation " + ee.toString());
		} finally {
			try {
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ex) {
				Loger.log(2, "SQL Error in Class CustomerInfo and method -setNewitemNameEstimation " + ex.toString());

			}
		}
	}

	public void setUnitPriceEstimation(String companyID, int itemId, double price) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();
		try {
			String sqlString = "update bca_iteminventory set SalePrice=? where InventoryID =? and CompanyID=?";
			pstmt = con.prepareStatement(sqlString);
			price = Double.parseDouble(new DecimalFormat("##.##").format(price));
			pstmt.setDouble(1, price);
			pstmt.setInt(2, itemId);
			pstmt.setString(3, companyID);
			int count = pstmt.executeUpdate();
			if (count > 0) {
				System.out.println(count + " Row updated...");
			}
		} catch (SQLException ee) {
			Loger.log(2, "SQL Error in Class CustomerInfo and  method -setNewUnitPrice " + ee.toString());
		} finally {
			try {
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception ex) {
				Loger.log(2, "SQL Error in Class CustomerInfo and method -setUnitPriceEstimation " + ex.toString());
			}
		}
	}
}
