package com.aoyibuy.app.service;

import java.util.Arrays;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aoyibuy.commons.passport.Account;
import com.aoyibuy.commons.passport.AccountService;
import com.aoyibuy.commons.passport.MemberAdapter;
import com.aoyibuy.commons.passport.SessionService;
import com.aoyibuy.commons.passport.accesstoken.AccessTokenGenerator;
import com.aoyibuy.commons.springside.Digests;
import com.aoyibuy.commons.springside.Encodes;
import com.aoyibuy.dao.jpa.entity.Member;
import com.aoyibuy.dao.jpa.entity.MemberRepository;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements AccountService<Account<Member>, Member> {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private SessionService sessionService;
	
    @Autowired
    private AccessTokenGenerator accessTokenGenerator;
	
	@Override
	public int getSaltSize() {
		return 8;
	}
	
	@Override
	public String getHashAlgorithm() {
		return "SHA-1";
	}

	@Override
	public int getHashInterations() {
		return 1024;
	}

	@Override
	public Account<Member> findByAccount(String account) {
		Member member = memberRepository.findByAccount(account);
		return new MemberAdapter(member);
	}
	
	@Transactional
	@Override
	public Member encryptAndSave(Member member) {
		String password = member.getPassword();
		
		byte[] salt = Digests.generateSalt(getSaltSize());
		member.setSalt(Encodes.encodeHex(salt));
		
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt, getHashInterations());
		member.setPassword(Encodes.encodeHex(hashPassword));
		
		return memberRepository.save(member);
	}

	@Transactional
	@Override
	public Member resetPassword(String memberNeedToResetPassword,
			String newPassword) {
		Member member = findById(memberNeedToResetPassword);
		member.setPassword(newPassword);
		return encryptAndSave(member);
	}

	@Override
	public Long countByAccount(String account) {
		return memberRepository.countByAccount(account);
	}

	@Override
	public Member findById(String accountId) {
		return memberRepository.findOne(accountId);
	}

	/**
	 * @see
	 * <code>StatelessLoginRealm.doGetAuthenticationInfo()</code>
	 * <code>StatelessLoginRealm.initCredentialsMatcher()</code>
	 * <code>HashedCredentialsMatcher.hashProvidedCredentials()</code>
	 */
	@Override
	public boolean checkOldPassword(String memberId, String oldPassword) {
		Member member = findById(memberId);
		Hash hashedOldPassword = new SimpleHash(getHashAlgorithm(), oldPassword, 
				Hex.decode(member.getSalt()), getHashInterations());
		return Arrays.equals(hashedOldPassword.getBytes(), Hex.decode(member.getPassword()));
	}

	/**
	 * @see StatelessLoginAuthcFilter.onLoginSuccess()
	 */
	@Override
	public Account<Member> autoLogin(Member member) {
		String accessToken = accessTokenGenerator.encode(member.getId());
		Account<Member> account = new MemberAdapter(member);
        sessionService.record(accessToken, account); // 记录登录账户信息
		return account; // 在record()方法中，向设置了accessToken
	}

	@Override
	public String getAccountMobile(Account<Member> account) {
		// TODO Auto-generated method stub
		return null;
	}

}
