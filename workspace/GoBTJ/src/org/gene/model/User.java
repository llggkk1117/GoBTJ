package org.gene.model;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class User
{
	@Expose private Long id;
	@Expose private String emailAddress;
	@Expose private String koreanName;
	@Expose private String englishName;
	@Expose private Date dateOfBirth;
	@Expose private Date dateJoined;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	public String getKoreanName()
	{
		return koreanName;
	}
	public void setKoreanName(String koreanName)
	{
		this.koreanName = koreanName;
	}
	public String getEnglishName()
	{
		return englishName;
	}
	public void setEnglishName(String englishName)
	{
		this.englishName = englishName;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Date getDateJoined() {
		return dateJoined;
	}
	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}
}
