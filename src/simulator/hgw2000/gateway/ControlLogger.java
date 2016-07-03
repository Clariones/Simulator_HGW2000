package simulator.hgw2000.gateway;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simulator.hgw2000.device.DeviceInfo;

public class ControlLogger {
	protected static final int GLOBAL_LOG_SIZE = 100;
	protected static final int DEVICE_LOG_SIZE = 20;
	protected Gateway gateway;
	protected List<String> globalLog = new LinkedList<>();
	protected Map<String, List<String>> deviceLog = new HashMap<>();
	public Gateway getGateway() {
		return gateway;
	}
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}
	protected static SimpleDateFormat ctrlLogTimeFormater = new SimpleDateFormat("HH:mm:ss.SSS");
	public void log(String from, String deviceId, String message) {
		Map<String, DeviceInfo> devices = gateway.getDeviceManager().getDevices();
		if (devices == null){
			addGlobleLog("has no any devices yet");
			return;
		}
		DeviceInfo device = devices.get(deviceId);
		String timeStr = ctrlLogTimeFormater.format(new Date());
		if (device != null){
			String msg = String.format("[%-13s %5s]: %s", timeStr, from, message);
			addDeviceLog(deviceId, msg);
		}
		String msg = String.format("[%-13s %5s]: (%s) %s", 
				timeStr, from, deviceId,  message);
		addGlobleLog(msg);
	}
	protected void addGlobleLog(String msg) {
		if (globalLog.size() > GLOBAL_LOG_SIZE){
			globalLog.remove(0);
		}
		globalLog.add(msg);
	}
	protected void addDeviceLog(String deviceId, String msg) {
		List<String> list = deviceLog.get(deviceId);
		if (list == null){
			list = new LinkedList<>();
			deviceLog.put(deviceId, list);
		}
		if (list.size() > DEVICE_LOG_SIZE){
			list.remove(0);
		}
		list.add(msg);
	}
	public List<String> getGlobalLog() {
		return globalLog;
	}
	public void setGlobalLog(List<String> globalLog) {
		this.globalLog = globalLog;
	}
	public Map<String, List<String>> getDeviceLog() {
		return deviceLog;
	}
	public void setDeviceLog(Map<String, List<String>> deviceLog) {
		this.deviceLog = deviceLog;
	}

}
