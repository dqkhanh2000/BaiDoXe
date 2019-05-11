package src.QuanLyVaoRa;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.github.sarxos.webcam.WebcamPanel;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private TextField tfBienSoVao;

    @FXML
    private TextField tfThoiGianVao;

    @FXML
    private TextArea taGhiChuVao;

    @FXML
    private Button btnXacNhanVao;

    @FXML
    private TextField tfBienSoRa;

    @FXML
    private TextField tfThoiGianRa;

    @FXML
    private TextField tfThoiGianDo;

    @FXML
    private TextField tfTongTien;

    @FXML
    private TextArea taGhiChuRa;

    @FXML
    private Button btnXacNhanRa;

    @FXML
    private ImageView imgMatVao;

    @FXML
    private ImageView imgBienSoVao;

    @FXML
    private ImageView imgMatRa;

    @FXML
    private ImageView imgMatTT;

    @FXML
    private ImageView imgBienSoTT;

    @FXML
    private ImageView imgBienSoRa;

    @FXML
    private ImageView imgCamera1;

    @FXML
    private ImageView imgCamera2;

    @FXML
    private TextField tfIDTK;

    @FXML
    private TextField tfIDTKTT;

    @FXML
    private Button btnXeMay;

    @FXML
    private Button btnOto;

    @FXML
    private TextField tfThoiGianVaoTT;

    @FXML
    private TextArea taGhiChuVaoTT;

    @FXML
    private ComboBox cbbLoaiXe;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblSoXe;

    @FXML
    private Label lblChoTrong;

    @FXML
    private TextField tfSearch;

    @FXML
    private  TableColumn<XeVao, String> columnID;

    @FXML
    private  TableColumn<XeVao, String> columnIDTK;

    @FXML
    private  TableColumn<XeVao, String> columnBienSo;

    @FXML
    private  TableColumn<XeVao, String> columnThoiGianVao;

    @FXML
    private  TableColumn<XeVao, String> columnGhiChu;

    @FXML
    private TableView<XeVao> table;

    private static IntegerProperty dangdoXeMay, dangdoOto;
    private static int trongXeMay, trongOto, tongOto, tongXeMay;

    private static int IDBai;

    private static Double giaOto, giaXemay;

    private Webcam webcam;

    private static ObservableList<XeVao> dataXeVao;
    private FilteredList<XeVao> filtered, filteredTable;
    private XeVao xeHienTai;

    private static String tab = "Xe máy";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dangdoXeMay = new SimpleIntegerProperty();
        dangdoOto = new SimpleIntegerProperty();
        dangdoOto.addListener((observableValue, oldvalue, newvalue) -> {
            trongOto = tongOto - newvalue.intValue();
            if(tab.equals("Xe máy")){
                lblChoTrong.setText(String.valueOf(trongXeMay));
                lblSoXe.setText(String.valueOf(dangdoXeMay.getValue()));
            }
        });
        dangdoXeMay.addListener((observableValue, oldvalue, newvalue) -> {
            trongXeMay = tongXeMay - newvalue.intValue();
            if(tab.equals("Ô tô")){
                lblChoTrong.setText(String.valueOf(trongOto));
                lblSoXe.setText(String.valueOf(dangdoOto.getValue()));
            }
        });
        dataXeVao = FXCollections.observableArrayList();
        StartClock();
        new Data().start();
        cbbLoaiXe.getItems().addAll("Xe máy", "Ô tô");

        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320, 240));
        filtered = filteredTable = new FilteredList<>(dataXeVao, p -> true);

        columnIDTK.setCellValueFactory(new PropertyValueFactory<XeVao, String>("ID_TK"));
        columnID.setCellValueFactory(new PropertyValueFactory<XeVao, String>("ID_The"));
        columnBienSo.setCellValueFactory(new PropertyValueFactory<XeVao, String>("BienSo"));
        columnThoiGianVao.setCellValueFactory(new PropertyValueFactory<XeVao, String>("ThoiGian"));
        columnGhiChu.setCellValueFactory(new PropertyValueFactory<XeVao, String>("GhiChu"));

        btnXeMay.setStyle("-fx-background-color: rgb(161,228,149);");
        btnOto.setStyle("-fx-background-color: rgb(207,213,211);");
        tab = "Xe máy";
        setTab();

        tfIDTKTT.textProperty().addListener((observable, oldValue, newValue) ->{
            searchXeVao(newValue, tfIDTKTT);
        });
        tfBienSoRa.textProperty().addListener((observable, oldValue, newValue) ->{
            searchXeVao(newValue, tfBienSoRa);
        });

        tfSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredTable.setPredicate(xeVao -> {
                if(xeVao.getLoaiXe().equals(tab)){
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(xeVao.getBienSo().contains(lowerCaseFilter)) return true;
                    else if(xeVao.getID_TK() != null){
                        if(xeVao.getID_TK().contains(lowerCaseFilter))return true;
                    }
                    else if(xeVao.getID_The() != null){
                            if(xeVao.getID_The().contains(lowerCaseFilter)) return true;
                    }
                    else if(xeVao.getThoiGian().contains(lowerCaseFilter)) return true;
                    else if(xeVao.getGhiChu().contains(lowerCaseFilter)) return true;
                }
                return false;
            });
            SortedList<XeVao> sortedData = new SortedList<>(filteredTable);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        });
    }

    public static int getIDBai() {
        return IDBai;
    }
    public static void getData(Vector<String[]> data){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (String s[]:data) {
            String IDThe = null, IDNV,IDTK= null, BienSo, ThoiGian = null, GhiChu, Loaixe;
            if(s[0] != null) IDThe = s[0].trim();
            else IDTK = s[1].trim();
            IDNV = s[2];
            BienSo = s[3];
            try {
                ThoiGian = dateFormat.format(format.parse(s[4]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GhiChu = s[5];
            Loaixe = s[7];
            XeVao xeVao = new XeVao(IDThe, IDTK, IDNV, BienSo, ThoiGian, GhiChu, Loaixe);
            xeVao.setIDVao(Integer.parseInt(s[6]));
            if(s[8] != null ) xeVao.setImgMat(getImage(s[8].trim()));
            if(s[9] != null )xeVao.setImgBienSo(getImage(s[9].trim()));
            dataXeVao.add(xeVao);
        }
    }
    public Scene getScene(int idBai){
        IDBai = idBai;
        Scene scene = null;
        URL url = null;
        try {
            url = new File("src/QuanLyVaoRa/QuanLyVao.fxml").toURL();
            scene = new Scene(FXMLLoader.load(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }
    public void ChupAnh(Event e){
        if(!webcam.isOpen()) webcam.open();
        ImageView imgView = (ImageView) e.getSource();
        Tooltip.install(imgView, new Tooltip("Nếu ảnh không rõ bấm để chụp lại"));
        Image img = SwingFXUtils.toFXImage(webcam.getImage(), null);
        imgView.setImage(img);
    };
    public void XacNhanXeVao(){
        String IDThe, IDTK, BienSo, ThoiGian, GhiChu, LoaiXe;
        if(tfIDTK.getText().startsWith("T")) {
            IDThe = "'"+tfIDTK.getText().trim()+"'";
            IDTK = "NULL";
        }
        else {
            IDTK = tfIDTK.getText().trim();
            IDThe = "NULL";
        }
        int IDNV = application.Main.getID_NhanVien();
        BienSo = tfBienSoVao.getText().trim();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ThoiGian = format.format(calendar.getTime());
        GhiChu = taGhiChuVao.getText();
        LoaiXe = String.valueOf(cbbLoaiXe.getItems().get(cbbLoaiXe.getSelectionModel().getSelectedIndex()));
        XeVao xeVao = new XeVao(IDThe, IDTK, String.valueOf(IDNV), BienSo, ThoiGian, GhiChu, LoaiXe);
        xeVao.setImgMat(imgMatVao.getImage());
        xeVao.setImgBienSo(imgBienSoVao.getImage());
        xeVao.setIDVao(0);
        dataXeVao.add(xeVao);
        String imgMat = getImageName(BienSo, "Mat");
        String imgBien = getImageName(BienSo, "Bien");
        Data.sendData("Update_XeVao "+IDThe+", "+IDTK+", "+IDNV+", "+IDBai+", N'"+BienSo+"', '"+ThoiGian+"', N'"+GhiChu
                +"', N'"+LoaiXe+"', '"+imgMat+"', '"+imgBien+"'");
        tfIDTKTT.setText("");
        tfBienSoVao.setText("");
        tfThoiGianVao.setText("");
        taGhiChuVao.setText("");
        SaveImage(imgMatVao.getImage(), imgMat);
        SaveImage(imgBienSoVao.getImage(), imgBien);
        imgMatVao.setImage(null);
        imgBienSoVao.setImage(null);
        if(LoaiXe.equals("Xe máy")) dangdoXeMay.set(dangdoXeMay.getValue()+1);
        else dangdoOto.set(dangdoOto.getValue()+1);
    }
    public void XeRa(){
        int IDVao = xeHienTai.getIDVao();
        String BienSo = tfBienSoRa.getText();
        String GhiChu = taGhiChuRa.getText();
        int IDBai = getIDBai();
        String LoaiXe = xeHienTai.getLoaiXe();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String ThoiGian = null;
        try {
            ThoiGian = format.format(dateFormat.parse(tfThoiGianRa.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Data.sendData("Update_XeRa "+IDVao+", N'"+BienSo+"', '"+ThoiGian+"', N'"+GhiChu+"', "+IDBai+", N'"+LoaiXe+"'");
        tfIDTKTT.setText("");
        tfThoiGianVaoTT.setText("");
        imgBienSoTT.setImage(null);
        imgMatTT.setImage(null);
        tfThoiGianRa.setText("");
        tfTongTien.setText("");
        tfThoiGianDo.setText("");
        taGhiChuRa.setText("");
        taGhiChuVaoTT.setText("");
        tfBienSoRa.setText("");
        dataXeVao.removeAll(xeHienTai);
        xeHienTai = null;
        if(LoaiXe.equals("Xe máy")) dangdoXeMay.set(dangdoXeMay.getValue()-1);
        else dangdoOto.set(dangdoOto.getValue()-1);
    }
    public void StartClock(){
        Thread threadClock = new Thread(new Runnable(){

            @Override
            public void run() {
               while (true){
                   Calendar calendar = Calendar.getInstance();
                   SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                   String time = format.format(calendar.getTime());
                   try {
                       Platform.runLater(() -> {
                           lblClock.setText(time);
                       });
                       Thread.sleep(1000);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
            }
        });
        threadClock.start();
    }
    public void setTimeIN(Event e){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String time = format.format(calendar.getTime());
        TextField tf = (TextField) e.getSource();
        tf.setText(time);
    }
    public void StartCamera(){
        if(!webcam.isOpen()) webcam.open();
        if(imgCamera1.getImage() != null) return;
        Thread camThread = new Thread(() -> {
            if(!webcam.isOpen()) webcam.open();
            while (true){
                Image img = SwingFXUtils.toFXImage(webcam.getImage(), null);
                imgCamera1.setImage(img);
                imgCamera2.setImage(img);
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        camThread.start();
    }

    public static void setGiaOto(Double giaOto) {
        Controller.giaOto = giaOto;
    }

    public static void setGiaXemay(Double giaXemay) {
        Controller.giaXemay = giaXemay;
    }
    public void searchXeVao(String data, TextField tf){
        filtered = new FilteredList<>(dataXeVao, p -> true);
        filtered.setPredicate(xeVao -> {
            if(data.equals(xeVao.getID_TK())) return true;
            else if(data.equals(xeVao.getID_The())) return true;
            else if(data.equals(xeVao.getBienSo())) return true;
            return false;
        });
        Tooltip tooltip = new Tooltip("Không tìm thấy thông tin ");
        if(filtered.isEmpty()) {
            if(data.isEmpty()){
                if(tf != null)
                    if(tf == tfIDTKTT)  tfBienSoRa.setText("");
                    else tfIDTKTT.setText("");

                Tooltip.install(tf, tooltip);
                tfThoiGianVaoTT.setText("");
                imgBienSoTT.setImage(null);
                imgMatTT.setImage(null);
                tfThoiGianRa.setText("");
                tfTongTien.setText("");
                tfThoiGianDo.setText("");
                taGhiChuRa.setText("");
                taGhiChuVaoTT.setText("");
            }
        }
        else{
            XeVao xeVao = filtered.get(0);
            Tooltip.uninstall(tf, tooltip);

            if(tf == null ){
                tfBienSoRa.setText(xeVao.getBienSo());
                if(xeVao.getID_The()!=null)tfIDTKTT.setText(xeVao.getID_The());
                else tfIDTKTT.setText(xeVao.getID_TK());
            }
            else if(tf == tfIDTKTT) tfBienSoRa.setText(xeVao.getBienSo());
            else if(xeVao.getID_The()!=null)tfIDTKTT.setText(xeVao.getID_The());
            else tfIDTKTT.setText(xeVao.getID_TK());

            String thoigianvao = xeVao.getThoiGian();
            tfThoiGianVaoTT.setText(thoigianvao);
            try{
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String time = format.format(calendar.getTime());
                tfThoiGianRa.setText(time);
            }catch(Exception e){}

            Image imgMat = null;
            Image imgBien = null;
            if (xeVao.getImgMat() !=null) imgMat = xeVao.getImgMat();
            if (xeVao.getImgBienSo()!= null) imgBien = xeVao.getImgBienSo();
            imgMatTT.setImage(imgMat);
            imgBienSoTT.setImage(imgBien);
            if(imgMat==null) Tooltip.install(imgMatTT, new Tooltip("Không tìm thấy ảnh"));
            else Tooltip.uninstall(imgMatTT, new Tooltip("Không tìm thấy ảnh"));
            if(imgBien==null) Tooltip.install(imgBienSoTT, new Tooltip("Không tìm thấy ảnh"));
            else Tooltip.uninstall(imgBienSoTT, new Tooltip("Không tìm thấy ảnh"));

            taGhiChuVaoTT.setText(xeVao.getGhiChu());

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try{
                Date ThoiGianVao = format.parse(tfThoiGianVaoTT.getText());
                Date ThoiGianRa = format.parse(tfThoiGianRa.getText());
                long time = ThoiGianRa.getTime()/1000/3600-ThoiGianVao.getTime()/1000/3600;
                tfThoiGianDo.setText(String.valueOf(time));
                double tien = 0;
                if(time <= 12) tien = 1;
                else tien = time/12;
                if(xeVao.getLoaiXe().equals("Xe máy")) tfTongTien.setText(String.valueOf(tien*giaXemay));
                else tfTongTien.setText(String.valueOf(tien*giaOto));
            }catch (Exception e){}
            xeHienTai = xeVao;
        }
    }
    public void tabClick(Event e){
        Button btn = (Button) e.getSource();
        if(btn == btnXeMay) {
            if(tab.equals("Xe máy")) return;
            btnXeMay.setStyle("-fx-background-color: rgb(161,228,149);");
            btnOto.setStyle("-fx-background-color: rgb(207,213,211);");
            tab = "Xe máy";
            setTab();
            lblChoTrong.setText(String.valueOf(trongXeMay));
            lblSoXe.setText(String.valueOf(dangdoXeMay.getValue()));
        }
        else if(btn == btnOto){
            if(tab.equals("Ô tô")) return;
            btnXeMay.setStyle("-fx-background-color: rgb(207,213,211);");
            btnOto.setStyle("-fx-background-color: rgb(161,228,149);");
            tab = "Ô tô";
            setTab();
            lblChoTrong.setText(String.valueOf(trongOto));
            lblSoXe.setText(String.valueOf(dangdoOto.getValue()));
        }
    }


    public void setTab(){
        filteredTable = new FilteredList<>(dataXeVao, p -> true);
        filteredTable.setPredicate(xeVao -> {
            if(xeVao.getLoaiXe().equals(tab)){
                return true;
            }
            return false;
        });
        SortedList<XeVao> sortedData = new SortedList<>(filteredTable);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
    public void MouseClickOnTable() {
        if(table.getSelectionModel().getSelectedItem()==null) return;
        String bienso = columnBienSo.getCellData(table.getSelectionModel().getSelectedIndex());
        searchXeVao(bienso, null);
    }

    public static void setDangdoXeMay(int dangdoXemay) {
        dangdoXeMay.setValue(dangdoXemay);
    }
    public void setlblChoTrong(){
        lblChoTrong.setText(String.valueOf(trongXeMay));
        lblSoXe.setText(String.valueOf(dangdoXeMay.getValue()));
    }
    public static void setDangdoOto(int dangdooto) {
        dangdoOto.setValue(dangdooto);
    }

    public static void setTongOto(int tongoto) {
        Controller.tongOto = tongoto;
    }

    public static void setTongXemay(int tongXemay) {
        Controller.tongXeMay = tongXemay;
    }

    public String getImageName(String BienSo, String type){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("ddMMyy-HHmm");
        String time = format.format(calendar.getTime());
        String rt = "img/"+type+"-"+BienSo+"-"+time+".png";
        return rt;
    }

    public static Image getImage(String path){
        if(!(new File(path).exists())) return null;
        return new Image(path);
    }

    public void SaveImage(Image img, String path){
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException e) {}
    }
}
