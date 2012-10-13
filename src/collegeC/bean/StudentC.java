package collegeC.bean;

import java.util.List;

public class StudentC {
	private String sno;
	private String snm;
	private String sex;
	private String sde;
	private AccountC acc;
	private List<CourseC> selectedCourse;
	
	public static String FEMALE = "f";
	public static String MALE= "m";
	
	public static String INNER_STUDENT_ID = "2";
	
	public StudentC(String sno, String snm, String sex, String sde) {
		this.snm = snm;
		this.sno = sno;
		this.sex = sex;
		this.sde = sde;
	}
	
	public StudentC() {
		
	}
	public boolean isSelectFull() {
		return hasSelectedCourse() && selectedCourse.size() == 5;
	}
	public boolean hasSelectedCourse() {
		return selectedCourse == null ? false : true;
	}
	public void setAccount(AccountC acc) {
		this.acc = acc;
	}
	public AccountC getAccount() {
		return this.acc;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getSnm() {
		return snm;
	}
	public void setSnm(String snm) {
		this.snm = snm;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSde() {
		return sde;
	}
	public void setSde(String sde) {
		this.sde = sde;
	}

	public AccountC getAcc() {
		return acc;
	}

	public void setAcc(AccountC acc) {
		this.acc = acc;
	}

	public List<CourseC> getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(List<CourseC> selectedCourse) {
		this.selectedCourse = selectedCourse;
	}
}
