package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RandevuPanelController {

    @FXML private TableColumn<RandevuModel, Integer> idColumn;
    @FXML private TableColumn<RandevuModel, String> adSoyadColumn;
    @FXML private TableColumn<RandevuModel, String> telefonColumn;
    @FXML private TableColumn<RandevuModel, String> hizmetColumn;
    @FXML private TableColumn<RandevuModel, String> uzmanColumn;
    @FXML private TableColumn<RandevuModel, String> tarihColumn;
    @FXML private TableColumn<RandevuModel, String> saatColumn;
    @FXML private TableColumn<RandevuModel, String> notColumn;
    @FXML private TableColumn<RandevuModel, String> durumColumn;

    @FXML private TableView<RandevuModel> randevuTable;

    @FXML private Button anaSayfaButton;
    @FXML private Button bekleyenButton;
    @FXML private Button onaylananButton;
    @FXML private Button tumRandevularButton;
    @FXML private Button onaylaButton;
    @FXML private Button iptalbutton;
    @FXML private Button silbutton;

    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button randevularButton1;

    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    private ObservableList<RandevuModel> liste =
            FXCollections.observableArrayList();

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/application/" + fxmlAdi)
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saatTarihBaslat() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        takvimlabel.setText(
                                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        );

                        saatlabel.setText(
                                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        );
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void randevulariYukle() {
        liste.clear();

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM randevular";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(new RandevuModel(
                        rs.getInt("id"),
                        rs.getString("ad_soyad"),
                        rs.getString("telefon"),
                        rs.getString("hizmet"),
                        rs.getString("uzman"),
                        rs.getString("tarih"),
                        rs.getString("saat"),
                        rs.getString("notunuz"),
                        rs.getString("durum")
                ));
            }

            randevuTable.setItems(liste);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Randevular yüklenemedi: " + e.getMessage());
        }
    }

    private void durumGuncelle(String yeniDurum) {
        RandevuModel secili =
                randevuTable.getSelectionModel().getSelectedItem();

        if (secili == null) {
            alertGoster("Uyarı", "Lütfen bir randevu seçiniz.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "UPDATE randevular SET durum = ? WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, yeniDurum);
            ps.setInt(2, secili.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster("Başarılı", "Randevu durumu güncellendi.");
            randevulariYukle();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Durum güncellenemedi: " + e.getMessage());
        }
    }

    @FXML
    void onaylaButton(ActionEvent event) {
        durumGuncelle("Onaylandı");
    }

    @FXML
    void iptalbutton(ActionEvent event) {
        durumGuncelle("İptal");
    }

    @FXML
    void silbutton(ActionEvent event) {
        RandevuModel secili =
                randevuTable.getSelectionModel().getSelectedItem();

        if (secili == null) {
            alertGoster("Uyarı", "Lütfen silinecek randevuyu seçiniz.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "DELETE FROM randevular WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, secili.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster("Başarılı", "Randevu silindi.");
            randevulariYukle();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Randevu silinemedi: " + e.getMessage());
        }
    }

    @FXML
    void bekleyenButton(ActionEvent event) {
        ObservableList<RandevuModel> filtre =
                FXCollections.observableArrayList();

        for (RandevuModel r : liste) {
            if (r.getDurum() != null &&
                    (r.getDurum().equalsIgnoreCase("Bekliyor")
                            || r.getDurum().equalsIgnoreCase("Beklemede"))) {
                filtre.add(r);
            }
        }

        randevuTable.setItems(filtre);
    }

    @FXML
    void onaylananButton(ActionEvent event) {
        ObservableList<RandevuModel> filtre =
                FXCollections.observableArrayList();

        for (RandevuModel r : liste) {
            if (r.getDurum() != null &&
                    r.getDurum().equalsIgnoreCase("Onaylandı")) {
                filtre.add(r);
            }
        }

        randevuTable.setItems(filtre);
    }

    @FXML
    void tumRandevularButton(ActionEvent event) {
        randevulariYukle();
    }

    @FXML
    void randevulariListele(ActionEvent event) {
        randevulariYukle();
    }

    @FXML
    void dashboardGoster(ActionEvent event) {
        try {
            sayfaAc(event, "AdminPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void musterileriGoster(ActionEvent event) {
        try {
            sayfaAc(event, "MusteriPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void hizmetleriGoster(ActionEvent event) {
        try {
            sayfaAc(event, "HizmetlerPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void calisanlariGoster(ActionEvent event) {
        try {
            sayfaAc(event, "CalisanlarPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajlarGoster(ActionEvent event) {
        try {
            sayfaAc(event, "Adminmesajlar.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cikisYap(ActionEvent event) {
        try {
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adSoyadColumn.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        hizmetColumn.setCellValueFactory(new PropertyValueFactory<>("hizmet"));
        uzmanColumn.setCellValueFactory(new PropertyValueFactory<>("uzman"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        saatColumn.setCellValueFactory(new PropertyValueFactory<>("saat"));
        notColumn.setCellValueFactory(new PropertyValueFactory<>("notunuz"));
        durumColumn.setCellValueFactory(new PropertyValueFactory<>("durum"));

        saatTarihBaslat();
        randevulariYukle();
    }
}