package collegeA.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import collegeA.bean.CourseA;
import collegeA.controller.StudentViewControllerA;

public class StudentViewA {
	StudentViewControllerA controller;
	public JFrame studentFrame;
	public JPanel ProfilePanel;
	public JPanel coursePanel;
	public JTable profileTable;
	public JTable courseTable;
	public JTable selectTable;
	public JButton chooseButton;
	public JButton quitButton;

	public MyTableModel myModel;
	public MyTableModel selectModel;

	public StudentViewA() {
		init();
		addActionListener();
		controller = new StudentViewControllerA(this);
	}

	private void init() {
		studentFrame = new JFrame();
		ProfilePanel = new JPanel(new BorderLayout());
		coursePanel = new JPanel(new BorderLayout());
		studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		studentFrame.getContentPane().add(ProfilePanel, BorderLayout.WEST);
		studentFrame.getContentPane().add(coursePanel, BorderLayout.CENTER);

		profileTable = new JTable(4, 2);
		profileTable.setValueAt("姓名：", 0, 0);
		profileTable.setValueAt("学号：", 1, 0);
		profileTable.setValueAt("性别：", 2, 0);
		profileTable.setValueAt("院系：", 3, 0);
		// profileTable.setValueAt("选课：", 4, 0);

		JPanel myPanel = new JPanel();
		// myPanel.add(new JLabel());
		myPanel.add(profileTable);
		ProfilePanel.add(myPanel, BorderLayout.CENTER);
		JLabel myLabel = new JLabel("欢迎来到教务系统！");
		// myLabel.setVerticalAlignment(0);
		ProfilePanel.add(myLabel, BorderLayout.NORTH);
		// ProfilePanel.add(new JPanel(),BorderLayout.SOUTH);

		JPanel selectPanel = new JPanel(new BorderLayout());
		selectPanel.add(new JLabel("已选课程"), BorderLayout.NORTH);
		selectTable = new JTable(6, 2);
		Vector<String> selecColumn = new Vector<String>();
		selecColumn.add("编号");
		selecColumn.add("课程");
		selectModel = new MyTableModel(new Vector(), selecColumn);
		selectModel.insertRow(0, selecColumn);
		selectTable.setModel(selectModel);

		selectPanel.add(selectTable, BorderLayout.CENTER);
		quitButton = new JButton("退选");
		selectPanel.add(quitButton, BorderLayout.SOUTH);
		ProfilePanel.add(selectPanel, BorderLayout.SOUTH);

		Vector<String> column = new Vector<String>();
		// String column[]=new String[]{"编号","课程","教师","学分"};
		column.add("编号");
		column.add("课程");
		column.add("教师");
		column.add("学分");
		column.add("地点");

		courseTable = new JTable(0, 5);
		// courseTable.setEnabled(false);
		myModel = new MyTableModel(new Vector(), column);
		myModel.insertRow(0, column);
		courseTable.setModel(myModel);
		coursePanel.add(courseTable, BorderLayout.CENTER);

		chooseButton = new JButton("选课");
		chooseButton.setSize(100, 80);
		coursePanel.add(chooseButton, BorderLayout.SOUTH);

		studentFrame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		studentFrame.setSize(screenSize.width / 2, screenSize.height / 2);
		Dimension frameSize = studentFrame.getSize();
		studentFrame.setLocation(screenSize.width / 2 - frameSize.width / 2,
				screenSize.height / 2 - frameSize.height / 2);
		studentFrame.setResizable(false);
		studentFrame.setVisible(true);
		studentFrame.setTitle("学生主页");

	}

	private void addActionListener() {
		// TODO Auto-generated method stub
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				;// apart from the first row of column name
				if (chooseCourse(courseTable.getSelectedRow() - 1))
					System.out.println("quit course"
							+ (String) courseTable.getValueAt(
									courseTable.getSelectedRow(), 0));
				controller.chooseBtnClicked((String) courseTable.getValueAt(
						courseTable.getSelectedRow(), 0));
			}

		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// apart from the first row
				if (chooseCourse(selectTable.getSelectedRow() - 1))
					System.out.println("quit course"
							+ (String) selectTable.getValueAt(
									selectTable.getSelectedRow(), 0));

				controller.quitBtnClicked((String) selectTable.getValueAt(
						selectTable.getSelectedRow(), 0));
			}

		});
	}

	private boolean chooseCourse(int selectIndex) {
		// TODO Auto-generated method stub
		if (selectIndex < 0) {
			JOptionPane.showMessageDialog(null, "请选中一门课程! ", "提示",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else
			return true;
	}

	// private void courseSelected(int selectIndex) {
	// // TODO Auto-generated method stub]
	// controller.chooseBtnClicked((String)courseTable.getValueAt(selectIndex,
	// 0));
	// // updateCourseInfo();
	// }

	public void updateProfile(String info[]) {
		for (int i = 0; i < info.length; i++) {
			profileTable.setValueAt(info[i], i, 1);
		}
	}

	public void updateCourseTable(List<CourseA> allcourse) {
		for (int i = 0; i < allcourse.size(); i++) {
			myModel.insertRow(i + 1, new String[] {
					allcourse.get(i).getClassId(),
					allcourse.get(i).getClassName(),
					allcourse.get(i).getTeacher(), allcourse.get(i).getGrade(),
					allcourse.get(i).getLocation() });
		}
		System.out.println("call updateCourseTable " + allcourse.size());

	}

	// private int getCourseTableSelection(){
	// return courseTable.getSelectedRow();
	// }

	public void updateSelectTable(List<CourseA> courses) {

		// profileTable.setValueAt(courseName, 5, 1);

		// profileTable.setValueAt(courseName, 4, 1);
		System.out.println("row count" + selectModel.getRowCount());
		int a = selectModel.getRowCount();
		for (int i = a; i > 1; i--) {
			selectModel.removeRow(1);
			System.out.println("row count remove" + i);
			// 行的序号随着删除的进行而改变了！！
		}
		System.out.println("call update SelectTable" + courses.size());
		for (int i = 0; i < courses.size(); i++) {
			selectModel.insertRow(1,
					new String[] { courses.get(i).getClassId(),
							courses.get(i).getClassName() });
			System.out.println("row count add" + i);

		}
		// profileTable.repaint();
	}

	public void toggleCourseTable(boolean b) {
		// TODO Auto-generated method stub

		courseTable.setEnabled(b);
		System.out.println("enabelv course table" + b);

		courseTable.repaint();

	}

}

class MyTableModel extends DefaultTableModel {

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public MyTableModel(Vector data, Vector columns) {
		super(data, columns);
	}

}
