package collegeB.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import collegeB.bean.ChoiceB;
import collegeB.bean.CourseB;
import collegeB.server.ListenServer;

/*
 * 处理本院服务器与集成服务器的交互，主要是两者之间传递xml文件
 */
public class NetworkServerB {
	private static String collegeB = "b";
	private int serverPort = 8810;
	private String serverIp = "192.168.47.94";// 设置成服务器IP

	private static NetworkServerB networkServer = null;
	private static ServerB server = null;
	Thread tread = null;

	private static final int REQUEST_COURSE_INTO = 0;
	private static final int COURSE_CHOICE = 1;
	private static final int VAIN_COURSE_CHOICE = 2;

	// to indicate the result of course choise
	private static int SUCCESS = 1;
	private static int FAILURE = 0;

	private NetworkServerB(ServerB serverB) {
		server = serverB;
		tread = new Thread(new ListenServer(this));
		tread.start();
	}

	public static NetworkServerB getInstance(ServerB server) {
		if (null == networkServer) {
			networkServer = new NetworkServerB(server);
			return networkServer;
		} else {
			return networkServer;
		}
	}

	/*
	 * 发送包含取消的课程选择的xml
	 */
	public boolean sendCourseChoiseAndStudentInfo(String courseId, String choiceXML,
			String stuInfoXML) {
		boolean result = false;
		Socket socket = createSocket(serverIp, serverPort);
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream is = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			os.writeUTF(collegeB);
			os.flush();
			os.writeInt(COURSE_CHOICE);
			os.flush();
			os.writeUTF(courseId);
			os.flush();
			boolean transferSuccess = sendXML(socket, choiceXML);
			if (transferSuccess) {
				int success = is.readInt();
				if (SUCCESS == success) {
					result = sendXML(socket, stuInfoXML);
				}
			}
			int success = is.readInt();
			if(SUCCESS == success) {
				result = true;
			}else {
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * 发送包含取消的课程选择的xml
	 */
	public boolean sendVainCourseChoose(String choiseXML, String courseId) {
		boolean result = false;
		Socket socket = createSocket(serverIp, serverPort);
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream is = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			os.writeUTF(collegeB);
			os.flush();
			os.writeInt(VAIN_COURSE_CHOICE);
			os.flush();
			os.writeUTF(courseId);
			os.flush();
			sendXML(socket, choiseXML);
			int success = is.readInt();
			if(SUCCESS == success) {
				result = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取其他院_共享课程_或者_学生_信息,并存储在本地，待XMLServerA解析
	public List<String> askForSharedCourseInfo() {
		List<String> list = new ArrayList<String>();
		Socket socket = createSocket(serverIp, serverPort);
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream is = new DataInputStream(socket.getInputStream());
			os.writeUTF(collegeB);
			os.flush();
			os.writeInt(REQUEST_COURSE_INTO);
			os.flush();

			int fileNum = is.readInt();
			while (fileNum != 0) {
				String fileURL = receiveXML(socket);
				list.add(fileURL);
				fileNum--;
				os.writeBoolean(true); // tell the server that it is ready for
										// next file
				os.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return list;
	}

	/*
	 * 监听发送过来的请求共享课程请求并发送xml文件
	 */
	public void listeningForRequestingCoursesInfo(Socket socket) {
		String fileURL = server.getLocalSharedCourses();
		if (null != fileURL) {
			sendXML(socket, fileURL);
		}
		File file = new File(fileURL);
		file.delete();
	}

	/*
	 * 监听发送过来的包含课程选择信息的xml文件
	 */
	public void listeningForCourseChoise(Socket socket) {
		String choiceFile = receiveXML(socket);
		List<ChoiceB> list = XMLServerB.getInstance().readCourseChoiceFromXML(
				choiceFile);
		if (null != list && 0 != list.size()) {
			ChoiceB c = (ChoiceB) list.get(0);
			CourseB course = server.getCourse(c.getCourseId());
			if (null != course) {
				DataOutputStream ps;
				try {
					ps = new DataOutputStream(socket.getOutputStream());
					ps.writeInt(SUCCESS);
					ps.flush();
					String stuInfoFile = receiveXML(socket);
					server.saveStudentOther(stuInfoFile);
					server.saveCourseChooseOther(choiceFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				DataOutputStream ps;
				try {
					ps = new DataOutputStream(socket.getOutputStream());
					ps.writeInt(FAILURE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void listeningForStudentInfo(Socket socket) {
		String fileURL = receiveXML(socket);
		server.saveStudentOther(fileURL);
	}

	/*
	 * 监听发送过来的包含取消课程选择信息的xml文件
	 */
	public void listeningForVainCourseChoise(Socket socket) {
		String fileURL = receiveXML(socket);
		boolean result = server.deleteCourseChooseOther(fileURL);
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			if(result) {
				os.writeInt(SUCCESS);
				os.flush();
			}else {
				os.writeInt(FAILURE);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean sendXML(Socket socket, String fileURL) {
		boolean result = false;
		File file = new File(fileURL);

		System.out.println("文件长度:" + (int) file.length());
		DataInputStream fileStream;
		try {
			fileStream = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileURL)));
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			// 将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java
			// 4th里有现成的代码。
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
			// 注意关闭socket链接哦，不然客户端会等待server的数据过来，
			// 直到socket超时，导致数据不完整。
			fileStream.close();
			System.out.println("文件传输完成");
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * 用来接收从集成服务器传来的xml文件，并返回所新建的xml的url
	 */
	private String receiveXML(Socket socket) {
		DataInputStream inputStream = null;
		String fileURL = null;
		try {
			inputStream = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
		} catch (Exception e) {
			System.out.print("接收消息缓存错误\n");
		}

		try {
			// 本地保存路径，文件名会自动从服务器端继承而来。
			String savePath = getTempPath();
			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];
			int passedlen = 0;
			long len = 0;

			DataOutputStream fileOut = new DataOutputStream(
					new BufferedOutputStream(new BufferedOutputStream(
							new FileOutputStream(savePath))));
			len = inputStream.readLong();

			System.out.println("文件的长度为:" + len + "\n");
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
				// 下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
				System.out.println("文件接收了" + (passedlen * 100 / len) + "%\n");
				fileOut.write(buf, 0, read);
				
				if(passedlen == len) {
					break;
				}
			}
			System.out.println("接收完成，文件存为" + savePath + "\n");

			fileOut.close();
			fileURL = savePath;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("接收消息错误" + "\n");
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

	public void test(Socket socket) {
		try {
			DataOutputStream pos = new DataOutputStream(
					socket.getOutputStream());
			DataInputStream pis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			int num = pis.readInt();
			System.out.println("from client :" + num);
			pos.flush();
			int n = 90;
			System.out.println("send to client : " + n);
			pos.writeInt(n);
			pos.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
