package collegeB.controller;

import collegeB.bean.CourseB;
import collegeB.server.ServerB;
import collegeB.view.CourseSelectedStuViewB;

public class CourseSelectedStuControllerB {
	CourseSelectedStuViewB view;
	ServerB model;
	CourseB currentCourse;
	
	public CourseSelectedStuControllerB(
			CourseSelectedStuViewB view, CourseB currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerB.getInstance();
		this.currentCourse= currentCourse;
		this.getStudentTable();
	}
	
	public boolean deleteBtnClicked(String studentId) {
		// TODO Auto-generated method stub
//		model
		System.out.println("delete chosen student "+studentId+" "+model.getStudent(studentId)+"from course"+currentCourse.getId());
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
