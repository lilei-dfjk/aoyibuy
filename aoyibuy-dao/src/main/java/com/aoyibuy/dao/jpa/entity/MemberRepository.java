package com.aoyibuy.dao.jpa.entity;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemberRepository extends PagingAndSortingRepository<Member, String> {

	Member findByAccount(String account);

	List<Member> findByStatus(String status);

	@Query("from Member where id in (?1)")
	List<Member> findByIds(List<String> ids);
	
	Member findByCode(String code);
	
//	Member findByContactMobile(String mobile);
	
	Member findByRegisterMobile(String registerMobile);

	Long countByAccount(String account);

	Long countByRegisterMobile(String registerMobile);
	
	@Query("select m.operateStatus from Member m where m.id = ?1")
	String findOperateStatusById(String memberId);
}
