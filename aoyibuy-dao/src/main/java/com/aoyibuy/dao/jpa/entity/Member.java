package com.aoyibuy.dao.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.aoyibuy.dao.jpa.UuidEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_member")
public class Member extends UuidEntity {
	
	@Column(length = 36, nullable = false, unique = true)
	private String account; // 账户
	
	@JsonIgnore
	@Column(length = 40, nullable = false)
	private String password; // 密码
	
	@JsonIgnore
	@Column(length = 16, nullable = false)
	private String salt; // 混淆码
	
	@Column(length = 15)
	private String mobile;
	
	@Column(length = 15)
	private String webChatAccount; // 微信账号
	
	private String mail;
	
	@Column(length = 19)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerAt;
	 
	private AccountStatus status;
	
	public enum AccountStatus {

		/**
		 * 正常
		 */
		NORMAL("正常"),
		
		/**
		 * 冻结
		 */
		FORZEN("冻结"),
		
		/**
		 * 未认证
		 */
		UNAUTHC("未认证"); // 表示已完成基础信息注册，但未完成认证
		
		@Getter
		private String text;
		
		private AccountStatus(String text) {
			this.text = text;
		}
	}
}
