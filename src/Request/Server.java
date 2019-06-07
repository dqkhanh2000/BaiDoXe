package Request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Vector;

public class Server {
    
	private static String URL = "jdbc:sqlserver://reycs2.database.windows.net:1433;database=db_BaiDoXe;user=reycs@reycs2;password=Khanh01092000;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	private static Connection con = null;
	private static Statement st = null;
	private static DataOutputStream os;
	private static DataInputStream is;

	public static void main(String[] args) {
		
		ConnectDB();
		try {
			ServerSocket server = new ServerSocket(8005);
			System.out.println("Wait");
			Thread threadRecive = null;
			while(true) {
				Socket client = server.accept();
				System.out.println(client.getInetAddress()+" connected");
				is = new DataInputStream(client.getInputStream());
				os = new DataOutputStream(client.getOutputStream());
				threadRecive = startRecive();
				threadRecive.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Thread startRecive() {
		Thread t = null;
		t = new Thread(() ->{
			String s;
			try {
				while(true) {
					s = is.readUTF();
					System.out.println(s);
					if(s.startsWith("Add_") || s.startsWith("Delete") || s.startsWith("Update_")) {
						Update(s);
					}
					else if(s.startsWith("Check_Login")) {
						CheckLogin(s);
					}
					else if(s.startsWith("Get_ID")) {
						try {
							String name = String.copyValueOf(s.toCharArray(), s.indexOf(" ")+1,s.length()- s.lastIndexOf(" ")-1);
							Statement st = con.createStatement();
								ResultSet rs = st.executeQuery("Select ID_ChuBai From dbo.Login where Username = '"+name+"'");
							if(rs.next()) {
								if(rs.getString(1) != null) {
									SendData(rs.getString(1));
									SendData(rs.getString(1));
								}
								else {
										ResultSet rs2 = st.executeQuery("Select ID_NV From dbo.Login where Username = '"+name+"'");
										rs2.next();
										SendData(rs2.getString(1));
										ResultSet rs3 = st.executeQuery("Select ID_ChuBai From Info_NhanVien where ID_NV = "+rs2.getString(1));
										rs3.next();
										SendData(rs3.getString(1));
										//System.out.println(rs2.getString(1));
								}
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					
					else if(s.startsWith("Get_")) {
						Vector vector = GetInfo(s);
						SendData(vector);
					}
					
				}
			}catch (IOException e1) {
				//e1.printStackTrace();
				try {
					is.close();
					os.close();
				} catch (IOException e) {
				}
			}finally {
				try {
					is.close();
					os.close();
				} catch (IOException e) {
				}
				
			}
		});
		return t;
	}
	public static void CheckLogin(String s) {
		String user = String.copyValueOf(s.toCharArray(), 12, s.lastIndexOf(" ")-s.indexOf(" "));
		String pass = String.copyValueOf(s.toCharArray(), s.lastIndexOf(" ")+1, s.length()- s.lastIndexOf(" ")-1);
		try {
			CallableStatement cstmt = con.prepareCall("EXEC Check_Login ?, ?, ?");
			cstmt.setString(1, user);
			cstmt.setString(2, pass);
			cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
			cstmt.execute();
			SendData(String.valueOf(cstmt.getInt(3)));
			System.out.println("Levels: " + cstmt.getInt(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void ConnectDB() {
		try {
			con = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void Update(String data) {
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
	public static Vector GetInfo(String s) {
		Vector vector = new Vector();
		try {
			if(st == null) st = con.createStatement();
			else st.clearBatch();
			ResultSet rs = st.executeQuery("EXECUTE "+s);
			ResultSetMetaData rsm = rs.getMetaData();
			int countCol = rsm.getColumnCount();
			while(rs.next()) {
				String data[] = new String[countCol];
				for(int i = 1; i <= countCol ;i++) {
					data[i-1] = rs.getString(i);
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
				new ObjectOutputStream(os).writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else System.out.println("os null");
	}
	public static void SendData(String data) {
		if(os != null) {
			try {
				os.writeUTF(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else System.out.println("os null");
	}
}
