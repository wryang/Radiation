package collegeA.bean;

public class AccountA {
	private String userName;
	private String password = defaultPassword;
	private Character priority = STUDENT;
	
	private static String defaultPassword = "123";
	public static Character ADMINISTER = 'a';
	public static Character STUDENT = 's';
	
	public AccountA() {
		
	}
	
	public AccountA(String username) {
		this.userName = username;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Character getPriority() {
		return priority;
	}
	public void setPriority(Character priority) {
		this.priority = priority;
	}
}
