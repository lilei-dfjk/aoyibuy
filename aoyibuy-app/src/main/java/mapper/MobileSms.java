package mapper;

public class MobileSms {

	private String mobile;
	private String smsCode;
	private long lastSendAt;
	private String encryptedToken;
	
	public MobileSms() {
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public long getLastSendAt() {
		return lastSendAt;
	}

	public void setLastSendAt(long lastSendAt) {
		this.lastSendAt = lastSendAt;
	}

	public String getEncryptedToken() {
		return encryptedToken;
	}

	public void setEncryptedToken(String encryptedToken) {
		this.encryptedToken = encryptedToken;
	}

}
