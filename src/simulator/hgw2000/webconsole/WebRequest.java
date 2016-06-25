package simulator.hgw2000.webconsole;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import simulator.hgw2000.gateway.Gateway;

public abstract class WebRequest {
	protected String leadUri;
	protected Gateway gateway;
	protected Logger logger;
	
	public WebRequest(){
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}
	
	
	public Gateway getGateway() {
		return gateway;
	}
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}
	protected abstract Map<String, Object> getTemplateData();
	protected abstract File getTemplateFile();
	
	public String getLeadUri() {
		return leadUri;
	}

	protected String uri;
	protected Map<String, String> params;
	protected WebRequestHandler handler = new WebRequestHandler();
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public WebRequestHandler getHandler() {
		return handler;
	}
	public void setHandler(WebRequestHandler handler) {
		this.handler = handler;
	}
	
	public void appendParameters(Map<String, String> newParams){
		if (params == null){
			params = new HashMap<>();
		}else{
			params.clear();
		}
		params.putAll(newParams);
	}
}
