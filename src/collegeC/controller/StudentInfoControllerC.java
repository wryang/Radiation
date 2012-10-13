package collegeC.controller;


import javax.swing.JOptionPane;

import collegeC.bean.StudentC;
import collegeC.server.ServerC;
import collegeC.view.StudentInfoViewC;

public class StudentInfoControllerC {
	StudentInfoViewC view;
	StudentC currentStu;
	ServerC model;
	
	public StudentInfoControllerC(StudentInfoViewC view,StudentC currentStu){
		this.view=view;
		model=ServerC.getInstance();
		this.currentStu=currentStu;
		if(this.currentStu!=null){
			String a[]=new String[]{this.currentStu.getSnm(),this.currentStu.getSno(),this.currentStu.getSex(),this.currentStu.getSde()," "," "," "," "," "};
			if(currentStu.hasSelectedCourse())
				for(int i=0;i<currentStu.getSelectedCourse().size();i++)
					a[i+4]=currentStu.getSelectedCourse().get(i).getCnm();
			view.updateInfo(a);
		}
	}
	
	public void okBtnClicked(String info[]){
		if(currentStu!=null){
			currentStu.setSnm(info[0]);
			currentStu.setSno(info[1]);
			currentStu.setSex(info[2]);
			currentStu.setSde(info[3]);
			model.modifyStudentInfo(currentStu);	
		}
		else{
			currentStu = new StudentC();
			currentStu.setSnm(info[0]);
			currentStu.setSno(info[1]);
			currentStu.setSex(info[2]);
			currentStu.setSde(info[3]);
			
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

