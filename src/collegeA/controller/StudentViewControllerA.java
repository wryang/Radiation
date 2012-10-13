package collegeA.controller;

import javax.swing.JOptionPane;

import collegeA.bean.StudentA;
import collegeA.server.ServerA;
import collegeA.view.StudentViewA;

public class StudentViewControllerA {
	StudentViewA view;
	ServerA model;
	StudentA loginUser;

	public StudentViewControllerA(StudentViewA view) {
		this.view = view;
		model = ServerA.getInstance();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		loginUser = model.getCurrentStudent();
		String info[] = new String[] { loginUser.getStudentName(),
				loginUser.getStudentId(), loginUser.getGender(),
				loginUser.getInstitution() };
		view.updateProfile(info);

		if (loginUser.hasSelectedCourse()) {
			System.out.println(" init select table"
					+ loginUser.getSelectedCourse().size());
			view.updateSelectTable(loginUser.getSelectedCourse());
		}
		view.updateCourseTable(model.getAllCourses());
		view.toggleCourseTable(!loginUser.isSelectFull());
	}

	public void chooseBtnClicked(String selectCourseId) {
		// TODO Auto-generated method stub
		System.out.println("Selected Course ID " + selectCourseId
				+ " loginUser " + loginUser.getStudentId());
		if (model.chooseCourse(loginUser, model.getCourse(selectCourseId))) {
			JOptionPane.showMessageDialog(null, "ѡ�γɹ��� ", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			// model.getCourse(selectCourseId).getClassName();
			model.updateCurrentUser();
			view.updateSelectTable(model.getCurrentStudent()
					.getSelectedCourse());
			view.toggleCourseTable(!model.getCurrentStudent().isSelectFull());

		} else
			JOptionPane.showMessageDialog(null, "ѡ��ʧ�� !", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);

	}

	public void quitBtnClicked(String selectCourseId) {
		// TODO Auto-generated method stub
		System.out.println("Selected Course ID " + selectCourseId
				+ " loginUser " + loginUser.getStudentId());

		if (model
				.cancelCourseChoice(loginUser, model.getCourse(selectCourseId))) {
			JOptionPane.showMessageDialog(null, "�˿γɹ��� ", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			// model.getCourse(selectCourseId).getClassName();
			model.updateCurrentUser();
			view.updateSelectTable(model.getCurrentStudent()
					.getSelectedCourse());
			if (!model.getCurrentStudent().isSelectFull())
				view.toggleCourseTable(true);
		} else
			JOptionPane.showMessageDialog(null, "�˿�ʧ�� !", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);

	}

}
