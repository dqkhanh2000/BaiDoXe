package Request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import src.QuanLyNhanVien.Controller;

public class ConnectServer{
	private static  Socket socket;
	private  String addServer = "13.67.49.28";
	private int port = 8005;
	public ConnectServer() {
		try {
			socket = new Socket(addServer, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Socket getSocket() {
		return socket;
	}
	
}
