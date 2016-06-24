package simulator.hgw2000.webconsole;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import simulator.hgw2000.gateway.GatewayConfig;

public class WebReqWelcome extends WebRequest {
	protected String fileName = "index.html.ftl";
	
	@Override
	protected Map<String, Object> getTemplateData() {
		return new HashMap<>();
	}

	@Override
	protected File getTemplateFile() {
		GatewayConfig cfg = getGateway().getConfiguration();
		File baseFolder = new File(cfg.getHomeFolder());
		File htmlFolder = new File(baseFolder, cfg.getHtmlTemplateFolder());
		return new File(htmlFolder, fileName);
	}

}
