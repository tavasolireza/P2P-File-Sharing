import java.io.*;
import java.util.ArrayList;

public class Utils{
    public static String convertByteArrayToString(byte[] array){
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : array){
            if(b == -127) break;
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();
    }

    public static byte[] getFileBinaries(String path){
        File file = new File(path);
        ArrayList<Byte> arrayList = new ArrayList<>();
        try{
            // we can close the input stream after the read
            FileInputStream inputStream = new FileInputStream(file);
            byte b;
            int i;
            while((i = inputStream.read()) != -1){
                b = (byte) i;
                arrayList.add(b);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        byte[] binaries = new byte[arrayList.size()];
        for(int i = 0; i < binaries.length; i++){
            binaries[i] = arrayList.get(i);
        }
        return binaries;
    }

    public static void writeFile(String fileName, byte[] byteArray, int size){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("received/" + fileName);
            fileOutputStream.write(byteArray, 0, size);
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void clearBuffer(byte[] buffer){
        for(int i = 0; i < buffer.length; i++){
            buffer[i] = -127;
        }
    }
}
