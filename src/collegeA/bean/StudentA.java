package collegeA.bean;

import java.util.List;

public class StudentA {
	private String studentId;
	private String studentName;
	private String gender;
	private String institution;
	private List<CourseA> selectedCourse;
	private AccountA relativeUser;
	
	public static String MALE = "m";
	public static String FEMALE = "f";
	
	public static String INNER_STUDENT_ID = "0";
	
	public StudentA() {
		
	}
	
	public StudentA(String studentID, String studentName, String gender, String institution) {
		this.studentId = studentID;
		this.studentName = studentName;
		this.gender = gender;
		this.institution = institution;
	}
	public boolean isSelectFull() {
		return hasSelectedCourse() && selectedCourse.size() == 5;
	}
	public boolean hasSelectedCourse() {
		return selectedCourse == null ? false : true;
	}
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public List<CourseA> getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(List<CourseA> selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public AccountA getRelativeUser() {
		return relativeUser;
	}

	public void setRelativeUser(AccountA account) {
		this.relativeUser = account;
	}

}
