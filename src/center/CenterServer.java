package center;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.io.OutputFormat;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

public class CenterServer {
	private static CenterServer networkServer = null;

	Thread tread = null;

	private static final int REQUEST_COURSE_INTO = 0;
	private static final int COURSE_CHOICE = 1;
	private static final int VAIN_COURSE_CHOICE = 2;

	// to indicate the result of course choise
	private static int SUCCESS = 1;
	private static int FAILURE = 0;

	private CenterServer() {
		tread = new Thread(new ListenServer(this));
	}

	public void start() {
		tread.start();
	}
	public static CenterServer getInstance() {
		if (null == networkServer) {
			networkServer = new CenterServer();
			return networkServer;
		} else {
			return networkServer;
		}
	}

	public void transferVAinCourseChoice(Socket socket, String college, String courseId)
			{
		String choiceFile = null;
		try {
			boolean result = false;
			DataOutputStream opos = new DataOutputStream(
					socket.getOutputStream());

			String choiceXSD = Assistance.getRelativeChoiceXSD(college);
			choiceFile = receiveXML(socket);

			if (null != choiceFile && validate(choiceXSD, choiceFile)) {
				result = transferVainCourseChoiceXML(choiceFile, courseId);
			} else {
				result = false;
			}

			if (result) {
				opos.writeInt(SUCCESS);
			} else {
				opos.writeInt(FAILURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != choiceFile) {
				File file = new File(choiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
		}
	}

	private boolean transferVainCourseChoiceXML(
			String choiceFile, String courseId){
		boolean result = false;
		String formatChoiceFile = null;
		String specificChoiceFile = null;
		try {
			String toServerIp = Assistance
					.getServerIpFromPrefixOfCourseId(courseId);
			int toServerPort = Assistance
					.getServerPortFromPrefixOfCourseId(courseId);

			if (null == toServerIp || toServerPort == 0) {
				return false;
			}

			Socket socket = createSocket(toServerIp, toServerPort);
			DataOutputStream tpos = new DataOutputStream(
					socket.getOutputStream());
			tpos.writeInt(VAIN_COURSE_CHOICE);
			tpos.flush();
			
			String choiceXSL = Assistance.getRelativeChoiceXSLFromServer(
					toServerIp, toServerPort);
			String formatChoiceXSL = Assistance.getFormatClassChoiceXSL();

			formatChoiceFile = formatXML(formatChoiceXSL, choiceFile);
			specificChoiceFile = formatXML(choiceXSL, formatChoiceFile);
			result = sendXML(socket, specificChoiceFile);
			DataInputStream tpis = new DataInputStream(
					socket.getInputStream());
			int success = tpis.readInt();
			if(SUCCESS == success) {
				result = true;
			}else {
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != specificChoiceFile) {
				File file = new File(specificChoiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
			if(null != formatChoiceFile) {
				File file = new File(formatChoiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
		}
		return result;
	}

	public void transferCourseChoiceFrom(Socket socket, String college,
			String courseId) {
		String choiceFile = null;
		String studentInfoFile = null;
		try {
			boolean result = false;
			DataOutputStream opos = new DataOutputStream(
					socket.getOutputStream());

			String choiceXSD = Assistance.getRelativeChoiceXSD(college);
			String studentXSD = Assistance.getRelativeStudentXSD(college);

			choiceFile = receiveXML(socket);
			opos.writeInt(SUCCESS);
			opos.flush();
			studentInfoFile = receiveXML(socket);

			String toServerIp = Assistance
					.getServerIpFromPrefixOfCourseId(courseId);
			int toServerPort = Assistance
					.getServerPortFromPrefixOfCourseId(courseId);

			if (null != choiceFile && null != studentInfoFile
					&& validate(choiceXSD, choiceFile)
					&& validate(studentXSD, studentInfoFile)) {
				result = transferCourseChoiceAndStudentInfoXML(socket,
						toServerIp, toServerPort, choiceFile, studentInfoFile);
			} else {
				result = false;
			}

			if (result) {
				opos.writeInt(SUCCESS);
			} else {
				opos.writeInt(FAILURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != choiceFile) {
				File file = new File(choiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
			if(null != studentInfoFile) {
				File file = new File(studentInfoFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
		}
	}

	private boolean transferCourseChoiceAndStudentInfoXML(
			Socket originalSocket, String toServerIp, int toServerPort,
			String choiceFile, String studentInfoFile) {
		boolean result = false;
		String formatChoiceFile = null;
		String specificChoiceFile = null;
		String formatStudentInfoFile = null;
		String specificStudentInfoFile = null;
		try {
			if (null == toServerIp || toServerPort == 0) {
				return false;
			}

			Socket socket = createSocket(toServerIp, toServerPort);

			DataOutputStream tpos = new DataOutputStream(
					socket.getOutputStream());
			tpos.writeInt(COURSE_CHOICE);
			tpos.flush();

			String choiceXSL = Assistance.getRelativeChoiceXSLFromServer(
					toServerIp, toServerPort);
			String studentXSL = Assistance.getRelativeStudentXSLFromServer(
					toServerIp, toServerPort);
			String formatChoiceXSL = Assistance.getFormatClassChoiceXSL();

			formatChoiceFile = formatXML(formatChoiceXSL, choiceFile);
			specificChoiceFile = formatXML(choiceXSL, formatChoiceFile);
			sendXML(socket, specificChoiceFile);

			DataInputStream tpis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			int state = tpis.readInt();
			if (SUCCESS == state) {
				String formatStudentXSL = Assistance.getFormatStudentXSL();
				
				formatStudentInfoFile = formatXML(formatStudentXSL, studentInfoFile);
				specificStudentInfoFile = formatXML(studentXSL, formatStudentInfoFile);
				boolean success = sendXML(socket, specificStudentInfoFile);
				if (success) {
					result = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != formatChoiceFile) {
				File file = new File(formatChoiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
			if(null != specificChoiceFile) {
				File file = new File(specificChoiceFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
			if(null != specificStudentInfoFile) {
				File file = new File(specificStudentInfoFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
			if(null != formatStudentInfoFile) {
				File file = new File(formatStudentInfoFile);
				if(null != file && file.exists()) {
					file.delete();
				}
			}
		}
		return result;
	}

	public void askSharedCouseInfoFor(Socket socket, String college) {
		if (null == socket || null == college) {
			return;
		}
		String classXSL = Assistance.getRelativeClassXSL(college);

		List<ServerInfo> list = Assistance.getExcluseCollegeIpAndPort(college);
		List<String> fileList = new ArrayList<String>();
		Iterator<ServerInfo> it = list.iterator();
		while (it.hasNext()) {
			ServerInfo info = (ServerInfo) it.next();
			String tmp = askForSharedCourseInfo(socket, info.serverIp,
					info.serverPort, classXSL);
			fileList.add(tmp);
		}

		int size = fileList.size();
		DataOutputStream os;
		DataInputStream is;
		try {
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
			os.writeInt(size);
			os.flush();
			Iterator<String> fit = fileList.iterator();
			while (fit.hasNext()) {
				String file = (String) fit.next();
				if (sendXML(socket, file)) {
					boolean readyForNext = is.readBoolean();
					if (readyForNext) {
						continue;
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			Iterator<String> fit = fileList.iterator();
			while(fit.hasNext()) {
				String filename = (String)fit.next();
				File tmp = new File(filename);
				if (null != tmp && tmp.exists()) {
					tmp.delete();
				}
			}
		}
		try {
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean validate(String xsd, String fileURL) {
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
							xsd);

			XMLErrorHandler errorHandler = new XMLErrorHandler();
			saxReader.setErrorHandler(errorHandler);
			File file = new File(fileURL);
			saxReader.read(new FileReader(file));
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

	private String formatXML(String xsl, String fileURL) {
		if (null == xsl || null == fileURL) {
			return null;
		}
		String outputFileName = null;
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			String path = getTempPath();
			document = saxReader.read(new FileReader(new File(fileURL)));
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					xsl));
			DocumentSource source = new DocumentSource(document);
			DocumentResult result = new DocumentResult();
			transformer.transform(source, result);
			Document transformedDoc = result.getDocument();
			Writer w = new FileWriter(path);
			OutputFormat opf = OutputFormat.createPrettyPrint();
			opf.setEncoding("GB2312");
			XMLWriter xw = new XMLWriter(w, opf);
			xw.write(transformedDoc);
			xw.close();
			w.close();
			outputFileName = path;
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFileName;
	}

	private String askForSharedCourseInfo(Socket originalSocket,
			String serverIp, int serverPort, String xsl) {
		String specificFileURL = null;
		Socket socket = createSocket(serverIp, serverPort);
		if (null == socket) {
			return null;
		}
		try {
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			ps.writeInt(REQUEST_COURSE_INTO);
			ps.flush();
			String tempFileURL = receiveXML(socket);
			socket.close();

			String classXSD = Assistance.getRelativeClassXSDFromServer(
					serverIp, serverPort);

			if (null != tempFileURL && validate(classXSD, tempFileURL)) {
				String formatClassXSL = Assistance.getFormatClassXSL();
				String formatFileURL = formatXML(formatClassXSL, tempFileURL);
				specificFileURL = formatXML(xsl, formatFileURL);
				File file = new File(tempFileURL);
				if (null != file && file.exists()) {
					file.delete();
				}
				file = new File(formatFileURL);
				if (null != file && file.exists()) {
					file.delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return specificFileURL;
	}

	private boolean sendXML(Socket socket, String fileURL) {
		boolean result = false;
		File file = new File(fileURL);

		System.out.println("文件长度为:" + (int) file.length());
		DataInputStream fileStream;
		try {
			fileStream = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileURL)));
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			ps.writeLong((long) file.length());
			ps.flush();

			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];

			while (true) {
				int read = 0;
				if (fileStream != null) {
					read = fileStream.read(buf);
				}

				if (read == -1) {
					break;
				}
				ps.write(buf, 0, read);
			}
			ps.flush();
			fileStream.close();
			System.out.println("文件发送成功");
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String receiveXML(Socket socket) {
		DataInputStream inputStream = null;
		String fileURL = null;
		try {
			inputStream = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
		} catch (Exception e) {
			System.out.print("文件接收失败\n");
		}

		try {
			String savePath = getTempPath();
			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];
			int passedlen = 0;
			long len = 0;

			DataOutputStream fileOut = new DataOutputStream(
					new BufferedOutputStream(new BufferedOutputStream(
							new FileOutputStream(savePath))));
			len = inputStream.readLong();

			System.out.println("文件长度:" + len + "\n");
			System.out.println("开始接收文件!" + "\n");

			while (true) {
				int read = 0;
				if (inputStream != null) {
					read = inputStream.read(buf);
				}
				passedlen += read;
				if (read == -1) {
					break;
				}
				System.out.println("已经接收" + (passedlen * 100 / len) + "%\n");
				fileOut.write(buf, 0, read);
				if (passedlen == len) {
					break;
				}
			}
			System.out.println("已保存到Ϊ" + savePath + "\n");

			fileOut.close();
			fileURL = savePath;
		} catch (Exception e) {
			System.out.println("文件接收失败" + "\n");
		}
		return fileURL;
	}

	private Socket createSocket(String ip, int port) {
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return socket;
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

	public static void main(String[] args) {
		// XMLServerB xmlServer = XMLServerB.getInstance();
		// NetworkServer server = NetworkServer.getInstance();
		// StudentB stu = new StudentB();
		// stu.setName("���˽�");
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
		// course.setAddress("��Ժ");
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

		// ChoiceB choice = new ChoiceB();
		// choice.setCourseId("111");
		// choice.setStudentNumber("091250197");
		// choice.setGrade("12");
		// List<ChoiceB> list = new ArrayList<ChoiceB>();
		// list.add(choice);
		// String filename = xmlServer.changeChoiseToXMLFile(list);
		// System.out.println(filename);
		// boolean s = server.validate("./src/collegeA/schema/choiceA.xsd",
		// "D:/outputChoiceA.xml");
		// System.out.println(s);
		// String fileURL =
		// server.formatXML(Assistance.getRelativePath(formatChoiceXSL),
		// filename);
		// System.out.println(fileURL);

		CenterServer cenServer = CenterServer.getInstance();
		cenServer.start();
	}
}
