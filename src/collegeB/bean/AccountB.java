package collegeB.bean;

public class AccountB {
	private String username;
	private String password = defaultPassword;
	private String degree = STUDNET.toString();
	private StudentB studentB;

	private static String defaultPassword = "123";
	
	public static Character ADMINISTER = 'a';
	public static Character STUDNET = 's';
	
	public AccountB() {
		
	}
	public AccountB(String stuNum) {
		this.username = stuNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public StudentB getStudentB() {
		return studentB;
	}
	public void setStudentB(StudentB studentB) {
		this.studentB = studentB;
	}
	
}
