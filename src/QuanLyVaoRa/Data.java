package src.QuanLyVaoRa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

public class Data extends Thread{

	private static  Vector reciveData;
	private static DataInputStream is;
	private static  DataOutputStream os;
	private static Socket socket;
	
	public Data() {
		try {
			this.socket = new Request.ConnectServer().getSocket();
			is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF("Get_DataXeVao " +Controller.getIDBai());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		try {
			while(true) {
				reciveData= (Vector) new ObjectInputStream(is).readObject();
				Controller.getData(reciveData);
			}
		} catch (Exception e) {}

	}

	public static void sendData(String send) {
		if(os != null) {
			try {
				os.writeUTF(send);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else System.out.println("os null");
	}
}
