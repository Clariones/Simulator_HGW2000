package simulator.hgw2000.webconsole;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import simulator.hgw2000.device.DeviceInfo;
import simulator.hgw2000.gateway.DeviceManager;

public class WebReqControlDevice extends WebReqShowDevices {
	private static final int POS_STATUS_VALUE_START = 15;

	public WebReqControlDevice(){
		leadUri = "controlDevice";
		setHandler(new WebRequestHandler(){

			@Override
			public Response handleRequest(WebRequest request) throws IOException {
				controlDevice(request.getParams());
				//return super.handleRequest(request);
				Response resp= HttpSevice.newFixedLengthResponse(Status.REDIRECT, HttpSevice.MIME_HTML, null);
				resp.addHeader("location", "/devices");
				return resp;
			}
			
		});
	}

	protected void controlDevice(Map<String, String> params) {
		String devID = params.get("control_device_id");
		DeviceManager dm = gateway.getDeviceManager();
		DeviceInfo device = dm.getDevices().get(devID);
		Map<String, Object> status = device.getStatus();
		if (status == null){
			status = new HashMap<String, Object>();
			device.setStatus(status);
		}
		updateStatus(devID, params.get("control_device_profile"), status, params);
	}

	private void updateStatus(String devId, String profile, Map<String, Object> status, Map<String, String> params) {
		for(String param : params.keySet()){
			if (param.startsWith("control_status_")){
				String statusName = param.substring(POS_STATUS_VALUE_START);
				String value = params.get(param);
				status.put(statusName, value);
				gateway.controlLog("控制台", devId, "Set " + statusName +" to " + value);
			}
		}
	}

}
