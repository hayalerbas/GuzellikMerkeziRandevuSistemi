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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminmesajlarController {

    @FXML private Label aktifMusteriAdLabel;
    @FXML private Label aktifMusteriEmailLabel;
    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    @FXML private Button anaSayfaButton;
    @FXML private Button arsivleButton;
    @FXML private Button arsivlenenButton;
    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button gonderButton;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button okunmamisButton;
    @FXML private Button randevularButton;
    @FXML private Button silButton;
    @FXML private Button tumuButton;
    @FXML private Button yanitlananButton;
    @FXML private Button yenileButton;

    @FXML private TextArea mesajYazArea;

    @FXML private VBox mesajKartlariVBox;
    @FXML private VBox mesajlarVBox;

    @FXML private ScrollPane mesajListeScrollPane;
    @FXML private ScrollPane mesajScrollPane;

    @FXML private Pane mesajAnaPane;
    @FXML private Pane mesajListePane;

    private int aktifMusteriId = 0;

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

    private void mesajKartlariniYukle(String filtre) {
        mesajKartlariVBox.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql;

            if ("Okunmamış".equals(filtre)) {
                sql = "SELECT DISTINCT m.musteri_id, mu.ad_soyad, mu.email "
                        + "FROM mesajlar m "
                        + "JOIN musteriler mu ON m.musteri_id = mu.id "
                        + "WHERE m.okundu = false AND m.gonderen_tipi = 'musteri' "
                        + "ORDER BY m.musteri_id DESC";
            } else if ("Yanıtlanan".equals(filtre)) {
                sql = "SELECT DISTINCT m.musteri_id, mu.ad_soyad, mu.email "
                        + "FROM mesajlar m "
                        + "JOIN musteriler mu ON m.musteri_id = mu.id "
                        + "WHERE m.gonderen_tipi = 'admin' "
                        + "ORDER BY m.musteri_id DESC";
            } else if ("Arşivlenen".equals(filtre)) {
                sql = "SELECT DISTINCT m.musteri_id, mu.ad_soyad, mu.email "
                        + "FROM mesajlar m "
                        + "JOIN musteriler mu ON m.musteri_id = mu.id "
                        + "WHERE m.arsiv = true "
                        + "ORDER BY m.musteri_id DESC";
            } else {
                sql = "SELECT DISTINCT m.musteri_id, mu.ad_soyad, mu.email "
                        + "FROM mesajlar m "
                        + "JOIN musteriler mu ON m.musteri_id = mu.id "
                        + "WHERE m.arsiv = false "
                        + "ORDER BY m.musteri_id DESC";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int musteriId = rs.getInt("musteri_id");
                String adSoyad = rs.getString("ad_soyad");
                String email = rs.getString("email");

                Pane kart = mesajKartOlustur(
                        musteriId,
                        adSoyad,
                        email,
                        sonMesajiGetir(musteriId),
                        sonSaatGetir(musteriId)
                );

                mesajKartlariVBox.getChildren().add(kart);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Mesaj kartları yüklenemedi: " + e.getMessage());
        }
    }

    private Pane mesajKartOlustur(int musteriId, String adSoyad, String email, String sonMesaj, String saat) {
        Pane kart = new Pane();

        kart.setPrefWidth(260);
        kart.setPrefHeight(80);
        kart.setStyle(
                "-fx-background-color: rgba(245,235,220,0.85);"
                        + "-fx-background-radius: 16;"
                        + "-fx-cursor: hand;"
        );

        Label adLabel = new Label(adSoyad);
        adLabel.setLayoutX(18);
        adLabel.setLayoutY(12);
        adLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2d1b12;");

        Label mesajLabel = new Label(sonMesaj);
        mesajLabel.setLayoutX(18);
        mesajLabel.setLayoutY(40);
        mesajLabel.setPrefWidth(165);
        mesajLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a5c49;");

        Label saatLabel = new Label(saat);
        saatLabel.setLayoutX(205);
        saatLabel.setLayoutY(14);
        saatLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #b77a18; -fx-font-weight: bold;");

        kart.getChildren().addAll(adLabel, mesajLabel, saatLabel);

        kart.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                aktifMusteriId = musteriId;

                aktifMusteriAdLabel.setText(adSoyad);

                aktifMusteriEmailLabel.setText(email);

                mesajlariYukle(musteriId);

                mesajlariOkunduYap(musteriId);
            }
        });

        return kart;
    }

    private String sonMesajiGetir(int musteriId) {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT mesaj FROM mesajlar "
                            + "WHERE musteri_id = ? "
                            + "ORDER BY tarih DESC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musteriId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String mesaj = rs.getString("mesaj");

                rs.close();
                ps.close();
                conn.close();

                if (mesaj != null && mesaj.length() > 25) {
                    return mesaj.substring(0, 25) + "...";
                }

                return mesaj;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String sonSaatGetir(int musteriId) {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT TIME_FORMAT(tarih, '%H:%i') AS saat "
                            + "FROM mesajlar "
                            + "WHERE musteri_id = ? "
                            + "ORDER BY tarih DESC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musteriId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String saat = rs.getString("saat");

                rs.close();
                ps.close();
                conn.close();

                return saat;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private void mesajlariYukle(int musteriId) {
        mesajlarVBox.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT * FROM mesajlar "
                            + "WHERE musteri_id = ? "
                            + "ORDER BY tarih ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musteriId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mesaj = rs.getString("mesaj");
                String gonderen = rs.getString("gonderen_tipi");

                boolean adminMi = "admin".equalsIgnoreCase(gonderen);

                mesajBalonuEkle(mesaj, adminMi);
            }

            rs.close();
            ps.close();
            conn.close();

            mesajScrollPane.setVvalue(1.0);

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Mesajlar yüklenemedi: " + e.getMessage());
        }
    }

    private void mesajBalonuEkle(String mesaj, boolean adminMi) {
        HBox satir = new HBox();
        satir.setPrefWidth(600);
        satir.setPadding(new Insets(5, 20, 5, 20));

        Label mesajLabel = new Label(mesaj);
        mesajLabel.setWrapText(true);
        mesajLabel.setMaxWidth(330);
        mesajLabel.setStyle(
                "-fx-padding: 12;"
                        + "-fx-background-radius: 18;"
                        + "-fx-font-size: 14px;"
                        + "-fx-text-fill: #2d1b12;"
        );

        if (adminMi) {
            satir.setAlignment(Pos.CENTER_RIGHT);
            mesajLabel.setStyle(mesajLabel.getStyle() + "-fx-background-color: #eef6ea;");
        } else {
            satir.setAlignment(Pos.CENTER_LEFT);
            mesajLabel.setStyle(mesajLabel.getStyle() + "-fx-background-color: #f8efe5;");
        }

        satir.getChildren().add(mesajLabel);
        mesajlarVBox.getChildren().add(satir);
    }

    private void mesajlariOkunduYap(int musteriId) {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "UPDATE mesajlar SET okundu = true "
                            + "WHERE musteri_id = ? AND gonderen_tipi = 'musteri'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, musteriId);
            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajGonder(ActionEvent event) {
        if (aktifMusteriId == 0) {
            alertGoster("Uyarı", "Lütfen önce bir müşteri seçiniz.");
            return;
        }

        String mesaj = mesajYazArea.getText().trim();

        if (mesaj.isEmpty()) {
            alertGoster("Uyarı", "Mesaj alanı boş olamaz.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO mesajlar "
                            + "(musteri_id, gonderen_tipi, mesaj, okundu, arsiv) "
                            + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, aktifMusteriId);
            ps.setString(2, "admin");
            ps.setString(3, mesaj);
            ps.setBoolean(4, true);
            ps.setBoolean(5, false);

            ps.executeUpdate();

            ps.close();
            conn.close();

            mesajYazArea.clear();

            mesajlariYukle(aktifMusteriId);
            mesajKartlariniYukle("Tümü");

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster("Hata", "Mesaj gönderilemedi: " + e.getMessage());
        }
    }

    @FXML
    void mesajiArsivle(ActionEvent event) {
        if (aktifMusteriId == 0) {
            alertGoster("Uyarı", "Lütfen arşivlenecek sohbeti seçiniz.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "UPDATE mesajlar SET arsiv = true WHERE musteri_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aktifMusteriId);
            ps.executeUpdate();

            ps.close();
            conn.close();

            aktifMusteriId = 0;
            aktifMusteriAdLabel.setText("Müşteri seçilmedi");
            aktifMusteriEmailLabel.setText("-");
            mesajlarVBox.getChildren().clear();

            mesajKartlariniYukle("Tümü");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajiSil(ActionEvent event) {
        if (aktifMusteriId == 0) {
            alertGoster("Uyarı", "Lütfen silinecek sohbeti seçiniz.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "DELETE FROM mesajlar WHERE musteri_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aktifMusteriId);
            ps.executeUpdate();

            ps.close();
            conn.close();

            aktifMusteriId = 0;
            aktifMusteriAdLabel.setText("Müşteri seçilmedi");
            aktifMusteriEmailLabel.setText("-");
            mesajlarVBox.getChildren().clear();

            mesajKartlariniYukle("Tümü");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void tumMesajlariGoster(ActionEvent event) {
        mesajKartlariniYukle("Tümü");
    }

    @FXML
    void okunmamisMesajlariGoster(ActionEvent event) {
        mesajKartlariniYukle("Okunmamış");
    }

    @FXML
    void yanitlananMesajlariGoster(ActionEvent event) {
        mesajKartlariniYukle("Yanıtlanan");
    }

    @FXML
    void arsivlenenMesajlariGoster(ActionEvent event) {
        mesajKartlariniYukle("Arşivlenen");
    }

    @FXML
    void mesajlariYenile(ActionEvent event) {
        mesajKartlariniYukle("Tümü");

        if (aktifMusteriId != 0) {
            mesajlariYukle(aktifMusteriId);
        }
    }

    @FXML
    void mesajlarGoster(ActionEvent event) {
        mesajKartlariniYukle("Tümü");
    }

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
    void hizmetleriGoster(ActionEvent event) {
        try { sayfaAc(event, "HizmetlerPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void calisanlariGoster(ActionEvent event) {
        try { sayfaAc(event, "CalisanlarPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void cikisYap(ActionEvent event) {
        try { sayfaAc(event, "AnaSayfa.fxml"); } catch (Exception e) { e.printStackTrace(); }
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
        aktifMusteriId = 0;
        aktifMusteriAdLabel.setText("Müşteri seçilmedi");
        aktifMusteriEmailLabel.setText("-");

        saatTarihBaslat();
        mesajKartlariniYukle("Tümü");
    }
}