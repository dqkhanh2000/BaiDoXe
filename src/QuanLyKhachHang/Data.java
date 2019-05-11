package src.QuanLyKhachHang;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import  src.QuanLyKhachHang.Controller;

public class Data extends Thread {
    public static Vector<String[]> reciveData;
    private DataInputStream is;
    private static DataOutputStream os;
    private static Socket socket;

    public Data() {
        try {
            this.socket = new Request.ConnectServer().getSocket();
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            SendData("Get_Info_KhachHang "+ application.Main.GetID_ChuBai());
            SendData("Get_Info_HopDong "+ application.Main.GetID_ChuBai());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void refresh() {
            SendData("Get_Info_KhachHang "+ application.Main.GetID_ChuBai());
            SendData("Get_Info_HopDong "+ application.Main.GetID_ChuBai());
    }

    @Override
    public void run() {
        try {
            while(true) {
                reciveData = (Vector<String[]>) new ObjectInputStream(is).readObject();
                reciveData.forEach(strings -> {
                    if(strings.length == 6) Controller.LoadHopDong(reciveData);
                    else Controller.LoadInfoKhachHang(reciveData);
                    return;
                });

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Vector<String[]> getReciveData() {
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
}
