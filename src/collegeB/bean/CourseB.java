package collegeB.bean;

public class CourseB {
	private String id;
	private String courseName;
	private String period;
	private String point;
	private String teacher;
	private String address;
	private Character share = NON_SHARED;

	public static Character SHARED = 's';
	public static Character NON_SHARED = 'u';
	
	public static String INNER_COURSE_ID = "2";

	public CourseB() {

	}
	
	public boolean isLocalCourse() {
		if(id.startsWith(INNER_COURSE_ID)) {
			return true;
		}else {
			return false;
		}
	}

	public CourseB(String id, String courseName, String period, String point,
			String teacher, String address, Character share) {
		this.id = id;
		this.courseName = courseName;
		this.period = period;
		this.teacher = teacher;
		this.address = address;
		this.point = point;
		this.share = share;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Character getShare() {
		return share;
	}

	public void setShared(Character share) {
		this.share = share;
	}

}
