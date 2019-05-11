package src.QuanLyKhachHang;

import java.sql.Date;

public class HopDong {
    private int ID_HopDong;
    private int ID_KhachHang;
    private String BienSo;
    private String ThoiGianKy;
    private float GiaTri;
    private String ThoiHan;

    public HopDong(int ID_HopDong, int ID_KhachHang, String BienSo, String ThoiGianKy, float GiaTri, String ThoiHan){
        this.ID_HopDong = ID_HopDong;
        this.ID_KhachHang = ID_KhachHang;
        this.BienSo = BienSo;
        this.ThoiGianKy = ThoiGianKy;
        this.GiaTri = GiaTri;
        this.ThoiHan = ThoiHan;
    }

    public int getID_HopDong() {
        return ID_HopDong;
    }

    public int getID_KhachHang() {
        return ID_KhachHang;
    }

    public String getBienSo() {
        return BienSo;
    }

    public String getThoiGianKy() {
        return ThoiGianKy;
    }

    public float getGiaTri() {
        return GiaTri;
    }

    public String getThoiHan() {
        return ThoiHan;
    }
}
