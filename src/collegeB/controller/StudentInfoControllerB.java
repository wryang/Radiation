package collegeB.controller;


import javax.swing.JOptionPane;

import collegeB.bean.StudentB;
import collegeB.server.ServerB;
import collegeB.view.StudentInfoViewB;

public class StudentInfoControllerB {
	StudentInfoViewB view;
	StudentB currentStu;
	ServerB model;
	
	public StudentInfoControllerB(StudentInfoViewB view,StudentB currentStu){
		this.view=view;
		model=ServerB.getInstance();
		this.currentStu=currentStu;
		if(this.currentStu!=null){
			String a[]=new String[]{this.currentStu.getName(),this.currentStu.getStudentNumber(),this.currentStu.getSex(),this.currentStu.getSpeciality()," "," "," "," "," "};
			if(currentStu.hasSelectedCourse())
				for(int i=0;i<currentStu.getSelectedCourse().size();i++)
					a[i+4]=currentStu.getSelectedCourse().get(i).getCourseName();
			view.updateInfo(a);
		}
	}
	
	public void okBtnClicked(String info[]){
		if(currentStu!=null){
			currentStu.setName(info[0]);
			currentStu.setStudentNumber(info[1]);
			currentStu.setSex(info[2]);
			currentStu.setSpeciality(info[3]);
			model.modifyStudentInfo(currentStu);	
		}
		else{
			currentStu=new StudentB();
			currentStu.setName(info[0]);
			currentStu.setStudentNumber(info[1]);
			currentStu.setSex(info[2]);
			currentStu.setSpeciality(info[3]);
			
			if(model.addLocalStudent(currentStu)){
				JOptionPane.showMessageDialog(null,
			            "添加学生成功 !",
			            "提示",
			            JOptionPane.INFORMATION_MESSAGE);
				view.disposeFrame();
			}
			else
				JOptionPane.showMessageDialog(null,
			            "添加学生失败 !",
			            "提示",
			            JOptionPane.INFORMATION_MESSAGE);
		}
		}
	}

