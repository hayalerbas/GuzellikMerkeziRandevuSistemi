package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

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

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import javafx.util.Duration;

public class CalisanlarPanelController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<CalisanModel, Integer> idColumn;
    @FXML private TableColumn<CalisanModel, String> adSoyadColumn;
    @FXML private TableColumn<CalisanModel, String> uzmanlikColumn;
    @FXML private TableColumn<CalisanModel, String> telefonColumn;
    @FXML private TableColumn<CalisanModel, String> emailColumn;
    @FXML private TableColumn<CalisanModel, String> durumColumn;

    @FXML private Label aktifCalisanLabel;
    @FXML private Label pasifCalisanLabel;
    @FXML private Label toplamCalisanLabel;
    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    @FXML private Button anaSayfaButton;
    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button randevularButton1;
    @FXML private Button yeniCalisanButton;
    @FXML private Button silCalisanButton;

    @FXML private TableView<CalisanModel> calisanTable;

    private ObservableList<CalisanModel> calisanListesi =
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
                new KeyFrame(
                        Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {

                                takvimlabel.setText(
                                        LocalDate.now().format(
                                                DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                        )
                                );

                                saatlabel.setText(
                                        LocalTime.now().format(
                                                DateTimeFormatter.ofPattern("HH:mm:ss")
                                        )
                                );
                            }
                        }
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void alertGoster(String baslik, String mesaj) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    private void calisanlariYukle() {

        calisanListesi.clear();

        int aktif = 0;
        int pasif = 0;

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT id, ad_soyad, uzmanlik_alani, telefon, email, durum FROM calisanlar";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CalisanModel calisan = new CalisanModel(
                        rs.getInt("id"),
                        rs.getString("ad_soyad"),
                        rs.getString("uzmanlik_alani"),
                        rs.getString("telefon"),
                        rs.getString("email"),
                        rs.getString("durum")
                );

                calisanListesi.add(calisan);

                if (calisan.getDurum() != null
                        && calisan.getDurum().equalsIgnoreCase("Aktif")) {

                    aktif++;

                } else {

                    pasif++;
                }
            }

            calisanTable.setItems(calisanListesi);

            toplamCalisanLabel.setText(String.valueOf(calisanListesi.size()));
            aktifCalisanLabel.setText(String.valueOf(aktif));
            pasifCalisanLabel.setText(String.valueOf(pasif));

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster(
                    "Hata",
                    "Çalışanlar yüklenemedi: " + e.getMessage()
            );
        }
    }

    @FXML
    void calisanSil(ActionEvent event) {

        CalisanModel seciliCalisan =
                calisanTable.getSelectionModel().getSelectedItem();

        if (seciliCalisan == null) {

            alertGoster(
                    "Seçim Yapılmadı",
                    "Lütfen pasif yapılacak çalışanı tablodan seçiniz."
            );

            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "UPDATE calisanlar "
                  + "SET durum = 'Pasif' "
                  + "WHERE id = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(
                    1,
                    seciliCalisan.getId()
            );

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster(
                    "Başarılı",
                    "Çalışan pasif duruma getirildi."
            );

            calisanlariYukle();

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster(
                    "Hata",
                    "Çalışan pasif yapılırken hata oluştu."
            );
        }
    }
    
    @FXML
    void yeniCalisanEkle(ActionEvent event) {

        TextInputDialog adDialog = new TextInputDialog();
        adDialog.setTitle("Yeni Çalışan");
        adDialog.setHeaderText("Çalışan adı soyadı giriniz");
        adDialog.setContentText("Ad Soyad:");

        Optional<String> adSonuc = adDialog.showAndWait();

        if (!adSonuc.isPresent() || adSonuc.get().trim().isEmpty()) {
            return;
        }

        TextInputDialog uzmanlikDialog = new TextInputDialog();
        uzmanlikDialog.setTitle("Yeni Çalışan");
        uzmanlikDialog.setHeaderText("Uzmanlık alanı giriniz");
        uzmanlikDialog.setContentText("Uzmanlık Alanı:");

        Optional<String> uzmanlikSonuc = uzmanlikDialog.showAndWait();

        if (!uzmanlikSonuc.isPresent() || uzmanlikSonuc.get().trim().isEmpty()) {
            return;
        }

        TextInputDialog telefonDialog = new TextInputDialog();
        telefonDialog.setTitle("Yeni Çalışan");
        telefonDialog.setHeaderText("Telefon giriniz");
        telefonDialog.setContentText("Telefon:");

        Optional<String> telefonSonuc = telefonDialog.showAndWait();

        if (!telefonSonuc.isPresent() || telefonSonuc.get().trim().isEmpty()) {
            return;
        }

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Yeni Çalışan");
        emailDialog.setHeaderText("E-posta giriniz");
        emailDialog.setContentText("E-posta:");

        Optional<String> emailSonuc = emailDialog.showAndWait();

        if (!emailSonuc.isPresent() || emailSonuc.get().trim().isEmpty()) {
            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO calisanlar "
                            + "(ad_soyad, uzmanlik_alani, telefon, email, durum) "
                            + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, adSonuc.get().trim());
            ps.setString(2, uzmanlikSonuc.get().trim());
            ps.setString(3, telefonSonuc.get().trim());
            ps.setString(4, emailSonuc.get().trim());
            ps.setString(5, "Aktif");

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster("Başarılı", "Yeni çalışan başarıyla eklendi.");

            calisanlariYukle();

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster(
                    "Hata",
                    "Çalışan eklenemedi: " + e.getMessage()
            );
        }
    }

    @FXML
    void calisanlariGoster(ActionEvent event) {

        calisanlariYukle();
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
    void randevulariListele(ActionEvent event) {

        try {
            sayfaAc(event, "RandevuPanel.fxml");
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

    @FXML
    void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, Integer>("id")
        );

        adSoyadColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, String>("adSoyad")
        );

        uzmanlikColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, String>("uzmanlikAlani")
        );

        telefonColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, String>("telefon")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, String>("email")
        );

        durumColumn.setCellValueFactory(
                new PropertyValueFactory<CalisanModel, String>("durum")
        );

        saatTarihBaslat();
        calisanlariYukle();
    }
}