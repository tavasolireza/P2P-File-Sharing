import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientBack {
    public static void main(String[] args) throws IOException {


        String text;
        do {
            Socket socket = new Socket("127.0.0.1", 7000);
//             while (!socket.isClosed()){
            Scanner scanner = new Scanner(System.in);
//            System.out.println("enter your Text");

            text = scanner.next();

            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(text);

            Scanner s = new Scanner(socket.getInputStream());
            System.out.println(s.next());


        } while (!text.equals("end"));
    }

}
