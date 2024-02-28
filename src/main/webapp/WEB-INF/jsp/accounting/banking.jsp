<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@include file="/WEB-INF/jsp/include/headlogo.jsp"%>
<%@include file="/WEB-INF/jsp/include/header.jsp"%>
<%@include file="/WEB-INF/jsp/include/menu.jsp"%>
<title><spring:message code="BzComposer.bankingtitle" /></title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<spring:url value="/cssresources/Myapp.css" var="mycss" />
<script>var ctx = "${pageContext.request.contextPath}";</script>
<style type="text/css">
div#pie { /* 	color:#05A9C5;; */
	padding: 10px 0px 20px 0px;
}

table.tabla-listados {
	width: 100%;
	border: 1px solid rgb(207, 207, 207);
	margin: 20px 0px 20px 0px;
}

table.tabla-listados thead tr th {
	font-size: .7em;
	text-align: left;
	padding: 5px 10px;
	/* 	background: rgba(5, 169, 197, 0.11); */
	border-bottom: 1px solid rgba(5, 169, 197, 0.2);
	/* 	color: #333; */
	text-shadow: #999 0px 1px 1px;
	white-space: nowrap;
}

table.tabla-listados tbody tr td {
	font-size: .8em;
	/* 	color: #666; */
	padding: 5px 0px 5px 14px;
	/* 	border-bottom: 1px solid rgb(207, 207, 207); */
	background: #fff;
	vertical-align: top;
}

#popupWindow {
	/* width: 1500px;
    height: 900px;*/
	
}
/* .ui-dialog.ui-corner-all.ui-widget.ui-widget-content.ui-front.ui-draggable.ui-resizable {
    min-width: 55%;
    min-height: 500px;
    overflow:"auto";
   	position: absolute;
    height: auto;
    width: 300px;
    top: 300px !important;
    left: 356px;
    display: block;
    z-index: 101;
   
} */
.ui-dialog.ui-corner-all.ui-widget.ui-widget-content.ui-front.ui-draggable.ui-resizable
	{
	min-width: 55%;
	height: auto;
	width: 300px;
	display: block;
	z-index: 101;
}

#popupWindow {
	
}

.highlight {
	background-color: #00CED1 !important;
	color: #fff;
	padding: 5px;
}
/* .table tr:hover { cursor: pointer;} */
.bz_dialogTable tr td {
	padding: 10px;
}

.bz_dialogTable tr td label {
	font-size: 12px;
}

.bz_dialogTable tr td input {
	font-size: 12px;
}

.bz_dialogTable tr td select {
	font-size: 12px;
}

.bzbtn {
	position: relative;
	top: 10px;
}

.highlight {
	background-color: #00CED1 !important;
	color: #fff
}

label {
	display: inline-block;
	margin-bottom: 0;
}

.nav {
	padding-left: 15px;
}

.nav-item {
	border-color: #dee2e6 ! important;
}

.nav-item.active {
	border-color: 2px solid #dee2e6 ! important;
}

