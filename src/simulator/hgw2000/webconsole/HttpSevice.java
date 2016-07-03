package simulator.hgw2000.webconsole;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.skynet.bgby.driverutils.DriverUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import simulator.hgw2000.gateway.Gateway;

public class HttpSevice extends NanoHTTPD {
	protected Logger logger = Logger.getLogger(HttpSevice.class.getName());
	protected static final String MIME_PLAINTEXT_UTF8 = MIME_PLAINTEXT + "; charset=utf-8";
	protected Gson gson = new GsonBuilder().setPrettyPrinting().create();
	protected RequestParser requestParser = new RequestParser();

	public HttpSevice(int port) {
		super(port);
		servicePort = port;
	}

	protected int servicePort;
	protected Gateway gateway;

	public int getServicePort() {
		return servicePort;
	}

	@Override
	public Response serve(IHTTPSession session) {
		try {
			WebRequest request = requestParser.parse(session);
			if (request == null) {
				logger.log(Level.FINE, "Cannot parse request \'{0}\', with params {1}",
						new Object[] { session.getUri(), session.getParms() });
				return this.newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT_UTF8, "无法解析请求");
			}

			return request.getHandler().handleRequest(request);
		} catch (Exception e) {
			String msg = DriverUtils.dumpExceptionToString(e);
			return newFixedLengthResponse(msg);
		}
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
		requestParser.setGateway(gateway);
	}

	public void init() {
		requestParser.register(new WebReqShowDevices());
		requestParser.register(new WebReqShowConsole());
		requestParser.register(new WebReqCreateDevice());
		requestParser.register(new WebReqShowImages());
		requestParser.register(new WebReqControlDevice());
		requestParser.register(new WebReqUpdateDevice());

		requestParser.sortRequest();
	}

}
