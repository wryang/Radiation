package collegeC.server;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import collegeC.bean.CourseC;
import collegeC.bean.StudentC;
import collegeC.server.NetworkServerC;
import collegeC.server.XMLServerC;
import collegeC.bean.AccountC;
import collegeC.bean.ChoiceC;

public class ServerC {

	private static ServerC server = null;
	private static XMLServerC xmlServer = null;
	public static NetworkServerC networkServer = null;
	private static StudentC currentUser = null;
	private static Connection con = null;

	private ServerC() {
		this.connectDatabase();
		networkServer = NetworkServerC.getInstance(this);
		xmlServer = XMLServerC.getInstance();
	}

	public static ServerC getInstance() {
		if (null == server) {
			server = new ServerC();
			return server;
		} else {
			return server;
		}
	}

	private void connectDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Properties props = new Properties();
		props.put("user", "root");
		props.put("password", "091250193");
		String conURL = "jdbc:mysql://localhost/Radiation?userUnicode=true$characterEncoding=utf8";
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
		setCurrentStudent(getStudent(currentUser.getSno()));
	}

	private void setCurrentStudent(StudentC student) {
		currentUser = student;
	}

	public boolean validate(String username, String password) {
		ResultSet rs = null;
		boolean result = false;
		try {
			String sql = "select * from account where ACC='" + username
					+ "' and PASSWD='" + password + "'";
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			if (null != rs && rs.next()) {
				Integer degree = rs.getInt(4);
				if (degree.equals(AccountC.STUDENT)) {
					StudentC stu = getStudent(username);
					setCurrentStudent(stu);
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

	public StudentC getCurrentUser() {
		return currentUser;
	}

	public boolean isAdminister() {
		return currentUser == null ? true : false;
	}

	public boolean addLocalStudent(StudentC student) {
		if (null == student || student.getSno() == null
				|| student.getSno().equals("")) {
			return false;
		}
		student.setSno(StudentC.INNER_STUDENT_ID + student.getSno());
		return addStudent(student);
	}

	private boolean addStudent(StudentC student) {
		if (null == student || student.getSno() == null) {
			return false;
		}
		StudentC stu = getStudent(student.getSno());
		if (stu != null) {
			return false;
		}
		boolean result = false;
		try {
			AccountC newAccount = null;
			if (student.getAccount() != null) {
				newAccount = student.getAccount();
			} else {
				newAccount = new AccountC(student.getSno());
			}
			AccountC account = getAccount(student.getSno());
			PreparedStatement pstmt = null;
			if (null == account) {
				pstmt = con
						.prepareStatement("insert into ACCOUNT values(?, ?, ?, ?)");
				java.util.Date date = new java.util.Date();
				Timestamp tt = new Timestamp(date.getTime());
				pstmt.setString(1, newAccount.getAcc());
				pstmt.setString(2, newAccount.getPasswd());
				pstmt.setTimestamp(3, tt);
				pstmt.setInt(4, newAccount.getDegree());
			} else {
				pstmt = con
						.prepareStatement("update ACCOUNT set PASSWD=?, CREATEDATE=?, DEGREE=? where ACC='"
								+ account.getAcc() + "'");
				java.util.Date date = new java.util.Date();
				Timestamp tt = new Timestamp(date.getTime());
				pstmt.setString(1, newAccount.getPasswd());
				pstmt.setTimestamp(2, tt);
				pstmt.setInt(3, newAccount.getDegree());
			}

			pstmt.execute();
			student.setAccount(newAccount);

			PreparedStatement pstmtS = con
					.prepareStatement("insert into STUDENT values(?, ?, ?, ?, ?)");

			pstmtS.setString(1, student.getSno());
			pstmtS.setString(2, student.getSnm());
			pstmtS.setString(3, student.getSex());
			pstmtS.setString(4, student.getSde());
			pstmtS.setString(5, student.getAccount().getAcc());

			pstmtS.execute();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean removeStudent(StudentC student) {
		if (null == student || student.getSno() == null
				|| student.getSno().equals("") || student.getAccount() == null) {
			return false;
		}
		boolean result = false;
		try {
			if (student.hasSelectedCourse()) {
				List<CourseC> list = student.getSelectedCourse();
				Iterator<CourseC> it = list.iterator();
				while (it.hasNext()) {
					CourseC course = (CourseC) it.next();
					cancelCourseChoice(student, course);
				}
			}
			Statement st = con.createStatement();
			st.execute("delete from STUDENT where SNO='" + student.getSno()
					+ "'");
			st.execute("delete from ACCOUNT where ACC='"
					+ student.getAccount().getAcc() + "'");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean addLocalCourse(CourseC course) {
		if (null == course) {
			return false;
		}
		course.setCno(CourseC.INNER_COURSE_ID + course.getCno());
		return addCourse(course);
	}

	private boolean addCourse(CourseC course) {
		if (null == course) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("insert into COURSE values(?, ?, ?, ?, ?, ?, ?)");

			pstmt.setString(1, course.getCno());
			pstmt.setString(2, course.getCnm());
			pstmt.setInt(3, course.getCtm());
			pstmt.setInt(4, course.getCpt());
			pstmt.setString(5, course.getTec());
			pstmt.setString(6, course.getPla());
			pstmt.setBoolean(7, course.getShare());

			pstmt.executeUpdate();
			pstmt.close();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean removeCourse(CourseC course) {
		if (null == course || course.getCno() == null
				|| course.getCno().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from SELECTION where CNO='"
							+ course.getCno() + "'");
			if (rs.next()) {
				String studentNumber = rs.getString(2);
				StudentC student = getStudent(studentNumber);
				cancelCourseChoice(student, course);
			}
			st.execute("delete from COURSE where CNO='" + course.getCno() + "'");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public StudentC getStudent(String studentId) {
		if (null == studentId) {
			return null;
		}
		StudentC stu = null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from STUDENT where SNO='"
					+ studentId + "'");
			if (rs.next()) {
				String sno = rs.getString(1);
				String name = rs.getString(2);
				String sex = rs.getString(3);
				String speciality = rs.getString(4);

				stu = new StudentC(sno, name, sex, speciality);

				rs = st.executeQuery("select * from SELECTION where SNO='"
						+ stu.getSno() + "'");
				List<CourseC> list = new ArrayList<CourseC>();
				while (rs.next()) {
					String classId = rs.getString(1);
					CourseC course = getCourse(classId);
					list.add(course);
				}
				if (list.size() != 0) {
					stu.setSelectedCourse(list);
				}
				stu.setAcc(getAccount(sno));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stu;
	}

	public CourseC getCourse(String classId) {
		if (null == classId) {
			return null;
		}
		CourseC cc = null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from COURSE where cno='"
					+ classId + "'");
			if (rs.next()) {
				String cno = rs.getString(1);
				String cnm = rs.getString(2);
				int ctm = rs.getInt(3);
				int cpt = rs.getInt(4);
				String tec = rs.getString(5);
				String pla = rs.getString(6);
				boolean share = rs.getBoolean(7);

				cc = new CourseC(cno, cnm, ctm, cpt, tec, pla, share);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cc;
	}

	public boolean modifyStudentInfo(StudentC student) {
		if (null == student || student.getSno() == null
				|| student.getSno().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update STUDENT set SNM=?, SEX=?, DEPARTMENT=? where SNO='"
							+ student.getSno() + "'");
			pstmt.setString(1, student.getSnm());
			pstmt.setString(2, student.getSex());
			pstmt.setString(3, student.getSde());
			pstmt.executeUpdate();

			if (null != student.getAccount()) {
				AccountC ac = student.getAccount();
				pstmt = con
						.prepareStatement("update ACCOUNT set PASSWD=?, CREATEDATE=?, DEGREE=? where ACC='"
								+ ac.getAcc() + "'");

				java.util.Date date = new java.util.Date();
				Timestamp tt = new Timestamp(date.getTime());
				pstmt.setString(1, ac.getPasswd());
				pstmt.setTimestamp(2, tt);
				pstmt.setInt(3, ac.getDegree());
				pstmt.executeUpdate();
			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean modifyCourseInfo(CourseC course) {
		if (null == course || course.getCno() == null
				|| course.getCno().equals("")) {
			return false;
		}
		boolean result = false;
		try {
			PreparedStatement pstmt = con
					.prepareStatement("update COURSE set CNM=?, CTM=?, CPT=?, TEC=?, PLA=?, SHARE=? where CNO='"
							+ course.getCno() + "'");
			pstmt.setString(1, course.getCnm());
			pstmt.setInt(2, course.getCtm());
			pstmt.setInt(3, course.getCpt());
			pstmt.setString(4, course.getTec());
			pstmt.setString(5, course.getPla());
			pstmt.setBoolean(6, course.getShare());
			pstmt.executeUpdate();

			pstmt.close();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public List<CourseC> getAllCourse() {
		ResultSet rs = null;
		List<CourseC> courseList = new ArrayList<CourseC>();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from course");
			String cno = "";

			while (rs.next()) {
				cno = rs.getString(1);

				CourseC c = getCourse(cno);
				courseList.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courseList;
	}

	public List<StudentC> getAllStudents() {
		ResultSet rs = null;
		List<StudentC> studentList = new ArrayList<StudentC>();

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery("select * from student");
			String studentNumber = "";

			while (rs.next()) {
				studentNumber = rs.getString(1);

				StudentC student = getStudent(studentNumber);
				studentList.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return studentList;
	}

	// 将学生新选课信息存入数据库,如果是其他院课程，输出xml
	public boolean chooseCourse(StudentC student, CourseC course) {
		boolean result = false;
		if (null == student || null == course) {
			return false;
		}
		String courseId = course.getCno();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from SELECTION where CNO='"
							+ courseId + "' and SNO='" + student.getSno() + "'");
			if (rs.next()) {
				return false;
			} else {
				PreparedStatement pstmt = con
						.prepareStatement("insert into SELECTION values(?, ? , ?)");
				pstmt.setString(1, course.getCno());
				pstmt.setString(2, student.getSno());
				pstmt.setInt(3, 0);

				pstmt.executeUpdate();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to choose course");
			return false;
		}
		if (!courseId.startsWith(CourseC.INNER_COURSE_ID)) {
			String choiceFileURL = null;
			String stuInfoFileURL = null;
			// 如果是外院课程，用XMLServerA生成XML并传给NetworkServerA，然后传给集成服务器
			try {
				choiceFileURL = xmlServer.getChoiceData(student.getSno(),
						course.getCno());
				List<StudentC> list = new ArrayList<StudentC>();
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
	public boolean cancelCourseChoice(StudentC student, CourseC course) {
		if (null == student) {
			return false;
		}
		String stuID = student.getSno();
		String courseID = course.getCno();
		try {
			Statement st = con.createStatement();
			st.execute("delete from SELECTION where CNO='" + courseID
					+ "' and SNO='" + stuID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (stuID.startsWith(StudentC.INNER_STUDENT_ID)) {
			if (!courseID.startsWith(CourseC.INNER_COURSE_ID)) {
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
				st.execute("delete from STUDENT where SNO='" + student.getSno()
						+ "'");
				st.execute("delete from ACCOUNT where ACC='"
						+ student.getAccount().getAcc() + "'");
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

	/*
	 * 用户点击更新课程列表时调用
	 */
	public boolean removeStudentFromChoiceList(StudentC student, CourseC course) {
		return cancelCourseChoice(student, course);
	}

	public boolean addStudentIntoChoiseList(StudentC student, CourseC course) {
		return chooseCourse(student, course);
	}

	public List<StudentC> getStudentsOfCourse(CourseC course) {
		List<StudentC> list = new ArrayList<StudentC>();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select SNO from SELECTION where CNO='"
							+ course.getCno() + "'");
			Statement stuST = con.createStatement();
			while (rs.next()) {
				String studentID = rs.getString(1);
				StudentC stu = getStudent(studentID);
				list.add(stu);
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
			List<CourseC> list = new ArrayList<CourseC>();
			PreparedStatement pstmt = con
					.prepareStatement("select * from COURSE where SHARE=?");
			pstmt.setBoolean(1, CourseC.SHARED);
			ResultSet crs = pstmt.executeQuery();
			while (crs.next()) {
				String id = crs.getString(1);
				String courseName = crs.getString(2);
				String periodStr = crs.getString(3);
				String pointStr = crs.getString(4);
				String teacher = crs.getString(5);
				String address = crs.getString(6);
				String share = crs.getString(7);

				CourseC course = new CourseC(id, courseName, new Integer(
						periodStr), new Integer(pointStr), teacher, address,
						new Boolean(share));
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
				List<CourseC> list = xmlServer.readCourseFromXML(fileURL);
				Iterator<CourseC> it = list.iterator();
				Statement st = con.createStatement();
				while (it.hasNext()) {
					CourseC course = (CourseC) it.next();
					ResultSet trs = st
							.executeQuery("select * from COURSE where CNO='"
									+ course.getCno() + "'");
					if (trs.next()) {
						continue;
					}
					if (addCourse(course)) {
						result = false;
					}
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
			List<ChoiceC> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceC> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceC choice = (ChoiceC) it.next();
				String cno = choice.getCno();
				String sno = choice.getSno();
				String grade = choice.getGrd();
				int grd = 0;
				if (null != grade && !grade.equals("")) {
					grd = Integer.parseInt(grade);
				}
				PreparedStatement ps = con
						.prepareStatement("insert into SELECTION VALUES(?,?,?)");
				ps.setString(1, cno);
				ps.setString(2, sno);
				ps.setInt(3, grd);

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

	private AccountC getAccount(String username) {
		AccountC account = null;
		String sql = "select * from account where ACC='" + username + "'";
		ResultSet rs;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			if (null != rs && rs.next()) {
				String password = rs.getString(2);
				Date createDate = rs.getTimestamp(3);
				int degree = rs.getInt(4);
				account = new AccountC();
				account.setAcc(username);
				account.setPasswd(password);
				account.setCreateDate(createDate);
				account.setDegree(degree);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public boolean deleteCourseChooseOther(String fileURL) {
		boolean result = false;
		try {
			List<ChoiceC> choiceList = xmlServer
					.readCourseChoiceFromXML(fileURL);
			Iterator<ChoiceC> it = choiceList.iterator();
			while (it.hasNext()) {
				ChoiceC choice = (ChoiceC) it.next();
				String cno = choice.getCno();
				String sno = choice.getSno();

				StudentC stu = this.getStudent(sno);
				CourseC course = this.getCourse(cno);
				if (null != stu && null != course) {
					Statement st = con.createStatement();
					
					st.execute("delete from SELECTION where CNO='" + course.getCno()
							+ "' and SNO='" + stu.getSno() + "'");
					
//					st.execute("delete from STUDENT where SNO='" + stu.getSno()
//							+ "'");
//					
//					st.execute("delete from ACCOUNT where ACC='"
//							+ stu.getAccount().getAcc() + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			File file = new File(fileURL);
			file.delete();
		}
		return result;
	}

	// NetworkServerA 调用此方法将外院发来的学生XML信息存入数据库
	public boolean saveStudentOther(String fileURL) {
		boolean result = false;
		try {
			List<StudentC> list = xmlServer.readStudentFromXML(fileURL);
			Iterator<StudentC> it = list.iterator();
			while (it.hasNext()) {
				StudentC stu = (StudentC) it.next();
				this.addStudent(stu);
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

	// public static void main(String[] args) {
	// ServerC server = ServerC.getInstance();
	// boolean result = server.validate("yyj", "091250197");
	// StudentC stu = server.getCurrentUser();
	// System.out.println(result + stu.getSde());
	// AccountC ac = new AccountC();
	// ac.setAcc("ywr");
	// ac.setCreateDate(Calendar.getInstance().getTime());
	// ac.setDegree(AccountC.ADMINISTER);
	// ac.setPasswd("091250193");
	// StudentC ts = new StudentC("091250193", "杨薇然", StudentC.FEMALE, "软件学院");
	// ts.setAccount(ac);
	// server.addStudent(ts);
	// }
}
