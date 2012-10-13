package collegeB.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import collegeB.bean.CourseB;
import collegeB.controller.CourseInfoControllerB;
import collegeC.view.AdministerViewC;

public class CourseInfoViewB {
	CourseInfoControllerB controller;
	JFrame infoFrame ;
	JPanel infoPanel;
	JTextField nameField;
	JTextField teacherField;
	JTextField idField;
	JTextField creditField;
	JTextField addressField;
	JTextField periodField;
	
	JCheckBox shared;

	JButton okBtn;
	JButton cancelBtn;
	AdministerViewB adView;
	public CourseInfoViewB(CourseB currentCourse,AdministerViewB adView){
		init();
		addActionListener();
		this.adView= adView;
		controller=new CourseInfoControllerB(this,currentCourse);
	}


	private void init() {
		// TODO Auto-generated method stub
		 infoFrame = new JFrame();
		 infoPanel=new JPanel(new GridLayout());
		 JPanel btnPanel=new JPanel();
		 infoFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
		 infoFrame.getContentPane().add(btnPanel,BorderLayout.SOUTH);

		 
		 nameField=new JTextField(10);
		 teacherField=new JTextField(10);
		 idField=new JTextField(10);
		 creditField=new JTextField(10);
		 addressField=new JTextField(10);
		 periodField=new JTextField(10);
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
		 infoPanel.add(addressField);
		 infoPanel.add(new JLabel("学时："));
		 infoPanel.add(periodField);
		 infoPanel.add(new JLabel("共享："));
		 infoPanel.add(shared);
		
		 
		 okBtn=new JButton("确定");
		 cancelBtn=new JButton("取消");
		 btnPanel.add(okBtn);
		 btnPanel.add(cancelBtn);

		 	infoFrame.pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			infoFrame.setSize(screenSize.width/2,screenSize.height/10);
			Dimension frameSize = infoFrame.getSize();
			infoFrame.setLocation(screenSize.width/2-frameSize.width/2, screenSize.height/2-frameSize.height/2);		
			infoFrame.setResizable(false);
			infoFrame.setVisible(true);
			infoFrame.setTitle("课程信息");		 
	}
	
	private void addActionListener() {
		// TODO Auto-generated method stub
		okBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.okBtnClicked(getCourseInfo());
				 adView.repaint();
			}
		});
		
		cancelBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				infoFrame.dispose();
			}

			
		});
	}
	
	public void updateInfo(String info[]) {
		// TODO Auto-generated method stub
		nameField.setText(info[0]);
		idField.setText(info[1]);
		teacherField.setText(info[2]);
		creditField.setText(info[3]);
		addressField.setText(info[4]);
		periodField.setText(info[5]);

		if(info[6].equals("s"))
			shared.setSelected(true);
		else
			shared.setSelected(false);

	}
	
	private String[] getCourseInfo() {
		// TODO Auto-generated method stub
		String share = "n";
		if(shared.isSelected())
			share = "s";
		return new String[]{nameField.getText(),idField.getText(),teacherField.getText(),creditField.getText(),addressField.getText(),periodField.getText(),share};
	}
	public void disposeFrame() {
		// TODO Auto-generated method stub
		infoFrame.dispose();
	}
}
