package com.nxsol.bzcomposer.company.repos;

import com.nxsol.bzcomposer.company.domain.BcaCreditcardtype;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BcaCreditcardtypeRepository extends JpaRepository<BcaCreditcardtype, Integer> {
	List<BcaCreditcardtype>findByCompany_CompanyIdAndActive(Long companyId,Integer active);
}
