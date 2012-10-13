package collegeA.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import collegeA.bean.CourseA;
import collegeA.bean.StudentA;
import collegeA.server.ServerA;
import collegeA.view.AdministerViewA;
import collegeA.view.CourseInfoViewA;
import collegeA.view.CourseSelectedStuViewA;
import collegeA.view.StudentInfoViewA;

public class AdministerViewControllerA {
	private AdministerViewA view;
	private ServerA	model;
	
	public AdministerViewControllerA(){
		ServerA server = ServerA.getInstance();
		this.model = server;
	}
	
	public void setView(AdministerViewA view){
		this.view = view;
	}

	public ArrayList<StudentA> getStdList() throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<StudentA> students = new ArrayList<StudentA>();
		students = (ArrayList<StudentA>)model.getAllStudents();
		return students;
	}
	
	public ArrayList<CourseA> getCourseList(){
		ArrayList<CourseA> courses = new ArrayList<CourseA>();
		courses = (ArrayList<CourseA>)model.getAllCourses();
		return courses;
	}

	public void requestRemoveStd() throws SQLException {
		// TODO Auto-generated method stub
		StudentA stdSelected = new StudentA();
		stdSelected = view.getStdSelected();
		if(model.removeStudent(stdSelected)){
			view.repaint();
		}
	}

	public void requestAddStdInfo() {
		// TODO Auto-generated method stub
		StudentInfoViewA stdView = new StudentInfoViewA(null , view);
//		StudentInfoController stdController = new StudentInfoController(stdView , null);
	}

	public void requestEditStdInfo() {
		// TODO Auto-generated method stub
		StudentA student = new StudentA();
		student = view.getStdSelected();
		System.out.println("admins 59 line"+student.getStudentId());

		StudentInfoViewA stdView = new StudentInfoViewA(student , view);
	//	StudentInfoController stdController = new StudentInfoController(stdView , student);
	//	stdView.createController();
	}
	
	public void requestRemoveCourse() throws SQLException {
		// TODO Auto-generated method stub
		CourseA courseSelected = new CourseA();
		courseSelected = view.getCourseSelected();
		if(model.removeCourse(courseSelected.getClassId()))
			view.repaint();
	}

	public void requestAddCourse() {
		// TODO Auto-generated method stub
		CourseInfoViewA courseView = new CourseInfoViewA(null , view);
		
//		CourseInfoController courseController = new CourseInfoController(courseView , null);
	}

	public void requestEditCourseInfo() {
		// TODO Auto-generated method stub
		CourseA course = new CourseA();
		course = view.getCourseSelected();
		
		CourseInfoViewA courseView = new CourseInfoViewA(course , view);
//		CourseInfoController courseController = new CourseInfoController(courseView , course);
	}

	public void requestViewCourseDetail() {
		// TODO Auto-generated method stub
		CourseA course = new CourseA();
		course = view.getCourseSelected();
		
		CourseSelectedStuViewA cssv = new CourseSelectedStuViewA(course);
		//detail
	}

	public void getSharedCourses() {
		// TODO Auto-generated method stub
		if(model.getSharedCourse())
			try {
				view.repaint();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
