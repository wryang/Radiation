package center;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServer implements Runnable {
	private CenterServer netServer = null;
	private int localPort = 8810;
	private static final int REQUEST_COURSE_INTO = 0;
	private static final int COURSE_CHOICE = 1;
	private static final int VAIN_COURSE_CHOICE = 2;

	public ListenServer(CenterServer netServer) {
		this.netServer = netServer;
	}

	public void run() {
		Socket socket = null;
		try {
			ServerSocket ss = new ServerSocket(localPort);
			while (true) {
				socket = ss.accept();
				System.out.println("Ω®¡¢socket¡¥Ω”, serverCenter");
				DataInputStream pi = new DataInputStream(
						socket.getInputStream());
				String originalCollege = pi.readUTF();
				int type = pi.readInt();

				switch (type) {
				case REQUEST_COURSE_INTO: {
					netServer.askSharedCouseInfoFor(socket, originalCollege);
					break;
				}
				case COURSE_CHOICE: {
					String courseId = pi.readUTF();
					netServer.transferCourseChoiceFrom(socket, originalCollege,
							courseId);
					break;
				}
				case VAIN_COURSE_CHOICE: {
					String courseId = pi.readUTF();
					netServer.transferVAinCourseChoice(socket, originalCollege,
							courseId);
					break;
				}
				}
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
