package simulator.hgw2000;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import simulator.hgw2000.gateway.Gateway;
import simulator.hgw2000.gateway.GatewayConfig;

public class Main {

	public static void main(String[] args) throws Exception {
		Gateway gateway = new Gateway();
		String fileName = "testData/gateway.cfg.json";
		
		GatewayConfig cfg = loadConfig(fileName);
		gateway.setConfiguration(cfg);
		gateway.startService();
	}

	private static GatewayConfig loadConfig(String fileName) {
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
			return new Gson().fromJson(reader, GatewayConfig.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (Throwable t) {
			}
		}
		return null;
	}

}
