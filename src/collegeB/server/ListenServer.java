package collegeB.server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import collegeB.server.NetworkServerB;

public class ListenServer implements Runnable{
	private NetworkServerB netServer = null;
	private int localPort = 8830;
	private static final int REQUEST_COURSE_INTO = 0;	
	private static final int COURSE_CHOICE = 1;
	private static final int VAIN_COURSE_CHOICE = 2;

	public ListenServer(NetworkServerB netServer) {
		this.netServer = netServer;
	}
	
	public void run() {
		Socket socket = null;
		try {
			ServerSocket ss = new ServerSocket(localPort);
			while (true) {
				socket = ss.accept();
				System.out.println("Ω®¡¢socket¡¥Ω”, serverB");

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
