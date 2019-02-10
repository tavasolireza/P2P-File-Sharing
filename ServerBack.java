import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerBack {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7000);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                Scanner scanner = new Scanner(socket.getInputStream());
                System.out.println(scanner.next());


                Scanner s = new Scanner(System.in);
                String text = s.next();
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                printStream.println(text);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
