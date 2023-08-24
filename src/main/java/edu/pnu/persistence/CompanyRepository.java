package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.CodeList;
import edu.pnu.domain.Company;


public interface CompanyRepository extends JpaRepository<Company, String> {

	Company findByCompanyName(String companyName);
	
	 

}
