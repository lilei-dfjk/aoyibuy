package com.aoyibuy.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.aoyibuy.dao.jpa.entity.Member;


public interface MemberService {

	Page<Member> findMemberList(Member member, Integer pageNumber, Integer pageSize);

	Member saveMember(Member member);
	
	Member editMember(Member member);

	void delMember(String id);

	Member findById(String id);
	
	public List<Member> findAll();

	Object[] getMemberAuths(String id);

	void saveMemberAuths(String id, String auths) throws Exception;

}
