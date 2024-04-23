/*
 * Author : Avibha IT Solutions Copyright 2007 Avibha IT Solutions. All rights
 * reserved. AVIBHA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * www.avibha.com
 */

package com.avibha.bizcomposer.sales.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.avibha.bizcomposer.configuration.dao.ConfigurationInfo;
import com.avibha.bizcomposer.configuration.forms.ConfigurationDto;
import com.avibha.bizcomposer.purchase.dao.PurchaseInfo;
import com.avibha.bizcomposer.purchase.dao.VendorCategory;
import com.avibha.bizcomposer.purchase.forms.PurchaseOrderDto;
import com.avibha.bizcomposer.sales.dto.CartItem;
import com.avibha.bizcomposer.sales.dto.InvoiceDetailsResponse;
import com.avibha.bizcomposer.sales.dto.SavePrintResponse;
import com.avibha.bizcomposer.sales.forms.CreditCardDto;
import com.avibha.bizcomposer.sales.forms.CustomerDto;
import com.avibha.bizcomposer.sales.forms.InvoiceDto;
import com.avibha.bizcomposer.sales.forms.UpdateInvoiceDto;
import com.avibha.common.db.SQLExecutor;
import com.avibha.common.log.Loger;
import com.avibha.common.mail.MailSend;
import com.avibha.common.utility.DateInfo;
import com.avibha.common.utility.MyUtility;
import com.nxsol.bizcomposer.accounting.dao.ReceivableLIst;
import com.nxsol.bizcomposer.accounting.daoimpl.ReceivableListImpl;
import com.nxsol.bizcomposer.common.EmailSenderDto;
import com.nxsol.bzcomposer.company.domain.BcaBillingaddress;
import com.nxsol.bzcomposer.company.domain.BcaCart;
import com.nxsol.bzcomposer.company.domain.BcaCctype;
import com.nxsol.bzcomposer.company.domain.BcaClientvendor;
import com.nxsol.bzcomposer.company.domain.BcaClientvendorfinancecharges;
import com.nxsol.bzcomposer.company.domain.BcaClientvendorservice;
import com.nxsol.bzcomposer.company.domain.BcaCompany;
import com.nxsol.bzcomposer.company.domain.BcaCvcreditcard;
import com.nxsol.bzcomposer.company.domain.BcaInvoice;
import com.nxsol.bzcomposer.company.domain.BcaInvoicestyle;
import com.nxsol.bzcomposer.company.domain.BcaInvoicetype;
import com.nxsol.bzcomposer.company.domain.BcaIteminventory;
import com.nxsol.bzcomposer.company.domain.BcaMessage;
import com.nxsol.bzcomposer.company.domain.BcaPaymenttype;
import com.nxsol.bzcomposer.company.domain.BcaSalesrep;
import com.nxsol.bzcomposer.company.domain.BcaSalestax;
import com.nxsol.bzcomposer.company.domain.BcaServicetype;
import com.nxsol.bzcomposer.company.domain.BcaShipcarrier;
import com.nxsol.bzcomposer.company.domain.BcaShippingaddress;
import com.nxsol.bzcomposer.company.domain.BcaTerm;
import com.nxsol.bzcomposer.company.repos.BcaBillingaddressRepository;
import com.nxsol.bzcomposer.company.repos.BcaCartRepository;
import com.nxsol.bzcomposer.company.repos.BcaClientvendorRepository;
import com.nxsol.bzcomposer.company.repos.BcaClientvendorserviceRepository;
import com.nxsol.bzcomposer.company.repos.BcaCompanyRepository;
import com.nxsol.bzcomposer.company.repos.BcaInvoiceRepository;
import com.nxsol.bzcomposer.company.repos.BcaInvoicestyleRepository;
import com.nxsol.bzcomposer.company.repos.BcaInvoicetypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaIteminventoryRepository;
import com.nxsol.bzcomposer.company.repos.BcaMessageRepository;
import com.nxsol.bzcomposer.company.repos.BcaPaymenttypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaPreferenceRepository;
import com.nxsol.bzcomposer.company.repos.BcaSalesrepRepository;
import com.nxsol.bzcomposer.company.repos.BcaSalestaxRepository;
import com.nxsol.bzcomposer.company.repos.BcaServicetypeRepository;
import com.nxsol.bzcomposer.company.repos.BcaShipcarrierRepository;
import com.nxsol.bzcomposer.company.repos.BcaShippingaddressRepository;
import com.nxsol.bzcomposer.company.repos.BcaTermRepository;
import com.nxsol.bzcomposer.company.utils.DateHelper;
import com.nxsol.bzcomposer.company.utils.JpaHelper;
import com.pritesh.bizcomposer.accounting.bean.BcaShippingaddressDto;

/*
 *
 */
@Repository
public class InvoiceInfoDao {
	@Autowired
	BcaCompanyRepository bcaCompanyRepository;

	@Autowired
	private BcaIteminventoryRepository bcaIteminventoryRepository;

	@Autowired
	private BcaServicetypeRepository bcaServicetypeRepository;

	@Autowired
	private BcaInvoicestyleRepository bcaInvoicestyleRepository;

	@Autowired
	private BcaClientvendorserviceRepository bcaClientvendorserviceRepository;

	@Autowired
	private BcaInvoiceRepository bcaInvoiceRepository;
	@Autowired
	private ConfigurationInfo configInfo;

	@Autowired
	private BcaClientvendorRepository bcaClientvendorRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private BcaPreferenceRepository bcaPreferenceRepository;

	@Autowired
	private BcaShipcarrierRepository bcaShipcarrierRepository;

	@Autowired
	private BcaSalesrepRepository bcaSalesrepRepository;

	@Autowired
	private BcaTermRepository bcaTermRepository;

	@Autowired
	private BcaPaymenttypeRepository bcaPaymenttypeRepository;

	@Autowired
	private BcaMessageRepository bcaMessageRepository;

	@Autowired
	private BcaSalestaxRepository bcaSalestaxRepository;

	@Autowired
	private BcaBillingaddressRepository bcaBillingaddressRepository;

	@Autowired
	private BcaShippingaddressRepository bcaShippingaddressRepository;

	@Autowired
	private BcaInvoicestyleRepository invoicestyleRepository;

	@Autowired
	private BcaServicetypeRepository servicetypeRepository;

	@Autowired
	private BcaCartRepository bcaCartRepository;

	@Autowired
	private BcaInvoicetypeRepository bcaInvoicetypeRepository;

	@Autowired
	private ReceivableLIst receivableLIst;

