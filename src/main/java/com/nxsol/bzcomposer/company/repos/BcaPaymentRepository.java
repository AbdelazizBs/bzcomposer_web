package com.nxsol.bzcomposer.company.repos;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nxsol.bizcomposer.common.ConstValue;
import com.nxsol.bizcomposer.common.JProjectUtil;
import com.nxsol.bzcomposer.company.domain.BcaAccount;
import com.nxsol.bzcomposer.company.domain.BcaCompany;
import com.nxsol.bzcomposer.company.domain.BcaInvoice;
import com.nxsol.bzcomposer.company.domain.BcaPayment;

@Repository
public interface BcaPaymentRepository extends JpaRepository<BcaPayment, Integer> {

	@Query("SELECT p.paymentId,p.amount,p.paymentType,p.clientVendor,p.invoice,p.dateAdded, p.isToBePrinted, p.isNeedtoDeposit,"
			+ "p.payeeId, p.payerId, p.checkNumber, p.category,p.accountCategoryId,p.transactionType, p.deleted ,p.account,"
			+ "c.firstName, c.lastName, c.name AS CompanyName , cat.name as CategoryName FROM BcaPayment  p "
			+ "LEFT JOIN BcaClientvendor  c ON p.clientVendor= c.clientVendorId "
			+ "LEFT JOIN BcaCategory  cat ON p.category=cat.categoryId WHERE p.company = :companyId "
			+ "AND c.status IN ('U','N') AND p.payerId = :accountId "
			+ "AND (:fromDate IS NULL OR p.dateAdded >= :fromDate) "
			+ "AND (:toDatee IS NULL OR p.dateAdded <= :toDatee) "
			+ "AND (p.deleted = 0 OR (p.payerId = -1 AND p.deleted = 0)) " + "ORDER BY p.priority DESC")
	List<Object[]> findListOfBcaPayment(@Param("accountId") int accountId, @Param("companyId") BcaCompany companyId,
			@Param("fromDate") OffsetDateTime fromDate, @Param("toDatee") OffsetDateTime toDatee);

	@Query("SELECT bill.service, bill.vendorId, payment.dateAdded, bill.payerId, bill.memo, payment.checkNumber, payment.amount, payment.isToBePrinted, "
			+ "bill.billNum, bill.category,bill.amountDue, payment.paymentType,"
			+ "payment.paymentId, cv.name as CompanyName, cv.firstName, cv.lastName,"
			+ "account.name as AccountName FROM BcaPayment as payment "
			+ "INNER JOIN BcaBill as bill ON payment.billNum = bill.billNum "
			+ "LEFT JOIN BcaClientvendor as cv ON bill.vendorId = cv.clientVendorId "
			+ "LEFT JOIN BcaAccount as account ON payment.payerId = account.accountId "
			+ "WHERE payment.deleted <> 1 AND cv.status = 'N' AND bill.company = :companyId "
			+ "ORDER BY payment.dateAdded DESC")
	List<Object[]> findPaidBillListsPayment(@Param("companyId") BcaCompany companyId);

	List<BcaPayment> findByCompany(BcaCompany compId);

	Optional<Integer> findTopByOrderByPaymentIdDesc();

	Optional<Integer> findTopByOrderByPriorityDesc();

	@Modifying
	@Transactional
	@Query("UPDATE BcaPayment bp SET bp.isNeedtoDeposit =0 ,bp.payeeId = :payeeId , bp.dateAdded = :dateAdded , "
			+ " bp.account = :account , bp.deleted = 0 , bp.priority = :priority WHERE bp.company = :company"
			+ " AND bp.paymentId = :paymentId")
	int depositToBcaPayment(@Param("payeeId") Integer payeeId, @Param("dateAdded") String dateAdded,
			@Param("account") BcaAccount account, @Param("priority") Integer priority,
			@Param("company") BcaCompany company, @Param("paymentId") Integer paymentId);

	@Query("select sum(bp.amount) from BcaPayment bp where bp.company =:company AND bp.invoice = :invoice")
	Double getSumOfAmount(BcaCompany company, BcaInvoice invoice);

	List<BcaPayment> findByPaymentIdAndCompany(int paymentId, BcaCompany company);

	@Modifying
	@Transactional
	@Query("update BcaPayment bp set bp.deleted = :deleted , amount = :amount ,"
			+ " payerId = :payerId , payeeId = :payeeId , payFromBalance = :payFromBalance ,"
			+ " payToBalance = :payToBalance , dateAdded = :dateAdded  where paymentId = :paymentId")
	int updateTranscationByPaymentId(@Param("deleted") boolean deleted, @Param("amount") double amount,
			@Param("payerId") int payerId, @Param("payeeId") int payeeId,
			@Param("payFromBalance") double payFromBalance, @Param("payToBalance") double payToBalance,
			@Param("dateAdded") OffsetDateTime dateAdded, @Param("paymentId") int paymentId);

	@Modifying
	@Transactional
	@Query("update BcaPayment bp set bp.isNeedtoDeposit = 1 where bp.paymentId = :paymentId")
	int updateBcaPaymentForToSetIsneedTodeposit(@Param("paymentId") Integer paymentId);
}
