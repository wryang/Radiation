package collegeC.controller;


import java.util.ArrayList;

import collegeC.bean.CourseC;
import collegeC.bean.StudentC;
import collegeC.server.ServerC;
import collegeC.view.AdministerViewC;
import collegeC.view.CourseInfoViewC;
import collegeC.view.CourseSelectedStuViewC;
import collegeC.view.StudentInfoViewC;

public class AdministerViewControllerC {
	private AdministerViewC view;
	private ServerC	model;
	
	public AdministerViewControllerC(){
		ServerC server = ServerC.getInstance();
		this.model = server;
	}
	
	public void setView(AdministerViewC view){
		this.view = view;
	}

	public ArrayList<StudentC> getStdList() {
		// TODO Auto-generated method stub
		ArrayList<StudentC> students = new ArrayList<StudentC>();
		students = (ArrayList<StudentC>)model.getAllStudents();
		return students;
	}
	
	public ArrayList<CourseC> getCourseList(){
		ArrayList<CourseC> courses = new ArrayList<CourseC>();
		courses = (ArrayList<CourseC>)model.getAllCourse();
		return courses;
	}

	public void requestRemoveStd() {
		// TODO Auto-generated method stub
		StudentC stdSelected = new StudentC();
		stdSelected = view.getStdSelected();
		if(model.removeStudent(stdSelected)){
			view.repaint();
		}
	}

	public void requestAddStdInfo() {
		// TODO Auto-generated method stub
		StudentInfoViewC stdView = new StudentInfoViewC(null , view);
		StudentInfoControllerC stdController = new StudentInfoControllerC(stdView , null);
	}

	public void requestEditStdInfo() {
		// TODO Auto-generated method stub
		StudentC student = new StudentC();
		student = view.getStdSelected();

		StudentInfoViewC stdView = new StudentInfoViewC(student , view);
	//	StudentInfoController stdController = new StudentInfoController(stdView , student);
	//	stdView.createController();
	}
	
	public void requestRemoveCourse() {
		// TODO Auto-generated method stub
		CourseC courseSelected = new CourseC();
		courseSelected = view.getCourseSelected();
		if(model.removeCourse(courseSelected))
			view.repaint();
	}

	public void requestAddCourse() {
		// TODO Auto-generated method stub
		CourseInfoViewC courseView = new CourseInfoViewC(null , view);
//		CourseInfoController courseController = new CourseInfoController(courseView , null);
	}

	public void requestEditCourseInfo() {
		// TODO Auto-generated method stub
		CourseC course = new CourseC();
		course = view.getCourseSelected();
		
		CourseInfoViewC courseView = new CourseInfoViewC(course , view);
		CourseInfoControllerC courseController = new CourseInfoControllerC(courseView , course);
	}

	public void requestViewCourseDetail() {
		// TODO Auto-generated method stub
		CourseC course = new CourseC();
		course = view.getCourseSelected();
		
		CourseSelectedStuViewC cssv = new CourseSelectedStuViewC(course);
		//detail
	}
	
	public void getSharedCourses() {
		// TODO Auto-generated method stub
		if(model.getSharedCourse())
			view.repaint();
	}

}

