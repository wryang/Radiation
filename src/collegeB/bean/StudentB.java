package collegeB.bean;

import java.util.List;

public class StudentB {
	private String studentNumber;
	private String name;
	private String sex;
	private String speciality;
	private List<CourseB> selectedCourse = null;
	private AccountB account;
	
	public static String FEMAL = "f";
	public static String MALE = "m";
	
	public static String INNER_STUDENT_ID = "1";
	
	public StudentB() {
		
	}
	
	public boolean isSelectFull() {
		return hasSelectedCourse() && selectedCourse.size() == 5;
	}
	public StudentB(String studentNumber, String name, String sex, String speciality) {
		this.studentNumber = studentNumber;
		this.name = name;
		this.sex = sex;
		this.speciality = speciality;
	}
	
	public boolean hasSelectedCourse() {
		return selectedCourse == null ? false : true;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public List<CourseB> getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(List<CourseB> selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public AccountB getAccount() {
		return account;
	}

	public void setAccount(AccountB account) {
		this.account = account;
	}
}
