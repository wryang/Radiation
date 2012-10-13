package collegeA.controller;

import collegeA.bean.CourseA;
import collegeA.server.ServerA;
import collegeA.view.CourseSelectedStuViewA;

public class CourseSelectedStuControllerA {
	CourseSelectedStuViewA view;
	ServerA model;
	CourseA currentCourse;
	
	public CourseSelectedStuControllerA(
			CourseSelectedStuViewA view, CourseA currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerA.getInstance();
		this.currentCourse= currentCourse;
		this.getStudentTable();
	}
	
	public boolean deleteBtnClicked(String studentId) {
		// TODO Auto-generated method stub
//		model
		System.out.println("delete chosen student "+studentId+" "+model.getStudent(studentId)+"from course"+currentCourse.getClassId());
		return model.removeStudentFromChoiseList(model.getStudent(studentId), currentCourse);
	}
	
	public boolean addBtnClicked(String inputValue) {
		// TODO Auto-generated method stub
		if(model.getStudent(inputValue).isSelectFull())
			return false;
		else
			return model.addStudentIntoChoiseList(model.getStudent(inputValue), currentCourse);
	}

	public void getStudentTable() {
		// TODO Auto-generated method stub
		view.updateStudentTable(model.getStudentsOfCourse(currentCourse));
		
	}

	
}
