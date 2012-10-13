package collegeA.controller;

import javax.swing.JOptionPane;

import collegeA.bean.CourseA;
import collegeA.server.ServerA;
import collegeA.view.CourseInfoViewA;

public class CourseInfoControllerA {
	CourseInfoViewA view;
	CourseA currentCourse;
	ServerA model;
	public CourseInfoControllerA(CourseInfoViewA view,
			CourseA currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerA.getInstance();
		this.currentCourse=currentCourse;
		if(currentCourse!=null){
			String share = null;
			if(this.currentCourse.getShare().equals('s'))
				share = "s";
			else
				share = "n";
			
			view.updateInfo(new String[]{this.currentCourse.getClassName(),this.currentCourse.getClassId(),this.currentCourse.getTeacher(),this.currentCourse.getGrade(),this.currentCourse.getLocation(),share});
		}
	}

	public void okBtnClicked(String[] info) {
		// TODO Auto-generated method stub
		if(currentCourse!=null){
			currentCourse.setClassName(info[0]);
			currentCourse.setClassId(info[1]);
			currentCourse.setTeacher(info[2]);
			currentCourse.setGrade(info[3]);
			currentCourse.setLocation(info[4]);
			currentCourse.setShare(info[5].charAt(0));

			if(model.modifyCourseInfo(currentCourse))
				view.disposeFrame();
			
		}
		else{
			CourseA newCourse=new CourseA();
			newCourse.setClassName(info[0]);
			newCourse.setClassId(info[1]);
			newCourse.setTeacher(info[2]);
			newCourse.setGrade(info[3]);
			newCourse.setLocation(info[4]);
			newCourse.setShare(info[5].charAt(0));

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


