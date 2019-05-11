package src.QuanLyKhachHang;

import java.awt.*;

public class KhachHang {
    private int ID_KH;
    private int ID_ChuBai;
    private String Name;
    private String NgaySinh;
    private String DiaChi;
    private String SDT;

    public KhachHang(int ID_KH, String TenKH, String NgaySinh, String SDT, String DiaChi){
        this.ID_KH = ID_KH;
        this.ID_ChuBai = ID_ChuBai;
        this.Name= TenKH;
        this.NgaySinh= NgaySinh;
        this.SDT=SDT;
        this.DiaChi=DiaChi;
    }

    public int getID_KH() {
        return ID_KH;
    }
    public int getID_ChuBai() {
        return ID_ChuBai;
    }
    public String getName() {
        return Name;
    }
    public String getNgaySinh() {
        return NgaySinh;
    }
    public String getSDT() {
        return SDT;
    }
    public String getDiaChi() {
        return DiaChi;
    }
}
