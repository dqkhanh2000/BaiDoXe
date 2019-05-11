package application;
	
import Request.ConnectServer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import src.QuanLy.Data;
import src.QuanLyVaoRa.Controller;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.Vector;


public class Main extends Application {
	public static int ID_ChuBai, ID_NhanVien;
	private static Socket socket;
	public Optional<Pair<String, String>> result;
	int resultLogin = 0;

	@Override
	public void start(Stage primaryStage) {
	    primaryStage.setOnCloseRequest(windowEvent -> {
	        System.exit(1);
        });
//		src.QuanLyVaoRa.Controller ctl = new Controller();
//		primaryStage.setScene(ctl.getScene());
//		primaryStage.setTitle("Quản lý bãi");
//		primaryStage.show();
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
				else if (dialogButton == ButtonType.CLOSE) System.exit(0);
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
			if(resultLogin == 3){
				ID_ChuBai = getID(Username.getText());
				primaryStage.setScene(new src.QuanLy.Controller().getScene(primaryStage));
				primaryStage.setTitle("Quản lý bãi");
			}
			else if(resultLogin == 2){
                ID_NhanVien = getID(Username.getText());
				Dialog<Integer> ChonBai = new Dialog<>();
				ChonBai.getDialogPane().getButtonTypes().add(ButtonType.OK);
				GridPane pane = new GridPane();
                pane.setHgap(10);
                pane.setVgap(10);
                pane.setPadding(new Insets(10));
                pane.add(new Label("ID Bãi"),0,1);
                pane.add(new Label("Tên Bãi"),1,1);
                pane.add(new Label("Địa chỉ"),2,1);
				ToggleGroup group = new ToggleGroup();
                DataOutputStream os = null;
                Vector<String[]> data = null;
                try{
                    os = new DataOutputStream(socket.getOutputStream());
                    os.writeUTF("Get_Info_Bai "+ ID_ChuBai);
                    data = (Vector<String[]>) new ObjectInputStream(socket.getInputStream()).readObject();
                }catch (Exception e){}

                int i = 2;
                for (String[] row : data) {
                    RadioButton radioButton = new RadioButton(row[0]);
                    radioButton.setToggleGroup(group);
                    pane.add(radioButton, 0,i);
                    pane.add(new Label(row[1]), 1, i);
                    pane.add(new Label(row[2]), 2, i);
                    i++;
                }
                ChonBai.setResultConverter(buttonType1 -> {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    return Integer.parseInt(button.getText());
                });
                ChonBai.getDialogPane().setContent(pane);
                ChonBai.setTitle("Chọn bãi");
				Vector<String[]> finalData = data;
				ChonBai.showAndWait().ifPresent(integer -> {
					primaryStage.setScene(new src.QuanLyVaoRa.Controller().getScene(integer));
					primaryStage.setTitle("Quản lý bãi");
					for (String[] s: finalData){
                		if(integer == Integer.parseInt(s[0])){
                			Controller.setTongXemay(Integer.parseInt(s[3]));
                			Controller.setTongOto(Integer.parseInt(s[4]));
                			Controller.setDangdoXeMay(Integer.parseInt(s[5]));
                			Controller.setDangdoOto(Integer.parseInt(s[6]));
                			Controller.setGiaXemay(Double.parseDouble(s[7]));
                			Controller.setGiaOto(Double.parseDouble(s[8]));
						}
					}
                });
//
			}
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int CheckLogin(String Username, String Password){
		socket = new ConnectServer().getSocket();
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
	private int getID(String Username){
		DataInputStream is;
		DataOutputStream os;
		int ID = -1;
		try {
			is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF("Get_ID "+Username);
			String recive = is.readUTF();
			ID = Integer.parseInt(recive);
			ID_ChuBai = Integer.parseInt(is.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ID;
	}
	public static int GetID_ChuBai(){
		return ID_ChuBai;
	}

    public static int getID_NhanVien() {
        return ID_NhanVien;
    }

    public static void main(String[] args) {
		launch(args);
	}
}
