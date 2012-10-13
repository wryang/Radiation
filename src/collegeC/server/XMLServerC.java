package collegeC.server;

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

import collegeC.bean.CourseC;
import collegeC.bean.StudentC;
import collegeC.bean.ChoiceC;

public class XMLServerC {

	private static XMLServerC xmlServer = null;

	private XMLServerC() {

	}

	public static XMLServerC getInstance() {
		if (xmlServer == null) {
			xmlServer = new XMLServerC();
		}
		return xmlServer;
	}

	// 将数据库中_学生信息_生成xml
	public String changeStudentToXMLFile(List<StudentC> list) {
		String fileURL = null;

		String path = getTempPath();
		Document doc = null;
		doc = DocumentHelper.createDocument();
		String[] columnName = { "Sno", "Snm", "Sex", "Sde" };

		Element root = doc.addElement("Students");
		Iterator<StudentC> it = list.iterator();
		while (it.hasNext()) {
			StudentC stu = (StudentC) it.next();
			Element c = root.addElement("student");
			Element c0 = c.addElement(columnName[0]);
			c0.setText(stu.getSno());
			Element c1 = c.addElement(columnName[1]);
			c1.setText(stu.getSnm());
			Element c2 = c.addElement(columnName[2]);
			c2.setText(stu.getSex());
			Element c3 = c.addElement(columnName[3]);
			c3.setText(stu.getSde());
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
		// String fileURL = null;
		//
		// String path = getTempPath();
		// String rootLabel = "Students";
		// String[] labels = { "Sno", "Snm", "Sex", "Sde" };
		//
		// Map<String, Object> data = new HashMap<String, Object>();
		// Iterator<StudentC> it = list.iterator();
		// while (it.hasNext()) {
		// Map<String, Object> tmp = new HashMap<String, Object>();
		// String label = "student";
		//
		// StudentC stu = (StudentC) it.next();
		// tmp.put(labels[0], stu.getSno());
		// tmp.put(labels[1], stu.getSnm());
		// tmp.put(labels[2], stu.getSex());
		// tmp.put(labels[3], stu.getSde());
		//
		// data.put(label, tmp);
		// }
		// boolean success = buildXMLFile(rootLabel, data, path);
		// if (success) {
		// fileURL = path;
		// }
		// return fileURL;
	}

	public String changeCourseToXMLFile(List<CourseC> list) {
		String fileURL = null;
		try {
			String path = getTempPath();
			Document doc = null;
			String[] columnName = { "Cno", "Cnm", "Pla", "Tec", "Ctm", "Cpt" };
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Classes");
			Iterator<CourseC> it = list.iterator();
			while (it.hasNext()) {
				CourseC course = (CourseC) it.next();
				Element c = root.addElement("class");
				Element c0 = c.addElement(columnName[0]);
				c0.setText(course.getCno());
				Element c1 = c.addElement(columnName[1]);
				c1.setText(course.getCnm());
				Element c2 = c.addElement(columnName[2]);
				c2.setText(course.getPla());
				Element c3 = c.addElement(columnName[3]);
				c3.setText(course.getTec());
				Element c4 = c.addElement(columnName[4]);
				if (null == course.getCtm()) {
					c4.setText("0");
				} else {
					c4.setText(course.getCtm().toString());
				}
				Element c5 = c.addElement(columnName[5]);
				c5.setText(course.getCpt().toString());
			}
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
		// String fileURL = null;
		//
		// String path = getTempPath();
		// String rootLabel = "Classes";
		// String[] labels = { "Cno", "Cnm", "Pla", "Tec", "Cpt", "Ctm" };
		//
		// Map<String, Object> data = new HashMap<String, Object>();
		// Iterator<CourseC> it = list.iterator();
		// while (it.hasNext()) {
		// Map<String, Object> tmp = new HashMap<String, Object>();
		// String label = "class";
		//
		// CourseC course = (CourseC) it.next();
		// tmp.put(labels[0], course.getCno());
		// tmp.put(labels[1], course.getCnm());
		// tmp.put(labels[2], course.getPla());
		// tmp.put(labels[3], course.getTec());
		// tmp.put(labels[4], course.getCpt());
		// tmp.put(labels[5], course.getCtm());
		//
		// data.put(label, tmp);
		// }
		// boolean success = buildXMLFile(rootLabel, data, path);
		// if (success) {
		// fileURL = path;
		// }
		// return fileURL;
	}

	public String changeChoiseToXMLFile(List<ChoiceC> list) {
		String fileURL = null;
		try {
			String path = getTempPath();
			Document doc = null;
			String[] columnName = { "Cno", "Sno", "Grd" };
			doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Choices");
			Iterator<ChoiceC> it = list.iterator();
			while (it.hasNext()) {
				ChoiceC choice = (ChoiceC) it.next();
				Element c = root.addElement("choice");
				Element c0 = c.addElement(columnName[0]);
				c0.setText(choice.getCno());
				Element c1 = c.addElement(columnName[1]);
				c1.setText(choice.getSno());
				Element c2 = c.addElement(columnName[2]);
				c2.setText(choice.getGrd());
			}
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
		// String fileURL = null;
		//
		// String path = getTempPath();
		// String rootLabel = "Choices";
		// String[] labels = { "Cno", "Sno", "Grd" };
		//
		// Map<String, Object> data = new HashMap<String, Object>();
		// Iterator<ChoiceC> it = list.iterator();
		// while (it.hasNext()) {
		// Map<String, Object> tmp = new HashMap<String, Object>();
		// String label = "choice";
		//
		// ChoiceC choice = (ChoiceC) it.next();
		// tmp.put(labels[0], choice.getCno());
		// tmp.put(labels[1], choice.getSno());
		// tmp.put(labels[2], choice.getGrd());
		//
		// data.put(label, tmp);
		// }
		//
		// boolean success = buildXMLFile(rootLabel, data, path);
		// if (success) {
		// fileURL = path;
		// }
		// return fileURL;
	}

	// 将数据库中_选课信息_生成xml
	public String getChoiceData(String studentId, String classId) {
		String fileURL = null;
		Document doc = null;
		String[] columnName = { "Cno", "Sno", "Grd" };
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
	public List<CourseC> readCourseFromXML(String fileName) {
		List<CourseC> list = new ArrayList<CourseC>();
		if (!validate(fileName, "./src/collegeC/schema/classC.xsd")) {
			return list;
		}
		String[] labels = { "Cno", "Cnm", "Ctm", "Cpt", "Tec", "Pla" };
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File(fileName));
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			while (it.hasNext()) {
				Element subRoot = (Element) it.next();
				CourseC course = new CourseC();
				Iterator subIt = subRoot.elementIterator();

				Map<String, String> data = new HashMap<String, String>();
				while (subIt.hasNext()) {
					Element tmp = (Element) subIt.next();
					String name = tmp.getName();
					String text = tmp.getText();
					data.put(name, text);
				}

				if (data.size() == labels.length) {
					course.setCno(data.get(labels[0]));
					course.setCnm(data.get(labels[1]));
					int tm = 0;
					if (data.get(labels[2]).equals("")) {
						tm = 0;
					} else {
						tm = new Integer(data.get(labels[2]));
					}
					course.setCtm(tm);
					course.setCpt(new Integer(data.get(labels[3])));
					course.setTec(data.get(labels[4]));
					course.setPla(data.get(labels[5]));
					course.setShare(CourseC.SHARED);

					list.add(course);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 读取集成服务器传来的 学生 xml文件，将其转化为标准的SQL语句传出
	@SuppressWarnings("rawtypes")
	public List<StudentC> readStudentFromXML(String fileName) {
		List<StudentC> list = new ArrayList<StudentC>();
		if (!validate(fileName, "./src/collegeC/schema/studentC.xsd")) {
			return list;
		}
		String[] labels = { "Sno", "Snm", "Sex", "Sde" };
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
					StudentC student = new StudentC();

					student.setSno(data.get(labels[0]));
					student.setSnm(data.get(labels[1]));
					student.setSex(data.get(labels[2]));
					student.setSde(data.get(labels[3]));

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
	public List<ChoiceC> readCourseChoiceFromXML(String fileName) {
		List<ChoiceC> list = new ArrayList<ChoiceC>();
		if (!validate(fileName, "./src/collegeC/schema/choiceC.xsd")) {
			return list;
		}
		String[] labels = { "Cno", "Sno", "Grd" };
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
					ChoiceC choice = new ChoiceC();
					choice.setCno(data.get(labels[0]));
					choice.setSno(data.get(labels[1]));
					choice.setGrd(data.get(labels[2]));
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

	// private boolean buildXMLFile(String rootLabel, Map<String, Object> data,
	// String path) {
	// boolean result = false;
	// Document doc = DocumentHelper.createDocument();
	// Element root = doc.addElement(rootLabel);
	// Writer w = null;
	// buildStructure(root, data, path);
	// try {
	// w = new FileWriter(path);
	// OutputFormat opf = OutputFormat.createPrettyPrint();
	// opf.setEncoding("GB2312");
	// XMLWriter xw = new XMLWriter(w, opf);
	// xw.write(doc);
	// xw.close();
	// w.close();
	// result = true;
	// } catch (IOException e) {
	// e.printStackTrace();
	// result = false;
	// }
	// return result;
	// }

	// @SuppressWarnings("unchecked")
	// private void buildStructure(Element root, Map<String, Object> data,
	// String path) {
	// Set<String> labels = data.keySet();
	// Iterator<String> it = labels.iterator();
	// while (it.hasNext()) {
	// String label = (String) it.next();
	// Object datum = data.get(label);
	// if (datum instanceof Map) {
	// Map<String, Object> subMap = (Map<String, Object>) datum;
	// Element subRoot = root.addElement(label);
	// buildStructure(subRoot, subMap, path);
	// } else {
	// Element subElement = root.addElement(label);
	// subElement.setText(datum.toString());
	// }
	// }
	// }

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
		XMLServerC xmlServer = XMLServerC.getInstance();
		// StudentC stu = new StudentC();
		// stu.setSnm("杨宜杰");
		// stu.setSex(StudentC.MALE);
		// stu.setSde("sadfasd");
		// stu.setSno("091250197");
		// List<StudentC> list = new ArrayList<StudentC>();
		// list.add(stu);
		// String filename = xmlServer.changeStudentToXMLFile(list);
		// List<StudentC> l = xmlServer.readStudentFromXML(filename);
		// if(null != l && l.size() != 0) {
		// StudentC s = l.get(0);
		// System.out.println(s.getSnm());
		// }else {
		// System.out.println("failure");
		// }

		CourseC course = new CourseC();
		course.setPla("软院");
		course.setCnm("sdfs");
		course.setCno("091250193");
		course.setCtm(12);
		course.setCpt(3);
		course.setShare(CourseC.NON_SHARED);
		course.setTec("justin");
		List<CourseC> list = new ArrayList<CourseC>();
		list.add(course);
		String filename = xmlServer.changeCourseToXMLFile(list);
		List<CourseC> l = xmlServer.readCourseFromXML(filename);
		if (null != l && l.size() != 0) {
			CourseC c = (CourseC) l.get(0);
			System.out.println(c.getPla());
		} else {
			System.out.println("failure");
		}

		// ChoiceC choice = new ChoiceC();
		// choice.setCno("111");
		// choice.setSno("091250197");
		// choice.setGrd("12");
		// List<ChoiceC> list = new ArrayList<ChoiceC>();
		// list.add(choice);
		// String filename = xmlServer.changeChoiseToXMLFile(list);
		// List<ChoiceC> l = xmlServer.readCourseChoiceFromXML(filename);
		// if (null != l && l.size() != 0) {
		// ChoiceC c = (ChoiceC) l.get(0);
		// System.out.println(c.getSno());
		// } else {
		// System.out.println("failure");
		// }
	}
}
