package src.QuanLyNhanVien;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable{
	@FXML
	private TableView<NhanVien> table;
	
	@FXML
	private RadioButton rdoNam;
	
	@FXML
	private RadioButton rdoNu;
	
	@FXML
	private TextField tfSearch;
	
	@FXML 
	private TextField tfID;
	
	@FXML 
	private TextField tfName;
	
	@FXML 
	private TextField tfDiaChi;
	
	@FXML 
	private TextField tfNgaySinh;
	
	@FXML 
	private TextField tfUsername;
	
	@FXML 
	private TextField tfPassword;
	
	@FXML 
	private Button btnRefresh;
	
	@FXML 
	private Button btnThem;
	
	@FXML 
	private Button btnSua;
	
	@FXML 
	private Button btnXoa;
	
	@FXML 
	private TableColumn<NhanVien, Integer> IDColum;
	
	@FXML 
	private TableColumn<NhanVien, String> NameColum;
	
	@FXML 
	private TableColumn<NhanVien, String> GioiTinhColum;
	
	@FXML 
	private TableColumn<NhanVien, String> DiaChiColum;

	@FXML 
	private TableColumn<NhanVien, String> UsernameColum;

	@FXML 
	private TableColumn<NhanVien, String> PasswordColum;

	@FXML 
	private TableColumn<NhanVien, String> NgaySinhColum;
	
	private static ObservableList<NhanVien> dataTable;
	private FilteredList<NhanVien> filtered;
	
	public Scene getScene(Socket socket){
		dataTable = FXCollections.observableArrayList();
		new Data(socket).start();
		Scene scene = null;
		URL url = null;
		try {
			url = new File("src/QuanLyNhanVienfalse/QuanLyNhanVien.fxml").toURL();
			Parent root = FXMLLoader.load(url);
		 	scene = new Scene(root);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/QuanLyNhanVienfalse/QuanLyNhanVienfalse.fxml"));

		return scene;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		IDColum.setCellValueFactory(new PropertyValueFactory<NhanVien, Integer>("ID"));
		NameColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("Name"));
		DiaChiColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("DiaChi"));
		GioiTinhColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("GioiTinh"));
		NgaySinhColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("NgaySinh"));
		UsernameColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("Username"));
		PasswordColum.setCellValueFactory(new PropertyValueFactory<NhanVien, String>("Password"));
		table.setItems(dataTable);
		
		//Tìm kiếm
		filtered = new FilteredList<>(dataTable, p -> true);
		tfSearch.textProperty().addListener((observable, oldValue, newValue) ->{
			 filtered.setPredicate(NhanVien -> {
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }
	                String lowerCaseFilter = newValue.toLowerCase();
	                if (String.valueOf(NhanVien.getID()).contains(lowerCaseFilter)) {
	                    return true;
	                } else if (NhanVien.getName().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                }else if (NhanVien.getDiaChi().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                }
	                else if (NhanVien.getUsername().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                }
	                else if (NhanVien.getNgaySinh().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                }
	                return false;
	         });
		});
		SortedList<NhanVien> sortedData = new SortedList<>(filtered);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
	}
	
	public static void LoadDB(Vector<String[]> data) {
		dataTable.clear();
		for (String[] row : data) {
			int ID = Integer.parseInt(row[0]);
			String Name = row[1];
			String NgaySinh = row[2];
			String GioiTinh = row[3];
			String DiaChi = row[4];
			String Username = row[5];
			String Password = row[6];
			dataTable.add(new NhanVien(ID, Name, GioiTinh, NgaySinh,  DiaChi, Username, Password));
		}
	}
	
	public void ThemNV() {
		int ID = Data.Get_LastedID()+1;
		String Name = tfName.getText();
		String NgaySinh = tfNgaySinh.getText();
		String Username = tfUsername.getText();
		String Password = tfPassword.getText();
		String DiaChi = tfDiaChi.getText();
		String GioiTinh;
		if(rdoNam.isSelected()) GioiTinh = "Nam";
		else GioiTinh = "Nữ";
		Data.SendData("Add_NhanVien N'"+Name+"', '"+NgaySinh+"', N'"+GioiTinh+"', N'"+DiaChi+"', '"+Username+"', '"+Password+"'");
		Data.refresh();
	}
	
	public void XoaNV() {
		int ID = Integer.parseInt(tfID.getText());
		dataTable.remove(table.getSelectionModel().getSelectedItem());
		Data.SendData("Delete_NhanVien "+ ID);
	}
	
	public void CapNhatNV() {
		int ID = Integer.parseInt(tfID.getText());
		String Name = tfName.getText();
		String NgaySinh = tfNgaySinh.getText();
		String Username = tfUsername.getText();
		String Password = tfPassword.getText();
		String DiaChi = tfDiaChi.getText();
		String GioiTinh;
		if(rdoNam.isSelected()) GioiTinh = "Nam";
		else GioiTinh = "Nữ";
		Data.SendData("Update_NhanVien "+ID+", N'"+Name+"', '"+NgaySinh+"', N'"+GioiTinh+"', N'"+DiaChi+"', '"+Username+"', '"+Password+"'");
		Data.refresh();
	}
	
	public void MouseClickOnTable() {
		//tfID.setEditable(false);
		if(table.getSelectionModel().getSelectedItem()==null) return;
		tfID.setText(String.valueOf(IDColum.getCellData(table.getSelectionModel().getSelectedIndex())));
		tfNgaySinh.setText(NgaySinhColum.getCellData(table.getSelectionModel().getSelectedIndex()));
		tfName.setText(NameColum.getCellData(table.getSelectionModel().getSelectedIndex()));
		tfDiaChi.setText(DiaChiColum.getCellData(table.getSelectionModel().getSelectedIndex()));
		tfUsername.setText(UsernameColum.getCellData(table.getSelectionModel().getSelectedIndex()).trim());
		tfPassword.setText(PasswordColum.getCellData(table.getSelectionModel().getSelectedIndex()).trim());
		if(GioiTinhColum.getCellData(table.getSelectionModel().getSelectedIndex()).equals("Nam")) rdoNam.setSelected(true);
		else rdoNu.setSelected(true);
	}
	
	public void Refresh() {
		tfSearch.setText("");
		Data.refresh();
	}
	
}
