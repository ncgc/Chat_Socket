package wthread;

import java.io.InputStream;
import java.util.Scanner;

public class RecebedorCliente implements Runnable {

	private InputStream servidor;
	
	public RecebedorCliente(InputStream servidor) {
		this.servidor = servidor;
	}
	
	
	@Override
	public void run() {
		Scanner s = new Scanner(this.servidor);
		while(s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}