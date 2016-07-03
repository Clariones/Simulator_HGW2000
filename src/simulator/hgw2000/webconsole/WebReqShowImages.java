package simulator.hgw2000.webconsole;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import freemarker.log.Logger;

public class WebReqShowImages extends WebRequest {
	protected Logger logger = Logger.getLogger(WebReqShowImages.class.getSimpleName());
	protected static final Map<String, String> fileTypeMap = new HashMap<>();
	protected static final int POS_FILE_PATH_STARTED = 7;
	protected static final int HUGE_IMAGE_SIZE = 2*1024*1024;;
	protected static final Map<String, byte[]> imageCache = new HashMap<>();
	static {
		fileTypeMap.put(".tif", "image/tiff");
		fileTypeMap.put(".fax", "image/fax");
		fileTypeMap.put(".gif", "image/gif");
		fileTypeMap.put(".ico", "image/x-icon");
		fileTypeMap.put(".jfif", "image/jpeg");
		fileTypeMap.put(".jpe", "image/jpeg");
		fileTypeMap.put(".jpeg", "image/jpeg");
		fileTypeMap.put(".jpg", "image/jpeg");
		fileTypeMap.put(".png", "image/png");
		fileTypeMap.put(".wbmp", "image/vnd.wap.wbmp");
		fileTypeMap.put(".tiff", "image/tiff");
	}
	protected String fileName = "images/";
	public WebReqShowImages(){
		leadUri = fileName;
		setHandler(new ImageReqHandler());
	}
	
	
	@Override
	protected Map<String, Object> getTemplateData() {
		return null;
	}


	@Override
	protected File getTemplateFile() {
		return null;
	}


	class ImageReqHandler extends WebRequestHandler{

		@Override
		public Response handleRequest(WebRequest request) throws IOException {
			byte[] image = getImage(request.getUri());
			if (image == null || image.length == 0){
				return HttpSevice.newFixedLengthResponse(Status.NOT_FOUND, HttpSevice.MIME_HTML, null);
			}
			String contentType = getContentType(request.getUri());
			return HttpSevice.newFixedLengthResponse(Status.OK, contentType, new ByteArrayInputStream(image), image.length);
		}
		
	}


	public byte[] getImage(String uri) {
		String fileName = uri.substring(POS_FILE_PATH_STARTED);
		if (fileName.indexOf('?') > 0){
			fileName = fileName.substring(0, fileName.indexOf('?'));
		}
		logger.debug("Load image file " + fileName);
		byte[] image = getCachedImage(fileName);
		if (image == null){
			image = loadImageFile(fileName);
			cacheImage(fileName, Arrays.copyOf(image, image.length));
		}
		return image;
	}


	protected void cacheImage(String fileName, byte[] image) {
		if (image == null || image.length > HUGE_IMAGE_SIZE){
			return;
		}
		imageCache.put(fileName, image);
	}


	protected byte[] loadImageFile(String fileName) {
		File baseFolder = new File(gateway.getConfiguration().getHomeFolder());
		File imageFolder = new File(baseFolder, gateway.getConfiguration().getImageFolder());
		File imageFile = new File(imageFolder, fileName);
		if (!imageFile.exists() || !imageFile.canRead()){
			return null;
		}
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		FileInputStream fIn = null;
		try {
			fIn= new FileInputStream(imageFile);
			byte[] buffer = new byte[4*1024];
			while(fIn.available() > 0){
				int size = fIn.read(buffer);
				bOut.write(buffer, 0, size);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fIn.close();
			} catch (Throwable e) {
			}
		}
		return bOut.toByteArray();
	}


	protected byte[] getCachedImage(String fileName) {
		return imageCache.get(fileName);
	}


	public String getContentType(String uri) {
		int pos = uri.lastIndexOf('.');
		if (pos < 0){
			return "image/jpeg";
		}
		String postfix = uri.substring(pos+1).toLowerCase();
		String type = fileTypeMap.get(postfix);
		return type==null?"image/jpeg":type;
	}
}
