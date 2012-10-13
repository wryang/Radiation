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
 * ����Ժ�������뼯�ɷ������Ľ�������Ҫ������֮�䴫��xml�ļ�
 */
public class NetworkServerB {
	private static String collegeB = "b";
	private int serverPort = 8810;
	private String serverIp = "192.168.47.94";// ���óɷ�����IP

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
	 * ���Ͱ���ȡ���Ŀγ�ѡ���xml
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
	 * ���Ͱ���ȡ���Ŀγ�ѡ���xml
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

	// ��ȡ����Ժ_����γ�_����_ѧ��_��Ϣ,���洢�ڱ��أ���XMLServerA����
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
	 * �������͹�����������γ����󲢷���xml�ļ�
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
	 * �������͹����İ����γ�ѡ����Ϣ��xml�ļ�
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
	 * �������͹����İ���ȡ���γ�ѡ����Ϣ��xml�ļ�
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

		System.out.println("�ļ�����:" + (int) file.length());
		DataInputStream fileStream;
		try {
			fileStream = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileURL)));
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			// ���ļ��������ȴ����ͻ��ˡ�����Ҫ������������ƽ̨�������������Ĵ�������Ҫ�ӹ���������Բμ�Think In Java
			// 4th�����ֳɵĴ��롣
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
			// ע��ر�socket����Ŷ����Ȼ�ͻ��˻�ȴ�server�����ݹ�����
			// ֱ��socket��ʱ���������ݲ�������
			fileStream.close();
			System.out.println("�ļ��������");
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * �������մӼ��ɷ�����������xml�ļ������������½���xml��url
	 */
	private String receiveXML(Socket socket) {
		DataInputStream inputStream = null;
		String fileURL = null;
		try {
			inputStream = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
		} catch (Exception e) {
			System.out.print("������Ϣ�������\n");
		}

		try {
			// ���ر���·�����ļ������Զ��ӷ������˼̳ж�����
			String savePath = getTempPath();
			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];
			int passedlen = 0;
			long len = 0;

			DataOutputStream fileOut = new DataOutputStream(
					new BufferedOutputStream(new BufferedOutputStream(
							new FileOutputStream(savePath))));
			len = inputStream.readLong();

			System.out.println("�ļ��ĳ���Ϊ:" + len + "\n");
			System.out.println("��ʼ�����ļ�!" + "\n");

			while (true) {
				int read = 0;
				if (inputStream != null) {
					read = inputStream.read(buf);
				}
				passedlen += read;
				if (read == -1) {
					break;
				}
				// �����������Ϊͼ�ν����prograssBar���ģ���������Ǵ��ļ������ܻ��ظ���ӡ��һЩ��ͬ�İٷֱ�
				System.out.println("�ļ�������" + (passedlen * 100 / len) + "%\n");
				fileOut.write(buf, 0, read);
				
				if(passedlen == len) {
					break;
				}
			}
			System.out.println("������ɣ��ļ���Ϊ" + savePath + "\n");

			fileOut.close();
			fileURL = savePath;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("������Ϣ����" + "\n");
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
