package Request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class test {
	
	private static String URL = "jdbc:sqlserver://reycs.database.windows.net:1433;database=db_BaiDoXe;user=reycs@reycs;password=Khanh01092000;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	private static Connection con = null;
	private static Statement st = null;
	private static DataOutputStream os;
	private static DataInputStream is;

	public static void main(String[] args) {
		
		ConnectDB();
		try {
			ServerSocket server = new ServerSocket(8005);
			System.out.println("Wait");
			
				Socket client = server.accept();
				System.out.println(client.getInetAddress()+" connected");
				is = new DataInputStream(client.getInputStream());
				os = new DataOutputStream(client.getOutputStream());
				while(true) {
					String s = is.readUTF();
					System.out.println(s);
					if(s.startsWith("Add_NhanVien")) {
						Add_NhanVien(s);
					}
					if(s.startsWith("Get_Info_NhanVien")) {
						Vector vector = Get_Info_NhanVien();
						for (Object object : vector) {
							SendData(object);
						}
					}
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ConnectDB() {
		try {
			con = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Add_NhanVien(String data) {
		try {
			if(st == null) st = con.createStatement();
			else st.clearBatch();
			if(st.executeUpdate("EXEC "+data)>0) System.out.println("ok");
			else System.out.println("false");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Vector Get_Info_NhanVien() {
		Vector vector = new Vector();
		try {
			if(st == null) st = con.createStatement();
			else st.clearBatch();
			ResultSet rs = st.executeQuery("EXECUTE [dbo].[Get_Info_NhanVien]");
			while(rs.next()) {
				Vector data = new Vector();
				for(int i = 1; i <= 7 ;i++) {
					data.addElement(rs.getString(i));
				}
				vector.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vector;
	}
	
	public static void SendData(Object data) {
		if(os != null) {
			try {
				os.write(data.toString().getBytes());;
				//os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else System.out.println("os null");
	}
}
