package collegeC.controller;

import collegeC.bean.CourseC;
import collegeC.server.ServerC;
import collegeC.view.CourseSelectedStuViewC;

public class CourseSelectedStuControllerC {
	CourseSelectedStuViewC view;
	ServerC model;
	CourseC currentCourse;
	
	public CourseSelectedStuControllerC(
			CourseSelectedStuViewC view, CourseC currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerC.getInstance();
		this.currentCourse= currentCourse;
		this.getStudentTable();
	}
	
	public boolean deleteBtnClicked(String studentId) {
		// TODO Auto-generated method stub
//		model
		System.out.println("delete chosen student "+studentId+" "+model.getStudent(studentId)+"from course"+currentCourse.getCno());
		return model.removeStudentFromChoiceList(model.getStudent(studentId), currentCourse);
	}
	
	public boolean addBtnClicked(String inputValue) {
		// TODO Auto-generated method stub
		return model.addStudentIntoChoiseList(model.getStudent(inputValue), currentCourse);
	}

	public void getStudentTable() {
		// TODO Auto-generated method stub
		view.updateStudentTable(model.getStudentsOfCourse(currentCourse));
		
	}

	
}
