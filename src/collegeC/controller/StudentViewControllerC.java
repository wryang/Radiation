package collegeC.controller;



import javax.swing.JOptionPane;

import collegeC.bean.StudentC;
import collegeC.server.ServerC;
import collegeC.view.StudentViewC;

public class StudentViewControllerC {
	StudentViewC view;
	ServerC model;
	StudentC loginUser;
	public StudentViewControllerC(StudentViewC view){
		this.view=view;
		model=ServerC.getInstance();
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		loginUser=model.getCurrentUser();
		String info[]=new String[]{loginUser.getSnm(),loginUser.getSno(),loginUser.getSex(),loginUser.getSde()};
		view.updateProfile(info);

		if(loginUser.hasSelectedCourse()){
			System.out.println(" init select table"+loginUser.getSelectedCourse().size());
			view.updateSelectTable(loginUser.getSelectedCourse());
		}
			view.updateCourseTable(model.getAllCourse());
			view.toggleCourseTable(!loginUser.isSelectFull());
	}
	
	public void chooseBtnClicked(String selectCourseId) {
		// TODO Auto-generated method stub
		System.out.println("Selected Course ID " +selectCourseId+" loginUser "+loginUser.getSno());
		if(model.chooseCourse(loginUser, model.getCourse(selectCourseId))){
			JOptionPane.showMessageDialog(null,
		            "选课成功！ ",
		            "提示",
		            JOptionPane.INFORMATION_MESSAGE);
			model.updateCurrentUser();
			view.updateSelectTable(model.getCurrentUser().getSelectedCourse());
			view.toggleCourseTable(!model.getCurrentUser().isSelectFull());
		}
		else
			JOptionPane.showMessageDialog(null,
		            "选课失败 !",
		            "提示",
		            JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public void quitBtnClicked(String selectCourseId) {
		// TODO Auto-generated method stub
		System.out.println("Selected Course ID " +selectCourseId+" loginUser "+loginUser.getSno());

		if(model.cancelCourseChoice(loginUser, model.getCourse(selectCourseId))){
			JOptionPane.showMessageDialog(null,
		            "退课成功！ ",
		            "提示",
		            JOptionPane.INFORMATION_MESSAGE);
//			model.getCourse(selectCourseId).getClassName();
			model.updateCurrentUser();
			view.updateSelectTable(model.getCurrentUser().getSelectedCourse());
			if(!model.getCurrentUser().isSelectFull())
				view.toggleCourseTable(true);
		}
		else
			JOptionPane.showMessageDialog(null,
		            "退课失败 !",
		            "提示",
		            JOptionPane.INFORMATION_MESSAGE);
		
	}
}
