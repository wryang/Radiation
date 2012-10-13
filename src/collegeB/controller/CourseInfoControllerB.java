package collegeB.controller;

import javax.swing.JOptionPane;

import collegeB.bean.CourseB;
import collegeB.server.ServerB;
import collegeB.view.CourseInfoViewB;

public class CourseInfoControllerB {
	CourseInfoViewB view;
	CourseB currentCourse;
	ServerB model;
	public CourseInfoControllerB(CourseInfoViewB view,
			CourseB currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerB.getInstance();
		this.currentCourse=currentCourse;
		if(currentCourse!=null){
			String share = null;
			if(this.currentCourse.getShare().equals('s'))
				share = "s";
			else
				share = "n";
			view.updateInfo(new String[]{this.currentCourse.getCourseName(),this.currentCourse.getId(),this.currentCourse.getTeacher(),this.currentCourse.getPoint(),this.currentCourse.getAddress(),this.currentCourse.getPeriod(),share});
		}
		}

	public void okBtnClicked(String[] info) {
		// TODO Auto-generated method stub
		if(currentCourse!=null){
			currentCourse.setCourseName(info[0]);
			currentCourse.setId(info[1]);
			currentCourse.setTeacher(info[2]);
			currentCourse.setPoint(info[3]);
			currentCourse.setAddress(info[4]);
			currentCourse.setPeriod(info[5]);
			currentCourse.setShared(info[6].charAt(0));
			if(model.modifyCourseInfo(currentCourse))
				view.disposeFrame();
			
		}
		else{
			CourseB newCourse=new CourseB();
			newCourse.setCourseName(info[0]);
			newCourse.setId(info[1]);
			newCourse.setTeacher(info[2]);
			newCourse.setPoint(info[3]);
			newCourse.setAddress(info[4]);
			newCourse.setPeriod(info[5]);
			newCourse.setShared(info[6].charAt(0));
			if(model.addLocalCourse(newCourse)){
				JOptionPane.showMessageDialog(null,
			            "添加课程成功 !",
			            "提示",
			            JOptionPane.INFORMATION_MESSAGE);
				view.disposeFrame();
			}
			else
				JOptionPane.showMessageDialog(null,
			            "添加课程失败 !",
			            "提示",
			            JOptionPane.INFORMATION_MESSAGE);
		}
		}
			
	}


