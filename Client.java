import java.io.IOException;
import java.net.*;

public class Client{
    private String fileName;
    private DatagramSocket Datasocket;

    public Client(String name){
        fileName = name;

        try{
            Datasocket = new DatagramSocket(Consts.CLIENT_PORT);
            Datasocket.setBroadcast(true);
            requestFile();
            Datasocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void requestFile(){
        String message = fileName;
        byte[] buffer = message.getBytes();
        DatagramPacket packet;
        try{
            packet = new DatagramPacket(buffer, 0, buffer.length, Inet4Address.getLocalHost(), Consts.SERVER_PORT);
            Datasocket.send(packet);
            buffer = new byte[Consts.BUFFER_SIZE];
            Utils.clearBuffer(buffer);
            packet = new DatagramPacket(buffer, buffer.length);
            Datasocket.receive(packet);
            String[] splits = Utils.convertByteArrayToString(packet.getData()).split(" ");
            int fileSize = Integer.parseInt(splits[1]);
            int packetNum = Integer.parseInt(splits[0]);
            System.out.println("The file is beeing recieved from" + packet.getAddress().getHostName() + " " + packet.getPort() + ".");
            System.out.println("The file size is : " + fileSize + " bytes");
            System.out.println("# of packets to be receive is " + packetNum);
            int packets = 0;
            byte[] file = new byte[fileSize];
            while(packets != packetNum){
                Utils.clearBuffer(buffer);
                packet = new DatagramPacket(buffer, buffer.length);
                Datasocket.receive(packet);
                byte[] data = packet.getData();
                int offset = data[0] << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
                System.out.println("Packet received : " + offset + " " + packets + " " + packetNum);
                for(int i = 4; i < data.length; i++){
                    try{
                        file[offset * (Consts.BUFFER_SIZE - 4) + i - 4] = data[i];
                    }catch(Exception e){
                        break;
                    }
                }
                packets++;
                Datasocket.send(new DatagramPacket(new byte[1], 0, 1, packet.getAddress(), packet.getPort()));
            }
            System.out.println("All of the packets has been received.");
            Utils.writeFile(fileName, file, fileSize);
            System.out.println("File has been created.");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
