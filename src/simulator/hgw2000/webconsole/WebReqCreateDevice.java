package simulator.hgw2000.webconsole;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.skynet.bgby.driverutils.DriverUtils;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import simulator.hgw2000.device.DeviceInfo;
import simulator.hgw2000.gateway.DeviceManager;
import simulator.hgw2000.gateway.Gateway;
import simulator.hgw2000.gateway.GatewayConfig;
import simulator.hgw2000.profile.DeviceProfile;

public class WebReqCreateDevice extends WebReqShowConsole {

	private static final int PREFIX_SIZE_OF_IDENTIFIER = 11;

	public WebReqCreateDevice(){
		super();
		leadUri = "devices/create";
		setHandler(new WebRequestHandler(){
			@Override
			public Response handleRequest(WebRequest request) throws IOException {
				createDevice(request);
				Response resp= HttpSevice.newFixedLengthResponse(Status.REDIRECT, HttpSevice.MIME_HTML, null);
				resp.addHeader("location", "/management");
				return resp;
			}
		});
	}
	
	protected void createDevice(WebRequest request) {
		Map<String, String> inParams = request.getParams();
		DeviceInfo device = createNewDevice(inParams);
		gateway.getDeviceManager().addDevice(device);
	}

	protected DeviceInfo createNewDevice(Map<String, String> inParams) {
		DeviceInfo device = new DeviceInfo();
		
		String profile = inParams.get("identifier_device_profile");
		device.setProfileID(profile);
		
		device.setIdentity(getIdentity(inParams));
		
		String name = inParams.get("identifier_device_name");
		if (name != null){
			device.setDeviceID(name);
		}
		return device;
	}

	private Map<String, Object> getIdentity(Map<String, String> inParams) {
		Map<String, Object> result = new HashMap<>();
		DeviceProfile profile = gateway.getProfileManager().getProfiles().get(inParams.get("identifier_device_profile"));
		assert(profile != null);
		Map<String, String> iders = profile.getIdentifiers();
		for(String key : inParams.keySet()){
			String value = inParams.get(key);
			String idName = key.substring(PREFIX_SIZE_OF_IDENTIFIER);
			String idType = iders.get(idName);
			if (idType == null){
				continue;
			}
			if (idType.equals("string")){
				result.put(idName, value);
			}else if (idType.equals("int")){
				result.put(idName, DriverUtils.getAsInt(value, 0));
			}
		}
		return result;
	}

}
