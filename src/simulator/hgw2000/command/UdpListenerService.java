package simulator.hgw2000.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.skynet.bgby.protocol.UdpData;


public class UdpListenerService {
	protected Logger logger = Logger.getLogger(UdpListenerService.class.getSimpleName());
	protected final String TAG;
	protected String listeningAddress;
	protected DatagramSocket listeningSocket;
	protected MainLoopThread mainThread;
	protected boolean damon;
	protected boolean needToStop;

	public UdpListenerService() {
		TAG = this.getClass().getName();
	}

	public boolean isNeedToStop() {
		return needToStop;
	}

	public void setNeedToStop(boolean needToStop) {
		this.needToStop = needToStop;
	}

	public boolean isDamon() {
		return damon;
	}

	public void setDamon(boolean damon) {
		this.damon = damon;
	}

	public String getListeningAddress() {
		return listeningAddress;
	}

	public void setListeningAddress(String listeningAddress) {
		this.listeningAddress = listeningAddress;
	}

	public int getListeningPort() {
		return listeningPort;
	}

	public void setListeningPort(int listeningPort) {
		this.listeningPort = listeningPort;
	}

	public boolean isHasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	protected int listeningPort;
	protected boolean hasStarted = false;
	protected ExecutorService threadPool;
	protected DatagramSocket sendingSocket;

	public void start() throws ListeningServerException {
		if (wasStarted()) {
			throw new ListeningServerException("Already stated");
		}
		// create socket
		try {
			createListeningSocket();
		} catch (Exception e) {
			throw new ListeningServerException("Failed create multicast socket", e);
		}
		// create thread pool to execute tasks
		createThreadPool();
		// then start the work-loop
		startLoop();
	}

	protected void createThreadPool() {
		this.threadPool = Executors.newFixedThreadPool(5);
	}

	protected void startLoop() {
		mainThread = new MainLoopThread();
		mainThread.setDaemon(isDamon());
		mainThread.setName("Multicast Listener");
		mainThread.start();
	}

	protected void createListeningSocket() throws IOException {
		InetAddress group = InetAddress.getByName(getListeningAddress());
//		MulticastSocket msr = null;
		DatagramSocket lstSocket = new DatagramSocket(getListeningPort());
//		msr.setLoopbackMode(false);
//		msr.joinGroup(group);
		listeningSocket = lstSocket;
		sendingSocket = new DatagramSocket();
	}

	public boolean wasStarted() {
		return isHasStarted();
	}

	public boolean isAlive() {
		return wasStarted() && !listeningSocket.isClosed() && mainThread.isAlive();
	}

	public void stop() {
		if (!mainThread.isAlive()) {
			return;
		}
		setNeedToStop(true);
		mainThread.interrupt();
		threadPool.shutdownNow();
		sendingSocket.close();
		listeningSocket.close();
	}

	public void processDataPacket(DatagramPacket dp) {
		UdpData message = new UdpData();
		message.setFrom(dp.getSocketAddress());
		byte[] data = Arrays.copyOfRange(dp.getData(), dp.getOffset(), dp.getOffset() + dp.getLength());
		message.setData(data);
		threadPool.execute(new UdpMsgProcessTask(message));
	}

	public class UdpMsgProcessTask implements Runnable {
		protected UdpData message;

		public UdpMsgProcessTask(UdpData message) {
			this.message = message;
		}

		@Override
		public void run() {
			UdpData response = serve(message);
			if (response != null) {
				sendResponse(response);
			}
		}

	}

	public class MainLoopThread extends Thread {
		public void run() {
			setHasStarted(true);
			byte[] buffer = new byte[1024];
			logger.log(Level.INFO, TAG, "Multicast listening service started");
			while (!isNeedToStop() && !listeningSocket.isClosed()) {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length); // 2.����һ��ָ����������С���鲥��ַ�Ͷ˿ڵ�DatagramPacket�鲥���ݰ�����
				try {
					listeningSocket.receive(dp);
					processDataPacket(dp);
				} catch (IOException e) {
					e.printStackTrace();
					logger.log(Level.SEVERE, TAG, "Multicast listening service exception: " + e.getMessage());
				}
			}
		}
	}

	public UdpData serve(UdpData message) {
		// You must override this method to handle your message.
		// by default, nothing do here
		logger.log(Level.INFO, TAG, new String(message.getData()));
		return null;
	}

	public void sendResponse(UdpData response) {
		if (this.sendingSocket == null || sendingSocket.isClosed()) {
			logger.log(Level.SEVERE, TAG, "Send message when socket not ready");
			return;
		}
		byte[] data = response.getData();
		if (data == null || data.length == 0) {
			logger.log(Level.SEVERE, TAG, "Cannot send empty message");
			return;
		}

		synchronized (sendingSocket) {
			try {
				DatagramPacket dp = new DatagramPacket(data, data.length,
						response.getSocketAddress());

				sendingSocket.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, TAG, "Exception when send message");
			}
		}
	}
}
