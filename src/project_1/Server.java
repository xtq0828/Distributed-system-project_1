package project_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Server {

	public static int PORT = 8080;
	public static HashMap<String, Integer> set = new HashMap<String, Integer>();

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket s = null;
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		File file_1 = new File("server_log.txt");
		File file_2 = new File("server.txt");
		s = new ServerSocket(PORT);
		System.out.println("Waiting for communication:" + s);
		try {
			while (true) {
				BufferedReader reader = new BufferedReader(new FileReader(file_2));
				getstoredvalue(reader);
				socket = s.accept();
				System.out.println("Connection to Client:" + socket);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
				FileWriter fileWritter_1 = new FileWriter(file_1.getName(), true);
				BufferedWriter bufferWritter_1 = new BufferedWriter(fileWritter_1);
				handle(socket.getInetAddress(), socket.getPort(), br, pw, bufferWritter_1);
				FileWriter fileWritter_2 = new FileWriter(file_2.getName());
				BufferedWriter bufferWritter_2 = new BufferedWriter(fileWritter_2);
				updatestoredvalue(bufferWritter_2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("close......");
				br.close();
				pw.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void handle(InetAddress address, int port, BufferedReader br, PrintWriter pw,
			BufferedWriter bufferWritter) throws IOException {
		while (true) {
			String str = br.readLine();
			if (str.equals("END"))
				break;
			String[] arr = str.split("\\s+");
			if ((arr[0].equals("PUT") || arr[0].equals("put")) && arr.length == 3) {
				String key = arr[1];
				int value = Integer.valueOf(arr[2]);
				set.put(key, value);
				pw.println("(" + key + "," + value + ") has been stored");
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				bufferWritter.write("(" + key + "," + value + ") has been stored in " + time + "\r\n");
				pw.flush();
			} else if ((arr[0].equals("GET") || arr[0].equals("get")) && arr.length == 2) {
				String key = arr[1];
				if (!set.containsKey(key)) {
					pw.println("There is not a key " + key);
					continue;
				}
				int value = set.get(key);
				pw.println("The value of key " + key + " is " + value);
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				bufferWritter.write("There is a request of key " + key + " in " + time + "\r\n");
				pw.flush();
			} else if ((arr[0].equals("DELETE") || arr[0].equals("delete") && arr.length == 2)) {
				String key = arr[1];
				if (!set.containsKey(key)) {
					pw.println("There is not a key " + key);
					continue;
				}
				set.remove(key);
				pw.println("The key " + key + " has been deleted");
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				bufferWritter.write("The key " + key + " has been deleted in " + time + "\r\n");
				pw.flush();
			} else {
				pw.println("Error of message format:" + str);
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				bufferWritter.write("There is a request with wrong message format in " + time + "\r\n");
			}
		}
		pw.println("END");
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
