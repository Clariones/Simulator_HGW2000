package simulator.hgw2000.gateway;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Base64;

import org.junit.Test;

import simulator.hgw2000.command.UdpService;

public class UdpServiceTest {

	@Test
	public void testGetKeyedDigest() throws Exception, Exception {
		String password = "123456";
		String dist = UdpService.getKeyedDigest(password);
		
		System.out.println(dist);
		byte[] rst = Base64.getEncoder().encode((dist).getBytes());
		System.out.println(new String(rst));
	}

	@Test
	public void testSendMessage(){
		byte[] data = "Hello world".getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length);
		try {
			dp.setSocketAddress(new InetSocketAddress("127.0.0.1", 10099));
			DatagramSocket socket = new DatagramSocket();
			socket.send(dp);
			
			DatagramPacket rp = new DatagramPacket(new byte[1000], 1000);
			socket.receive(rp);
			int len = rp.getLength();
			System.out.println(new String(rp.getData(), 0, len));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
