package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class Server extends Thread {

	private static Vector<PrintStream> clientes;
	private Socket socket;
	private String name;

	public Server(Socket socket) {
		this.socket = socket;
	}

	
	public void broadCast(PrintStream output, String verbo, String linha) throws IOException {
		Enumeration e = clientes.elements();

		while (e.hasMoreElements()) {
			
			PrintStream chat = (PrintStream) e.nextElement();

			if (chat != output) {
				chat.println(name + verbo + linha);
			}
		}
	}

	@Override
	public void run() {

		try {
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream output = new PrintStream(socket.getOutputStream());

			name = input.readLine();

			if (name == null) {
				return;
			}

			clientes.add(output);

			String linha = input.readLine();

			while (linha != null && !(linha.trim().equals(""))) {
				broadCast(output, " diz: ", linha);
				linha = input.readLine();
			}

			broadCast(output, " saiu: ", " do chat!");
			clientes.remove(output);
			socket.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) {

		
		clientes = new Vector();

		try {
			
			ServerSocket server = new ServerSocket(2000);

			
			while (true) {

				System.out.println("Esperando conexões");
				Socket socket = server.accept();
				System.out.println("Nova conexão");

				
				Thread t = new Server(socket);
				t.start();

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}
