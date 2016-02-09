package project_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Server_2 {

	public static int PORT = 8080;
	public static int maxLength = 30;
	public static HashMap<String, Integer> set = new HashMap<String, Integer>();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket socket = new DatagramSocket(PORT);
		File file_1 = new File("server_log.txt");
		File file_2 = new File("server.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file_2));
		getstoredvalue(reader);
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(new byte[maxLength], maxLength);
			socket.receive(receivePacket);
			System.out.println("Handling client at " + receivePacket.getAddress().getHostAddress() + " on port "
					+ receivePacket.getPort());
			FileWriter fileWritter_1 = new FileWriter(file_1.getName(), true);
			BufferedWriter bufferWritter_1 = new BufferedWriter(fileWritter_1);
			handle(socket, receivePacket, bufferWritter_1);
			FileWriter fileWritter_2 = new FileWriter(file_2.getName());
			BufferedWriter bufferWritter_2 = new BufferedWriter(fileWritter_2);
			updatestoredvalue(bufferWritter_2);
		}
	}

	public static void handle(DatagramSocket socket, DatagramPacket receivePacket, BufferedWriter bufferWritter)
			throws IOException {
		byte[] data = receivePacket.getData();
		if (data.equals(null))
			return;
		String str = "";
		for (int i = 0; i < data.length; i++) {
			str += (char) data[i];
		}
		str = str.trim();
		String[] arr = str.split("\\s+");
		if ((arr[0].equals("PUT") || arr[0].equals("put")) && arr.length == 3) {
			String key = arr[1];
			int value = Integer.valueOf(arr[2]);
			set.put(key, value);
			String temp = "(" + key + "," + value + ") has been stored";
			byte[] ret = temp.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
					receivePacket.getPort());
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			bufferWritter.write("(" + key + "," + value + ") has been stored in " + time + "\r\n");
			socket.send(sendPacket);
		} else if ((arr[0].equals("GET") || arr[0].equals("get")) && arr.length == 2) {
			String key = arr[1];
			if (!set.containsKey(key)) {
				String temp = "There is not a key " + key;
				byte[] ret = temp.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
				return;
			}
			int value = set.get(key);
			String temp = "The value of key" + key + " is " + value;
			byte[] ret = temp.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
					receivePacket.getPort());
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			bufferWritter.write("There is a request of key " + key + " in " + time + "\r\n");
			socket.send(sendPacket);
		} else if ((arr[0].equals("DELETE") || arr[0].equals("delete") && arr.length == 2)) {
			String key = arr[1];
			if (!set.containsKey(key)) {
				String temp = "There is not a key " + key;
				byte[] ret = temp.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
				return;
			}
			set.remove(key);
			String temp = "The key" + key + " has been deleted";
			byte[] ret = temp.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
					receivePacket.getPort());
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			bufferWritter.write("The key " + key + " has been deleted in " + time + "\r\n");
			socket.send(sendPacket);
		} else {
			String temp = "Error of message format:" + str;
			byte[] ret = temp.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(ret, ret.length, receivePacket.getAddress(),
					receivePacket.getPort());
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			bufferWritter.write("There is a request with wrong message format in " + time + "\r\n");
			socket.send(sendPacket);
		}
		bufferWritter.close();
	}

	public static void getstoredvalue(BufferedReader reader) throws IOException {
		String line = null;
		while (true) {
			line = reader.readLine();
			if (line == null || line.equals("END"))
				break;
			String[] arr = line.split("\\s+");
			set.put(arr[0], Integer.valueOf(arr[1]));
		}
		reader.close();
	}

	public static void updatestoredvalue(BufferedWriter bufferWritter) throws IOException {
		for (String key : set.keySet()) {
			String value = set.get(key).toString();
			bufferWritter.write(key + " " + value + "\r\n");
		}
		bufferWritter.write("END");
		bufferWritter.close();
	}

}
