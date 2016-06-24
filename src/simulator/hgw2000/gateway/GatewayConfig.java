package simulator.hgw2000.gateway;

public class GatewayConfig {
	protected String deviceDBFile;
	protected String homeFolder;
	protected int hostPort;
	protected String profileDBFile;
	protected String HtmlTemplateFolder;
	protected String imageFolder;
	
	public String getImageFolder() {
		return imageFolder;
	}
	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}
	public String getHtmlTemplateFolder() {
		return HtmlTemplateFolder;
	}
	public void setHtmlTemplateFolder(String htmlTemplateFolder) {
		HtmlTemplateFolder = htmlTemplateFolder;
	}
	public String getProfileDBFile() {
		return profileDBFile;
	}
	public void setProfileDBFile(String profileDBFile) {
		this.profileDBFile = profileDBFile;
	}
	protected int webConsolePort;
	public String getDeviceDBFile() {
		return deviceDBFile;
	}
	public String getHomeFolder() {
		return homeFolder;
	}
	public int getHostPort() {
		return hostPort;
	}
	public int getWebConsolePort() {
		return webConsolePort;
	}
	public void setDeviceDBFile(String deviceDBString) {
		this.deviceDBFile = deviceDBString;
	}
	public void setHomeFolder(String homeFolder) {
		this.homeFolder = homeFolder;
	}
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
	public void setWebConsolePort(int webConsolePort) {
		this.webConsolePort = webConsolePort;
	}
}
