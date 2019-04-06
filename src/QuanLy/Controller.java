package src.QuanLy;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Controller{
    @FXML
    private TableView<BaiDoXe> table;

    @FXML
    private TableColumn IDColumn;

    @FXML
    private TableColumn DiaChiColumn;

    @FXML
    private TableColumn XeMayColumn;

    @FXML
    private TableColumn OtoColumn;

    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private TextField tfChoXeMay;
    @FXML
    private TextField tfChoOto;

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

    //private


}