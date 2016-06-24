package simulator.hgw2000.gateway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;

import simulator.hgw2000.profile.DeviceProfile;

public class ProfileManager {
	public Map<String, DeviceProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Map<String, DeviceProfile> profiles) {
		this.profiles = profiles;
	}

	protected File DBFile;
	protected Map<String, DeviceProfile> profiles;
	private Gateway gateway;
	public File getDBFile() {
		return DBFile;
	}

	public void setDBFile(File dBFile) {
		DBFile = dBFile;
	}

	public void start() {
		Gson gson = new Gson();
		FileReader reader = null;
		try {
			reader = new FileReader(DBFile);
			DeviceProfile[] prs = gson.fromJson(reader, new DeviceProfile[0].getClass());
			profiles = new HashMap<>();
			for(DeviceProfile pro : prs){
				profiles.put(pro.getID(), pro);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (Throwable e) {
			}
		}
		
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public DeviceProfile getProfileByDeviceName(String deviceShortName) {
		Iterator<DeviceProfile> it = profiles.values().iterator();
		while(it.hasNext()){
			DeviceProfile profile = it.next();
			String deviceName = (String) profile.getExtParams().get("deviceName");
			if (deviceShortName.equals(deviceName)){
				return profile;
			}
		}
		return null;
	}

}
