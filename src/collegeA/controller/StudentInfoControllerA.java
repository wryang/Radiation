package collegeA.controller;


import javax.swing.JOptionPane;

import collegeA.bean.StudentA;
import collegeA.server.ServerA;
import collegeA.view.StudentInfoViewA;

public class StudentInfoControllerA {
	StudentInfoViewA view;
	StudentA currentStu;
	ServerA model;
	
	public StudentInfoControllerA(StudentInfoViewA view,StudentA currentStu){
		this.view=view;
		model=ServerA.getInstance();
		this.currentStu=currentStu;
		if(this.currentStu!=null){
			String a[]=new String[]{this.currentStu.getStudentName(),this.currentStu.getStudentId(),this.currentStu.getGender(),this.currentStu.getInstitution()," "," "," "," "," "};
			if(currentStu.hasSelectedCourse())
				for(int i=0;i<currentStu.getSelectedCourse().size();i++)
					a[i+4]=currentStu.getSelectedCourse().get(i).getClassName();
			view.updateInfo(a);
		}
	}
	
	public void okBtnClicked(String info[]){
		if(currentStu!=null){
			currentStu.setStudentName(info[0]);
			currentStu.setStudentId(info[1]);
			currentStu.setGender(info[2]);
			currentStu.setInstitution(info[3]);
			model.modifyStudentInfo(currentStu);	
		}
		else{
			currentStu=new StudentA();
			currentStu.setStudentName(info[0]);
			currentStu.setStudentId(info[1]);
			currentStu.setGender(info[2]);
			currentStu.setInstitution(info[3]);
			
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

