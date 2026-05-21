package application;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KullaniciPanelController {

    @FXML private Button anaSayfaButton, cikisButton, mesajlarButton, profilDuzenleButton,
            profilGoruntuleButton, profilMenuButton, randevuAlMenuButton,
            randevularimMenuButton, randevularimiGorButton, tumRandevularButton,
            yaklasanDetayButton, yeniRandevuButton;

    @FXML private Label hosgeldinizLabel, profilAdSoyadLabel, profilEmailLabel,
            profilTelefonLabel, saatlabel, tarihLabel, sonRandevuHizmetLabel,
            sonRandevuSaatLabel, sonRandevuTarihLabel, sonRandevuUzmanLabel,
            yaklasanHizmetLabel, yaklasanSaatLabel, yaklasanTarihLabel, yaklasanUzmanLabel;

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/" + fxmlAdi));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saatTarihBaslat() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                saatlabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                tarihLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void profilYukle() {
        try {
            Connection conn = DatabaseConnection.baglan();
            String sql = "SELECT * FROM musteriler WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String adSoyad = rs.getString("ad_soyad");
                String email = rs.getString("email");
                String telefon = rs.getString("telefon");

                hosgeldinizLabel.setText("Hoş geldiniz, " + adSoyad);
                profilAdSoyadLabel.setText(adSoyad);
                profilEmailLabel.setText(email);
                profilTelefonLabel.setText(telefon);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void randevuYukle() {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM randevular WHERE musteri_id = ? ORDER BY tarih ASC, saat ASC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sonRandevuHizmetLabel.setText(rs.getString("hizmet"));
                sonRandevuUzmanLabel.setText(rs.getString("uzman"));
                sonRandevuTarihLabel.setText(rs.getString("tarih"));
                sonRandevuSaatLabel.setText(rs.getString("saat"));

                yaklasanHizmetLabel.setText(rs.getString("hizmet"));
                yaklasanUzmanLabel.setText(rs.getString("uzman"));
                yaklasanTarihLabel.setText(rs.getString("tarih"));
                yaklasanSaatLabel.setText(rs.getString("saat"));
            } else {
                sonRandevuHizmetLabel.setText("-");
                sonRandevuUzmanLabel.setText("-");
                sonRandevuTarihLabel.setText("-");
                sonRandevuSaatLabel.setText("-");

                yaklasanHizmetLabel.setText("-");
                yaklasanUzmanLabel.setText("-");
                yaklasanTarihLabel.setText("-");
                yaklasanSaatLabel.setText("-");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void panelimGoster(ActionEvent event) { profilYukle(); randevuYukle(); }

    @FXML void profilBilgileriAc(ActionEvent event) {
        try { sayfaAc(event, "ProfilBilgileri.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void randevuAlAc(ActionEvent event) {
        try { sayfaAc(event, "KullaniciRandevuAl.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void randevularimiAc(ActionEvent event) {
        try { sayfaAc(event, "Randevularim.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void mesajlarGoster(ActionEvent event) {
        try { sayfaAc(event, "KullaniciMesajlar.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void cikisYap(ActionEvent event) {
        try {
            KullaniciOturum.oturumKapat();
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void initialize() {
        saatTarihBaslat();
        profilYukle();
        randevuYukle();
    }
}