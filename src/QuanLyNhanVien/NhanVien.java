package src.QuanLyNhanVien;

public class NhanVien {
	private int ID;
	private String Name;
	private String NgaySinh;
	private String Username;
	private String Password;
	private String DiaChi;
	private String GioiTinh;
	
	public NhanVien( int ID, String Name, String GioiTinh, String NgaySinh, String DiaChi, String Username, 
			String Password ) {
		this.ID = ID;
		this.Name = Name;
		this.GioiTinh = GioiTinh;
		this.NgaySinh = NgaySinh;
		this.DiaChi = DiaChi;
		this.Username = Username;
		this.Password = Password;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNgaySinh() {
		return NgaySinh;
	}
	public void setNgaySinh(String ngaySinh) {
		NgaySinh = ngaySinh;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDiaChi() {
		return DiaChi;
	}
	public void setDiaChi(String diaChi) {
		DiaChi = diaChi;
	}
	public String getGioiTinh() {
		return GioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		GioiTinh = gioiTinh;
	}
}
