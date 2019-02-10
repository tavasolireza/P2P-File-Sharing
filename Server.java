import javax.rmi.CORBA.Util;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Server{
    private static final Object lock = new Object();
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> filePaths = new ArrayList<>();
    private DatagramSocket Datasocket;

    public Server(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }).start();
    }

    private void listen(){
        try{
            Datasocket = new DatagramSocket(Consts.SERVER_PORT);
            byte[] buffer = new byte[Consts.BUFFER_SIZE];
            Utils.clearBuffer(buffer);
            while(true){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                Datasocket.receive(packet);
                synchronized(lock){
                    for(int i = 0; i < fileNames.size(); i++){
                        if(fileNames.get(i).equals(Utils.convertByteArrayToString(packet.getData()))){
                            sendFile(packet.getAddress(), packet.getPort(), fileNames.get(i), filePaths.get(i));
                            break;
                        }
                    }
                }
                Utils.clearBuffer(buffer);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void serveFile(String name, String path){
        synchronized(lock){
            fileNames.add(name);
            filePaths.add(path);
        }
    }

    private void sendFile(InetAddress address, int port, String fileName, String filePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket Datasocket = null;
                try {
                    Datasocket = new DatagramSocket((int) (Math.random() * 64000 + 1000));
                    byte[] file = Utils.getFileBinaries(filePath);
                    int packetNum = (file.length / (Consts.BUFFER_SIZE - 4) + 1);
                    String message = "" + packetNum + " " + file.length;
                    byte[] buffer = message.getBytes();
                    System.out.println("Sending file " + fileName + " to : " + address.getHostName() + " " + port);
                    DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, address, port);
                    try{
                        buffer = new byte[Consts.BUFFER_SIZE];
                        Datasocket.send(packet);
                        int packets = 0;
                        int index = 0;
                        while(packets != packetNum){
                            Utils.clearBuffer(buffer);
                            buffer[0] = (byte) (packets >> 24);
                            buffer[1] = (byte) (packets >> 16);
                            buffer[2] = (byte) (packets >> 8);
                            buffer[3] = (byte) (packets);
                            for(int i = 4; i < Consts.BUFFER_SIZE; i++){
                                buffer[i] = file[index];
                                index++;
                                if(index == file.length) break;
                            }
                            packet = new DatagramPacket(buffer, 0, buffer.length, address, port);
                            Datasocket.send(packet);
                            Datasocket.receive(new DatagramPacket(new byte[1], 1));
                            packets++;
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
