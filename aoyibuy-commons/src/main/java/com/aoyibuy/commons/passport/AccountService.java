package com.aoyibuy.commons.passport;


/**
 * 账户服务接口。由业务系统提供实现。
 * 
 * @author wh
 * @lastModified 2016-6-7 15:50:22
 */
public interface AccountService<A extends Account<T>, T> {

	/**
	 * 混淆码长度
	 */
	int getSaltSize();
	
	/**
	 * 获取散列算法
	 */
	String getHashAlgorithm();

	/**
	 * 获取加密次数
	 */
	int getHashInterations();
	
	/**
	 * 通过账户名称获取账户
	 * @param accountName
	 * @return
	 */
	A findByAccount(String account);

	T encryptAndSave(T account);

	T resetPassword(String accountNeedToResetPassword, String newPassword);

	String getAccountMobile(A account);

	Long countByAccount(String account);

	T findById(String accountId);
	
	A autoLogin(T account);

	boolean checkOldPassword(String accountId, String oldPassword);
}
