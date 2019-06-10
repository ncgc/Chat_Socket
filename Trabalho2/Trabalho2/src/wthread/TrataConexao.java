package wthread;

import java.io.InputStream;
import java.util.Scanner;

public class TrataConexao implements Runnable{
	private InputStream cliente;
	
	public TrataConexao(InputStream cliente) {
		this.cliente = cliente;
	}

	public void run() {
		Scanner s = new Scanner(this.cliente);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}