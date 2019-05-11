package src.QuanLyVaoRa;

import javafx.scene.image.Image;

public class XeVao {
    private String BienSo;
    private String ThoiGian,ID_NV, ID_The,ID_TK, GhiChu, LoaiXe;
    private Image imgMat, imgBienSo;
    int IDVao;

    public XeVao(String ID_The, String ID_TK, String ID_NV, String bienSo,  String thoiGian,String GhiChu, String loaixe) {
        BienSo = bienSo;
        this.ID_TK = ID_TK;
        this.ID_NV = ID_NV;
        ThoiGian = thoiGian;
        this.ID_The = ID_The;
        this.GhiChu = GhiChu;
        this.LoaiXe = loaixe;
    }

    public String getBienSo() {
        return BienSo;
    }

    public String getLoaiXe() {
        return LoaiXe;
    }

    public int getIDVao() {
        return IDVao;
    }

    public void setIDVao(int IDVao) {
        IDVao = IDVao;
    }

    public String getID_TK() {
        return ID_TK;
    }

    public String getID_NV() {
        return ID_NV;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public String getID_The() {
        return ID_The;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public Image getImgMat() {
        return imgMat;
    }

    public void setID_The(String ID_The) {
        this.ID_The = ID_The;
    }

    public void setImgMat(Image imgMat) {
        this.imgMat = imgMat;
    }

    public Image getImgBienSo() {
        return imgBienSo;
    }

    public void setImgBienSo(Image imgBienSo) {
        this.imgBienSo = imgBienSo;
    }
}
