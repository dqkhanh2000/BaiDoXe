package src.QuanLy;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;


@SuppressWarnings("ALL")
public class Controller implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private TableView<BaiDoXe> table;
    @FXML
    private TableColumn IDColumn;
    @FXML
    private TableColumn TenBaiColumn;
    @FXML
    private TableColumn DiaChiColumn;
    @FXML
    private TableColumn XeMayColumn;

    @FXML
    private TableColumn OtoColumn;

    @FXML
    private TableColumn GiaOtoColumn;

    @FXML
    private TableColumn GiaXeMayColumn;

    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfTenBai;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private TextField tfChoXeMay;
    @FXML
    private TextField tfChoOto;
    @FXML
    private TextField tfGiaXeMay;
    @FXML
    private TextField tfGiaOto;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private MenuItem mnQuanLyNhanVien;
    @FXML
    private MenuItem mnQuanLyKhachHangl;
    @FXML
    private MenuItem mnXemBaoCao;

    @FXML
    private PieChart chartXeMay;
    @FXML
    private PieChart chartOto;

    private Label caption;

    private static ObservableList<BaiDoXe> dataTable;
    private FilteredList<BaiDoXe> filtered;

    private static Stage primaryStage = null;
    public static src.QuanLy.Data data = null;

    private static Vector<String[]> vector;

    public Scene getScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        dataTable = FXCollections.observableArrayList();
        this.data = new Data();
        data.start();
        Scene scene = null;
        URL url = null;
        try {
            url = Controller.class.getClassLoader().getResource("src/QuanLy/Main.fxml");
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
        caption = new Label("");
        Platform.runLater(() ->{pane.getChildren().addAll(caption);});

        IDColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, Integer>("ID"));
        TenBaiColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, String>("TenBai"));
        DiaChiColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, String>("DiaChi"));
        XeMayColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, Integer>("ChoXeMay"));
        OtoColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, Integer>("ChoOto"));
        GiaOtoColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, Double>("GiaOto"));
        GiaXeMayColumn.setCellValueFactory(new PropertyValueFactory<BaiDoXe, Double>("GiaXeMay"));
        table.setItems(dataTable);
        //search
        filtered = new FilteredList<>(dataTable, p -> true);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filtered.setPredicate(BaiDoXe -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(BaiDoXe.getID()).contains(lowerCaseFilter)) {
                    return true;
                } else if (BaiDoXe.getTenBai().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (BaiDoXe.getDiaChi().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(BaiDoXe.getChoOto()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(BaiDoXe.getChoXeMay()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(BaiDoXe.getGiaOto()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(BaiDoXe.getGiaXeMay()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<BaiDoXe> sortedData = new SortedList<>(filtered);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public static void LoadDB(Vector<String[]> data) {
        dataTable.clear();
        vector = data;
        for (String[] row : data) {
            int ID = Integer.parseInt(row[0]);
            String TenBai = row[1];
            String DiaChi = row[2];
            int ChoXeMay = Integer.parseInt(row[3]);
            int ChoOto = Integer.parseInt(row[4]);
            int DangDoXeMay = Integer.parseInt(row[5]);
            int DangDoOto = Integer.parseInt(row[6]);
            double GiaXeMay = Double.parseDouble(row[7]);
            double GiaOto = Double.parseDouble(row[8]);
            dataTable.add(new BaiDoXe(ID, TenBai, DiaChi, ChoXeMay, ChoOto, DangDoXeMay, DangDoOto, GiaXeMay, GiaOto));
        }
    }

    public void ThemBai() {
        String TenBai = tfTenBai.getText();
        String DiaChi = tfDiaChi.getText();
        int ChoXeMay = Integer.parseInt(tfChoXeMay.getText());
        int ChoOto = Integer.parseInt(tfChoOto.getText());
        double GiaXeMay = Double.parseDouble(tfGiaXeMay.getText());
        double GiaOto = Double.parseDouble(tfGiaOto.getText());
        src.QuanLy.Data.SendData("Add_Bai " + src.application.Main.GetID_ChuBai() + ", N'" + TenBai + "',N'" + DiaChi + "', " + ChoXeMay + ", " + ChoOto + ", " + GiaXeMay + ", " + GiaOto);
        src.QuanLy.Data.refresh();
    }

    public void SuaBai() {
        int IDBai = Integer.parseInt(tfID.getText());
        String TenBai = tfTenBai.getText();
        String DiaChi = tfDiaChi.getText();
        int ChoXeMay = Integer.parseInt(tfChoXeMay.getText());
        int ChoOto = Integer.parseInt(tfChoOto.getText());
        double GiaXeMay = Double.parseDouble(tfGiaXeMay.getText());
        double GiaOto = Double.parseDouble(tfGiaOto.getText());
        src.QuanLy.Data.SendData("Update_Bai " + IDBai + ", N'" + TenBai + "',N'" + DiaChi + "', " + ChoXeMay + ", " + ChoOto + ", " + GiaXeMay + ", " + GiaOto);
        src.QuanLy.Data.refresh();
    }

    public void XoaBai() {
        int IDBai = Integer.parseInt(tfID.getText());
        src.QuanLy.Data.SendData("Delete_Bai " + IDBai);
        src.QuanLy.Data.refresh();
    }

    public void MouseClickOnTable() {
        if (table.getSelectionModel().getSelectedItem() == null) return;
        double TongChoOto = (double) dataTable.get(table.getSelectionModel().getSelectedIndex()).getChoOto();
        double TongChoXeMay = (double) dataTable.get(table.getSelectionModel().getSelectedIndex()).getChoXeMay();
        tfID.setText(String.valueOf(IDColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfTenBai.setText(TenBaiColumn.getCellData(table.getSelectionModel().getSelectedIndex()).toString().trim());
        tfDiaChi.setText(DiaChiColumn.getCellData(table.getSelectionModel().getSelectedIndex()).toString().trim());
        tfChoOto.setText(String.valueOf((int) TongChoOto));
        tfChoXeMay.setText(String.valueOf(XeMayColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfGiaOto.setText(String.valueOf(GiaOtoColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        tfGiaXeMay.setText(String.valueOf(GiaXeMayColumn.getCellData(table.getSelectionModel().getSelectedIndex())));
        double dangdoOto = (double) dataTable.get(table.getSelectionModel().getSelectedIndex()).getDangDoOto();
        double dangdoXeMay = (double) dataTable.get(table.getSelectionModel().getSelectedIndex()).getDangDoXeMay();
        chartXeMay.getData().clear();
        chartOto.getData().clear();
        chartXeMay.setLabelsVisible(true);
        chartOto.setLabelsVisible(true);
        chartOto.getData().add(new PieChart.Data("Đang đỗ", dangdoOto));
        chartOto.getData().add(new PieChart.Data("Trống", TongChoOto - dangdoOto));
        chartXeMay.getData().add(new PieChart.Data("Đang đỗ", dangdoXeMay));
        chartXeMay.getData().add(new PieChart.Data("Trống", TongChoXeMay - dangdoXeMay));
    }

    public void QuanLyNhanVien() {
        try {
            data.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new src.QuanLyNhanVien.Controller().getScene());
        primaryStage.setTitle("Quản lý nhân viên");
    }

    public void QuanLyKhachHang() {
        try {
            data.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new src.QuanLyKhachHang.Controller().getScene());
        primaryStage.setTitle("Quản lý khách hàng");
    }

    public void XemBaoCao() {
        try {
            data.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new src.BaoCao.Controller().getScene(vector));
        primaryStage.setTitle("Xem Báo cáo");
    }

    public void MouseClickOnChart(Event event) {
        try{
            caption.setTextFill(Color.DARKORANGE);
            caption.setStyle("-fx-font: 13 arial; -fx-text-fill: black");
            PieChart pieChart = (PieChart) event.getSource();

            for (final PieChart.Data data : pieChart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        e -> {
                            Platform.runLater(() ->{
                                caption.setTranslateX(e.getSceneX());
                                caption.setTranslateY(e.getSceneY());
                                caption.setText(((int) data.getPieValue())+" xe");
                            });
                        });
            }
        }
        catch (Exception e){}
    }
}