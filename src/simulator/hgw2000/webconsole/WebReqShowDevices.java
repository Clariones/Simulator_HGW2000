package simulator.hgw2000.webconsole;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import simulator.hgw2000.gateway.DeviceManager;
import simulator.hgw2000.gateway.Gateway;
import simulator.hgw2000.gateway.GatewayConfig;

public class WebReqShowDevices extends WebRequest {

	protected String fileName = "devices.html.ftl";
	public WebReqShowDevices(){
		leadUri = "devices";
	}
	
	@Override
	protected Map<String, Object> getTemplateData() {
		Map<String, Object> data = new HashMap<>();
		Gateway gw = getGateway();
		DeviceManager dm = gw.getDeviceManager();
		data.put("devices", dm.getDevices());
		data.put("profiles", gw.getProfileManager().getProfiles());
		data.put("deviceLog", gw.getControlLogger().getDeviceLog());
		data.put("sysLog", gw.getControlLogger().getGlobalLog());
		return data;
	}

	@Override
	public String getLeadUri() {
		// TODO Auto-generated method stub
		return super.getLeadUri();
	}

	@Override
	protected File getTemplateFile() {
		GatewayConfig cfg = getGateway().getConfiguration();
		File baseFolder = new File(cfg.getHomeFolder());
		File htmlFolder = new File(baseFolder, cfg.getHtmlTemplateFolder());
		return new File(htmlFolder, fileName);
	}

}
