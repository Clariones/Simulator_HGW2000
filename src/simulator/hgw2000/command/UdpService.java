package simulator.hgw2000.command;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.skynet.bgby.driverutils.DriverUtils;
import org.skynet.bgby.protocol.UdpData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import simulator.hgw2000.device.DeviceInfo;
import simulator.hgw2000.gateway.Gateway;

public class UdpService {
	private static final String FROM_WEB = "WEB请求";
	protected Logger logger = Logger.getLogger(UdpService.class.getSimpleName());
	protected Gson gson = new Gson();
	protected int port;
	private Gateway gateway;
	private ListenerService service;
	private Map<String, Map<String, CommandInfo>>  commandInfo;
	protected Map<InetAddress, String> verifiedTokens = new HashMap<>();

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public static String getKeyedDigest(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		if (password == null) {
			throw new IllegalArgumentException("The password can not be null");
		}
		if (password.length() == 0) {
			throw new IllegalArgumentException("The password can not be empty");
		}
		if (password.trim().length() == 0) {
			throw new IllegalArgumentException("The password can not be emptry after trimed.");
		}

		byte[] utf16leBytes = password.getBytes("UTF-16LE");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(utf16leBytes);
		byte[] digistedBytes = md5.digest();
		return toHexString(digistedBytes);

	}

	public static String toHexString(byte[] fieldData) {
		StringBuilder resultBuffer = new StringBuilder();
		for (int i = 0; i < fieldData.length; i++) {
			int v = (fieldData[i] & 0xFF);
			if (v <= 0xF) {
				resultBuffer.append("0");
			}
			resultBuffer.append(Integer.toHexString(v));
		}
		return resultBuffer.toString();
	}

	class ListenerService extends UdpListenerService{

		@Override
		public UdpData serve(UdpData message) {
			String result = handleCommand(message);
			if (result == null || result.isEmpty()){
				return null;
			}
			UdpData reData = new UdpData();
			reData.setSocketAddress(message.getSocketAddress());
			reData.setData(result.getBytes());
			return reData;
		}
		
	}

	public String handleCommand(UdpData message) {
		Hgw2000Command cmd = parseCmd(message.getData());
		if (cmd == null){
			return "$verify,error,105\n";
		}
		if (cmd.getCommand().equals("verify")){
			String uName = cmd.getParams().get("username");
			String uPass = cmd.getParams().get("password");
			String passDigest = "\0";
			try {
				passDigest = getKeyedDigest("123456");
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ("admin".equals(uName) && passDigest.equals(uPass)){
				InetSocketAddress addr = (InetSocketAddress) message.getSocketAddress();
				String newToken = new String(Base64.getEncoder().encode(uPass.getBytes()));
				verifiedTokens.put(addr.getAddress(), newToken);
				gateway.controlLog(FROM_WEB, null, "Connected with " + addr.getAddress());
				return "$verify," + newToken + "\n";
			}else{
				return "$verify,error,104\n";
			}
		}
		cmd.setFromAddress(message.getSocketAddress());
		if (!verifyToken(cmd)){
			return "$verify,error,103\n";
		}
		DeviceInfo  device = gateway.getDeviceManager().lookupDevice(cmd.getDeviceName(), cmd.getCmdInfo().getDeviceKeys(), cmd.getParams());
		if (device == null){
			gateway.controlLog(FROM_WEB, null, "Denied control command " + new String(message.getData()));
			return newErrorResponse(cmd, 134);
		}
		
		executeCommand(device, cmd);
		return newErrorResponse(cmd, 0);
	}

	private void executeCommand(DeviceInfo device, Hgw2000Command cmd) {
		CommandInfo info = cmd.getCmdInfo();
		Map<String, String> operation = info.getOperation();
		for(String field : operation.keySet()){
			executeOperation(field, operation.get(field), cmd, device);
		}
	}

	private void executeOperation(String field, String instrument, Hgw2000Command cmd, DeviceInfo device) {
		String[] insts = instrument.split("_");
		String oper = insts[0];
		String paramName = insts[1];
		logger.log(Level.FINE, "execute operation {0} {1}", insts );
		boolean state;
		Map<String, Object> status = device.getStatus();
		if (status == null){
			status = new HashMap<>();
			device.setStatus(status);
		}
		switch (oper){
		case "setTo":
			cmd.getParams().put(paramName, String.valueOf(device.getStatus().get(field)));
			break;
		case "getFrom":
			status.put(field, cmd.getParams().get(paramName));
			break;
		case "setOnOff":
			state = DriverUtils.getAsBoolean(cmd.getParams().get(paramName), false);
			status.put(field, state?"on":"off");
			break;
		case "getOnOff":
			state = DriverUtils.getAsBoolean(status.get(field), false);
			cmd.getParams().put(paramName, state?"1":"0");
			break;
		}
	}

	private String newErrorResponse(Hgw2000Command cmd, int errCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(cmd.getToken());
		sb.append('$').append(cmd.getCommand()).append(',').append(cmd.getDeviceName());
		Iterator<String> it = cmd.getCmdInfo().getTokens().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = cmd.getParams().get(key);
			if (key.equals("err")){
				sb.append(',').append(String.valueOf(errCode));
			}else{
				sb.append(',').append(value);
			}
		}
		return sb.toString();
	}

