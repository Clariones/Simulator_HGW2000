package simulator.hgw2000.webconsole;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import simulator.hgw2000.device.DeviceInfo;
import simulator.hgw2000.gateway.DeviceManager;

public class WebReqUpdateDevice extends WebReqCreateDevice {

	public WebReqUpdateDevice() {
		leadUri = "updateDevice";
		setHandler(new WebRequestHandler(){

			@Override
			public Response handleRequest(WebRequest request) throws IOException {
				updateDevice(request.getParams());
				//return super.handleRequest(request);
				Response resp= HttpSevice.newFixedLengthResponse(Status.REDIRECT, HttpSevice.MIME_HTML, null);
				resp.addHeader("location", "/management");
				return resp;
			}
		});
	}
	
	protected void updateDevice(Map<String, String> params) {
		String action = params.get("edit_action");
		DeviceManager dm = gateway.getDeviceManager();
		String deviceID = params.get("device_org_id");
		
		
		if (action.equals("delete")){
			dm.removeDevice(deviceID);
			return;
		}
		
		DeviceInfo device = createNewDevice(params);
		dm.getDevices().remove(deviceID);
		dm.addDevice(device);
	}
}
