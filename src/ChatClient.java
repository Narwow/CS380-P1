import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class ChatClient implements Runnable {
	private Socket socket;

	public ChatClient(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String userInput = null;

		try (Socket mainSocket = new Socket("codebank.xyz", 38001)) {
			// output data to server
			OutputStream os = mainSocket.getOutputStream();
			PrintStream out = new PrintStream(os, true, "UTF-8");

			// get username
			System.out.println("Connected, type 'exit' to disconnect.");
			System.out.println("Client> Please enter your username.");
			userInput = sc.nextLine();
			out.print(userInput);

			// client thread to read server data
			Thread clientThread = new Thread(new ChatClient(mainSocket));
			clientThread.start();

			while (sc.hasNext()) {
				userInput = sc.nextLine();
				if (userInput.equals("exit")) {
					System.out.println("Disconnected from server, good bye!");
					System.exit(0);
				} else {
					out.println(userInput);
				}
			}

		} catch (UnknownHostException e) {
			System.out.println("Wrong host name");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	//client thread starts here
	public void run() {
		String serverInput = null;
		
		try{
			while (true) {
				//read input from server
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				serverInput = br.readLine();
				System.out.println("Server> " +serverInput);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