	private boolean verifyToken(Hgw2000Command cmd) {
		InetSocketAddress addr = (InetSocketAddress) cmd.getFromAddress();
		String token = verifiedTokens.get(addr.getAddress());
		return cmd.getToken().equals(token);
	}

	protected static Pattern PTN_SPLITER_1 = Pattern.compile("\\$((cfg)|(ack)|(req)|(res)|(verify))\\s*,");
	private Hgw2000Command parseCmd(byte[] data) {
		if (data == null || data.length < 10){
			return null;
		}
		String strCmd = new String(data);
		Matcher m = PTN_SPLITER_1.matcher(strCmd);
		if (m == null || !m.find()){
			logger.warning("Cannot handle command: " + strCmd);
			return null;
		}
		if (!strCmd.endsWith("\n")){
			return null;
		}
		Hgw2000Command cmd = new Hgw2000Command();
		String tokenCmd = m.group(1);
		String splitCmd = m.group();
		strCmd = strCmd.trim();
		int pos = strCmd.indexOf(splitCmd);
		if (pos > 0){
			cmd.setToken(strCmd.substring(0, pos));
		}
		cmd.setCommand(tokenCmd);
		String[] params = strCmd.substring(pos + splitCmd.length()).split(",");
		Map<String, String> paramap = new HashMap<String, String>();
		cmd.setParams(paramap);
		if (cmd.getCommand().equals("verify")){
			if (params.length != 2){
				return null;
			}
			paramap.put("username", params[0]);
			paramap.put("password", params[1]);
			return cmd;
		}
		
		cmd.setDeviceName(params[0]);
		Map<String, CommandInfo> tokens = commandInfo.get(cmd.getDeviceName());
		if (tokens == null){
			return null;
		}
		CommandInfo info = tokens.get(cmd.getCommand());
		if (info == null){
			return null;
		}
		cmd.setCmdInfo(info);
		List<String> validParams = info.getTokens();
		if (params.length != validParams.size()+1){
			return null;
		}
		for(int i = 1; i<params.length; i++){
			paramap.put(validParams.get(i-1), params[i]);
		}
		return cmd;
	}

	public void start() throws ListeningServerException, Exception {
		File baseFolder = new File(gateway.getConfiguration().getHomeFolder());
		File confFile = new File(baseFolder, "config/command.Info.json");
		FileReader reader= new FileReader(confFile);
		commandInfo = gson.fromJson(reader, new TypeToken<Map<String, Map<String, CommandInfo>>>(){}.getType());
		logger.config("get config " + commandInfo);
		reader.close();
		
		this.service = new ListenerService();
		service.setListeningPort(port);
		service.start();
	}
}
