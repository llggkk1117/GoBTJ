package org.gene.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.gene.model.User;
import org.gene.modules.utils.DateUtils;


public class UserDAO extends AbstractDAO
{
	private static HashMap<Long, User> users;
	static
	{
		users = new HashMap<Long, User>();
	}
	
	public static final int USER_EXISTS = 30000;
	public static final int CREDENTIAL_EXPIRED = 20000;
	public static final int ACCOUNT_LOCKED = 10000;
	public static final int USER_NOT_EXISTS = 0;
	public static int userExists(String emailAddress, String password) throws ClassNotFoundException, SQLException, InterruptedException
	{
		int userExists = 0; 
		db.openSession();
		@SuppressWarnings("unchecked")
		List<String[]> activeAccountsResult = (List<String[]>) db.execute("select member_id from credentials where email_address='?' and password='?' and activated=TRUE and expiration_date>=now()", emailAddress, password);
		db.closeSession();
		userExists = activeAccountsResult.size()>1 ? USER_EXISTS : USER_NOT_EXISTS; 
		
		if(userExists==0)
		{
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> accountResult = (List<String[]>) db.execute("select member_id from credentials where email_address='?' and password='?' and activated=FALSE", emailAddress, password);
			db.closeSession();
			userExists = accountResult.size()>1 ? ACCOUNT_LOCKED : USER_NOT_EXISTS; 
		}
		
		if(userExists==0)
		{
			db.openSession();
			@SuppressWarnings("unchecked")
			List<String[]> accountResult = (List<String[]>) db.execute("select member_id from credentials where email_address='?' and password='?' and expiration_date<now()", emailAddress, password);
			db.closeSession();
			userExists = accountResult.size()>1 ? CREDENTIAL_EXPIRED : USER_NOT_EXISTS; 
		}

		return userExists;
	}
	
	public static User retrieveUser(String emailAddress) throws SQLException, ClassNotFoundException, InterruptedException
    {
		User user = null;
		if(emailAddress!=null && !"".equals(emailAddress))
		{
			user = users.get(emailAddress);
			if(user == null)
			{
				String sql = "select id, korean_name, english_name, date_of_birth, email_address, date_joined from members where email_address='?'";
				db.openSession();
				@SuppressWarnings("unchecked")
				List<String[]> result = (List<String[]>) db.execute(sql, emailAddress);
				db.closeSession();
				if(result.size()==2)
				{
					String[] tuple = result.get(1);
					user = new User();
					Long memberId = Long.parseLong(tuple[0]);
					user.setId(memberId);
					user.setKoreanName(tuple[1]);
					user.setEnglishName(tuple[2]);
					user.setDateOfBirth(DateUtils.convertToDate(tuple[3]));
					user.setEmailAddress(tuple[4]);
					user.setDateJoined(DateUtils.convertToDate(tuple[5]));
					users.put(memberId, user);
				}
			}
		}

    	return user;
    }
	
	
	
	public static User retrieveUser(Long memberId) throws SQLException, ClassNotFoundException, InterruptedException
    {
		User user = null;
		if(memberId!=null && memberId>0)
		{
			user = users.get(memberId);
			if(user == null)
			{
				String sql = "select id, korean_name, english_name, date_of_birth, email_address, date_joined from members where id='?'";
				db.openSession();
				@SuppressWarnings("unchecked")
				List<String[]> result = (List<String[]>) db.execute(sql, memberId);
				db.closeSession();
				if(result.size()==2)
				{
					String[] tuple = result.get(1);
					user = new User();
					user.setId(Long.parseLong(tuple[0]));
					user.setKoreanName(tuple[1]);
					user.setEnglishName(tuple[2]);
					user.setDateOfBirth(DateUtils.convertToDate(tuple[3]));
					user.setEmailAddress(tuple[4]);
					user.setDateJoined(DateUtils.convertToDate(tuple[5]));
					users.put(memberId, user);
				}
			}
		}

    	return user;
    }
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException
	{
		User user = UserDAO.retrieveUser("gene.kwan.lee@gmail.com");
		System.out.println(user.getKoreanName());
		System.out.println(user.getDateOfBirth());
	}
}
