package collegeA.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

import collegeA.bean.ChoiceA;
import collegeA.bean.CourseA;
import collegeA.bean.StudentA;

/*
 * -生成本院数据库相关xml文件
 * -读取集成服务器传来的xml文件，并解析成sql语句传递给ServerA
 */
public class XMLServerA {
	private static XMLServerA xmlServerA = null;

	private XMLServerA() {

	}

	public static XMLServerA getInstance() {
		if (xmlServerA == null) {
			xmlServerA = new XMLServerA();
		}
		return xmlServerA;
	}

	// 将数据库中_学生信息_生成xml
	public String getStudentData(ResultSet rsStudent) {
		String fileURL = null;
		try {
			Document doc = null;
			String[] columnName = { "studentId", "name", "gender",
					"institution" };
			int count = columnName.length;
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Students");
			while (rsStudent.next()) {
				Element student = root.addElement("student");
				for (int i = 1; i <= count; i++) {
					Element column = student.addElement(columnName[i - 1]);
					if (rsStudent.getObject(i) != null) {
						column.setText(rsStudent.getObject(i) + "");
					} else {
						column.setText(" ");
					}
				}
			}
			String path = getTempPath();
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(doc);
			xw.close();
			w.close();
			fileURL = path;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileURL;
	}

	// 将数据库中_课程信息_生成xml
	public String getCourseData(ResultSet rsClass) {
		String fileURL = null;
		try {
			String path = getTempPath();
			ResultSetMetaData rsmdClass = rsClass.getMetaData();
			int count = rsmdClass.getColumnCount();
			Document doc = null;
			String[] columnName = { "id", "className", "point", "tea",
					"place" };
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Classes");
			while (rsClass.next()) {
				Element classes = root.addElement("class");
				for (int i = 1; i < count; i++) {
					Element column = classes.addElement(columnName[i - 1]);
					if (rsClass.getObject(i) != null) {
						column.setText(rsClass.getObject(i) + " ");
					} else {
						column.setText(" ");
					}
				}
			}
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(doc);
			xw.close();
			w.close();
			fileURL = path;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileURL;
	}

	// 将数据库中_选课信息_生成xml
	public String getChoiceData(String studentId, String classId) {
		String fileURL = null;
		Document doc = null;
		String[] columnName = { "classId", "studentId", "score" };
		doc = DocumentHelper.createDocument();

		Element root = doc.addElement("Choices");
		Element choice = root.addElement("choice");
		Element column0 = choice.addElement(columnName[0]);
		column0.setText(classId);
		Element column1 = choice.addElement(columnName[1]);
		column1.setText(studentId);
		Element column2 = choice.addElement(columnName[2]);
		column2.setText(" ");

		try {
			String path = getTempPath();
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(doc);
			xw.close();
			w.close();
			fileURL = path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileURL;
	}

	// 验证XML格式是否正确
	public static boolean validate(String fileName, String format) {
		boolean result = false;
		SAXReader saxReader = new SAXReader();
		saxReader.setValidation(true);
		try {
			saxReader
					.setFeature("http://xml.org/sax/features/validation", true);
			saxReader.setFeature(
					"http://apache.org/xml/features/validation/schema", true);
			saxReader
					.setProperty(
							"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
							format);

			XMLErrorHandler errorHandler = new XMLErrorHandler();
			saxReader.setErrorHandler(errorHandler);
			saxReader.read(new FileReader(new File(fileName)));
			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
			if (errorHandler.getErrors().hasContent()) {
				writer.write(errorHandler.getErrors());
				System.out.println("format not correct");
				result = false;
			} else {
				System.out.println("Validate success");
				result = true;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 读取集成服务器传来的 课程 xml文件，将其转化为标准的SQL语句传出
	@SuppressWarnings("rawtypes")
	public List<CourseA> readCourseFromXML(String fileName) {
		List<CourseA> list = new ArrayList<CourseA>();
		if (!validate(fileName, "./src/collegeA/schema/classA.xsd")) {
			return null;
		}
		String[] labels = { "id", "className", "point", "tea", "place" };
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new FileReader(new File(fileName)));
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element subRoot = (Element) it.next();
				Iterator subIt = subRoot.elementIterator();

				Map<String, String> data = new HashMap<String, String>();
				while (subIt.hasNext()) {
					Element tmp = (Element) subIt.next();
					String name = tmp.getName();
					String text = tmp.getText();
					data.put(name, text);
				}

				if (data.size() == labels.length) {
					CourseA course = new CourseA();
					course.setClassId(data.get(labels[0]));
					course.setClassName(data.get(labels[1]));
					course.setGrade(data.get(labels[2]));
					course.setTeacher(data.get(labels[3]));
					course.setLocation(data.get(labels[4]));
					course.setShare(CourseA.SHARED);

					list.add(course);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 读取集成服务器传来的 学生 xml文件，将其转化为标准的SQL语句传出
	@SuppressWarnings("rawtypes")
	public List<StudentA> readStudentFromXML(String fileName) {
		List<StudentA> list = new ArrayList<StudentA>();
		if (!validate(fileName, "./src/collegeA/schema/studentA.xsd")) {
			return null;
		}
		String[] labels = { "studentId", "name", "gender", "institution" };
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new FileReader(new File(fileName)));
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element subRoot = (Element) it.next();
				Iterator subIt = subRoot.elementIterator();

				Map<String, String> data = new HashMap<String, String>();
				while (subIt.hasNext()) {
					Element tmp = (Element) subIt.next();
					String name = tmp.getName();
					String text = tmp.getText();
					data.put(name, text);
				}

				if (data.size() == labels.length) {
					StudentA student = new StudentA();

					student.setStudentId(data.get(labels[0]));
					student.setStudentName(data.get(labels[1]));
					student.setGender(data.get(labels[2]));
					student.setInstitution(data.get(labels[3]));

					list.add(student);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 读取集成服务器传来的 选课 xml文件，将其转化为标准的SQL语句传出
	@SuppressWarnings("rawtypes")
	public List<ChoiceA> readCourseChoiceFromXML(String fileName) {
		List<ChoiceA> list = new ArrayList<ChoiceA>();
		if (!validate(fileName, "./src/collegeA/schema/choiceA.xsd")) {
			return list;
		}
		String[] labels = { "classId", "studentId", "score" };
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new FileReader(new File(fileName)));
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element subRoot = (Element) it.next();
				Iterator subIt = subRoot.elementIterator();

				Map<String, String> data = new HashMap<String, String>();
				while (subIt.hasNext()) {
					Element tmp = (Element) subIt.next();
					String name = tmp.getName();
					String text = tmp.getText();
					data.put(name, text);
				}

				if (data.size() == labels.length) {
					ChoiceA choice = new ChoiceA();
					choice.setCourseId(data.get(labels[0]));
					choice.setStudentNumber(data.get(labels[1]));
					choice.setGrade(data.get(labels[2]));

					list.add(choice);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getTempPath() {
		Random r = new Random();
		int randomInt = r.nextInt();
		Calendar cal = Calendar.getInstance();
		return "E://" + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
				+ "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR)
				+ "-" + cal.get(Calendar.MINUTE) + "-"
				+ cal.get(Calendar.SECOND) + "-"
				+ cal.get(Calendar.MILLISECOND) + randomInt + ".xml";
	}

	public static void main(String[] args) throws Exception {
		XMLServerA xmlServer = XMLServerA.getInstance();
		// StudentB stu = new StudentB();
		// stu.setName("杨宜杰");
		// stu.setSex(StudentB.MALE);
		// stu.setSpeciality("sadfasd");
		// stu.setStudentNumber("091250197");
		// List<StudentB> list = new ArrayList<StudentB>();
		// list.add(stu);
		// xmlServer.changeStudentToXMLFile(list);
		// CourseB course = new CourseB();
		// course.setAddress("软院");
		// course.setCourseName("sdfs");
		// course.setId("091250193");
		// course.setPeriod("12");
		// course.setPoint("3");
		// course.setShared(CourseB.NON_SHARED);
		// course.setTeacher("justin");
		// List<CourseB> list = new ArrayList<CourseB>();
		// list.add(course);
		// xmlServer.changeCourseToXMLFile(list);
		xmlServer.readCourseFromXML("d://outputClassesA.xml");
	}
}
