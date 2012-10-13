package center;

import java.util.ArrayList;
import java.util.List;

public class Assistance {
	
	private static String serverAIp = "127.0.0.1"; //192.168.47.83 yyz
	private static int serverAPort = 8820;
	private static String serverBIp = "192.168.47.41"; //192.168.47.41 weibao
	private static int serverBPort = 8830;
	private static String serverCIp = "192.168.47.57"; // 192.168.47.57 xiaoran
	private static int serverCPort = 8840;

	private static String prefixOfcourseA = "1";
	private static String prefixOfcourseB = "2";
	private static String prefixOfcourseC = "3";

	private static String collegeA = "a";
	private static String collegeB = "b";
	private static String collegeC = "c";

	public static String classAXSL = "classToA.xsl";
	public static String classBXSL = "classToB.xsl";
	public static String classCXSL = "classToC.xsl";

	public static String studentAXSL = "studentToA.xsl";
	public static String studentBXSL = "studentToB.xsl";
	public static String studentCXSL = "studentToC.xsl";

	public static String choiceAXSL = "choiceToA.xsl";
	public static String choiceBXSL = "choiceToB.xsl";
	public static String choiceCXSL = "choiceToC.xsl";

	public static String classAXSD = "classA.xsd";
	public static String classBXSD = "classB.xsd";
	public static String classCXSD = "classC.xsd";

	public static String studentAXSD = "studentA.xsd";
	public static String studentBXSD = "studentB.xsd";
	public static String studentCXSD = "studentC.xsd";

	public static String choiceAXSD = "choiceA.xsd";
	public static String choiceBXSD = "choiceB.xsd";
	public static String choiceCXSD = "choiceC.xsd";

	public static String formatClassXSD = "formatClass.xsd";
	public static String formatStudentXSD = "formatStudent.xsd";
	public static String formatClassChoiceXSD = "formatClassChoice.xsd";

	public static String formatClassXSL = "formatClass.xsl";
	public static String formatStudentXSL = "formatStudent.xsl";
	public static String formatClassChoiceXSL = "formatClassChoice.xsl";
	
