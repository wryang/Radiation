package collegeA.bean;

public class CourseA {
	private String classId;
	private String className;
	private String grade;
	private String teacher;
	private String location;
	private Character share = NON_SHARED;
	
	public static Character SHARED = 's';
	public static Character NON_SHARED = 'n';
	
	public static String INNER_COURSE_ID = "1";
	
	public boolean isLocalCourse() {
		if(classId.startsWith(INNER_COURSE_ID)) {
			return true;
		}else {
			return false;
		}
	}
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Character getShare() {
		return share;
	}
	public void setShare(Character share) {
		this.share = share;
	}
	public static Character getSHARED() {
		return SHARED;
	}
	public static void setSHARED(Character sHARED) {
		SHARED = sHARED;
	}
	public static Character getNON_SHARED() {
		return NON_SHARED;
	}
	public static void setNON_SHARED(Character nON_SHARED) {
		NON_SHARED = nON_SHARED;
	}
	
}
