<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/include/header.jsp"%>
<title><spring:message code="BzComposer.salesreportbyrepdetailstitle"/></title>
<style>
table.tabla-listados { width: 100%; border: 1px solid rgb(207, 207, 207); margin:auto; }
table.tabla-listados tbody tr.odd td { background: #e1e5e9; }
table.tabla-listados thead tr th { font-size: 14px; }
table.tabla-listados tbody tr td { font-size: 14px; }
</style>
</head>
<body>
<form:form action="SalesBord.do?tabid=SalesReportByRep" method="post" modelAttribute="salesBoardDto">
<div id="">
<div id="ddcolortabsline">&nbsp;</div>
<div id="cos">
<div class="statusquo ok">
<div id="hoja">
<div id="blanquito">
<div id="padding">
<div>
	<span style="font-size: 1.1em; font-weight: normal; color: #838383; margin: 30px 0px 15px 0px; 
	border-bottom: 1px dotted #333; padding: 0 0 .3em 0;">
		<spring:message code="BzComposer.report.salesreportbyrep.salesreportbyreptitle" />
	</span>
</div>
<div>
	<div id="table-negotiations">
		<table cellspacing="0" align="center" class="section-border" style="width: 100%;">
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr align="center">
				<td align="left">
					<c:if test="${not empty IsUpdated}">
						<strong>
							<span class="msgstyle"> ${IsUpdated}</span>
						</strong>
					</c:if>
				</td>
			</tr>
			<tr>
				<td align="left" width="46%" valign="top">
					<table class="table-notifications" width="100%">
						<tr>
							<th colspan="3" align="left" style="font-size:14px;">
								<spring:message code="BzComposer.report.salesreportbyrep.filteroption" />
							</th>
						</tr>
						<tr>
							<td>
							<div style="float:left">
							    <spring:message code="BzComposer.reportcenter.allinvoicelist.orderdate" />&nbsp;&nbsp;&nbsp;
								<c:if test="${not empty BlankValue}">
									<form:input path="orderDate1"  size="15" value="" />
								</c:if>
								<c:if test="${empty BlankValue}">
									<form:input path="orderDate1"  size="15" />
								</c:if>&nbsp;
                            </div>
                            <div style="padding-top: 5px;">
                                <img src="images/cal.gif" onclick="displayCalendar(document.salesboardForm.orderDate1,'mm-dd-yyyy',this);" />
                            </div>
							</td>
							<td align="left">
							    <div style="float:left">
								<spring:message code="BzComposer.reportcenter.allinvoicelist.to" /> &nbsp;&nbsp;&nbsp;
								<c:if test="${not empty BlankValue}">
									<form:input path="orderDate2"  size="15" value="" />
								</c:if>
								<c:if test="${empty BlankValue}">
									<form:input path="orderDate2"  size="15" />
								</c:if>&nbsp;
								</div>
								<div style="padding-top: 5px;">
								    <img src="images/cal.gif" onclick="displayCalendar(document.salesboardForm.orderDate2,'mm-dd-yyyy',this);">
								</div>
							</td>
							<td align="center">
								<input type="submit" class="formbutton" name="findBtn" 
								value='<spring:message code="BzComposer.reportcenter.allinvoicelist.searchbtn"/>'>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div>
			<table align="center">
				<tr>
					<td align="center">
						<c:if test="${not empty msg}">
							<font color="red"><b>${msg}</b></font>
						</c:if>
					</td>
				</tr>
			</table>	
			<span style="font-size: 1.1em; font-weight: normal; color: #838383; margin: 30px 0px 15px 0px; 
			border-bottom: 1px dotted #333; padding: 0 0 .3em 0;">
				<spring:message code="BzComposer.Report.Employee.SalesReportByRep" />
			</span>
			<div id="table-negotiations" style="overflow:auto;height:400;width:100%" class="section-border">
				<table class="tabla-listados" cellspacing="0">
					<thead>
						<tr>
							<th><spring:message code="BzComposer.report.salesreportbyrep.salesrepresentative" /></th>
							<th><spring:message code="BzComposer.report.salesreportbyrep.salesquantity" /></th>
							<th><spring:message code="BzComposer.report.salesreportbyrep.salesamount" /></th>
							<th><spring:message code="BzComposer.report.salesreportbyrep.refundamount" /></th>
							<th><spring:message code="BzComposer.report.salesreportbyrep.refundquantity" /></th>
							<th><spring:message code="BzComposer.report.salesreportbyrep.adjustedsales" /></th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty SalesReportByRep}">
							<input type="hidden" name="sListSize" id="lSize" value='${SalesReportByRep.size()}'>
                            <c:forEach items="${SalesReportByRep}" var="sale">
                                <tr>
                                    <td align="left">${sale.rep}</td>
                                    <td align="left">${sale.inventoryQty}</td>
                                    <td align="left">${sale.total}</td>
                                    <td align="left">0</td>
                                    <td align="left">.00</td>
                                    <td align="left">${sale.total}</td>
                                </tr>
                            </c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</form:form>
<%@ include file="/WEB-INF/jsp/include/footer.jsp"%>
</body>
</html>