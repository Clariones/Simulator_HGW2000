package simulator.hgw2000.gateway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.skynet.bgby.driverutils.DriverUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import freemarker.log.Logger;
import simulator.hgw2000.device.DeviceInfo;
import simulator.hgw2000.profile.DeviceProfile;

public class DeviceManager {
	protected Logger logger = Logger.getLogger(DeviceManager.class.getName());
	protected Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public Map<String, DeviceInfo> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, DeviceInfo> devices) {
		this.devices = devices;
	}

	public Gateway getGateway() {
		return gateway;
	}

	protected Map<String, DeviceInfo> devices;
	protected File DBFile;
	private Gateway gateway;

	public File getDBFile() {
		return DBFile;
	}

	public void setDBFile(File dBFile) {
		DBFile = dBFile;
	}
	
	public void start(){
		loadExistingDeviceStatus();
	}
	

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	private void loadExistingDeviceStatus() {
		if (!DBFile.exists()){
			devices = new HashMap<>();
			return;
		}
		
		FileReader reader = null;
		try {
			reader = new FileReader(DBFile);
			devices = new Gson().fromJson(reader, new TypeToken<Map<String, DeviceInfo>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Throwable e) {
			}
		}
	}

	public void addDevice(DeviceInfo device) {
		String id = device.getDeviceID();
		if (id == null){
			id = generateDeviceId(device);
			device.setDeviceID(id);
		}
		if (devices == null){
			devices = new HashMap<>();
		}
		devices.put(id, device);
		saveDevices();
	}

	private void saveDevices() {
		if (!DBFile.exists()){
			if (!createDBFile()){
				throw new RuntimeException("Cannot create Device DB File");
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(DBFile);
			String jsonStr = gson.toJson(devices);
			writer.write(jsonStr);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				writer.close();
			} catch (Throwable e) {
			}
		}
	}

	private boolean createDBFile() {
		File folder=DBFile.getParentFile();
		if (!folder.exists()){
			if (folder.mkdirs()){
				logger.info("Create Device DB folder " + folder.getAbsolutePath() +" success");
			}else{
				logger.info("Create Device DB folder " + folder.getAbsolutePath() +" failed");
				return false;
			}
		}
		try {
			if (DBFile.createNewFile()){
				logger.info("Create Device DB file " + DBFile.getAbsolutePath() +" success");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Create Device DB file " + DBFile.getAbsolutePath() +" failed");
		return false;
	}

	protected static int DEV_ID_SEED = 1;
	private String generateDeviceId(DeviceInfo device) {
		int id = DEV_ID_SEED++;
		String strId = device.getProfileID()+"-"+id;
		return strId.replace(' ', '_');
	}

	
	public DeviceInfo lookupDevice(String deviceShortName, List<String> deviceKeys, Map<String, String> params) {
		ProfileManager pm = gateway.getProfileManager();
		DeviceProfile profile = pm.getProfileByDeviceName(deviceShortName);
		if (profile == null){
			logger.warn("Cannot found device named " + deviceShortName);
			return null;
		}
		Iterator<Entry<String, DeviceInfo>> it = devices.entrySet().iterator();
		DeviceInfo device = null;
		while(it.hasNext()){
			Entry<String, DeviceInfo> ent = it.next();
			String devId = ent.getKey();
			DeviceInfo deviceStatus = ent.getValue();
			
			if (!deviceStatus.getProfileID().equals(profile.getID())){
				continue;
			}
			boolean mayMatch = true;
			for(String key : deviceKeys){
				int devIdty = DriverUtils.getAsInt(deviceStatus.getIdentity().get(key), -1);
				int reqIdty = DriverUtils.getAsInt(params.get(key), -2);
				if (devIdty != reqIdty){
					mayMatch = false;
					break;
				}
			}
			if (mayMatch){
				device = deviceStatus;
				break;
			}
		}
		if (device == null){
			logger.warn("Cannot found device named " + deviceShortName + params);
		}
		return device;
	}

}