	public InvoiceDetailsResponse invoiceDetails(int invoiceId, String companyId) {
		InvoiceDetailsResponse response = new InvoiceDetailsResponse();

		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement invStmt, cartStmt;
		ResultSet invResultSet, cartResultSet;

		String invStr = "SELECT inv.InvoiceID, inv.OrderNum, inv.ClientVendorID, inv.Subtotal, inv.Tax, inv.Balance, inv.Total, inv.PaidAmount, inv.DateConfirmed, company.Name, cus.FirstName, cus.LastName  FROM bca_invoice inv LEFT JOIN bca_clientvendor cus ON inv.ClientVendorID=cus.ClientVendorID LEFT JOIN bca_company company ON inv.CompanyID=company.CompanyID where inv.IsPaymentCompleted='1' and inv.InvoiceID='"
				+ invoiceId + "' and inv.CompanyID='" + companyId + "' ";

		try {
			invStmt = con.prepareStatement(invStr);
			invResultSet = invStmt.executeQuery();
			/* Insert into invoice */
			int invoiceID = 0;
			if (invResultSet.next()) {
				invoiceID = invResultSet.getInt(1);
				response.setInvoiceId(invoiceID);
				response.setOrderNo(invResultSet.getInt("OrderNum"));
				response.setCustomerId(String.valueOf(invResultSet.getInt("ClientVendorID")));
				response.setSubTotal(String.valueOf(invResultSet.getDouble("Subtotal")));
				response.setTaxTotal(String.valueOf(invResultSet.getDouble("Tax")));
				response.setDiscount(String.valueOf(invResultSet.getDouble("Balance")));
				response.setGrandTotalWithTax(String.valueOf(invResultSet.getDouble("Total")));
				response.setGrandTotal(String.valueOf(invResultSet.getDouble("PaidAmount")));
				// datetime add here
				LocalDateTime localDateTime = invResultSet.getTimestamp("DateConfirmed").toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = localDateTime.format(formatter);
				response.setDate(formattedDateTime);
				String customerName = invResultSet.getString("LastName") == null
						&& invResultSet.getString("FirstName") == null ? null
								: invResultSet.getString("LastName") + invResultSet.getString("FirstName");
				response.setCustomerName(customerName);
				response.setCompanyName(invResultSet.getString("Name"));

			}

			if (invoiceId == 0) {
				throw new SQLException("Invoice is zero");
			}

			List<CartItem> cartItems = new ArrayList<>();

			String cartStr = "SELECT cart.CartID, cart.InventoryID, cart.InventoryName, cart.InventoryCode, cart.Qty, cart.UnitPrice FROM bca_cart cart INNER JOIN bca_invoice inv ON cart.InvoiceID=inv.InvoiceID where cart.InvoiceID='"
					+ invoiceId + "' and inv.CompanyID='" + companyId + "' order by CartID ASC";

			cartStmt = con.prepareStatement(cartStr);
			cartResultSet = cartStmt.executeQuery();

			while (cartResultSet.next()) {
				int cartId = cartResultSet.getInt(1);
				int inventoryId = cartResultSet.getInt("InventoryID");
				String inventoryName = cartResultSet.getString("InventoryName");
				String inventoryCode = cartResultSet.getString("InventoryCode");
				int qty = cartResultSet.getInt("Qty");
				double unitPrice = cartResultSet.getDouble("UnitPrice");

				// Create a new CartItem instance
				CartItem cartItem = new CartItem();
				cartItem.setCartId(cartId);
				cartItem.setItemId(String.valueOf(inventoryId));
				cartItem.setItemName(inventoryName);
				cartItem.setItemCode(inventoryCode);
				cartItem.setQty(String.valueOf(qty));
				cartItem.setPrice(String.valueOf(unitPrice));
				// amount
				BigDecimal number = new BigDecimal(String.valueOf(qty * unitPrice));
				BigDecimal amount = number.setScale(2, RoundingMode.HALF_UP);
				cartItem.setAmount(String.valueOf(amount));

				// Add the CartItem to the list
				cartItems.add(cartItem);
			}

			response.setCartItems(cartItems);
			return response;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public SavePrintResponse posInvoiceSave(BoxInvoice boxInvoice) {

		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement maxInvStmt = null, invHeaderInsStmt = null;
		PreparedStatement cartInsertStmt = null, updateStmt = null;
		ResultSet maxInvResultSet;

		try {
			// Getting and inserting last max invoiceId
			maxInvStmt = con.prepareStatement("select max(InvoiceID) from bca_invoice");
			maxInvResultSet = maxInvStmt.executeQuery();
			/* Insert into invoice */
			if (maxInvResultSet.next()) {
				int invoiceID = maxInvResultSet.getInt(1);
				boxInvoice.setInvoiceId(++invoiceID);
				// Header
				String headerSql = "INSERT INTO bca_invoice (OrderNum, ClientVendorID, InvoiceTypeID, CompanyID, Subtotal, Tax, Balance, Total, PaidAmount, PaymentTypeID, SalesRepID, IsPaymentCompleted, DateConfirmed, DateAdded, IsInvoice, IsSalestype, TermID, AdjustedTotal, OrderType, InvoiceID)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				invHeaderInsStmt = con.prepareStatement(headerSql);
				// get new sales order number
				String orderNoStr = getNewOrderNo(String.valueOf(boxInvoice.getCompanyId()));
				int orderNo = Integer.parseInt(orderNoStr);
				invHeaderInsStmt.setInt(1, orderNo);
				invHeaderInsStmt.setInt(2, boxInvoice.getCustomerId());
				invHeaderInsStmt.setInt(3, boxInvoice.getInvoiceTypeId());
				invHeaderInsStmt.setInt(4, boxInvoice.getCompanyId());
				invHeaderInsStmt.setDouble(5, boxInvoice.getSubTotal());
				invHeaderInsStmt.setDouble(6, boxInvoice.getTaxTotal());
				invHeaderInsStmt.setDouble(7, boxInvoice.getDiscount());
				invHeaderInsStmt.setDouble(8, boxInvoice.getGrandTotal() + boxInvoice.getTaxTotal());
				invHeaderInsStmt.setDouble(9, boxInvoice.getGrandTotal());
				invHeaderInsStmt.setInt(10, boxInvoice.getPaymentMethod());
				invHeaderInsStmt.setInt(11, boxInvoice.getSalesRepId());
				invHeaderInsStmt.setInt(12, boxInvoice.getIsPaymentCompleted());
				invHeaderInsStmt.setTimestamp(13, Timestamp.valueOf(boxInvoice.getDateConfirmed()));
				invHeaderInsStmt.setTimestamp(14, Timestamp.valueOf(boxInvoice.getDateAdded()));
				invHeaderInsStmt.setInt(15, boxInvoice.getIsInvoice());
				invHeaderInsStmt.setInt(16, boxInvoice.getIsSalesType());
				invHeaderInsStmt.setInt(17, boxInvoice.getTermId());
				invHeaderInsStmt.setDouble(18, boxInvoice.getGrandTotal());
				invHeaderInsStmt.setInt(19, 7);
				invHeaderInsStmt.setInt(20, boxInvoice.getInvoiceId());
				invHeaderInsStmt.executeUpdate();
				invHeaderInsStmt.close();

				for (BoxInvoiceItem item : boxInvoice.getInvoiceItems()) {
					String cartInsertStr = "INSERT INTO bca_cart (InvoiceID, CompanyID, InventoryID, InventoryName, InventoryCode, Qty, UnitPrice) VALUES (?,?,?,?,?,?,?)";
					cartInsertStmt = con.prepareStatement(cartInsertStr);
					cartInsertStmt.setInt(1, boxInvoice.getInvoiceId());
					cartInsertStmt.setInt(2, boxInvoice.getCompanyId());
					cartInsertStmt.setInt(3, item.getItemId());
					cartInsertStmt.setString(4, item.getItemName());
					cartInsertStmt.setString(5, item.getItemCode());
					cartInsertStmt.setInt(6, item.getQty());
					cartInsertStmt.setDouble(7, item.getPrice());
					int updatedRows = cartInsertStmt.executeUpdate();

					if (updatedRows > 0) {
						updateStmt = con.prepareStatement(
								"UPDATE bca_iteminventory SET ExpectedQty=ExpectedQty-? WHERE InventoryID=?");
						updateStmt.setInt(1, item.getQty());
						updateStmt.setInt(2, item.getItemId());
						updateStmt.executeUpdate();
					}

				}

				return new SavePrintResponse(true, String.valueOf(invoiceID));
			}
		} catch (SQLException ee) {
			Loger.log("Exception" + ee);

		} finally {
			try {
				if (maxInvStmt != null) {
					db.close(maxInvStmt);
				}
				if (invHeaderInsStmt != null) {
					db.close(invHeaderInsStmt);
				}
				if (cartInsertStmt != null) {
					db.close(cartInsertStmt);
				}
				if (updateStmt != null) {
					db.close(updateStmt);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return new SavePrintResponse(false, null);
	}

	public List<Item> getItemListByCategory(String categoryId, String compId) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Item> tempList = new ArrayList<>();
		try {
			String sqlString = "SELECT InventoryID,ParentID,InventoryCode,InventoryName,InventoryDescription,Qty,Weight,SalePrice,isCategory,ItemTypeID,SerialNum "
					+ "FROM bca_iteminventory WHERE ParentID=" + categoryId + " and CompanyID=" + compId
					+ " and Active=1";
			pstmt = con.prepareStatement(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Item item1 = new Item();
				item1.setInvID(rs.getInt("InventoryID"));
				item1.setParentID(rs.getInt("ParentID"));
				item1.setInvCode(rs.getString("InventoryCode"));
				item1.setInventoryName(rs.getString("InventoryName"));
				item1.setInvDesc(rs.getString("InventoryDescription"));
				item1.setQty(rs.getInt("Qty"));
				item1.setWeight(rs.getDouble("Weight"));
				if (rs.getBigDecimal("SalePrice") != null) {
					item1.setSalePrice(rs.getBigDecimal("SalePrice").setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					item1.setSalePrice(rs.getBigDecimal("SalePrice"));
				}

				item1.setIsCategory(rs.getInt("isCategory"));
				item1.setItemTypeID(rs.getInt("ItemTypeID"));
				item1.setSerialNo(rs.getString("SerialNum"));

				if (item1.getIsCategory() != 1) {
					tempList.add(item1);
				}
			}
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -getItemList " + ee.toString());
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

		return tempList;
	}

	public Map<String, ArrayList<Item>> getItemListAndCategories(String compId) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Item> categoryList = new ArrayList<>();
		ArrayList<Item> categoryWiseList = new ArrayList<>();
		ArrayList<Item> tempList = new ArrayList<>();
		try {
			String sqlString = "SELECT InventoryID,ParentID,InventoryCode,InventoryName,InventoryDescription,Qty,Weight,SalePrice,isCategory,ItemTypeID,SerialNum "
					+ "FROM bca_iteminventory WHERE CompanyID=" + compId + " and Active=1";
			pstmt = con.prepareStatement(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Item item1 = new Item();
				item1.setInvID(rs.getInt("InventoryID"));
				item1.setParentID(rs.getInt("ParentID"));
				item1.setInvCode(rs.getString("InventoryCode"));
				item1.setInventoryName(rs.getString("InventoryName"));
				item1.setInvDesc(rs.getString("InventoryDescription"));
				item1.setQty(rs.getInt("Qty"));
				item1.setWeight(rs.getDouble("Weight"));
				if (rs.getBigDecimal("SalePrice") != null) {
					item1.setSalePrice(rs.getBigDecimal("SalePrice").setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					item1.setSalePrice(rs.getBigDecimal("SalePrice"));
				}

				item1.setIsCategory(rs.getInt("isCategory"));
				item1.setItemTypeID(rs.getInt("ItemTypeID"));
				item1.setSerialNo(rs.getString("SerialNum"));
				tempList.add(item1);
				if (item1.getIsCategory() == 1) {
					categoryList.add(item1);
				}
			}
			for (Item item1 : categoryList) {
				categoryWiseList.add(item1);
				for (Item item2 : tempList) {
					if (item1.getInvID() == item2.getParentID()) {
						categoryWiseList.add(item2);
					}
				}
			}
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -getItemList " + ee.toString());
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
		Map<String, ArrayList<Item>> itemListAndCategory = new HashMap<>();
		itemListAndCategory.put("categories", categoryList);
		itemListAndCategory.put("itemList", categoryWiseList);

		return itemListAndCategory;
	}

	public List<Item> getItemsBySearchValue(String searchValue, String compId) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Item> categoryList = new ArrayList<>();
		ArrayList<Item> categoryWiseList = new ArrayList<>();
		ArrayList<Item> tempList = new ArrayList<>();
		try {
			String sqlString = "SELECT InventoryID,ParentID,InventoryCode,InventoryName,InventoryDescription,Qty,Weight,SalePrice,isCategory,ItemTypeID,SerialNum "
					+ "FROM bca_iteminventory WHERE (InventoryCode like '" + searchValue + "%' or InventoryName like '"
					+ searchValue + "%') and CompanyID=" + compId + " and Active=1";
			pstmt = con.prepareStatement(sqlString);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Item item1 = new Item();
				item1.setInvID(rs.getInt("InventoryID"));
				item1.setParentID(rs.getInt("ParentID"));
				item1.setInvCode(rs.getString("InventoryCode"));
				item1.setInventoryName(rs.getString("InventoryName"));
				item1.setInvDesc(rs.getString("InventoryDescription"));
				item1.setQty(rs.getInt("Qty"));
				item1.setWeight(rs.getDouble("Weight"));
				if (rs.getBigDecimal("SalePrice") != null) {
					item1.setSalePrice(rs.getBigDecimal("SalePrice").setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					item1.setSalePrice(rs.getBigDecimal("SalePrice"));
				}

				item1.setIsCategory(rs.getInt("isCategory"));
				item1.setItemTypeID(rs.getInt("ItemTypeID"));
				item1.setSerialNo(rs.getString("SerialNum"));

				if (item1.getIsCategory() != 1) {
					tempList.add(item1);
				}
			}

		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -getItemList " + ee.toString());
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

		return tempList;
	}

	public ArrayList<Item> getItemList(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
		ArrayList<Item> categoryList = new ArrayList<>();
		ArrayList<Item> categoryWiseList = new ArrayList<>();
		ArrayList<Item> tempList = new ArrayList<>();
		try {

			List<BcaIteminventory> bcaIteminventories = bcaIteminventoryRepository
					.findByCompany_CompanyIdAndActive(Long.parseLong(compId), 1);
			for (BcaIteminventory iteminventory : bcaIteminventories) {
				Item item1 = new Item();
				item1.setInvID(iteminventory.getInventoryId());
				item1.setParentID(iteminventory.getParentId());
				item1.setInvCode(iteminventory.getInventoryCode());
				item1.setInventoryName(iteminventory.getInventoryName());
				item1.setInvDesc(iteminventory.getInventoryDescription());
				if (null != iteminventory.getQty())
					item1.setQty(iteminventory.getQty());
				if (null != iteminventory.getWeight())
					item1.setWeight(iteminventory.getWeight());
				if (iteminventory.getSalePrice() != null) {
					item1.setSalePrice(
							new BigDecimal(iteminventory.getSalePrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
//				else {
//					item1.setSalePrice(new BigDecimal(iteminventory.getSalePrice()));
//				}

				item1.setIsCategory(iteminventory.getIsCategory() != true ? 0 : 1);
				item1.setItemTypeID(iteminventory.getItemTypeId());
				item1.setSerialNo(iteminventory.getSerialNum());
				tempList.add(item1);
				if (item1.getIsCategory() == 1) {
					categoryList.add(item1);
				}
			}
			for (Item item1 : categoryList) {
				categoryWiseList.add(item1);
				for (Item item2 : tempList) {
					if (item1.getInvID() == item2.getParentID()) {
						categoryWiseList.add(item2);
					}
				}
			}

//			String sqlString = "SELECT InventoryID,ParentID,InventoryCode,InventoryName,InventoryDescription,Qty,Weight,SalePrice,isCategory,ItemTypeID,SerialNum "
//					+ "FROM bca_iteminventory WHERE CompanyID=" + compId + " and Active=1";
//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Item item1 = new Item();
//				item1.setInvID(rs.getInt("InventoryID"));
//				item1.setParentID(rs.getInt("ParentID"));
//				item1.setInvCode(rs.getString("InventoryCode"));
//				item1.setInventoryName(rs.getString("InventoryName"));
//				item1.setInvDesc(rs.getString("InventoryDescription"));
//				item1.setQty(rs.getInt("Qty"));
//				item1.setWeight(rs.getDouble("Weight"));
//				if (rs.getBigDecimal("SalePrice") != null) {
//					item1.setSalePrice(rs.getBigDecimal("SalePrice").setScale(2, BigDecimal.ROUND_HALF_UP));
//				} else {
//					item1.setSalePrice(rs.getBigDecimal("SalePrice"));
//				}
//
//				item1.setIsCategory(rs.getInt("isCategory"));
//				item1.setItemTypeID(rs.getInt("ItemTypeID"));
//				item1.setSerialNo(rs.getString("SerialNum"));
//				tempList.add(item1);
//				if (item1.getIsCategory() == 1) {
//					categoryList.add(item1);
//				}
//			}
//			for (Item item1 : categoryList) {
//				categoryWiseList.add(item1);
//				for (Item item2 : tempList) {
//					if (item1.getInvID() == item2.getParentID()) {
//						categoryWiseList.add(item2);
//					}
//				}
//			}
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -getItemList " + ee.toString());
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
		return categoryWiseList;
	}

//	public ArrayList<Item> getItemList(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		ArrayList<Item> categoryList = new ArrayList<>();
//		ArrayList<Item> categoryWiseList = new ArrayList<>();
//		ArrayList<Item> tempList = new ArrayList<>();
//		try {
//
//			String sqlString = "SELECT InventoryID,ParentID,InventoryCode,InventoryName,InventoryDescription,Qty,Weight,SalePrice,isCategory,ItemTypeID,SerialNum "
//					+ "FROM bca_iteminventory WHERE CompanyID=" + compId + " and Active=1";
//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Item item1 = new Item();
//				item1.setInvID(rs.getInt("InventoryID"));
//				item1.setParentID(rs.getInt("ParentID"));
//				item1.setInvCode(rs.getString("InventoryCode"));
//				item1.setInventoryName(rs.getString("InventoryName"));
//				item1.setInvDesc(rs.getString("InventoryDescription"));
//				item1.setQty(rs.getInt("Qty"));
//				item1.setWeight(rs.getDouble("Weight"));
//				if (rs.getBigDecimal("SalePrice") != null) {
//					item1.setSalePrice(rs.getBigDecimal("SalePrice").setScale(2, BigDecimal.ROUND_HALF_UP));
//				} else {
//					item1.setSalePrice(rs.getBigDecimal("SalePrice"));
//				}
//
//				item1.setIsCategory(rs.getInt("isCategory"));
//				item1.setItemTypeID(rs.getInt("ItemTypeID"));
//				item1.setSerialNo(rs.getString("SerialNum"));
//				tempList.add(item1);
//				if (item1.getIsCategory() == 1) {
//					categoryList.add(item1);
//				}
//			}
//			for (Item item1 : categoryList) {
//				categoryWiseList.add(item1);
//				for (Item item2 : tempList) {
//					if (item1.getInvID() == item2.getParentID()) {
//						categoryWiseList.add(item2);
//					}
//				}
//			}
//		} catch (Exception ee) {
//			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -getItemList " + ee.toString());
//		} finally {
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
//		return categoryWiseList;
//	}

	public ArrayList shipAddress(String companyId, String cvID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<InvoiceDto> objList = new ArrayList<>();
//		CountryState conState = new CountryState();
		try {
//			ConfigurationInfo configInfo = new ConfigurationInfo();
			ConfigurationDto configDto = configInfo.getDefaultCongurationData(companyId);

			StringBuffer query = new StringBuffer("select distinct new  "
					+ BcaShippingaddressDto.class.getCanonicalName()
					+ "(a.addressId , a.clientVendor.clientVendorId , a.name , a.firstName , a.lastName , a.address1 , a.address2, "
					+ " a.zipCode ,a.city , ct.name as cityName , a.state , s.name as stateName , a.country , c.name as countryName) from BcaShippingaddress as a left join "
					+ " BcaCountries as c on c.id=a.country left join  BcaStates as s on s.id=a.state left join BcaCities as ct on ct.id = a.city where a.status in ('N', 'U') and "
					+ " a.active =1 and a.isDefault = 1 ");
			if (cvID != null && !cvID.trim().isEmpty()) {
				query = query.append(" and a.clientVendor.clientVendorId = :clientVendorId");
			}

			if (companyId != null && !companyId.trim().isEmpty()) {
				query.append(" and a.company.companyId = :companyId");
			}

			TypedQuery<BcaShippingaddressDto> typedQuery = this.entityManager.createQuery(query.toString(),
					BcaShippingaddressDto.class);
			if (cvID != null)
				JpaHelper.addParameter(typedQuery, query.toString(), "clientVendorId", Integer.parseInt(cvID));

			if (companyId != null)
				JpaHelper.addParameter(typedQuery, query.toString(), "companyId", Long.valueOf(companyId));

			List<BcaShippingaddressDto> list = typedQuery.getResultList();
			for (BcaShippingaddressDto dto : list) {
				InvoiceDto invoiceDto = new InvoiceDto();
				invoiceDto.setShAddressID(dto.getAddressId().toString());
				invoiceDto.setClientVendorID(dto.getClientVendorId().toString());
				invoiceDto.setFullName(dto.getFirstName() + "  " + dto.getLastName());
				/*
				 * conState.getStatesName(rs.getString(9)) + "\n"+
				 * conState.getCountryName(rs.getString(11));
				 */
				invoiceDto.setAddress1(dto.getAddress1());
				invoiceDto.setAddress2(dto.getAddress2());
				invoiceDto.setZipcode(dto.getZipCode());
				invoiceDto.setState(dto.getStateName());
				invoiceDto.setCountry(dto.getCountryName());
				String ADDRESS_ASD22 = invoiceDto.getAddress2();
				if (ADDRESS_ASD22 != null && ADDRESS_ASD22.trim().length() > 0) {
					ADDRESS_ASD22 = "\n" + ADDRESS_ASD22;
				} else {
					ADDRESS_ASD22 = "";
				}
				String ship = invoiceDto.getFullName() + "\n" + dto.getName() + "\n" + dto.getAddress1() + ADDRESS_ASD22
						+ "\n" + dto.getCityName() + ", " + dto.getStateName() + " " + dto.getZipCode();
//                if (configDto.isShowUSAInBillShipAddress()) {
//                    ship = ship + "\n" + rs.getString("CountryName");
//                } else if (!invoiceDto.getCountry().equals("231")) {
//                    ship = ship + "\n" + rs.getString("CountryName");
//                }
				if (ship.equals(""))
					invoiceDto.setShipTo("");
				else {
					invoiceDto.setShipTo(ship);
				}
				objList.add(invoiceDto);
			}

//			String sqlString = "SELECT distinct a.AddressID,a.ClientVendorID,a.Name,a.FirstName,a.LastName,a.Address1,a.Address2,a.ZipCode,"
//					+ "a.City, ct.Name As CityName, a.State, s.name AS StateName, a.Country,c.name AS CountryName "
//					+ " FROM bca_shippingaddress AS a LEFT JOIN bca_countries AS c ON c.id=a.Country LEFT JOIN bca_states AS s ON s.id=a.State "
//					+ " LEFT JOIN bca_cities AS ct ON ct.id=a.City  WHERE a.Status IN ('N') and a.Active=1 and a.isDefault=1";
//			String sqlString = "SELECT distinct a.BSAddressID,a.ClientVendorID,a.Name,a.FirstName,a.LastName,a.Address1,a.Address2,a.ZipCode,"
//					+ "a.City, ct.Name As CityName, a.State, s.name AS StateName, a.Country,c.name AS CountryName "
//					+ " FROM bca_bsaddress AS a LEFT JOIN bca_countries AS c ON c.id=a.Country LEFT JOIN bca_states AS s ON s.id=a.State "
//					+ " LEFT JOIN bca_cities AS ct ON ct.id=a.City WHERE a.Status IN ('N', 'U') and a.AddressType=0";
//			if (cvID != null && !cvID.trim().isEmpty()) {
//				sqlString = sqlString + " AND a.ClientVendorID=" + cvID + " LIMIT 1";
//			}
//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();

//			while (rs.next()) {
//				InvoiceDto invoiceDto = new InvoiceDto();
//				invoiceDto.setShAddressID(rs.getString(1));
//				invoiceDto.setClientVendorID(rs.getString(2));
//				invoiceDto.setFullName(rs.getString(4) + "  " + rs.getString(5));
//				/*
//				 * conState.getStatesName(rs.getString(9)) + "\n"+
//				 * conState.getCountryName(rs.getString(11));
//				 */
//				invoiceDto.setAddress1(rs.getString(6));
//				invoiceDto.setAddress2(rs.getString(7));
//				invoiceDto.setZipcode(rs.getString("ZipCode"));
//				invoiceDto.setState(rs.getString("StateName"));
//				invoiceDto.setCountry(rs.getString("CountryName"));
//				String ADDRESS_ASD22 = invoiceDto.getAddress2();
//				if (ADDRESS_ASD22 != null && ADDRESS_ASD22.trim().length() > 0) {
//					ADDRESS_ASD22 = "\n" + ADDRESS_ASD22;
//				} else {
//					ADDRESS_ASD22 = "";
//				}
//				String ship = invoiceDto.getFullName() + "\n" + rs.getString(3) + "\n" + rs.getString(6) + ADDRESS_ASD22
//						+ "\n" + rs.getString("CityName") + ", " + rs.getString("StateName") + " "
//						+ rs.getString("ZipCode");
////                if (configDto.isShowUSAInBillShipAddress()) {
////                    ship = ship + "\n" + rs.getString("CountryName");
////                } else if (!invoiceDto.getCountry().equals("231")) {
////                    ship = ship + "\n" + rs.getString("CountryName");
////                }
//				if (ship.equals(""))
//					invoiceDto.setShipTo("");
//				else {
//					invoiceDto.setShipTo(ship);
//				}
//				objList.add(invoiceDto);
//			}
		} catch (Exception ee) {
			ee.printStackTrace();
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -shipAddress " + ee.toString());

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

	public ArrayList billAddress(String companyID, String cvID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<InvoiceDto> objList = new ArrayList<>();
		try {
//			ConfigurationInfo configInfo = new ConfigurationInfo();
			ConfigurationDto configDto = configInfo.getDefaultCongurationData(companyID);

//			String sqlString = "SELECT distinct a.AddressID,a.ClientVendorID,a.Name,a.FirstName,a.LastName,a.Address1,a.Address2,a.ZipCode,"
//					+ "a.City,ct.Name As CityName, a.State,s.name AS StateName, a.Country,c.name AS CountryName "
//					+ " FROM bca_billingaddress AS a LEFT JOIN bca_countries AS c ON c.id=a.Country LEFT JOIN bca_states AS s ON s.id=a.State "
//					+ " LEFT JOIN bca_cities AS ct ON ct.id=a.City WHERE a.Status IN ('N') and a.Active=1 and a.isDefault=1";

			StringBuffer query = new StringBuffer("select distinct new  "
					+ BcaShippingaddressDto.class.getCanonicalName()
					+ "(a.addressId , a.clientVendor.clientVendorId , a.name , a.firstName , a.lastName , a.address1 , a.address2, "
					+ " a.zipCode ,a.city , ct.name as cityName , a.state , s.name as stateName , a.country , c.name as countryName) from BcaBillingaddress as a left join "
					+ " BcaCountries as c on c.id=a.country left join  BcaStates as s on s.id=a.state left join BcaCities as ct on ct.id = a.city where a.status in ('N', 'U') and "
					+ " a.active =:active and a.isDefault = :isDefault ");

			if (cvID != null && !cvID.trim().isEmpty()) {
				query = query.append(" and a.clientVendor.clientVendorId= :clientVendorId");
			}
			if (companyID != null && !companyID.trim().isEmpty()) {
				query.append(" and a.company.companyId = :companyId");
			}

			TypedQuery<BcaShippingaddressDto> typedQuery = this.entityManager.createQuery(query.toString(),
					BcaShippingaddressDto.class);
			if (null != cvID)
				JpaHelper.addParameter(typedQuery, query.toString(), "clientVendorId", Integer.valueOf(cvID));
			JpaHelper.addParameter(typedQuery, query.toString(), "active", 1);
			JpaHelper.addParameter(typedQuery, query.toString(), "isDefault", 1);
			if (companyID != null)
				JpaHelper.addParameter(typedQuery, query.toString(), "companyId", Long.valueOf(companyID));

			List<BcaShippingaddressDto> list = typedQuery.getResultList();

			for (BcaShippingaddressDto dto : list) {
				InvoiceDto invoiceDto = new InvoiceDto();
				invoiceDto.setBsAddressID(dto.getAddressId().toString());
				invoiceDto.setClientVendorID(dto.getClientVendorId().toString());
				invoiceDto.setFullName(dto.getFirstName() + "  " + dto.getLastName());
				/*
				 * conState.getStatesName(rs.getString(9)) + "\n"+
				 * conState.getCountryName(rs.getString(11));
				 */
				invoiceDto.setAddress1(dto.getAddress1());
				invoiceDto.setAddress2(dto.getAddress2());
				invoiceDto.setZipcode(dto.getZipCode());
				invoiceDto.setState(dto.getStateName());
				invoiceDto.setCountry(dto.getCountryName());
				String ADDRESS_ASD22 = invoiceDto.getAddress2();
				if (ADDRESS_ASD22 != null && ADDRESS_ASD22.trim().length() > 0) {
					ADDRESS_ASD22 = "\n" + ADDRESS_ASD22;
				} else {
					ADDRESS_ASD22 = "";
				}
				String bill = invoiceDto.getFullName() + "\n" + dto.getName() + "\n" + dto.getAddress1() + ADDRESS_ASD22
						+ "\n" + dto.getCityName() + ", " + dto.getStateName() + " " + dto.getZipCode();
//                if (configDto.isShowUSAInBillShipAddress()) {
//                    bill = bill + "\n" + rs.getString("CountryName");
//                } else if (!invoiceDto.getCountry().equals("231")) {
//                    bill = bill + "\n" + rs.getString("CountryName");
//                }
				if (bill.equals(""))
					invoiceDto.setBillTo("");
				else {
					invoiceDto.setBillTo(bill);
				}
				objList.add(invoiceDto);
			}

//			String sqlString = "SELECT distinct a.AddressID,a.ClientVendorID,a.Name,a.FirstName,a.LastName,a.Address1,a.Address2,a.ZipCode,"
//					+ "a.City,ct.Name As CityName, a.State,s.name AS StateName, a.Country,c.name AS CountryName "
//					+ " FROM bca_billingaddress AS a LEFT JOIN bca_countries AS c ON c.id=a.Country LEFT JOIN bca_states AS s ON s.id=a.State "
//					+ " LEFT JOIN bca_cities AS ct ON ct.id=a.City WHERE a.Status IN ('N', 'U') and a.Active=1 and a.isDefault=1";
//			String sqlString = "SELECT distinct a.BSAddressID,a.ClientVendorID,a.Name,a.FirstName,a.LastName,a.Address1,a.Address2,a.ZipCode,"
//					+ "a.City, ct.Name As CityName, a.State, s.name AS StateName, a.Country,c.name AS CountryName "
//					+ " FROM bca_bsaddress AS a LEFT JOIN bca_countries AS c ON c.id=a.Country LEFT JOIN bca_states AS s ON s.id=a.State "
//					+ " LEFT JOIN bca_cities AS ct ON ct.id=a.City WHERE a.Status IN ('N', 'U') and a.AddressType=1";
//			if (cvID != null && !cvID.trim().isEmpty()) {
//				sqlString = sqlString + " AND a.ClientVendorID=" + cvID + " LIMIT 1";
//			}
//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				InvoiceDto invoiceDto = new InvoiceDto();
//				invoiceDto.setBsAddressID(rs.getString(1));
//				invoiceDto.setClientVendorID(rs.getString(2));
//				invoiceDto.setFullName(rs.getString(4) + "  " + rs.getString(5));
//				/*
//				 * conState.getStatesName(rs.getString(9)) + "\n"+
//				 * conState.getCountryName(rs.getString(11));
//				 */
//				invoiceDto.setAddress1(rs.getString(6));
//				invoiceDto.setAddress2(rs.getString(7));
//				invoiceDto.setZipcode(rs.getString("ZipCode"));
//				invoiceDto.setState(rs.getString("StateName"));
//				invoiceDto.setCountry(rs.getString("CountryName"));
//				String ADDRESS_ASD22 = invoiceDto.getAddress2();
//				if (ADDRESS_ASD22 != null && ADDRESS_ASD22.trim().length() > 0) {
//					ADDRESS_ASD22 = "\n" + ADDRESS_ASD22;
//				} else {
//					ADDRESS_ASD22 = "";
//				}
//				String bill = invoiceDto.getFullName() + "\n" + rs.getString(3) + "\n" + rs.getString(6) + ADDRESS_ASD22
//						+ "\n" + rs.getString("CityName") + ", " + rs.getString("StateName") + " "
//						+ rs.getString("ZipCode");
////                if (configDto.isShowUSAInBillShipAddress()) {
////                    bill = bill + "\n" + rs.getString("CountryName");
////                } else if (!invoiceDto.getCountry().equals("231")) {
////                    bill = bill + "\n" + rs.getString("CountryName");
////                }
//				if (bill.equals(""))
//					invoiceDto.setBillTo("");
//				else {
//					invoiceDto.setBillTo(bill);
//				}
//				objList.add(invoiceDto);
//			}
		} catch (Exception ee) {
			ee.printStackTrace();
			Loger.log(2, " SQL Error in Class TaxInfo and  method -billAddress " + ee.toString());

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

	public ArrayList customerDetails(String compId, HttpServletRequest request) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
		ArrayList<LabelValueBean> objList = new ArrayList<>();
		ArrayList<InvoiceDto> details = new ArrayList<>();
		try {

			List<String> status = Arrays.asList("U", "N");
			List<BcaClientvendor> clientVendor = bcaClientvendorRepository
					.findDistinctByCompany_CompanyIdAndStatusInAndDeletedAndActiveOrderByName(Long.parseLong(compId),
							status, 0, 1);
			for (BcaClientvendor vendor : clientVendor) {
				String cvId = vendor.getClientVendorId().toString();
				InvoiceDto invForm = new InvoiceDto();
				objList.add(new LabelValueBean(
						vendor.getName() + "(" + vendor.getLastName() + ", " + vendor.getFirstName() + ")", cvId));
				invForm.setClientVendorID(cvId);
				invForm.setCompanyName(vendor.getName());
				invForm.setFirstName(vendor.getFirstName());
				invForm.setLastName(vendor.getLastName());
				if (null != vendor.getShipCarrier())
					invForm.setVia(vendor.getShipCarrier().getShipCarrierId().toString());
				if (null != vendor.getPaymentType())
					invForm.setPayMethod(vendor.getPaymentType().getPaymentTypeId().toString());
				if (null != vendor.getTerm())
					invForm.setTerm(vendor.getTerm().getTermId().toString());
				if (null != vendor.getSalesRep())
					invForm.setRep(vendor.getSalesRep().getSalesRepId().toString());
				invForm.setTaxable(vendor.getTaxable().toString());
				invForm.setCustomerHasBalance(isCustomerHasBalance(cvId));
				details.add(invForm);
			}

//		String sqlString = "SELECT distinct ClientVendorID,FirstName,LastName,ShipCarrierID,PaymentTypeID,TermID,SalesRepID,Taxable,Name "
//					+ "FROM bca_clientvendor WHERE CompanyID=? AND Status IN ('U', 'N') AND Deleted=0 AND Active=1 ORDER BY Name";
//
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setString(1, compId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				String cvId = rs.getString(1);
//				InvoiceDto invForm = new InvoiceDto();
//				objList.add(new LabelValueBean(
//						rs.getString("Name") + "(" + rs.getString(3) + ", " + rs.getString(2) + ")", cvId));
//				invForm.setClientVendorID(cvId);
//				invForm.setCompanyName(rs.getString("Name"));
//				invForm.setFirstName(rs.getString(2));
//				invForm.setLastName(rs.getString(3));
//				invForm.setVia(rs.getString("ShipCarrierID"));
//				invForm.setPayMethod(rs.getString("PaymentTypeID"));
//				invForm.setTerm(rs.getString("TermID"));
//				invForm.setRep(rs.getString("SalesRepID"));
//				invForm.setTaxable(rs.getString("Taxable"));
//				invForm.setCustomerHasBalance(isCustomerHasBalance(cvId));
//				details.add(invForm);
//			}
			request.setAttribute("CustDetails", details);
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -customerDetails " + ee.toString());
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

	public ArrayList sortedcustomerDetails(String compId, HttpServletRequest request, String sort) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
		ArrayList<LabelValueBean> objList = new ArrayList<LabelValueBean>();
		ArrayList<InvoiceDto> details = new ArrayList<InvoiceDto>();
//		ResultSet rs = null;
//		con = db.getConnection();
		String cvId = "";
		try {

			StringBuffer query = new StringBuffer(
					"select distinct bcv from BcaClientvendor where bcv.company.companyId = :companyId and bcv.status "
							+ " in ('U','N') and bcv.deleted = 0 and active =1 order by bcv." + sort);

			TypedQuery<BcaClientvendor> typedQuery = this.entityManager.createNamedQuery(query.toString(),
					BcaClientvendor.class);
			JpaHelper.addParameter(typedQuery, query.toString(), "companyId", Long.parseLong(compId));
			List<BcaClientvendor> bcaClientvendors = typedQuery.getResultList();
			for (BcaClientvendor bcv : bcaClientvendors) {
				cvId = String.valueOf(bcv.getClientVendorId());
				InvoiceDto invForm = new InvoiceDto();
				objList.add(new LabelValueBean(bcv.getName() + "(" + bcv.getLastName() + "," + bcv.getFirstName() + ")",
						cvId));
				invForm.setClientVendorID(cvId);
				invForm.setFirstName(bcv.getFirstName());
				invForm.setLastName(bcv.getLastName());
				if (bcv.getShipCarrier() != null)
					invForm.setVia(String.valueOf(bcv.getShipCarrier().getShipCarrierId()));
				if (null != bcv.getPaymentType())
					invForm.setPayMethod(String.valueOf(bcv.getPaymentType().getPaymentTypeId()));
				if (null != bcv.getTerm())
					invForm.setTerm(String.valueOf(bcv.getTerm().getTermId()));
				if (null != bcv.getSalesRep())
					invForm.setRep(String.valueOf(bcv.getSalesRep().getSalesRepId()));
				invForm.setTaxable(String.valueOf(bcv.getTaxable()));
				details.add(invForm);
			}
			/*
			 * String sqlString =
			 * "select distinct ClientVendorID,FirstName,LastName,ShipCarrierID,PaymentTypeID,TermID,SalesRepID,Taxable,Name from bca_clientvendor"
			 * +
			 * " where  (Status like 'N' or Status like 'U')  and  (CVTypeID = '1' or CVTypeID = '2') "
			 * + " and ( Deleted = '0') and CompanyID=? and Active=1 order by "+sort;
			 */

			String sqlString = "SELECT distinct ClientVendorID,FirstName,LastName,ShipCarrierID,PaymentTypeID,TermID,SalesRepID,Taxable,Name "
					+ "FROM bca_clientvendor " + "WHERE CompanyID = ? " + "AND Status IN ('U', 'N' ) "
					+ "AND Deleted = 0 " + "AND Active = 1 " + "ORDER BY " + sort;
			/*
			 * String sqlString1 =
			 * "SELECT distinct ClientVendorID,FirstName,LastName,ShipCarrierID,PaymentTypeID,TermID,SalesRepID,Taxable,Name "
			 * + "FROM bca_clientvendor " + "WHERE CompanyID = ? AND Status IN ('U', 'N' ) "
			 * + "AND Deleted = 0 AND Active IN (0, 1) " + "ORDER BY "+sort;
			 */
//
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setString(1, compId);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				cvId = rs.getString(1);
//				InvoiceDto invForm = new InvoiceDto();
//				objList.add(new LabelValueBean(
//						rs.getString("Name") + "(" + rs.getString(3) + "," + rs.getString(2) + ")", cvId));
//				invForm.setClientVendorID(cvId);
//				invForm.setFirstName(rs.getString(2));
//				invForm.setLastName(rs.getString(3));
//				invForm.setVia(rs.getString("ShipCarrierID"));
//				invForm.setPayMethod(rs.getString("PaymentTypeID"));
//				invForm.setTerm(rs.getString("TermID"));
//				invForm.setRep(rs.getString("SalesRepID"));
//				invForm.setTaxable(rs.getString("Taxable"));
//				details.add(invForm);
//				/*
//				 * Loger.log("BEAN___________________________" + invForm.getTaxable());
//				 */
//			}
			request.setAttribute("CustDetails", details);
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

	public ArrayList getInvoiceStyle() {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<>();
		try {

			List<BcaInvoicestyle> bcaInvoiceStyle = bcaInvoicestyleRepository.findByActive(1);
			for (BcaInvoicestyle invoicestyle : bcaInvoiceStyle) {
				arr.add(new LabelValueBean(invoicestyle.getName(), invoicestyle.getInvoiceStyleId().toString()));
			}

//			pstmt = con.prepareStatement("select InvoiceStyleID,Name from bca_invoicestyle where Active=1");
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("InvoiceStyleID")));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getInvoiceStyle " + ee.toString());
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
		return arr;

	}

	public ArrayList getRep(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<>();
		try {

			List<BcaSalesrep> bcaSalesRep = bcaSalesrepRepository
					.findByCompany_CompanyIdAndActive(Long.parseLong(compId), 1);
			for (BcaSalesrep salesrep : bcaSalesRep) {
				arr.add(new LabelValueBean(salesrep.getName(), salesrep.getSalesRepId().toString()));
			}

//			String sqlString = "select SalesRepID,Name from bca_salesrep where Active=1 and CompanyID=? order by Name";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, Integer.parseInt(compId));
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("SalesRepID")));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getRep " + ee.toString());
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
		return arr;
	}

	public ArrayList getVia(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<LabelValueBean>();
		try {

			List<BcaShipcarrier> bcaShipCarrier = bcaShipcarrierRepository
					.findByCompany_CompanyIdAndActive(Long.parseLong(compId), 1);
			for (BcaShipcarrier shipcarrier : bcaShipCarrier) {
				arr.add(new LabelValueBean(shipcarrier.getName(), shipcarrier.getShipCarrierId().toString()));
			}

//			String sqlString = "select ShipCarrierID,Name from bca_shipcarrier where Active=1 and CompanyID=? order by Name";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, Integer.parseInt(compId));
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("ShipCarrierID")));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getVia " + ee.toString());
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
		return arr;
	}

	public ArrayList getTerm(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<>();
		try {

			List<BcaTerm> bcaTerm = bcaTermRepository.findByCompany_CompanyIdAndActive(Long.parseLong(compId), 1);

			for (BcaTerm term : bcaTerm) {
				arr.add(new LabelValueBean(term.getName(), term.getTermId().toString()));
			}

//			String sqlString = "select TermID,Name from bca_term where Active=1 and CompanyID=? order by Name";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, Integer.parseInt(compId));
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("TermID")));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getTerm " + ee.toString());
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
		return arr;
	}

	public ArrayList getPayMethod(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<>();
		try {
			List<BcaPaymenttype> bcaPaymentType = bcaPaymenttypeRepository
					.findByCompany_CompanyIdAndActiveAndTypeCategory(Long.parseLong(compId), 1, 1);
			for (BcaPaymenttype paymenttype : bcaPaymentType) {
				arr.add(new LabelValueBean(paymenttype.getName(), paymenttype.getPaymentTypeId().toString()));
			}

//			String sqlString = "SELECT * FROM bca_paymenttype WHERE CompanyID=? AND Active=1 AND TypeCategory=1 ORDER BY PaymentTypeID";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, Integer.parseInt(compId));
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("PaymentTypeID")));
//
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getPayMethod " + ee.toString());
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
		return arr;
	}

	public ArrayList getMessage(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> arr = new ArrayList<>();
		try {

			List<BcaMessage> bcaMessage = bcaMessageRepository.findByCompany_CompanyIdAndActive(Long.parseLong(compId),
					1);

			for (BcaMessage message : bcaMessage) {
				arr.add(new LabelValueBean(message.getName(), message.getMessageId().toString()));
			}
//			String sqlString = "select MessageID,Name from bca_message where Active=1 and CompanyID=? order by Name";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, Integer.parseInt(compId));
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				arr.add(new LabelValueBean(rs.getString("Name"), rs.getString("MessageID")));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getMessage " + ee.toString());
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
		return arr;
	}

	public ArrayList getTaxes(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		int cid = Integer.parseInt(compId);
		ArrayList<InvoiceDto> arr = new ArrayList<>();
		try {
			List<BcaSalestax> bcaSalesTax = bcaSalestaxRepository
					.findByCompany_CompanyIdAndActive(Long.parseLong(compId), 1);
			for (BcaSalestax salestax : bcaSalesTax) {
				InvoiceDto invoice = new InvoiceDto();
				invoice.setSalesTaxID(salestax.getSalesTaxId().toString());
				invoice.setState(salestax.getState());
				invoice.setRate(salestax.getRate());
				arr.add(invoice);
			}
//			String sqlString = "SELECT SalesTaxID,State,Rate FROM bca_salestax WHERE Active=1 and CompanyID=? ORDER BY State";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setInt(1, cid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				InvoiceDto invoice = new InvoiceDto();
//				invoice.setSalesTaxID(rs.getString(1));
//				invoice.setState(rs.getString(2));
//				invoice.setRate(rs.getInt(3));
//				arr.add(invoice);
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getTaxes " + ee.toString());
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
		return arr;
	}

	public String getNewOrderNo(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		int lastOrderNo = 0;
		try {
//            ConfigurationInfo configInfo = new ConfigurationInfo();
			ConfigurationDto configDto = configInfo.getDefaultCongurationDataBySession();
			List<Integer> invoiceStatus = Arrays.asList(0, 2);
			List<Integer> orderNum = bcaInvoiceRepository
					.findOrderNumByCompanyIdAndInvoiceStatus(Long.parseLong(compId), invoiceStatus);

			if (orderNum != null && !orderNum.isEmpty()) {
				lastOrderNo = orderNum.get(0) + 1;
			} else {
				String startNumber = configDto.getStartInvoiceNum();
				lastOrderNo = Integer.parseInt(startNumber.substring(startNumber.indexOf("-") + 1));
			}
//            String sqlString = "SELECT OrderNum FROM bca_invoice WHERE CompanyID=? AND invoiceStatus in (0,2) ORDER BY OrderNum DESC";
//            pstmt = con.prepareStatement(sqlString);
//            pstmt.setString(1, compId);
////            rs = pstmt.executeQuery();
//            if (rs.next()) {
//                lastOrderNo = rs.getInt(1) + 1;
//            } else {
//                String startNumber = configDto.getStartInvoiceNum();
//                lastOrderNo = Integer.parseInt(startNumber.substring(startNumber.indexOf("-") + 1));
//            }
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getTaxes " + ee.toString());

		}
//        finally {
//            try {
//                if (rs != null) {
//                    db.close(rs);
//                }
//                if (pstmt != null) {
//                    db.close(pstmt);
//                }
//                if (con != null) {
//                    db.close(con);
//                }
//            } catch (Exception e) {
//                Loger.log(e.toString());
//            }
//        }
		return String.valueOf(lastOrderNo);
	}

	public String getNewSalesOrderNo(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		int lastOrderNo = 0;
		try {
//			ConfigurationInfo configInfo = new ConfigurationInfo();
			ConfigurationDto configDto = configInfo.getDefaultCongurationDataBySession();
			List<Integer> invoiceStatus = Arrays.asList(0, 2);

			List<Integer> invoiceTypeID = Arrays.asList(7);
			List<BcaInvoice> bcaInvoices = bcaInvoiceRepository
					.findByCompanyIdAndInvoiceStatusAndInvoiceTypeIdOrderBySonumDesc(Long.valueOf(compId),
							invoiceStatus, invoiceTypeID);
			if (bcaInvoices.isEmpty()) {
				String startNumber = configDto.getStartSalesOrderNum();
				lastOrderNo = Integer.parseInt(startNumber.substring(startNumber.indexOf("-") + 1));
			} else {
				BcaInvoice bcaInvoice = bcaInvoices.get(0);
				lastOrderNo = bcaInvoice.getSonum() + 1;
			}
//			String sqlString = "select SONum from bca_invoice  where CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID IN (1,7,9)  order by SONum desc";
//			pstmt = con.prepareStatement(sqlString);
//			pstmt.setString(1, compId);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				lastOrderNo = rs.getInt(1) + 1;
//			} else {
//				String startNumber = configDto.getStartSalesOrderNum();
//				lastOrderNo = Integer.parseInt(startNumber.substring(startNumber.indexOf("-") + 1));
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getNewSalesOrderNo " + ee.toString());

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
		return String.valueOf(lastOrderNo);
	}

	public String getDefaultInvoiceStyleNo(String compId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		long cid = Long.parseLong(compId);
		int invStyle = 0;
		String no = "";
		try {
			invStyle = bcaPreferenceRepository.findInvoiceStyleIDByCompanyId(cid);

//			pstmt = con.prepareStatement("select InvoiceStyleID from bca_preference where CompanyID=?");
//			pstmt.setLong(1, cid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				invStyle = rs.getInt(1);
//			}
		} catch (Exception ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getDefaultInvoiceStyleNo " + ee.toString());
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
		no = String.valueOf(invStyle);
		return no;
	}

	final public boolean invoiceExist(String compId, String orderNo) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int cid = Integer.parseInt(compId);
		boolean exist = false;

		// String sql = "select OrderNum from bca_invoice where OrderNum = ? and
		// CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID in (1,7)";
		// String sql = "select OrderNum from bca_invoice where OrderNum = ? and
		// CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID in (1)";
		try {
			pstmt = con.prepareStatement(
					"select OrderNum from bca_invoice where OrderNum = ? and CompanyID = ? and invoiceStatus in (0,2)");
			pstmt.setString(1, orderNo);
			pstmt.setInt(2, cid);
			rs = pstmt.executeQuery();
			if (rs.next())
				exist = true;
		} catch (SQLException ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getTaxes " + ee.toString());
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
		return exist;
	}

	final public boolean SalesOrderExist(String compId, String orderNo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		con = db.getConnection();
		int cid = Integer.parseInt(compId);
		boolean exist = false;
		try {
			pstmt = con.prepareStatement(
					"select SONum from bca_invoice where SONum = ? and CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID in (7)");
			pstmt.setString(1, orderNo);
			pstmt.setInt(2, cid);
			rs = pstmt.executeQuery();
			if (rs.next())
				exist = true;
		} catch (SQLException ee) {
			Loger.log(2, "Error in  Class InvoiceInfo and  method -getTaxes " + ee.toString());
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
		return exist;
	}

	public boolean Save(String compId, InvoiceDto form, String custId) {
		boolean saveStatus = false;
		boolean shippedLastTime = false;
		try {
			BcaInvoice bcaInvoice = new BcaInvoice();
			bcaInvoice.setOrderNum(Integer.parseInt(form.getOrderNo()));
			bcaInvoice.setRefNum(form.getPoNum());
			Optional<BcaClientvendor> clientVendor = bcaClientvendorRepository.findById(Integer.parseInt(custId));
			if (clientVendor.isPresent()) {
				bcaInvoice.setClientVendor(clientVendor.get());
			}

			if (null != form.getInvoiceStyle()) {
				Optional<BcaInvoicestyle> bcaInvoiceStyle = bcaInvoicestyleRepository
						.findById(Integer.parseInt(form.getInvoiceStyle()));
				bcaInvoice.setInvoiceStyle(bcaInvoiceStyle.get());
			}

			Optional<BcaInvoicetype> invoiceType = bcaInvoicetypeRepository.findById(1);
			if (invoiceType.isPresent())
				bcaInvoice.setInvoiceType(invoiceType.get());
			BcaCompany company = bcaCompanyRepository.findById(Long.parseLong(compId)).orElse(null);
			bcaInvoice.setCompany(company);
			bcaInvoice.setWeight(form.getWeight());
			bcaInvoice.setSubTotal(form.getSubtotal());
			bcaInvoice.setTax(form.getTax());
			bcaInvoice.setSh(form.getShipping());
			bcaInvoice.setBillingAddrId(Integer.valueOf(form.getBsAddressID()));
			bcaInvoice.setShippingAddrId(Integer.valueOf(form.getShAddressID()));
			bcaInvoice.setTotal(form.getTotal());
			bcaInvoice.setAdjustedTotal(form.getAdjustedtotal());
			bcaInvoice.setPaidAmount(0D);
			bcaInvoice.setBalance(form.getAdjustedtotal());
			if (null != form.getVia()) {
				Optional<BcaShipcarrier> shipCarrier = bcaShipcarrierRepository
						.findById(Integer.parseInt(form.getVia()));
				if (shipCarrier.isPresent())
					bcaInvoice.setShipCarrier(shipCarrier.get());
			}
			if (null != form.getRep())
				bcaInvoice.setSalesRepId(Integer.parseInt(form.getRep()));
			if (!"0".equals(form.getMessage()) && null != form.getMessage()) {
				Optional<BcaMessage> message = bcaMessageRepository.findById(Integer.parseInt(form.getMessage()));
				if (message.isPresent())
					bcaInvoice.setMessage(message.get());
			}
			if (null != form.getTerm()) {
				Optional<BcaTerm> term = bcaTermRepository.findById(Integer.parseInt(form.getTerm()));
				if (term.isPresent()) {
					bcaInvoice.setTerm(term.get());
				}
			}

			if (!"0".equals(form.getPayMethod()) && null != form.getPayMethod()) {
				Optional<BcaPaymenttype> paymentType = bcaPaymenttypeRepository
						.findById(Integer.parseInt(form.getPayMethod()));
				if (paymentType.isPresent()) {
					bcaInvoice.setPaymentType(paymentType.get());
				}
			}

			if (null != form.getTaxID()) {
				Optional<BcaSalestax> salesTax = bcaSalestaxRepository.findById(Integer.parseInt(form.getTaxID()));
				if (salesTax.isPresent()) {
					bcaInvoice.setSalesTax(salesTax.get());
				}
			}
			String tax = form.getTaxable();
			if (tax != null && (tax.equals("on") || tax.equals("true"))) {
				bcaInvoice.setTaxable(1);
			} else {
				bcaInvoice.setTaxable(0);
			}
			String shipped = form.getItemShipped();
			if (shipped != null && (shipped.equals("on") || shipped.equals("true"))) {
				bcaInvoice.setShipped(1);
			} else {
				bcaInvoice.setShipped(0);
			}
			bcaInvoice.setMemo(form.getMemo());
			bcaInvoice.setVendorAddrId(-1);
			if (null != form.getBsAddressID())
				bcaInvoice.setBsaddressId(Integer.parseInt(form.getBsAddressID()));
			OffsetDateTime dateConfirmed = DateHelper.string2OffsetDateTime(form.getShipDate());
			bcaInvoice.setDateConfirmed(dateConfirmed);

			OffsetDateTime dateAdded = DateHelper.string2OffsetDateTime(form.getOrderDate());
			bcaInvoice.setDateAdded(dateAdded);
			bcaInvoice.setInvoiceStatus(0);
			bcaInvoice.setEstNum(0);
			bcaInvoice.setOrderType(7);

			String paid = form.getPaid();
			if (paid != null && (paid.equals("on") || paid.equals("true"))) {
				bcaInvoice.setIsPaymentCompleted(true);
			} else {
				bcaInvoice.setIsPaymentCompleted(false);
			}
			if (form.getServiceID() == 0) {
				bcaInvoice.setServiceId(0);
			} else {
				bcaInvoice.setServiceId(form.getServiceID());

			}

			bcaInvoice.setIsInvoice(1);
			bcaInvoice.setIsSalestype(1);
			String isPending = form.getIsPending();
			if (isPending != null && (isPending.equals("on") || isPending.equals("true"))) {
				bcaInvoice.setIsPending(1);
			} else {
				bcaInvoice.setIsPending(0);
			}
			BcaInvoice invoice = bcaInvoiceRepository.save(bcaInvoice);
			if (null != invoice) {
				receivableLIst.insertIntoBillingStatement(invoice.getInvoiceId());
				saveStatus = true;
			}

			if (null != invoice) {
				Map<Integer, Integer> oldInvData = new HashMap<>();
				List<BcaCart> cart = bcaCartRepository.findByInvoiceIdAndCompanyId(invoice.getInvoiceId(),
						Long.parseLong(compId));
				if (!cart.isEmpty()) {
					if (null != cart.get(0).getInventory())
						oldInvData.put(cart.get(0).getInventory().getInventoryId(), cart.get(0).getQty());
				}
				bcaCartRepository.deleteByInvoice_InvoiceIdAndCompany_CompanyId(invoice.getInvoiceId(),
						Long.parseLong(compId));

				if (invoice.getShipped() == 1)
					shippedLastTime = true;

				if (shippedLastTime) {
					for (Integer key : oldInvData.keySet()) {
						Optional<BcaIteminventory> itemInventory = bcaIteminventoryRepository.findById(key);
						if (itemInventory.isPresent()) {
							BcaIteminventory item = itemInventory.get();
							int expectedQty = item.getExpectedQty();
							item.setExpectedQty(expectedQty + oldInvData.get(key));
							bcaIteminventoryRepository.save(item);
						}
					}
				}
				/* Add Item to Cart */
				AddItem(invoice.getInvoiceId(), Integer.valueOf(compId), form);
			}

		} catch (Exception ee) {
			ee.printStackTrace();
			Loger.log("Exception" + ee.toString());

		}
		return saveStatus;
	}

//	public boolean Save(String compId, InvoiceDto form, String custId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null, pstmt2 = null;
//		boolean saveStatus = false;
//		try {
//			pstmt = con.prepareStatement("SELECT MAX(InvoiceID) FROM bca_invoice");
//			rs = pstmt.executeQuery();
//			/* Insert into invoice */
//			if (rs.next()) {
//				int invoiceID = rs.getInt(1) + 1;
//				pstmt2 = con.prepareStatement("INSERT INTO bca_invoice (InvoiceID) values (?)");
//				pstmt2.setInt(1, invoiceID);
//				pstmt2.executeUpdate();
//				pstmt2.close();
//				saveStatus = Update(compId, form, invoiceID, custId);
//				ReceivableLIst rl = new ReceivableListImpl();
//				rl.insertIntoBillingStatement(invoiceID);
//			}
//		} catch (SQLException ee) {
//			Loger.log("Exception" + ee.toString());
//
//		} finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
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
//		return saveStatus;
//	}

	public boolean SaveSalesOrder(String compId, InvoiceDto form, int salesOrderType) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		int invoiceID = 0;
		String invStr = "select max(InvoiceID) from bca_invoice";
		boolean saveStatus = false;
		try {
			pstmt = con.prepareStatement(invStr);
			rs = pstmt.executeQuery();
			/* Insert into invoice */
			if (rs.next()) {
				invoiceID = rs.getInt(1) + 1;
				String insertInv = "insert into bca_invoice (InvoiceID)values (?)";
				pstmt2 = con.prepareStatement(insertInv);
				pstmt2.setInt(1, invoiceID);
				pstmt2.executeUpdate();
				pstmt2.close();
				saveStatus = SalesUpdate(compId, form, salesOrderType, invoiceID);
			}
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (pstmt2 != null) {
					db.close(pstmt2);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return saveStatus;
	}

	public void AddItem(int invoiceID, int cid, InvoiceDto form) {

		if (form.getItem() == null || form.getItem().isEmpty())
			return;
		String invIDs[] = form.getItem().split(";");
		String invCodes[] = form.getCode().split(";");
		String invNames[] = form.getDesc().split(";");
		String invQtys[] = form.getQty().split(";");
		String invUWeights[] = form.getUnitWeight().split(";");
		String invUPrices[] = form.getUprice().split(";");
		String invIsTaxables[] = form.getIsTaxable().split(";");
		String invItemIDs[] = form.getItemTypeID().split(";");
		String invItemOrders[] = form.getItemOrder().split(";");
//		String itemCategoryId[] = form.getCategory().split(";");
		boolean shippedNow = false;
		if (form.getItemShipped() != null && form.getItemShipped().equals("on")) {
			shippedNow = true;
		}

		try {
			for (int i = 0; i < form.getSize(); i++) {
				int inventoryID = Integer.parseInt(invIDs[i]);
				String itemCode = invCodes[i];
				String itemName = invNames[i];
				String qty = (invQtys[i] != null && invQtys[i].length() > 0) ? invQtys[i] : "0";
				String uweight = (invUWeights[i] != null && invUWeights[i].length() > 0) ? invUWeights[i] : "0.0";
				String uprice = (invUPrices[i] != null && invUPrices[i].length() > 0) ? invUPrices[i] : "0.0";
				String taxable = (invIsTaxables[i] != null && invIsTaxables[i].length() > 0) ? invIsTaxables[i] : "0";
				String taxable1;
				if (taxable.equalsIgnoreCase("Yes")) {
					taxable1 = "1";
				} else {
					taxable1 = "0";
				}
				String itmTypeID = (invItemIDs[i] != null && invItemIDs[i].length() > 0) ? invItemIDs[i] : "0";
				String itmOrder = invItemOrders[i];
				BcaCart bcaCart = new BcaCart();
				Optional<BcaIteminventory> itemInventory = bcaIteminventoryRepository.findById(inventoryID);
				if (itemInventory.isPresent())
					bcaCart.setInventory(itemInventory.get());
				Optional<BcaCompany> company = bcaCompanyRepository.findById(new Long(cid));
				if (company.isPresent())
					bcaCart.setCompany(company.get());

				Optional<BcaInvoice> invoice = bcaInvoiceRepository.findById(invoiceID);
				if (invoice.isPresent())
					bcaCart.setInvoice(invoice.get());

				bcaCart.setInventoryCode(itemCode);
				bcaCart.setInventoryName(itemName);
				bcaCart.setQty(Integer.parseInt(qty));
				bcaCart.setUnitWeight(Double.parseDouble(uweight));
				bcaCart.setWeight(0.0);
				bcaCart.setUnitPrice(Double.parseDouble(truncate(uprice)));
				bcaCart.setTaxable(Integer.parseInt(taxable1));
				bcaCart.setItemTypeId(Integer.parseInt(itmTypeID));
				bcaCart.setItemOrder(Integer.parseInt(itmOrder));
				Date dateAdded = new Date();
				bcaCart.setDateAdded(DateHelper.convertDateToOffsetDateTime(dateAdded));
//				bcaCart.setCategoryId(Integer.valueOf(itemCategoryId[i]));

				BcaCart cart = bcaCartRepository.save(bcaCart);
				if (null != cart && shippedNow) {
					Optional<BcaIteminventory> inventory = bcaIteminventoryRepository.findById(inventoryID);
					if (itemInventory.isPresent()) {
						BcaIteminventory item = inventory.get();
						int expectedQty = item.getExpectedQty();
						item.setExpectedQty(expectedQty - Integer.parseInt(qty));
						bcaIteminventoryRepository.save(item);
					}
				}

			}

		} catch (Exception ee) {
			Loger.log("Exception" + ee.toString());
		}
	}

//	public void AddItem(int invoiceID, int cid, InvoiceDto form) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null, pstmt2 = null;
//
//		if (form.getItem() == null || form.getItem().isEmpty())
//			return;
//		String invIDs[] = form.getItem().split(";");
//		String invCodes[] = form.getCode().split(";");
//		String invNames[] = form.getDesc().split(";");
//		String invQtys[] = form.getQty().split(";");
//		String invUWeights[] = form.getUnitWeight().split(";");
//		String invUPrices[] = form.getUprice().split(";");
//		String invIsTaxables[] = form.getIsTaxable().split(";");
//		String invItemIDs[] = form.getItemTypeID().split(";");
//		String invItemOrders[] = form.getItemOrder().split(";");
//		boolean shippedNow = false;
//		if (form.getItemShipped() != null && form.getItemShipped().equals("on")) {
//			shippedNow = true;
//		}
//		try {
//			for (int i = 0; i < form.getSize(); i++) {
//				int inventoryID = Integer.parseInt(invIDs[i]);
//				String itemCode = invCodes[i];
//				String itemName = invNames[i];
//				String qty = (invQtys[i] != null && invQtys[i].length() > 0) ? invQtys[i] : "0";
//				String uweight = (invUWeights[i] != null && invUWeights[i].length() > 0) ? invUWeights[i] : "0.0";
//				String uprice = (invUPrices[i] != null && invUPrices[i].length() > 0) ? invUPrices[i] : "0.0";
//				String taxable = (invIsTaxables[i] != null && invIsTaxables[i].length() > 0) ? invIsTaxables[i] : "0";
//				String taxable1;
//				if (taxable.equalsIgnoreCase("Yes")) {
//					taxable1 = "1";
//				} else {
//					taxable1 = "0";
//				}
//				String itmTypeID = (invItemIDs[i] != null && invItemIDs[i].length() > 0) ? invItemIDs[i] : "0";
//				String itmOrder = invItemOrders[i];
//
//				pstmt2 = con.prepareStatement("SELECT MAX(CartID) FROM bca_cart");
//				rs = pstmt2.executeQuery();
//				/* Insert into invoice */
//				if (rs.next()) {
//					int cartID = rs.getInt(1) + 1;
//					String insertItem = "INSERT INTO bca_cart (InventoryID,InvoiceID,CompanyID,InventoryCode,InventoryName,Qty,"
//							+ " UnitWeight,Weight,UnitPrice,Taxable,ItemTypeID,ItemOrder,CartID)"
//							+ " VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,? )";
//					pstmt = con.prepareStatement(insertItem);
//					pstmt.setInt(1, inventoryID);
//					pstmt.setInt(2, invoiceID);
//					pstmt.setInt(3, cid);
//					pstmt.setString(4, itemCode);
//					pstmt.setString(5, itemName);
//					pstmt.setInt(6, Integer.parseInt(qty));
//					pstmt.setDouble(7, Double.parseDouble(uweight));
//					pstmt.setDouble(8, 0.0);
//					pstmt.setDouble(9, Double.parseDouble(truncate(uprice)));
//					pstmt.setInt(10, Integer.parseInt(taxable1));
//					pstmt.setInt(11, Integer.parseInt(itmTypeID));
//					pstmt.setInt(12, Integer.parseInt(itmOrder));
//					pstmt.setInt(13, cartID);
//					int updatedRows = pstmt.executeUpdate();
//					if (updatedRows > 0 && shippedNow) {
//						pstmt = con.prepareStatement(
//								"UPDATE bca_iteminventory SET ExpectedQty=ExpectedQty-? WHERE InventoryID=?");
//						pstmt.setInt(1, Integer.parseInt(qty));
//						pstmt.setInt(2, inventoryID);
//						updatedRows = pstmt.executeUpdate();
//					}
//				}
//			}
//		} catch (SQLException ee) {
//			Loger.log("Exception" + ee.toString());
//
//		} finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
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
//	}

	public boolean SalesUpdate(String compId, InvoiceDto form, int salesOrderType, int invoiceID) {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt3 = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		CustomerInfo cinfo = new CustomerInfo();
		int cid = Integer.parseInt(compId);
		boolean saveStatus = false;
		try {
			String updateStr = "update bca_invoice set  SONum =?,RefNum =?,"
					+ "ClientVendorID =? ,BSAddressID =? ,InvoiceStyleID =? ,InvoiceTypeID =? ,"
					+ "CompanyID =? ,Weight =? ,Subtotal =? ,Tax =? ,SH = ?  ,Total = ? ,AdjustedTotal = ?  ,"
					+ "PaidAmount = ?  ,Balance = ? ,ShipCarrierID = ? ,SalesRepID =? ,MessageID = ? ,TermID =? ,"
					+ "PaymentTypeID =? ,SalesTaxID =?  ,Taxable =? ,Shipped =? , Memo = ? , VendorAddrID = ? , "
					+ "ShippingAddrID = ? ,DateConfirmed = ?  ,DateAdded =? ,invoiceStatus = ? ,EstNum=0 ,OrderType = 7 , "
					+ "IsPaymentCompleted=? , ServiceID=? , PONum=?,IsInvoice=?,IsSalestype=?,isPending=?  where InvoiceID =? ";

			pstmt1 = con.prepareStatement(updateStr);
			pstmt1.setString(1, form.getOrderNo());
			pstmt1.setString(2, form.getPoNum());

			pstmt1.setString(3, form.getCustID());
			pstmt1.setString(4, form.getBsAddressID());
			pstmt1.setString(5, form.getInvoiceStyle());
			pstmt1.setInt(6, salesOrderType); // Sales Order Type id
			pstmt1.setString(7, form.getCompanyID());
			pstmt1.setDouble(8, form.getWeight());
			pstmt1.setDouble(9, form.getSubtotal());
			pstmt1.setDouble(10, form.getTax());

			pstmt1.setDouble(11, form.getShipping());
			pstmt1.setDouble(12, form.getTotal());
			pstmt1.setDouble(13, form.getAdjustedtotal());
			pstmt1.setDouble(14, 0);
			pstmt1.setDouble(15, form.getAdjustedtotal());
			pstmt1.setString(16, form.getVia());
			pstmt1.setString(17, form.getRep());
//            pstmt1.setString(18, form.getMessage());
			pstmt1.setString(18, "0".equals(form.getMessage()) ? null : form.getMessage());
			pstmt1.setString(19, form.getTerm());
//            pstmt1.setString(20, form.getPayMethod());
			pstmt1.setString(20, "0".equals(form.getPayMethod()) ? null : form.getPayMethod());
			pstmt1.setString(21, form.getTaxID());

			String tax = form.getTaxable();
			if (tax != null && (tax.equals("on") || tax.equals("true"))) {
				pstmt1.setInt(22, 1);
			} else {
				pstmt1.setInt(22, 0);
			}

			String shipped = form.getItemShipped();
			if (shipped != null && (shipped.equals("on") || shipped.equals("true"))) {
				pstmt1.setInt(23, 1);
			} else {
				pstmt1.setInt(23, 0);
			}

			pstmt1.setString(24, form.getMemo());
			pstmt1.setInt(25, -1);
			pstmt1.setInt(26, -1);
			pstmt1.setDate(27, (form.getShipDate() == null || form.getShipDate().isEmpty()) ? cinfo.string2date("now()")
					: cinfo.string2date(form.getShipDate()));
			pstmt1.setDate(28,
					(form.getOrderDate() == null || form.getOrderDate().isEmpty()) ? cinfo.string2date("now()")
							: cinfo.string2date(form.getOrderDate()));

			pstmt1.setInt(29, 0);
			String paid = form.getPaid();
			if (paid != null && (paid.equals("on") || paid.equals("true"))) {
				pstmt1.setInt(30, 1);
			} else {
				pstmt1.setInt(30, 0);
			}

			if (form.getServiceName() == null || form.getServiceName().equals("")) {
				pstmt1.setInt(31, 0);
			} else {
				pstmt1.setInt(31, getServiceID(form.getServiceName()));
			}
			pstmt1.setInt(32, 0);
			pstmt1.setString(33, form.getIsInvoice());
			pstmt1.setString(34, form.getIsSalestype());
			String isPending = form.getIsPending();
			if (isPending != null && (isPending.equals("on") || isPending.equals("true"))) {
				pstmt1.setInt(35, 1);
			} else {
				pstmt1.setInt(35, 0);
			}
			pstmt1.setInt(36, invoiceID);
			int rows = pstmt1.executeUpdate();
			if (rows > 0) {
				/* Delete Item from Cart */
				String cartDelete = "delete from bca_cart  where  InvoiceID = ? and CompanyID = ?";
				pstmt3 = con.prepareStatement(cartDelete);
				pstmt3.setInt(1, invoiceID);
				pstmt3.setInt(2, cid);
				pstmt3.executeUpdate();

				/* Add Item to Cart */
				AddItem(invoiceID, cid, form);
				saveStatus = true;
			}
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

		} finally {
			try {
				if (pstmt1 != null) {
					db.close(pstmt1);
				}
				if (pstmt3 != null) {
					db.close(pstmt3);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return saveStatus;
	}

	public boolean makeSelectedOrderAsInvoice(int invoiceID, int orderNo) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		boolean updateStatus = false;
		try {
			String updateStr = "update bca_invoice set OrderNum=?, RefNum=?, PONum=0, SONum=0, EstNum=0,"
					+ "InvoiceTypeID=1, IsInvoice=1, OrderType=7 WHERE InvoiceID=?";
			pstmt = con.prepareStatement(updateStr);
			pstmt.setInt(1, orderNo);
			pstmt.setInt(2, orderNo);
			pstmt.setInt(3, invoiceID);
			if (pstmt.executeUpdate() > 0) {
				updateStatus = true;
			}
			updateStatus = true;
		} catch (SQLException ee) {

			Loger.log("Exception" + ee.toString());
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
		return updateStatus;
	}

	public boolean Update(String compId, InvoiceDto form, int invoiceID, String custID) {
		int cid = Integer.parseInt(compId);
		boolean updateStatus = false;
		try {
			boolean shippedLastTime = false;
			Optional<BcaInvoice> bInvoice = bcaInvoiceRepository.findByInvoiceIdAndCompany_CompanyId(invoiceID,
					Long.parseLong(compId));
			if (bInvoice.isPresent()) {
				if (bInvoice.get().getShipped() == 1)
					shippedLastTime = true;
			}

			BcaInvoice bcaInvoice = bcaInvoiceRepository.findById(invoiceID)
					.orElseThrow(() -> new IllegalArgumentException("Invoice Not Present"));

			bcaInvoice.setOrderNum(Integer.parseInt(form.getOrderNo()));
			bcaInvoice.setRefNum(form.getPoNum());
			if (null != custID) {
				Optional<BcaClientvendor> clientVendor = bcaClientvendorRepository.findById(Integer.parseInt(custID));
				if (clientVendor.isPresent()) {
					bcaInvoice.setClientVendor(clientVendor.get());
				}
			}
			bcaInvoice.setBsaddressId(Integer.parseInt(form.getBsAddressID()));
			if (null != form.getInvoiceStyle()) {
				Optional<BcaInvoicestyle> bcaInvoiceStyle = bcaInvoicestyleRepository
						.findById(Integer.parseInt(form.getInvoiceStyle()));
				bcaInvoice.setInvoiceStyle(bcaInvoiceStyle.get());
			}

			Optional<BcaInvoicetype> invoiceType = bcaInvoicetypeRepository.findById(1);
			if (invoiceType.isPresent())
				bcaInvoice.setInvoiceType(invoiceType.get());
			BcaCompany company = bcaCompanyRepository.findById(Long.parseLong(compId)).orElse(null);
			bcaInvoice.setCompany(company);
			bcaInvoice.setWeight(form.getWeight());
			bcaInvoice.setSubTotal(form.getSubtotal());
			bcaInvoice.setTax(form.getTax());
			bcaInvoice.setSh(form.getShipping());
			bcaInvoice.setTotal(form.getTotal());
			bcaInvoice.setAdjustedTotal(form.getAdjustedtotal());
			bcaInvoice.setPaidAmount(0D);
			bcaInvoice.setBalance(form.getAdjustedtotal());
			if (null != form.getVia()) {
				Optional<BcaShipcarrier> shipCarrier = bcaShipcarrierRepository
						.findById(Integer.parseInt(form.getVia()));
				if (shipCarrier.isPresent())
					bcaInvoice.setShipCarrier(shipCarrier.get());
			}
			if (null != form.getRep())
				bcaInvoice.setSalesRepId(Integer.parseInt(form.getRep()));
			if (!"0".equals(form.getMessage())) {
				Optional<BcaMessage> message = bcaMessageRepository.findById(Integer.parseInt(form.getMessage()));
				if (message.isPresent())
					bcaInvoice.setMessage(message.get());
			}
			if (null != form.getTerm()) {
				Optional<BcaTerm> term = bcaTermRepository.findById(Integer.parseInt(form.getTerm()));
				if (term.isPresent()) {
					bcaInvoice.setTerm(term.get());
				}
			}

			if (!"0".equals(form.getPayMethod())) {
				Optional<BcaPaymenttype> paymentType = bcaPaymenttypeRepository
						.findById(Integer.parseInt(form.getPayMethod()));
				if (paymentType.isPresent()) {
					bcaInvoice.setPaymentType(paymentType.get());
				}
			}

			if (null != form.getTaxID()) {
				Optional<BcaSalestax> salesTax = bcaSalestaxRepository.findById(Integer.parseInt(form.getTaxID()));
				if (salesTax.isPresent()) {
					bcaInvoice.setSalesTax(salesTax.get());
				}
			}
			String tax = form.getTaxable();
			if (tax != null && (tax.equals("on") || tax.equals("true"))) {
				bcaInvoice.setTaxable(1);
			} else {
				bcaInvoice.setTaxable(0);
			}
			String shipped = form.getItemShipped();
			if (shipped != null && (shipped.equals("on") || shipped.equals("true"))) {
				bcaInvoice.setShipped(1);
			} else {
				bcaInvoice.setShipped(0);
			}
			bcaInvoice.setMemo(form.getMemo());
			bcaInvoice.setVendorAddrId(-1);
			if (null != form.getBsAddressID())
				bcaInvoice.setBsaddressId(Integer.parseInt(form.getBsAddressID()));
			OffsetDateTime dateConfirmed = DateHelper.string2OffsetDateTime(form.getShipDate());
			bcaInvoice.setDateConfirmed(dateConfirmed);

			OffsetDateTime dateAdded = DateHelper.string2OffsetDateTime(form.getOrderDate());
			bcaInvoice.setDateAdded(dateAdded);
			bcaInvoice.setInvoiceStatus(0);
			bcaInvoice.setEstNum(0);
			bcaInvoice.setOrderType(7);

			String paid = form.getPaid();
			if (paid != null && (paid.equals("on") || paid.equals("true"))) {
				bcaInvoice.setIsPaymentCompleted(true);
			} else {
				bcaInvoice.setIsPaymentCompleted(false);
			}
			if (form.getServiceID() == 0) {
				bcaInvoice.setServiceId(0);
			} else {
				bcaInvoice.setServiceId(form.getServiceID());

			}

			bcaInvoice.setIsInvoice(1);
			bcaInvoice.setIsSalestype(1);
			String isPending = form.getIsPending();
			if (isPending != null && (isPending.equals("on") || isPending.equals("true"))) {
				bcaInvoice.setIsPending(1);
			} else {
				bcaInvoice.setIsPending(0);
			}
			BcaInvoice invoice = bcaInvoiceRepository.save(bcaInvoice);
			if (null != invoice) {
				Map<Integer, Integer> oldInvData = new HashMap<>();
				List<BcaCart> cart = bcaCartRepository.findByInvoiceIdAndCompanyId(invoice.getInvoiceId(),
						Long.parseLong(compId));
				if (!cart.isEmpty()) {
					if (null != cart.get(0).getInventory())
						oldInvData.put(cart.get(0).getInventory().getInventoryId(), cart.get(0).getQty());
				}
				bcaCartRepository.deleteByInvoice_InvoiceIdAndCompany_CompanyId(invoice.getInvoiceId(),
						Long.parseLong(compId));
				if (shippedLastTime) {
					for (Integer key : oldInvData.keySet()) {
						Optional<BcaIteminventory> itemInventory = bcaIteminventoryRepository.findById(key);
						if (itemInventory.isPresent()) {
							BcaIteminventory item = itemInventory.get();
							int expectedQty = item.getExpectedQty();
							item.setExpectedQty(expectedQty + oldInvData.get(key));
							bcaIteminventoryRepository.save(item);
						}
					}
				}
				/* Add Item to Cart */
				AddItem(invoiceID, cid, form);
			}

			updateStatus = true;
		} catch (Exception ee) {
			ee.printStackTrace();
			Loger.log("Exception" + ee.toString());
		}

		return updateStatus;
	}
//	public boolean Update(String compId, InvoiceDto form, int invoiceID, String custID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt5 = null;
//		int cid = Integer.parseInt(compId);
//		CustomerInfo cinfo = new CustomerInfo();
//		boolean updateStatus = false;
//		try {
//			boolean shippedLastTime = false;
//			pstmt = con.prepareStatement(
//					"SELECT Shipped FROM bca_invoice WHERE InvoiceID = " + invoiceID + " and CompanyID = " + cid);
//			rs = pstmt.executeQuery();
//			if (rs.next() && rs.getInt("Shipped") == 1) {
//				shippedLastTime = true;
//			}
//			String updateStr = "update bca_invoice set  OrderNum =?,RefNum =?,"
//					+ "ClientVendorID =? ,BSAddressID =? ,InvoiceStyleID =? ,InvoiceTypeID =? ,"
//					+ "CompanyID =? ,Weight =? ,Subtotal =? ,Tax =? ,SH = ?  ,Total = ? ,AdjustedTotal = ?  ,"
//					+ "PaidAmount = ?  ,Balance = ? ,ShipCarrierID = ? ,SalesRepID =? ,MessageID = ? ,TermID =? ,"
//					+ "PaymentTypeID =? ,SalesTaxID =?  ,Taxable =? ,Shipped =? , Memo = ? , VendorAddrID = ? , "
//					+ "BSAddressID = ? ,DateConfirmed = ?  ,DateAdded =? ,invoiceStatus = ? ,EstNum=0 ,OrderType = 7 , "
//					+ "IsPaymentCompleted=? , ServiceID=? ,IsInvoice=?,IsSalestype=?,isPending=? where InvoiceID =? ";
//			pstmt1 = con.prepareStatement(updateStr);
//
//			pstmt1.setString(1, form.getOrderNo());
//			pstmt1.setString(2, form.getPoNum());
//			/* pstmt1.setString(2, ""); */
//			pstmt1.setString(3, custID);
//			pstmt1.setString(4, form.getBsAddressID());
//			pstmt1.setString(5, form.getInvoiceStyle());
//			/* pstmt1.setString(6, form.getInvoiceType()); */
//			pstmt1.setString(6, "1");
//			pstmt1.setString(7, compId);
//			pstmt1.setDouble(8, form.getWeight());
//			pstmt1.setDouble(9, form.getSubtotal());
//			pstmt1.setDouble(10, form.getTax());
//
//			pstmt1.setDouble(11, form.getShipping());
//			pstmt1.setDouble(12, form.getTotal());
//			pstmt1.setDouble(13, form.getAdjustedtotal());
//			pstmt1.setDouble(14, 0);
//			pstmt1.setDouble(15, form.getAdjustedtotal());
//			pstmt1.setString(16, form.getVia());
//			pstmt1.setString(17, form.getRep());
////            pstmt1.setString(18, form.getMessage());
//			pstmt1.setString(18, "0".equals(form.getMessage()) ? null : form.getMessage());
//			pstmt1.setString(19, form.getTerm());
////            pstmt1.setString(20, form.getPayMethod());
//			pstmt1.setString(20, "0".equals(form.getPayMethod()) ? null : form.getPayMethod());
//			pstmt1.setString(21, form.getTaxID());
//
//			String tax = form.getTaxable();
//			if (tax != null && (tax.equals("on") || tax.equals("true"))) {
//				pstmt1.setInt(22, 1);
//			} else {
//				pstmt1.setInt(22, 0);
//			}
//
//			String shipped = form.getItemShipped();
//			if (shipped != null && (shipped.equals("on") || shipped.equals("true"))) {
//				pstmt1.setInt(23, 1);
//			} else {
//				pstmt1.setInt(23, 0);
//			}
//
//			pstmt1.setString(24, form.getMemo());
//			pstmt1.setInt(25, -1);
//			pstmt1.setString(26, form.getBsAddressID());
//			pstmt1.setDate(27, (form.getShipDate() == null || form.getShipDate().isEmpty()) ? cinfo.string2date("now()")
//					: cinfo.string2date(form.getShipDate()));
//			pstmt1.setDate(28,
//					(form.getOrderDate() == null || form.getOrderDate().isEmpty()) ? cinfo.string2date("now()")
//							: cinfo.string2date(form.getOrderDate()));
//			pstmt1.setInt(29, 0);
//			String paid = form.getPaid();
//			if (paid != null && (paid.equals("on") || paid.equals("true"))) {
//				pstmt1.setInt(30, 1);
//			} else {
//				pstmt1.setInt(30, 0);
//			}
//			if (form.getServiceID() == 0) {
//				pstmt1.setInt(31, 0);
//			} else {
//				pstmt1.setInt(31, form.getServiceID());
//			}
//			pstmt1.setString(32, "1");
//			pstmt1.setString(33, "1");
//			String isPending = form.getIsPending();
//			if (isPending != null && (isPending.equals("on") || isPending.equals("true"))) {
//				pstmt1.setInt(34, 1);// pending
//			} else {
//				pstmt1.setInt(34, 0);
//			}
//			pstmt1.setInt(35, invoiceID); // set pending value order
//
//			int rows = pstmt1.executeUpdate();
//			if (rows > 0) {
//				/* Delete Item from Cart */
//				Map<Integer, Integer> oldInvData = new HashMap<>();
//				pstmt2 = con.prepareStatement(
//						"SELECT * FROM bca_cart WHERE InvoiceID = " + invoiceID + " and CompanyID = " + cid);
//				rs = pstmt2.executeQuery();
//				if (rs.next()) {
//					oldInvData.put(rs.getInt("InventoryID"), rs.getInt("Qty"));
//				}
//				/* Delete Item from Cart */
//				pstmt3 = con.prepareStatement(
//						"DELETE FROM bca_cart WHERE InvoiceID = " + invoiceID + " and CompanyID=" + cid);
//				int updatedRows = pstmt3.executeUpdate();
//				if (updatedRows > 0 && shippedLastTime) {
//					con.setAutoCommit(false);
//					pstmt4 = con.prepareStatement(
//							"UPDATE bca_iteminventory SET ExpectedQty=ExpectedQty+? WHERE InventoryID=?");
//					for (Integer key : oldInvData.keySet()) {
//						pstmt4.setInt(1, oldInvData.get(key));
//						pstmt4.setInt(2, key);
//						pstmt4.addBatch();
//					}
//					pstmt4.executeBatch();
//					con.commit();
//				}
//
//				/* Add Item to Cart */
//				AddItem(invoiceID, cid, form);
//			}
//			updateStatus = true;
//		} catch (SQLException ee) {
//
//			Loger.log("Exception" + ee.toString());
//		} finally {
//			try {
//				if (pstmt1 != null) {
//					db.close(pstmt1);
//				}
//				if (pstmt2 != null) {
//					db.close(pstmt2);
//				}
//				if (pstmt3 != null) {
//					db.close(pstmt3);
//				}
//				if (pstmt4 != null) {
//					db.close(pstmt4);
//				}
//				if (pstmt5 != null) {
//					db.close(pstmt5);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
//		return updateStatus;
//	}

	public int getInvoiceNo(String compId, String no) {
		int invoiceID = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		if (db == null)
			return 0;
		con = db.getConnection();
		int cid = Integer.parseInt(compId);
		if (con == null)
			return 0;
		try {
			String sql = "SELECT InvoiceID from bca_invoice where OrderNum=? and CompanyID=? and invoiceStatus IN (0,2)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, no);
			pstmt.setInt(2, cid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				invoiceID = rs.getInt(1);
			}
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

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
		return invoiceID;
	}

	public int getSalesInvoiceNo(String compId, String no) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int cid = Integer.parseInt(compId);
		int invoiceID = 0;
		try {
			String sql = "SELECT InvoiceID from bca_invoice where SONum=? and CompanyID = ? and invoiceStatus IN (0,2) and InvoiceTypeID IN (7)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, no);
			pstmt.setInt(2, cid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				invoiceID = rs.getInt(1);
			}
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

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
		return invoiceID;
	}

	public void Delete(String compId, String orderNo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		if (db == null)
			return;
		con = db.getConnection();
		if (con == null)
			return;
		try {
			pstmt = con.prepareStatement("update bca_invoice set invoiceStatus=? where OrderNum =? and CompanyID=?");
			pstmt.setInt(1, 1);
			pstmt.setString(2, orderNo);
			pstmt.setString(3, compId);
			pstmt.executeUpdate();
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

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
	}

	public void DeleteOrder(String compId, String orderNo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		if (db == null)
			return;
		con = db.getConnection();
		if (con == null)
			return;
		try {
			pstmt = con.prepareStatement("update bca_invoice set invoiceStatus=? where SONum =? and CompanyID=?");
			pstmt.setInt(1, 1);
			pstmt.setString(2, orderNo);
			pstmt.setString(3, compId);
			pstmt.executeUpdate();
		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

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
	}

	public void getBillShipAddr(int custID, UpdateInvoiceDto form) {
		Connection con = null;
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null;
		PreparedStatement pstmt3 = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null, rs1 = null, rs2 = null;
		ResultSet rs3 = null;
		if (db == null)
			return;
		con = db.getConnection();
		if (con == null)
			return;
		try {
			/* For billing Address Info */
			String billAddr = "select * from bca_bsaddress where ClientVendorID = ? and AddressType = 1 and Status in ('N','U')";
			pstmt = con.prepareStatement(billAddr);
			pstmt.setInt(1, custID);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String id = rs.getString("Country");
				String stid = rs.getString("State");
				form.setBscname(rs.getString("Name"));
				form.setBsfirstName(rs.getString("FirstName"));
				form.setBslastName(rs.getString("LastName"));
				form.setBsaddress1(rs.getString("Address1"));
				form.setBsaddress2(rs.getString("Address2"));
				form.setBscity(rs.getString("City"));
				form.setBszipCode(rs.getString("ZipCode"));
				form.setBsprovince(rs.getString("Province"));
				;

				pstmt2 = con.prepareStatement("select CountryID from country where CountryName=?");
				pstmt2.setString(1, id);
				rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					form.setBscountry(rs2.getString("CountryID"));

					pstmt3 = con.prepareStatement("select StateID from state where StateName=? and CountryID=?");
					pstmt3.setString(1, stid);
					pstmt3.setString(2, id);
					rs3 = pstmt3.executeQuery();
					if (rs3.next()) {
						form.setBsstate(rs3.getString("StateID"));
					}
				}

			}

			/* For Shipping Address Info */
			String shipAddr = "select * from bca_bsaddress where ClientVendorID = ? and AddressType = 0 and Status in ('N','U')";
			pstmt1 = con.prepareStatement(shipAddr);
			pstmt1.setInt(1, custID);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				form.setShcname(rs1.getString("Name"));
				form.setShfirstName(rs1.getString("FirstName"));
				form.setShlastName(rs1.getString("LastName"));
				form.setShaddress1(rs1.getString("Address1"));
				form.setShaddress2(rs1.getString("Address2"));
				form.setShcity(rs1.getString("City"));
				form.setShzipCode(rs1.getString("ZipCode"));

				form.setShprovince(rs1.getString("Province"));
				;
			}

		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

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
				if (rs2 != null) {
					db.close(rs2);
				}
				if (pstmt2 != null) {
					db.close(pstmt2);
				}
				if (rs3 != null) {
					db.close(rs3);
				}
				if (pstmt3 != null) {
					db.close(pstmt3);
				}

				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
	}

	public void getCountry(HttpServletRequest request, String country, UpdateInvoiceDto form) {
		Connection con = null;
		PreparedStatement pstmt1 = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs1 = null;

		if (db == null)
			return;
		con = db.getConnection();
		if (con == null)
			return;
		try {
			String str1 = form.getCountry();
			Loger.log("CC" + str1);
			pstmt1 = con.prepareStatement("select CountryID from country where CountryName='" + str1 + "'");
			rs1 = pstmt1.executeQuery();
			Loger.log("###");

			String str = "";
			if (rs1.next()) {
				Loger.log("!!!");
				str = rs1.getString("CountryID");
				Loger.log("EEE");
				Loger.log("Country" + str);
			}
			form.setCountry(str);

		} catch (SQLException ee) {
			Loger.log("Exception" + ee.toString());

		} finally {
			try {
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
	}

	public ArrayList SearchCustomerDELETE(String compId, String cvId, HttpServletRequest request,
			CustomerDto customer) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null, rs3 = null, rs1 = null, rs2 = null, rs22 = null, rs12 = null, rs13 = null;
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt12 = null,
				pstmt13 = null;
		ArrayList<CustomerDto> customerList = new ArrayList<>();
		try {
			StringBuffer sqlString = new StringBuffer();
			sqlString.append(
					" SELECT distinct cv.ClientVendorID,cv.Name,cv.FirstName, cv.LastName,cv.Address1, cv.Address2,cv.City,");
			sqlString.append(
					" cv.State, cv.Province, cv.Country,cv.ZipCode, cv.Phone, cv.CellPhone,cv.Fax, cv.Email,cv.HomePage,");
			sqlString.append(
					"cv.CustomerTitle,cv.ResellerTaxID,cv.VendorOpenDebit,cv.VendorAllowedCredit,cv.Detail,cv.Taxable,");
			sqlString.append("cv.CVTypeID, cv.cvcategoryid, date_format(cv.DateAdded,'%m-%d-%Y') As DateAdded,");
			sqlString.append(
					"cc.CardNumber ,cc.CardExpMonth,cc.CardExpYear,cc.CardCW2 ,cc.CardHolderName,cc.CardBillingAddress,cc.CardBillingZipCode,");
			sqlString.append(
					"ad1.Name,ad1.FirstName,ad1.LastName,ad1.Address1,ad1.Address2,ad1.City,ad1.ZipCode,ad1.Country,ad1.State,ad1.Province,ad1.AddressType,");
			sqlString.append(
					"cvf.UseIndividual ,cvf.AnnualInterestRate ,cvf.MinimumFinanceCharge,cvf.GracePeriod ,cvf.AssessFinanceCharge, ");
			// sqlString.append("cv.isPhoneMobileNumber, cv.isMobilePhoneNumber,
			// cv.MiddleName,date_format(cv.DateInput,'%m-%d-%Y') As DateInput,");
			sqlString.append("cv.MiddleName,date_format(cv.DateInput,'%m-%d-%Y') As DateInput,");
			sqlString.append(
					"date_format(cv.DateTerminated,'%m-%d-%Y') As DateTerminated,cv.isTerminated,cv.DBAName,cv.Active,cvf.MarkFinanceCharge ");

			sqlString.append(
					" FROM  bca_clientvendor cv left join ( bca_cvcreditcard cc ,bca_bsaddress ad1 ,bca_clientvendorfinancecharges cvf )");
			sqlString.append(
					" on (cc.ClientVendorID= cv.ClientVendorID and ad1.ClientVendorID=cv.ClientVendorID and cvf.ClientVendorID= cv.ClientVendorID )");
			sqlString.append(" WHERE CompanyID=" + compId
					+ " AND cv.CVTypeID IN (1, 2) AND cv.Status IN ('U', 'N') AND cv.Deleted = 0 AND cv.Active=1 ");
			if (cvId != null) {
				sqlString.append(" AND cv.ClientVendorID =" + cvId);
			}
			sqlString.append(" group by ( cv.ClientVendorID ) order by cv.ClientVendorID ");
			pstmt = con.prepareStatement(sqlString.toString());
			Loger.log("SQL: " + sqlString);
			rs = pstmt.executeQuery();
			/* General */
			while (rs.next()) {
				if (!customerList.isEmpty()) {
					customer = new CustomerDto();
				}
				customer.setClientVendorID(rs.getString("ClientVendorID"));
				customer.setCname(rs.getString("NAME"));
				customer.setFirstName(rs.getString("FirstName"));
				customer.setLastName(rs.getString("LastName"));
				customer.setAddress1(rs.getString("Address1"));
				customer.setAddress2(rs.getString("Address2"));
				customer.setCity(rs.getString("City"));
				customer.setState(rs.getString("State"));

				request.setAttribute("state_gen", rs.getString("State"));

				customer.setProvince(rs.getString("Province"));
				customer.setCountry(rs.getString("Country"));
				customer.setZipCode(rs.getString("ZipCode"));
				customer.setPhone(rs.getString("Phone"));
				customer.setCellPhone(rs.getString("CellPhone"));
				customer.setFax(rs.getString("Fax"));
				customer.setEmail(rs.getString("Email"));
				customer.setHomePage(rs.getString("HomePage"));
				customer.setTitle(rs.getString("CustomerTitle"));
				customer.setTexID(rs.getString("ResellerTaxID"));
				customer.setOpeningUB(rs.getString("VendorOpenDebit"));

				customer.setExtCredit(rs.getString("VendorAllowedCredit"));
				customer.setMemo(rs.getString("Detail"));
				customer.setTaxAble(rs.getString("Taxable"));

				customer.setIsclient(rs.getString("CVTypeID")); // cvtypeid
				customer.setType(rs.getString("cvcategoryid"));
				customer.setDateAdded(rs.getString("DateAdded"));

				/* Account */
				customer.setCardNo(rs.getString("CardNumber"));
				customer.setExpDate(rs.getString("CardExpMonth") + "/" + rs.getString("CardExpYear"));

				customer.setCw2(rs.getString("CardCW2"));
				customer.setCardHolderName(rs.getString("CardHolderName"));
				customer.setCardBillAddress(rs.getString("CardBillingAddress"));
				customer.setCardZip(rs.getString("CardBillingZipCode"));

				/* Finance */
				// customer.setFsUseIndividual(rs.getString(44).equals("1")?"true":"false");
				customer.setFsUseIndividual(rs.getString("UseIndividual"));
				customer.setAnnualIntrestRate(rs.getString("AnnualInterestRate"));
				customer.setMinFCharges(rs.getString("MinimumFinanceCharge"));
				customer.setGracePrd(rs.getString("GracePeriod"));
				String str1 = rs.getString("AssessFinanceCharge");
				if (str1 == null)
					customer.setFsAssessFinanceCharge("false");
				else
					customer.setFsAssessFinanceCharge(rs.getString(48).equals("1") ? "true" : "false");
				// customer.setIsPhoneMobileNumber(rs.getBoolean(49));
				// customer.setIsMobilePhoneNumber(rs.getBoolean(50));
				customer.setMiddleName(rs.getString("MiddleName"));
				customer.setDateInput(rs.getString("DateInput"));
				customer.setTerminatedDate(rs.getString("DateTerminated"));
				customer.setTerminated(rs.getBoolean("isTerminated"));
				customer.setDbaName(rs.getString("DBAName"));
				customer.setActive(rs.getBoolean("Active"));
				customer.setFsMarkFinanceCharge(rs.getString("MarkFinanceCharge"));

				String sqlString1 = "SELECT Name,FirstName,LastName,Address1,Address2,City,ZipCode,Country,State,Province,DBAName "
						+ "FROM bca_bsaddress WHERE ClientVendorID=" + cvId
						+ " and AddressType='1' and Status in ('N', 'U')";
				pstmt1 = con.prepareStatement(sqlString1);
				rs1 = pstmt1.executeQuery();
				if (rs1.next()) {
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
					customer.setBsdbaName(rs1.getString(11));
					request.setAttribute("state_bt", customer.getBsstate());
				}

				String sqlString2 = "SELECT Name,FirstName,LastName,Address1,Address2,City,ZipCode,Country,State,Province,DBAName "
						+ "FROM bca_bsaddress WHERE ClientVendorID=" + cvId
						+ " and AddressType='0' and Status in ('N', 'U')";
				pstmt3 = con.prepareStatement(sqlString2);
				rs2 = pstmt3.executeQuery();
				if (rs2.next()) {
					customer.setShcname(rs2.getString(1));
					customer.setShfirstName(rs2.getString(2));
					customer.setShlastName(rs2.getString(3));
					customer.setShaddress1(rs2.getString(4));
					customer.setShaddress2(rs2.getString(5));
					customer.setShcity(rs2.getString(6));
					customer.setShzipCode(rs2.getString(7));
					customer.setShcountry(rs2.getString(8));
					customer.setShstate(rs2.getString(9));
					customer.setShprovince(rs2.getString(10));
					customer.setShdbaName(rs2.getString(11));
					request.setAttribute("state_st", customer.getShstate());
				}

				/* for Account tab */
				pstmt4 = con.prepareStatement(
						"select SalesRepID,TermID,PaymentTypeID,ShipCarrierID from bca_clientvendor where CompanyID=? and ClientVendorID=?");
				pstmt4.setString(1, compId);
				pstmt4.setString(2, cvId);
				rs3 = pstmt4.executeQuery();
				if (rs3.next()) {
					customer.setRep(rs3.getString(1));
					customer.setTerm(rs3.getString(2));
					customer.setPaymentType(rs3.getString(3));
					customer.setShipping(rs3.getString(4));
				}

				// ----------------------start---code---------------------------
				List<CreditCardDto> creditCards = new ArrayList<>();
				pstmt4 = con.prepareStatement(
						"select DISTINCT c.*,t.Name AS CardTypeName from bca_cvcreditcard AS c INNER JOIN bca_creditcardtype AS t ON t.CCTypeID=c.CCTypeID where c.clientvendorid=? and c.active=1");
				pstmt4.setString(1, cvId);
				rs3 = pstmt4.executeQuery();
				while (rs3.next()) {
					CreditCardDto card = new CreditCardDto();
					card.setCardID(rs3.getInt("CreditCardID"));
					card.setClientVendorID(rs3.getInt("ClientVendorID"));
					card.setCcType(rs3.getString("CCTypeID"));
					card.setCardNo(rs3.getString("CardNumber"));
					card.setExpDate(rs3.getString("CardExpMonth") + " / " + rs3.getString("CardExpYear"));
					card.setCw2(rs3.getString("CardCW2"));
					card.setCardHolderName(rs3.getString("CardHolderName"));
					card.setCardBillAddress(rs3.getString("CardBillingAddress"));
					card.setCardZip(rs3.getString("CardBillingZipCode"));
					card.setCardDefault(rs3.getBoolean("DEFAULTCard"));

					String ccTypeName = rs3.getString("CardTypeName") + "...."
							+ card.getCardNo().substring(card.getCardNo().length() - 4);
					card.setCcTypeName(ccTypeName);
					creditCards.add(card);
				}
				customer.setCustomerCards(creditCards);

//				============================== Services ==============================
				String sqlString11 = "select ClientVendorID,ServiceID,DateAdded,InvoiceStyleID,ServiceBalance,DefaultService from bca_clientvendorservice where CompanyID = ? and ClientVendorID = ?";
				pstmt2 = con.prepareStatement(sqlString11);
				pstmt2.setString(1, compId);
				pstmt2.setString(2, cvId);
				rs22 = pstmt2.executeQuery();
				String default_ser = "";
				ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<>();
				while (rs22.next()) {
					UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
					uform1.setServiceBalance((rs22.getDouble("ServiceBalance")));
					uform1.setDefaultService(rs22.getInt("DefaultService"));
					Loger.log("SERVICE   DDDD__________-----------________" + uform1.getDefaultService());

					int svID = rs22.getInt("ServiceID");
					uform1.setServiceID(svID);
					if (uform1.getDefaultService() == 1) {
						default_ser = String.valueOf(svID);
					}

					String sqlString12 = "select  Name from bca_invoicestyle where Active=1 and InvoiceStyleID=?";
					pstmt12 = con.prepareStatement(sqlString12);
					pstmt12.setString(1, rs22.getString("InvoiceStyleID"));
					rs12 = pstmt12.executeQuery();
					while (rs12.next()) {
						uform1.setInvoiceStyle(rs12.getString(1));

					}
					String sqlString13 = "select ServiceName from bca_servicetype where ServiceID=? ";
					pstmt13 = con.prepareStatement(sqlString13);
					pstmt13.setString(1, String.valueOf(svID));
					rs13 = pstmt13.executeQuery();
					while (rs13.next()) {
						uform1.setServiceName(rs13.getString(1));
					}
					serviceinfo.add(uform1);
				}
				request.setAttribute("ServiceInfo", serviceinfo);
				Loger.log("deafult_ser________________" + default_ser);
				if (!(default_ser.equals(""))) {
					request.setAttribute("DefaultService", default_ser);
				} else {
					default_ser = "0";
					request.setAttribute("DefaultService", default_ser);
				}
				customerList.add(customer);
			}
			request.setAttribute("CustomerDetails", customer);
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());

		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (rs1 != null) {
					db.close(rs1);
				}
				if (rs2 != null) {
					db.close(rs2);
				}
				if (rs3 != null) {
					db.close(rs3);
				}
				if (rs22 != null) {
					db.close(rs22);
				}
				if (rs12 != null) {
					db.close(rs12);
				}
				if (rs13 != null) {
					db.close(rs13);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (pstmt1 != null) {
					db.close(pstmt1);
				}
				if (pstmt2 != null) {
					db.close(pstmt2);
				}
				if (pstmt3 != null) {
					db.close(pstmt3);
				}
				if (pstmt4 != null) {
					db.close(pstmt4);
				}
				if (pstmt12 != null) {
					db.close(pstmt12);
				}
				if (pstmt13 != null) {
					db.close(pstmt13);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return customerList;
	}

	public ArrayList SearchCustomer(String compId, String cvId, HttpServletRequest request, CustomerDto customer) {
		ArrayList<CustomerDto> customerList = new ArrayList<>();

		try {
			StringBuffer jpql = new StringBuffer();

			jpql.append("SELECT DISTINCT cv " + "FROM BcaClientvendor cv ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaCvcreditcards cc ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaBillingaddresss ba ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaClientvendorfinancechargess cf ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaClientvendorservices cvs ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaShippingaddresss sa ");
			jpql.append(
					"WHERE cv.cvtypeId IN (1, 2, 8) AND cv.status IN ('N', 'U') AND cv.deleted = 0 AND cv.company.companyId = :companyId ");

			if (cvId != null || cvId != "") {
				jpql.append(" AND cv.clientVendorId = " + cvId);
			}

			jpql.append("GROUP BY cv.clientVendorId " + "ORDER BY cv.clientVendorId");

			TypedQuery<BcaClientvendor> query = entityManager.createQuery(jpql.toString(), BcaClientvendor.class)
					.setParameter("companyId", Long.valueOf(compId));

			List<BcaClientvendor> clientVendors = query.getResultList();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

			Loger.log("SQL: " + jpql.toString());
			/* General */
			for (BcaClientvendor cv : clientVendors) {
				if (!customerList.isEmpty()) {
					customer = new CustomerDto();
				}
				customer.setClientVendorID(cv.getClientVendorId().toString());
				customer.setCname(cv.getName());
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setCity(cv.getCity());
				customer.setState(cv.getState());

				request.setAttribute("state_gen", cv.getState());

				customer.setProvince(cv.getProvince());
				customer.setCountry(cv.getCountry());
				customer.setZipCode(cv.getZipCode());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setEmail(cv.getEmail());
				customer.setHomePage(cv.getHomePage());
				customer.setTitle(cv.getCustomerTitle());
				customer.setTexID(cv.getResellerTaxId());
				customer.setOpeningUB(cv.getVendorOpenDebit().toString());

				customer.setExtCredit(cv.getVendorAllowedCredit().toString());
				customer.setMemo(cv.getDetail());
				customer.setTaxAble(cv.getTaxable() != null ? cv.getTaxable().toString() : "0");
				customer.setIsclient(cv.getCvtypeId().toString()); // cvtypeid
				customer.setType(cv.getCvcategoryId().toString());

				OffsetDateTime dateAdded = cv.getDateAdded();
				String formattedDate = dateAdded.format(formatter);

				customer.setDateAdded(formattedDate);

				// Account
				Set<BcaCvcreditcard> creditCards = cv.getClientVendorBcaCvcreditcards();

				// Assuming you want to handle only the first credit card.
				// You can iterate through the set if you have multiple cards for a single
				// customer.
				if (!creditCards.isEmpty()) {
					BcaCvcreditcard firstCreditCard = creditCards.iterator().next(); // Retrieve the first credit card

					customer.setCardNo(firstCreditCard.getCardNumber());
					customer.setExpDate(firstCreditCard.getCardExpMonth() + "/" + firstCreditCard.getCardExpYear());
					customer.setCw2(firstCreditCard.getCardCw2());
					customer.setCardHolderName(firstCreditCard.getCardHolderName());
					customer.setCardBillAddress(firstCreditCard.getCardBillingAddress());
					customer.setCardZip(firstCreditCard.getCardBillingZipCode());
				}

				// Finance
				Set<BcaClientvendorfinancecharges> financeChargesSet = cv
						.getClientVendorBcaClientvendorfinancechargess();
				if (financeChargesSet != null && !financeChargesSet.isEmpty()) { // Ensure that there's associated
																					// finance data
					BcaClientvendorfinancecharges financeCharges = financeChargesSet.iterator().next();

					customer.setFsUseIndividual(financeCharges.getUseIndividual() ? "true" : "false");
					customer.setAnnualIntrestRate(financeCharges.getAnnualInterestRate().toString());
					customer.setMinFCharges(financeCharges.getMinimumFinanceCharge().toString());
					customer.setGracePrd(financeCharges.getGracePeriod().toString());
					customer.setFsMarkFinanceCharge(financeCharges.getMarkFinanceCharge().toString());
					customer.setFsAssessFinanceCharge(financeCharges.getAssessFinanceCharge() ? "true" : "false");
				}

				customer.setIsPhoneMobileNumber(cv.getIsPhoneMobileNumber() != null ? cv.getIsPhoneMobileNumber() : false);
				customer.setIsMobilePhoneNumber(cv.getIsMobilePhoneNumber() != null ? cv.getIsMobilePhoneNumber() : false);

				customer.setMiddleName(cv.getMiddleName());
				customer.setDateInput(
						cv.getDateInput() != null ? cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
								: "");
				// customer.setDateInput(cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				// customer.setTerminatedDate(cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				customer.setTerminatedDate(cv.getDateTerminated() != null
						? cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
						: "");
				customer.setTerminated(cv.getIsTerminated());
				customer.setDbaName(cv.getDbaname());
				customer.setActive(cv.getActive() == 1 ? true : false);

				// Process Billing Addresses
				Set<BcaBillingaddress> billingAddresses = cv.getClientVendorBcaBillingaddresss();
				if (!billingAddresses.isEmpty()) {
					BcaBillingaddress firstBillAddress = billingAddresses.iterator().next(); // Retrieve the first
																								// credit card
					// Process or store shipping address information
					customer.setBscname(firstBillAddress.getName());
					customer.setBsfirstName(firstBillAddress.getFirstName());
					customer.setBslastName(firstBillAddress.getLastName());
					customer.setBsaddress1(firstBillAddress.getAddress1());
					customer.setBsaddress2(firstBillAddress.getAddress2());
					customer.setBscity(firstBillAddress.getCity());
					customer.setBszipCode(firstBillAddress.getZipCode());
					customer.setBscountry(firstBillAddress.getCountry());
					customer.setBsstate(firstBillAddress.getState());
					customer.setBsprovince(firstBillAddress.getProvince());
					customer.setBsdbaName(firstBillAddress.getDbaname());
					request.setAttribute("state_bt", customer.getBsstate());
				}

				// Process Shipping Addresses
				Set<BcaShippingaddress> shippingAddresses = cv.getClientVendorBcaShippingaddresss();
				if (!shippingAddresses.isEmpty()) {
					BcaShippingaddress firstShipAddres = shippingAddresses.iterator().next();
					// Process or store shipping address information
					customer.setShcname(firstShipAddres.getName());
					customer.setShfirstName(firstShipAddres.getFirstName());
					customer.setShlastName(firstShipAddres.getLastName());
					customer.setShaddress1(firstShipAddres.getAddress1());
					customer.setShaddress2(firstShipAddres.getAddress2());
					customer.setShcity(firstShipAddres.getCity());
					customer.setShzipCode(firstShipAddres.getZipCode());
					customer.setShcountry(firstShipAddres.getCountry());
					customer.setShstate(firstShipAddres.getState());
					customer.setShprovince(firstShipAddres.getProvince());
					customer.setShdbaName(firstShipAddres.getDbaname());
					request.setAttribute("state_st", customer.getShstate());
				}
				customer.setRep(cv.getSalesRep().getSalesRepId().toString());
				customer.setTerm(cv.getTerm().getTermId().toString());
				customer.setPaymentType(
						cv.getPaymentType() != null ? cv.getPaymentType().getPaymentTypeId().toString() : null);
				customer.setShipping(
						cv.getShipCarrier() != null ? cv.getShipCarrier().getShipCarrierId().toString() : null);

				// Process Credit Card Information
				List<CreditCardDto> creditCardsList = new ArrayList<>();
				Set<BcaCvcreditcard> cvCreditCards = cv.getClientVendorBcaCvcreditcards();
				for (BcaCvcreditcard creditCard : cvCreditCards) {
					// Store or process other credit card information as needed
					CreditCardDto card = new CreditCardDto();
					card.setCardID(creditCard.getCreditCardId());
					card.setClientVendorID(creditCard.getClientVendor().getClientVendorId());
					card.setCcType(creditCard.getCctype().getcCTypeID().toString());
					card.setCardNo(creditCard.getCardNumber());
					card.setExpDate(creditCard.getCardExpMonth() + " / " + creditCard.getCardExpYear());
					card.setCw2(creditCard.getCardCw2());
					card.setCardHolderName(creditCard.getCardHolderName());
					card.setCardBillAddress(creditCard.getCardBillingAddress());
					card.setCardZip(creditCard.getCardBillingZipCode());
					card.setCardDefault(creditCard.getDefaultCard() != null ? creditCard.getDefaultCard() : false);
					String ccTypeName = creditCard.getCctype().getName() + "...."
							+ card.getCardNo().substring(card.getCardNo().length() - 4);
					card.setCcTypeName(ccTypeName);
					creditCardsList.add(card);
				}
				customer.setCustomerCards(creditCardsList);
				// Process Client Vendor Services
				Set<BcaClientvendorservice> clientServices = cv.getClientVendorBcaClientvendorservices();
				String default_ser = "";
				int invoiceStyleID = -1;
				int serviceID = -1;
				ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<>();
				for (BcaClientvendorservice service : clientServices) {
					// Process or store service information
					UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
					uform1.setServiceBalance((service.getServiceBalance()));

					uform1.setDefaultService(service.getDefaultService() ? 1 : 0);
					// Loger.log("SERVICE DDDD__________-----------________"+
					// uform1.getDefaultService());

					serviceID = service.getServiceId();
					uform1.setServiceID(serviceID);
					if (uform1.getDefaultService() == 1) {
						default_ser = String.valueOf(serviceID);
					}
					invoiceStyleID = service.getInvoiceStyleId();

					//
					Optional<BcaInvoicestyle> invoiceStyle = invoicestyleRepository.findById(invoiceStyleID);

					uform1.setInvoiceStyle(invoiceStyle.get().getName());

					Optional<BcaServicetype> serviceType = servicetypeRepository.findById(serviceID);

					uform1.setServiceName(serviceType.get().getServiceName());
					serviceinfo.add(uform1);
				}
				request.setAttribute("ServiceInfo", serviceinfo);
				Loger.log("deafult_ser________________" + default_ser);
				if (!(default_ser.equals(""))) {
					request.setAttribute("DefaultService", default_ser);
				} else {
					default_ser = "0";
					request.setAttribute("DefaultService", default_ser);
				}
				customerList.add(customer);
			}

			request.setAttribute("CustomerDetails", customer);
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());

		}
		return customerList;
	}

	public ArrayList searchContact(String compId, String cvId, HttpServletRequest request, CustomerDto customer) {
		ArrayList<CustomerDto> customerList = new ArrayList<>();

		try {
			StringBuffer jpql = new StringBuffer();

			jpql.append("SELECT DISTINCT cv " + "FROM BcaClientvendor cv ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaCvcreditcards cc ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaBillingaddresss ba ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaClientvendorfinancechargess cf ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaClientvendorservices cvs ");
			jpql.append("LEFT JOIN FETCH cv.clientVendorBcaShippingaddresss sa ");
			jpql.append(
					"WHERE cv.cvtypeId IN (7) AND cv.status IN ('N', 'U') AND cv.deleted = 0 AND cv.company.companyId = :companyId ");

			if (cvId != null || cvId != "") {
				jpql.append(" AND cv.clientVendorId = " + cvId);
			}

			jpql.append("GROUP BY cv.clientVendorId " + "ORDER BY cv.clientVendorId");

			TypedQuery<BcaClientvendor> query = entityManager.createQuery(jpql.toString(), BcaClientvendor.class)
					.setParameter("companyId", Long.valueOf(compId));

			List<BcaClientvendor> clientVendors = query.getResultList();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

			Loger.log("SQL: " + jpql.toString());
			/* General */
			for (BcaClientvendor cv : clientVendors) {
				if (!customerList.isEmpty()) {
					customer = new CustomerDto();
				}
				customer.setClientVendorID(cv.getClientVendorId().toString());
				customer.setCname(cv.getName());
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setCity(cv.getCity());
				customer.setState(cv.getState());

				request.setAttribute("state_gen", cv.getState());

				customer.setProvince(cv.getProvince());
				customer.setCountry(cv.getCountry());
				customer.setZipCode(cv.getZipCode());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setEmail(cv.getEmail());
				customer.setHomePage(cv.getHomePage());
				customer.setTitle(cv.getCustomerTitle());
				customer.setTexID(cv.getResellerTaxId());
				customer.setOpeningUB(cv.getVendorOpenDebit().toString());

				customer.setExtCredit(cv.getVendorAllowedCredit().toString());
				customer.setMemo(cv.getDetail());
				customer.setTaxAble(cv.getTaxable() != null ? cv.getTaxable().toString() : "0");
				customer.setIsclient(cv.getCvtypeId().toString()); // cvtypeid
				customer.setType(cv.getCvcategoryId().toString());

				OffsetDateTime dateAdded = cv.getDateAdded();
				String formattedDate = dateAdded.format(formatter);

				customer.setDateAdded(formattedDate);

				// Account
				Set<BcaCvcreditcard> creditCards = cv.getClientVendorBcaCvcreditcards();

				// Assuming you want to handle only the first credit card.
				// You can iterate through the set if you have multiple cards for a single
				// customer.
				if (!creditCards.isEmpty()) {
					BcaCvcreditcard firstCreditCard = creditCards.iterator().next(); // Retrieve the first credit card

					customer.setCardNo(firstCreditCard.getCardNumber());
					customer.setExpDate(firstCreditCard.getCardExpMonth() + "/" + firstCreditCard.getCardExpYear());
					customer.setCw2(firstCreditCard.getCardCw2());
					customer.setCardHolderName(firstCreditCard.getCardHolderName());
					customer.setCardBillAddress(firstCreditCard.getCardBillingAddress());
					customer.setCardZip(firstCreditCard.getCardBillingZipCode());
				}

				// Finance
				Set<BcaClientvendorfinancecharges> financeChargesSet = cv
						.getClientVendorBcaClientvendorfinancechargess();
				if (financeChargesSet != null && !financeChargesSet.isEmpty()) { // Ensure that there's associated
																					// finance data
					BcaClientvendorfinancecharges financeCharges = financeChargesSet.iterator().next();

					customer.setFsUseIndividual(financeCharges.getUseIndividual() ? "true" : "false");
					customer.setAnnualIntrestRate(financeCharges.getAnnualInterestRate().toString());
					customer.setMinFCharges(financeCharges.getMinimumFinanceCharge().toString());
					customer.setGracePrd(financeCharges.getGracePeriod().toString());
					customer.setFsMarkFinanceCharge(financeCharges.getMarkFinanceCharge().toString());
					customer.setFsAssessFinanceCharge(financeCharges.getAssessFinanceCharge() ? "true" : "false");
				}

				customer.setIsPhoneMobileNumber(cv.getIsPhoneMobileNumber());
				customer.setIsMobilePhoneNumber(cv.getIsMobilePhoneNumber());
				customer.setMiddleName(cv.getMiddleName());
				customer.setDateInput(
						cv.getDateInput() != null ? cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
								: "");
				// customer.setDateInput(cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				// customer.setTerminatedDate(cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				customer.setTerminatedDate(cv.getDateTerminated() != null
						? cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
						: "");
				customer.setTerminated(cv.getIsTerminated());
				customer.setDbaName(cv.getDbaname());
				customer.setActive(cv.getActive() == 1 ? true : false);

				// Process Billing Addresses
				Set<BcaBillingaddress> billingAddresses = cv.getClientVendorBcaBillingaddresss();
				if (!billingAddresses.isEmpty()) {
					BcaBillingaddress firstBillAddress = billingAddresses.iterator().next(); // Retrieve the first
																								// credit card
					// Process or store shipping address information
					customer.setBscname(firstBillAddress.getName());
					customer.setBsfirstName(firstBillAddress.getFirstName());
					customer.setBslastName(firstBillAddress.getLastName());
					customer.setBsaddress1(firstBillAddress.getAddress1());
					customer.setBsaddress2(firstBillAddress.getAddress2());
					customer.setBscity(firstBillAddress.getCity());
					customer.setBszipCode(firstBillAddress.getZipCode());
					customer.setBscountry(firstBillAddress.getCountry());
					customer.setBsstate(firstBillAddress.getState());
					customer.setBsprovince(firstBillAddress.getProvince());
					customer.setBsdbaName(firstBillAddress.getDbaname());
					request.setAttribute("state_bt", customer.getBsstate());
				}

				// Process Shipping Addresses
				Set<BcaShippingaddress> shippingAddresses = cv.getClientVendorBcaShippingaddresss();
				if (!shippingAddresses.isEmpty()) {
					BcaShippingaddress firstShipAddres = shippingAddresses.iterator().next();
					// Process or store shipping address information
					customer.setShcname(firstShipAddres.getName());
					customer.setShfirstName(firstShipAddres.getFirstName());
					customer.setShlastName(firstShipAddres.getLastName());
					customer.setShaddress1(firstShipAddres.getAddress1());
					customer.setShaddress2(firstShipAddres.getAddress2());
					customer.setShcity(firstShipAddres.getCity());
					customer.setShzipCode(firstShipAddres.getZipCode());
					customer.setShcountry(firstShipAddres.getCountry());
					customer.setShstate(firstShipAddres.getState());
					customer.setShprovince(firstShipAddres.getProvince());
					customer.setShdbaName(firstShipAddres.getDbaname());
					request.setAttribute("state_st", customer.getShstate());
				}
				customer.setRep(cv.getSalesRep().getSalesRepId().toString());
				customer.setTerm(cv.getTerm().getTermId().toString());
				customer.setPaymentType(
						cv.getPaymentType() != null ? cv.getPaymentType().getPaymentTypeId().toString() : null);
				customer.setShipping(
						cv.getShipCarrier() != null ? cv.getShipCarrier().getShipCarrierId().toString() : null);

				// Process Credit Card Information
				List<CreditCardDto> creditCardsList = new ArrayList<>();
				Set<BcaCvcreditcard> cvCreditCards = cv.getClientVendorBcaCvcreditcards();
				for (BcaCvcreditcard creditCard : cvCreditCards) {
					// Store or process other credit card information as needed
					CreditCardDto card = new CreditCardDto();
					card.setCardID(creditCard.getCreditCardId());
					card.setClientVendorID(creditCard.getClientVendor().getClientVendorId());
					card.setCcType(creditCard.getCctype().getcCTypeID().toString());
					card.setCardNo(creditCard.getCardNumber());
					card.setExpDate(creditCard.getCardExpMonth() + " / " + creditCard.getCardExpYear());
					card.setCw2(creditCard.getCardCw2());
					card.setCardHolderName(creditCard.getCardHolderName());
					card.setCardBillAddress(creditCard.getCardBillingAddress());
					card.setCardZip(creditCard.getCardBillingZipCode());
					card.setCardDefault(creditCard.getDefaultCard());
					String ccTypeName = creditCard.getCctype().getName() + "...."
							+ card.getCardNo().substring(card.getCardNo().length() - 4);
					card.setCcTypeName(ccTypeName);
					creditCardsList.add(card);
				}
				customer.setCustomerCards(creditCardsList);
				// Process Client Vendor Services
				Set<BcaClientvendorservice> clientServices = cv.getClientVendorBcaClientvendorservices();
				String default_ser = "";
				int invoiceStyleID = -1;
				int serviceID = -1;
				ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<>();
				for (BcaClientvendorservice service : clientServices) {
					// Process or store service information
					UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
					uform1.setServiceBalance((service.getServiceBalance()));

					uform1.setDefaultService(service.getDefaultService() ? 1 : 0);
					// Loger.log("SERVICE DDDD__________-----------________"+
					// uform1.getDefaultService());

					serviceID = service.getServiceId();
					uform1.setServiceID(serviceID);
					if (uform1.getDefaultService() == 1) {
						default_ser = String.valueOf(serviceID);
					}
					invoiceStyleID = service.getInvoiceStyleId();

					//
					Optional<BcaInvoicestyle> invoiceStyle = invoicestyleRepository.findById(invoiceStyleID);

					uform1.setInvoiceStyle(invoiceStyle.get().getName());

					Optional<BcaServicetype> serviceType = servicetypeRepository.findById(serviceID);

					uform1.setServiceName(serviceType.get().getServiceName());
					serviceinfo.add(uform1);
				}
				request.setAttribute("ServiceInfo", serviceinfo);
				Loger.log("deafult_ser________________" + default_ser);
				if (!(default_ser.equals(""))) {
					request.setAttribute("DefaultService", default_ser);
				} else {
					default_ser = "0";
					request.setAttribute("DefaultService", default_ser);
				}
				customerList.add(customer);
			}

			request.setAttribute("CustomerDetails", customer);
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());

		}
		return customerList;
	}

	@Cacheable(value = "clientVendorDetailsCache", key = "{#compId, #cvId}")
	public List<BcaClientvendor> fetchClientVendorDetails(String compId, String cvId) {
		String jpql = "SELECT DISTINCT cv FROM BcaClientvendor cv "
				+ "LEFT JOIN FETCH cv.clientVendorBcaCvcreditcards cc "
				+ "LEFT JOIN FETCH cv.clientVendorBcaBillingaddresss ba "
				+ "LEFT JOIN FETCH cv.clientVendorBcaClientvendorfinancechargess cf "
				+ "LEFT JOIN FETCH cv.clientVendorBcaClientvendorservices cvs "
				+ "LEFT JOIN FETCH cv.clientVendorBcaShippingaddresss sa "
				+ "WHERE cv.status IN ('N', 'U') AND cv.deleted = 0 AND cv.company.companyId = :companyId AND cv.clientVendorId = :clientVendorId "
				+ "GROUP BY cv.clientVendorId ORDER BY cv.clientVendorId";

		TypedQuery<BcaClientvendor> query = entityManager.createQuery(jpql, BcaClientvendor.class)
				.setParameter("companyId", Long.valueOf(compId)).setParameter("clientVendorId", Integer.valueOf(cvId));

		return query.getResultList();
	}

	public void searchSelectedCustomer(String compId, String cvId, HttpServletRequest request) {
		ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<UpdateInvoiceDto>();
		UpdateInvoiceDto customer = new UpdateInvoiceDto();
		try {
//			String jpql = "SELECT DISTINCT cv " + "FROM BcaClientvendor cv "
//					+ "LEFT JOIN FETCH cv.clientVendorBcaCvcreditcards cc "
//					+ "LEFT JOIN FETCH cv.clientVendorBcaBillingaddresss ba "
//					+ "LEFT JOIN FETCH cv.clientVendorBcaClientvendorfinancechargess cf "
//					+ "LEFT JOIN FETCH cv.clientVendorBcaClientvendorservices cvs "
//					+ "LEFT JOIN FETCH cv.clientVendorBcaShippingaddresss sa "
//					+ "WHERE cv.status IN ('N', 'U') AND cv.deleted = 0 AND cv.company.companyId = :companyId AND cv.clientVendorId = :clientVendorId "
//					+ "GROUP BY cv.clientVendorId " + "ORDER BY cv.clientVendorId";
//
//			TypedQuery<BcaClientvendor> query = entityManager.createQuery(jpql, BcaClientvendor.class)
//					.setParameter("companyId", Long.valueOf(compId))
//					.setParameter("clientVendorId", Integer.valueOf(cvId));

			List<BcaClientvendor> clientVendors = fetchClientVendorDetails(compId, cvId);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

			for (BcaClientvendor cv : clientVendors) {
				System.out.println("Client Vendor: " + cv.getName());
				/* General */
				customer.setClientVendorID(cv.getClientVendorId().toString());
				customer.setCname(cv.getName());
				customer.setFirstName(cv.getFirstName());
				customer.setLastName(cv.getLastName());
				customer.setAddress1(cv.getAddress1());
				customer.setAddress2(cv.getAddress2());
				customer.setCity(cv.getCity());
				customer.setState(cv.getState());

				request.setAttribute("state_gen", cv.getState());

				customer.setProvince(cv.getProvince());
				customer.setCountry(cv.getCountry());
				customer.setZipCode(cv.getZipCode());
				customer.setPhone(cv.getPhone());
				customer.setCellPhone(cv.getCellPhone());
				customer.setFax(cv.getFax());
				customer.setEmail(cv.getEmail());
				customer.setHomePage(cv.getHomePage());
				customer.setTitle(cv.getCustomerTitle());
				customer.setTexID(cv.getResellerTaxId());
				customer.setOpeningUB(cv.getVendorOpenDebit().toString());

				customer.setExtCredit(cv.getVendorAllowedCredit().toString());
				customer.setMemo(cv.getDetail());
				customer.setTaxAble(cv.getTaxable() != null ? cv.getTaxable().toString() : "0");
				customer.setIsclient(cv.getCvtypeId().toString()); // cvtypeid
				customer.setType(cv.getCvcategoryId().toString());

				OffsetDateTime dateAdded = cv.getDateAdded();
				String formattedDate = dateAdded.format(formatter);

				customer.setDateAdded(formattedDate);

				// Account
				Set<BcaCvcreditcard> creditCards = cv.getClientVendorBcaCvcreditcards();

				// Assuming you want to handle only the first credit card.
				// You can iterate through the set if you have multiple cards for a single
				// customer.
				if (creditCards != null && !creditCards.isEmpty()) {
					BcaCvcreditcard firstCreditCard = creditCards.iterator().next(); // Retrieve the first credit card

					customer.setCardNo(firstCreditCard.getCardNumber());
					customer.setExpDate(firstCreditCard.getCardExpMonth() + "/" + firstCreditCard.getCardExpYear());
					customer.setCw2(firstCreditCard.getCardCw2());
					customer.setCardHolderName(firstCreditCard.getCardHolderName());
					customer.setCardBillAddress(firstCreditCard.getCardBillingAddress());
					customer.setCardZip(firstCreditCard.getCardBillingZipCode());
				}

				// Finance
				Set<BcaClientvendorfinancecharges> financeChargesSet = cv
						.getClientVendorBcaClientvendorfinancechargess();
				if (financeChargesSet != null && !financeChargesSet.isEmpty()) { // Ensure that there's associated
																					// finance data
					BcaClientvendorfinancecharges financeCharges = financeChargesSet.iterator().next();

					customer.setFsUseIndividual(financeCharges.getUseIndividual() ? "true" : "false");
					customer.setAnnualIntrestRate(financeCharges.getAnnualInterestRate().toString());
					customer.setMinFCharges(financeCharges.getMinimumFinanceCharge().toString());
					customer.setGracePrd(financeCharges.getGracePeriod().toString());
					customer.setFsAssessFinanceCharge(financeCharges.getAssessFinanceCharge() ? "true" : "false");
				}

				customer.setIsPhoneMobileNumber(cv.getIsPhoneMobileNumber());
				customer.setIsMobilePhoneNumber(cv.getIsMobilePhoneNumber());
				customer.setMiddleName(cv.getMiddleName());
				customer.setDateInput(
						cv.getDateInput() != null ? cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
								: "");
				// customer.setDateInput(cv.getDateInput().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				// customer.setTerminatedDate(cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
				customer.setTerminatedDate(cv.getDateTerminated() != null
						? cv.getDateTerminated().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
						: "");
				customer.setTerminated(cv.getIsTerminated());

				// Process Credit Card Information
				List<CreditCardDto> creditCardsList = new ArrayList<>();
				Set<BcaCvcreditcard> cvCreditCards = cv.getClientVendorBcaCvcreditcards();
				for (BcaCvcreditcard creditCard : cvCreditCards) {
					// Store or process other credit card information as needed
					CreditCardDto card = new CreditCardDto();
					card.setCardID(creditCard.getCreditCardId());
					card.setClientVendorID(creditCard.getClientVendor().getClientVendorId());
					card.setCcType(creditCard.getCctype().getcCTypeID().toString());
					card.setCardNo(creditCard.getCardNumber());
					card.setExpDate(creditCard.getCardExpMonth() + " / " + creditCard.getCardExpYear());
					card.setCw2(creditCard.getCardCw2());
					card.setCardHolderName(creditCard.getCardHolderName());
					card.setCardBillAddress(creditCard.getCardBillingAddress());
					card.setCardZip(creditCard.getCardBillingZipCode());
					card.setCardDefault(creditCard.getDefaultCard());
					String ccTypeName = creditCard.getCctype().getName() + "...."
							+ card.getCardNo().substring(card.getCardNo().length() - 4);
					card.setCcTypeName(ccTypeName);
					creditCardsList.add(card);
				}
				customer.setCustomerCards(creditCardsList);

				// Process Shipping Addresses
				Set<BcaShippingaddress> shippingAddresses = cv.getClientVendorBcaShippingaddresss();
				if (shippingAddresses != null && !shippingAddresses.isEmpty()) {
					BcaShippingaddress firstShipAddres = shippingAddresses.iterator().next();
					// Process or store shipping address information
					customer.setShcname(firstShipAddres.getName());
					customer.setShfirstName(firstShipAddres.getFirstName());
					customer.setShlastName(firstShipAddres.getLastName());
					customer.setShaddress1(firstShipAddres.getAddress1());
					customer.setShaddress2(firstShipAddres.getAddress2());
					customer.setShcity(firstShipAddres.getCity());
					customer.setShzipCode(firstShipAddres.getZipCode());
					customer.setShcountry(firstShipAddres.getCountry());
					customer.setShstate(firstShipAddres.getState());
					customer.setShprovince(firstShipAddres.getProvince());

					request.setAttribute("state_st", customer.getShstate());
				}

				// Process Billing Addresses
				Set<BcaBillingaddress> billingAddresses = cv.getClientVendorBcaBillingaddresss();
				if (billingAddresses != null && !billingAddresses.isEmpty()) {
					BcaBillingaddress firstBillAddress = billingAddresses.iterator().next(); // Retrieve the first
																								// credit card
					// Process or store shipping address information
					customer.setBscname(firstBillAddress.getName());
					customer.setBsfirstName(firstBillAddress.getFirstName());
					customer.setBslastName(firstBillAddress.getLastName());
					customer.setBsaddress1(firstBillAddress.getAddress1());
					customer.setBsaddress2(firstBillAddress.getAddress2());
					customer.setBscity(firstBillAddress.getCity());
					customer.setBszipCode(firstBillAddress.getZipCode());
					customer.setBscountry(firstBillAddress.getCountry());
					customer.setBsstate(firstBillAddress.getState());
					customer.setBsprovince(firstBillAddress.getProvince());

					request.setAttribute("state_bt", customer.getBsstate());
				}
				customer.setRep(cv.getSalesRep().getSalesRepId().toString());
				customer.setTerm(cv.getTerm().getTermId().toString());
				customer.setPaymentType(
						cv.getPaymentType() != null ? cv.getPaymentType().getPaymentTypeId().toString() : null);
				customer.setShipping(
						cv.getShipCarrier() != null ? cv.getShipCarrier().getShipCarrierId().toString() : null);

//				customer.setPaymentType(cv.getPaymentType().getPaymentTypeId().toString());
				// customer.setShipping(cv.getShipCarrier().getShipCarrierId().toString());

				// Process Client Vendor Services
				Set<BcaClientvendorservice> clientServices = cv.getClientVendorBcaClientvendorservices();
				String default_ser = "";
				int invoiceStyleID = -1;
				int serviceID = -1;
				for (BcaClientvendorservice service : clientServices) {
					// Process or store service information
					UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
					uform1.setServiceBalance((service.getServiceBalance()));

					uform1.setDefaultService(service.getDefaultService() ? 1 : 0);
					// Loger.log("SERVICE DDDD__________-----------________"+
					// uform1.getDefaultService());

					serviceID = service.getServiceId();
					uform1.setServiceID(serviceID);
					if (uform1.getDefaultService() == 1) {
						default_ser = String.valueOf(serviceID);
					}
					invoiceStyleID = service.getInvoiceStyleId();

					//
					Optional<BcaInvoicestyle> invoiceStyle = invoicestyleRepository.findById(invoiceStyleID);

					uform1.setInvoiceStyle(invoiceStyle.get().getName());

					Optional<BcaServicetype> serviceType = servicetypeRepository.findById(serviceID);

					uform1.setServiceName(serviceType.get().getServiceName());
					serviceinfo.add(uform1);
				}

				request.setAttribute("ServiceInfo", serviceinfo);

				if (!(default_ser.equals(""))) {
					request.setAttribute("DefaultService", default_ser);
				} else {
					default_ser = "0";
					request.setAttribute("DefaultService", default_ser);
				}

			}

			request.setAttribute("CustomerDetails1", customer);
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());
		}
	}

	// search selected customer base on custid
	public void SearchselectedCustomerDELETE(String compId, String cvId, HttpServletRequest request) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null, pstmt12 = null,
				pstmt13 = null;
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs22 = null, rs12 = null, rs13 = null;
		ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<UpdateInvoiceDto>();
		UpdateInvoiceDto customer = new UpdateInvoiceDto();
		try {
			StringBuffer sqlString = new StringBuffer();
			sqlString.append(" select distinct bca_clientvendor.ClientVendorID,bca_clientvendor.Name,");
			sqlString.append("bca_clientvendor.FirstName,bca_clientvendor.LastName,bca_clientvendor.Address1,");
			sqlString.append("bca_clientvendor.Address2,bca_clientvendor.City,bca_clientvendor.State,");
			sqlString.append("bca_clientvendor.Province,bca_clientvendor.Country,bca_clientvendor.ZipCode,");
			sqlString.append("bca_clientvendor.Phone,bca_clientvendor.CellPhone,bca_clientvendor.Fax,");
			sqlString.append("bca_clientvendor.Email,bca_clientvendor.HomePage,bca_clientvendor.CustomerTitle,");
			sqlString.append(
					"bca_clientvendor.ResellerTaxID,bca_clientvendor.VendorOpenDebit,bca_clientvendor.VendorAllowedCredit,");
			sqlString.append("bca_clientvendor.Detail,bca_clientvendor.Taxable,bca_clientvendor.CVTypeID,");
			sqlString.append(
					"bca_clientvendor.cvcategoryid,date_format(bca_clientvendor.DateAdded,'%m-%d-%Y') As DateAdded,");

			sqlString.append(
					"bca_creditcard.CardNumber,bca_creditcard.CardExpMonth,bca_creditcard.CardExpYear,bca_creditcard.CardCW2,");
			sqlString.append(
					"bca_creditcard.CardHolderName,bca_creditcard.CardBillingAddress,bca_creditcard.CardBillingZipCode,");

			sqlString.append("bca_bsaddress.Name,bca_bsaddress.FirstName,bca_bsaddress.LastName,");
			sqlString.append("bca_bsaddress.Address1,bca_bsaddress.Address2,bca_bsaddress.City,");
			sqlString.append(
					"bca_bsaddress.ZipCode,bca_bsaddress.Country,bca_bsaddress.State,bca_bsaddress.Province,bca_bsaddress.AddressType,");

			sqlString.append(
					"bca_clientvendorfinancecharges.UseIndividual,bca_clientvendorfinancecharges.AnnualInterestRate,bca_clientvendorfinancecharges.MinimumFinanceCharge ,");
			/*
			 * sqlString.
			 * append("bca_clientvendorfinancecharges.GracePeriod ,bca_clientvendorfinancecharges.AssessFinanceCharge ,bca_clientvendorfinancecharges.MarkFinanceCharge "
			 * );
			 */ /* Commented on 26-04-2019 */
			sqlString.append(
					"bca_clientvendorfinancecharges.GracePeriod,bca_clientvendorfinancecharges.AssessFinanceCharge,");
			sqlString.append(
					"bca_clientvendor.isPhoneMobileNumber, bca_clientvendor.isMobilePhoneNumber, bca_clientvendor.MiddleName,");
			sqlString.append("date_format(bca_clientvendor.DateInput,'%m-%d-%Y') As DateInput,");
			sqlString.append(
					"date_format(bca_clientvendor.DateTerminated,'%m-%d-%Y') As DateTerminated,bca_clientvendor.isTerminated ");

			sqlString.append(
					" from bca_clientvendor left join ( bca_cvcreditcard ,bca_bsaddress ,bca_clientvendorfinancecharges )");
			sqlString.append(
					" on (bca_creditcard.ClientVendorID= bca_clientvendor.ClientVendorID and bca_bsaddress.ClientVendorID= ");
			sqlString.append(
					"bca_clientvendor.ClientVendorID and bca_clientvendorfinancecharges.ClientVendorID= bca_clientvendor.ClientVendorID )");
			sqlString.append(" WHERE bca_clientvendor.Status IN ('N', 'U') and bca_clientvendor.Deleted = '0' ");
			sqlString.append(" and CompanyID='" + compId + "' and bca_clientvendor.ClientVendorID ='" + cvId
					+ "' group by ( bca_clientvendor.ClientVendorID )");
			sqlString.append(" order by bca_clientvendor.ClientVendorID ");

			pstmt = con.prepareStatement(sqlString.toString());
			Loger.log(sqlString);
			rs = pstmt.executeQuery();

			String sqlString11 = "select ClientVendorID,ServiceID,DateAdded,InvoiceStyleID,ServiceBalance,DefaultService from bca_clientvendorservice where CompanyID = ? and ClientVendorID = ?";
			String sqlString12 = "select  Name from bca_invoicestyle where Active=1 and InvoiceStyleID=?";
			String sqlString13 = "select ServiceName from bca_servicetype where ServiceID=? ";

			pstmt2 = con.prepareStatement(sqlString11);
			pstmt12 = con.prepareStatement(sqlString12);
			pstmt13 = con.prepareStatement(sqlString13);
			pstmt2.setString(1, compId);
			pstmt2.setString(2, cvId);
			rs22 = pstmt2.executeQuery();
			String default_ser = "";
			while (rs22.next()) {
				UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
				uform1.setServiceBalance((rs22.getDouble("ServiceBalance")));

				uform1.setDefaultService(rs22.getInt("DefaultService"));
				// Loger.log("SERVICE DDDD__________-----------________"+
				// uform1.getDefaultService());

				int svID = rs22.getInt("ServiceID");
				uform1.setServiceID(svID);

				if (uform1.getDefaultService() == 1) {
					default_ser = String.valueOf(svID);
				}

				pstmt12.setString(1, rs22.getString("InvoiceStyleID"));
				rs12 = pstmt12.executeQuery();
				pstmt13.setString(1, String.valueOf(svID));
				rs13 = pstmt13.executeQuery();

				while (rs12.next()) {
					uform1.setInvoiceStyle(rs12.getString(1));

				}
				while (rs13.next()) {
					uform1.setServiceName(rs13.getString(1));
				}
				serviceinfo.add(uform1);
			}
			request.setAttribute("ServiceInfo", serviceinfo);
			// Loger.log("deafult_ser________________" + default_ser);
			if (!(default_ser.equals(""))) {
				request.setAttribute("DefaultService", default_ser);
			} else {
				default_ser = "0";
				request.setAttribute("DefaultService", default_ser);
			}

			if (rs.next()) {
				/* General */

				customer.setClientVendorID(rs.getString(1));
				customer.setCname(rs.getString(2));
				customer.setFirstName(rs.getString(3));
				customer.setLastName(rs.getString(4));
				customer.setAddress1(rs.getString(5));
				customer.setAddress2(rs.getString(6));
				customer.setCity(rs.getString(7));
				customer.setState(rs.getString(8));

				request.setAttribute("state_gen", rs.getString(8));

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

				customer.setIsclient(rs.getString(23)); // cvtypeid

				customer.setType(rs.getString(24));
				customer.setDateAdded(rs.getString(25));

				/* Account */
				customer.setCardNo(rs.getString(26));
				customer.setExpDate(rs.getString(27) + "/" + rs.getString(28));

				customer.setCw2(rs.getString(29));
				customer.setCardHolderName(rs.getString(30));
				customer.setCardBillAddress(rs.getString(31));
				customer.setCardZip(rs.getString(32));

				/* Finance */
				if (rs.getString(44) != null) // changed by nxsol 09-05-2018 Applying condition
				{
					customer.setFsUseIndividual(rs.getString(44).equals("1") ? "true" : "false");
				}
				if (rs.getString(45) != null) // changed by nxsol 09-05-2018 Applying condition
				{
					customer.setAnnualIntrestRate(rs.getString(45));
				}
				if (rs.getString(46) != null) // changed by nxsol 09-05-2018 Applying condition
				{
					customer.setMinFCharges(rs.getString(46));
				}
				if (rs.getString(47) != null) // changed by nxsol 09-05-2018 Applying condition
				{
					customer.setGracePrd(rs.getString(47));
				}
				String str1 = rs.getString(48);

				if (str1 == null)
					customer.setFsAssessFinanceCharge("false");
				else
					customer.setFsAssessFinanceCharge(rs.getString(48).equals("1") ? "true" : "false");
				customer.setIsPhoneMobileNumber(rs.getBoolean(49));
				customer.setIsMobilePhoneNumber(rs.getBoolean(50));
				customer.setMiddleName(rs.getString(51));
				customer.setDateInput(rs.getString(52));
				customer.setTerminatedDate(rs.getString(53));
				customer.setTerminated(rs.getBoolean(54));

			}
			StringBuffer sqlString1 = new StringBuffer();
			sqlString1.append("select bca_bsaddress.Name,bca_bsaddress.FirstName,");
			sqlString1.append(
					"bca_bsaddress.LastName,bca_bsaddress.Address1,bca_bsaddress.Address2,bca_bsaddress.City,bca_bsaddress.ZipCode,bca_bsaddress.Country,");
			sqlString1.append("bca_bsaddress.State,bca_bsaddress.Province ");
			sqlString1.append(" from bca_bsaddress ");
			sqlString1.append(
					" where ClientVendorID like '" + cvId + "' and AddressType like '1' and Status in ('N' , 'U') ");
			pstmt1 = con.prepareStatement(sqlString1.toString());
			// Loger.log(sqlString1);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
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

				request.setAttribute("state_bt", customer.getBsstate());
			}

			StringBuffer sqlString2 = new StringBuffer();
			sqlString2.append(" select bca_bsaddress.Name,bca_bsaddress.FirstName,");
			sqlString2.append(
					"bca_bsaddress.LastName,bca_bsaddress.Address1,bca_bsaddress.Address2,bca_bsaddress.City,bca_bsaddress.ZipCode,bca_bsaddress.Country,");
			sqlString2.append("bca_bsaddress.State,bca_bsaddress.Province ");
			sqlString2.append(" from bca_bsaddress ");
			sqlString2.append(
					" where ClientVendorID like '" + cvId + "' and AddressType like '0' and Status in ('N' , 'U') ");

			pstmt3 = con.prepareStatement(sqlString2.toString());
			// Loger.log(sqlString2);
			rs2 = pstmt3.executeQuery();
			// rs2.beforeFirst();
			if (rs2.next()) {
				customer.setShcname(rs2.getString(1));
				customer.setShfirstName(rs2.getString(2));
				customer.setShlastName(rs2.getString(3));
				customer.setShaddress1(rs2.getString(4));
				customer.setShaddress2(rs2.getString(5));
				customer.setShcity(rs2.getString(6));
				customer.setShzipCode(rs2.getString(7));
				customer.setShcountry(rs2.getString(8));
				customer.setShstate(rs2.getString(9));
				customer.setShprovince(rs2.getString(10));

				request.setAttribute("state_st", customer.getShstate());

			}

			/* for Account tab */
			pstmt4 = con.prepareStatement(
					"select SalesRepID,TermID,PaymentTypeID,ShipCarrierID from bca_clientvendor where CompanyID=? and ClientVendorID=?");
			pstmt4.setString(1, compId);
			pstmt4.setString(2, cvId);

			rs3 = pstmt4.executeQuery();
			if (rs3.next()) {
				customer.setRep(rs3.getString(1));
				customer.setTerm(rs3.getString(2));
				customer.setPaymentType(rs3.getString(3));
				customer.setShipping(rs3.getString(4));
			}
			pstmt4 = con.prepareStatement(
					"select c.*,t.Name AS CardTypeName from bca_cvcreditcard AS c INNER JOIN bca_cctype AS t ON t.CCTypeID=c.CCTypeID where c.clientvendorid=? and c.active=1");
			pstmt4.setString(1, cvId);
			rs3 = pstmt4.executeQuery();
			List<CreditCardDto> creditCards = new ArrayList<>();
			while (rs3.next()) {
				CreditCardDto card = new CreditCardDto();
				card.setCardID(rs3.getInt("CreditCardID"));
				card.setClientVendorID(rs3.getInt("ClientVendorID"));
				card.setCcType(rs3.getString("CCTypeID"));
				card.setCardNo(rs3.getString("CardNumber"));
				card.setExpDate(rs3.getString("CardExpMonth") + " / " + rs3.getString("CardExpYear"));
				card.setCw2(rs3.getString("CardCW2"));
				card.setCardHolderName(rs3.getString("CardHolderName"));
				card.setCardBillAddress(rs3.getString("CardBillingAddress"));
				card.setCardZip(rs3.getString("CardBillingZipCode"));
				card.setCardDefault(rs3.getBoolean("DEFAULTCard"));
				String ccTypeName = rs3.getString("CardTypeName") + "...."
						+ card.getCardNo().substring(card.getCardNo().length() - 4);
				card.setCcTypeName(ccTypeName);
				creditCards.add(card);
			}
			customer.setCustomerCards(creditCards);
			request.setAttribute("CustomerDetails1", customer);
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (rs1 != null) {
					db.close(rs1);
				}
				if (rs2 != null) {
					db.close(rs2);
				}
				if (rs3 != null) {
					db.close(rs3);
				}
				if (rs22 != null) {
					db.close(rs22);
				}
				if (rs12 != null) {
					db.close(rs12);
				}
				if (rs13 != null) {
					db.close(rs13);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (pstmt1 != null) {
					db.close(pstmt1);
				}
				if (pstmt2 != null) {
					db.close(pstmt2);
				}
				if (pstmt3 != null) {
					db.close(pstmt3);
				}
				if (pstmt4 != null) {
					db.close(pstmt4);
				}
				if (pstmt12 != null) {
					db.close(pstmt12);
				}
				if (pstmt13 != null) {
					db.close(pstmt13);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
	}

	public boolean insertCustomer(String cId, CustomerDto c, String compID, int istaxable, int isAlsoClient,
			int useIndividualFinanceCharges, int AssessFinanceChk, int FChargeInvoiceChk, String status) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement ps = null;
		SQLExecutor db = new SQLExecutor();

		if (db == null)
			return ret;
		con = db.getConnection();
		if (con == null)
			return ret;

		try {
			String oBal = "0";
			String exCredit = "0";
			PurchaseInfo pinfo = new PurchaseInfo();
			int cvID = Integer.parseInt(cId);
			Loger.log("istaxable:" + istaxable);
			Loger.log("isAlsoClient:" + isAlsoClient);

			if (isAlsoClient == 1) {
				isAlsoClient = 3;
			} else
				isAlsoClient = 1;

			if (c.getOpeningUB() != null && c.getOpeningUB().trim().length() > 0)
				oBal = c.getOpeningUB();

			if (c.getExtCredit() != null && c.getExtCredit().trim().length() > 0)
				exCredit = c.getExtCredit();

			VendorCategory vc = new VendorCategory();
			String vcName = vc.CVCategory(c.getType());

			Loger.log("The country name is ============" + c.getCountry());

			String sqlString = "insert into bca_clientvendor(ClientVendorID, Name,DateAdded, CustomerTitle, FirstName, LastName, Address1, Address2,"
					+ " City, State, Province, Country, ZipCode, Phone, CellPhone,Fax,HomePage, Email, CompanyID,"
					+ " ResellerTaxID,VendorOpenDebit,VendorAllowedCredit,Detail,Taxable,CVTypeID,CVCategoryID,CVCategoryName,Active,Deleted,Status) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

			pstmt = con.prepareStatement(sqlString);
			pstmt.setInt(1, cvID);

			pstmt.setString(2, c.getCname());
			pstmt.setString(3, (c.getDateAdded().equals("")) ? String.valueOf(pinfo.getdate(" now() "))
					: String.valueOf(pinfo.getdate(c.getDateAdded())));
			pstmt.setString(4, c.getTitle());
			pstmt.setString(5, c.getFirstName());
			pstmt.setString(6, c.getLastName());
			pstmt.setString(7, c.getAddress1());
			pstmt.setString(8, c.getAddress2());
			pstmt.setString(9, c.getCity());
			pstmt.setString(10, c.getState());
			pstmt.setString(11, c.getProvince());
			pstmt.setString(12, c.getCountry());
			pstmt.setString(13, c.getZipCode());
			pstmt.setString(14, c.getPhone());
			pstmt.setString(15, c.getCellPhone());
			pstmt.setString(16, c.getFax());
			pstmt.setString(17, c.getHomePage());
			pstmt.setString(18, c.getEmail());
			pstmt.setString(19, compID);
			pstmt.setString(20, c.getTexID());
			pstmt.setString(21, oBal);
			pstmt.setString(22, exCredit);
			pstmt.setString(23, c.getMemo());
			pstmt.setInt(24, istaxable);
			pstmt.setInt(25, isAlsoClient);
			pstmt.setString(26, c.getType());
			pstmt.setString(27, vcName);
			pstmt.setString(28, "1");
			pstmt.setString(29, "0");
			pstmt.setString(30, status);

			Loger.log(sqlString);

			int num = pstmt.executeUpdate();

			if (num > 0) {
				ret = true;
				Loger.log("num:" + num);
			}
			Loger.log("@@@@@@@@@@@@@@@@@@@");
			if (c.getShipping() != null && c.getShipping().trim().length() > 0)
				pinfo.updateClientVendor("ShipCarrierID", c.getShipping(), cvID);

			if (c.getPaymentType() != null && c.getPaymentType().trim().length() > 0)
				pinfo.updateClientVendor("PaymentTypeID", c.getPaymentType(), cvID);

			if (c.getRep() != null && c.getRep().trim().length() > 0)
				pinfo.updateClientVendor("SalesRepID", c.getRep(), cvID);

			if (c.getTerm() != null && c.getTerm().trim().length() > 0)
				pinfo.updateClientVendor("TermID", c.getTerm(), cvID);

			if (c.getCcType() != null && c.getCcType().trim().length() > 0) {
				pinfo.updateClientVendor("CCTypeID", c.getCcType(), cvID);
			}
			pinfo.insertVendorCreditCard(cvID, c.getCcType(), c.getCardNo(), c.getExpDate(), c.getCw2(),
					c.getCardHolderName(), c.getCardBillAddress(), c.getCardZip());
			int bsAddID = pinfo.getLastBsAdd() + 1;

			setStatus(cvID, bsAddID);
			Loger.log("The country name is ============" + c.getBscountry());
			Loger.log("----------------->>>>>>>>>>>The Bill to State is---------------->>>>>>>>>>>> " + c.getBsstate());
			Loger.log(
					"----------------->>>>>>>>>>>The Shipt  to State is---------------->>>>>>>>>>>> " + c.getShstate());

			insertVendorBSAddress(cvID, bsAddID, c.getBscname(), c.getBsfirstName(), c.getBslastName(),
					c.getBsaddress1(), c.getBsaddress2(), c.getBscity(), c.getBsstate(), c.getBsprovince(),
					c.getBscountry(), c.getBszipCode(), "1");

			Loger.log("The Ship country code is" + c.getShcountry());

			insertVendorBSAddress(cvID, bsAddID, c.getShcname(), c.getShfirstName(), c.getShlastName(),
					c.getShaddress1(), c.getShaddress2(), c.getShcity(), c.getShstate(), c.getShprovince(),
					c.getShcountry(), c.getShzipCode(), "0");

			pinfo.insertVFCharge(cvID, useIndividualFinanceCharges, c.getAnnualIntrestRate(), c.getMinFCharges(),
					c.getGracePrd(), AssessFinanceChk, FChargeInvoiceChk);

			int i;
			String sql;
			String serviceID = c.getTable_serID();

			String serviceBal = c.getTable_bal();
			String defaultser = c.getTable_defaultVal();

			String invStyleID = c.getTable_invId();

			sql = "delete from bca_clientvendorservice where ClientVendorID = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, cvID);
			int qryRet = ps.executeUpdate();
			Loger.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>The No. of rows deleted are=" + qryRet);

			if (!(serviceID.equals("") || invStyleID.equals("") || serviceBal.equals(""))) {

				String temp[], temp2[], temp3[];
				temp = serviceID.split(";"); // serviceID is in form like
				// 3;6;8;
				temp2 = invStyleID.split(";");
				temp3 = serviceBal.split(";");

				java.sql.Date d = new java.sql.Date(new Date().getTime());

				for (i = 0; i < temp.length; i++) {
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

					System.out.println("\ninvstyle=" + temp2[i] + ", bal=" + temp3[i] + ", servID=" + temp[i]);

					qryRet = ps.executeUpdate();
				}

			}

		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class Employee and  method -insertEmployee " + " " + ee.toString());
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

	public boolean insertVendorBSAddress(int cvID, int bsID, String cname, String fname, String lname, String add1,
			String add2, String city, String state, String province, String country, String zip, String addressType) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();

		if (db == null)
			return ret;
		con = db.getConnection();
		if (con == null)
			return ret;
		PurchaseInfo pinfo = new PurchaseInfo();

		try {
			String sqlString = "insert into bca_bsaddress(BSAddressID,ClientVendorID, Name,FirstName,"
					+ " LastName,Address1, Address2, City,ZipCode,Country,State,Province,AddressType,DateAdded,Status) "
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			Loger.log("BSAddress Query-------------->" + sqlString);
			pstmt = con.prepareStatement(sqlString);
			pstmt.setInt(1, bsID);
			pstmt.setInt(2, cvID);
			pstmt.setString(3, cname);
			pstmt.setString(4, fname);
			pstmt.setString(5, lname);
			pstmt.setString(6, add1);
			pstmt.setString(7, add2);
			pstmt.setString(8, city);
			pstmt.setString(9, zip);
			pstmt.setString(10, country);
			pstmt.setString(11, state);
			pstmt.setString(12, province);
			pstmt.setString(13, addressType);
			pstmt.setDate(14, pinfo.getdate("now()"));
			pstmt.setString(15, "U");

			int num = pstmt.executeUpdate();

			if (num > 0) {
				ret = true;
				Loger.log("num:" + num);
			}
			pstmt.close();

		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class Employee and  method -insertEmployee " + " " + ee.toString());
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

	public boolean setStatus(int cvID, int bsID) {

		boolean ret = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();

		if (db == null)
			return ret;
		con = db.getConnection();
		if (con == null)
			return ret;

		try {
			Loger.log("The bsaAddressID is" + bsID);
			String sqlUpdate = "update bca_bsaddress set  Status='0' where ClientVendorID = ? ";
			pstmt = con.prepareStatement(sqlUpdate);
			Loger.log("The CVID VALUE IS " + cvID);
			pstmt.setInt(1, cvID);
			// pstmt.setInt(2,bsID);
			int updateresult = pstmt.executeUpdate();
			Loger.log("The Updated to 0 are " + updateresult);

		} catch (Exception e) {
			// TODO: handle exception
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

	public void setDefaultVal(int cvID) {
		Connection con = null;
		PreparedStatement pstmt5 = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();

		try {
			Loger.log("inside matched try");
			String sqlUpdate = "update bca_clientvendorservice set  DefaultService='0' where ClientVendorID = ?";
			pstmt5 = con.prepareStatement(sqlUpdate);
			Loger.log("The CVID VALUE IS " + cvID);
			pstmt5.setInt(1, cvID);
			int updateresult = pstmt5.executeUpdate();

			Loger.log("The no of uptation done is " + updateresult);

		} catch (Exception e) {
			Loger.log(e.toString());
		} finally {
			try {
				if (pstmt5 != null) {
					db.close(pstmt5);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
	}

	/**
	 * @return
	 * @author: Sarfraz-Malik
	 */
	public List<String> getCustomerSalesOrderNums(String custID, String compId) {
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		List<String> orderNums = new ArrayList<>();
		try {
			String sql = "SELECT SONum FROM bca_invoice WHERE ClientVendorID=? and CompanyID=? AND invoiceStatus in (0,2) AND InvoiceTypeID in (1,7,9) AND SONum>0;";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, custID);
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orderNums.add(rs.getString("SONum"));
			}
		} catch (SQLException ex) {
			Loger.log("Exception in getCustomerSalesOrderNums Function " + ex.toString());
			ex.printStackTrace();
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
		return orderNums;
	}

	public InvoiceDto getRecordForSalesOrder(String compId, String orderNo, InvoiceDto form,
			HttpServletRequest request) {
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT i.InvoiceID,i.ClientVendorID,i.RefNum,i.InvoiceStyleID,i.SalesRepID,i.TermID,i.PaymentTypeID,i.ShipCarrierID,"
					+ "i.MessageID,i.SalesTaxID,i.Weight,i.SubTotal,i.Tax,i.SH,i.Total,i.AdjustedTotal,i.BSAddressID,i.CompanyID,"
					+ "date_format(i.DateConfirmed,'%m-%d-%Y') as DateConfirmed, date_format(i.DateAdded,'%m-%d-%Y') as DateAdded,"
					+ "i.Taxable,i.Balance,i.InvoiceTypeID,i.Shipped,i.ServiceID,i.IsPaymentCompleted,i.Memo,i.isPending, "
					+ "r.Name AS RepName, t.Name As TermName, p.Name AS PaymentTypeName, s.Name AS ShipCarrierName, m.Name AS MessageName "
					+ "FROM bca_invoice As i LEFT JOIN bca_salesrep AS r ON r.SalesRepID=i.SalesRepID "
					+ "LEFT JOIN bca_term AS t ON t.TermID=i.TermID LEFT JOIN bca_paymenttype AS p ON p.PaymentTypeID=i.PaymentTypeID "
					+ "LEFT JOIN bca_shipcarrier AS s ON s.ShipCarrierID=i.ShipCarrierID LEFT JOIN bca_message AS m ON m.MessageID=i.MessageID "
					+ "WHERE i.SONum=? and i.CompanyID=? AND i.invoiceStatus in (0,2) AND i.InvoiceTypeID in (1,7,9)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, Long.parseLong(orderNo));
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			int invoiceID = 0;
			if (rs.next()) {
				invoiceID = rs.getInt("InvoiceID");
				form.setCustID(rs.getString("ClientVendorID"));
				form.setPoNum(rs.getString("RefNum")); // purches order
				form.setOrderNo(orderNo);
				form.setInvoiceStyle(rs.getString("InvoiceStyleID"));

				form.setRep(rs.getString("RepName") != null ? rs.getString("RepName") : rs.getString("SalesRepID"));
				form.setTerm(rs.getString("TermName") != null ? rs.getString("TermName") : rs.getString("TermID"));
				form.setPayMethod(rs.getString("PaymentTypeName") != null ? rs.getString("PaymentTypeName")
						: rs.getString("PaymentTypeID"));
				form.setVia(rs.getString("ShipCarrierName") != null ? rs.getString("ShipCarrierName")
						: rs.getString("ShipCarrierID"));
				form.setMessage(
						rs.getString("MessageName") != null ? rs.getString("MessageName") : rs.getString("MessageID"));
				form.setSalesTaxID(rs.getString("SalesTaxID"));
				form.setServiceID(rs.getInt("ServiceID"));
				form.setWeight(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Weight")))));
				form.setSubtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SubTotal")))));

				form.setTax(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Tax")))));
				form.setShipping(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SH")))));
				form.setTotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Total")))));
				form.setAdjustedtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("AdjustedTotal")))));
				form.setBsAddressID(rs.getString("BSAddressID"));
				form.setCompanyID(rs.getString("CompanyID"));
				form.setTaxID(rs.getString("SalesTaxID"));
				form.setShipDate(rs.getString("DateConfirmed"));
				form.setOrderDate(rs.getString("DateAdded"));

				form.setBalance(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Balance")))));
				form.setIsPending(rs.getInt("isPending") == 1 ? "true" : "false");
				form.setTaxable(rs.getInt("Taxable") == 1 ? "true" : "false");
				form.setItemShipped(rs.getInt("Shipped") == 1 ? "true" : "false");
				form.setPaid(rs.getInt("IsPaymentCompleted") == 1 ? "true" : "false");
				form.setMemo(rs.getString("Memo"));
			}
			/* Bill Address */
			ArrayList<InvoiceDto> billAddresses = billAddress(compId, form.getCustID());
			if (!billAddresses.isEmpty()) {
				InvoiceDto invoiceBillAddr = billAddresses.get(0);
				form.setBsAddressID(invoiceBillAddr.getBsAddressID());
				String billTo = invoiceBillAddr.getBillTo();
				form.setBillTo(billTo != null ? billTo.replace("\n", "<br>") : "");
			}
			/* Ship Address */
			ArrayList<InvoiceDto> shipAddresses = shipAddress(compId, form.getCustID());
			if (!shipAddresses.isEmpty()) {
				InvoiceDto invoiceShipAddr = shipAddresses.get(0);
				form.setShAddressID(invoiceShipAddr.getShAddressID());
				String shipTo = invoiceShipAddr.getShipTo();
				form.setShipTo(shipTo != null ? shipTo.replace("\n", "<br>") : "");
			}
			String clientSQL = "SELECT cv.Name,cv.FirstName,cv.LastName,cv.Address1,cv.Address2,cv.City,cv.State,cv.Country,cv.ZipCode,"
					+ "cv.Email,cv.Phone, ct.Name As CityName, s.name AS StateName, c.name AS CountryName "
					+ "FROM bca_clientvendor AS cv LEFT JOIN bca_countries AS c ON c.id=cv.Country LEFT JOIN bca_states AS s ON s.id=cv.State "
					+ "LEFT JOIN bca_cities AS ct ON ct.id=cv.City WHERE cv.ClientVendorID=?";
			pstmt = con.prepareStatement(clientSQL);
			pstmt.setString(1, form.getCustID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				form.setCompanyName(rs.getString("Name"));
				form.setFullName(rs.getString("FirstName") + " " + rs.getString("LastName"));
				form.setFirstName(rs.getString("FirstName"));
				form.setLastName(rs.getString("LastName"));
				form.setAddress1(rs.getString("Address1"));
				form.setAddress2(rs.getString("Address2"));
				form.setCity(rs.getString("CityName") != null ? rs.getString("CityName") : rs.getString("City"));
				form.setState(rs.getString("StateName") != null ? rs.getString("StateName") : rs.getString("State"));
				form.setCountry(rs.getString("CountryName") != null ? rs.getString("CountryName")
						: rs.getString("CountryName"));
				form.setZipcode(rs.getString("ZipCode"));
				form.setEmailAddr(rs.getString("Email"));
				String phoneNumber = rs.getString("Phone");
				if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
					form.setBillTo(form.getBillTo() + "<br>" + phoneNumber);
					form.setShipTo(form.getShipTo() + "<br>" + phoneNumber);
				}
			}
			itemList(invoiceID, compId, request, form);
		} catch (SQLException ex) {
			Loger.log("Exception in getRecord Function " + ex.toString());
			ex.printStackTrace();
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

	public ArrayList getSalesOrderRecord(HttpServletRequest request, InvoiceDto form, String compId, long OrderNo) { // Sales
																														// Order
																														// Fetch
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null, rs2 = null;
		ArrayList<InvoiceDto> list = new ArrayList<>();
		try {
			String sql = " select InvoiceID,ClientVendorID,RefNum,InvoiceStyleID,SalesRepID,TermID,PaymentTypeID,"
					+ " ShipCarrierID,MessageID,SalesTaxID,Weight,SubTotal,Tax,SH,Total,AdjustedTotal,"
					+ " BSAddressID,CompanyID,date_format(DateConfirmed,'%m-%d-%Y') as DateConfirmed,"
					+ " date_format(DateAdded,'%m-%d-%Y') as DateAdded ,Taxable,Balance,InvoiceTypeID,"
					+ " Shipped,ServiceID,IsPaymentCompleted,Memo,isPending " + " FROM bca_invoice WHERE CompanyID="
					+ compId;
			if (OrderNo > 0) {
				sql = sql + " AND invoiceStatus in (0,2) AND InvoiceTypeID in (1,7,9) AND SONum=" + OrderNo;
			} else {
				sql = sql + " AND invoiceStatus=0 AND InvoiceTypeID=7";
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int invoiceID = 0;
			String style = "";
			while (rs.next()) {
				if (!list.isEmpty()) {
					form = new InvoiceDto();
				}
				invoiceID = rs.getInt("InvoiceID");
				form.setCustID(rs.getString("ClientVendorID"));
				form.setPoNum(rs.getString("RefNum")); // sales Order
				form.setOrderNo(String.valueOf(OrderNo));
				style = rs.getString("InvoiceStyleID");
				form.setInvoiceStyle(style);

				form.setRep(rs.getString("SalesRepID"));
				form.setTerm(rs.getString("TermID"));
				form.setPayMethod(rs.getString("PaymentTypeID"));
				form.setVia(rs.getString("ShipCarrierID"));
				form.setMessage(rs.getString("MessageID"));
				form.setSalesTaxID(rs.getString("SalesTaxID"));
				form.setServiceID(rs.getInt("ServiceID"));
				form.setWeight(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Weight")))));
				form.setSubtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SubTotal")))));

				form.setTax(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Tax")))));
				form.setShipping(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SH")))));
				form.setTotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Total")))));
				form.setAdjustedtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("AdjustedTotal")))));
				form.setBsAddressID(rs.getString("BSAddressID"));
				form.setCompanyID(rs.getString("CompanyID"));
				form.setTaxID(rs.getString("SalesTaxID"));
				form.setShipDate(rs.getString("DateConfirmed"));
				form.setOrderDate(rs.getString("DateAdded"));
				form.setBalance(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Balance")))));

				form.setIsPending(rs.getInt("InvoiceTypeID") == 7 ? "true" : "false");
				form.setTaxable(rs.getInt("Taxable") == 1 ? "true" : "false");
				form.setItemShipped(rs.getInt("Shipped") == 1 ? "true" : "false");
				form.setPaid(rs.getInt("IsPaymentCompleted") == 1 ? "true" : "false");
				form.setMemo(rs.getString("Memo"));
				form.setIsPending(rs.getInt("isPending") == 1 ? "true" : "false");

				/* Bill Address */
				ArrayList<InvoiceDto> billAddresses = billAddress(compId, form.getCustID());
				if (!billAddresses.isEmpty()) {
					InvoiceDto invoiceBillAddr = billAddresses.get(0);
					form.setBsAddressID(invoiceBillAddr.getBsAddressID());
					form.setBillTo(invoiceBillAddr.getBillTo());
				}
				/* Ship Address */
				ArrayList<InvoiceDto> shipAddresses = shipAddress(compId, form.getCustID());
				if (!shipAddresses.isEmpty()) {
					InvoiceDto invoiceShipAddr = shipAddresses.get(0);
					form.setShAddressID(invoiceShipAddr.getShAddressID());
					form.setShipTo(invoiceShipAddr.getShipTo());
				}
				pstmt2 = con.prepareStatement(
						"select LastName,FirstName from bca_clientvendor where ClientVendorID=? and CVTypeID in (1,2)");
				pstmt2.setString(1, form.getCustID());
				rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					if (form.getServiceID() == 0) {
						form.setFullName(rs2.getString("LastName") + ", " + rs2.getString("FirstName"));
					} else
						form.setFullName(rs2.getString("LastName") + ", " + rs2.getString("FirstName") + "["
								+ getServiceName(form.getServiceID()) + "]");
				}
				request.setAttribute("CustomerName", form.getFullName());
				list.add(form);
				/* Item List in the cart */
				itemList(invoiceID, compId, request, form);
				request.setAttribute("Style", style);
			}
		} catch (SQLException ex) {
			Loger.log("Exception in getRecord Function" + ex.toString());
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					db.close(rs);
				}
				if (rs2 != null) {
					db.close(rs2);
				}
				if (pstmt != null) {
					db.close(pstmt);
				}
				if (pstmt2 != null) {
					db.close(pstmt2);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(e.toString());
			}
		}
		return list;
	}

	public ArrayList getRecord(HttpServletRequest request, InvoiceDto form, String compId, long OrderNo) {

		ArrayList<InvoiceDto> list = new ArrayList<>();
		String action = request.getParameter("tabid");

		List<BcaInvoice> bcaInvoice = null;
		try {
			if (form.getTabid().equalsIgnoreCase("IBLU")) { // Send Invoice it

				String query = "SELECT bi from BcaInvoice bi where bi.company.companyId= :companyId and bi.invoiceStatus IN :invoiceStatus"
						+ " and bi.invoiceType.invoiceTypeId IN :invoiceTypeId and bi.sonum =:sonum and bi.deleted = :deleted";
				TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
				JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
				JpaHelper.addParameter(typedQuery, query, "sonum", (int) OrderNo);
				JpaHelper.addParameter(typedQuery, query, "invoiceStatus", Arrays.asList(0, 2));
				JpaHelper.addParameter(typedQuery, query, "invoiceTypeId", Arrays.asList(1, 7, 9));
				JpaHelper.addParameter(typedQuery, query, "deleted", 0);
				bcaInvoice = typedQuery.getResultList();

			} else {
				// This Query Find Sales Order Num (SONum)
				StringBuffer queryBuffer = new StringBuffer(
						"select bi from BcaInvoice bi where bi.company.companyId =:companyId ");

				String query = "";
				if (OrderNo > 0) {
					queryBuffer.append("and bi.invoiceStatus in :invoiceStatus and bi.orderNum= :orderNum");
					query = queryBuffer.toString();
					TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
					JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
					JpaHelper.addParameter(typedQuery, query, "invoiceStatus", Arrays.asList(0, 2));
					JpaHelper.addParameter(typedQuery, query, "orderNum", (int) OrderNo);
					bcaInvoice = typedQuery.getResultList();
				} else {
					queryBuffer.append(
							"and invoiceStatus = invoiceStatus and bi.invoiceTypeId=:invoiceTypeId and bi.orderNum>:orderNum");
					query = queryBuffer.toString();
					TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
					JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
					JpaHelper.addParameter(typedQuery, query, "invoiceStatus", 0);
					JpaHelper.addParameter(typedQuery, query, "invoiceTypeId", 1);
					JpaHelper.addParameter(typedQuery, query, "orderNum", 0);
					bcaInvoice = typedQuery.getResultList();

				}
			}

			int invoiceID = 0;
			String style = "";
			for (BcaInvoice invoice : bcaInvoice) {
				if (!list.isEmpty()) {
					form = new InvoiceDto();
				}
				invoiceID = invoice.getInvoiceId();
				if (null != invoice.getClientVendor()) {
					form.setCustID(String.valueOf(invoice.getClientVendor().getClientVendorId()));
					form.setClientVendorID(String.valueOf(invoice.getClientVendor().getClientVendorId()));
				}
				form.setPoNum(invoice.getRefNum()); // purches order
				form.setOrderNo(String.valueOf(invoice.getOrderNum()));
				if (form.getTabid().equalsIgnoreCase("IBLU")) { // Send Invoice it
//					form.setOrderNo(getNewOrderNo(compId)); // Send New Invoice num
					form.setOrderNo(String.valueOf(OrderNo));
				}
				if (null != invoice.getInvoiceStyle()) {
					style = String.valueOf(invoice.getInvoiceStyle().getInvoiceStyleId());
					form.setInvoiceStyle(style);
					request.setAttribute("Style", style);
				}
				if (null != invoice.getSalesRepId())
					form.setRep(String.valueOf(invoice.getSalesRepId()));
				if (null != invoice.getTerm())
					form.setTerm(String.valueOf(invoice.getTerm().getTermId()));
				if (null != invoice.getPaymentType())
					form.setPayMethod(String.valueOf(invoice.getPaymentType().getPaymentTypeId()));
				if (null != invoice.getShipCarrier())
					form.setVia(String.valueOf(invoice.getShipCarrier().getShipCarrierId()));

				if (null != invoice.getMessage())
					form.setMessage(String.valueOf(invoice.getMessage().getMessageId()));
				if (null != invoice.getSalesTax())
					form.setSalesTaxID(String.valueOf(invoice.getSalesTax().getSalesTaxId()));
				form.setServiceID(invoice.getServiceId());
				if (null != invoice.getWeight())
					form.setWeight(invoice.getWeight());
				if (null != invoice.getSubTotal())
					form.setSubtotal(invoice.getSubTotal());
				if (null != invoice.getTax())
					form.setTax(invoice.getTax());
				if (null != invoice.getSh())
					form.setShipping(invoice.getSh());
				form.setTotal(invoice.getTotal());
				form.setAdjustedtotal(invoice.getAdjustedTotal());
				if (null != invoice.getBsaddressId())
					form.setBsAddressID(String.valueOf(invoice.getBsaddressId()));
				if (null != invoice.getCompany())
					form.setCompanyID(String.valueOf(invoice.getCompany().getCompanyId()));
				if (null != invoice.getSalesTax())
					form.setTaxID(String.valueOf(invoice.getSalesTax().getSalesTaxId()));
				form.setShipDate(DateHelper.dateFormatter(invoice.getDateConfirmed()));
				form.setOrderDate(DateHelper.dateFormatter(invoice.getDateAdded()));

				form.setBalance(invoice.getBalance());
//				form.setIsPending(rs.getInt("InvoiceTypeID") == 7 ? "true": "false");
				form.setIsPending(invoice.getIsPending() == 1 ? "true" : "false"); // set value pending
				form.setTaxable(invoice.getTaxable() == 1 ? "true" : "false");
				form.setItemShipped(invoice.getShipped() == 1 ? "true" : "false");
				form.setPaid(invoice.getIsPaymentCompleted() ? "true" : "false");
				form.setMemo(invoice.getMemo());

				/* Bill Address */
				ArrayList<InvoiceDto> billAddresses = billAddress(compId, form.getCustID());
				if (!billAddresses.isEmpty()) {
					InvoiceDto invoiceBillAddr = billAddresses.get(0);
					form.setBsAddressID(invoiceBillAddr.getBsAddressID());
					form.setBillTo(invoiceBillAddr.getBillTo());
				}
				/* Ship Address */
				ArrayList<InvoiceDto> shipAddresses = shipAddress(compId, form.getCustID());
				if (!shipAddresses.isEmpty()) {
					InvoiceDto invoiceShipAddr = shipAddresses.get(0);
					form.setShAddressID(invoiceShipAddr.getShAddressID());
					form.setShipTo(invoiceShipAddr.getShipTo());
				}
				Optional<BcaClientvendor> clientVendor = bcaClientvendorRepository
						.findByClientVendorIdAndCvtypeIdIn(Integer.parseInt(form.getCustID()), Arrays.asList(1, 2));
				if (clientVendor.isPresent()) {
					if (form.getServiceID() == 0) {
						form.setFullName(clientVendor.get().getLastName() + ", " + clientVendor.get().getFirstName());
					} else {
						form.setFullName(clientVendor.get().getLastName() + ", " + clientVendor.get().getFirstName()
								+ "[" + getServiceName(form.getServiceID()) + "]");
					}
				}

				request.setAttribute("CustomerName", form.getFullName());
				/* Item List in the cart */
				itemList(invoiceID, compId, request, form);
				list.add(form);
			}
		} catch (Exception ex) {
			Loger.log("Exception in getRecord Function " + ex.toString());
			ex.printStackTrace();
		}
		return list;
	}

//	public ArrayList getRecord(HttpServletRequest request, InvoiceDto form, String compId, long OrderNo) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		PreparedStatement pstmt = null, pstmt2 = null;
//		ResultSet rs = null, rs2 = null;
//		;
//		ArrayList<InvoiceDto> list = new ArrayList<>();
//		String action = request.getParameter("tabid");
//		String sql = "";
//		try {
//			if (action.equalsIgnoreCase("IBLU")) { // Send Invoice it
//				sql = " select InvoiceID,ClientVendorID,RefNum,InvoiceStyleID,SalesRepID,TermID,PaymentTypeID,"
//						+ " ShipCarrierID,MessageID,SalesTaxID,Weight,SubTotal,Tax,SH,Total,AdjustedTotal,"
//						+ " BSAddressID,CompanyID,date_format(DateConfirmed,'%m-%d-%Y') as DateConfirmed,"
//						+ " date_format(DateAdded,'%m-%d-%Y') as DateAdded ,Taxable,Balance,InvoiceTypeID,"
//						+ " Shipped,ServiceID,IsPaymentCompleted,Memo,isPending "
//						+ "FROM bca_invoice WHERE SONum =? and CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID in (1,7,9)";
//			} else {
//				// This Query Find Sales Order Num (SONum)
//				sql = "select InvoiceID,ClientVendorID,OrderNum,RefNum,InvoiceStyleID,SalesRepID,TermID,PaymentTypeID,"
//						+ "ShipCarrierID,MessageID,SalesTaxID,Weight,SubTotal,Tax,SH,Total,AdjustedTotal,"
//						+ "BSAddressID,CompanyID,date_format(DateConfirmed,'%m-%d-%Y') as DateConfirmed,"
//						+ "date_format(DateAdded,'%m-%d-%Y') as DateAdded ,Taxable,Balance,InvoiceTypeID,"
//						+ "Shipped,ServiceID,IsPaymentCompleted,Memo,isPending " + "FROM bca_invoice WHERE CompanyID = "
//						+ compId;
//				if (OrderNo > 0) {
//					sql = sql + " and invoiceStatus in (0,2) and OrderNum=" + OrderNo;
//				} else {
//					sql = sql + " and invoiceStatus=0 and InvoiceTypeID=1 and OrderNum>0";
//				}
//			}
//			pstmt = con.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			int invoiceID = 0;
//			String style = "";
//			while (rs.next()) {
//				if (!list.isEmpty()) {
//					form = new InvoiceDto();
//				}
//				invoiceID = rs.getInt("InvoiceID");
//				form.setCustID(rs.getString("ClientVendorID"));
//				form.setClientVendorID(rs.getString("ClientVendorID"));
//				form.setPoNum(rs.getString("RefNum")); // purches order
//				form.setOrderNo(rs.getString("OrderNum"));
//				if (action.equalsIgnoreCase("IBLU")) { // Send Invoice it
//					form.setOrderNo(getNewOrderNo(compId)); // Send New Invoice num
//				}
//				style = rs.getString("InvoiceStyleID");
//				form.setInvoiceStyle(style);
//				request.setAttribute("Style", style);
//
//				form.setRep(rs.getString("SalesRepID"));
//				form.setTerm(rs.getString("TermID"));
//				form.setPayMethod(rs.getString("PaymentTypeID"));
//				form.setVia(rs.getString("ShipCarrierID"));
//				form.setMessage(rs.getString("MessageID"));
//				form.setSalesTaxID(rs.getString("SalesTaxID"));
//				form.setServiceID(rs.getInt("ServiceID"));
//				form.setWeight(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Weight")))));
//				form.setSubtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SubTotal")))));
//
//				form.setTax(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Tax")))));
//				form.setShipping(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SH")))));
//				form.setTotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Total")))));
//				form.setAdjustedtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("AdjustedTotal")))));
//				form.setBsAddressID(rs.getString("BSAddressID"));
//				form.setCompanyID(rs.getString("CompanyID"));
//				form.setTaxID(rs.getString("SalesTaxID"));
//				form.setShipDate(rs.getString("DateConfirmed"));
//				form.setOrderDate(rs.getString("DateAdded"));
//
//				form.setBalance(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Balance")))));
////				form.setIsPending(rs.getInt("InvoiceTypeID") == 7 ? "true": "false");
//				form.setIsPending(rs.getInt("isPending") == 1 ? "true" : "false"); // set value pending
//				form.setTaxable(rs.getInt("Taxable") == 1 ? "true" : "false");
//				form.setItemShipped(rs.getInt("Shipped") == 1 ? "true" : "false");
//				form.setPaid(rs.getInt("IsPaymentCompleted") == 1 ? "true" : "false");
//				form.setMemo(rs.getString("Memo"));
//
//				/* Bill Address */
//				ArrayList<InvoiceDto> billAddresses = billAddress(compId, form.getCustID());
//				if (!billAddresses.isEmpty()) {
//					InvoiceDto invoiceBillAddr = billAddresses.get(0);
//					form.setBsAddressID(invoiceBillAddr.getBsAddressID());
//					form.setBillTo(invoiceBillAddr.getBillTo());
//				}
//				/* Ship Address */
//				ArrayList<InvoiceDto> shipAddresses = shipAddress(compId, form.getCustID());
//				if (!shipAddresses.isEmpty()) {
//					InvoiceDto invoiceShipAddr = shipAddresses.get(0);
//					form.setShAddressID(invoiceShipAddr.getShAddressID());
//					form.setShipTo(invoiceShipAddr.getShipTo());
//				}
//				String clientName = "select LastName,FirstName from bca_clientvendor where ClientVendorID=? and CVTypeID in (1,2) ";
//				pstmt2 = con.prepareStatement(clientName);
//				pstmt2.setString(1, form.getCustID());
//				rs2 = pstmt2.executeQuery();
//				if (rs2.next()) {
//					if (form.getServiceID() == 0) {
//						form.setFullName(rs2.getString("LastName") + ", " + rs2.getString("FirstName"));
//					} else {
//						form.setFullName(rs2.getString("LastName") + ", " + rs2.getString("FirstName") + "["
//								+ getServiceName(form.getServiceID()) + "]");
//					}
//				}
//				request.setAttribute("CustomerName", form.getFullName());
//				/* Item List in the cart */
//				itemList(invoiceID, compId, request, form);
//				list.add(form);
//			}
//		} catch (SQLException ex) {
//			Loger.log("Exception in getRecord Function " + ex.toString());
//			ex.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					db.close(rs);
//				}
//				if (rs2 != null) {
//					db.close(rs2);
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
//		return list;
//	}

	/**
	 * @return
	 * @author: Sarfraz-Malik
	 */
	public List<String> getCustomerInvoiceOrderNums(String custID, String compId) {
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		List<String> orderNums = new ArrayList<>();
		try {
			String sql = "SELECT OrderNum FROM bca_invoice WHERE ClientVendorID=? and CompanyID=? AND invoiceStatus in (0,2) AND OrderNum>0;";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, custID);
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orderNums.add(rs.getString("OrderNum"));
			}
		} catch (SQLException ex) {
			Loger.log("Exception in getRecord Function " + ex.toString());
			ex.printStackTrace();
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
		return orderNums;
	}

	public InvoiceDto getRecordForInvoice(String compId, String orderNo, InvoiceDto form, HttpServletRequest request) {
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT i.InvoiceID,i.ClientVendorID,i.RefNum,i.InvoiceStyleID,i.SalesRepID,i.TermID,i.PaymentTypeID,i.ShipCarrierID,"
					+ "i.MessageID,i.SalesTaxID,i.Weight,i.SubTotal,i.Tax,i.SH,i.Total,i.AdjustedTotal,i.BSAddressID,i.CompanyID,"
					+ "date_format(i.DateConfirmed,'%m-%d-%Y') as DateConfirmed, date_format(i.DateAdded,'%m-%d-%Y') as DateAdded,"
					+ "i.Taxable,i.Balance,i.InvoiceTypeID,i.Shipped,i.ServiceID,i.IsPaymentCompleted,i.Memo,i.isPending, "
					+ "r.Name AS RepName, t.Name As TermName, p.Name AS PaymentTypeName, s.Name AS ShipCarrierName, m.Name AS MessageName "
					+ "FROM bca_invoice As i LEFT JOIN bca_salesrep AS r ON r.SalesRepID=i.SalesRepID "
					+ "LEFT JOIN bca_term AS t ON t.TermID=i.TermID LEFT JOIN bca_paymenttype AS p ON p.PaymentTypeID=i.PaymentTypeID "
					+ "LEFT JOIN bca_shipcarrier AS s ON s.ShipCarrierID=i.ShipCarrierID LEFT JOIN bca_message AS m ON m.MessageID=i.MessageID "
					+ "WHERE i.OrderNum=? and i.CompanyID=? AND i.invoiceStatus in (0,2)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, Long.parseLong(orderNo));
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			int invoiceID = 0;
			if (rs.next()) {
				invoiceID = rs.getInt("InvoiceID");
				form.setCustID(rs.getString("ClientVendorID"));
				form.setPoNum(rs.getString("RefNum")); // purches order
				form.setOrderNo(orderNo);
				form.setInvoiceStyle(rs.getString("InvoiceStyleID"));

				form.setRep(rs.getString("RepName") != null ? rs.getString("RepName") : rs.getString("SalesRepID"));
				form.setTerm(rs.getString("TermName") != null ? rs.getString("TermName") : rs.getString("TermID"));
				form.setPayMethod(rs.getString("PaymentTypeName") != null ? rs.getString("PaymentTypeName")
						: rs.getString("PaymentTypeID"));
				form.setVia(rs.getString("ShipCarrierName") != null ? rs.getString("ShipCarrierName")
						: rs.getString("ShipCarrierID"));
				form.setMessage(
						rs.getString("MessageName") != null ? rs.getString("MessageName") : rs.getString("MessageID"));
				form.setSalesTaxID(rs.getString("SalesTaxID"));
				form.setServiceID(rs.getInt("ServiceID"));
				form.setWeight(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Weight")))));
				form.setSubtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SubTotal")))));

				form.setTax(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Tax")))));
				form.setShipping(Double.parseDouble(truncate(String.valueOf(rs.getDouble("SH")))));
				form.setTotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Total")))));
				form.setAdjustedtotal(Double.parseDouble(truncate(String.valueOf(rs.getDouble("AdjustedTotal")))));
				form.setBsAddressID(rs.getString("BSAddressID"));
				form.setCompanyID(rs.getString("CompanyID"));
				form.setTaxID(rs.getString("SalesTaxID"));
				form.setShipDate(rs.getString("DateConfirmed"));
				form.setOrderDate(rs.getString("DateAdded"));

				form.setBalance(Double.parseDouble(truncate(String.valueOf(rs.getDouble("Balance")))));
				form.setIsPending(rs.getInt("isPending") == 1 ? "true" : "false");
				form.setTaxable(rs.getInt("Taxable") == 1 ? "true" : "false");
				form.setItemShipped(rs.getInt("Shipped") == 1 ? "true" : "false");
				form.setPaid(rs.getInt("IsPaymentCompleted") == 1 ? "true" : "false");
				form.setMemo(rs.getString("Memo"));
			}
			/* Bill Address */
			ArrayList<InvoiceDto> billAddresses = billAddress(compId, form.getCustID());
			if (!billAddresses.isEmpty()) {
				InvoiceDto invoiceBillAddr = billAddresses.get(0);
				form.setBsAddressID(invoiceBillAddr.getBsAddressID());
				String billTo = invoiceBillAddr.getBillTo();
				form.setBillTo(billTo != null ? billTo.replace("\n", "<br>") : "");
			}
			/* Ship Address */
			ArrayList<InvoiceDto> shipAddresses = shipAddress(compId, form.getCustID());
			if (!shipAddresses.isEmpty()) {
				InvoiceDto invoiceShipAddr = shipAddresses.get(0);
				form.setShAddressID(invoiceShipAddr.getShAddressID());
				String shipTo = invoiceShipAddr.getShipTo();
				form.setShipTo(shipTo != null ? shipTo.replace("\n", "<br>") : "");
			}
			String clientSQL = "SELECT cv.Name,cv.FirstName,cv.LastName,cv.Address1,cv.Address2,cv.City,cv.State,cv.Country,cv.ZipCode,"
					+ "cv.Email,cv.Phone, ct.Name As CityName, s.name AS StateName, c.name AS CountryName "
					+ "FROM bca_clientvendor AS cv LEFT JOIN bca_countries AS c ON c.id=cv.Country LEFT JOIN bca_states AS s ON s.id=cv.State "
					+ "LEFT JOIN bca_cities AS ct ON ct.id=cv.City WHERE cv.ClientVendorID=?";
			pstmt = con.prepareStatement(clientSQL);
			pstmt.setString(1, form.getCustID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				form.setCompanyName(rs.getString("Name"));
				form.setFullName(rs.getString("FirstName") + " " + rs.getString("LastName"));
				form.setFirstName(rs.getString("FirstName"));
				form.setLastName(rs.getString("LastName"));
				form.setAddress1(rs.getString("Address1"));
				form.setAddress2(rs.getString("Address2"));
				form.setCity(rs.getString("CityName") != null ? rs.getString("CityName") : rs.getString("City"));
				form.setState(rs.getString("StateName") != null ? rs.getString("StateName") : rs.getString("State"));
				form.setCountry(rs.getString("CountryName") != null ? rs.getString("CountryName")
						: rs.getString("CountryName"));
				form.setZipcode(rs.getString("ZipCode"));
				form.setEmailAddr(rs.getString("Email"));
				String phoneNumber = rs.getString("Phone");
				if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
					form.setBillTo(form.getBillTo() + "<br>" + phoneNumber);
					form.setShipTo(form.getShipTo() + "<br>" + phoneNumber);
				}
			}
			itemList(invoiceID, compId, request, form);
		} catch (SQLException ex) {
			Loger.log("Exception in getRecord Function " + ex.toString());
			ex.printStackTrace();
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

	public void itemList(int invoiceID, String compId, HttpServletRequest request, InvoiceDto invoiceDto) {
		ArrayList<Item> cart = new ArrayList<>();
		try {
			StringBuilder invIDs = new StringBuilder();
			StringBuilder invCodes = new StringBuilder();
			StringBuilder invNames = new StringBuilder();
			StringBuilder invQtys = new StringBuilder();
			StringBuilder invUPrices = new StringBuilder();
			StringBuilder invUWeights = new StringBuilder();
			StringBuilder invTaxables = new StringBuilder();
			StringBuilder invItemIDs = new StringBuilder();
			StringBuilder invItemOrders = new StringBuilder();
			double taxTotal = 0;
			List<BcaCart> bcaCarts = bcaCartRepository.findByInvoiceIdAndCompanyId(invoiceID, Long.parseLong(compId));
			for (BcaCart bcacart : bcaCarts) {
				Item inForm = new Item();
				if (null != bcacart.getInventory())
					inForm.setInventoryID(String.valueOf(bcacart.getInventory().getInventoryId()));
				inForm.setInvCode(bcacart.getInventoryCode());
				inForm.setInvDesc(bcacart.getInventoryName());
				inForm.setItemTypeID(bcacart.getItemTypeId());
				inForm.setItemOrder(bcacart.getItemOrder());
				int qty = bcacart.getQty();
				double uprice = bcacart.getUnitPrice();
				inForm.setQty(qty);
				inForm.setUprice(uprice);
				if (null != bcacart.getUnitWeight())
					inForm.setWeight(bcacart.getUnitWeight());
				int tax = bcacart.getTaxable();
				inForm.setAmount(Double.parseDouble(truncate(String.valueOf(qty * uprice))));
				if (tax == 1) {
					inForm.setTax("Yes");
					taxTotal += (qty * uprice);
				} else if (tax == 0) {
					inForm.setTax("No");
				}
				cart.add(inForm);

				invIDs.append(inForm.getInventoryID() + ";");
				invCodes.append(inForm.getInvCode() + ";");
				invNames.append(inForm.getInvDesc() + ";");
				invQtys.append(inForm.getQty() + ";");
				invUPrices.append(inForm.getUprice() + ";");
				invUWeights.append(inForm.getWeight() + ";");
				invTaxables.append(inForm.getTax() + ";");
				invItemIDs.append(inForm.getItemTypeID() + ";");
				invItemOrders.append(inForm.getItemOrder() + ";");
			}

			invoiceDto.setItem(invIDs.toString());
			invoiceDto.setCode(invCodes.toString());
			invoiceDto.setDesc(invNames.toString());
			invoiceDto.setQty(invQtys.toString());
			invoiceDto.setUprice(invUPrices.toString());
			invoiceDto.setUnitWeight(invUWeights.toString());
			invoiceDto.setIsTaxable(invTaxables.toString());
			invoiceDto.setItemTypeID(invItemIDs.toString());
			invoiceDto.setItemOrder(invItemOrders.toString());

			InvoiceDto form = new InvoiceDto();
			form.setTaxValue(Double.parseDouble(truncate(String.valueOf(taxTotal))));
			request.setAttribute("TaxValue", form);
			request.setAttribute("Cart", cart);
			invoiceDto.setCart(cart);
			invoiceDto.setTaxValue(Double.parseDouble(truncate(String.valueOf(taxTotal))));
		} catch (Exception ex) {
			Loger.log("Exception in getRecord Function" + ex.toString());
			ex.printStackTrace();
		}
	}

//	public void itemList(int invoiceID, String compId, HttpServletRequest request, InvoiceDto invoiceDto) {
//		PreparedStatement pstmt = null;
//		SQLExecutor db = new SQLExecutor();
//		ResultSet rs = null;
//		ArrayList<Item> cart = new ArrayList<>();
//		Connection con = db.getConnection();
//		try {
//			StringBuilder invIDs = new StringBuilder();
//			StringBuilder invCodes = new StringBuilder();
//			StringBuilder invNames = new StringBuilder();
//			StringBuilder invQtys = new StringBuilder();
//			StringBuilder invUPrices = new StringBuilder();
//			StringBuilder invUWeights = new StringBuilder();
//			StringBuilder invTaxables = new StringBuilder();
//			StringBuilder invItemIDs = new StringBuilder();
//			StringBuilder invItemOrders = new StringBuilder();
//			double taxTotal = 0;
//
//			pstmt = con.prepareStatement("select * from bca_cart where InvoiceID=? and companyID=?");
//			pstmt.setInt(1, invoiceID);
//			pstmt.setString(2, compId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Item inForm = new Item();
//				inForm.setInventoryID(rs.getString("InventoryID"));
//				inForm.setInvCode(rs.getString("InventoryCode"));
//				inForm.setInvDesc(rs.getString("InventoryName"));
//				inForm.setItemTypeID(rs.getInt("ItemTypeID"));
//				inForm.setItemOrder(rs.getInt("ItemOrder"));
//				int qty = rs.getInt("Qty");
//				double uprice = rs.getDouble("UnitPrice");
//				inForm.setQty(qty);
//				inForm.setUprice(uprice);
//				inForm.setWeight(rs.getDouble("UnitWeight"));
//				int tax = rs.getInt("Taxable");
//				inForm.setAmount(Double.parseDouble(truncate(String.valueOf(qty * uprice))));
//				if (tax == 1) {
//					inForm.setTax("Yes");
//					taxTotal += (qty * uprice);
//				} else if (tax == 0) {
//					inForm.setTax("No");
//				}
//				cart.add(inForm);
//
//				invIDs.append(inForm.getInventoryID() + ";");
//				invCodes.append(inForm.getInvCode() + ";");
//				invNames.append(inForm.getInvDesc() + ";");
//				invQtys.append(inForm.getQty() + ";");
//				invUPrices.append(inForm.getUprice() + ";");
//				invUWeights.append(inForm.getWeight() + ";");
//				invTaxables.append(inForm.getTax() + ";");
//				invItemIDs.append(inForm.getItemTypeID() + ";");
//				invItemOrders.append(inForm.getItemOrder() + ";");
//			}
//			invoiceDto.setItem(invIDs.toString());
//			invoiceDto.setCode(invCodes.toString());
//			invoiceDto.setDesc(invNames.toString());
//			invoiceDto.setQty(invQtys.toString());
//			invoiceDto.setUprice(invUPrices.toString());
//			invoiceDto.setUnitWeight(invUWeights.toString());
//			invoiceDto.setIsTaxable(invTaxables.toString());
//			invoiceDto.setItemTypeID(invItemIDs.toString());
//			invoiceDto.setItemOrder(invItemOrders.toString());
//
//			InvoiceDto form = new InvoiceDto();
//			form.setTaxValue(Double.parseDouble(truncate(String.valueOf(taxTotal))));
//			request.setAttribute("TaxValue", form);
//			request.setAttribute("Cart", cart);
//			invoiceDto.setCart(cart);
//			invoiceDto.setTaxValue(Double.parseDouble(truncate(String.valueOf(taxTotal))));
//		} catch (SQLException ex) {
//			Loger.log("Exception in getRecord Function" + ex.toString());
//			ex.printStackTrace();
//		} finally {
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
//	}

	public Long getInvoiceOrderNumberByBtnName(String compId, HttpServletRequest request) {

		Long orderNo = null;
		try {
			String action = request.getParameter("tabid");
			int currentIndex = Integer.valueOf(request.getParameter("index"));

			Long prevInvoiceOrderNo = (Long) request.getSession().getAttribute("prevInvoiceOrderNo");
			prevInvoiceOrderNo = prevInvoiceOrderNo != null ? prevInvoiceOrderNo : 0l;
			String query = "SELECT  bi FROM BcaInvoice bi where bi.company.companyId =:companyId "
					+ "and bi.invoiceStatus IN :invoiceStatus" + " and bi.invoiceType.invoiceTypeId =:invoiceTypeId "
					+ "and bi.orderNum>0 ORDER BY bi.orderNum";
			TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
			JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
			JpaHelper.addParameter(typedQuery, query, "invoiceStatus", Arrays.asList(0, 2));
			JpaHelper.addParameter(typedQuery, query, "invoiceTypeId", 1);

			if (action.equalsIgnoreCase("PreviousInvoice")) {
				BcaInvoice invoice = typedQuery.getResultList().stream().filter(x -> x.getOrderNum() < currentIndex)
						.sorted(Comparator.comparing(BcaInvoice::getOrderNum).reversed()).findFirst()
						.orElse(typedQuery.getResultList().stream()
								.sorted(Comparator.comparing(BcaInvoice::getOrderNum).reversed()).findFirst().get());

				orderNo = new Long(invoice.getOrderNum());

			}
			if (action.equalsIgnoreCase("NextInvoice")) {
				BcaInvoice invoice = typedQuery.getResultList().stream().filter(x -> x.getOrderNum() > currentIndex)
						.sorted(Comparator.comparing(BcaInvoice::getOrderNum)).findFirst()
						.orElse(typedQuery.getResultList().stream()
								.sorted(Comparator.comparing(BcaInvoice::getOrderNum)).findFirst().get());

				orderNo = new Long(invoice.getOrderNum());

			}
			if (action.equalsIgnoreCase("FirstInvoice")) {
				Optional<BcaInvoice> invoice = typedQuery.getResultList().stream()
						.sorted(Comparator.comparing(BcaInvoice::getOrderNum)).findFirst();
				if (invoice.isPresent()) {
					orderNo = new Long(invoice.get().getOrderNum());
				}
			}
			if (action.equalsIgnoreCase("LastInvoice")) {
				Optional<BcaInvoice> invoice = typedQuery.getResultList().stream()
						.sorted(Comparator.comparing(BcaInvoice::getOrderNum).reversed()).findFirst();
				if (invoice.isPresent()) {
					orderNo = new Long(invoice.get().getOrderNum());
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Loger.log("Exception in FirstOrderNo Function" + ex.toString());
		}

		return orderNo;
	}
//	public Long getInvoiceOrderNumberByBtnName(String compId, HttpServletRequest request) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		Long orderNo = null;
//		try {
//			List<Long> orderNumbers = new ArrayList<>();
//			String action = request.getParameter("tabid");
//			
//			Long prevInvoiceOrderNo = (Long) request.getSession().getAttribute("prevInvoiceOrderNo");
//			prevInvoiceOrderNo = prevInvoiceOrderNo != null ? prevInvoiceOrderNo : 0l;
//			String sql = "SELECT InvoiceID,OrderNum FROM bca_invoice WHERE CompanyID =? "
//					+ "and invoiceStatus in (0,2) and InvoiceTypeID=1 and OrderNum > 0 ORDER BY OrderNum ";
//			if (action.equalsIgnoreCase("PreviousInvoice")) {
//				sql = sql + "DESC";
//			} else {
//				sql = sql + "ASC";
//			}
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, compId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				orderNumbers.add(rs.getLong("OrderNum"));
//			}
//			if (!orderNumbers.isEmpty()) {
//				Long firstOrderNo = orderNumbers.get(0);
//				Long lastOrderNo = orderNumbers.get(orderNumbers.size() - 1);
//				orderNo = firstOrderNo;
//				if (action.equalsIgnoreCase("FirstInvoice")) {
//					orderNo = firstOrderNo;
//				} else if (action.equalsIgnoreCase("LastInvoice")) {
//					orderNo = lastOrderNo;
//				} else if (action.equalsIgnoreCase("NextInvoice")) {
//					if (prevInvoiceOrderNo == lastOrderNo) {
//						orderNo = prevInvoiceOrderNo;
//					} else {
//						for (Long currOdrNo : orderNumbers) {
//							if (currOdrNo > prevInvoiceOrderNo) {
//								orderNo = currOdrNo;
//								break;
//							}
//						}
//					}
//				} else if (action.equalsIgnoreCase("PreviousInvoice")) {
//					if (prevInvoiceOrderNo == lastOrderNo) {
//						orderNo = lastOrderNo;
//					} else {
//						for (Long currOdrNo : orderNumbers) {
//							if (currOdrNo < prevInvoiceOrderNo) {
//								orderNo = currOdrNo;
//								break;
//							}
//						}
//					}
//				}
//				request.getSession().setAttribute("prevInvoiceOrderNo", orderNo);
//			}
//		} catch (SQLException ex) {
//			Loger.log("Exception in FirstOrderNo Function" + ex.toString());
//			ex.printStackTrace();
//		} finally {
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
//		return orderNo;
//	}

	public int getSalesOrderNumberByTransformBtnName(String compId, HttpServletRequest request) {

		int orderNo = 0;
		int custId = 0;
		try {

//			String action = request.getParameter("tabid");
			int currentIndex = Integer.valueOf(request.getParameter("index"));

			String query = "SELECT  bi FROM BcaInvoice bi where bi.company.companyId =:companyId "
					+ "and bi.invoiceStatus IN :invoiceStatus" + " and bi.invoiceType.invoiceTypeId =:invoiceTypeId "
					+ "and bi.sonum =:sonum ORDER BY bi.sonum";
			TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
			JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
			JpaHelper.addParameter(typedQuery, query, "invoiceStatus", Arrays.asList(0, 2));
			JpaHelper.addParameter(typedQuery, query, "invoiceTypeId", 7);
			JpaHelper.addParameter(typedQuery, query, "sonum", currentIndex);

			BcaInvoice invoice = typedQuery.getResultList().stream().findFirst()
					.orElseThrow(() -> new EntityNotFoundException("Sales Order NOt Found"));
//			orderNo=invoice.getClientVendor().getClientVendorId();

			orderNo = bcaInvoiceRepository.findMaxValueOfOrderNum() + 1;
			if (null != invoice.getClientVendor())
				custId = invoice.getClientVendor().getClientVendorId();
//			ModelMapper modelMapper = new ModelMapper();
//			BcaInvoice newInvoice = modelMapper.map(invoice, BcaInvoice.class);
//			newInvoice.setInvoiceId(bcaInvoiceRepository.findMaxValueOfInvoiceId() + 1);
//			newInvoice.setOrderNum(orderNo);
//			newInvoice.setSonum(null);
//			bcaInvoiceRepository.save(newInvoice);
			InvoiceDto form = new InvoiceDto();
			form.setOrderNo(String.valueOf(orderNo));
			form.setPoNum(invoice.getRefNum());
			if (null != invoice.getInvoiceStyle())
				form.setInvoiceStyle(String.valueOf(invoice.getInvoiceStyle().getInvoiceStyleId()));
			form.setWeight(invoice.getWeight());
			form.setSubtotal(invoice.getSubTotal());
			form.setTax(invoice.getTax());
			form.setShipping(invoice.getSh());

			form.setBsAddressID(String.valueOf(invoice.getBsaddressId()));
			form.setShAddressID(String.valueOf(invoice.getShippingAddrId()));
			form.setTotal(invoice.getTotal());
			form.setAdjustedtotal(invoice.getAdjustedTotal());
			if (null != invoice.getShipCarrier())
				form.setVia(String.valueOf(invoice.getShipCarrier().getShipCarrierId()));
			form.setRep(String.valueOf(invoice.getSalesRepId()));
			if (null != invoice.getMessage())
				form.setMessage(String.valueOf(invoice.getMessage().getMessageId()));
			if (null != invoice.getTerm())
				form.setTerm(String.valueOf(invoice.getTerm().getTermId()));
			if (null != invoice.getPaymentType())
				form.setPayMethod(String.valueOf(invoice.getPaymentType().getPaymentTypeId()));
			if (null != invoice.getSalesTax())
				form.setTaxID(String.valueOf(invoice.getSalesTax().getSalesTaxId()));
			form.setTaxable((invoice.getTaxable() > 0 ? "true" : "false"));
			form.setItemShipped(invoice.getShipped() > 0 ? "true" : "false");
			form.setMemo(invoice.getMemo());
			form.setShipDate(invoice.getDateConfirmed().toString());
			form.setOrderDate(invoice.getDateAdded().toString());
			form.setPaid(String.valueOf(invoice.getIsPaymentCompleted()));
			form.setServiceID(invoice.getServiceId());
			form.setIsPending(invoice.getIsPending() > 0 ? "true" : "false");
			String invCodessa = "";
			String invIDs = "";
			String invCodes = "";
			String invNames = "";
			String invQtys = "";
			String invUWeights = "";
			String invUPrices = "";
			String invIsTaxables = "";
			String invItemIDs = "";
			String invItemOrders = "";
////			String itemCategoryId[] = form.getCategory().split(";");
//			boolean shippedNow = false;
//			if (form.getItemShipped() != null && form.getItemShipped().equals("on")) {
//				shippedNow = true;
//			}
			List<BcaCart> bcaCart = bcaCartRepository.findByInvoiceIdAndCompanyId(invoice.getInvoiceId(),
					Long.parseLong(compId));
			form.setSize(bcaCart.size());
			for (BcaCart cart : bcaCart) {
				invIDs = invIDs + cart.getInventory().getInventoryId() + ";";

				invCodes = invCodes + cart.getInventoryCode() + ";";
				invNames = invNames + cart.getInventoryName() + ";";
				invQtys = invQtys + cart.getQty() + ";";
				invUWeights = invUWeights + cart.getUnitWeight() + ";";
				invUPrices = invUPrices + cart.getUnitPrice() + ";";
				invIsTaxables = invIsTaxables + cart.getTaxable() + ";";
				invItemIDs = invItemIDs + cart.getItemTypeId() + ";";
				invItemOrders = invItemOrders + cart.getItemOrder() + ";";

			}

			form.setItem(invIDs);

			form.setCode(invCodes);
			form.setDesc(invNames);
			form.setQty(invQtys);
			form.setUnitWeight(invUWeights);
			form.setUprice(invUPrices);
			form.setIsTaxable(invIsTaxables);
			form.setItemTypeID(invItemIDs);
			form.setItemOrder(invItemOrders);
			boolean save = Save(compId, form, String.valueOf(custId));
			if (save) {
				invoice.setDeleted(1);
				bcaInvoiceRepository.save(invoice);
			}

		} catch (Exception ex) {
			Loger.log("Exception in getSalesOrderNumberByBtnName Function" + ex.toString());
			ex.printStackTrace();
		}
		return orderNo;
	}

	public Long getSalesOrderNumberByBtnName(String compId, HttpServletRequest request) {

		Long orderNo = null;
		try {

			String action = request.getParameter("tabid");
			int currentIndex = Integer.valueOf(request.getParameter("index"));
			Long prevSOOrderNo = (Long) request.getSession().getAttribute("prevSOOrderNo");
			prevSOOrderNo = prevSOOrderNo != null ? prevSOOrderNo : 0l;

			String query = "SELECT  bi FROM BcaInvoice bi where bi.company.companyId =:companyId "
					+ "and bi.invoiceStatus IN :invoiceStatus" + " and bi.invoiceType.invoiceTypeId =:invoiceTypeId "
//							+ " and bi.deleted = :deleted "
					+ "and bi.sonum>0 ORDER BY bi.sonum";
			TypedQuery<BcaInvoice> typedQuery = this.entityManager.createQuery(query, BcaInvoice.class);
			JpaHelper.addParameter(typedQuery, query, "companyId", Long.parseLong(compId));
			JpaHelper.addParameter(typedQuery, query, "invoiceStatus", Arrays.asList(0, 2));
			JpaHelper.addParameter(typedQuery, query, "invoiceTypeId", 7);
//			JpaHelper.addParameter(typedQuery, query, "deleted", 0);

			if (action.equalsIgnoreCase("PreviousSalesOrder")) {
				BcaInvoice invoice = typedQuery.getResultList().stream().filter(x -> x.getSonum() < currentIndex)
						.sorted(Comparator.comparing(BcaInvoice::getSonum).reversed()).findFirst()
						.orElse(typedQuery.getResultList().stream()
								.sorted(Comparator.comparing(BcaInvoice::getSonum).reversed()).findFirst().get());

				orderNo = new Long(invoice.getSonum());

			}
			if (action.equalsIgnoreCase("NextSalesOrder")) {
				BcaInvoice invoice = typedQuery.getResultList().stream().filter(x -> x.getSonum() > currentIndex)
						.sorted(Comparator.comparing(BcaInvoice::getSonum)).findFirst()
						.orElse(typedQuery.getResultList().stream().sorted(Comparator.comparing(BcaInvoice::getSonum))
								.findFirst().get());

				orderNo = new Long(invoice.getSonum());

			}
			if (action.equalsIgnoreCase("FirstSalesOrder")) {
				Optional<BcaInvoice> invoice = typedQuery.getResultList().stream()
						.sorted(Comparator.comparing(BcaInvoice::getSonum)).findFirst();
				if (invoice.isPresent()) {
					orderNo = new Long(invoice.get().getSonum());
				}
			}
			if (action.equalsIgnoreCase("LastSalesOrder")) {
				Optional<BcaInvoice> invoice = typedQuery.getResultList().stream()
						.sorted(Comparator.comparing(BcaInvoice::getSonum).reversed()).findFirst();
				if (invoice.isPresent()) {
					orderNo = new Long(invoice.get().getSonum());
				}
			}

		} catch (Exception ex) {
			Loger.log("Exception in getSalesOrderNumberByBtnName Function" + ex.toString());
			ex.printStackTrace();
		}
		return orderNo;
	}

//	public Long getSalesOrderNumberByBtnName(String compId, HttpServletRequest request) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		Long orderNo = null;
//		try {
//			List<Long> orderNumbers = new ArrayList<>();
//			String action = request.getParameter("tabid");
//			Long prevSOOrderNo = (Long) request.getSession().getAttribute("prevSOOrderNo");
//			prevSOOrderNo = prevSOOrderNo != null ? prevSOOrderNo : 0l;
//			String sql = "SELECT InvoiceID, SONum FROM bca_invoice WHERE CompanyID =? and invoiceStatus in (0,2)"
//					+ " and InvoiceTypeID=7 and SONum>0 ORDER BY SONum ";
//			if (action.equalsIgnoreCase("PreviousSalesOrder")) {
//				sql = sql + "DESC";
//			} else {
//				sql = sql + "ASC";
//			}
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, compId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				orderNumbers.add(rs.getLong("SONum"));
//			}
//			if (!orderNumbers.isEmpty()) {
//				Long firstOrderNo = orderNumbers.get(0);
//				Long lastOrderNo = orderNumbers.get(orderNumbers.size() - 1);
//				orderNo = firstOrderNo;
//				if (action.equalsIgnoreCase("FirstSalesOrder")) {
//					orderNo = firstOrderNo;
//				} else if (action.equalsIgnoreCase("LastSalesOrder")) {
//					orderNo = lastOrderNo;
//				} else if (action.equalsIgnoreCase("NextSalesOrder")) {
//					if (prevSOOrderNo == lastOrderNo) {
//						orderNo = prevSOOrderNo;
//					} else {
//						for (Long currOdrNo : orderNumbers) {
//							if (currOdrNo > prevSOOrderNo) {
//								orderNo = currOdrNo;
//								break;
//							}
//						}
//					}
//				} else if (action.equalsIgnoreCase("PreviousSalesOrder")) {
//					if (prevSOOrderNo == lastOrderNo) {
//						orderNo = lastOrderNo;
//					} else {
//						for (Long currOdrNo : orderNumbers) {
//							if (currOdrNo < prevSOOrderNo) {
//								orderNo = currOdrNo;
//								break;
//							}
//						}
//					}
//				}
//				request.getSession().setAttribute("prevSOOrderNo", orderNo);
//			}
//		} catch (SQLException ex) {
//			Loger.log("Exception in FirstOrderNo Function" + ex.toString());
//			ex.printStackTrace();
//		} finally {
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
//		return orderNo;
//	}

	public void paymentHistory(String cvId, String compId, HttpServletRequest request) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null, rs1 = null;
		PreparedStatement pstmt = null, pstmt1 = null;
		ArrayList<InvoiceDto> list = new ArrayList<>();
		ArrayList<String> count = new ArrayList<>();
		ArrayList<InvoiceDto> total = new ArrayList<>();
		try {
			String sqlString = "select Name,FirstName,LastName from bca_clientvendor where Status IN ('N', 'U') and CVTypeID IN (1,2,3) And Active=1 and ClientVendorID=?";
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, cvId);
			rs = pstmt.executeQuery();
			String custName = "";
			String companyName = "";
			if (rs.next()) {
				custName = rs.getString(3) + "," + rs.getString(2);
				companyName = rs.getString(1);
			}

			String sql = "SELECT distinct i.OrderNum,i.PONum,i.SONum, date_format(i.dateadded,'%m-%d-%Y') as DateAdded, i.Total, i.Balance, it.Name,i.InvoiceTypeID,"
					+ "i.Memo,c.FirstName,c.LastName,c.Name AS CompanyName,t.Name AS TermName,ct.Name AS CategoryName "
					+ "FROM bca_invoice AS i INNER JOIN bca_invoicetype AS it ON i.ClientVendorID=? AND i.InvoiceTypeID=it.InvoiceTypeID "
					+ "INNER JOIN bca_clientvendor AS c ON c.ClientVendorID=i.ClientVendorID LEFT JOIN bca_term AS t ON t.TermID=i.TermID "
					+ "LEFT JOIN bca_category AS ct ON ct.CategoryID=i.CategoryID order by it.name, ordernum";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cvId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ((request.getParameter("Type").equals("Invoice") && rs.getString("InvoiceTypeID").equals("1"))
						|| (request.getParameter("Type").equals("PO") && rs.getString("InvoiceTypeID").equals("2"))) {
					InvoiceDto invoiceDto = new InvoiceDto();
					String orderNo = rs.getString(1);
					String poNumber = rs.getString(2);
					String soNumber = rs.getString(3);
					if (poNumber != null && !poNumber.equals("0")) {
						invoiceDto.setOrderNo(poNumber);
					} else if (soNumber != null && !soNumber.equals("0")) {
						invoiceDto.setOrderNo(soNumber);
					} else {
						invoiceDto.setOrderNo(orderNo);
					}
					invoiceDto.setOrderDate(rs.getString(4));
					invoiceDto.setTotal(Double.parseDouble(truncate(rs.getString(5))));
					double balance = Double.parseDouble(truncate(rs.getString(6)));
					invoiceDto.setBalance(balance);
					invoiceDto.setPaid((balance == 0) ? "1" : "0");
					invoiceDto.setType(rs.getString(7));
					invoiceDto.setMemo(rs.getString("Memo"));
					invoiceDto.setCustomer(rs.getString("FirstName") + " " + rs.getString("LastName"));
					invoiceDto.setCompanyName(rs.getString("CompanyName"));
					invoiceDto.setTerm(rs.getString("TermName"));
					invoiceDto.setCategory(rs.getString("CategoryName"));
					list.add(invoiceDto);
				}
			}
			pstmt = con.prepareStatement("select distinct i.InvoiceTypeID  from bca_invoice as i "
					+ "inner join  bca_invoicetype as it on (i.ClientVendorID = ? and  i.InvoiceTypeID = it.InvoiceTypeID ) order by it.name, ordernum");
			pstmt.setString(1, cvId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count.add(String.valueOf(rs.getInt(1)));
			}
			for (int i = 0; i < count.size(); i++) {
				InvoiceDto invForm = new InvoiceDto();
				String sql1 = "select sum(Total),sum(Balance) from bca_invoice where ClientVendorID = ? and InvoiceTypeID =? ";
				pstmt = con.prepareStatement(sql1);
				pstmt.setString(1, cvId);
				pstmt.setInt(2, Integer.parseInt((String) count.get(i)));
				rs = pstmt.executeQuery();
				if (rs.next()) {
					invForm.setTotal(Double.parseDouble(truncate(rs.getString(1))));
					invForm.setBalance(Double.parseDouble(truncate(rs.getString(2))));
				}
				pstmt.close();
				rs.close();

				String sql2 = "select Name from bca_invoicetype where InvoiceTypeID = ?";
				pstmt1 = con.prepareStatement(sql2);
				pstmt1.setInt(1, Integer.parseInt((String) count.get(i)));
				rs1 = pstmt1.executeQuery();
				if (rs1.next()) {
					invForm.setType(rs1.getString(1));
				}
				total.add(invForm);
			}
			request.setAttribute("PayHistory", list);
			request.setAttribute("CustName", custName);
			request.setAttribute("Company", companyName);
			request.setAttribute("Total", total);

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

	}

	public boolean isCustomerHasBalance(String cvId) {
		boolean isBalanceFound = false;

		try {
			String sqlString = "SELECT DISTINCT Balance FROM bca_invoice WHERE ClientVendorID = ? AND Balance > 0";

			Query query = entityManager.createNativeQuery(sqlString);
			query.setParameter(1, cvId); // Parameter binding

			List<Double> balances = query.getResultList();

			if (!balances.isEmpty()) {
				isBalanceFound = true;
			}
		} catch (Exception ex) {
			// Handle exceptions
			ex.printStackTrace();
		}

		return isBalanceFound;
	}

//	public boolean isCustomerHasBalance(String cvId) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		boolean isBalanceFound = false;
//		try {
//			String sql = "SELECT distinct OrderNum, Total, Balance FROM bca_invoice WHERE ClientVendorID=? HAVING Balance>0";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, cvId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				double balance = rs.getDouble(3);
//				if (balance > 0) {
//					isBalanceFound = true;
//					break;
//				}
//			}
//		} catch (SQLException ee) {
//			Loger.log(2, " SQL Error in Class TaxInfo and  method -getFederalTax " + ee.toString());
//		} finally {
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
//		return isBalanceFound;
//	}

	public String truncate(String num) {
		String string1 = null;
		int seperation = 0;
		string1 = "" + num;
		if (string1.indexOf(".") == -1)
			return (string1 + ".00");
		seperation = string1.length() - string1.indexOf('.');
		if (seperation > 3)
			return string1.substring(0, string1.length() - seperation + 3);
		else if (seperation == 2)
			return string1 + '0';
		return string1;
	}

	public long getInvoiceID(String compId, String ordNo, String orderType) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		long orderNo = 0;
		try {
			String sql = "";
			if (("invoice").equals(orderType))
				sql = " select InvoiceID from bca_invoice where OrderNum = ? and CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID in (1,7,9) ";
			else if (("estimation").equals(orderType))
				sql = " select InvoiceID from bca_invoice where EstNum = ? and CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID=10 ";
			else if (("SO").equals(orderType))
				sql = " select InvoiceID from bca_invoice where SONum = ? and CompanyID = ? and invoiceStatus in (0,2) and InvoiceTypeID IN (1,7,9)";
			else if (("PO").equals(orderType))
				sql = " select InvoiceID from bca_invoice where PONum = ? and CompanyID = ? and not(invoiceStatus=1) and InvoiceTypeID IN (2)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, ordNo);
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				orderNo = rs.getLong(1);
			}
		} catch (SQLException ex) {
			Loger.log("Exception in PreviousOrderNo Function" + ex.toString());
			ex.printStackTrace();
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
		return orderNo;
	}

	public void emailInfo(HttpServletRequest request, long invoiceID, String compId, String ordNo, InvoiceDto form) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from bca_invoice where InvoiceID = ? and CompanyID = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, invoiceID);
			pstmt.setString(2, compId);
			rs = pstmt.executeQuery();
			long cvId = 0;
			int isEmail = 0;
			String orderDate = "";
			if (rs.next()) {
				cvId = rs.getLong("ClientVendorID");
				isEmail = rs.getInt("IsEmailed");
				orderDate = rs.getString("DateAdded");
			}
			String sql1 = "select Email from bca_clientvendor where ClientVendorID=? and Status in ('U','N')";
			pstmt = con.prepareStatement(sql1);
			pstmt.setLong(1, cvId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				form.setEmailAddr(rs.getString(1));
				Loger.log("SS" + form.getEmailAddr());
				if (isEmail == 1) {
					form.setIsEmailSent("true");
				} else {
					form.setIsEmailSent("false");
				}
				form.setSubject("Your orders are delivered from BzComposer.com");
			}
			form.setContent(getEmailContent(invoiceID, orderDate, ordNo).toString());
			request.setAttribute("EmailInfo", form);
		} catch (SQLException ex) {
			Loger.log("Exception in PreviousOrderNo Function" + ex.toString());
			ex.printStackTrace();
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
	}

	public StringBuffer getEmailContent(long invoiceID, String orderDate, String ordNo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		SQLExecutor db = new SQLExecutor();
		ResultSet rs = null;
		StringBuffer content = new StringBuffer();
		con = db.getConnection();
		try {
			String sql = "select * from bca_cart where InvoiceID = ? order by ItemOrder asc";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, invoiceID);
			rs = pstmt.executeQuery();
			content.append("Your order has been successfully received, and is scheduled to be processed shortly.\n\n"
					+ "We really appreciate doing business with you.\n" + "\nItem Details\n\n" + "Order Date : ")
					.append(orderDate + "\n\nOrder ID : " + ordNo);
			int items = 0;
			while (rs.next()) {
				items++;
				content.append("\nItem#:");
				content.append(rs.getString("InventoryCode"));
				content.append("\nQty:");
				content.append(rs.getInt("Qty"));
				content.append("\nDescription:");
				content.append(rs.getString("InventoryName"));
				content.append("\n");
			}
			content.append("\n\nTotal# of Items: " + items).append("\n==============================================");
			// content.append("\nCDROMUSA.com"); //commented on 19-06-2019
			content.append("\nBzComposer.com");
		} catch (SQLException ex) {
			Loger.log("Exception in PreviousOrderNo Function" + ex.toString());
			ex.printStackTrace();
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
		return content;
	}

	public EmailSenderDto getEmailSenderInfo(String compId) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EmailSenderDto form = new EmailSenderDto();
		try {
			String sql = "SELECT Mailserver,Mail_username,Mail_password,Mail_senderEmail FROM bca_preference WHERE CompanyID=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, compId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				form.setMailServer(rs.getString("Mailserver"));
				form.setMailUsername(rs.getString("Mail_username"));
				form.setMailPassword(rs.getString("Mail_password"));
				form.setMailSenderEmail(rs.getString("Mail_senderEmail"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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

	public boolean send(String compId, InvoiceDto form) {
		boolean result = false;
		try {
			EmailSenderDto emailSenderDto = getEmailSenderInfo(compId);
			MailSend mailSend = new MailSend();
			result = mailSend.sendMail(form.getEmailAddr(), form.getSubject(), form.getContent(),
					emailSenderDto.getMailSenderEmail());
		} catch (Exception ex) {
			Loger.log("Exception in PreviousOrderNo Function" + ex.toString());
			ex.printStackTrace();
		}
		return result;
	}

	public ArrayList searchHistory(HttpServletRequest request, String cond, String cvId, String periodFrom,
			String periodTo) {
		String sqlString = null, sqlString1 = null;
		String finalTotal = null, finalBalance = null;
		ArrayList<TrHistoryLookUp> objList = new ArrayList<>();
		ResultSet rs = null, rs1 = null;
		Connection con = null;
		CustomerInfo cinfo = new CustomerInfo();
		SQLExecutor db = new SQLExecutor();
		PreparedStatement pstmt = null, pstmt1 = null;
		con = db.getConnection();

		if (cond.equalsIgnoreCase("ShowAll")) {
			sqlString = "select i.InvoiceID, i.OrderNum, date_format(i.dateadded,'%m-%d-%Y') as DateAdded, i.Total , i.Balance, it.Name from bca_invoice as i inner join bca_invoicetype as it on(i.ClientVendorID =?"
					+ " and i.InvoiceTypeID = it.InvoiceTypeID ) order by it.name, i.Ordernum";
			Loger.log("The string of showall is " + sqlString);
			sqlString1 = "select sum(i.Total),sum(i.Balance) from bca_invoice as i inner join bca_invoicetype as it on(i.ClientVendorID =?"
					+ " and i.InvoiceTypeID = it.InvoiceTypeID )";
		} else {

			sqlString = "select i.InvoiceID, i.OrderNum, date_format(i.dateadded,'%m-%d-%Y') as DateAdded, i.Total , i.Balance, it.Name from bca_invoice as i inner join bca_invoicetype as  it on (i.clientvendorid =?"
					+ " and i.invoicetypeid = it.invoicetypeid";
			Loger.log("the String of the By Period is" + sqlString);

			if (periodFrom != null && periodTo != null && periodFrom.trim().length() > 1
					&& periodTo.trim().length() > 1) {
				sqlString += "	and i.DateAdded between '" + cinfo.string2date(periodFrom) + "' and '"
						+ cinfo.string2date(periodTo) + "') order by it.name, i.Ordernum";
				Loger.log("The Date query is " + sqlString);
				sqlString1 = "select sum(i.Total),sum(i.Balance) from bca_invoice as i inner join bca_invoicetype as it on(i.ClientVendorID =?"
						+ " and i.InvoiceTypeID = it.InvoiceTypeID )";
				sqlString1 += "	and i.DateAdded between '" + cinfo.string2date(periodFrom) + "' and '"
						+ cinfo.string2date(periodTo) + "'";
			} else {
				sqlString = "select i.InvoiceID, i.OrderNum, date_format(i.dateadded,'%m-%d-%Y') as DateAdded, i.Total , i.Balance, it.Name from bca_invoice as i inner join bca_invoicetype as it on(i.ClientVendorID =?"
						+ " and i.InvoiceTypeID = it.InvoiceTypeID ) order by it.name, i.Ordernum";
				sqlString1 = "select sum(i.Total),sum(i.Balance) from bca_invoice as i inner join bca_invoicetype as it on(i.ClientVendorID =?"
						+ " and i.InvoiceTypeID = it.InvoiceTypeID )";
			}

		}

		try {
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, cvId);
			pstmt1 = con.prepareStatement(sqlString1);
			pstmt1.setString(1, cvId);
			rs = pstmt.executeQuery();
			rs1 = pstmt1.executeQuery();
			while (rs.next()) {
				TrHistoryLookUp hlookup = new TrHistoryLookUp();
				Loger.log("The Invoice id is " + rs.getString(1));
				hlookup.setInvoiceId(rs.getString(1));
				Loger.log("The orderno is" + rs.getString(1));
				hlookup.setOrderNum(rs.getString(2));
				hlookup.setDateAdded(rs.getString(3));
				hlookup.setTotal(truncate(rs.getString(4)));
				hlookup.setBalance(truncate(rs.getString(5)));
				hlookup.setName(rs.getString(6));
				Loger.log("The Name is " + rs.getString(6));

				objList.add(hlookup);
			}
			while (rs1.next()) {
				// TrHistoryLookUp hlookup=new TrHistoryLookUp();
				finalTotal = truncate(rs1.getString(1));
				finalBalance = truncate(rs1.getString(2));
				Loger.log("The Final Total is " + rs1.getString(1));
			}
			request.setAttribute("FinalTotal", finalTotal);
			request.setAttribute("FinalBalance", finalBalance);

		} catch (Exception e) {
			System.out.println("EXpe " + e.toString());
			Loger.log(e.toString());
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

	public void getServices(HttpServletRequest request, String compId, String cvId) {
		// TODO Auto-generated method stub
		ArrayList<UpdateInvoiceDto> serviceList = new ArrayList<>();
		ArrayList<UpdateInvoiceDto> invoiceName = new ArrayList<>();
		ArrayList<UpdateInvoiceDto> balenceDetails = new ArrayList<>();
//		ResultSet rs = null, rs1 = null, rs2 = null;
//		Connection con = null;
//		SQLExecutor db = new SQLExecutor();
//		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null;
//		con = db.getConnection();
		// Loger.log("@@@@@@@@The Client Vendor Id is @@@@@@@@" + cvId);
//		String sqlString = "select * from bca_servicetype";
//		String sqlString1 = "select  * from bca_invoicestyle where Active=1";
//		String sqlString2 = "select * from bca_clientvendorservice where CompanyID=? and ClientVendorID=?";
		try {

			List<BcaServicetype> bcaServicetypes = bcaServicetypeRepository.findAll();
			for (BcaServicetype bcaServicetype : bcaServicetypes) {
				UpdateInvoiceDto uform = new UpdateInvoiceDto();
				uform.setServiceID(bcaServicetype.getServiceId());
				uform.setServiceName(bcaServicetype.getServiceName());
				uform.setInvoiceStyleId(bcaServicetype.getInvoiceStyle().getInvoiceStyleId());
				serviceList.add(uform);
			}

//        	    pstmt = con.prepareStatement(sqlString);
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                UpdateInvoiceDto uform = new UpdateInvoiceDto();
//                uform.setServiceID(rs.getInt(1));
//                uform.setServiceName(rs.getString(2));
//                uform.setInvoiceStyleId(rs.getInt(3));
//                serviceList.add(uform);
//            }

		} catch (Exception e) {
			e.printStackTrace();
			Loger.log(e.toString());
		}
		request.setAttribute("ServiceList", serviceList);

		try {
			List<BcaInvoicestyle> bcaInvoicestyles = bcaInvoicestyleRepository.findByActive(1);
			for (BcaInvoicestyle bcaInvoicestyle : bcaInvoicestyles) {
				UpdateInvoiceDto uform = new UpdateInvoiceDto();
				uform.setInvoiceStyleId(bcaInvoicestyle.getInvoiceStyleId());
				uform.setInvoiceStyle(bcaInvoicestyle.getName());
				invoiceName.add(uform);
			}
//            pstmt1 = con.prepareStatement(sqlString1);
//            rs1 = pstmt1.executeQuery();
//            while (rs1.next()) {
//                UpdateInvoiceDto uform = new UpdateInvoiceDto();
//                //Loger.log("The Incoice style id is " + rs1.getString(1));
//                uform.setInvoiceStyleId(rs1.getInt(1));
//                //Loger.log("The Invoice Style name is " + rs1.getString(2));
//                uform.setInvoiceStyle(rs1.getString(2));
//                invoiceName.add(uform);
//            }

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			Loger.log(e.toString());
		}
		request.setAttribute("InvoiceName", invoiceName);

		try {
//select * from bca_clientvendorservice where CompanyID=? and ClientVendorID=?
			if (cvId != null) {
				List<BcaClientvendorservice> bcaClientvendorservices = bcaClientvendorserviceRepository
						.findByCompanyAndClientVendor(Long.parseLong(compId), Integer.parseInt(cvId));
				for (BcaClientvendorservice clientvendorservice : bcaClientvendorservices) {
					UpdateInvoiceDto uform = new UpdateInvoiceDto();

					uform.setClientVendorID(String.valueOf(clientvendorservice.getClientVendor().getClientVendorId()));
					uform.setServiceBalance(clientvendorservice.getServiceBalance());
					// Loger.log("The Service Balence is "+ uform.getServiceBalance());
					// uform.setInvoiceStyleId(rs1.getInt(1));

					uform.setDefaultService(clientvendorservice.getDefaultService() != true ? 0 : 1);
					// Loger.log("The Default Service is "+ uform.getDefaultService());

					uform.setServiceID(clientvendorservice.getServiceId());
					// Loger.log("The ServiceID is " + uform.getServiceID());
					balenceDetails.add(uform);
				}
			}
//            pstmt2 = con.prepareStatement(sqlString2);
//            pstmt2.setString(1, compId);
//            pstmt2.setString(2, cvId);
//
//            rs2 = pstmt2.executeQuery();
//            while (rs2.next()) {
//                UpdateInvoiceDto uform = new UpdateInvoiceDto();
//
//                uform.setClientVendorID(String.valueOf(rs2
//                        .getInt("ClientVendorID")));
//                uform.setServiceBalance(rs2.getDouble("ServiceBalance"));
//                //Loger.log("The Service Balence is "+ uform.getServiceBalance());
//                // uform.setInvoiceStyleId(rs1.getInt(1));
//
//                uform.setDefaultService(rs2.getInt("DefaultService"));
//                //Loger.log("The Default Service  is "+ uform.getDefaultService());
//
//                uform.setServiceID(rs2.getInt("ServiceID"));
//                //Loger.log("The  ServiceID  is " + uform.getServiceID());
//                balenceDetails.add(uform);
//            }

		} catch (Exception e) {
			// TODO: handle exception
			Loger.log(e.toString());
		}
//        finally {
//            try {
//                if (rs != null) {
//                    db.close(rs);
//                }
//                if (pstmt != null) {
//                    db.close(pstmt);
//                }
//                if (rs1 != null) {
//                    db.close(rs1);
//                }
//                if (pstmt1 != null) {
//                    db.close(pstmt1);
//                }
//                if (rs2 != null) {
//                    db.close(rs2);
//                }
//                if (pstmt2 != null) {
//                    db.close(pstmt2);
//                }
//                if (con != null) {
//                    db.close(con);
//                }
//            } catch (Exception e) {
//                Loger.log(e.toString());
//            }
//        }
		request.setAttribute("BalenceDetails", balenceDetails);

	}

	public java.sql.Date getdate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		Date d1 = null;
		try {
			d1 = sdf.parse(d);

		} catch (ParseException e) {
			Loger.log(2, "ParseException" + e.getMessage());
		}

		return (new java.sql.Date(d1.getTime()));

	}

	public void set(String cvId, HttpServletRequest request, UpdateInvoiceDto form, String compId) {

//		Connection con = null;
//
//		PreparedStatement pstmt2 = null;
//		PreparedStatement pstmt12 = null, pstmt13 = null;

//		SQLExecutor db = new SQLExecutor();
		// ArrayList<Object> objList = new ArrayList<Object>();
		ArrayList<UpdateInvoiceDto> serviceinfo = new ArrayList<UpdateInvoiceDto>();

//		ResultSet rs22 = null, rs12 = null;
//		ResultSet rs13 = null;
//		if (db == null)
//			return;
//		con = db.getConnection();
//		if (con == null)
//			return;
		// UpdateInvoiceDto customer = new UpdateInvoiceDto();
		try {
			if (cvId != null) {
				List<BcaClientvendorservice> clientVendorServiceList = bcaClientvendorserviceRepository
						.findByCompanyIdAndClientVendorId(Long.valueOf(compId), Integer.parseInt(cvId));
				for (BcaClientvendorservice bcvs : clientVendorServiceList) {
					UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
//					Loger.log("we r in Search Customer");
//					Loger.log("The InvoiceStyleID from client vendor  is " + rs22.getString("InvoiceStyleID"));
//					Loger.log("The ServiceId  from client vendor is " + rs22.getString("ServiceID"));
					uform1.setServiceBalance((bcvs.getServiceBalance()));
					Loger.log("The ServiceBalance is from clientvendor " + bcvs.getServiceBalance());

					uform1.setDefaultService(bcvs.getDefaultService() ? 1 : 0);

					// uform1.setServiceIdNo(rs22.getInt("ServiceID"));
					uform1.setServiceID(bcvs.getServiceId());

					Loger.log("The  service ID is from clientvendor" + bcvs.getServiceId());
					Loger.log("33333333The  service ID is from clientvendor33333" + uform1.getServiceID());
					BcaInvoicestyle invoiceStyle = bcaInvoicestyleRepository
							.findByInvoiceStyleIdAndActive(bcvs.getInvoiceStyleId(), 1);
					uform1.setInvoiceStyle(invoiceStyle.getName());
					Optional<BcaServicetype> serviceType = bcaServicetypeRepository.findById(bcvs.getServiceId());
					if (serviceType.isPresent()) {
						uform1.setServiceName(serviceType.get().getServiceName());
					}

					serviceinfo.add(uform1);
					Loger.log("Valur @@@@@@@@@@" + uform1.getDefaultService());
				}
			}

//			String sqlString11 = "select ClientVendorID,ServiceID,DateAdded,InvoiceStyleID,ServiceBalance,DefaultService from bca_clientvendorservice where CompanyID = ? and ClientVendorID = ?";
//			String sqlString12 = "select  Name from bca_invoicestyle where Active=1 and InvoiceStyleID=?";
//			String sqlString13 = "select ServiceName from bca_servicetype where ServiceID=? ";
//
//			pstmt2 = con.prepareStatement(sqlString11);
//			pstmt12 = con.prepareStatement(sqlString12);
//			pstmt13 = con.prepareStatement(sqlString13);
//			pstmt2.setString(1, compId);
//			pstmt2.setString(2, cvId);
//			Loger.log("The Company ID is " + compId);
//			Loger.log("The Client Vendor ID is" + cvId);
//			rs22 = pstmt2.executeQuery();
//			while (rs22.next()) {
//				UpdateInvoiceDto uform1 = new UpdateInvoiceDto();
//				Loger.log("we r in Search Customer");
//				Loger.log("The InvoiceStyleID from client vendor  is " + rs22.getString("InvoiceStyleID"));
//				Loger.log("The ServiceId  from client vendor is " + rs22.getString("ServiceID"));
//				uform1.setServiceBalance((rs22.getDouble("ServiceBalance")));
//				Loger.log("The ServiceBalance is from clientvendor " + rs22.getDouble("ServiceBalance"));
//
//				uform1.setDefaultService(rs22.getInt("DefaultService"));
//
//				// uform1.setServiceIdNo(rs22.getInt("ServiceID"));
//				uform1.setServiceID(rs22.getInt("ServiceID"));
//
//				Loger.log("The  service ID is from clientvendor" + rs22.getInt("ServiceID"));
//				Loger.log("33333333The  service ID is from clientvendor33333" + uform1.getServiceID());
//
//				pstmt12.setString(1, rs22.getString("InvoiceStyleID"));
//				rs12 = pstmt12.executeQuery();
//				pstmt13.setString(1, rs22.getString("ServiceID"));
//				rs13 = pstmt13.executeQuery();
//
//				while (rs12.next()) {
//					uform1.setInvoiceStyle(rs12.getString(1));
//
//				}
//				while (rs13.next()) {
//					uform1.setServiceName(rs13.getString(1));
//				}
//
//				serviceinfo.add(uform1);
//				Loger.log("Valur @@@@@@@@@@" + uform1.getDefaultService());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			Loger.log(e.toString());
		}
//		finally {
//			try {
//				if (rs22 != null) {
//					db.close(rs22);
//				}
//				if (pstmt2 != null) {
//					db.close(pstmt2);
//				}
//				if (rs12 != null) {
//					db.close(rs12);
//				}
//				if (pstmt12 != null) {
//					db.close(pstmt12);
//				}
//				if (rs13 != null) {
//					db.close(rs13);
//				}
//				if (pstmt13 != null) {
//					db.close(pstmt13);
//				}
//				if (con != null) {
//					db.close(con);
//				}
//			} catch (Exception e) {
//				Loger.log(e.toString());
//			}
//		}
		request.setAttribute("ServiceInfo", serviceinfo);

	}

	public String setCurrentDate() {

		DateInfo date = new DateInfo();
		int month = date.getMonth();
		int day = date.getDay();

		String da = "", d = "", m = "";
		if (month >= 1 && month <= 9) {
			m = "0" + month;
		} else
			m = "" + month;
		if (day >= 1 && day <= 9) {
			d = "0" + day;
		} else
			d = "" + day;
		da = m + "-" + d + "-" + (date.getYear());
		return da;
	}

	public int getServiceID(String serviceNm) {
		Connection con = null;
		int serviceid = 0;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		SQLExecutor db = new SQLExecutor();
		if (db == null)
			return 0;
		con = db.getConnection();
		try {
			String serviceQuery = "select ServiceID from bca_servicetype where ServiceName=?";
			pstmt = con.prepareStatement(serviceQuery);
			pstmt.setString(1, serviceNm);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				serviceid = rs.getInt("ServiceID");
			}

		} catch (Exception e) {
			// TODO: handle exception
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
		return serviceid;
	}

	public String getServiceName(int serviceId) {
		Connection con = null;
		String serviceNm = "";
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		SQLExecutor db = new SQLExecutor();
		if (db == null)
			return "";
		con = db.getConnection();
		try {
			String serviceQuery = "select ServiceName from bca_servicetype where ServiceID=?";
			pstmt = con.prepareStatement(serviceQuery);
			pstmt.setInt(1, serviceId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				serviceNm = rs.getString("ServiceName");
			}

		} catch (Exception e) {
			// TODO: handle exception
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
		return serviceNm;
	}

	public void invoiceIt(String SONum) {
		Connection con = null;
		PreparedStatement pstmtUpdate = null;
		SQLExecutor db = new SQLExecutor();
		con = db.getConnection();
		try {
			String updateQuery = "";
			updateQuery = "update bca_invoice set  ISInvoice = 1 where SONum = ?";
			pstmtUpdate = con.prepareStatement(updateQuery);
			pstmtUpdate.setString(1, SONum);
			pstmtUpdate.executeUpdate();

		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -InvoiceIt " + " " + ee.toString());
		} finally {
			try {
				if (pstmtUpdate != null) {
					db.close(pstmtUpdate);
				}
				if (con != null) {
					db.close(con);
				}
			} catch (Exception e) {
				Loger.log(2, "ParseException" + e.getMessage());
			}
		}

	}

	public ArrayList customerInvoiceDetails(String compId, HttpServletRequest request) {
		SQLExecutor db = new SQLExecutor();
		Connection con = db.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<LabelValueBean> objList = new ArrayList<>();
		ArrayList<InvoiceDto> details = new ArrayList<>();
		String cvId = "";
		try {
			String sqlString = "select distinct ClientVendorID,FirstName,LastName,ShipCarrierID,PaymentTypeID,TermID,SalesRepID,Taxable,Name from bca_clientvendor"
					+ " where  (Status like 'N' or Status like 'U')  and  (CVTypeID = '1' or CVTypeID = '2') "
					+ " and ( Deleted = '0') and CompanyID=? and Active=1 order by FirstName";

			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, compId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cvId = rs.getString(1);
				InvoiceDto invForm = new InvoiceDto();
				objList.add(new LabelValueBean(
						rs.getString("Name") + "(" + rs.getString(3) + " " + rs.getString(2) + ")", cvId));
				invForm.setClientVendorID(cvId);
				invForm.setFirstName(rs.getString(2));
				invForm.setLastName(rs.getString(3));
				invForm.setVia(rs.getString("ShipCarrierID"));
				invForm.setPayMethod(rs.getString("PaymentTypeID"));
				invForm.setTerm(rs.getString("TermID"));
				invForm.setRep(rs.getString("SalesRepID"));
				invForm.setTaxable(rs.getString("Taxable"));
				details.add(invForm);
			}
			request.setAttribute("CustDetails", details);
		} catch (SQLException ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -customerInvoiceDetails " + ee.toString());
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

	public InvoiceDto getBillingAddress(InvoiceDto form, String addressType) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null;
//		int addType = 1;
		try {
//			String tblName = "BcaBillingaddress";
			if (addressType.equalsIgnoreCase("ship")) {
//				tblName = "BcaShippingaddress";

				BcaShippingaddress address = bcaShippingaddressRepository.findByClientVendorIdAndAddressId(
						Integer.parseInt(form.getClientVendorID()), Integer.parseInt(form.getAddressID()));
				if (null != address) {
					form.setCompanyName(address.getName());
					form.setFirstName(address.getFirstName());
					form.setLastName(address.getLastName());
					form.setAddress1(address.getAddress1());
					form.setAddress2(address.getAddress2());
					form.setCity(address.getCity());
					form.setZipcode(address.getZipCode());
					form.setState(address.getState());
					form.setCountry(address.getCountry());
				}

//				addType = 0;
			} else {
				BcaBillingaddress address = bcaBillingaddressRepository.findByClientVendorIdAndAddressId(
						Integer.parseInt(form.getClientVendorID()), Integer.parseInt(form.getAddressID()));
				if (null != address) {
					form.setCompanyName(address.getName());
					form.setFirstName(address.getFirstName());
					form.setLastName(address.getLastName());
					form.setAddress1(address.getAddress1());
					form.setAddress2(address.getAddress2());
					form.setCity(address.getCity());
					form.setZipcode(address.getZipCode());
					form.setState(address.getState());
					form.setCountry(address.getCountry());
				}
			}

//			String tblName = "bca_billingaddress";
//			if (addressType.equalsIgnoreCase("ship")) {
//				tblName = "bca_shippingaddress";
//				addType = 0;
//			}
//			pstmt = con.prepareStatement("SELECT * FROM " + tblName + " WHERE ClientVendorID=? and AddressID=?");
//			pstmt.setString(1, form.getClientVendorID());
//			pstmt.setString(2, form.getAddressID());
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				form.setCompanyName(rs.getString("Name"));
//				form.setFirstName(rs.getString("FirstName"));
//				form.setLastName(rs.getString("LastName"));
//				form.setAddress1(rs.getString("Address1"));
//				form.setAddress2(rs.getString("Address2"));
//				form.setCity(rs.getString("City"));
//				form.setZipcode(rs.getString("ZipCode"));
//				form.setState(rs.getString("State"));
//				form.setCountry(rs.getString("Country"));
//			}
			form.setCity(MyUtility.checkDefaultCityID(form.getCity()));
			form.setState(MyUtility.checkDefaultStateID(form.getState()));
			form.setCountry(MyUtility.checkDefaultCountryID(form.getCountry()));
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -updateBillingAddress :" + ee.toString());

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
		return form;
	}

	public boolean updateBillingAddress(InvoiceDto frm, String cvId, String addressID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
//		ResultSet rs = null;
//		PreparedStatement pstmt = null, pstmt2 = null;
		boolean status = false;
		try {
//			String sqlString = "select Name,FirstName,LastName, Address1,Address2,City,"
//					+ "State,ZipCode,Country from bca_billingaddress where ClientVendorID ="+cvId+" and AddressID="+addressID;
//			String sqlString2 = "update bca_billingaddress set Name=?,FirstName=?,LastName=?, Address1=?,Address2=?,City=?,"
//					+ "State=?,ZipCode=?,Country=? where AddressID ="+addressID;

//			String sqlString2 = "update bca_billingaddress set Name=?,FirstName=?,LastName=?, Address1=?,Address2=?,City=?,"
//					+ "State=?,ZipCode=?,Country=? where AddressID =" + addressID + " AND ClientVendorID=" + cvId;
//			pstmt2 = con.prepareStatement(sqlString2);
//			

//			pstmt = con.prepareStatement(sqlString);
//			rs = pstmt.executeQuery();
			BcaBillingaddress bcaBillingaddress = bcaBillingaddressRepository
					.findByClientVendorIdAndAddressId(Integer.parseInt(cvId), Integer.parseInt(addressID));
			bcaBillingaddress.setName(frm.getCompanyName());
			bcaBillingaddress.setFirstName(frm.getFirstName());
			bcaBillingaddress.setLastName(frm.getLastName());
			bcaBillingaddress.setAddress1(frm.getAddress1());
			bcaBillingaddress.setAddress2(frm.getAddress2());
			bcaBillingaddress.setCity(frm.getCity());
			bcaBillingaddress.setState(frm.getState());
			bcaBillingaddress.setZipCode(frm.getZipcode());
			bcaBillingaddress.setCountry(frm.getCountry());
			try {
				BcaBillingaddress save = bcaBillingaddressRepository.save(bcaBillingaddress);
				if (save != null) {
					status = true;
				}

			} catch (IllegalArgumentException exception) {
				status = false;
				Loger.log(2,
						" SQL Error in Class InvoiceInfo and  method -updateBillingAddress :" + exception.toString());

			}

//			pstmt2.setString(1, frm.getCompanyName());
//			pstmt2.setString(2, frm.getFirstName());
//			pstmt2.setString(3, frm.getLastName());
//			pstmt2.setString(4, frm.getAddress1());
//			pstmt2.setString(5, frm.getAddress2());
//			pstmt2.setString(6, frm.getCity());
//			pstmt2.setString(7, frm.getState());
//			pstmt2.setString(8, frm.getZipcode());
//			pstmt2.setString(9, frm.getCountry());

//			while (rs.next()) {
//				pstmt2.setString(1, frm.getCompanyName() != null ? frm.getCompanyName():rs.getString(1));
//				pstmt2.setString(2, frm.getFirstName()!=null ? frm.getFirstName():rs.getString(2));
//				pstmt2.setString(3, frm.getLastName()!=null ? frm.getLastName():rs.getString(3));
//				pstmt2.setString(4, frm.getAddress1()!=null ? frm.getAddress1():rs.getString(4));
//				pstmt2.setString(5, frm.getAddress2()!=null ? frm.getAddress2():rs.getString(5));
//				pstmt2.setString(6, frm.getCity()!=null ? frm.getCity():rs.getString(6));
//				pstmt2.setString(7, frm.getState()!=null ? frm.getState():rs.getString(7));
//				pstmt2.setString(8, frm.getZipcode()!=null ? frm.getZipcode():rs.getString(8));
//				pstmt2.setString(9, frm.getCountry()!=null ? frm.getCountry():rs.getString(9));
//			}
////			int rows = pstmt2.executeUpdate();
//			if (rows > 0) {
//				status = true;
//			}
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -updateBillingAddress :" + ee.toString());

		}
//		finally {
//			try {
////				if (rs != null) { db.close(rs); }
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
		return status;
	}

	public boolean updateShippingAddress(InvoiceDto frm, String cvId, String addressID) {
//		SQLExecutor db = new SQLExecutor();
//		Connection con = db.getConnection();
////		ResultSet rs = null;
//		PreparedStatement pstmt = null, pstmt2 = null;
		boolean status = false;
		try {
//			String sqlString1 = "select Name,FirstName,LastName, Address1,Address2,City,"
//					+ "State,ZipCode,Country from bca_shippingaddress where Active=1 and AddressID="+addressID;
//			String sqlString = "update bca_shippingaddress set Name=?,FirstName=?,LastName=?, Address1=?,Address2=?,City=?,"
//					+ "State=?,ZipCode=?,Country=? where AddressID =" + addressID + " AND ClientVendorID=" + cvId;
//			
			BcaShippingaddress shippingAddress = bcaShippingaddressRepository
					.findByClientVendorIdAndAddressId(Integer.parseInt(cvId), Integer.parseInt(addressID));
			shippingAddress.setName(frm.getCompanyName());
			shippingAddress.setFirstName(frm.getFirstName());
			shippingAddress.setLastName(frm.getLastName());
			shippingAddress.setAddress1(frm.getAddress1());
			shippingAddress.setAddress2(frm.getAddress2());
			shippingAddress.setCity(frm.getCity());
			shippingAddress.setState(frm.getState());
			shippingAddress.setZipCode(frm.getZipcode());
			shippingAddress.setCountry(frm.getCountry());
			try {
				BcaShippingaddress save = bcaShippingaddressRepository.save(shippingAddress);
				if (save != null) {
					status = true;
				}

			} catch (IllegalArgumentException exception) {
				status = false;
				Loger.log(2,
						" SQL Error in Class InvoiceInfo and  method -updateBillingAddress :" + exception.toString());

			}
//			pstmt2 = con.prepareStatement(sqlString);
//			pstmt2.setString(1, frm.getCompanyName());
//			pstmt2.setString(2, frm.getFirstName());
//			pstmt2.setString(3, frm.getLastName());
//			pstmt2.setString(4, frm.getAddress1());
//			pstmt2.setString(5, frm.getAddress2());
//			pstmt2.setString(6, frm.getCity());
//			pstmt2.setString(7, frm.getState());
//			pstmt2.setString(8, frm.getZipcode());
//			pstmt2.setString(9, frm.getCountry());
//			pstmt = con.prepareStatement(sqlString1);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				pstmt2.setString(1, frm.getCompanyName() != null ? frm.getCompanyName():rs.getString(1));
//				pstmt2.setString(2, frm.getFirstName()!=null ? frm.getFirstName():rs.getString(2));
//				pstmt2.setString(3, frm.getLastName()!=null ? frm.getLastName():rs.getString(3));
//				pstmt2.setString(4, frm.getAddress1()!=null ? frm.getAddress1():rs.getString(4));
//				pstmt2.setString(5, frm.getAddress2()!=null ? frm.getAddress2():rs.getString(5));
//				pstmt2.setString(6, frm.getCity()!=null ? frm.getCity():rs.getString(6));
//				pstmt2.setString(7, frm.getState()!=null ? frm.getState():rs.getString(7));
//				pstmt2.setString(8, frm.getZipcode()!=null ? frm.getZipcode():rs.getString(8));
//				pstmt2.setString(9, frm.getCountry()!=null ? frm.getCountry():rs.getString(9));
//			}

//			int rows = pstmt2.executeUpdate();
//			if (rows > 0) {
//				status = true;
//			}
		} catch (Exception ee) {
			Loger.log(2, " SQL Error in Class InvoiceInfo and  method -updateShippingAddress :" + ee.toString());

		}
//		finally {
//			try {
////				if (rs != null) { db.close(rs); }
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
		return status;
	}
}
