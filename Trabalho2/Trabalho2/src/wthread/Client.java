package wthread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		RecebedorCliente r = new RecebedorCliente(this.socket.getInputStream());
		new Thread(r).start();
	}

	private void trataConexao() throws IOException{
		try {	
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			PrintStream output = new PrintStream(socket.getOutputStream());
			
			String linha = input.readLine();
			while (linha != null && !linha.contentEquals("/sair")){
				output.println(this.nome + ": " + linha);
				linha = input.readLine();
			}
			input.close();
			output.close();
		} finally {
			fechaSocket();
		}
	}
	
	private void fechaSocket() throws IOException {
		this.socket.close();
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
