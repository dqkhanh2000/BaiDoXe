package src.BaoCao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

public class Data extends Thread {
    private static Vector reciveData;
    private DataInputStream is;
    private static DataOutputStream os;
    private static Socket socket;

    public Data() {
        try {
            this.socket = new Request.ConnectServer().getSocket();
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void refresh(int IDBai, int Thang, int Nam) {
        SendData("Get_BaoCao "+IDBai+", "+Thang+", "+Nam);
    }

    @Override
    public void run() {
        try {
            while(true) {
                reciveData= (Vector) new ObjectInputStream(is).readObject();
                Controller.LoadDB(reciveData);
            }
        } catch (Exception e) {}
    }


    public static void SendData(String send) {
        if(os != null) {
            try {
                os.writeUTF(send);
                //os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("os null");
    }

}
