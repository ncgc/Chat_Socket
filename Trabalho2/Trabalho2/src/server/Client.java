package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class Client extends Thread {

	private static boolean connection = false;
	private Socket socket;

	public Client(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String linha = input.readLine();

				if (linha == null) {
					System.out.println("Fim da conex�o");
					break;
				}

				System.out.println(linha);
				System.out.println("> ");

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		connection = true;
	}

	public static void main(String[] args) {
		try {
		
			
			Socket socket = new Socket("127.0.0.1", 2000);

		
			BufferedReader keyboad = new BufferedReader(new InputStreamReader(System.in));
			PrintStream output = new PrintStream(socket.getOutputStream());

			System.out.println("Insira seu nome: ");
			String name = keyboad.readLine();
			output.println(name);

			
			Thread t = new Client(socket);
			t.start();

			
			while (true) {
				System.out.print("> ");
				String linha = keyboad.readLine();

				
				if (connection) {
					break;
				}
				output.println(linha);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}

