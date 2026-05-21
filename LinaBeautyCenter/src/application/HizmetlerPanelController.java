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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HizmetlerPanelController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Label aktifHizmetLabel;
    @FXML private Label pasifHizmetLabel;
    @FXML private Label toplamHizmetLabel;
    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    @FXML private Button anaSayfaButton;
    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button randevularButton1;
    @FXML private Button yeniHizmetButton;

    @FXML private TextField hizmetAraField;

    @FXML private TableView<HizmetModel> hizmetlerTable;

    @FXML private TableColumn<HizmetModel, Integer> idColumn;
    @FXML private TableColumn<HizmetModel, String> hizmetadiColumn;
    @FXML private TableColumn<HizmetModel, String> kategoriColumn;
    @FXML private TableColumn<HizmetModel, Double> fiyatColumn;
    @FXML private TableColumn<HizmetModel, String> sureColumn;
    @FXML private TableColumn<HizmetModel, String> durumColumn;

    @FXML private Label toplamRandevuLabel1;
    @FXML private Label toplamRandevuLabel11;
    @FXML private Label toplamRandevuLabel111;

    private ObservableList<HizmetModel> hizmetListesi =
            FXCollections.observableArrayList();

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/" + fxmlAdi));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saatTarihBaslat() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        takvimlabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                        saatlabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    }
                })
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
    
    @FXML
    void hizmetSil(ActionEvent event) {

        HizmetModel seciliHizmet =
                hizmetlerTable.getSelectionModel().getSelectedItem();

        if (seciliHizmet == null) {

            alertGoster(
                    "Seçim Yapılmadı",
                    "Lütfen pasif yapılacak hizmeti tablodan seçiniz."
            );

            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "UPDATE hizmetler "
                  + "SET durum = 'Pasif' "
                  + "WHERE id = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, seciliHizmet.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster(
                    "Başarılı",
                    "Hizmet pasif duruma getirildi."
            );

            hizmetleriYukle();

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster(
                    "Hata",
                    "Hizmet pasif yapılırken hata oluştu."
            );
        }
    }
    
    

    private void hizmetleriYukle() {
        hizmetListesi.clear();

        int aktif = 0;
        int pasif = 0;

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT id, hizmet_adi, kategori, fiyat, sure, durum FROM hizmetler";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HizmetModel hizmet = new HizmetModel(
                        rs.getInt("id"),
                        rs.getString("hizmet_adi"),
                        rs.getString("kategori"),
                        rs.getDouble("fiyat"),
                        rs.getString("sure"),
                        rs.getString("durum")
                );

                hizmetListesi.add(hizmet);

                if (hizmet.getDurum() != null
                        && hizmet.getDurum().equalsIgnoreCase("Aktif")) {
                    aktif++;
                } else {
                    pasif++;
                }
            }

            hizmetlerTable.setItems(hizmetListesi);

            toplamHizmetLabel.setText(String.valueOf(hizmetListesi.size()));
            aktifHizmetLabel.setText(String.valueOf(aktif));
            pasifHizmetLabel.setText(String.valueOf(pasif));

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Hizmetler yüklenemedi: " + e.getMessage());
        }
    }

    @FXML
    void yeniHizmetEkle(ActionEvent event) {

        TextInputDialog adDialog = new TextInputDialog();
        adDialog.setTitle("Yeni Hizmet");
        adDialog.setHeaderText("Hizmet adı giriniz");
        adDialog.setContentText("Hizmet Adı:");
        Optional<String> adSonuc = adDialog.showAndWait();

        if (!adSonuc.isPresent() || adSonuc.get().trim().isEmpty()) return;

        TextInputDialog kategoriDialog = new TextInputDialog();
        kategoriDialog.setTitle("Yeni Hizmet");
        kategoriDialog.setHeaderText("Kategori giriniz");
        kategoriDialog.setContentText("Kategori:");
        Optional<String> kategoriSonuc = kategoriDialog.showAndWait();

        if (!kategoriSonuc.isPresent() || kategoriSonuc.get().trim().isEmpty()) return;

        TextInputDialog fiyatDialog = new TextInputDialog();
        fiyatDialog.setTitle("Yeni Hizmet");
        fiyatDialog.setHeaderText("Fiyat giriniz");
        fiyatDialog.setContentText("Fiyat:");
        Optional<String> fiyatSonuc = fiyatDialog.showAndWait();

        if (!fiyatSonuc.isPresent() || fiyatSonuc.get().trim().isEmpty()) return;

        TextInputDialog sureDialog = new TextInputDialog();
        sureDialog.setTitle("Yeni Hizmet");
        sureDialog.setHeaderText("Süre giriniz");
        sureDialog.setContentText("Süre örnek: 60 dk.");
        Optional<String> sureSonuc = sureDialog.showAndWait();

        if (!sureSonuc.isPresent() || sureSonuc.get().trim().isEmpty()) return;

        try {
            double fiyat = Double.parseDouble(fiyatSonuc.get().trim());

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO hizmetler "
                            + "(hizmet_adi, kategori, fiyat, sure, durum) "
                            + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, adSonuc.get().trim());
            ps.setString(2, kategoriSonuc.get().trim());
            ps.setDouble(3, fiyat);
            ps.setString(4, sureSonuc.get().trim());
            ps.setString(5, "Aktif");

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster("Başarılı", "Yeni hizmet başarıyla eklendi.");
            hizmetleriYukle();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Hizmet eklenemedi: " + e.getMessage());
        }
    }

    @FXML void hizmetleriGoster(ActionEvent event) { hizmetleriYukle(); }

    @FXML
    void dashboardGoster(ActionEvent event) {
        try { sayfaAc(event, "AdminPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void randevulariListele(ActionEvent event) {
        try { sayfaAc(event, "RandevuPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void musterileriGoster(ActionEvent event) {
        try { sayfaAc(event, "MusteriPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void calisanlariGoster(ActionEvent event) {
        try { sayfaAc(event, "CalisanlarPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void mesajlarGoster(ActionEvent event) {
        try { sayfaAc(event, "Adminmesajlar.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void cikisYap(ActionEvent event) {
        try { sayfaAc(event, "AnaSayfa.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, Integer>("id"));
        hizmetadiColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, String>("hizmetAdi"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, String>("kategori"));
        fiyatColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, Double>("fiyat"));
        sureColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, String>("sure"));
        durumColumn.setCellValueFactory(new PropertyValueFactory<HizmetModel, String>("durum"));

        saatTarihBaslat();
        hizmetleriYukle();
    }
}