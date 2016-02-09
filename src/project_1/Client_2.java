package project_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client_2 {

	public static int PORT = 8080;
	public static int maxLength = 30;
	public static String fileName = "input.txt";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(10000);
		File file = new File("client_log.txt");
		send(socket, fileName);
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(new byte[maxLength], maxLength);
			socket.receive(receivePacket);
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			handle(receivePacket, bufferWritter);
		}
	}

	public static void handle(DatagramPacket receivePacket, BufferedWriter bufferWritter) throws IOException {
		byte[] data = receivePacket.getData();
		String str = "";
		for (int i = 0; i < data.length; i++) {
			str += (char) data[i];
		}
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		bufferWritter.write("Feedback: " + str + "  " + time + "\r\n");
		System.out.println(str);
		bufferWritter.close();
	}

	public static void send(DatagramSocket socket, String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.equals("END"))
				break;
			byte[] send = line.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, InetAddress.getByName("127.0.0.1"), PORT);
			socket.send(sendPacket);
		}
		reader.close();
	}

}
