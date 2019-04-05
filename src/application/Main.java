package application;
	
import QuanLyNhanVien.Controller;
import Request.ConnectServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Parent root = FXMLLoader.load(getClass().getResource("/QuanLyNhanVien/QuanLyNhanVien.fxml"));
			ConnectServer connectServer = new ConnectServer();
			primaryStage.setTitle("Quản lý nhân viên");
	        primaryStage.setScene(new Controller().getScene(connectServer.getSocket()));
	        primaryStage.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
