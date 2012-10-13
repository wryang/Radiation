package center;

public class ServerInfoUtil {
	private String serverAIp;
	private int serverAPort;
	private String serverBIp;
	private int serverBPort;
	private String serverCIp;
	private int serverCPort;
	
	public ServerInfoUtil() {
		serverAIp = "192.168.47.41";
		serverAPort = 8821;
		serverBIp = "192.168.47.83";
		serverBPort = 8822;
		serverCIp = "192.168.47.57";
		serverCPort = 8823;
	}
	
	public String getServerAIp() {
		return serverAIp;
	}
	public void setServerAIp(String serverAIp) {
		this.serverAIp = serverAIp;
	}
	public int getServerAPort() {
		return serverAPort;
	}
	public void setServerAPort(int serverAPort) {
		this.serverAPort = serverAPort;
	}
	public String getServerBIp() {
		return serverBIp;
	}
	public void setServerBIp(String serverBIp) {
		this.serverBIp = serverBIp;
	}
	public int getServerBPort() {
		return serverBPort;
	}
	public void setServerBPort(int serverBPort) {
		this.serverBPort = serverBPort;
	}
	public String getServerCIp() {
		return serverCIp;
	}
	public void setServerCIp(String serverCIp) {
		this.serverCIp = serverCIp;
	}
	public int getServerCPort() {
		return serverCPort;
	}
	public void setServerCPort(int serverCPort) {
		this.serverCPort = serverCPort;
	}
	
}
