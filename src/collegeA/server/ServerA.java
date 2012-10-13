package collegeA.server;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import collegeA.bean.AccountA;
import collegeA.bean.ChoiceA;
import collegeA.bean.CourseA;
import collegeA.bean.StudentA;

/*
 * 处理本院服务器数据库信息，包括本院服务器与本院客户端的交互
 */
public class ServerA {

	private static ServerA server = null;
	private static XMLServerA xmlServer = null;
	private static NetworkServerA networkServer = null;
	private static StudentA currentStudent = null;
	private static Connection con = null;

	private ServerA() {
		this.connectDatabase();
		networkServer = NetworkServerA.getInstance(this);
		xmlServer = XMLServerA.getInstance();
	}

	public static ServerA getInstance() {
		if (null == server) {
			server = new ServerA();
			return server;
		} else {
			return server;
		}
	}

	private void connectDatabase() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String conURL = "jdbc:sqlserver://127.0.0.1:1434;DatabaseName=Radiation";
		Properties props = new Properties();
		props.put("user", "sa");
		props.put("password", "091250193");
		try {
			con = DriverManager.getConnection(conURL, props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement tmpSt = con.createStatement();
			ResultSet rsStudent = tmpSt
					.executeQuery("select * from user_table");
			while (rsStudent.next()) {
				System.out.println(rsStudent.getObject(1) + "");
			}
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
		setCurrentStudent(getStudent(currentStudent.getStudentId()));
	}

	private static void setCurrentStudent(StudentA student) {
		currentStudent = student;
	}

	// 验证用户，并记录登录用户信息
	public boolean validate(String username, String password) {
		if (null == username || null == password) {
			return false;
		}
		ResultSet rs = null;
		boolean result = false;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from user_table where USERNAME='"
					+ username + "' AND PASSWRD='" + password + "'");
			if (rs.next()) {
				Character degree = rs.getString("priority").charAt(0);
				if (degree.equals(AccountA.STUDENT)) {
					rs = st.executeQuery("select * from STUDENT where RELATIVEUSER='"
							+ username + "'");
					if (rs.next()) {
						String sid = rs.getString(1);
						StudentA sA = getStudent(sid);
						setCurrentStudent(sA);
					}
				}
				result = true;
			} else {
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isAdminister() {
		return currentStudent == null ? true : false;
	}

	public StudentA getCurrentStudent() {
		return currentStudent;
	}

	public boolean addLocalStudent(StudentA student) {
		if (null == student || student.getStudentId() == null
				|| student.getStudentId().equals("")) {
			return false;
		}
		student.setStudentId(StudentA.INNER_STUDENT_ID + student.getStudentId());
		return addStudent(student);
	}

	// 添加学生信息，由管理员调用
	private boolean addStudent(StudentA student) {
		if (null == student || student.getStudentId() == null
				|| student.getStudentId().equals("")) {
			return false;
		}
		try {
			AccountA c = new AccountA(student.getStudentId());
			PreparedStatement pstmt = con
					.prepareStatement("insert into USER_TABLE values(?, ?, ?)");
			pstmt.setString(1, c.getUserName());
			pstmt.setString(2, c.getPassword());
			pstmt.setString(3, c.getPriority().toString());

			pstmt.execute();

			PreparedStatement ps = con
					.prepareStatement("insert into STUDENT values(?, ?, ?, ?, ?)");
			ps.setString(1, student.getStudentId());
			ps.setString(2, student.getStudentName());
			ps.setString(3, student.getGender());
			ps.setString(4, student.getInstitution());
			ps.setString(5, c.getUserName());

			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to add student");
			return false;
		}
		return true;
	}

	// 删除学生信息，由管理员调用
	public boolean removeStudent(StudentA student) {
		if (null == student || student.getStudentId() == null
				|| student.getStudentId().equals("")) {
			return false;
		}
		try {
			if (student.hasSelectedCourse()) {
				List<CourseA> list = student.getSelectedCourse();
				Iterator<CourseA> it = list.iterator();
				while (it.hasNext()) {
					CourseA course = (CourseA) it.next();
					cancelCourseChoice(student, course);
				}
			}
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select RELATIVEUSER from STUDENT where STUDENTID='"
							+ student.getStudentId() + "'");
			String username = null;
			if (rs.next()) {
				username = rs.getString(1);
			}
			st.executeUpdate("delete from student where studentID='"
					+ student.getStudentId() + "'");
			if (null != username) {
				st.execute("delete from USER_TABLE where USERNAME='" + username
						+ "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to remove student");
			return false;
		}
		return true;
	}

	public boolean addLocalCourse(CourseA course) {
		if (null == course || course.getClassId() == null
				|| course.getClassId().equals("")) {
			return false;
		}
		course.setClassId(CourseA.INNER_COURSE_ID + course.getClassId());
		return addCourse(course);
	}

	// 添加课程信息，由管理员调用
	private boolean addCourse(CourseA course) {
		if (null == course || course.getClassId() == null
				|| course.getClassId().equals("")) {
			return false;
		}
		try {
			CourseA tmp = this.getCourse(course.getClassId());
			if (null == tmp) {
				PreparedStatement ps = con
						.prepareStatement("insert into CLASS values(?, ?, ?, ?, ?, ?)");
				ps.setString(1, course.getClassId());
				ps.setString(2, course.getClassName());
				ps.setString(3, course.getGrade());
				ps.setString(4, course.getTeacher());
				ps.setString(5, course.getLocation());
				ps.setString(6, course.getShare().toString());

				ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to add COURSE");
			return false;
		}
		return true;
	}

	// 删除课程信息，由管理员调用
	public boolean removeCourse(String courseId) {
		if (null == courseId || courseId.equals("")) {
			return false;
		}
		try {
			Statement st = con.createStatement();
			st.execute("delete from CLASS_CHOOSE where CLASSID='" + courseId
					+ "'");
			st.executeUpdate("delete from class where classID='" + courseId
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to remove the course");
		}
		return true;
	}

	// 删除课程信息，由管理员调用
	public boolean removeCourse(CourseA course) {
		if (null == course || course.getClassId() == null
				|| course.getClassId().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from CLASS_CHOOSE where CLASSID='"
							+ course.getClassId() + "'");
			if (rs.next()) {
				String studentNumber = rs.getString(2);
				StudentA student = getStudent(studentNumber);
				cancelCourseChoice(student, course);
			}
			st.executeUpdate("delete from class where classID='"
					+ course.getClassId() + "'");
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to remove the course");
			result = false;
		}
		return result;
	}

	// 传入学生编号，获取学生信息
	public StudentA getStudent(String studentId) {
		if (null == studentId || studentId.equals("")) {
			return null;
		}
		StudentA stu = null;
		try {
			Statement tmpSt = con.createStatement();
			ResultSet rsStudent = tmpSt
					.executeQuery("select * from student where studentId ="
							+ studentId);
			if (rsStudent.next()) {
				stu = new StudentA();
				stu.setStudentId(studentId);
				stu.setStudentName(rsStudent.getString(2));
				stu.setGender(rsStudent.getString(3));
				stu.setInstitution(rsStudent.getString(4));
				stu.setRelativeUser(getAccountOfStudent(stu));
				stu.setSelectedCourse(getSelectedCourse(stu));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stu;
	}

	public List<CourseA> getSelectedCourse(StudentA student) {
		List<CourseA> list = new ArrayList<CourseA>();
		CourseA course = null;
		ResultSet rs;
		try {
			Statement tmpSt = con.createStatement();
			rs = tmpSt
					.executeQuery("select CLASSID from CLASS_CHOOSE where STUDENTID='"
							+ student.getStudentId() + "'");
			while (rs.next()) {
				String courseId = rs.getString(1);

				course = getCourse(courseId);
				list.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public AccountA getAccountOfStudent(StudentA student) {
		AccountA account = null;
		ResultSet rs;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select RELATIVEUSER from STUDENT where STUDENTID='"
					+ student.getStudentId() + "'");
			if (rs.next()) {
				String username = rs.getString(1);

				Statement tmpst = con.createStatement();
				ResultSet tmp = tmpst
						.executeQuery("select * from USER_TABLE where USERNAME='"
								+ username + "'");
				if (tmp.next()) {
					String password = tmp.getString(2);
					String priority = tmp.getString(3);
					account = new AccountA();
					account.setUserName(username);
					account.setPassword(password);
					account.setPriority(priority.charAt(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	// 传入课程编号，获取课程具体信息
	public CourseA getCourse(String classId) {
		if (null == classId || classId.equals("")) {
			return null;
		}
		CourseA ca = null;
		try {
			Statement st = con.createStatement();
			ResultSet rsClass = st
					.executeQuery("select * from class where classID='"
							+ classId + "'");

			if (rsClass.next()) {
				ca = new CourseA();
				ca.setClassId(classId);
				ca.setClassName(rsClass.getString(2));
				ca.setGrade(rsClass.getString(3));
				ca.setTeacher(rsClass.getString(4));
				ca.setLocation(rsClass.getString(5));
				ca.setShare(rsClass.getString(6).charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ca;
	}

	// 修改学生信息，由管理员调用
	public boolean modifyStudentInfo(StudentA student) {
		if (null == student || student.getStudentId() == null
				|| student.getStudentId().equals("")) {
			return false;
		}
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update STUDENT set STUDENTNAME=?, GENDER=?, INSTITUTION=? where STUDENTID='"
							+ student.getStudentId() + "'");
			pstmt.setString(1, student.getStudentName());
			pstmt.setString(2, student.getGender());
			pstmt.setString(3, student.getInstitution());
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to modify student");
			return false;
		}
		return true;
	}

	// 修改课程信息，由管理员调用
	public boolean modifyCourseInfo(CourseA course) {
		if (null == course || course.getClassId() == null
				|| course.getClassId().equals("")) {
			return false;
		}
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update CLASS set CLASSNAME=?, GRADE=?, TEACHER=?, LOCATION=?, SHARE=? where CLASSID='"
							+ course.getClassId() + "'");
			pstmt.setString(1, course.getClassName());
			pstmt.setString(2, course.getGrade());
			pstmt.setString(3, course.getTeacher());
			pstmt.setString(4, course.getLocation());
			pstmt.setString(5, course.getShare().toString());

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to modify course info");
			return false;
		}
		return true;
	}

	// 从本地数据库获取本院所有课程，包括它院共享课程信息
	public List<CourseA> getAllCourses() {
		List<CourseA> list = new ArrayList<CourseA>();
		try {
			Statement st = con.createStatement();
			ResultSet rsClass = st.executeQuery("select * from class");
			while (rsClass.next()) {
				CourseA ca = new CourseA();
				ca.setClassId(rsClass.getString(1));
				ca.setClassName(rsClass.getString(2));
				ca.setGrade(rsClass.getString(3));
				ca.setTeacher(rsClass.getString(4));
				ca.setLocation(rsClass.getString(5));
				ca.setShare(rsClass.getString(6).charAt(0));
				list.add(ca);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 从本地数据库获取所有学生信息
	public List<StudentA> getAllStudents() throws SQLException {
		List<StudentA> lsA = new ArrayList<StudentA>();
		ResultSet rsStudent = null;
		try {
			Statement tmpSt = con.createStatement();
			rsStudent = tmpSt.executeQuery("select * from student");
			while (rsStudent.next()) {
				StudentA st = getStudent(rsStudent.getString(1));
				lsA.add(st);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lsA;
	}

	public boolean chooseCourse(StudentA student, CourseA course) {
		boolean result = false;
		if (null == student || null == course) {
			return false;
		}
		String courseId = course.getClassId();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("insert into class_choose values(?, ? , ?)");
			pstmt.setString(1, course.getClassId());
			pstmt.setString(2, student.getStudentId());
			pstmt.setString(3, "0");

			pstmt.executeUpdate();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to choose course");
			return false;
		}
		if (!courseId.startsWith(CourseA.INNER_COURSE_ID)) {
			String stuInfoFileURL = null;
			String choiseFileURL = null;
			// 如果是外院课程，用XMLServerA生成XML并传给NetworkServerA，然后传给集成服务器
			try {
				Statement st = con.createStatement();
				choiseFileURL = xmlServer.getChoiceData(student.getStudentId(),
						course.getClassId());
				ResultSet rsStudent = st
						.executeQuery("select * from STUDENT where studentID='"
								+ student.getStudentId() + "'");
				stuInfoFileURL = xmlServer.getStudentData(rsStudent);
				result = networkServer.sendCourseChoiseAndStudentInfo(courseId,
						choiseFileURL, stuInfoFileURL);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != stuInfoFileURL) {
					File file = new File(stuInfoFileURL);
					if (file.exists()) {
						file.delete();
					}
				}
				if (null != choiseFileURL) {
					File file = new File(choiseFileURL);
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
	public boolean cancelCourseChoice(StudentA student, CourseA course) {
		if (null == student) {
			return false;
		}
		if (null == student.getSelectedCourse()) {
			student.setSelectedCourse(getSelectedCourse(student));
		}
		String stuID = student.getStudentId();
		String courseID = course.getClassId();
		try {
			Statement st = con.createStatement();
			st.execute("delete from CLASS_CHOOSE where CLASSID='" + courseID
					+ "' and STUDENTID='" + stuID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (stuID.startsWith(StudentA.INNER_STUDENT_ID)) {
			if (!courseID.startsWith(CourseA.INNER_COURSE_ID)) {
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
				st.execute("delete from STUDENT where STUDENTID='" + stuID
						+ "'");
				st.execute("delete from USER_TABLE where USERNAME='" + stuID
						+ "'");
				fileURL = xmlServer.getChoiceData(stuID, courseID);
				networkServer.sendVainCourseChoose(fileURL, courseID);
			} catch (SQLException e) {
				e.printStackTrace();
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

	/*
	 * 用户点击更新课程列表时调用
	 */
	public boolean removeStudentFromChoiseList(StudentA student, CourseA course) {
		return cancelCourseChoice(student, course);
	}

	public boolean addStudentIntoChoiseList(StudentA student, CourseA course) {
		return chooseCourse(student, course);
	}

	public List<StudentA> getStudentsOfCourse(CourseA course) {
		List<StudentA> list = new ArrayList<StudentA>();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select studentID from CLASS_CHOOSE where CLASSID='"
							+ course.getClassId() + "'");
			while (rs.next()) {
				String studentID = rs.getString(1);

				StudentA stu = this.getStudent(studentID);
				list.add(stu);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public String getLocalSharedCourses() {
		String fileURL = null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from CLASS where SHARE='"
					+ CourseA.SHARED + "'");
			fileURL = xmlServer.getCourseData(rs);
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
				List<CourseA> list = xmlServer.readCourseFromXML(fileURL);
				if (null != list) {
					Iterator<CourseA> it = list.iterator();
					while (it.hasNext()) {
						CourseA course = (CourseA) it.next();
						CourseA dbCourse = getCourse(course.getClassId());

						if (dbCourse == null) {
							addCourse(course);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				File file = new File(fileURL);
				if (file.exists()) {
					file.delete();
				}
			}
		}
		return result;
	}

	// NetworkServerA 调用此方法将外院发来的选课XML信息存入数据库
	public boolean saveCourseChooseOther(String fileURL) {
		boolean result = false;
		try {
			List<ChoiceA> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceA> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceA choice = (ChoiceA) it.next();

				String cno = choice.getCourseId();
				String sno = choice.getStudentNumber();
				ChoiceA dbChoice = getChoice(sno, cno);
				if (dbChoice != null) {
					continue;
				}
				String grade = choice.getGrade();

				PreparedStatement ps = con
						.prepareStatement("insert into CLASS_CHOOSE VALUES(?,?,?)");
				ps.setString(1, cno);
				ps.setString(2, sno);
				ps.setString(3, grade);

				result = ps.execute();
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

	public ChoiceA getChoice(String studentId, String courseId) {
		ChoiceA choice = null;
		Statement st;
		try {
			st = con.createStatement();
			ResultSet trs = st
					.executeQuery("select * from CLASS_CHOOSE where STUDENTID='"
							+ studentId + "' AND CLASSID='" + courseId + "'");
			if (trs.next()) {
				choice = new ChoiceA();
				choice.setCourseId(trs.getString(1));
				choice.setStudentNumber(trs.getString(2));
				choice.setGrade(trs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return choice;
	}

	public boolean deleteCourseChooseOther(String fileURL) {
		boolean result = false;
		try {
			List<ChoiceA> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceA> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceA choice = (ChoiceA) it.next();
				String cno = choice.getCourseId();
				String sno = choice.getStudentNumber();

				StudentA stu = getStudent(sno);
				CourseA course = getCourse(cno);
				if (null != stu && null != course) {
					Statement st = con.createStatement();
					st.execute("delete from CLASS_CHOOSE where CLASSID='" + course.getClassId()
							+ "' and STUDENTID='" + stu.getStudentId() + "'");
					
//					st.executeUpdate("delete from student where studentID='"
//							+ stu.getStudentId() + "'");
//					
//					ResultSet rs = st
//							.executeQuery("select RELATIVEUSER from STUDENT where STUDENTID='"
//									+ stu.getStudentId() + "'");
//					String username = null;
//					if (rs.next()) {
//						username = rs.getString(1);
//					}
//					if (null != username) {
//						st.execute("delete from USER_TABLE where USERNAME='" + username
//								+ "'");
//					}
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
			List<StudentA> list = xmlServer.readStudentFromXML(fileURL);
			Iterator<StudentA> it = list.iterator();
			while (it.hasNext()) {
				StudentA stu = (StudentA) it.next();
				if (null == getStudent(stu.getStudentId())) {
					addStudent(stu);
				}
			}

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			File file = new File(fileURL);
			if (file.exists()) {
				file.delete();
			}
		}
		return result;
	}

	// public static void main(String[] args) throws Exception {
	// ServerA server = ServerA.getInstance();
	// CourseA course = new CourseA();
	// course.setClassId("001");
	// List l = server.getStudentsOfCourse(course);
	// StudentA stu = (StudentA) l.get(0);
	// System.out.println(stu.getStudentName());
	// try {
	// XMLServerA xmlsA = XMLServerA.getInstance();
	// ResultSet rsStudent = st.executeQuery("select * from student");
	// xmlsA.getStudentData(rsStudent);
	// ResultSet rsClass = st.executeQuery("select * from class");
	// xmlsA.getCourseData(rsClass);
	// xmlsA.getChoiceData("123", "324");
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void finalize() {
		this.closeDatabase();
	}
}
