package src.QuanLy;

public class BaiDoXe {
    private int ID;
    private String TenBai;
    private String DiaChi;
    private int SoChoOto;
    private int SoChoXeMay;
    private int GiaOto;
    private int GiaXeMay;


    public BaiDoXe(int ID, String TenBai, String DiaChi, int Oto, int Xemay,int GiaOto,int GiaXeMay) {
        this.ID = ID;
        this.TenBai=TenBai;
        this.DiaChi = DiaChi;
        this.SoChoOto = Oto;
        this.SoChoXeMay = Xemay;
        this.GiaOto=GiaOto;
        this.GiaXeMay=GiaXeMay;
    }

    public int getID() {
        return ID;
    }
    public String getTenBai() {
        return TenBai;
    }
    public String getDiaChi() {
        return DiaChi;
    }
    public int getSoChoOto() {
        return SoChoOto;
    }
    public int getSoChoXeMay() {
        return SoChoXeMay;
    }
    public int getGiaXeMay() {
        return GiaXeMay;
    }
    public int getGiaOto() {
        return GiaOto;
    }
}
