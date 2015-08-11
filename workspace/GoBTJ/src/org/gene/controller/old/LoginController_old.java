package org.gene.controller.old;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.annotations.Expose;

import org.gene.model.User;
import org.gene.controller.AbstractController;
import org.gene.dao.UserDAO;


@Controller
public class LoginController_old extends AbstractController
{
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
		@Expose private String errorMessage;
		
		public boolean isLoginSuccess()
		{
			return loginSuccess;
		}
		public void setLoginSuccess(boolean loginSuccess)
		{
			this.loginSuccess = loginSuccess;
		}
		public String getErrorMessage()
		{
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage)
		{
			this.errorMessage = errorMessage;
		}
	}
	
	@RequestMapping(value="/login.module", method=RequestMethod.POST)
	public String loginMethodPost(HttpServletRequest request)
	{
		String view = "login";
		request.getSession().removeAttribute("user");
		LoginRequest loginRequest = receiveObject(request, LoginRequest.class);
		if(loginRequest != null)
		{
			String emailAddressFromInput = loginRequest.getEmailAddress();
			String passwordFromInput = loginRequest.getPassword();
			if(emailAddressFromInput != null && !"".equals(emailAddressFromInput) && passwordFromInput!=null && !"".equals(passwordFromInput))
			{
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setLoginSuccess(false);
				
				int userExists = 0;
				try
				{
					userExists = UserDAO.userExists(emailAddressFromInput, passwordFromInput);
				}
				catch(Throwable t){t.printStackTrace();}
				
				if(userExists==1)
				{
					User user = null;
					try
					{
						user = UserDAO.retrieveUser(emailAddressFromInput);
					}
					catch(Throwable t){t.printStackTrace();}
					
					request.getSession().setAttribute("user", user);
					view = "pre-board";
					loginResponse.setLoginSuccess(true);
				}
				else if(userExists==2)
				{
					loginResponse.setErrorMessage("Account Locked");
				}
				else if(userExists==3)
				{
					loginResponse.setErrorMessage("Credential Expired");
				}
				else
				{
					loginResponse.setErrorMessage("Login Failed");
				}
				
				sendObject(request, loginResponse);
			}
		}
		
		return view;
	}
	
	
	
	@RequestMapping(value="/login.module", method=RequestMethod.GET)
	public String loginMethodGet(HttpServletRequest request)
	{
		request.getSession().removeAttribute("user");
		String view = "login";
		return view;
	}
}
