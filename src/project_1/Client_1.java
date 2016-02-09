package project_1;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		String fileName = "input.txt";
		File file = new File("client_log.txt");
		try {
			socket = new Socket("127.0.0.1", Server.PORT);
			System.out.println("Socket =" + socket);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			send(pw, fileName);
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			handle(br, bufferWritter);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("close......");
				br.close();
				pw.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void handle(BufferedReader br, BufferedWriter bufferWritter) throws IOException {
		String line = null;
		while (true) {
			line = br.readLine();
			if (line == null || line.equals("END"))
				break;
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			bufferWritter.write("Feedback: " + line + "  " + time + "\r\n");
			System.out.println(line);
		}
		bufferWritter.close();
	}

	public static void send(PrintWriter pw, String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			pw.println(line);
		}
		pw.flush();
		reader.close();
	}

}
