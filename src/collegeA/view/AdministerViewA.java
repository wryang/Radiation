package collegeA.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import collegeA.bean.CourseA;
import collegeA.bean.StudentA;
import collegeA.controller.AdministerViewControllerA;

public class AdministerViewA {
	private AdministerViewControllerA controller;

	private JFrame frame;
	private JTabbedPane tabs;
	private JPanel studentInfo;
	private JPanel courseInfo;
	private JTable stdTable;
	private AbstractTableModel stdTableModel;
	private JTable courseTable;
	private AbstractTableModel courseTableModel;

	private ArrayList<StudentA> stdList;
	private ArrayList<CourseA> courseList;
	private StudentA stdSelected;
	private CourseA courseSelected;
	
	//Button
	private JButton editStdInfoBtn;
	private JButton addStdInfoBtn;
	private JButton delStdInfoBtn;
	private JButton detailCourseInfoBtn;
	private JButton editCourseInfoBtn;
	private JButton addCourseInfoBtn;
	private JButton delCourseInfoBtn;
	private JButton getSharedCourseBtn;

	public AdministerViewA() {
		initialize();
		addActionListeners();
		
	}

	private void initialize() {
		frame = new JFrame("A 学院 欢迎您!");
		tabs = new JTabbedPane(JTabbedPane.NORTH);

		studentInfo = new JPanel(new BorderLayout());
		courseInfo = new JPanel(new BorderLayout());

//		student information 
		stdList = new ArrayList<StudentA>();
		stdSelected = new StudentA();
		
		
		//create button of studentInfo
		editStdInfoBtn = new JButton("编辑");
		addStdInfoBtn = new JButton("添加");
		delStdInfoBtn = new JButton("删除");
		
		
//		course information
		courseList = new ArrayList<CourseA>();
		courseSelected = new CourseA();
		
		
		// create button of courseInfo
		detailCourseInfoBtn = new JButton("查看");
		editCourseInfoBtn = new JButton("编辑");
		addCourseInfoBtn = new JButton("添加");
		delCourseInfoBtn = new JButton("删除");
		getSharedCourseBtn = new JButton("共享课程");
		
		tabs.add(studentInfo);
		tabs.add(courseInfo);

		JLabel studentLabel = new JLabel("学生信息");
		JLabel courseLabel = new JLabel("课程信息");
		tabs.setTabComponentAt(0, studentLabel);
		tabs.setTabComponentAt(1, courseLabel);

		frame.add(tabs);
	}
	
	private void addActionListeners(){
		editStdInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if (stdTable.getSelectedRow() < 0) {
					int choice1 = JOptionPane.showConfirmDialog(null,
							"请选择要编辑的学生资料!", null, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
				else{
					stdSelected = stdList.get(stdTable.getSelectedRow());
					controller.requestEditStdInfo();
				}
			}
		});
		
		addStdInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				controller.requestAddStdInfo();
			}
		});
		
		delStdInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (stdTable.getSelectedRow() >= 0) {
					JLabel temp = new JLabel();
					int choice1 = JOptionPane.showConfirmDialog(null,"确定要删除该学生资料吗？", null, JOptionPane.OK_CANCEL_OPTION);
					if (choice1 == 0) {
						stdSelected = stdList
		                   .get(stdTable.getSelectedRow());
						try {
							controller.requestRemoveStd();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					int choice1 = JOptionPane.showConfirmDialog(null,
							"请选择要删除的学生资料!", null, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		detailCourseInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if (courseTable.getSelectedRow() < 0) {
					int choice = JOptionPane.showConfirmDialog(null,
							"请选择要查看的课程!", null, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
				else{
					courseSelected = courseList.get(courseTable.getSelectedRow());
					controller.requestViewCourseDetail();
				}
			}
		});
		
		editCourseInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if (courseTable.getSelectedRow() < 0) {
					int choice = JOptionPane.showConfirmDialog(null,
							"请选择要编辑的课程!", null, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
				else{
					courseSelected = courseList.get(courseTable.getSelectedRow());
					controller.requestEditCourseInfo();
				}
			}
		});
		
		addCourseInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				controller.requestAddCourse();
			}
		});
		
		delCourseInfoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if (courseTable.getSelectedRow() >= 0) {
					JLabel temp = new JLabel();
					int choice1 = JOptionPane.showConfirmDialog(null,"确定要删除该课程吗？", null, JOptionPane.OK_CANCEL_OPTION);
					if (choice1 == 0) {
						courseSelected = courseList
		                   .get(courseTable.getSelectedRow());
						try {
							controller.requestRemoveCourse();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					int choice1 = JOptionPane.showConfirmDialog(null,
							"请选择要删除的课程!", null, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		getSharedCourseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.getSharedCourses();
			}
		});
	}
	
	private void initializeInfo() throws SQLException{
		studentInfo.removeAll();
		courseInfo.removeAll();
		drawStdTable();
		drawCourseTable();
	}

	private void drawStdTable() throws SQLException {
		//get student information
		stdList = controller.getStdList();
		
		final String[] ColumnName = { "学号", "姓名", "性别", "院系" };
		final int row;

		if (stdList == null || stdList.size() == 0) {
			row = 0;
		} else
			row = stdList.size();
		
		stdTableModel = new AbstractTableModel() {
			public int getRowCount() {
				return row;
			}

			public int getColumnCount() {
				return 4;
			}

			public Object getValueAt(int row, int column) {
				if (stdList != null && stdList.size() != 0) {
					stdSelected = stdList.get(row);
					switch (column) {
					case 0:
						return stdSelected.getStudentId(); // get student number
					case 1:
						return stdSelected.getStudentName(); // get name
					case 2:
						String sex = stdSelected.getGender();
						if(sex==null||sex.equals ("1"))
							return "Male";
						else 
							return "Female"; // get gender
					case 3:
						return stdSelected.getInstitution(); // get college
					}
				}
				return null;
			}
		};

		stdTable = new JTable(stdTableModel);
		for (int column = 0 ; column < 4 ; column++)
			stdTable.getTableHeader().getColumnModel().getColumn(column).setHeaderValue(ColumnName[column]);
		stdTable.setRowHeight(35);
		stdTable.setRowSelectionAllowed(true);
		stdTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		stdTable.setDefaultRenderer(stdTable.getColumnClass(0),
			new TableCellRenderer() {
				public Component getTableCellRendererComponent(
						JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					String text = (value == null) ? "" : value.toString();
					JLabel cell = new JLabel(text);
					cell.setOpaque(true);
						if (isSelected) {
							Font font = cell.getFont();
							cell.setFont(new Font(font.getFamily(),
									Font.BOLD, font.getSize() + 3));
						}
					return cell;
				}
			});
		
		JScrollPane stdScroll = new JScrollPane(stdTable);
		studentInfo.add(stdScroll , BorderLayout.CENTER);
		JPanel stdInfoBtnPanel = new JPanel(new GridLayout(1,8));
		stdInfoBtnPanel.add(new JLabel());
		stdInfoBtnPanel.add(new JLabel());
		stdInfoBtnPanel.add(new JLabel());
		stdInfoBtnPanel.add(new JLabel());
		stdInfoBtnPanel.add(new JLabel());
		stdInfoBtnPanel.add(editStdInfoBtn);
		stdInfoBtnPanel.add(addStdInfoBtn);
		stdInfoBtnPanel.add(delStdInfoBtn);
		studentInfo.add(stdInfoBtnPanel , BorderLayout.SOUTH);
	}

	private void drawCourseTable(){
		final String[] ColumnName = { "课程编号", "课程名称", "授课老师", "学分" , "授课地点"};
		courseList = controller.getCourseList();
		final int row;

		if (courseList == null || courseList.size() == 0) {
			row = 0;
		} else
			row = courseList.size();
		
		courseTableModel = new AbstractTableModel() {
			public int getRowCount() {
				return row;
			}

			public int getColumnCount() {
				return 5;
			}

			public Object getValueAt(int row, int column) {
				if (courseList != null && courseList.size() != 0) {
					courseSelected = courseList.get(row);
					switch (column) {
					case 0:
						return courseSelected.getClassId(); // get student number
					case 1:
						return courseSelected.getClassName(); // get name
					case 2:
						return courseSelected.getTeacher(); // get sex
					case 3:
						return courseSelected.getGrade(); // get college
					case 4:
						return courseSelected.getLocation(); // get college
					}
				
					return null;
				}
				else
					return null;
			}
		};

		courseTable = new JTable(courseTableModel);
		for (int column = 0 ; column < 5 ; column++)
			courseTable.getTableHeader().getColumnModel().getColumn(column).setHeaderValue(ColumnName[column]);
	
		courseTable.setRowHeight(35);
		courseTable.setRowSelectionAllowed(true);
		courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		courseTable.setDefaultRenderer(courseTable.getColumnClass(0),
				new TableCellRenderer() {
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						String text = (value == null) ? "" : value.toString();
						JLabel cell = new JLabel(text);
						cell.setOpaque(true);
					
						if (isSelected) {
							Font font = cell.getFont();
							
							cell.setFont(new Font(font.getFamily(),
									Font.BOLD, font.getSize() + 3));
						}

						return cell;
					}
				});	
		
		JScrollPane courseScroll = new JScrollPane(courseTable);
		courseInfo.add(courseScroll , BorderLayout.CENTER); 
		JPanel courseInfoBtnPanel =new JPanel(new GridLayout(1,8));
		courseInfoBtnPanel.add(new JLabel());
		courseInfoBtnPanel.add(new JLabel());
		courseInfoBtnPanel.add(new JLabel());
		courseInfoBtnPanel.add(new JLabel());
		courseInfoBtnPanel.add(detailCourseInfoBtn);
		courseInfoBtnPanel.add(editCourseInfoBtn);
		courseInfoBtnPanel.add(addCourseInfoBtn);
		courseInfoBtnPanel.add(delCourseInfoBtn);
		courseInfoBtnPanel.add(getSharedCourseBtn);
		courseInfo.add(courseInfoBtnPanel , BorderLayout.SOUTH);
		
	}
	
	public void setController(AdministerViewControllerA controller) {
		this.controller = controller;
	}

	public void show() throws SQLException {
		initializeInfo();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(700, 600);
		Dimension frameSize = frame.getSize();
		frame.setLocation(screenSize.width / 2 - frameSize.width / 2,
				screenSize.height / 2 - frameSize.height / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void repaint() throws SQLException{
		this.initializeInfo();
	}
	
	public StudentA getStdSelected() {
		// TODO Auto-generated method stub
		return stdSelected;
	}

	public CourseA getCourseSelected() {
		// TODO Auto-generated method stub
		return courseSelected;
	}
}
