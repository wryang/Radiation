package collegeC.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import collegeC.bean.CourseC;
import collegeC.view.MyTableModel;
import collegeC.bean.CourseC;
import collegeC.controller.StudentViewControllerC;

public class StudentViewC {
	StudentViewControllerC controller;
	public JFrame studentFrame;
	public JPanel ProfilePanel;
	public JPanel coursePanel;
	public JTable profileTable;
	public JTable courseTable;
	public JTable selectTable;
	public JButton chooseButton;
	public JButton 	quitButton;

	public MyTableModel myModel;
	public MyTableModel selectModel;
	
	public StudentViewC(){
		init();
		addActionListener();
		controller=new StudentViewControllerC(this);
	}
	
	
	private void init(){
		studentFrame=new JFrame();
		ProfilePanel=new JPanel(new BorderLayout());
		coursePanel=new JPanel(new BorderLayout());
		studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		studentFrame.getContentPane().add(ProfilePanel, BorderLayout.WEST);
		studentFrame.getContentPane().add(coursePanel, BorderLayout.CENTER);
		
		profileTable=new JTable(4,2);
		profileTable.setValueAt("������", 0, 0);
		profileTable.setValueAt("ѧ�ţ�", 1, 0);
		profileTable.setValueAt("�Ա�", 2, 0);
		profileTable.setValueAt("Ժϵ��", 3, 0);
//		profileTable.setValueAt("ѡ�Σ�", 4, 0);
		
		JPanel myPanel=new JPanel();
//		myPanel.add(new JLabel());
		myPanel.add(profileTable);
		ProfilePanel.add(myPanel,BorderLayout.CENTER);
		JLabel myLabel=new JLabel("��ӭ��������ϵͳ��");
//		myLabel.setVerticalAlignment(0);
		ProfilePanel.add(myLabel,BorderLayout.NORTH);
//		ProfilePanel.add(new JPanel(),BorderLayout.SOUTH);
		
		JPanel selectPanel=new JPanel(new BorderLayout());
		selectPanel.add(new JLabel("��ѡ�γ�"),BorderLayout.NORTH);
		selectTable=new JTable(6,2);
		Vector<String> selecColumn=new Vector<String>();
		selecColumn.add("���");
		selecColumn.add("�γ�");
		selectModel=new MyTableModel(new Vector(),selecColumn);
		selectModel.insertRow(0,selecColumn);		
		selectTable.setModel(selectModel);
		
		selectPanel.add(selectTable,BorderLayout.CENTER);
		quitButton=new JButton("��ѡ");
		selectPanel.add(quitButton,BorderLayout.SOUTH);
		ProfilePanel.add(selectPanel,BorderLayout.SOUTH);
		
		
		Vector<String> column=new Vector<String>();
//		String column[]=new String[]{"���","�γ�","��ʦ","ѧ��"};
		column.add("���");
		column.add("�γ�");
		column.add("��ʦ");
		column.add("ѧ��");
		column.add("�ص�");
		column.add("ѧʱ");



		courseTable=new JTable(0,6);
//		courseTable.setEnabled(false);
		myModel=new MyTableModel(new Vector(),column);
		myModel.insertRow(0,column);		
		courseTable.setModel(myModel);
		coursePanel.add(courseTable,BorderLayout.CENTER);
		
		chooseButton=new JButton("ѡ��");
		chooseButton.setSize(100, 80);
		coursePanel.add(chooseButton,BorderLayout.SOUTH);
		
		studentFrame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		studentFrame.setSize(screenSize.width/2,screenSize.height/2);
		Dimension frameSize = studentFrame.getSize();
		studentFrame.setLocation(screenSize.width/2-frameSize.width/2, screenSize.height/2-frameSize.height/2);		
		studentFrame.setResizable(false);
		studentFrame.setVisible(true);
		studentFrame.setTitle("ѧ����ҳ");
		
	}
	
	private void addActionListener() {
		// TODO Auto-generated method stub
		chooseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(chooseCourse(courseTable.getSelectedRow()-1))
					controller.chooseBtnClicked((String)courseTable.getValueAt(courseTable.getSelectedRow(), 0));
				}			

			
		});
		
		quitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//apart from the first row 
				if(chooseCourse(selectTable.getSelectedRow()-1))
					controller.quitBtnClicked((String)selectTable.getValueAt(selectTable.getSelectedRow(), 0));
			}

			
		});
	}
	
	private boolean chooseCourse(int selectIndex ) {
		// TODO Auto-generated method stub
		if(selectIndex<0){
			 JOptionPane.showMessageDialog(null,
			            "��ѡ��һ�ſγ�! ",
			            "��ʾ",
			            JOptionPane.WARNING_MESSAGE);
			 return false;
		}
		else return true;
	}
	

	public void updateProfile(String info[]){
		for(int i=0;i<info.length;i++){
			profileTable.setValueAt(info[i], i, 1);
		}
	}

public void updateCourseTable(List<CourseC> allcourse){
	for(int i=0;i<allcourse.size();i++){
		myModel.insertRow(i+1, new String[]{allcourse.get(i).getCno(),allcourse.get(i).getCnm(),allcourse.get(i).getTec(),allcourse.get(i).getCpt().toString(),allcourse.get(i).getPla(),allcourse.get(i).getCtm().toString()});
	}
	System.out.println("call updateCourseTable "+allcourse.size());

}
//	private int getCourseTableSelection(){
//		return courseTable.getSelectedRow();
//	}

public void updateSelectTable(List<CourseC> courses){

//	profileTable.setValueAt(courseName, 5, 1);

//	profileTable.setValueAt(courseName, 4, 1);
	System.out.println("row count"+selectModel.getRowCount());
	int a=selectModel.getRowCount();
	for(int i=a;i>1;i--){
		selectModel.removeRow(1);
		System.out.println("row count remove"+i);
//�е��������ɾ���Ľ��ж��ı��ˣ���
	}
	if(null == courses) {
		return;
	}
	
	System.out.println("call update SelectTable size: "+courses.size());
	for(int i=0;i<courses.size();i++){
		System.out.println("course "+i);
		System.out.println(courses.get(i).getCno()+courses.get(i).getCnm());

		selectModel.insertRow(1, new String[]{courses.get(i).getCno(),courses.get(i).getCnm()});
		System.out.println("row count add"+i);

	}
//	profileTable.repaint();
}





public void toggleCourseTable(boolean b) {
	// TODO Auto-generated method stub
	courseTable.setEnabled(b);
	System.out.println("enabelv course table"+b);

	courseTable.repaint();
}


	
}

class MyTableModel extends DefaultTableModel{ 

@Override 
public boolean isCellEditable(int row, int column) {	
return false; 
} 


public MyTableModel(Vector data,Vector columns){ 
super(data, columns); 
} 

} 