.errorField {
	color: red ! important;
}
</style>
</head>
<body>
	<div id="ddcolortabsline">&nbsp;</div>
	<form:form action="/Banking" method="post">
		<div class="content1 clearfix">
			<h3 class="title1">
				<spring:message code="BzComposer.banking.bankingtitle" />
			</h3>
			<div class="filterbar filterbar1">
				<div class="form-inline float-right">
					<label> <spring:message code="BzComposer.banking.datefrom" />
					</label> <input type="text" readonly="true" class="form-control"
						id="dateFrom">
					<div class="input-group-append">
						<a class="input-group-text" href="#">&#9662;</a>
					</div>
					<label> <spring:message code="BzComposer.banking.dateto" />
					</label> <input type="text" readonly="true" class="form-control"
						id="dateTo">
					<div class="input-group-append">
						<a class="input-group-text" href="#">&#9662;</a>
					</div>
				</div>
				<div class="form-inline">
					<label> <spring:message code="BzComposer.banking.filterby" />
					</label> <select class="form-control rangeOP" id="rangeId">
						<option value="1">All</option>
						<option value="2">Custom</option>
						<option value="3">Today</option>
						<option value="4">This Month</option>
						<option value="5">This Quarter</option>
						<option value="6">This Year</option>
						<option value="7">1 Year</option>
						<option value="8">2 year</option>
						<option value="9">3 Year</option>
						<option value="10">This Month-To-Date</option>
						<option value="11">This Quarter-To-Date</option>
						<option value="12">This Year-To-date</option>
						<option value="13">Last 10 Days</option>
						<option value="14">Last 30 Days</option>
						<option value="15">Last 60 Days</option>
						<option value="16">1 Week</option>
					</select> <label> <spring:message code="BzComposer.banking.show" />
					</label> <select class="form-control transactionFilter"
						id="transactionFilterId">
						<option value="">All Transaction</option>
						<option value="ALLPAYMENTS">Payments</option>
						<option value="ALLDEPOSITE">Deposits</option>
						<option value="INVOICE">Invoices</option>
						<option value="PURCHASE">Purchase Bill</option>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 d-flex flex-column full-height">
					<h4 class="title2">
						<spring:message code="BzComposer.banking.accountcategory" />
					</h4>
					<div id="treeView2" class="treeview">
						<ul class="list-group">
							<div style="float: left; width: 100%; margin-bottom: 10px">
								<spring:message code="BzComposer.banking.accountcategory" />
							</div>
							<!-- List Bank-Accounts -->
							<c:forEach items="${AccountCategoryList}" var="curObject"
								varStatus="loop">
								<li class="list-group-item"><label
									class="tree-toggler node-treeview2 node-selected"
									value="${curObject.accountCategoryID}"
									id="${curObject.accountCategoryID}"
									onclick="getAccountCategoryId(${curObject.accountCategoryID})">
										<img
										src="${pageContext.request.contextPath}/images/folder-lightblue-icon.png"
										class="iconImg" /> ${curObject.name}
								</label> <c:forEach items="${accountList}" var="curObject2">
										<c:if
											test="${curObject2.accountCategoryID == curObject.accountCategoryID}">
											<ul class="nav nav-list tree">
												<li><label style="font-size: 14px; cursor: pointer;"
													value="${curObject2.isitmainaccount}"
													id="${curObject2.accountID}"
													onclick="showTransaction(${curObject2.accountID}, ${curObject2.accountCategoryID}, '${curObject2.name}')">${curObject2.name}</label>
													<input type="hidden" id="IsItMain${curObject2.accountID}"
													value="${curObject2.isitmainaccount}" /> <input
													type="hidden" id="Account${curObject2.accountID}"
													value="${curObject2.customerStartingBalance}" /></li>
											</ul>
										</c:if>
									</c:forEach></li>
							</c:forEach>
							<!-- <li class="list-group-item">	<label class="  node-treeview2 node-selected">Header 2</label></li>
						    <li class="list-group-item"><label class=" node-treeview2 node-selected">Header 3</label></li> -->
						</ul>
					</div>
					<div class="mb-auto"></div>
					<div class="filterbar filterbar1 text-center mb-0">
						<a style="color: #fff; font-size: 14px;" class="btn btn-info"
							id="AddAccount"><spring:message code="BzComposer.global.add" /></a>
						<a style="color: #fff; font-size: 14px;" class="btn btn-info"
							id="EditAccount"><spring:message
								code="BzComposer.global.edit" /></a> <a
							style="color: #fff; font-size: 14px;" class="btn btn-info"
							id="TranseferFundsBtn"><spring:message
								code="BzComposer.banking.transeferfundsbtn" /></a>
					</div>
				</div>
				<div class="col-md-9 d-flex flex-column full-height">
					<div id="selectedAccount">
						<h4 class="title2">${selectedAccount.name}</h4>
						<!-- <h4 class="title2">Personal Checking 5423</h4> -->
					</div>
					<div id="transactionTable">
						<table class="table table-bordered table-sm  devBankingDatatable"
							style="font-size: 12px;">
							<thead class="thead-light">
								<tr>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.date" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.payerorpayee" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.payment" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.deposit" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.balance" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.invoiceorponumber" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.paymentmethod" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.category" /></th>
									<th scope="col" class="text-right"><spring:message
											code="BzComposer.banking.memo" /></th>
								</tr>
							</thead>
							<tbody>
								<c:set var="vendorId" value="-1" scope="page" />
								<c:set var="indexForVendor" value="-1" scope="page" />
								<c:set var="checkNumber" value="" scope="page" />
								<c:if test="${not empty payMentList}">
									<c:if test="${not empty indexForPayeeVendor}">
										<c:set var="vendorId" value="${vendorID}" scope="page" />
										<c:set var="indexForVendor" value="${indexForPayeeVendor}"
											scope="page" />
									</c:if>
									<c:forEach items="${payMentList}" var="curObject"
										varStatus="loop">
										<c:if
											test="${selectedAccount.accountID == curObject.payeeID || selectedAccount.accountID == curObject.payerID}">
											<tr
												onclick="return selectRow(${loop.index+1}, ${curObject.id}, ${selectedAccount.accountID},${curObject.payeeID},${curObject.paymentTypeID},${curObject.categoryId},${curObject.amount},${curObject.checkNumber},${curObject.toCurrentBalance})">
												<td class="text-right" id="formatedDate"><fmt:formatDate
														pattern="MM-dd-yyyy" value="${curObject.dateAdded}" /></td>
												<td class="text-right"><c:choose>
														<c:when
															test="${curObject.payeeID == vendorId && vendorId != -1}">
								    ${cvList[indexForVendor].firstName} ${cvList[indexForVendor].lastName}
								</c:when>
														<c:otherwise>
															<c:if test="${curObject.cvName == selectedAccount.name}">
																<c:if
																	test="${not empty curObject.pyerAccountForBanking}"> ${curObject.pyerAccountForBanking}</c:if>
																<c:if test="${empty curObject.pyerAccountForBanking}"> ${fn:replace(curObject.categoryName, "[^A-Za-z]", "")}</c:if>
															</c:if>
															<c:if test="${curObject.cvName != selectedAccount.name}">
																<c:if test="${not empty curObject.cvName}">${curObject.cvName}</c:if>
																<c:if test="${empty curObject.cvName}">${fn:replace(curObject.categoryName, "[^A-Za-z]", "")}</c:if>
															</c:if>
														</c:otherwise>
													</c:choose></td>
												<td class="text-right" id="paymentAmount"><c:if
														test="${selectedAccountId == curObject.payerID}">
														<fmt:formatNumber type="number"
														maxFractionDigits="2"  pattern="#,##0.00"
														value="${curObject.amount}" />
														</c:if>
												</td>
												<%-- <td class="text-right"><c:if
														test="${selectedAccountId != curObject.payerID}">
														${curObject.toCurrentBalance}</c:if>
												</td> --%>
												<td class="text-right" id="depositAmount"><c:if
														test="${selectedAccountId != curObject.payerID}">
														<fmt:formatNumber type="number"
														maxFractionDigits="2"  pattern="#,##0.00"
														value="${curObject.toCurrentBalance}" /></c:if>
												</td> 
												<td class="text-right"><fmt:formatNumber type="number"
														maxFractionDigits="2"  pattern="#,##0.00"
														value="${curObject.balanceForBanking}" /></td>
												<td class="text-right"><c:choose>
														<c:when
															test="${curObject.orderNum != 0 && curObject.orderNum > 0}">Order#${curObject.orderNum}</c:when>
														<c:when
															test="${curObject.poNum != 0 && curObject.poNum > 0}">PO#${curObject.poNum}</c:when>
														<c:otherwise>
															<c:if test="${curObject.billNum != -1}">Bill#${curObject.billNum}</c:if>
														</c:otherwise>
													</c:choose></td>
												<td class="text-right"><c:if
														test="${not empty curObject.checkNumber}">
														<c:choose>
															<c:when test="${curObject.checkNumber.length()>5}">
																<c:set var="checkNumber"
																	value="#${fn:substring(curObject.checkNumber, 0, 4)}"
																	scope="page" />
															</c:when>
															<c:otherwise>
																<c:set var="checkNumber"
																	value="#${curObject.checkNumber}" scope="page" />
															</c:otherwise>
														</c:choose>
														<c:if test="${curObject.paymentTypeID != -1}">${curObject.paymentTypeName}${checkNumber}</c:if>
														<c:if test="${curObject.paymentTypeID == -1}">Check ${checkNumber}</c:if>
													</c:if> <c:if test="${empty curObject.checkNumber}">
								    ${curObject.paymentTypeID != -1?curObject.paymentTypeName:"Check"}
							    </c:if></td>
												<td class="text-right">${curObject.categoryName == null || fn:startsWith(curObject.categoryName, 'null')?'':curObject.categoryName}
												</td>
												<td class="text-right"></td>
												<td hidden="oldBankAccount"
													value="${selectedAccount.accountID}"></td>
												<%-- <td hidden="payMethodId"
													value="${curObject.PaymentTypeID}"></td> --%>
												<%-- <td><input type="hidden" id="payeeBalance" /> <c:if
																test="${not empty payMentList}">
																<input type="hidden"
																	value='${payMentList[0].balanceForBanking}'
																	id="payerBalance" />
																	<input type="hidden"
																	value='${selectedAccount.customerCurrentBalance}'
																	id="payerBalance" />
															</c:if>  --%>
											</tr>
										</c:if>
									</c:forEach>
								</c:if>
								<tr>
									<td hidden="totalrowCount" value="${payMentList.size()}"
										id="RowCount">
								</tr>
							</tbody>
						</table>
					</div>
					<div class="mb-auto"></div>
					<div class="filterbar filterbar1 text-center mb-0" id="bottom">
						<label class=" float-left text-danger" style="font-size: 14px;">
							<spring:message code="BzComposer.banking.deletetransactionnote" />
						</label> <a style="color: white; font-size: 14px;" class="btn btn-info"
							id="AddTransaction"><spring:message
								code="BzComposer.global.add" /></a> <a
							style="color: white; font-size: 14px;" class="btn btn-info"
							id="showEditTransactionDialog"><spring:message
								code="BzComposer.global.edit" /></a> <a
							style="color: white; font-size: 14px;" class="btn btn-info"
							onclick="return clearTransaction()"><spring:message
								code="BzComposer.global.delete" /></a>
						<!--  <div id="totalLabel"> -->
						<c:forEach items="${paymentList}" var="curObject" varStatus="loop">
							<c:if
								test="${selectedAccount.accountID == curObject.accountID || selectedAccount.accountID == curObject.payeeID || selectedAccount.accountID == curObject.payerID()}">
								<label class="text-right float-right" id="totalLabel">${curObject.balanceForBanking}</label>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="tabid" name="tabid" value="" />
	</form:form>
	<div class="container" id="popupWindow">
		<div class="table-responsive" style="padding-top: 20px">
			<div class="d-flex flex-column full-height">

				<div class="modal-body1">
					<div class="content-tabs" id="dialogTabs">
						<nav>
							<div class="nav nav-tabs" id="tabId" role="tablist">
								<a class="nav-item nav-link active" id="nav-home-tab"
									data-toggle="tab" href="#nav-home" role="tab"
									aria-controls="nav-home" aria-selected="true"
									onclick="fundTrasferTab()"> <spring:message
										code="BzComposer.banking.fundtransfer" />
								</a> <a class="nav-item nav-link" id="nav-home1-tab"
									data-toggle="tab" href="#nav-profile" role="tab"
									aria-controls="nav-profile" aria-selected="false"
									onclick="paymentTab()"> <spring:message
										code="BzComposer.banking.payment" />
								</a> <a class="nav-item nav-link" id="nav-home2-tab"
									data-toggle="tab" href="#nav-contact" role="tab"
									aria-controls="nav-contact" aria-selected="false"
									onclick="depositTab()"> <spring:message
										code="BzComposer.banking.deposit" />
								</a>
							</div>
						</nav>
					</div>
					<!-- Here is the Fund Trasfer Tab Started -->
					<div class="container-fluid" id="fundTrasferTab">
						<div class=" row">
							<div class=" col-12">
								<div class="border1 clearfix">
									<form>
										<div class="row">
											<div class="col-md-6" id="firstTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payeraccount" /></label></td>
														<td><input type="text"
															value="${selectedAccount.name}" readonly="true"
															style="width: 150px" id="${selectedAccount.accountID}" />
															<input type="hidden"
															value="${selectedAccount.accountCategoryID}"
															id="categoryIdForPayer" /></td>
													</tr>
													<tr>
														<td><label> <spring:message
																	code="BzComposer.banking.paymethod" />
														</label></td>
														<td><select style="width: 100%"
															onchange="slectedPayMethod()" id="payMethodForDlg">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${simpleTypes}" var="curObject"
																	varStatus="loop">
																	<c:if test="${not empty curObject.typeName}">
																		<option id="${curObject.id}">${curObject.typeName}</option>
																	</c:if>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.date" /></label></td>
														<td><input type="text" readonly="true"
															style="width: 150px" id="dlgDate" /></td>
													</tr>
												</table>
											</div>
											<div class="col-md-6" id="secondTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payamount" /></label></td>
														<td><input type="number" style="width: 150px"
															id="payAmount" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.checkno" /></label></td>
														<td><input type="text" style="width: 150px"
															id="checkNumForDlg" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.payeeaccount" /></label></td>
														<td><select style="width: 100%" id="payeename"
															onchange="changeBank()">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${accountList}" var="curObject"
																	varStatus="loop">
																	<c:if
																		test="${curObject.accountCategoryID == 1 || curObject.accountCategoryID == 2 || curObject.accountCategoryID == 3}">
																		<option value="${curObject.customerCurrentBalance}"
																			id="${curObject.accountID}">${curObject.name}</option>
																	</c:if>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td>
															<!-- label><spring:message code="BzComposer.banking.endingbalance"/></label -->
														</td>
														<td><input type="hidden" id="payeeBalance" /> <c:if
																test="${not empty payMentList}">
																<%-- <input type="hidden"
																	value='${payMentList[0].balanceForBanking}'
																	id="payerBalance" /> --%>
																<%-- <input type="hidden"
																	value='${selectedAccount.customerCurrentBalance}'
																	id="payerBalance" /> --%>
															</c:if> <input type="hidden"
															value='${selectedAccount.customerCurrentBalance}'
															id="payerBalance" /> <%-- <c:if test="${empty payMentList}">
																<input type="hidden" value='0.0' id="payerBalance" />
															</c:if> --%></td>
													</tr>

												</table>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="bzbtn">
							<button type="button" class="btn btn-info"
								style="float: right; font-size: 14px;"
								onclick="closeAddTransaction()">
								<spring:message code="BzComposer.global.cancel" />
							</button>
							<button type="button" class="btn btn-info"
								onclick="return addTransactionFromDialog()" id="addButton"
								style="float: right; margin-right: 10px; font-size: 14px;">
								<spring:message code="BzComposer.global.add" />
							</button>
						</div>
					</div>
					<!-- Here is the Payment Tab started -->
					<div class="container-fluid" id="paymentTab">
						<div class=" row">
							<div class=" col-12">
								<div class="border1  clearfix">
									<form>
										<div class="row">
											<div class="col-md-6" id="thirdTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payeraccount" /></label></td>
														<td><input type="text"
															value="${selectedAccount.name}" readonly="true"
															style="width: 150px" id="${selectedAccount.accountID}" /></td>
														<td><input type="hidden"
															value="${selectedAccount.accountCategoryID}"
															id="categoryIdForPayerForPayment"></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.paymethod" /></label></td>
														<td><select style="width: 100%"
															onchange="selectedPaymenthodForPayment()"
															id="payMethodForDlgForPayment">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${simpleTypes}" var="curObject"
																	varStatus="loop">
																	<c:if test="${not empty curObject.typeName}">
																		<option id="${curObject.id}">${curObject.typeName}</option>
																	</c:if>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.date" /></label></td>
														<td><input type="text" readonly="true"
															style="width: 150px" id="dlgDateForPayment" /></td>
													</tr>
												</table>
											</div>
											<div class="col-md-6" id="fourthTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payamount" /></label></td>
														<td><input type="number" style="width: 150px"
															id="payAmountForPayment" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.checkno" /></label></td>
														<td><input type="text" style="width: 150px"
															id="checkNumForDlgForPayment" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.payeeaccount" /></label></td>
														<td><%-- <select style="width: 100%"
															id="payeenameForPayment" onchange="selectVendor()">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${cvList}" var="curObject"
																	varStatus="loop">
																	<c:forEach items="${accountForClientVendor}"
																		var="curObject2">
																		<c:if
																			test="${curObject2 != null && curObject2.cvID == curObject.cvID}">
																			<option id="${curObject.cvID}"
																				value="${curObject2.customerCurrentBalance}"label="${loop.index}">${curObject.firstName}
																				${curObject.lastName} (${curObject.name})</option>
																		</c:if>
																	</c:forEach>
																</c:forEach>
														</select>
														 --%>
														<select id="payeenameForPayment"
															onchange="selectVendor()" style="width: 100%;">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${cvList}"
																	var="curObject" varStatus="loop">
																	<c:forEach items="${accountForClientVendor}"
																		var="curObject2">
																		<c:if
																			test="${curObject2 != null && curObject2.cvID == curObject.cvID}">
																			<option value="${curObject2.customerCurrentBalance}"
																				id="${curObject2.cvID}"<%-- label="${curObject2.accountTypeID} --%> ">
																				${curObject.firstName} ${curObject.lastName}
																				(${curObject.name})</option>
																		</c:if>
																	</c:forEach>
																</c:forEach>
														</select>
														
														
														
														</td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.category" /></label></td>
														<td><select style="width: 100%" id="category">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${categoryListForPayment}"
																	var="curObject">
																	<option id="${curObject.id}">${curObject.categoryNumber}
																		${curObject.name}</option>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td>
															<!-- label><spring:message code="BzComposer.banking.endingbalance"/></label -->
														</td>
														<%-- <td><input type="hidden" id="payeeBalance2" /> <c:if
																test="${not empty payMentList}">
																<input type="hidden"
																	value='${payMentList[0].balanceForBanking}'
																	id="payerBalance2" /></td>
														</c:if>
														<c:if test="${empty payMentList}">
															<input type="hidden" value='0.0' id="payerBalance2" />
															</td>
														</c:if>
														</td> --%>
														<td><input type="hidden" id="payeeBalance2" /> <c:if
																test="${not empty payMentList}">
																<%-- <input type="hidden"
																	value='${payMentList[0].balanceForBanking}'
																	id="payerBalance" /> --%>
																<%-- <input type="hidden"
																	value='${selectedAccount.customerCurrentBalance}'
																	id="payerBalance" /> --%>
															</c:if> <input type="hidden"
															value='${selectedAccount.customerCurrentBalance}'
															id="payerBalance2" /> <%-- <c:if test="${empty payMentList}">
																<input type="hidden" value='0.0' id="payerBalance2" />
															</c:if> --%></td>
													</tr>
												</table>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="bzbtn">
							<button type="button" class="btn btn-info"
								style="float: right; font-size: 14px;"
								onclick="closeAddTransaction()">
								<spring:message code="BzComposer.global.cancel" />
							</button>
							<button type="button" class="btn btn-info"
								style="float: right; margin-right: 10px; font-size: 14px;"
								onclick="return addTransactionFromDialogForPayment()"
								id="addButtonForPayment">
								<spring:message code="BzComposer.global.add" />
							</button>
						</div>
					</div>
					<!--    Here is the Deposit tab started -->
					<div class="container-fluid" id="depositTab">
						<div class=" row">
							<div class=" col-12">
								<div class="border1  clearfix">
									<form>
										<div class="row">
											<div class="col-md-6" id="fifthTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payeraccount" /></label></td>
														<%-- <td><select id="payerForDeposit"
															onchange="selectVendorForDeposit()" style="width: 100%;">
																<option></option>
																<c:forEach items="${cleintListForDeposit}"
																	var="curObject" varStatus="loop">
																	<c:if
																		test="${accountForClientListForDeposit[loop.index] != null && accountForClientListForDeposit[loop.index].cvID == curObject.cvID}">
																		<option
																			value="${accountForClientListForDeposit[loop.index].customerCurrentBalance}"
																			id="${accountForClientListForDeposit[loop.index].accountID}"
																			label="${accountForClientListForDeposit[loop.index].accountTypeID}">
																			${curObject.firstName} ${curObject.lastName}
																			(${curObject.name})</option>
																	</c:if>
																</c:forEach>
														</select></td> --%>
														<td><select id="payerForDeposit"
															onchange="selectVendorForDeposit()" style="width: 100%;">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${cleintListForDeposit}"
																	var="curObject" varStatus="loop">
																	<c:forEach items="${accountForClientListForDeposit}"
																		var="curObject2">
																		<c:if
																			test="${curObject2 != null && curObject2.cvID == curObject.cvID}">
																			<option value="${curObject2.accountTypeID}"
																				id="${curObject2.accountID}"<%-- label="${curObject2.accountTypeID} " --%>>
																				${curObject.firstName} ${curObject.lastName}
																				(${curObject.name})</option>
																		</c:if>
																	</c:forEach>
																</c:forEach>
														</select></td>
														<td><input type="hidden" value=""
															id="categoryIdForPayerForDeposit"></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.paymethod" /></label></td>
														<td><select style="width: 100%"
															onchange="selectedPaymenthodForPayment()"
															id="payMethodForDeposit">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${simpleTypes}" var="curObject"
																	varStatus="loop">
																	<c:if test="${not empty curObject.typeName}">
																		<option id="${curObject.id}">${curObject.typeName}</option>
																	</c:if>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.date" /></label></td>
														<td><input type="text" readonly="true"
															style="width: 150px" id="dateForDeposit" /></td>
													</tr>
												</table>
											</div>
											<div class="col-md-6" id="sixthTable">
												<table class="bz_dialogTable">
													<tr>
														<td width="40%"><label><spring:message
																	code="BzComposer.banking.payamount" /></label></td>
														<td><input type="number" style="width: 150px"
															id="payAmountForDeposit" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.checkno" /></label></td>
														<td><input type="text" style="width: 150px"
															id="checkNumForDeposit" /></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.payeeaccount" /></label></td>
														<td><select style="width: 100%"
															id="payeenameForDeposit"
															onchange="changeBankForDeposit()">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${accountList}" var="curObject"
																	varStatus="loop">
																	<c:if test="${not empty curObject.name}">
																		<option value="${curObject.customerCurrentBalance}"
																			id="${curObject.accountID}">${curObject.name}</option>
																	</c:if>
																</c:forEach>
														</select></td>
													</tr>
													<tr>
														<td><label><spring:message
																	code="BzComposer.banking.category" /></label></td>
														<td><select style="width: 100%"
															id="categoryForDeposit">
																<option value=""><spring:message
																		code="BzComposer.ComboBox.Select" /></option>
																<c:forEach items="${categoryListForDeposit}"
																	var="curObject" varStatus="loop">
																	<option id="${curObject.id}">${curObject.categoryNumber}
																		${curObject.name}</option>
																</c:forEach>
													</tr>
													<tr>
														<td>
															<!-- label><spring:message code="BzComposer.banking.endingbalance"/></label -->
														</td>
														<td><input type="hidden" id="payeeBalanceForDeposit" />
															<c:if test="${not empty payMentList}">
																<input type="hidden"
																	value="${payMentList[0].balanceForBanking}"
																	id="payerBalanceForDeposit" />
															</c:if> <c:if test="${empty payMentList}">
																<input type="hidden" value="0.0"
																	id="payerBalanceForDeposit" />
															</c:if></td>
													</tr>

												</table>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="bzbtn">
							<button type="button" class="btn btn-info"
								style="float: right; font-size: 14px;"
								onClick="closeAddTransaction()">
								<spring:message code="BzComposer.global.cancel" />
							</button>
							<button type="button" class="btn btn-info"
								style="float: right; margin-right: 10px; font-size: 14px;"
								onclick="return addTrafsactioFromDeposit()"
								id="addButtonForDeposit">
								<spring:message code="BzComposer.global.add" />
							</button>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

	<!--    Here is the EditTransaction dialog started -->
	<div class="container-fluid" id="EditTransactionDialog">
		<div class="table-responsive" style="padding-top: 20px">
			<div class="d-flex flex-column full-height">

				<div class="modal-body1">
					<div class=" row">
						<div class=" col-12">
							<div class="border1  clearfix">
								<form>
									<div class="row">
										<div class="col-md-6 " id="editTable1">
											<table class="bz_dialogTable devFirstEditTable">
												<tr>
													<td><label> <spring:message
																code="BzComposer.banking.payer" />
													</label></td>
													<td><select class="payerField" id="payerForEdit"
														onchange="" style="width: 100%;">
															<c:forEach items="${allClientVendor}" var="curObject"
																varStatus="loop">
																<option value="${curObject.cvID}">${curObject.firstName}
																	${curObject.lastName} (${curObject.name})</option>
															</c:forEach>
													</select></td>
													<td><input type="hidden" value=""
														id="categoryIdForPayerForDeposit2"></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.payee" /></label></td>
													<td><select class="accountFieldForEdit"
														id="payeeForEdit">
															<c:forEach items="${accountList}" var="curObject"
																varStatus="loop">
																<option value="${curObject.accountID}">${curObject.name}</option>
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.paymethod" /></label></td>
													<td><select class="payMethodForEdit"
														style="width: 100%" onchange="" id="payMethodForEdit">
															<c:forEach items="${allPaymentList}" var="curObject"
																varStatus="loop">
																<option value="${curObject.id}">${curObject.typeName}</option>
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.category" /></label></td>
													<td><select class="categoryForEdit"
														id="categoryForEdit">
															<c:forEach items="${allCategoryList}" var="curObject"
																varStatus="loop">
																<option value="${curObject.id}">${curObject.name}
																	${curObject.categoryNumber}</option>
															</c:forEach>
													</select></td>
												</tr>
											</table>
										</div>
										<div class="col-md-6 " id="editTable2">
											<table class="bz_dialogTable devSecondEditTable">
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.amount" /></label></td>

													<td><input class="devAmount" type="text"
														id="amountToEdit" /></td>

												</tr>
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.date" /></label></td>
													<td><input type="text" class="devDate"
														id="devDateForEdit" /></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="BzComposer.banking.checknumber" /></label></td>
													<td><input type="text" class="checkNumberForEdit"
														id="checkNumberEdit" /></td>
												</tr>
											</table>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="bzbtn">
			<button type="button" class="btn btn-info"
				style="float: right; font-size: 14px;"
				onclick="closeEditTransaction()">
				<spring:message code="BzComposer.global.cancel" />
			</button>
			<button type="button" class="btn btn-info"
				style="float: right; margin-right: 10px; font-size: 14px;"
				onclick="return editTransaction()" id="addButtonForDeposit">
				<spring:message code="BzComposer.global.save" />
			</button>
		</div>
	</div>
	<!--    Here is the AddAccount dialog started -->
	<div class="container-fluid" id="AddAccountDialog">
		<!--    Here is the AddMainCategory dialog started -->
		<div class="table-responsive"
			style="padding: 0 15px; font-size: 14px;">
			<h6>
				<spring:message code="BzComposer.banking.maincategoryinfo" />
			</h6>
			<div class="boxborderd">
				<div class="row">
					<label class="col-sm-3"><spring:message
							code="BzComposer.banking.maincategory" /></label>
					<div class="col-sm-9">
						<select class="form-control" id="mainCategoryID"
							onchange="afterMainCategoryChange()" style="width: 150px;">
							<option value=""><spring:message
									code="BzComposer.ComboBox.Select" /></option>
							<c:forEach items="${AccountCategoryList}" var="curObject"
								varStatus="loop">
								<option value="${curObject.accountCategoryID}">${curObject.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<label class="col-sm-3"> <spring:message
							code="BzComposer.banking.categoryname" /> <span
						class="errorField">*</span>
					</label>
					<div class="col-sm-4">
						<input type="text" id="mainCategoryName" class="form-control"
							style="width: 150px;" />
					</div>
					<div class="col-sm-5">
						<button type="button" class="btn btn-info"
							style="font-size: 14px;" id="mainCategoryBtnAdd"
							onclick="return saveMainCategory()">
							<spring:message code="BzComposer.global.save" />
						</button>
						<button type="button" class="btn btn-info"
							style="font-size: 14px;" id="mainCategoryBtnUpdate"
							onclick="return saveMainCategory()">
							<spring:message code="BzComposer.global.update" />
						</button>
					</div>
				</div>

			</div>
		</div>
		<!--    Here is the AddMainCategory dialog End -->

		<div class="table-responsive"
			style="padding: 0 15px; font-size: 14px;">
			<h6>
				<spring:message code="BzComposer.banking.accountinformation" />
			</h6>
			<div class="boxborderd">

				<div class="row">
					<label class="col-sm-3"><spring:message
							code="BzComposer.banking.accountcategory" /></label>
					<div class="col-sm-9">
						<select class="form-control" onchange="afterCategoryChange()"
							id="acForAddAccount" style="width: 150px;">
							<c:forEach items="${AccountCategoryList}" var="curObject"
								varStatus="loop">
								<option value="${curObject.accountCategoryID}">${curObject.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="row">
					<label class="col-sm-3"> <spring:message
							code="BzComposer.banking.accountname" /> <span
						class="errorField">*</span>
					</label>
					<div class="col-sm-9">
						<input type="text" id="accountName" name="accountName"
							class="form-control" style="width: 300px;" />
						<!-- <label class="checkboxWrapLable" for="isCategory"><input type="checkbox" name="isCategory" id="isCategory" onclick="isCategory()"> Is Category</label> -->
					</div>
				</div>

				<div class="text-right">
					<div class="inline-form">
						<label><spring:message
								code="BzComposer.banking.subaccountof" /></label> <select
							id="subaccountof" class="form-control"></select>
					</div>
				</div>

				<div class="text-left">
					<div class="inline-form">
						<label class="checkboxWrapLable" for="mainbankAccount"> <input
							id="mainbankAccount" name="mainbankAccount" type="checkbox" /> <spring:message
								code="BzComposer.banking.isthatmainbankaccount" /></label>
					</div>
				</div>

				<div class="row">
					<label class="col-sm-3"><spring:message
							code="BzComposer.banking.description" /></label>
					<div class="col-sm-9">
						<input type="text" name="descriptionAd" class="form-control"
							id="descriptionAd" />
					</div>
				</div>

				<hr>

				<!-- <div class="row">
   <label class="col-sm-3"><spring:message code="BzComposer.banking.openingbalance"/></label>
    <div class="col-sm-9">
   <div class="form-inline">
   <input type="text" name="" class="form-control" id="openingBalanceToAddAccount"/>
   </div>

   <div class="form-inline">
        <label><spring:message code="BzComposer.banking.asof"/></label>
        <input type="text" name="" class="form-control" id="dateForAddAccount"/>
        <div class="input-group-append"><a class="input-group-text" href="#">&#9662;</a></div>
   </div>
   </div>
</div> -->

				<div class="row">
					<label class="col-sm-3"><spring:message
							code="BzComposer.banking.depositfrom" /></label>
					<div class="col-sm-6">
						<select class="form-control" id="devAdAccount">
							<c:forEach items="${accountList}" var="curObject"
								varStatus="loop">
								<option id="${curObject.accountID}"
									value="${curObject.depositPaymentID}">${curObject.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="row">
					<label class="col-sm-3"><spring:message
							code="BzComposer.banking.checkno" /></label>
					<div class="col-sm-6">
						<input type="text" name="" class="form-control" id="devAdCheck" />
					</div>
				</div>

			</div>
			<div class="bzbtn text-center">
				<button type="button" class="btn btn-info" style="font-size: 14px;"
					onclick="return deleteBankAccount()" id="deleteBank">
					<spring:message code="BzComposer.global.delete" />
				</button>
				<button type="button" class="btn btn-info" style="font-size: 14px;"
					onclick="return addAccount()" id="addButtonForDeposit">
					<spring:message code="BzComposer.global.save" />
				</button>
				<!-- <button type="button" class="btn btn-info" style="font-size: 14px;" onclick="return addAccount()" id="EditButtonForDeposit">Save</button> -->
				<button type="button" class="btn btn-info" style="font-size: 14px;"
					onclick="closeAddTransactionDialog()" id="addButtonForDeposit">
					<spring:message code="BzComposer.global.cancel" />
				</button>
			</div>
		</div>
	</div>

	<script type="text/javascript">                               <!-- Javascript Begins Here -->
	var accountId = -1;
	var acID = -1;
	var day = new Date().getDay();
	var dName = dayName(day);
	var paymentMethod = '';
	var paymentMethodId = -1;
	var payeeId = -1;
	var payerrId= -1;
	var Index = -1;
	var paymentId = -1;
	/* var accountId = -1; */
	var tableName = '';
	var accountCategoryId = -1;
	var custStartBalance = 0.00;
	var AcName = "";
	var IsItMain = -1;
	var status;
	var totalPaymentList = -1;
	function selectRow(index , payId,accId,payeId,payMethodId,catId,amount,checkNumber,toCurrentbalance)
	{
		  var formatedDate=document.getElementById("formatedDate").textContent;

		    // console.log("amount: " + amount);
		    // console.log("toCurrentBalance"+toCurrentbalance);
		    // console.log("formattedDate: " + formatedDate); 
		/*   */ 
		this.Index = index;
		// console.log("Index "+this.Index);
		this.paymentId = payId;
		this.accountId = accId;
		/* $("select.payerField").val($('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(2)').attr('value')); */
		$("select.payerField").val(payeId);
		// console.log("payerid "+payeId);
		 var id = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(2)').attr('value'); 
		 if(id == '-1')
			{
			 	id = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(2)').attr('label');
			}
		$("select.accountFieldForEdit").val(accountId);
		/* $("select.payMethodForEdit").val($('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(7)').attr('value'));
		 */
		 $("select.payMethodForEdit").val(payMethodId);
		 var paymentType = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(7)').text().trim();
		// console.log("checknumber : "+paymentType);
		/* var checkNumber = paymentType.substring(6, 10); */
		$(".checkNumberForEdit").val(checkNumber);
		/* $("select.categoryForEdit").val($('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(8)').attr('value')); */
		$("select.categoryForEdit").val(catId);
		
		
		/* if($('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(7)').attr('value') == '1' || $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(7)').attr('value') == '192')
		{
			var checkNumber = paymentType.substring(6, 10);
			$(".checkNumberForEdit").val(checkNumber);
		} */
		// var payAmount = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(3)').text(); 
		//var payAmount = $(".amountToEdit").val(amount);
		
	/*  if(payAmount == '')
		{
			this.tableName = 'Deposit';
			//payAmount = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(4)').text();
			payAmount = document.getElementById("depositAmount").textContent.trim();
		}
		else
		{
			this.tableName = 'Payment';
		}  */
		
		 if(toCurrentbalance == ''|| toCurrentbalance == '0' ||toCurrentbalance ==0)
			{
				this.tableName = 'Payment';
				//payAmount = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(4)').text();
				payAmount = amount;
				$(".devAmount").val(amount);
			}
			else
			{
				this.tableName = 'Deposit';
				payAmount=toCurrentbalance;
				$(".devAmount").val(toCurrentbalance);
			} 
		
		// console.log("payamount"+payAmount);
		// console.log("tableName"+this.tableName);
		//$(".devDate").val($('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(1)').text());
		$(".devDate").val(formatedDate);
	
		// console.log("this.paymentId "+this.paymentId );
		// console.log("this.accountId "+this.accountId );
		// console.log("paymethod "+payMethodId );
		// console.log("catMethodId "+catId );
		/* $(".accountFieldForEdit>option").map(function() 
				{
					var idv = $(this).val();
					if(idv == id)
					{
						
						$("#payerForEdit").empty();
						$('#payeeForEdit option').clone().appendTo('#payerForEdit');	
					}
					else
						{
							$("#payerForEdit").empty();
							$('#payerForEditExtra option').clone().appendTo('#payerForEdit');
						}
					
				}); */
	}
function closeEditTransaction(){
		
	    $('#EditTransactionDialog').dialog('close');
	}
function closeAddTransactionDialog(){
	
    $('#AddAccountDialog').dialog('close');
}
	
function getAccountCategoryId(catId)
{
	
	this.accountCategoryId = catId;
	$("select.acForAddAccount").val(catId);
}

function closeMe(){
	
    $('#AddAccountDialog').dialog('close');
}
function closeAddTransaction(){
	
    $('#popupWindow').dialog('close');
}

function closeAddTransactionDialog(){

    $('#AddAccountDialog').dialog('close');
}
function closeTransferFund(){
    $('#popupWindow').dialog('close');
}
function closeEditTransaction(){
    $('#EditTransactionDialog').dialog('close');
}
	function editTransaction()
	{
		// console.log("inside edit");
		
		var oldClientVendorId = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(2)').attr('value');
		// console.log("oldClientVendorId "+oldClientVendorId);
		var oldAccountId = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(10)').attr('value');
		// console.log("oldAccountId "+oldAccountId);
		var oldPaymentTypeId = $('table.devBankingDatatable tbody tr:nth-child('+Index+')').find('td:nth-child(7)').attr('value');
		// console.log("oldPaymentTypeId "+oldPaymentTypeId);
		var customerIdString = document.getElementById("payerForEdit");
		// console.log("customerIdString "+customerIdString);
		var customerId = customerIdString.options[customerIdString.selectedIndex].value;
		// console.log("customerId "+customerId);
		
		var paymenttypeIdString = document.getElementById("payMethodForEdit");
		// console.log("paymenttypeIdString "+paymenttypeIdString);
		
		var paymentTypeId = paymenttypeIdString.options[paymenttypeIdString.selectedIndex].value;
		
		// console.log("paymentTypeId "+paymentTypeId);
		
		
		var accountIdString = document.getElementById("payeeForEdit");
		// console.log("accountIdString "+accountIdString);
		
		var accountId = accountIdString.options[accountIdString.selectedIndex].value;
		// console.log("accountId "+accountId);
		
		var categoryIdString = document.getElementById("categoryForEdit");
		// console.log("categoryIdString "+categoryIdString);
		
		var categoryId = categoryIdString.options[categoryIdString.selectedIndex].value; 
		// console.log("categoryId "+categoryId);
		
		var paymenTypeNameString = document.getElementById("payMethodForEdit");
		// console.log("paymenTypeNameString "+paymenTypeNameString);
		
		var paymentTypeName = paymenTypeNameString.options[paymenTypeNameString.selectedIndex].text; 
		// console.log("paymentTypeName "+paymentTypeName);
		
		var checkNumber = document.getElementById("checkNumberEdit").value;
		// console.log("checkNumber "+checkNumber);
		
		var amount = document.getElementById("amountToEdit").value;
		// console.log("amount "+amount);
		
		var date = document.getElementById("devDateForEdit").value;
		// console.log("date "+date);
		
		var TblPayment={
				   "cvID":customerId,
				   "paymentTypeID":paymentTypeId,	   
				   "accountID":accountId,
				   "categoryID":categoryId,
				   "oldclientVendorID":oldClientVendorId,
				   "oldAccountID":oldAccountId,
				   "paymentTypeName":paymentTypeName,
				   "oldPaymentTypeId":oldPaymentTypeId,
				   "checkNumber":checkNumber,
		   };
		var obj=JSON.stringify(TblPayment);
		 $('#EditTransactionDialog').dialog('close'); 
		$.ajax({
			
			type : "POST",
			url : "BankingCategory?tabid=EditTransaction",
			data : "row=" + obj + "&PaymentId=" + paymentId + "&amount=" +amount + "&date=" + date + "&tableName=" +tableName,
		    success : function(data) {
				/* var html = "" + data.msg; */
				   
		    	updatebankingTab(data);
		    	location.reload();
			
			},
			 error : function(data) {

				return errorMessageDialog();
			} 
		});
		
	}

function saveMainCategory(){
    let params = null;
    var mainCategoryID = document.getElementById("mainCategoryID").value.trim();
    var mainCategoryName = document.getElementById("mainCategoryName").value.trim();
    if(status == 'Edit' && mainCategoryID == ''){
        return shownotdeletedaccountdialog();
        return false;
    }
    if(mainCategoryName == ''){
        return showEnterRequiredFieldDialog();
        return false;
    }
    if(status == 'Edit'){
        params = "accountCategoryId=" + mainCategoryID + "&categoryName=" + mainCategoryName + "&Status=" + status;
    }else{
        params = "categoryName=" + mainCategoryName + "&Status=" + status;
    }
    $.ajax({
        type : "POST",
        url : "BankingCategory?tabid=SaveAccountMainCategory",
        data : params,
        success : function(data) {
            $('#AddAccountDialog').dialog('close');
            location.reload();
        },
         error : function(data) {
            return errorMessageDialog();
        }
    });
}
function addAccount(){
	
	var payerIdString = document.getElementById("devAdAccount");
	var payerId = payerIdString.options[payerIdString.selectedIndex].id;
	var customerCurrentBalanceString = payerIdString.options[payerIdString.selectedIndex].value; 
	var customerCurrentBalance = parseFloat(customerCurrentBalanceString);
	
	var isCategory = $('#isCategory').is(':checked');
	let openingBalance = 0;
    var isMainAccount = $('#mainbankAccount').is(':checked');
    var description = document.getElementById("descriptionAd").value;
    var accountName = document.getElementById("accountName").value;
    var checkNumber = document.getElementById("devAdCheck").value;
    //var date = document.getElementById("dateForAddAccount").value;

	if(status == 'Save'){
        /**if(payerId == '-1' && openingBalance > customerCurrentBalance){

            return showInsufficientAmountDialog();
            return false;
        }*/
    }
    if(accountName == ''){
        return showEnterRequiredFieldDialog();
        return false;
    }

	var TblPayment = {
        "checkNumber": checkNumber,
        "accountCategoryId":accountCategoryId,
        "payerID": payerId,
        "accountID":payerId,
        "isCategory":isCategory,
        "isMainAccount":isMainAccount,
        "openingbalance":openingBalance,
        "descriptionForAddAccount":description,
        "accountNameString":accountName
    };
	
    var obj = JSON.stringify(TblPayment);
	 $('#AddAccountDialog').dialog('close'); 
	 $.ajax({
        type : "POST",
        url : "BankingCategory?tabid=AddAccount",
        data :"obj=" + obj + "&date=" + date + "&Status=" + status + "&AccountId=" + acID,
        success : function(data) {
            
            /* window.location = "${pageContext.request.contextPath}/Banking?tabid=Banking"; */
            /* $(document).find('div#treeView2').replaceWith('<div id="treeView2" class="treeview">'+ $(data).find('div#treeView2').html() + '</div>'); */
            //$(document).find('div#treeView2').replaceWith('<div id="treeView2" class="treeview">'+ $(data).find('div#treeView2').html() + '</div>');
            //updatebankingTab(data);
            //updateTree();
            location.reload();
        },
         error : function(data) {
            return errorMessageDialog();
         }
     });
	 
	    // $(document.forms[0]).submit(function( event ) {
		    //    event.preventDefault();
	    //	});
	}

	function clearTransaction()
	{
		if(paymentId == '' || paymentId == -1)
		{
			// console.log("ghhhhhhhhhhhhhhhhh")
	    return selectTransactionDialog();
		return false;
		}
		else
		{
			/* var status = window.confirm("<spring:message code='BzComposer.banking.removetransaction'/>");
		   	if(status != true)
	   		{
	   			return false;
	   		} */
			return showremovetranactiondialog()
		}
		
	}

function afterMainCategoryChange(){
    var mainCategoryElement = document.getElementById("mainCategoryID");
    var categoryNAme = mainCategoryElement.options[mainCategoryElement.selectedIndex].text;
    document.getElementById("mainCategoryName").value = categoryNAme;
}
function afterCategoryChange()
{
	
	var indexForCategory = document.getElementById("acForAddAccount");
	var index = indexForCategory.options[indexForCategory.selectedIndex].value;
}
 function updateTree()
{
	$(document).ready(function () {
	    $('label.tree-toggler').click(function () {
	        $(this).toggleClass('thisOpen').parent().children('ul.tree').toggle(300);
	    });
	    $('li label').click(function () {
	         var selected = $(this).hasClass("highlight"); 
	         $("li label").removeClass("highlight");
	         if(!selected)
	             $(this).addClass("highlight");
	    });
	});
} 
function deleteBankAccount()
{
	if(totalPaymentList > 1)
	{

		return shownotdeletedaccountdialog();
		$('#AddAccountDialog').dialog('close'); 
		return false;
	}
	else
	{
		$('#AddAccountDialog').dialog('close'); 
		
		event.preventDefault();
		$("#deleteaccountdialog").dialog({
		    	resizable: false,
		        height: 200,
		        width: 500,
		        modal: true,
		        buttons: {
		        	"<spring:message code='BzComposer.global.ok'/>": function () {
		            	
		            	$.ajax({
		        			
		        			type : "POST",
		        			url : "BankingCategory?tabid=DeleteAccount",
		        			data :"AccountId=" +acID,
		        		    success : function(data) {
		        				
		        			/*  window.location = "${pageContext.request.contextPath}/Banking?tabid=Banking&Ac="+accountId; */  
		        				$(document).find('div#treeView2').replaceWith('<div id="treeView2" class="treeview">'+ $(data).find('div#treeView2').html() + '</div>');
		        		    	updatebankingTab(data);
		        		    	updateTree();
		        			},
		        			 error : function(data) {
		        				return errorMessageDialog();
		        			} 
		        		});
		            },
		            "<spring:message code='BzComposer.global.cancel'/>": function () {
		                $(this).dialog("close");
		                return false;
		            }
		        }
		    });
		    return false;
		
		/* var answer = window.confirm("<spring:message code='BzComposer.banking.wanttodeleteaccount'/>");
		if(answer == true)
		{	
			$.ajax({
				
				type : "POST",
				url : "Banking?tabid=DeleteAccount",			
				data :"AccountId=" +acID,
			    success : function(data) {
					
					//window.location = "${pageContext.request.contextPath}/Banking?tabid=Banking&Ac="+accountId;  
					$(document).find('div#treeView2').replaceWith('<div id="treeView2" class="treeview">'+ $(data).find('div#treeView2').html() + '</div>');
			    	updatebankingTab(data);
			    	updateTree();
				},
				 error : function(data) {

					return errorMessageDialog();
				} 
			});
		}
		else
		{
			return false;
		} */
	}
}
function dayName(date) {
	 
    var days = new Array(31);
    var j = 0;
    var d  = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
    for(var i=0;i<=31;i++)
 	   {
 	   		days[i] = d[j];
 	   		j++;
 	   		if(j == 7)
 	   			{
 	   				j = 0;
 	   			}
 	   }
    var d = days[date];
  	return d;
}

$(function() {
    $( "#dateFrom" ).datepicker();
    /* $( "#dateTo" ).datepicker(); */
});

function showTransaction(Id, catID, accName){
    let seletedItem = document.getElementById(Id);
    let isActive = seletedItem.classList.contains('highlight');
    let allAccounts = document.querySelectorAll("#treeView2 ul li ul li label.highlight");
    for(let x=0; x<allAccounts.length; x++){
        allAccounts[x].classList.remove('highlight');
    }
    if(isActive){
        Id = -1;
    }else{
        seletedItem.classList.add('highlight');
    }

	
    this.accountId = Id;
    this.acID = Id;
    this.accountCategoryId = catID;
    this.IsItMain = document.getElementById("IsItMain"+accountId).value;
    this.custStartBalance = document.getElementById("Account"+Id).value;
    this.AcName = accName;
    if(IsItMain == '0'){
        document.getElementById("mainbankAccount").checked = false;
    }
    else{
	    document.getElementById("mainbankAccount").checked = true;
	}
    $("#TotalRow").empty();
	$.ajax({
		type : "POST",
		url : "BankingCategory?tabid=Banking",
		data :"Ac=" +accountId,
	    success : function(data) {
			
			updatebankingTab(data);
			var totalPaymentListString = $("#RowCount").attr('value');
			totalPaymentList = parseInt(totalPaymentListString);
			$('#EditTransactionDialog').hide();
		},
		 error : function(data) {
			return errorMessageDialog();
		} 
    });
}

$(document).ready(function () {
    $('label.tree-toggler').click(function () {
        $(this).toggleClass('thisOpen').parent().children('ul.tree').toggle(300);
    });
    
    this.dName = dayName(this.day);
    $("#dateFrom").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
    $("#dateTo").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
    $("#popupWindow").hide();
    $("#EditTransactionDialog").hide();
    $("#AddAccountDialog").hide();
    $("#payerForEditExtra").hide();

    $('#transactionTable table tr').click(function () {
         var selected = $(this).hasClass("highlight");
          $("tr").removeClass("highlight");
         if(!selected)
             $(this).addClass("highlight");
    });

    //Select default account category
    setTimeout(function() {
        $("#treeView2>ul>li>label").trigger('click');
        $("#treeView2>ul>li").first().find('ul>li>label').first().trigger('click');
    }, 1000);

    $(document.forms[0]).submit(function( event ) {
        event.preventDefault();
    });

    $("#AddTransaction").on("click", function(){
		   document.getElementById('tabid').value="AddAccount";
		   
		   if(accountId == '-1'){

			   	return selectBankAccountDialog();
			   	return false;
		    }
		   else {
		    $("#popupWindow").dialog({
	    	   modal: true,
	    	   title: 'Add Transaction'
	        });
		   $("#dlgDate").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		   $("#dlgDateForPayment").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		   $("#dateForDeposit").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		    $("#paymentTab").hide(); 
		    $("#fundTrasferTab").show();
		    $("#depositTab").hide();
		    
		   }      
	    });

	   $("#showEditTransactionDialog").on("click", function(){
		   
		   if(Index == -1){

				return selectPaymentDialog();
				return false;
		    }
		   $("#EditTransactionDialog").dialog({
	    	   modal: true,
	    	   title: 'Edit Transaction'
	       });
	   });

    $("#AddAccount").on("click", function(){
        
		$('#deleteBank').prop('disabled', true);
		if(accountCategoryId == -1){

			return showselectcategorydialog();
			return false;
		}
		var indexForCategory = document.getElementById("acForAddAccount");
		var index = indexForCategory.options[indexForCategory.selectedIndex].value;
		status = "Save";
		document.getElementById("mainCategoryBtnAdd").style.display = "block";
		document.getElementById("mainCategoryBtnUpdate").style.display = "none";

		$("#AddAccountDialog").dialog({
	    	modal: true,
	        title: 'Add Account'
	    });
	    $("#acForAddAccount").val(accountCategoryId);
	    //$("#dateForAddAccount").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
	});

	$("#EditAccount").on("click", function(){
	    
		$('#deleteBank').prop('disabled', false);
		status = 'Edit';
		document.getElementById("mainCategoryBtnAdd").style.display = "none";
        document.getElementById("mainCategoryBtnUpdate").style.display = "block";
		$("#AddAccountDialog").dialog({
	    	modal: true,
	        title: 'Edit Bank Account'
	    });
        $.ajax({
            type : "POST",
            url : "BankingCategoryAjax?tabid=getBankAccount",
            data :"accountID=" + accountId,
            success : function(data) {
                $("#acForAddAccount").val(data.accountCategoryID);
                document.getElementById("accountName").value = data.name;
                document.getElementById('mainbankAccount').checked = data.isitmainaccount==1?true:false;
                document.getElementById("descriptionAd").value = data.description;
                document.getElementById("devAdAccount").value = data.depositPaymentID;
                document.getElementById("devAdCheck").value = data.firstCheckNo;
                //$("#dateForAddAccount").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
            },
             error : function(data) {
                return errorMessageDialog();
            }
        });
	});

	$("#TranseferFundsBtn").on("click", function(){
        
        $("#paymentTab").hide();
        $("#depositTab").hide();
        $("#dlgDate").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		   $("#dlgDateForPayment").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		   $("#dateForDeposit").val(dName+" "+((new Date().getMonth())+1)+"-"+new Date().getDate()+"-"+new Date().getFullYear());
		   
        $("#popupWindow").dialog({
            modal: true,
            title: 'Transefer Funds'
        });
        
    });

});

function isCategory(){
 	
	if($('#isCategory').is(':checked')){
	    $( "#devAdAccount" ).prop( "disabled", true );
	    $( "#devAdCheck" ).prop( "disabled", true );
	    $( "#subaccountof" ).prop( "disabled", true );
    }
    else{
	    $( "#devAdAccount" ).prop( "disabled", false );
		$( "#devAdCheck" ).prop( "disabled", false );
		$( "#subaccountof" ).prop( "disabled", false );
	}
}

 function addCss(){
	 $(document).ready(function () {
        
		$('#transactionTable table tr').click(function () {
		    var selected1 = $(this).hasClass("highlight");
		        $("tr").removeClass("highlight");
		        if(!selected1) $(this).addClass("highlight");
		    });
			/* $('label.tree-toggler').click(function () {
		        $(this).toggleClass('thisOpen').parent().children('ul.tree').toggle(300);
		    });
		    $('li label').click(function () {
		         var selected2 = $(this).hasClass("highlight"); 
		         $("li label").removeClass("highlight");
		         if(!selected2) $(this).addClass("highlight");
		    }); */
	 });
 }

function fundTrasferTab(){
	
	/* $( "#dialogTabs" ).tabs({ active: "#nav-home1-tab" }); */
	$("#fundTrasferTab").show();
	$("#paymentTab").hide();
	$("#depositTab").hide();
}
function paymentTab(){
	
	/* $( "#dialogTabs" ).tabs({ active: "#nav-home1-tab" }); */
	$("#paymentTab").show();
	$("#fundTrasferTab").hide();
	$("#depositTab").hide();
}
function depositTab(){
	$("#depositTab").show();
	$("#paymentTab").hide();
	$("#fundTrasferTab").hide();
}

function vendorIndex(index){
	
	this.Index = index;
}
function changeBank(){
	// console.log("here");
	/* var payername = document.getElementById("${selectedAccount.accountID}");
	var payerbalance = payername.options[payername.selectedIndex].value;
	// console.log("change bank payerbalance"+payerbalance); */
	 var payeeName = document.getElementById("payeename");
	 var balance = payeeName.options[payeeName.selectedIndex].value;
		// console.log("change bank payeebalance"+balance);
		var payerbaalance=document.getElementById("payerBalance").value;
		// console.log("payer balance "+payerbaalance);
		
	 this.payeeId = payeeName.options[payeeName.selectedIndex].id;
	 if(parseInt(payeeId) == parseInt(accountId))
		 {

		 		return selectotherbankdialog();
		 		 $('#addButton').attr('disabled',true);
		 		return false;
		 }
	 else{
	 document.getElementById("payeeBalance").value = balance; 
	 
	 $('#addButton').attr('disabled',false);
	 }
}
function selectVendorForDeposit(){
	
	var payerName = document.getElementById("payerForDeposit");
	var balance = payerName.options[payerName.selectedIndex].value;
	if(balance == '2.2E-306')
	{
		document.getElementById("payerBalanceForDeposit").value = '0.00';
	}
	else{
		document.getElementById("payerBalanceForDeposit").value = balance.toFixed(2);
	}
}
function changeBankForDeposit(){
	
	var payeeName = document.getElementById("payeenameForDeposit");
	var balance = payeeName.options[payeeName.selectedIndex].value;
	document.getElementById("payeeBalanceForDeposit").value = balance; 
}
function selectVendor(){
	
	var payeeName = document.getElementById("payeenameForPayment");
	
	this.payeeId = payeeName.options[payeeName.selectedIndex].id;
	// console.log("payeeId"+this.payeeId);
	this.Index = payeeName.options[payeeName.selectedIndex].id;
	// console.log("Index"+this.Index);
	var bal=payeeName.options[payeeName.selectedIndex].value;
	// console.log("bal"+bal);
}
function slectedPayMethod(){
	
	var pType = document.getElementById("payMethodForDlg");
	this.paymentMethod =  pType.options[pType.selectedIndex].value;
	this.paymentMethodId = pType.options[pType.selectedIndex].id;
	if(paymentMethod == 'Check'){
		document.getElementById("checkNumForDlg").readOnly = false;
	}	
	else{
		document.getElementById("checkNumForDlg").readOnly = true;
	}
}
function selectedPaymenthodForPayment(){
	 var pType = document.getElementById("payMethodForDlgForPayment");
	this.paymentMethodId = pType.options[pType.selectedIndex].id; 
}

function addTransactionFromDialog(){
	
	var AccountCategoryId = document.getElementById("categoryIdForPayer").value;
	// console.log("AccountCategoryId " +AccountCategoryId);
	var payerBalance  = parseFloat(document.getElementById("payerBalance").value);
	// console.log("payerBalance " +payerBalance);
    var payeeBalance = parseFloat(document.getElementById("payeeBalance").value);
    // console.log("payeeBalance " +payeeBalance);
    var payAmount = parseFloat(document.getElementById("payAmount").value);
    // console.log("payAmount " +payAmount);
    var checkNum = document.getElementById("checkNumForDlg").value;
    // console.log("checkNum " +checkNum);
    var date = document.getElementById("dlgDate").value;
    // console.log("date " +date);
	var pType = document.getElementById("payMethodForDlg");
	// console.log("pType " +pType);
	this.paymentMethod =  pType.options[pType.selectedIndex].value;
	// console.log("paymentMethod " +this.paymentMethod );
	payAmount = payAmount != null?payAmount:0;
	// console.log("payAmount " +payAmount  );
	if(paymentMethod == 'Check'){
        if(checkNum == '0' || checkNum == ''){
            return selectValidNumberDialog();
            return false;
        }
    }
    if(paymentMethod == ''){
        return selectPaymentDialog();
        return false;
    }
    // console.log("paymentMethod" +paymentMethod);
    if(payeeId == '' || payeeId == -1){
        return showSelectPayeeAccountFirstDialog();
        return false;
    }
    // console.log("payeeId" +payeeId);
    var TblPayment = {
        "accountCategoryId":AccountCategoryId,
        "accountID":accountId,
        "payerID": accountId,
        "payeeID" : payeeId,
        "fromBalance":payerBalance ,
        "toBalance": payeeBalance,
        "amount":payAmount,
        "paymentTypeID": paymentMethodId,
        "checkNumber": checkNum
    }
    var obj=JSON.stringify(TblPayment);
    // console.log("obj : "+obj)
    $('#popupWindow').dialog('close');
    $.ajax({
        type : "POST",
        url : "BankingCategory?tabid=Transferfund",
        data :"payment=" + obj + "&date=" + date,
        success : function(data) {
            
            updatebankingTab(data);
            location.reload();
        },
         error : function(data) {
            return errorMessageDialog();
        }
    });
   
}

function addTransactionFromDialogForPayment(){
	
	var payerBalance  = parseFloat(document.getElementById("payerBalance2").value);
	// console.log("payerBalance "+payerBalance);
	var payeeBalance = parseFloat(document.getElementById("payeenameForPayment").value);
	// console.log("payeeBalance "+payeeBalance);
	
	var payeenameForPaymentSelect = document.getElementById("payeenameForPayment");

    // Get the selected option
    var selectedOption = payeenameForPaymentSelect.options[payeenameForPaymentSelect.selectedIndex];

    // Get the id attribute of the selected option
    var payeenameForPaymentID = selectedOption.id;
    // console.log("payeenameForPaymentID " + payeenameForPaymentID);

	
    /* var payeenameForPaymentID = document.getElementById("payeenameForPayment").value; */
  /*   var payeenameForPaymentID = document.getElementById("payeenameForPayment").id;
    // console.log("payeenameForPaymentID "+payeenameForPaymentID); */
	
    var payAmount = parseFloat(document.getElementById("payAmountForPayment").value);
    // console.log("payAmount "+payAmount);
	
    
    var checkNum = document.getElementById("checkNumForDlgForPayment").value;
    // console.log("checkNum "+checkNum);

    var date = document.getElementById("dlgDateForPayment").value;
    var AccountCategoryId = document.getElementById("categoryIdForPayerForPayment").value;
    // console.log("AccountCategoryId "+checkNum);

    var categoryIdString =  document.getElementById("category");
    // console.log("categoryIdString "+checkNum);

    var categoryId = categoryIdString.options[categoryIdString.selectedIndex].id;
    // console.log("categoryId "+categoryId);

	var pType = document.getElementById("payMethodForDlgForPayment");
	  // console.log("pType "+pType);

	this.paymentMethod =  pType.options[pType.selectedIndex].value;
	  // console.log("paymentMethod "+paymentMethod);
	  var paymentMethodId = pType.options[pType.selectedIndex].id;
		// console.log("paymentMethodId: "+paymentMethodId);

    payAmount = payAmount != null?payAmount:0;
    if(paymentMethod == 'Check'){
        if(checkNum == '0' || checkNum == ''){
            return selectValidNumberDialog();
            return false;
        }
    }
    var TblPayment = {
        "fromBalance":payerBalance ,
        "toBalance": payeeBalance,
        "checkNumber": checkNum,
        "accountCategoryId":AccountCategoryId,
        "paymentTypeID": paymentMethodId,
        "payerID": accountId,
        "payeeID" : payeenameForPaymentID,
        "accountID":accountId,
        "amount":payAmount,
        "categoryId":categoryId
    }
    var obj=JSON.stringify(TblPayment);
    $('#popupWindow').dialog('close');
    $.ajax({
        type : "POST",
        url : "BankingCategory?tabid=TransferfundFromPayment",
        data :"payment=" + obj + "&date=" + date + "&index=" + Index,
        success : function(data) {
            
            updatebankingTab(data);
            location.reload();
        },
         error : function(data) {
            return errorMessageDialog();
        }
    });
   
}

function addTrafsactioFromDeposit(){
	
	var pType = document.getElementById("payMethodForDeposit");
	// console.log("pType: "+pType);
	this.paymentMethod =  pType.options[pType.selectedIndex].value;
	// console.log("paymentMethod: "+paymentMethod);
	var paymentMethodId = pType.options[pType.selectedIndex].id;
	// console.log("paymentMethodId: "+paymentMethodId);
	var payerBalance  = parseFloat(document.getElementById("payerBalanceForDeposit").value);
	// console.log("payerBalance: "+payerBalance);
    var payerName = document.getElementById("payerForDeposit");
	// console.log("payerName: "+payerName);
    var payerId = payerName.options[payerName.selectedIndex].id;
	// console.log("payerId: "+payerId);
   /*  var accountTypeId = payerName.options[payerName.selectedIndex].label; */
    var accountTypeId = payerName.options[payerName.selectedIndex].value;
    // console.log("accountTypeId: "+accountTypeId);
    var payeeName = document.getElementById("payeenameForDeposit");
    // console.log("payeeName: "+payeeName);
    var payeeId = payeeName.options[payeeName.selectedIndex].id;
    // console.log("payeeId: "+payeeId);
    var payeeBalance = parseFloat(document.getElementById("payeeBalanceForDeposit").value);
    // console.log("payeeBalance: "+payeeBalance);
    var payAmount = parseFloat(document.getElementById("payAmountForDeposit").value);
    // console.log("payAmount: "+payAmount);
    var checkNum = document.getElementById("checkNumForDeposit").value;
    // console.log("checkNum: "+checkNum);
    var date = document.getElementById("dateForDeposit").value;
    // console.log("date: "+date);
    var categoryIdString =  document.getElementById("categoryForDeposit");
    // console.log("categoryIdString: "+categoryIdString);
    var categoryId = categoryIdString.options[categoryIdString.selectedIndex].id;
    // console.log("categoryId: "+categoryId);
	if(paymentMethod == 'Check'){
        if(checkNum == '0' || checkNum == ''){
            return selectValidNumberDialog();
            return false;
        }
    }
	
	if(paymentMethod == ''){
        return selectPaymentDialog();
        return false;
    }
	// console.log("paymentMethod: "+paymentMethod);
    if(payeeId == '' || payeeId == -1){
        return showSelectPayeeAccountFirstDialog();
        return false;
    }
    // console.log("payeeId: "+payeeId);
	var TblPayment = {
        "fromBalance":payerBalance ,
        "toBalance": payeeBalance,
        "checkNumber": checkNum,
        /* "accountCategoryId":AccountCategoryId, */
        "paymentTypeID": paymentMethodId,
        "payerID": payerId,
        "payeeID" : payeeId,
        "amount":payAmount,
        "categoryId":categoryId,
        "accountTypeId":accountTypeId
    }
	var obj=JSON.stringify(TblPayment);
	// console.log("obj: "+obj);
	$('#popupWindow').dialog('close');
	$.ajax({
		type : "POST",
		url : "BankingCategory?tabid=TransferfundFromDeposit",
		data :"payment=" + obj + "&date=" + date +"&Ac=" +accountId,
	    success : function(data) {
			
			updatebankingTab(data);
			location.reload();
		},
		 error : function(data) {
			return errorMessageDialog();
		} 
	});
	
}

function updatebankingTab(data)
{
	$(document).find('div#selectedAccount h4').replaceWith($(data).find('div#selectedAccount').html()); 
	$(document).find('div#transactionTable table').replaceWith($(data).find('div#transactionTable').html());
	if($(data).find('div#bottom label').eq(1).text(this.value).text() == ""){
		$(document).find('div#bottom label').eq(1).text(this.value).replaceWith("<label class='text-right float-right' id='totalLabel'>"+0.00+"</label>");
	}
	else{
		$(document).find('div#bottom label').eq(1).text(this.value).replaceWith($(data).find('div#bottom label').eq(1).text(this.value));
	}	
	$(document).find('div#firstTable table').replaceWith($(data).find('div#firstTable').html());
	$(document).find('div#secondTable table').replaceWith($(data).find('div#secondTable').html());
	$(document).find('div#thirdTable table').replaceWith($(data).find('div#thirdTable').html());
	$(document).find('div#fourthTable table').replaceWith($(data).find('div#fourthTable').html());
	$(document).find('div#fifthTable table').replaceWith($(data).find('div#fifthTable').html());
	$(document).find('div#sixthTable table').replaceWith($(data).find('div#sixthTable').html());
	$(document).find('div#editTable1 table').replaceWith($(data).find('div#editTable1').html());
	$(document).find('div#editTable2 table').replaceWith($(data).find('div#editTable2').html());
	/* updateTree(); */ 
	addCss();
}

$( ".rangeOP" ).change(function() {
	     
	   var rangeType = document.getElementById("rangeId");
	   if(acID == '-1')
	{
		   acID = '56933';
	}
	   var range = rangeType.options[rangeType.selectedIndex].value;
	   var transactionFilterType = document.getElementById("transactionFilterId");
	   var transactionFilterId = transactionFilterType.options[transactionFilterType.selectedIndex].value;
		  $.ajax({
				
				type : "POST",
			 	url : "BankingCategory?tabid=slectedMenu&SelectedRange=" + range,
			 	data:"Ac=" +acID + "&TransactionRange=" + transactionFilterId,		
			    success : function(data)
			    {
			
			    updatebankingTab(data);
					 
				},
				 error : function(data) {
					return errorMessageDialog();
				} 
	  
	 });
});
$( ".transactionFilter" ).change(function() {
	     
	   var rangeType = document.getElementById("rangeId");
	   var transactionFilterType = document.getElementById("transactionFilterId");
	   var transactionFilterId = transactionFilterType.options[transactionFilterType.selectedIndex].value;
	   if(acID == '-1')
	{
		   acID = '56933';
	}
	   var range = rangeType.options[rangeType.selectedIndex].value;
		  $.ajax({
				
				type : "POST",
				url : "BankingCategory?tabid=slectedMenu&SelectedRange=" + range,
			 	data:"Ac=" +acID + "&TransactionRange=" + transactionFilterId,
			    success : function(data)
			    {
			
			    updatebankingTab(data);
					 
				},
				 error : function(data) {

					return errorMessageDialog();
				} 
	  
	 });
})
function errorMessageDialog()
{
	
	event.preventDefault();
	$("#errorMessageDialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function selectValidNumberDialog()
{
	
	event.preventDefault();
	$("#selectValidNumberDialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function selectBankAccountDialog()
{
	
	event.preventDefault();
	$("#selectBankAccountDialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showInsufficientBalanceDialog()
{
	
	event.preventDefault();
	$("#showInsufficientBalanceDialog").dialog({
    	resizable: false,
        height: 200,
        width: 500,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showInsufficientAmountDialog()
{
	
	event.preventDefault();
	$("#showInsufficientAmountDialog").dialog({
    	resizable: false,
        height: 200,
        width: 500,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function selectPaymentDialog()
{
	
	event.preventDefault();
	$("#selectPaymentDialog").dialog({
    	resizable: false,
        height: 200,
        width: 350,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function selectTransactionDialog()
{
	
	event.preventDefault();
	$("#selectTransactionDialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function selectotherbankdialog()
{
	
	event.preventDefault();
	$("#selectotherbankdialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function editableaccountcategorydialog()
{
	
	event.preventDefault();
	$("#editableaccountcategorydialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function canteditesalesbankaccountdialog()
{
	
	event.preventDefault();
	$("#canteditesalesbankaccountdialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showselectcategorydialog(){
	
	event.preventDefault();
	$("#showselectcategorydialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showEditableAccountNotSelectedDialog(){
	
	event.preventDefault();
	$("#showEditableAccountNotSelectedDialog").dialog({
    	resizable: false,
        height: 200,
        width: 500,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function shownotdeletedaccountdialog()
{
	
	event.preventDefault();
	$("#shownotdeletedaccountdialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function shostartingaccbalancedialog()
{
	
	event.preventDefault();
	$("#shostartingaccbalancedialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showremovetranactiondialog()
{

	event.preventDefault();
	$("#showremovetranactiondialog").dialog({
    	resizable: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
                 $.ajax({					
					type : "POST",
					url : "BankingCategory?tabid=deleteTransaction",
				/* 	data : "row=" + row + "&paymentTypeId=" +paymentTypeId + "&memo=" + memo + "&accountId=" +accountId + "&categoryId=" +categoryId + "&receivedAmount=" +receivedAmount, */			
					data :"paymentId=" + paymentId,
				    success : function(data) {
				    	
				    	 window.location = "${pageContext.request.contextPath}/Banking?tabid=Banking"; 
				    	updatebankingTab(data);
				    },
					 error : function(data) {
							return errorMessageDialog();
						} 
					});
			   $(document.forms[0]).submit(function( event ) {
				    event.preventDefault();
				});
                return true;
            },
            "<spring:message code='BzComposer.global.cancel'/>": function () {
                $(this).dialog("close");
            	return false;
            }
        }
    });

   return false; 
}

function showSelectPayeeAccountFirstDialog(){
	event.preventDefault();
	$("#showSelectPayeeAccountFirstDialog").dialog({
    	resizable: false,
        height: 200,
        width: 400,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}
function showEnterRequiredFieldDialog(){
	event.preventDefault();
	$("#showEnterRequiredFieldDialog").dialog({
    	resizable: false,
        height: 200,
        width: 400,
        modal: true,
        buttons: {
            "<spring:message code='BzComposer.global.ok'/>": function () {
                $(this).dialog("close");
            }
        }
    });
    return false;
}

</script>
</body>
</html>
<!-- Dialog box used in this page -->
<div id="errorMessageDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.erroroccured" />
	</p>
</div>
<div id="selectValidNumberDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.entervalidchecknumber" />
	</p>
</div>
<div id="selectBankAccountDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.selectbankaccount" />
	</p>
</div>
<div id="showInsufficientBalanceDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.entervalidamount" />
	</p>
</div>
<div id="showInsufficientAmountDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.insuffientamountinaccount" />
	</p>
</div>
<div id="selectPaymentDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.selectpayment" />
	</p>
</div>
<div id="selectTransactionDialog" style="display: none;">
	<p>
		<spring:message code="BzComposer.banking.selecttransaction" />
	</p>
</div>
<div id="selectotherbankdialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.selectotherbank' />
	</p>
</div>
<div id="editableaccountcategorydialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.canteditaccountcategory' />
	</p>
</div>
<div id="canteditesalesbankaccountdialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.canteditesalesbankaccount' />
	</p>
</div>
<div id="showselectcategorydialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.selectcategory' />
	</p>
</div>
<div id="shownotdeletedaccountdialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.selectcategory' />
	</p>
</div>
<div id="showremovetranactiondialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.removetransaction' />
	</p>
</div>
<div id="shostartingaccbalancedialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.startingaccountbalance' />
	</p>
</div>
<div id="deleteaccountdialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.wanttodeleteaccount' />
	</p>
</div>
<div id="showEditableAccountNotSelectedDialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.selectbankaccounttoedit' />
	</p>
</div>
<div id="showSelectPayeeAccountFirstDialog" style="display: none;">
	<p>
		<spring:message code='BzComposer.banking.selectpayeeaccountfirst' />
	</p>
</div>
<div id="showEnterRequiredFieldDialog" style="display: none;">
	<p>
		<spring:message code='error.common.recover.required' />
	</p>
</div>