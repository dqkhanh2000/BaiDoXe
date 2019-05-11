package src.QuanLy;

public class BaiDoXe {
    private int ID;
    private String TenBai;
    private String DiaChi;
    private int ChoOto, DangDoXeMay, DangDoOto;
    private int ChoXeMay;
    private double GiaOto;
    private double GiaXeMay;


    public BaiDoXe(int ID, String TenBai, String DiaChi,int ChoXemay, int ChoOto, int DangDoXeMay, int DangDoOto ,
             double GiaXeMay,double GiaOto) {
        this.ID = ID;
        this.TenBai=TenBai;
        this.DiaChi = DiaChi;
        this.ChoOto = ChoOto;
        this.ChoXeMay = ChoXemay;
        this.GiaOto=GiaOto;
        this.GiaXeMay=GiaXeMay;
        this.DangDoOto = DangDoOto;
        this.DangDoXeMay = DangDoXeMay;

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

    public int getChoOto() {
        return ChoOto;
    }

    public int getDangDoXeMay() {
        return DangDoXeMay;
    }

    public int getDangDoOto() {
        return DangDoOto;
    }

    public int getChoXeMay() {
        return ChoXeMay;
    }

    public double getGiaOto() {
        return GiaOto;
    }

    public double getGiaXeMay() {
        return GiaXeMay;
    }
}
