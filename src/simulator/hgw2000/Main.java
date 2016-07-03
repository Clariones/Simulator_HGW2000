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
		System.out.println("========================================================");
		System.out.println("=====本程序仅供模拟HGW2000设备接口协议，非真实设备模拟========");
		System.out.println("=====设备型号和名称与实际使用的Profile无关                    ========");
		System.out.println("========================================================");
		GatewayConfig cfg = loadConfig(fileName);
		gateway.setConfiguration(cfg);
		gateway.startService();
	}

	protected static GatewayConfig loadConfig(String fileName) {
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
