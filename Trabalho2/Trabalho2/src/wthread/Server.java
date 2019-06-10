package wthread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	private ServerSocket serverSocket; 
	private int porta;
	private List<PrintStream> clientes;
	private Socket socket;
	
	public Server(int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<PrintStream>();
	}
	
	
	private void criarServerSocket() throws IOException {
		this.serverSocket = new ServerSocket(this.porta);
		System.out.println("Aguardando conexao na porta " + this.porta);
	}
	
	
	private void esperaConexao() throws IOException {
		this.socket = serverSocket.accept();
		System.out.println("Connectando cliente..." + socket.getInetAddress().getHostAddress());
		
		PrintStream ps = new PrintStream(socket.getOutputStream());
		this.clientes.add(ps);
		
		TrataConexao tc = new TrataConexao(socket.getInputStream());
		new Thread(tc).start();
	}
	
	/*public void distribuiMensagem(String msg) {
		System.out.println(msg); 
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg);
        }
        
    }*/
	
	private void trataConexao() throws IOException{
		try {	
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			PrintStream output = new PrintStream(socket.getOutputStream());
			
			String linha = input.readLine();
			while (linha != null && !linha.contentEquals("/sair")){
				output.println("Server: " + linha);
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
	
	public static void main(String[] args) throws IOException {
		Server servidor = new Server(2525);
		servidor.criarServerSocket();
		servidor.esperaConexao();
		servidor.trataConexao();
	}
}
