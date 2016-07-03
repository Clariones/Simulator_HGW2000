package simulator.hgw2000.device;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfo {
	protected String deviceID;
	protected String profileID;
	protected Map<String, Object> identity;
	protected Map<String, Object> status = new HashMap<>();
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getProfileID() {
		return profileID;
	}
	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}
	public Map<String, Object> getIdentity() {
		return identity;
	}
	public void setIdentity(Map<String, Object> identity) {
		this.identity = identity;
	}
	public Map<String, Object> getStatus() {
		return status;
	}
	public void setStatus(Map<String, Object> status) {
		this.status = status;
	}
}
