package com.nxsol.bzcomposer.company.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxsol.bzcomposer.company.domain.BcaPreference;

@Repository
public interface BcaPreferenceRepository extends JpaRepository<BcaPreference, Integer> {
	Optional<BcaPreference> findByCompany_CompanyId(Long companyId);

	@Query("select bp.invoiceStyle.invoiceStyleId from BcaPreference bp where bp.company.companyId = :companyId")
	Integer findInvoiceStyleIDByCompanyId(@Param("companyId") Long companyID);

	// Optional<BcaPreference> findByCompany_CompanyId(Long companyId);
	Optional<BcaPreference> findByCompany_CompanyIdAndActive(Long companyId, Integer active);

}
