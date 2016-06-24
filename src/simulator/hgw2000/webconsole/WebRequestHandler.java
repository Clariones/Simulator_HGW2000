package simulator.hgw2000.webconsole;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.skynet.bgby.driverutils.DriverUtils;

import fi.iki.elonen.NanoHTTPD.Response;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WebRequestHandler {

	public Response handleRequest(WebRequest request) throws IOException {
		File file = request.getTemplateFile();
		String result = renderTemplate(file, request.getTemplateData());
		return HttpSevice.newFixedLengthResponse(result);
	}
	
	public String renderTemplate(File tmpFile, Map<String, Object> data) throws IOException{
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setDirectoryForTemplateLoading(tmpFile.getParentFile());
		cfg.setDefaultEncoding("UTF-8");
		Template tmp = cfg.getTemplate(tmpFile.getName());
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(bOut);
		try {
			tmp.process(data, out);
			return bOut.toString();
		} catch (TemplateException e) {
			e.printStackTrace();
			return DriverUtils.dumpExceptionToString(e);
		} finally{
			out.close();
		}
	}

}
