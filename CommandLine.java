import java.util.Scanner;

public class CommandLine{
    private static Server server;

    private static void processCommand(String command){
        if(command.startsWith("r ")){
            System.out.println("Requesting file is : " + command.split(" ")[1]);
            new Client(command.split(" ")[1]);
        }else if(command.startsWith("s ")){
            if(server == null) server = new Server();
            System.out.println("Ready to send file to : " + command.split(" ")[1]);
            server.serveFile(command.split(" ")[1], command.split(" ")[2]);
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(true){
            processCommand(sc.nextLine());
        }
    }
}