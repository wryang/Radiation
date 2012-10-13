package collegeC.server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServer implements Runnable{
	private NetworkServerC netServer = null;
	private int localPort = 8840;
	private static final int REQUEST_COURSE_INTO = 0;	
	private static final int COURSE_CHOICE = 1;
	private static final int VAIN_COURSE_CHOICE = 2;

	public ListenServer(NetworkServerC netServer) {
		this.netServer = netServer;
	}
	
	public void run() {
		Socket socket = null;
		try {
			ServerSocket ss = new ServerSocket(localPort);
			while (true) {
				socket = ss.accept();
				System.out.println("Ω®¡¢socket¡¥Ω”, serverC");
				
				DataInputStream pi = new DataInputStream(
						socket.getInputStream());
				int type = pi.readInt();
				
				switch(type) {
				case REQUEST_COURSE_INTO:
					netServer.listeningForRequestingCoursesInfo(socket);
					break;
				case COURSE_CHOICE :
					netServer.listeningForCourseChoise(socket);
					break;
				case VAIN_COURSE_CHOICE :
					netServer.listeningForVainCourseChoise(socket);
					break;
				}
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
