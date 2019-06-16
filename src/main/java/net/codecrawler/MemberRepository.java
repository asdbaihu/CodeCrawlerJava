package net.codecrawler;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MemberRepository extends CrudRepository<Member, Long> {
	
	List<Member> findByEmail(String email);
	List<Member> findByMobile(String mobile);
	List<Member> findByAuth(String auth);
}