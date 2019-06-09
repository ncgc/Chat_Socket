package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private String host;
	private int porta;
	private String nome;
	
	public Client(String host, int porta, String nome) {
		this.host = host;
		this.porta = porta;
		this.nome = nome;
	}
	
	public void criaSocket() throws UnknownHostException, IOException {
		this.socket = new Socket(this.host, this.porta);
		System.out.println("Conexao estabelecida");
	}
	
	public void criaThread() throws IOException {
		Recebedor r = new Recebedor(this.socket.getInputStream());
		new Thread(r).start();
	}

	private void trataConexao() throws IOException{
		try {	
			Scanner input = new Scanner(System.in);
			PrintStream output = new PrintStream(socket.getOutputStream());
			
			boolean flag = true;
			while (flag){
				output.println(this.nome + ": " + input.nextLine());
				if(input.nextLine().contentEquals("/sair")) {
					flag = false;
				}
			}
			
			input.close();
			output.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Erro durante o envio da mensagem do cliente ao servidor");
			System.out.println("Erro: " + e.getMessage());
			
		} finally {
			fechaSocket(socket);
		}
	}
	
	private void fechaSocket(Socket socket) throws IOException {
		socket.close();
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Informe o host: ");
		String host = new Scanner(System.in).nextLine();
		
		System.out.println("Informe o nickname: ");
		String nickname = new Scanner(System.in).nextLine();
		
		Client cliente = new Client(host, 2525, nickname);
		cliente.criaSocket();
		cliente.criaThread();
		cliente.trataConexao();
	}
	
}
