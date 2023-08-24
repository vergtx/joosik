package edu.pnu.persistence;



import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Member findByUsername(String username);

	boolean existsByUsername(String username);


}