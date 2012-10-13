package collegeB.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import collegeB.bean.CourseB;
import collegeB.bean.StudentB;
import collegeB.server.ServerB;
import collegeB.view.AdministerViewB;
import collegeB.view.CourseInfoViewB;
import collegeB.view.CourseSelectedStuViewB;
import collegeB.view.StudentInfoViewB;

public class AdministerViewControllerB {
	private AdministerViewB view;
	private ServerB	model;
	
	public AdministerViewControllerB(){
		ServerB server = ServerB.getInstance();
		this.model = server;
	}
	
	public void setView(AdministerViewB view){
		this.view = view;
	}

	public ArrayList<StudentB> getStdList() {
		// TODO Auto-generated method stub
		ArrayList<StudentB> students = new ArrayList<StudentB>();
		students = (ArrayList<StudentB>)model.getAllStudents();
		return students;
	}
	
	public ArrayList<CourseB> getCourseList(){
		ArrayList<CourseB> courses = new ArrayList<CourseB>();
		courses = (ArrayList<CourseB>)model.getAllCourses();
		return courses;
	}

	public void requestRemoveStd() {
		// TODO Auto-generated method stub
		StudentB stdSelected = new StudentB();
		stdSelected = view.getStdSelected();
		if(model.removeStudent(stdSelected)){
			view.repaint();
		}
	}

	public void requestAddStdInfo() {
		// TODO Auto-generated method stub
		StudentInfoViewB stdView = new StudentInfoViewB(null , view);
//		StudentInfoController stdController = new StudentInfoController(stdView , null);
	}

	public void requestEditStdInfo() {
		// TODO Auto-generated method stub
		StudentB student = new StudentB();
		student = view.getStdSelected();

		StudentInfoViewB stdView = new StudentInfoViewB(student , view);
	//	StudentInfoController stdController = new StudentInfoController(stdView , student);
	//	stdView.createController();
	}
	
	public void requestRemoveCourse() {
		// TODO Auto-generated method stub
		CourseB courseSelected = new CourseB();
		courseSelected = view.getCourseSelected();
		if(model.removeCourse(courseSelected))
			view.repaint();
	}

	public void requestAddCourse() {
		// TODO Auto-generated method stub
		CourseInfoViewB courseView = new CourseInfoViewB(null , view);
//		CourseInfoController courseController = new CourseInfoController(courseView , null);
	}

	public void requestEditCourseInfo() {
		// TODO Auto-generated method stub
		CourseB course = new CourseB();
		course = view.getCourseSelected();
		
		CourseInfoViewB courseView = new CourseInfoViewB(course , view);
//		CourseInfoController courseController = new CourseInfoController(courseView , course);
	}

	public void requestViewCourseDetail() {
		// TODO Auto-generated method stub
		CourseB course = new CourseB();
		course = view.getCourseSelected();
		
		CourseSelectedStuViewB cssv = new CourseSelectedStuViewB(course);
		//detail
	}

	public void getSharedCourses() {
		// TODO Auto-generated method stub
		if(model.getSharedCourse())
			view.repaint();
	}
}
