package src.QuanLyKhachHang;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

@SuppressWarnings("ALL")
public class Controller implements Initializable {

    @FXML
    private TableView<KhachHang> table;

    @FXML
    private TableColumn IDColumn;


    @FXML
    private TableColumn NameColumn;

    @FXML
    private TableColumn NgaySinhColumn;

    @FXML
    private TableColumn SDTColumn;

    @FXML
    private TableColumn DiaChiColumn;

    @FXML
    private TextField tfID_KH;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfNgaySinh;

    @FXML
    private TextField tfSDT;

    @FXML
    private TextField tfDiaChi;

    @FXML
    private TextField tfBienSo;

    @FXML
    private TextField tfGia;

    @FXML
    private TextField tfThoiHan;

    @FXML
    private TextField tfSearch;

    @FXML
    private TextField tfTGKy;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnSua;

    @FXML
    private Button btnXoa;

    @FXML
    private Button btnThemHD;

    @FXML
    private Button btnSuaHD;

    @FXML
    private Button btnXoaHD;

    @FXML
    private Button btnTroVe;

    @FXML
    private ComboBox<Integer> cbbID;

    @FXML private ChoiceBox cbbLoaiXe;

    private static ObservableList<KhachHang>dataTable;
    private static ObservableList<HopDong>dataHopDong;
    private static ObservableList<HopDong>filteredHopDong;
    private FilteredList<KhachHang> filtered;
    public static src.QuanLyKhachHang.Data dataDB = null;

