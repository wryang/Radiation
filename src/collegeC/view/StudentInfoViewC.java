package collegeC.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import collegeA.view.AdministerViewA;
import collegeC.bean.StudentC;
import collegeC.controller.StudentInfoControllerC;

public class StudentInfoViewC {
	StudentInfoControllerC controller;
	JFrame infoFrame ;
	JPanel infoPanel;
	JTextField nameArea;
	JComboBox genderArea;
	String gender;
	JTextField idArea;
	JTextField instituteArea;
	JTextField courseArea1;
	JTextField courseArea2;
	JTextField courseArea3;
	JTextField courseArea4;
	JTextField courseArea5;
	
	JButton okBtn;
	JButton cancelBtn;
	StudentC currentStu;
	AdministerViewC adView;
	
	public StudentInfoViewC(StudentC currentStu,AdministerViewC adView){
		System.out.println("StudentInfoViewC");
		init();
		addActionListener();
		this.currentStu=currentStu;
		this.adView= adView;
//		System.out.println(currentStu.getStudentName());
		controller=new StudentInfoControllerC(this,currentStu);

	}
	
	public void createController(){
		controller=new StudentInfoControllerC(this,currentStu);

	}

	private void init() {
		// TODO Auto-generated method stub
		 infoFrame = new JFrame();
		 infoPanel=new JPanel(new GridLayout(9,2));
		 JPanel btnPanel=new JPanel();
		 infoFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
		 infoFrame.getContentPane().add(btnPanel,BorderLayout.SOUTH);

		 
		 nameArea=new JTextField(10);
		 genderArea=new JComboBox();
		 genderArea.addItem("Male");
		 genderArea.addItem("Female");
		 gender = "";
		 idArea=new JTextField(10);
		 instituteArea=new JTextField(10);
		 courseArea1=new JTextField(10);
		 courseArea1.setEditable(false);
		 courseArea2=new JTextField(10);
		 courseArea2.setEditable(false);
		 courseArea3=new JTextField(10);
		 courseArea3.setEditable(false);
		 courseArea4=new JTextField(10);
		 courseArea4.setEditable(false);
		 courseArea5=new JTextField(10);
		 courseArea5.setEditable(false);
		 infoPanel.add(new JLabel("姓名："));
		 infoPanel.add(nameArea);
		 infoPanel.add(new JLabel("学号："));
		 infoPanel.add(idArea);
		 infoPanel.add(new JLabel("性别："));
		 infoPanel.add(genderArea);
		 infoPanel.add(new JLabel("院系："));
		 infoPanel.add(instituteArea);
		 infoPanel.add(new JLabel("选课： "));
		 infoPanel.add(courseArea1);
		 infoPanel.add(new JLabel());
		 infoPanel.add(courseArea2);
		 infoPanel.add(new JLabel());
		 infoPanel.add(courseArea3);
		 infoPanel.add(new JLabel());
		 infoPanel.add(courseArea4);
		 infoPanel.add(new JLabel());
		 infoPanel.add(courseArea5);
		 
		 okBtn=new JButton("确定");
		 cancelBtn=new JButton("取消");
		 btnPanel.add(okBtn);
		 btnPanel.add(cancelBtn);

		 	infoFrame.pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			infoFrame.setSize(screenSize.width/4,screenSize.height/3);
			Dimension frameSize = infoFrame.getSize();
			infoFrame.setLocation(screenSize.width/2-frameSize.width/2, screenSize.height/2-frameSize.height/2);		
			infoFrame.setResizable(false);
			infoFrame.setVisible(true);
			infoFrame.setTitle("学生信息");		 
	}
	
	private void addActionListener() {
		// TODO Auto-generated method stub
		okBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.okBtnClicked(getStudentInfo());
				infoFrame.dispose();
				 adView.repaint();
			}

			
		});
		
		cancelBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				infoFrame.dispose();
			}

			
		});
		
		genderArea.addActionListener(new ActionListener()//监听
	 	 {
	 		 public void actionPerformed(ActionEvent event)
	 		 {
	 			gender = (String) genderArea.getSelectedItem();
	 		 }//getSelectedItem()返回当前选项
	 	 });
	}
	
	public void updateInfo(String info[]) {
		// TODO Auto-generated method stub
		nameArea.setText(info[0]);
		idArea.setText(info[1]);
		if("m" == info[2])
			genderArea.setSelectedItem("Male");
		else if("s" == info[2])
			genderArea.setSelectedItem("Female");
		instituteArea.setText(info[3]);
		courseArea1.setText(info[4]);
		courseArea2.setText(info[5]);
		courseArea3.setText(info[6]);
		courseArea4.setText(info[7]);
		courseArea5.setText(info[8]);
	}
	
	private String[] getStudentInfo() {
		// TODO Auto-generated method stub
		String g = null;
		if("Male" == gender)
			g = "m";
		else if("Female" == gender)
			g = "f";
		return new String[]{nameArea.getText(),idArea.getText(),g,instituteArea.getText()};
	}


	public void disposeFrame() {
		// TODO Auto-generated method stub
		infoFrame.dispose();
	}
	
//	public static void main(String args[]){
//		StudentInfoView infoView=new StudentInfoView(new StudentA());
//	}
}
