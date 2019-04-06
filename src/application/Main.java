package application;
	
import src.QuanLyNhanVien.Controller;
import Request.ConnectServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;


public class Main extends Application {
	private Socket socket;
	public Optional<Pair<String, String>> result;
	int resultLogin = 0;
	@Override
	public void start(Stage primaryStage) {
		try {

			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Login");
			ButtonType buttonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CLOSE);

			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			gridPane.setPadding(new Insets(10));

			TextField Username = new TextField();
			Username.setPromptText("Username");
			Username.setMinWidth(200);
			PasswordField Password = new PasswordField();
			Password.setPromptText("Password");

			gridPane.add(new Label("Username"),0,0);
			gridPane.add(Username,1,0);
			gridPane.add(new Label("Password"),0,1);
			gridPane.add(Password,1,1);

			Node loginButton = dialog.getDialogPane().lookupButton(buttonType);
			loginButton.setDisable(true);

			Username.textProperty().addListener((observableUser, oldValueUser, newValueUser) -> {
				Password.textProperty().addListener((observablePass, oldValuePass, newValuePass) -> {
					if(!newValueUser.trim().isEmpty()) loginButton.setDisable(newValuePass.trim().isEmpty());
				});
			});
			dialog.getDialogPane().setContent(gridPane);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == buttonType){
					return new Pair<>(Username.getText(), Password.getText());
				}
				return null;
			} );

			result = dialog.showAndWait();
			result.ifPresent(dataResult -> {
				resultLogin = CheckLogin(dataResult.getKey(),dataResult.getValue());
			});
			while (resultLogin == 0){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Thông báo");
				alert.setHeaderText("Sai tên đăng nhập hoặc mật khẩu");
				alert.showAndWait();
				result = dialog.showAndWait();
				result.ifPresent(dataResult -> {
					resultLogin = CheckLogin(dataResult.getKey(),dataResult.getValue());
				});
			}
			if(resultLogin == 2){
				primaryStage.setScene(new Controller().getScene(socket));
				primaryStage.setTitle("Quản lý nhân viên");
			}
			primaryStage.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int CheckLogin(String Username, String Password){
		new ConnectServer();
		socket = ConnectServer.getSocket();
		DataInputStream is;
		DataOutputStream os;
		int result = -1;
		try {
			is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF("Check_Login "+Username+" "+Password);
			String recive = is.readUTF();
			result = Integer.parseInt(String.copyValueOf(recive.toCharArray(), recive.lastIndexOf(" ")+1, 1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