    public Scene getScene(){
        filteredHopDong = FXCollections.observableArrayList();
        dataHopDong = FXCollections.observableArrayList();
        dataTable = FXCollections.observableArrayList();
        this.dataDB=new src.QuanLyKhachHang.Data();
        dataDB.start();
        Scene scene = null;
        URL url = null;
        try {
            url = Controller.class.getClassLoader().getResource("src/QuanLyKhachHang/QuanLyKhachHang.fxml");
            Parent root = FXMLLoader.load(url);
            scene = new Scene(root);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbbLoaiXe.getItems().addAll("Xe máy", "Ô tô");
        cbbLoaiXe.setOnAction(actionEvent -> {
            mouseClickOncbb();
        });

        IDColumn.setCellValueFactory(new PropertyValueFactory<KhachHang, Integer>("ID_KH"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("Name"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("NgaySinh"));
        SDTColumn.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("SDT"));
        DiaChiColumn.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("DiaChi"));
        table.setItems(dataTable);

        filtered = new FilteredList<>(dataTable, p -> true);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filtered.setPredicate(KhachHang -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(KhachHang.getID_KH()).contains(lowerCaseFilter)) {
                    return true;
                } else if (KhachHang.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if(KhachHang.getDiaChi() != null) {
                    if (KhachHang.getDiaChi().toLowerCase().contains(lowerCaseFilter))
                    return true;
                }
                else if (KhachHang.getSDT().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (KhachHang.getNgaySinh().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<KhachHang> sortedData = new SortedList<>(filtered);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
    public static void LoadInfoKhachHang(Vector<String[]> data) {
        if(dataTable != null)dataTable.clear();
        for (String[] row : data) {
            int ID_KH = Integer.parseInt(row[0]);
            String TenKH = row[1];
            String NgaySinh = row[2];
            String SDT = row[3];
            String DiaChi = row[4];
            dataTable.add(new KhachHang(ID_KH,TenKH,NgaySinh,SDT,DiaChi) );
        }
    }
    public static void LoadHopDong(Vector<String[]> data) {
        if(dataHopDong != null) dataHopDong.clear();
        for (String[] row : data) {
            int ID_HopDong = Integer.parseInt(row[0]);
            int ID_KhachHang = Integer.parseInt(row[1]);
            String BienSo = row[2];
            String ThoiGianKy = row[3];
            float GiaTri = Float.parseFloat(row[4]);
            String ThoiHan = row[5];
            String LoaiXe = row[6];
            dataHopDong.add(new HopDong(ID_HopDong, ID_KhachHang, BienSo, ThoiGianKy, GiaTri, ThoiHan, LoaiXe));
        }
    }
    public void ThemKH(){
        String Name = tfName.getText();
        String NgaySinh = tfNgaySinh.getText();
        String SDT = tfSDT.getText();
        String DiaChi = tfDiaChi.getText();
        Data.SendData("Add_KhachHang "+ src.application.Main.GetID_ChuBai() +", N'"+Name+"', '"+
                NgaySinh+"', '"+SDT+"', N'"+DiaChi+"'");
        Data.refresh();
    }
    public void SuaKH(){
        String ID = tfID_KH.getText();
        String Name = tfName.getText();
        String NgaySinh = tfNgaySinh.getText();
        String SDT = tfSDT.getText();
        String DiaChi = tfDiaChi.getText();
        Data.SendData("Update_KhachHang "+ ID +", N'"+Name+"', '"+
                NgaySinh+"', '"+SDT+"', N'"+DiaChi+"'");
        Data.refresh();
    }
    public void XoaKH(){
        String ID = tfID_KH.getText();
        Data.SendData("Delete_KhachHang "+ ID);
        Data.refresh();
    }
    public void ThemHD(){
        String IDKH = tfID_KH.getText();
        String BienSo = tfBienSo.getText();
        String ThoiGianKy = tfTGKy.getText();
        String Gia = tfGia.getText();
        String ThoiHan = tfThoiHan.getText();
        String LoaiXe = (String) cbbLoaiXe.getSelectionModel().getSelectedItem();
        Data.SendData("Add_HopDong "+ src.application.Main.GetID_ChuBai()+", "+ IDKH +", N'"+BienSo+"', N'"+
                LoaiXe+"', '"+ThoiGianKy+"', "+Gia+", '"+ThoiHan+"'");
        Data.refresh();
    }
    public void SuaHD(){
        int ID = cbbID.getValue();
        String BienSo = tfBienSo.getText();
        String ThoiHan = tfThoiHan.getText();
        Data.SendData("Update_HopDong "+ ID +", N'"+BienSo+"', '"+
                ThoiHan+"'");
        Data.refresh();
    }
    public void XoaHD(){
        int ID = cbbID.getValue();
        Data.SendData("Delete_HopDong "+ ID);
        Data.refresh();
    }
    public void TroVe(){
        Stage primaryStage = src.application.Main.getPrimaryStages();
        primaryStage.setScene(new src.QuanLy.Controller().getScene(primaryStage));
        primaryStage.setTitle("Quản lý bãi");
    }

    public void MouseClickOnTable() {
        //tfID.setEditable(false);
        if(table.getSelectionModel().getSelectedItem()==null) return;
        int IDKH = Integer.parseInt(String.valueOf(IDColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfID_KH.setText(String.valueOf(IDKH));
        tfNgaySinh.setText(String.valueOf(NgaySinhColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfName.setText(String.valueOf(NameColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfDiaChi.setText(String.valueOf(DiaChiColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfSDT.setText(String.valueOf(SDTColumn.getCellData(table.getSelectionModel().getSelectedIndex())));

        if(filteredHopDong != null) filteredHopDong.clear();
        cbbID.getItems().clear();
        for (HopDong hd: dataHopDong) {
            if(IDKH == hd.getID_KhachHang()) {
                filteredHopDong.add(hd);
                cbbID.getItems().add(hd.getID_HopDong());
                cbbID.getSelectionModel().selectFirst();
            }
            else {
                tfBienSo.setText("");
                tfTGKy.setText("");
                tfGia.setText("");
                tfThoiHan.setText("");
                cbbLoaiXe.getSelectionModel().select(null);
            }
        }
    }
    public void mouseClickOncbb(){
        for (HopDong hd: filteredHopDong) {
            if(cbbID.getValue()==hd.getID_HopDong()){
                tfBienSo.setText(hd.getBienSo());
                tfTGKy.setText(String.valueOf(hd.getThoiGianKy()));
                tfGia.setText(String.valueOf(hd.getGiaTri()));
                tfThoiHan.setText(String.valueOf(hd.getThoiHan()));
                cbbLoaiXe.getSelectionModel().select(String.valueOf(hd.getLoaiXe()));
            }
        }
    }
}
