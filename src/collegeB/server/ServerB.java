package collegeB.server;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import collegeB.bean.AccountB;
import collegeB.bean.ChoiceB;
import collegeB.bean.CourseB;
import collegeB.bean.StudentB;

public class ServerB {

	private static ServerB server = null;
	private static XMLServerB xmlServer = null;
	private static NetworkServerB networkServer = null;
	private static StudentB currentUser = null;
	private static Connection con = null;

	private ServerB() {
		this.connectDatabase();
		networkServer = NetworkServerB.getInstance(this);
		xmlServer = XMLServerB.getInstance();
	}

	public static ServerB getInstance() {
		if (null == server) {
			server = new ServerB();
			return server;
		} else {
			return server;
		}
	}

	private void connectDatabase() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Properties props = new Properties();
		props.put("user", "system");
		props.put("password", "091250193");
		String conURL = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			con = DriverManager.getConnection(conURL, props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeDatabase() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con = null;
	}

	public void updateCurrentUser() {
		setCurrentStudent(getStudent(currentUser.getStudentNumber()));
	}

	private void setCurrentStudent(StudentB student) {
		currentUser = student;
	}

	public boolean validate(String username, String password) {
		if (null == username || null == password) {
			return false;
		}
		ResultSet rs = null;
		boolean result = false;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from ACCOUNT where USERNAME='"
					+ username + "' and PASSWORD='" + password + "'");
			if (null != rs && rs.next()) {
				String stuRef = rs.getString(4);
				Character degree = rs.getString(3).charAt(0);
				if (degree.equals(AccountB.STUDNET)) {
					rs.close();
					rs = st.executeQuery("select * from student where STUDENTNUMBER="
							+ stuRef);
					if (null != rs && rs.next()) {
						String studentNumber = rs.getString(1);

						StudentB stu = getStudent(studentNumber);
						setCurrentStudent(stu);
					}
				} else {
					result = false;
				}
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public StudentB getCurrentUser() {
		return currentUser;
	}

	public boolean isAdminister() {
		return currentUser == null ? true : false;
	}

	public boolean addLocalStudent(StudentB student) {
		if (null == student || student.getStudentNumber() == null
				|| student.getStudentNumber().equals("")) {
			return false;
		}
		student.setStudentNumber(StudentB.INNER_STUDENT_ID
				+ student.getStudentNumber());
		return addStudent(student);
	}

	private boolean addStudent(StudentB student) {
		if (null == student || student.getStudentNumber() == null) {
			return false;
		}
		StudentB stu = getStudent(student.getStudentNumber());
		if (stu != null) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("insert into STUDENT values(?, ?, ?, ?)");

			pstmt.setString(1, student.getStudentNumber());
			pstmt.setString(2, student.getName());
			pstmt.setString(3, student.getSex());
			pstmt.setString(4, student.getSpeciality());

			pstmt.executeUpdate();
			AccountB c = null;

			if (student.getAccount() != null) {
				c = student.getAccount();
			} else {
				c = new AccountB(student.getStudentNumber());
			}

			PreparedStatement stmt = con
					.prepareStatement("insert into ACCOUNT values(?, ?, ?, ?)");
			c.setStudentB(student);
			stmt.setString(1, c.getUsername());
			stmt.setString(2, c.getPassword());
			stmt.setString(3, c.getDegree());
			stmt.setString(4, student.getStudentNumber());

			stmt.execute();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean removeStudent(StudentB student) {
		if (null == student || student.getStudentNumber() == null
				|| student.getStudentNumber().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			Statement st = con.createStatement();
			st.execute("delete from ACCOUNT where STUDENT_REF='"
					+ student.getStudentNumber() + "'");
			if (student.hasSelectedCourse()) {
				List<CourseB> list = student.getSelectedCourse();
				Iterator<CourseB> it = list.iterator();
				while (it.hasNext()) {
					CourseB course = (CourseB) it.next();
					cancelCourseChoice(student, course);
				}
			}
			st.execute("delete from STUDENT where STUDENTNUMBER='"
					+ student.getStudentNumber() + "'");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean addLocalCourse(CourseB course) {
		if (null == course) {
			return false;
		}
		course.setId(CourseB.INNER_COURSE_ID + course.getId());
		return addCourse(course);
	}

	private boolean addCourse(CourseB course) {
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("insert into COURSE values(?, ?, ?, ?, ?, ?, ?)");

			pstmt.setString(1, course.getId());
			pstmt.setString(2, course.getCourseName());
			pstmt.setString(3, course.getPeriod());
			pstmt.setString(4, course.getPoint());
			pstmt.setString(5, course.getTeacher());
			pstmt.setString(6, course.getAddress());
			pstmt.setString(7, course.getShare().toString());

			pstmt.executeUpdate();
			pstmt.close();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean removeCourse(CourseB course) {
		if (null == course || course.getId() == null
				|| course.getId().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from CHOOSE where COURSEID='"
							+ course.getId() + "'");
			if (rs.next()) {
				String studentNumber = rs.getString(2);
				StudentB student = getStudent(studentNumber);
				cancelCourseChoice(student, course);
			}
			st.executeQuery("delete from COURSE where ID='" + course.getId()
					+ "'");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public StudentB getStudent(String studentId) {
		if (null == studentId) {
			return null;
		}
		StudentB stu = null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from STUDENT where STUDENTNUMBER='"
							+ studentId + "'");
			if (rs.next()) {
				String studentNumber = rs.getString(1);
				String name = rs.getString(2);
				String sex = rs.getString(3);
				String speciality = rs.getString(4);

				stu = new StudentB(studentNumber, name, sex, speciality);
				Statement cst = con.createStatement();
				ResultSet crs = cst
						.executeQuery("select COURSEID from CHOOSE where STUDENTNUMBER='"
								+ studentId + "'");
				if (crs.next()) {
					String id = crs.getString(1);
					Statement tmpSt = con.createStatement();
					ResultSet tcrs = tmpSt
							.executeQuery("select * from COURSE where ID='"
									+ id + "'");
					List<CourseB> list = new ArrayList<CourseB>();
					while (tcrs.next()) {
						String courseId = crs.getString(1);
						CourseB selectedCourse = getCourse(courseId);

						list.add(selectedCourse);
					}
					if (list.size() != 0) {
						stu.setSelectedCourse(list);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stu;
	}

	public CourseB getCourse(String classId) {
		if (null == classId) {
			return null;
		}
		CourseB cb = null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from COURSE where ID='"
					+ classId + "'");
			if (rs.next()) {
				String id = rs.getString(1);
				String courseName = rs.getString(2);
				String period = rs.getString(3);
				String teacher = rs.getString(4);
				String point = rs.getString(5);
				String address = rs.getString(6);
				String share = rs.getString(7);

				cb = new CourseB(id, courseName, period, point, teacher,
						address, share.charAt(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cb;
	}

	public boolean modifyStudentInfo(StudentB student) {
		if (null == student || student.getStudentNumber() == null
				|| student.getStudentNumber().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update STUDENT set NAME=?, SEX=?, SPECIALITY=? where STUDENTNUMBER='"
							+ student.getStudentNumber() + "'");
			pstmt.setString(1, student.getName());
			pstmt.setString(2, student.getSex());
			pstmt.setString(3, student.getSpeciality());
			pstmt.executeUpdate();

			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean modifyCourseInfo(CourseB course) {
		if (null == course || course.getId() == null
				|| course.getId().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update COURSE set COURSENAME=?, PERIOD=?, TEACHER=?, POINT=?, ADDRESS=?, SHARED=? where ID='"
							+ course.getId() + "'");
			pstmt.setString(1, course.getCourseName());
			pstmt.setString(2, course.getPeriod());
			pstmt.setString(3, course.getTeacher());
			pstmt.setString(4, course.getPoint());
			pstmt.setString(5, course.getAddress());
			pstmt.setString(6, course.getShare().toString());
			pstmt.executeUpdate();

			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public List<CourseB> getAllCourses() {
		ResultSet rs = null;
		List<CourseB> courseList = new ArrayList<CourseB>();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from COURSE");
			String id = "";

			while (rs.next()) {
				id = rs.getString(1);
				CourseB cb = getCourse(id);
				courseList.add(cb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courseList;
	}

	public List<StudentB> getAllStudents() {
		ResultSet rs = null;
		List<StudentB> studentList = new ArrayList<StudentB>();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from STUDENT");
			String studentNumber = "";

			while (rs.next()) {
				studentNumber = rs.getString(1);

				StudentB student = getStudent(studentNumber);
				studentList.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return studentList;
	}

	// 将学生新选课信息存入数据库,如果是其他院课程，输出xml
	public boolean chooseCourse(StudentB student, CourseB course) {
		boolean result = false;
		if (null == student || null == course) {
			return false;
		}
		String courseId = course.getId();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from CHOOSE where COURSEID='"
							+ courseId + "' and STUDENTNUMBER='"
							+ student.getStudentNumber() + "'");
			if (rs.next()) {
				return false;
			} else {
				PreparedStatement pstmt = con
						.prepareStatement("insert into CHOOSE values(?, ? , ?)");
				pstmt.setString(1, course.getId());
				pstmt.setString(2, student.getStudentNumber());
				pstmt.setString(3, "0");

				pstmt.executeUpdate();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to choose course");
			return false;
		}
		if (!courseId.startsWith(CourseB.INNER_COURSE_ID)) {
			String choiceFileURL = null;
			String stuInfoFileURL = null;
			// 如果是外院课程，用XMLServerA生成XML并传给NetworkServerA，然后传给集成服务器
			try {
				choiceFileURL = xmlServer.getChoiceData(
						student.getStudentNumber(), course.getId());
				List<StudentB> list = new ArrayList<StudentB>();
				list.add(student);
				stuInfoFileURL = xmlServer.changeStudentToXMLFile(list);
				result = networkServer.sendCourseChoiseAndStudentInfo(courseId,
						choiceFileURL, stuInfoFileURL);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != choiceFileURL) {
					File file = new File(choiceFileURL);
					if (file.exists()) {
						file.delete();
					}
				}
				if (null != stuInfoFileURL) {
					File file = new File(stuInfoFileURL);
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		return result;
	}

	/*
	 * 当该学生是本院的学生且选择的是本院课程时，只需删除选课记录；
	 * 当是外院学生选择本院课程时，则要删除本地记录的学生记录和选课记录，同时也要像向集成服务器发送该选课记录；
	 * 当是本院学生选择外院课程时，则要删除选课记录，并向集成服务器发送该选课记录
	 */
	public boolean cancelCourseChoice(StudentB student, CourseB course) {
		if (null == student) {
			return false;
		}
		String stuID = student.getStudentNumber();
		String courseID = course.getId();
		try {
			Statement st = con.createStatement();
			st.execute("delete from CHOOSE where COURSEID='" + courseID
					+ "' and STUDENTNUMBER='" + stuID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (stuID.startsWith(StudentB.INNER_STUDENT_ID)) {
			if (!courseID.startsWith(CourseB.INNER_COURSE_ID)) {
				String fileURL = null;
				try {
					fileURL = xmlServer.getChoiceData(stuID, courseID);
					networkServer.sendVainCourseChoose(fileURL, courseID);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					if (null != fileURL) {
						File file = new File(fileURL);
						if (file.exists()) {
							file.delete();
						}
					}
				}
			}
		} else {
			String fileURL = null;
			try {
				Statement st = con.createStatement();
				st.execute("delete from ACCOUNT where STUDENT_REF='"
						+ student.getStudentNumber() + "'");
				st.execute("delete from STUDENT where STUDENTNUMBER='"
						+ student.getStudentNumber() + "'");
				fileURL = xmlServer.getChoiceData(stuID, courseID);
				networkServer.sendVainCourseChoose(fileURL, courseID);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (null != fileURL) {
					File file = new File(fileURL);
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		return true;
	}

	public boolean removeStudentFromChoiceList(StudentB student, CourseB course) {
		return cancelCourseChoice(student, course);
	}

	public boolean addStudentIntoChoiseList(StudentB student, CourseB course) {
		return chooseCourse(student, course);
	}

	public List<StudentB> getStudentsOfCourse(CourseB course) {
		List<StudentB> list = new ArrayList<StudentB>();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select STUDENTNUMBER from CHOOSE where COURSEID='"
							+ course.getId() + "'");
			Statement stuST = con.createStatement();
			ResultSet stuRS = null;
			while (rs.next()) {
				String studentID = rs.getString(1);
				stuRS = stuST
						.executeQuery("select * from STUDENT where STUDENTNUMBER='"
								+ studentID + "'");
				if (stuRS.next()) {
					String name = stuRS.getString(2);
					String sex = stuRS.getString(3);
					String speciality = stuRS.getString(4);

					StudentB stu = new StudentB(studentID, name, sex,
							speciality);
					list.add(stu);
				}
				stuRS.close();
			}
			stuST.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public String getLocalSharedCourses() {
		String fileURL = null;
		try {
			List<CourseB> list = new ArrayList<CourseB>();
			Statement st = con.createStatement();
			ResultSet crs = st
					.executeQuery("select * from COURSE where SHARED='"
							+ CourseB.SHARED + "'");
			while (crs.next()) {
				String id = crs.getString(1);
				String courseName = crs.getString(2);
				String period = crs.getString(3);
				String point = crs.getString(4);
				String teacher = crs.getString(5);
				String address = crs.getString(6);
				String share = crs.getString(7);

				CourseB course = new CourseB(id, courseName, period, point,
						teacher, address, share.charAt(0));
				list.add(course);
			}
			fileURL = xmlServer.changeCourseToXMLFile(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileURL;
	}

	public boolean getSharedCourse() {
		boolean result = true;
		List<String> clist = networkServer.askForSharedCourseInfo();
		Iterator<String> cit = clist.iterator();
		while (cit.hasNext()) {
			String fileURL = (String) cit.next();
			try {
				List<CourseB> list = xmlServer.readCourseFromXML(fileURL);
				Iterator<CourseB> it = list.iterator();
				while (it.hasNext()) {
					CourseB course = (CourseB) it.next();
					Statement st = con.createStatement();
					ResultSet trs = st
							.executeQuery("select * from COURSE where ID='"
									+ course.getId() + "'");
					if (trs.next()) {
						continue;
					}
					addCourse(course);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				File file = new File(fileURL);
				file.delete();
			}
		}
		return result;
	}

	// NetworkServerA 调用此方法将外院发来的选课XML信息存入数据库
	public boolean saveCourseChooseOther(String fileURL) {
		boolean result = false;
		try {
			List<ChoiceB> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceB> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceB choice = (ChoiceB) it.next();

				String cno = choice.getCourseId();
				String sno = choice.getStudentNumber();
				Statement st = con.createStatement();
				ResultSet trs = st
						.executeQuery("select * from CHOOSE where COURSEID='"
								+ "' AND STUDENTNUMBER='" + "'");
				if (trs.next()) {
					continue;
				}
				String grade = choice.getGrade();

				PreparedStatement ps = con
						.prepareStatement("insert into CHOOSE VALUES(?,?,?)");
				ps.setString(1, cno);
				ps.setString(2, sno);
				ps.setString(3, grade);

				result = ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			File file = new File(fileURL);
			file.delete();
		}
		return result;
	}

	public boolean deleteCourseChooseOther(String fileURL) {
		boolean result = false;
		try {
			List<ChoiceB> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceB> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceB choice = (ChoiceB) it.next();
				String cno = choice.getCourseId();
				String sno = choice.getStudentNumber();

				StudentB stu = getStudent(sno);
				CourseB course = getCourse(cno);
				
				if (null != stu && null != course) {
					Statement st = con.createStatement();
					
					st.execute("delete from CHOOSE where COURSEID='" + course.getId()
							+ "' and STUDENTNUMBER='" + stu.getStudentNumber() + "'");
					
//					st.execute("delete from ACCOUNT where STUDENT_REF='"
//							+ stu.getStudentNumber() + "'");
//					
//					st.execute("delete from STUDENT where STUDENTNUMBER='"
//							+ stu.getStudentNumber() + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			File file = new File(fileURL);
			if (file.exists()) {
				file.delete();
			}
		}
		return result;
	}

	// NetworkServerA 调用此方法将外院发来的学生XML信息存入数据库
	public boolean saveStudentOther(String fileURL) {
		boolean result = false;
		try {
			List<StudentB> list = xmlServer.readStudentFromXML(fileURL);
			Iterator<StudentB> it = list.iterator();
			while (it.hasNext()) {
				StudentB stu = (StudentB) it.next();
				if (null == getStudent(stu.getStudentNumber())) {
					this.addStudent(stu);
				}
			}

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			File file = new File(fileURL);
			file.delete();
		}
		return result;
	}

	@Override
	public void finalize() {
		this.closeDatabase();
	}

	public static void main(String[] args) {
		// ServerB server = ServerB.getInstance();
		// boolean result = server.validate("yyj", "091250197");
		// StudentB stu = server.getCurrentUser();
		// System.out.println(result + stu.getName());
		// boolean result = false;
		// StudentB stuT = new StudentB();
		// stuT.setName("xxr");
		// stuT.setSex(StudentB.FEMAL);
		// stuT.setSpeciality("软件学院");
		// stuT.setStudentNumber("091250190");
		// result = server.addStudent(stuT);
		// System.out.println(result);
		// result = server.removeStudent(stuT);
		// System.out.println(result);
		// List l = server.getAllCourse();
		// System.out.println(l.size());
		// l = server.getAllStudents();
		// System.out.println(l.size());
		// CourseB c = new CourseB();
		// c.setAddress("费祎民");
		// c.setCourseName("软件工程");
		// c.setId("123");
		// c.setPeriod("3");
		// c.setShared(CourseB.NON_SHARED);
		// c.setTeacher("杨宜杰");
		// result = server.addCourse(c);
		// System.out.println(result);
		// c.setTeacher("杨薇然");
		// StudentB stu = server.getStudent("091250190");
		// stu.setName("许晓然");
		// result = server.modifyStudentInfo(stu);
		// System.out.println(result);
	}
}
