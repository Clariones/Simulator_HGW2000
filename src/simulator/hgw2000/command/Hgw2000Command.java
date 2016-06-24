package simulator.hgw2000.command;

import java.net.SocketAddress;
import java.util.Map;

public class Hgw2000Command {

	protected String command;
	protected String deviceName;
	protected Map<String, String> params;
	protected String token;
	protected CommandInfo cmdInfo;
	protected SocketAddress fromAddress;
	
	public SocketAddress getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(SocketAddress fromAddress) {
		this.fromAddress = fromAddress;
	}
	public CommandInfo getCmdInfo() {
		return cmdInfo;
	}
	public void setCmdInfo(CommandInfo cmdInfo) {
		this.cmdInfo = cmdInfo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	

}
