package collegeC.bean;

import java.util.Date;

public class AccountC {
	private String acc;
	private String passwd = defaultPassword;
	private int degree = STUDENT;
	private Date createDate;
	
	private static String defaultPassword = "123";
	public static int ADMINISTER = 1;
	public static int STUDENT = 2;
	
	public AccountC() {
		
	}
	
	public AccountC(String username) {
		this.acc = username;
	}
	public String getAcc() {
		return acc;
	}
	public void setAcc(String acc) {
		this.acc = acc;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
