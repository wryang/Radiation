package collegeC.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import collegeC.bean.CourseC;
import collegeC.bean.StudentC;
import collegeC.controller.CourseSelectedStuControllerC;

public class CourseSelectedStuViewC {
	CourseSelectedStuControllerC controller;
	JFrame infoFrame ;
	JPanel infoPanel;
	JTable studentTable;
	JButton addBtn;
	JButton deleteBtn;
	MyTableModel myModel;

	
	public CourseSelectedStuViewC(CourseC cunrrentCourse){
		init();
		addActionListener();
		controller=new CourseSelectedStuControllerC(this,cunrrentCourse);
	}

	
	private void init() {
		// TODO Auto-generated method stub
		infoFrame = new JFrame();
		 infoPanel=new JPanel();
		 JPanel btnPanel=new JPanel();
		 infoFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
		 infoFrame.getContentPane().add(btnPanel,BorderLayout.SOUTH);
		 
		 addBtn=new JButton("���");
		 deleteBtn=new JButton("ɾ��");
		 btnPanel.add(addBtn);
		 btnPanel.add(deleteBtn);
		
		 		
			Vector<String> column=new Vector<String>();
//			String column[]=new String[]{"���","�γ�","��ʦ","ѧ��"};
			column.add("ѧ��");
			column.add("����");
	
	//	 String column[]=new String[]{"ѧ��","����"};
			studentTable=new JTable(0,2);
			myModel=new MyTableModel(new Vector(),column);
			myModel.insertRow(0,column);		
//			myModel=(DefaultTableModel)studentTable.getModel();
//			myModel.insertRow(0,column);\
			studentTable.setModel(myModel);
			infoPanel.add(studentTable);
			
		 	infoFrame.pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			infoFrame.setSize(screenSize.width/4,screenSize.height/2);
			Dimension frameSize = infoFrame.getSize();
			infoFrame.setLocation(screenSize.width/2-frameSize.width/2, screenSize.height/2-frameSize.height/2);		
			infoFrame.setResizable(false);
			infoFrame.setVisible(true);
			infoFrame.setTitle("ѡ��ѧ����Ϣ");	
	}
	
	private void addActionListener() {
		// TODO Auto-generated method stub
		addBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
	//			controller.addBtnClicked(getCourseInfo());
				String inputValue = JOptionPane.showInputDialog("��������ӵ�ѧ��ѧ�ţ� ");
				if(inputValue!=null){
					if(controller.addBtnClicked(inputValue))
						controller.getStudentTable();
					else
						JOptionPane.showMessageDialog(null,
					            "���ʧ�ܣ� ",
					            "��ʾ",
					            JOptionPane.WARNING_MESSAGE);
				}	else
					JOptionPane.showMessageDialog(null,
				            "ѧ�Ų���Ϊ�գ� ",
				            "��ʾ",
				            JOptionPane.WARNING_MESSAGE);
			}
		});
		
		deleteBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				infoFrame.dispose();
				int selectIndex=studentTable.getSelectedRow();
				if(selectIndex<=0)
					 JOptionPane.showMessageDialog(null,
					            "��ѡ��Ҫɾ����ѧ���� ",
					            "��ʾ",
					            JOptionPane.WARNING_MESSAGE);
				else {
					if(controller.deleteBtnClicked((String)studentTable.getValueAt(selectIndex, 0)))
						myModel.removeRow(selectIndex);
					else
						JOptionPane.showMessageDialog(null,
					            "ɾ��ʧ�ܣ� �� ",
					            "��ʾ",
					            JOptionPane.WARNING_MESSAGE);												
				}
			}

			
		});
	}

	public void updateStudentTable(List<StudentC> allStudents){
		int a =myModel.getRowCount();
		for(int i=a;i>1;i--)
			myModel.removeRow(1);
		for(int i=0;i<allStudents.size();i++){
			myModel.insertRow(i+1, new String[]{allStudents.get(i).getSno(),allStudents.get(i).getSnm()});
		}
		
	}
}
