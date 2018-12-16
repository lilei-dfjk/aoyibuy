package com.aoyibuy.app.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aoyibuy.app.model.form.RegisterForm;
import com.aoyibuy.commons.passport.Account;
import com.aoyibuy.commons.passport.AccountService;
import com.aoyibuy.dao.jpa.entity.Member;
import com.aoyibuy.dao.jpa.entity.Member.AccountStatus;


@Service
public class RegisterServiceImpl implements RegisterService { 	
    @Resource(name = RegisterMobileSmsImpl.BEAN)
    private MobileSmsService mobileSmsService;
    
    @Autowired
    private SmsSender smsSender;
    
    @Autowired
   	private AccountService<Account<Member>, Member> memberService;
    
    private static final String SMS = 
    		"感谢您奥绎代购平台，验证码为%s，10分钟内有效，请妥善保管。";
	
	@Override
	public boolean checkSmsInterval(String registerToken) {
		return mobileSmsService.checkSmsInterval(registerToken);
	}
	
	@Override
	public void sendSms(String registerToken, String mobile) {
		String smsCode = RandomStringUtils.randomNumeric(6); // 6位随机数
		mobileSmsService.recordMobileSms(registerToken, mobile, smsCode, registerToken);
		smsSender.sendSms(mobile, SMS, smsCode);
	}
	
	@Override
	public boolean checkMobileSmsCode(String registerToken, String mobile, String smsCode) {
		return mobileSmsService.checkMobileSmsCode(registerToken, mobile, smsCode, registerToken);
	}

	@Override
	public String generateRandomRegisterToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public Member register(String registerToken, RegisterForm registerForm) {
		clearRegisterToken(registerToken);
		return registerMember(registerForm);
	}

	private Member registerMember(RegisterForm registerForm) {
		Member member = new Member();
		member.setAccount(registerForm.getUsername());
		member.setPassword(registerForm.getPassword());
		member.setMobile(registerForm.getMobile());
		member.setRegisterAt(new Date());
		member.setStatus(AccountStatus.UNAUTHC); // 已注册，未认证
		return memberService.encryptAndSave(member);
	}
	
	private void clearRegisterToken(String registerToken) {
		mobileSmsService.deleteMobileSms(registerToken);
	}

}
