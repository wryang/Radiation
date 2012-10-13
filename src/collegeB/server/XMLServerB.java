package collegeB.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

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

import collegeB.bean.CourseB;
import collegeB.bean.StudentB;
import collegeB.bean.ChoiceB;

public class XMLServerB {

	private static XMLServerB xmlServerA = null;

	private XMLServerB() {

	}

	public static XMLServerB getInstance() {
		if (xmlServerA == null) {
			xmlServerA = new XMLServerB();
		}
		return xmlServerA;
	}

	// 将数据库中_学生信息_生成xml
	public String changeStudentToXMLFile(List<StudentB> list) {
		String fileURL = null;

		String path = getTempPath();
		Document doc = null;
		doc = DocumentHelper.createDocument();
		String[] columnName = { "studentNumber", "name", "sex", "speciality" };

		Element root = doc.addElement("Students");
		Iterator<StudentB> it = list.iterator();
		while (it.hasNext()) {
			StudentB stu = (StudentB)it.next();
			Element c = root.addElement("student");
			Element c0 = c.addElement(columnName[0]);
			c0.setText(stu.getStudentNumber());
			Element c1 = c.addElement(columnName[1]);
			c1.setText(stu.getName());
			Element c2 = c.addElement(columnName[2]);
			c2.setText(stu.getSex());
			Element c3 = c.addElement(columnName[3]);
			c3.setText(stu.getSpeciality());
		}
		Writer w;
		try {
			w = new FileWriter(path);
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

	public String changeCourseToXMLFile(List<CourseB> list) {
		String fileURL = null;
		try {
			String path = getTempPath();
			Document doc = null;
			String[] columnName = { "ID", "courseName", "place", "teacher", "time",
					"point"  };
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Classes");
			Iterator<CourseB> it = list.iterator();
			while (it.hasNext()) {
				CourseB course = (CourseB)it.next();
				Element c = root.addElement("class");
				Element c0 = c.addElement(columnName[0]);
				c0.setText(course.getId());
				Element c1 = c.addElement(columnName[1]);
				c1.setText(course.getCourseName());
				Element c2 = c.addElement(columnName[2]);
				c2.setText(course.getAddress());
				Element c3 = c.addElement(columnName[3]);
				c3.setText(course.getTeacher());
				Element c4 = c.addElement(columnName[4]);
				if(null == course.getPeriod()) {
					c4.setText("0");
				}else {
					c4.setText(course.getPeriod());
				}
				Element c5 = c.addElement(columnName[5]);
				c5.setText(course.getPoint());
			}
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(doc);
			xw.close();
			w.close();
			fileURL = path;
		}  catch (IOException e) {
			e.printStackTrace();
		}
		return fileURL;
//		String fileURL = null;
//
//		String path = getTempPath();
//		String rootLabel = "Classes";
//		String[] labels = { "ID", "courseName", "place", "teacher", "time",
//				"point" };
//
//		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//		Iterator<CourseB> it = list.iterator();
//		while (it.hasNext()) {
//			Map<String, Object> tmp = new HashMap<String, Object>();
//			String label = "class";
//			CourseB course = (CourseB) it.next();
//			tmp.put(labels[0], course.getId());
//			tmp.put(labels[1], course.getCourseName());
//			tmp.put(labels[2], course.getAddress());
//			tmp.put(labels[3], course.getTeacher());
//			tmp.put(labels[4], course.getPeriod());
//			tmp.put(labels[5], course.getPoint());
//
//			data.put(label, tmp);
//		}
//		boolean success = buildXMLFile(rootLabel, data, path);
//		if (success) {
//			fileURL = path;
//		}
//		return fileURL;
	}

	public String changeChoiseToXMLFile(List<ChoiceB> list) {
		String fileURL = null;
		try {
			String path = getTempPath();
			Document doc = null;
			String[] columnName = { "courseId", "studentNumber", "score"  };
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Choices");
			Iterator<ChoiceB> it = list.iterator();
			while (it.hasNext()) {
				ChoiceB choice = (ChoiceB)it.next();
				Element c = root.addElement("choice");
				Element c0 = c.addElement(columnName[0]);
				c0.setText(choice.getCourseId());
				Element c1 = c.addElement(columnName[1]);
				c1.setText(choice.getStudentNumber());
				Element c2 = c.addElement(columnName[2]);
				c2.setText(choice.getGrade());
			}
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(doc);
			xw.close();
			w.close();
			fileURL = path;
		}  catch (IOException e) {
			e.printStackTrace();
		}
		return fileURL;
	}

	// 将数据库中_选课信息_生成xml
	public String getChoiceData(String studentId, String classId) {
		String fileURL = null;
		Document doc = null;
		String[] columnName = { "courseId", "studentNumber", "score" };
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

	// 读取集成服务器传来的 课程 xml文件，将其转化为标准的SQL语句传出
	@SuppressWarnings("rawtypes")
	public List<CourseB> readCourseFromXML(String fileName) {
		List<CourseB> list = new ArrayList<CourseB>();
		if (!validate(fileName, "./src/collegeB/schema/classB.xsd")) {
			return list;
		}
		String[] labels = { "ID", "courseName", "time", "point", "teacher",
				"place" };
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
					CourseB course = new CourseB();
					course.setId(data.get(labels[0]));
					course.setCourseName(data.get(labels[1]));
					course.setPeriod(data.get(labels[2]));
					course.setPoint(data.get(labels[3]));
					course.setTeacher(data.get(labels[4]));
					course.setAddress(data.get(labels[5]));
					course.setShared(CourseB.SHARED);

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
	public List<StudentB> readStudentFromXML(String fileName) {
		List<StudentB> list = new ArrayList<StudentB>();
		if (!validate(fileName, "./src/collegeB/schema/studentB.xsd")) {
			return list;
		}
		String[] labels = { "studentNumber", "name", "sex", "speciality" };
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
					StudentB student = new StudentB();

					student.setStudentNumber(data.get(labels[0]));
					student.setName(data.get(labels[1]));
					student.setSex(data.get(labels[2]));
					student.setSpeciality(data.get(labels[3]));

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
	public List<ChoiceB> readCourseChoiceFromXML(String fileName) {
		List<ChoiceB> list = new ArrayList<ChoiceB>();
		if (!validate(fileName, "./src/collegeB/schema/choiceB.xsd")) {
			return list;
		}
		String[] labels = { "courseId", "studentNumber", "score" };
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
					ChoiceB choice = new ChoiceB();
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

	// 验证XML格式是否正确
	private static boolean validate(String fileName, String format) {
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

//	private boolean buildXMLFile(String rootLabel, Map<String, Object> data,
//			String path) {
//		boolean result = false;
//		Document doc = DocumentHelper.createDocument();
//		Element root = doc.addElement(rootLabel);
//		Writer w = null;
//		buildStructure(root, data, path);
//		try {
//			w = new FileWriter(path);
//			OutputFormat opf = OutputFormat.createPrettyPrint();
//			opf.setEncoding("GB2312");
//			XMLWriter xw = new XMLWriter(w, opf);
//			xw.write(doc);
//			xw.close();
//			w.close();
//			result = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//			result = false;
//		}
//		return result;
//	}

//	@SuppressWarnings("unchecked")
//	private void buildStructure(Element root, Map<String, Object> data,
//			String path) {
//		Set<String> labels = data.keySet();
//		Iterator<String> it = labels.iterator();
//		while (it.hasNext()) {
//			String label = (String) it.next();
//			Object datum = data.get(label);
//			if (datum instanceof Map) {
//				Map<String, Object> subMap = (Map<String, Object>) datum;
//				Element subRoot = root.addElement(label);
//				buildStructure(subRoot, subMap, path);
//			} else {
//				Element subElement = root.addElement(label);
//				subElement.setText(datum.toString());
//			}
//		}
//	}

	private String getTempPath() {
		Random r = new Random();
		int randomInt = r.nextInt();
		Calendar cal = Calendar.getInstance();
		return "E://" + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
				+ "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.HOUR)
				+ "-" + cal.get(Calendar.MINUTE) + "-"
				+ cal.get(Calendar.SECOND) + randomInt + ".xml";
	}

	public static void main(String[] args) {
		XMLServerB xmlServer = XMLServerB.getInstance();
		// StudentB stu = new StudentB();
		// stu.setName("杨宜杰");
		// stu.setSex(StudentB.MALE);
		// stu.setSpeciality("sadfasd");
		// stu.setStudentNumber("091250197");
		// List<StudentB> list = new ArrayList<StudentB>();
		// list.add(stu);
		// String filename = xmlServer.changeStudentToXMLFile(list);
		// List<StudentB> l = xmlServer.readStudentFromXML(filename);
		// if(null != l && l.size() != 0) {
		// StudentB s = l.get(0);
		// System.out.println(s.getName());
		// }else {
		// System.out.println("failure");
		// }

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
		// String filename = xmlServer.changeCourseToXMLFile(list);
		// List<CourseB> l = xmlServer.readCourseFromXML(filename);
		// if(null != l && l.size() != 0) {
		// CourseB c = (CourseB)l.get(0);
		// System.out.println(c.getAddress());
		// }else {
		// System.out.println("failure");
		// }

		ChoiceB choice = new ChoiceB();
		choice.setCourseId("111");
		choice.setStudentNumber("091250197");
		choice.setGrade("12");
		List<ChoiceB> list = new ArrayList<ChoiceB>();
		list.add(choice);
		String filename = xmlServer.changeChoiseToXMLFile(list);
		List<ChoiceB> l = xmlServer.readCourseChoiceFromXML(filename);
		if (null != l && l.size() != 0) {
			ChoiceB c = (ChoiceB) l.get(0);
			System.out.println(c.getStudentNumber());
		} else {
			System.out.println("failure");
		}
	}
}
