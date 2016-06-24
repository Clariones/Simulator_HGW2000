package simulator.hgw2000.webconsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.ResponseException;
import simulator.hgw2000.gateway.Gateway;

public class RequestParser {
	protected Gateway gateway;
	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	protected Logger logger = Logger.getLogger(RequestParser.class.getSimpleName());
	protected List<WebRequest> validRequest = new ArrayList<>();
	
	public WebRequest parse(IHTTPSession session) throws IOException, ResponseException {
		String uri = session.getUri();
		session.parseBody(new HashMap<String, String>());
		
		logger.log(Level.FINE, "Recieve URI " + session.getRemoteIpAddress());
		if (uri.startsWith("/")){
			uri = uri.substring(1);
		}
		String[] segments = uri.split("/");
		if (segments.length < 1 ||  segments[0].isEmpty()){
			logger.log(Level.FINE, "This is requir root");
			return newWellcomRequest();
		}
		
		Iterator<WebRequest> it = validRequest.iterator();
		while(it.hasNext()){
			WebRequest validReq = it.next();
			if (uri.startsWith(validReq.getLeadUri())){
				validReq.setUri(uri + (session.getQueryParameterString() == null?"":"?"+session.getQueryParameterString()));
				validReq.appendParameters(session.getParms());
				logger.fine("Found web request " + validReq.getLeadUri());
				return validReq;
			}
		}
		
		return null;
	}

	private WebRequest newWellcomRequest() {
		WebReqWelcome webReq = new WebReqWelcome();
		webReq.setGateway(gateway);
		webReq.setHandler(new WebRequestHandler());
		return webReq;
	}

	public void register(WebRequest webReq) {
		webReq.setGateway(gateway);
		validRequest.add(webReq);
	}

	public void sortRequest() {
		Collections.sort(this.validRequest, new Comparator<WebRequest>(){
			public int compare(WebRequest o1, WebRequest o2) {
				return o2.getLeadUri().compareTo(o1.getLeadUri());
			}
		});
	}

}
