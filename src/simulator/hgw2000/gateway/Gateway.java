package simulator.hgw2000.gateway;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gson.Gson;

import fi.iki.elonen.NanoHTTPD;
import simulator.hgw2000.command.UdpService;
import simulator.hgw2000.webconsole.HttpSevice;

public class Gateway {
	public ControlLogger getControlLogger() {
		return controlLogger;
	}

	public void setControlLogger(ControlLogger controlLogger) {
		this.controlLogger = controlLogger;
	}

	public HttpSevice getWebConsoleService() {
		return webConsoleService;
	}

	public void setWebConsoleService(HttpSevice webConsoleService) {
		this.webConsoleService = webConsoleService;
	}

	public UdpService getDeviceCommandService() {
		return deviceCommandService;
	}

	public void setDeviceCommandService(UdpService deviceCommandService) {
		this.deviceCommandService = deviceCommandService;
	}

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	protected Logger logger = Logger.getLogger(Gateway.class.getName());
	
	protected GatewayConfig configuration;
	protected HttpSevice webConsoleService;
	protected UdpService deviceCommandService;
	protected DeviceManager deviceManager;
	protected ProfileManager profileManager;
	protected ControlLogger controlLogger;
	
	public GatewayConfig getConfiguration() {
		return configuration;
	}

	public void setConfiguration(GatewayConfig configuration) {
		this.configuration = configuration;
	}

	public void startService() throws Exception {
		File baseFolder = new File(configuration.getHomeFolder());
		logger.config("configuration is " + new Gson().toJson(configuration));
		
		
		
		profileManager = new ProfileManager();
		profileManager.setDBFile(new File(baseFolder, configuration.getProfileDBFile()));
		profileManager.setGateway(this);
		profileManager.start();
		
		deviceManager = new DeviceManager();
		deviceManager.setDBFile(new File(baseFolder, configuration.getDeviceDBFile()));
		deviceManager.setGateway(this);
		deviceManager.start();
		
		controlLogger = new ControlLogger();
		controlLogger.setGateway(this);
		
		webConsoleService = new HttpSevice(configuration.getWebConsolePort());
		webConsoleService.setGateway(this);
		webConsoleService.init();
		webConsoleService.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		
		deviceCommandService = new UdpService();
		deviceCommandService.setGateway(this);
		deviceCommandService.setPort(configuration.getHostPort());
		deviceCommandService.start();
	}
	
	
	public void controlLog(String from, String deviceId, String message){
		controlLogger.log(from, deviceId, message);
	}
}
