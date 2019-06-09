package server;

import java.io.IOException;
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
	
	public Server(int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<PrintStream>();
	}
	
	/**
	 * O servidor fica "escutando" a porta informada. 
	 * @param Porta 
	 */
	private void criarServerSocket() throws IOException {
		this.serverSocket = new ServerSocket(this.porta);
		System.out.println("Aguardando conexao na porta " + this.porta);
	}
	
	/**
	 * O m�todo accept() bloqueia a execu��o at� que o servidor receba um pedido de conex�o
	 * Quando o pedido � aceito, cria-se um socket entre o servidor e o cliente.
	 * @throws IOException
	 * @return socket entre servidor e cliente
	 */
	private void esperaConexao() throws IOException {
		Socket socket = serverSocket.accept();
		System.out.println("Connectando cliente..." + socket.getInetAddress().getHostAddress());
		
		//adiciona cliente a lista
		PrintStream ps = new PrintStream(socket.getOutputStream());
		this.clientes.add(ps);
		
		//cria tratador de conexao em uma nova thread
		TrataConexao tc = new TrataConexao(socket.getInputStream(), this);
		new Thread(tc).start();
	}
	
	public void distribuiMensagem(String msg) {
        // envia msg para todo mundo
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg);
        }
    }
	
	public static void main(String[] args) throws IOException {
		Server servidor = new Server(2525);
		servidor.criarServerSocket();
		servidor.esperaConexao();
	}

}
