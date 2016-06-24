package simulator.hgw2000.command;

import java.util.List;
import java.util.Map;

public class CommandInfo {

	protected List<String> tokens;
	protected List<String> deviceKeys;
	protected Map<String, String> operation;

	public Map<String, String> getOperation() {
		return operation;
	}

	public void setOperation(Map<String, String> operation) {
		this.operation = operation;
	}

	public List<String> getDeviceKeys() {
		return deviceKeys;
	}

	public void setDeviceKeys(List<String> deviceKeys) {
		this.deviceKeys = deviceKeys;
	}

	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
	
	

}
