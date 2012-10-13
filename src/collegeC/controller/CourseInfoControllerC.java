package collegeC.controller;

import javax.swing.JOptionPane;

import collegeC.bean.CourseC;
import collegeC.server.ServerC;
import collegeC.view.CourseInfoViewC;

public class CourseInfoControllerC {
	CourseInfoViewC view;
	CourseC currentCourse;
	ServerC model;
	public CourseInfoControllerC(CourseInfoViewC view,
			CourseC currentCourse) {
		// TODO Auto-generated constructor stub
		this.view=view;
		model=ServerC.getInstance();
		this.currentCourse=currentCourse;
		if(currentCourse!=null){
			String share = null;
			if(this.currentCourse.getShare())
				share = "s";
			else
				share = "n";
			view.updateInfo(new String[]{this.currentCourse.getCnm(),this.currentCourse.getCno(),this.currentCourse.getTec(),this.currentCourse.getCpt().toString(),this.currentCourse.getPla(),this.currentCourse.getCtm().toString(),share});
		}
	}

	public void okBtnClicked(String[] info) {
		// TODO Auto-generated method stub
		if(currentCourse!=null){
			currentCourse.setCnm(info[0]);
			currentCourse.setCno(info[1]);
			currentCourse.setTec(info[2]);
			currentCourse.setCpt(Integer.parseInt(info[3]));
			currentCourse.setPla(info[4]);
			currentCourse.setCtm(Integer.parseInt(info[5]));
			boolean isShared = false;
			if(info[6].equals("s"))
				isShared = true;
			currentCourse.setShare(isShared);
			
			if(model.modifyCourseInfo(currentCourse))
				view.disposeFrame();
			
		}
		else{
			CourseC newCourse=new CourseC();
			newCourse.setCnm(info[0]);
			newCourse.setCno(info[1]);
			newCourse.setTec(info[2]);
			newCourse.setCpt(Integer.parseInt(info[3]));
			newCourse.setPla(info[4]);
			newCourse.setCtm(Integer.parseInt(info[5]));
			boolean isShared = false;
			if(info[6].equals("s"))
				isShared = true;
			newCourse.setShare(isShared);
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


