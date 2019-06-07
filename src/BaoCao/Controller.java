package src.BaoCao;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;

@SuppressWarnings("ALL")
public class Controller implements Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private ChoiceBox<Integer> cbThang;
    @FXML
    private ChoiceBox<Integer> cbNam;
    @FXML
    private ChoiceBox<String> cbTenBai;
    @FXML
    private ChoiceBox<Integer> cbIDBai;
    @FXML
    private Button btnTroVe;
    @FXML
    private LineChart<String, Integer> chart;

    private Label caption;

    @FXML
    private PieChart chartTongXe;

    private static PieChart pieChart;
    private static LineChart<String, Integer> chartCopy;

    private static XYChart.Series<String, Integer> seriesXeMay, seriesOto;

    private static Vector<String[]> vector;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caption = new Label("");
        Platform.runLater(() ->{pane.getChildren().addAll(caption);});
        chartCopy = chart;
        pieChart = chartTongXe;
        for (int i = 1; i <= 12; i++) cbThang.getItems().add(i);
        for (int i = 2017; i <= 2022; i++) cbNam.getItems().add(i);
        seriesXeMay = new XYChart.Series<String, Integer>();
        seriesOto = new XYChart.Series<String, Integer>();
        seriesXeMay.setName("Xe máy");
        seriesOto.setName("Ô tô");
        Platform.runLater(() -> {
            chartCopy.getData().addAll(seriesXeMay, seriesOto);
        });

        for (String[] row : vector) {
            cbIDBai.getItems().add(Integer.parseInt(row[0]));
            cbTenBai.getItems().add(row[1]);
        }
        cbTenBai.setOnAction(actionEvent -> {
            cbIDBai.getSelectionModel().select(cbTenBai.getSelectionModel().getSelectedIndex());
            changeChoiceBox();
        });
        cbIDBai.setOnAction(actionEvent -> {
            cbTenBai.getSelectionModel().select(cbIDBai.getSelectionModel().getSelectedIndex());
            changeChoiceBox();
        });
        cbThang.setOnAction(actionEvent -> {
            changeChoiceBox();
        });
        cbNam.setOnAction(actionEvent -> {
            changeChoiceBox();
        });


        cbIDBai.getSelectionModel().selectFirst();
        Calendar calendar = Calendar.getInstance();
        cbThang.getSelectionModel().select(calendar.getTime().getMonth());
        cbNam.getSelectionModel().select(2);
    }

    public Scene getScene(Vector data) {
        vector = data;
        new Data().start();
        Scene scene = null;
        URL url = null;
        try {
            url = Controller.class.getClassLoader().getResource("src/BaoCao/BaoCao.fxml");
            Parent root = FXMLLoader.load(url);
            scene = new Scene(root);
        } catch (Exception e) {
        }
        return scene;
    }

    public static void LoadDB(Vector<String[]> data) {
        if (data == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Thông báo");
            alert.setContentText("Không có dữ liệu");
            alert.showAndWait();
        } else {
            if (seriesOto == null || seriesXeMay == null) {
                seriesXeMay = new XYChart.Series<String, Integer>();
                seriesOto = new XYChart.Series<String, Integer>();
            }
            Platform.runLater(() -> {

                seriesOto.getData().clear();
                seriesXeMay.getData().clear();
                int tongXeMay = 0, tongOto = 0;
                for (int i = 1; i <= 31; i++) {
                    int kt = 0;
                    for (String[] row : data) {
                        if (Integer.parseInt(row[0]) == i)
                            if (row[2].trim().equals("Ô tô")) {
                                seriesOto.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), Integer.parseInt(row[1])));
                                tongOto+=Integer.parseInt(row[1]);
                                kt++;
                            } else {
                                seriesXeMay.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), Integer.parseInt(row[1])));
                                tongXeMay+=Integer.parseInt(row[1]);
                                kt++;
                            }
                    }
                    if (kt == 0) {
                        seriesOto.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), 0));
                        seriesXeMay.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), 0));
                    }
                }
                pieChart.getData().clear();
                PieChart.Data dataOto = new PieChart.Data("Ô tô", tongOto);
                PieChart.Data dataXeMay = new PieChart.Data("Xe máy", tongXeMay);
                pieChart.getData().add(dataOto);
                pieChart.getData().add(dataXeMay);
            });
        }
    }

    public void changeChoiceBox() {
        try {
            int id = cbIDBai.getSelectionModel().getSelectedItem();
            int thang = cbThang.getSelectionModel().getSelectedItem();
            int nam = cbNam.getSelectionModel().getSelectedItem();
            if (id != 0 && thang != 0) Data.refresh(id, thang, nam);
        } catch (Exception e) {
        }
    }

    public void TroVe() {
        Stage primaryStage = src.application.Main.getPrimaryStages();
        primaryStage.setScene(new src.QuanLy.Controller().getScene(primaryStage));
        primaryStage.setTitle("Quản lý bãi");
    }

    public void MouseClickOnChart(Event event) {
        try {
            caption.setTextFill(Color.DARKORANGE);
            caption.setStyle("-fx-font: 13 arial; -fx-text-fill: black");
            PieChart pieChart = (PieChart) event.getSource();

            for (final PieChart.Data data : pieChart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        e -> {
                            Platform.runLater(() -> {
                                caption.setTranslateX(e.getSceneX());
                                caption.setTranslateY(e.getSceneY());
                                caption.setText(((int) data.getPieValue()) + " xe");
                            });
                        });
            }

        } catch (Exception e) {}
    }
}