	public static String getRelativeClassXSLFromServer(String serverIp, int port) {
		String classXSL = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			classXSL = classAXSL;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			classXSL = classBXSL;
		} else if (serverIp.equals(serverCIp) && port == serverBPort) {
			classXSL = classCXSL;
		}
		return getRelativeXSLPath(classXSL);
	}
	
	public static String getRelativeStudentXSLFromServer(String serverIp, int port) {
		String studentXSL = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			studentXSL = studentAXSL;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			studentXSL = studentBXSL;
		} else if (serverIp.equals(serverCIp) && port == serverCPort) {
			studentXSL = studentCXSL;
		}
		return getRelativeXSLPath(studentXSL);
	}
	
	public static String getRelativeChoiceXSLFromServer(String serverIp, int port) {
		String choiceXSL = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			choiceXSL = choiceAXSL;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			choiceXSL = choiceBXSL;
		} else if (serverIp.equals(serverCIp) && port == serverCPort) {
			choiceXSL = choiceCXSL;
		}
		return getRelativeXSLPath(choiceXSL);
	}
	
	public static String getRelativeClassXSL(String college) {
		String classXSL = null;
		if (college.equals(collegeA)) {
			classXSL = classAXSL;
		} else if (college.equals(collegeB)) {
			classXSL = classBXSL;
		} else if (college.equals(collegeC)) {
			classXSL = classCXSL;
		}
		return getRelativeXSLPath(classXSL);
	}
	
	public static String getRelativeStudentXSL(String college) {
		String studentXSL = null;
		if (college.equals(collegeA)) {
			studentXSL = studentAXSL;
		} else if (college.equals(collegeB)) {
			studentXSL = studentBXSL;
		} else if (college.equals(collegeC)) {
			studentXSL = studentCXSL;
		}
		return getRelativeXSLPath(studentXSL);
	}
	
	public static String getRelativeChoiceXSL(String college) {
		String choiceXSL = null;
		if (college.equals(collegeA)) {
			choiceXSL = choiceAXSL;
		} else if (college.equals(collegeB)) {
			choiceXSL = choiceBXSL;
		} else if (college.equals(collegeC)) {
			choiceXSL = choiceCXSL;
		}
		return getRelativeXSLPath(choiceXSL);
	}
	
	public static String getRelativeClassXSDFromServer(String serverIp, int port) {
		String classXSD = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			classXSD = classAXSD;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			classXSD = classBXSD;
		} else if (serverIp.equals(serverCIp) && port == serverCPort) {
			classXSD = classCXSD;
		}
		return getRelativeXSDPath(classXSD);
	}
	
	public static String getRelativeStudentXSDFromServer(String serverIp, int port) {
		String studentXSD = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			studentXSD = studentAXSD;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			studentXSD = studentBXSD;
		} else if (serverIp.equals(serverCIp) && port == serverCPort) {
			studentXSD = studentCXSD;
		}
		return getRelativeXSDPath(studentXSD);
	}
	
	public static String getRelativeChoiceXSDFromServer(String serverIp, int port) {
		String choiceXSD = null;
		if (serverIp.equals(serverAIp) && port == serverAPort) {
			choiceXSD = choiceAXSD;
		} else if (serverIp.equals(serverBIp) && port == serverBPort) {
			choiceXSD = choiceBXSD;
		} else if (serverIp.equals(serverCIp) && port == serverCPort) {
			choiceXSD = choiceCXSD;
		}
		return getRelativeXSDPath(choiceXSD);
	}
	
	public static String getRelativeClassXSD(String college) {
		String classXSD = null;
		if (college.equals(collegeA)) {
			classXSD = classAXSD;
		} else if (college.equals(collegeB)) {
			classXSD = classBXSD;
		} else if (college.equals(collegeC)) {
			classXSD = classCXSD;
		}
		return getRelativeXSDPath(classXSD);
	}
	
	public static String getRelativeStudentXSD(String college) {
		String studentXSD = null;
		if (college.equals(collegeA)) {
			studentXSD = studentAXSD;
		} else if (college.equals(collegeB)) {
			studentXSD = studentBXSD;
		} else if (college.equals(collegeC)) {
			studentXSD = studentCXSD;
		}
		return getRelativeXSDPath(studentXSD);
	}
	
	public static String getRelativeChoiceXSD(String college) {
		String choiceXSD = null;
		if (college.equals(collegeA)) {
			choiceXSD = choiceAXSD;
		} else if (college.equals(collegeB)) {
			choiceXSD = choiceBXSD;
		} else if (college.equals(collegeC)) {
			choiceXSD = choiceCXSD;
		}
		return getRelativeXSDPath(choiceXSD);
	}
	
	public static String getServerIpFromPrefixOfCourseId(String courseId) {
		String serverIp = null;
		if (courseId.startsWith(prefixOfcourseA)) {
			serverIp = serverAIp;
		} else if (courseId.startsWith(prefixOfcourseB)) {
			serverIp = serverBIp;
		} else if (courseId.startsWith(prefixOfcourseC)) {
			serverIp = serverCIp;
		}
		return serverIp;
	}
	
	public static int getServerPortFromPrefixOfCourseId(String courseId) {
		int serverPort = 0;
		if (courseId.startsWith(prefixOfcourseA)) {
			serverPort = serverAPort;
		} else if (courseId.startsWith(prefixOfcourseB)) {
			serverPort = serverBPort;
		} else if (courseId.startsWith(prefixOfcourseC)) {
			serverPort = serverCPort;
		}
		return serverPort;
	}
	
	public static List<ServerInfo> getExcluseCollegeIpAndPort(String college) {
		List<ServerInfo> list = new ArrayList<ServerInfo>();

		if (college.equals(collegeA)) {
			ServerInfo fCollege = new ServerInfo(serverBIp, serverBPort);
			ServerInfo sCollege = new ServerInfo(serverCIp, serverCPort);
			list.add(fCollege);
			list.add(sCollege);
		} else if (college.equals(collegeB)) {
			ServerInfo fCollege = new ServerInfo(serverAIp, serverAPort);
			ServerInfo sCollege = new ServerInfo(serverCIp, serverCPort);
			list.add(fCollege);
			list.add(sCollege);
		} else if (college.equals(collegeC)) {
			ServerInfo fCollege = new ServerInfo(serverBIp, serverBPort);
			ServerInfo sCollege = new ServerInfo(serverAIp, serverAPort);
			list.add(fCollege);
			list.add(sCollege);
		}
		return list;
	}
	
	public static String getFormatStudentXSD() {
		return getRelativeXSDPath(formatStudentXSD);
	}
	
	public static String getFormatClassXSD() {
		return getRelativeXSDPath(formatClassXSD);
	}
	
	public static String getFormatClassChoiceXSD() {
		return getRelativeXSDPath(formatClassChoiceXSD);
	}
	
	public static String getFormatStudentXSL() {
		return getRelativeXSLPath(formatStudentXSL);
	}
	
	public static String getFormatClassXSL() {
		return getRelativeXSLPath(formatClassXSL);
	}
	
	public static String getFormatClassChoiceXSL() {
		return getRelativeXSLPath(formatClassChoiceXSL);
	}

	public static String getRelativeXSLPath(String path) {
		return "./src/center/xsl/" + path;
	}
	
	public static String getRelativeXSDPath(String path) {
		return "./src/center/schema/" + path;
	}
}
