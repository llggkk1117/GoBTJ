package org.gene.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.annotations.Expose;

import org.apache.log4j.Logger;
import org.gene.model.User;
import org.gene.modules.utils.JsonUtils;
import org.gene.dao.UserDAO;


@Controller
public class LoginController extends AbstractController
{
	final static Logger logger = Logger.getLogger(LoginController.class);
	
	public class LoginRequest
	{
		@Expose private String emailAddress;
		@Expose private String password;
		
		public String getEmailAddress()
		{
			return emailAddress;
		}
		public void setEmailAddress(String emailAddress)
		{
			this.emailAddress = emailAddress;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
	}
	
	
	public class LoginResponse
	{
		@Expose private boolean loginSuccess;
		@Expose private String displayMessage;
		@Expose private List<String> detailMessage;
		public boolean isLoginSuccess() {
			return loginSuccess;
		}
		public void setLoginSuccess(boolean loginSuccess) {
			this.loginSuccess = loginSuccess;
		}
		public String getDisplayMessage() {
			return displayMessage;
		}
		public void setDisplayMessage(String displayMessage) {
			this.displayMessage = displayMessage;
		}
		public List<String> getDetailMessage() {
			return detailMessage;
		}
		public void setDetailMessage(List<String> detailMessage) {
			this.detailMessage = detailMessage;
		}
	}
	

	
	
	@RequestMapping(value="/login.process", method=RequestMethod.POST)
	@ResponseBody
	public String processLogin(HttpServletRequest request, @RequestBody String json) throws UnsupportedEncodingException, ClassNotFoundException, SQLException, InterruptedException
	{
		request.getSession().removeAttribute("user");
		
		System.out.println("login module called");
		
		LoginRequest loginRequest = JsonUtils.jsonToObject(json, LoginRequest.class);
		String emailAddress = loginRequest.getEmailAddress();
		String password = loginRequest.getPassword();
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setLoginSuccess(false);
		int userStatus = UserDAO.userExists(emailAddress, password);
		if(userStatus==UserDAO.USER_EXISTS)
		{
			User user = UserDAO.retrieveUser(emailAddress);
			request.getSession().setAttribute("user", user);
			loginResponse.setLoginSuccess(true);
			loginResponse.setDisplayMessage("Logged in Successfully");
		}
		else if(userStatus==UserDAO.ACCOUNT_LOCKED)
		{
			loginResponse.setDisplayMessage("Account Locked");
		}
		else if(userStatus==UserDAO.CREDENTIAL_EXPIRED)
		{
			loginResponse.setDisplayMessage("Credential Expired");
		}
		else
		{
			loginResponse.setDisplayMessage("Login Failed");
		}
		
		String result = JsonUtils.objectToJson(loginResponse);
		System.out.println(result);

		result = encode(result);
    	
		return result;
	}
	

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String forwardToLogin(HttpServletRequest request)
	{
		request.getSession().removeAttribute("user");
		String view = "login";
		return view;
	}
}
