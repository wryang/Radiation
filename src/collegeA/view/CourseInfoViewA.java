package collegeA.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import collegeA.bean.CourseA;
import collegeA.controller.CourseInfoControllerA;

public class CourseInfoViewA {
	CourseInfoControllerA controller;
	JFrame infoFrame;
	JPanel infoPanel;
	JTextField nameField;
	JTextField teacherField;
	JTextField idField;
	JTextField creditField;
	JTextField locationField;

	JCheckBox shared;

	JButton okBtn;
	JButton cancelBtn;
	AdministerViewA adView;

	@SuppressWarnings("deprecation")
	public CourseInfoViewA(CourseA currentCourse, AdministerViewA adView) {
		init();
		addActionListener();
		this.adView = adView;

		if (null != currentCourse && !currentCourse.isLocalCourse()) {
			shared.enable();
		}
		controller = new CourseInfoControllerA(this, currentCourse);
	}

	private void init() {
		// TODO Auto-generated method stub
		infoFrame = new JFrame();
		infoPanel = new JPanel(new GridLayout());
		JPanel btnPanel = new JPanel();
		infoFrame.getContentPane().add(infoPanel, BorderLayout.CENTER);
		infoFrame.getContentPane().add(btnPanel, BorderLayout.SOUTH);

		nameField = new JTextField(10);
		teacherField = new JTextField(10);
		idField = new JTextField(10);
		creditField = new JTextField(10);
		locationField = new JTextField(10);
		shared = new JCheckBox();

		infoPanel.add(new JLabel("课程："));
		infoPanel.add(nameField);
		infoPanel.add(new JLabel("编号："));
		infoPanel.add(idField);
		infoPanel.add(new JLabel("教师："));
		infoPanel.add(teacherField);
		infoPanel.add(new JLabel("学分："));
		infoPanel.add(creditField);
		infoPanel.add(new JLabel("地点："));
		infoPanel.add(locationField);
		infoPanel.add(new JLabel("共享："));
		infoPanel.add(shared);

		okBtn = new JButton("确定");
		cancelBtn = new JButton("取消");
		btnPanel.add(okBtn);
		btnPanel.add(cancelBtn);

		infoFrame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		infoFrame.setSize(screenSize.width / 2, screenSize.height / 10);
		Dimension frameSize = infoFrame.getSize();
		infoFrame.setLocation(screenSize.width / 2 - frameSize.width / 2,
				screenSize.height / 2 - frameSize.height / 2);
		infoFrame.setResizable(false);
		infoFrame.setVisible(true);
		infoFrame.setTitle("课程信息");
	}

	private void addActionListener() {
		// TODO Auto-generated method stub
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.okBtnClicked(getCourseInfo());
				try {
					adView.repaint();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoFrame.dispose();
			}

		});

		/*
		 * shared.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e){
		 * 
		 * } });
		 */
	}

	public void updateInfo(String info[]) {
		// TODO Auto-generated method stub
		nameField.setText(info[0]);
		idField.setText(info[1]);
		teacherField.setText(info[2]);
		creditField.setText(info[3]);
		locationField.setText(info[4]);
		if (info[5].equals("s"))
			shared.setSelected(true);
		else
			shared.setSelected(false);

	}

	private String[] getCourseInfo() {
		// TODO Auto-generated method stub
		String share = "n";
		if (shared.isSelected())
			share = "s";
		return new String[] { nameField.getText(), idField.getText(),
				teacherField.getText(), creditField.getText(),
				locationField.getText(), share };
	}

	public void disposeFrame() {
		// TODO Auto-generated method stub
		infoFrame.dispose();
	}
}
