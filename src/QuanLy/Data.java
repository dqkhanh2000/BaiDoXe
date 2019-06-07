package src.QuanLy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import  src.QuanLy.Controller;

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
            os.writeUTF("Get_Info_Bai "+ src.application.Main.GetID_ChuBai());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void refresh() {
        try {
            os.writeUTF("Get_Info_Bai "+ src.application.Main.GetID_ChuBai());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                reciveData= (Vector) new ObjectInputStream(is).readObject();
                Controller.LoadDB(reciveData);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Vector getReciveData() {
        return reciveData;
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


    public static int Get_LastedID() {
        int rt = 0;
        Vector<String[]> data = getReciveData();
        for (String[] row : data) rt = Integer.parseInt(row[0]);
        return rt;
    }
}
