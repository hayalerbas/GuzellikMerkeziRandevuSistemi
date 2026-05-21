package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KullaniciRandevuAlController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Button anaSayfaButton;
    @FXML private Button bolgeselIncelmeButton;
    @FXML private Button cikisButton;
    @FXML private Button ciltBakimiButton;
    @FXML private Button kasKirpikButton;
    @FXML private Button lazerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button mezoterapiButton;
    @FXML private Button profilMenuButton;
    @FXML private Button proteztirnakButton;
    @FXML private Button randevuAlMenuButton;
    @FXML private Button randevuOnaylaButton;
    @FXML private Button randevularimMenuButton;
    @FXML private Button saat0900Button;
    @FXML private Button saat1000Button;
    @FXML private Button saat1100Button;
    @FXML private Button saat1200Button;
    @FXML private Button saat1300Button;
    @FXML private Button saat1400Button;
    @FXML private Button saat1500Button;
    @FXML private Button saat1600Button;
    @FXML private Button saat1700Button;
    @FXML private Button saat1800Button;
    @FXML private Button saat1900Button;
    @FXML private Button saat2000Button;
    @FXML private Button temizleButton;

    @FXML private Label ozetHizmetLabel;
    @FXML private Label ozetSaatLabel;
    @FXML private Label ozetSureLabel;
    @FXML private Label ozetTarihLabel;
    @FXML private Label ozetUcretLabel;
    @FXML private Label ozetUzmanLabel;

    @FXML private Label saatLabel;
    @FXML private Label seciliSaatLabel;
    @FXML private Label seciliTarihLabel;

    @FXML private DatePicker tarihPicker;
    @FXML private ComboBox<String> uzmanCombo;

    @FXML private Label yaklasanHizmetLabel;
    @FXML private Label yaklasanHizmetLabel1;
    @FXML private Label yaklasanHizmetLabel11;
    @FXML private Label yaklasanHizmetLabel111;
    @FXML private Label yaklasanHizmetLabel1111;
    @FXML private Label yaklasanHizmetLabel11111;
    @FXML private Label yaklasanHizmetLabel111111;
    @FXML private Label yaklasanHizmetLabel112;
    @FXML private Label yaklasanHizmetLabel12;
    @FXML private Label yaklasanHizmetLabel2;
    @FXML private Label yaklasanHizmetLabel21;

    private String seciliHizmet = "";
    private String seciliSaat = "";
    private String seciliSure = "";
    private String seciliUcret = "";

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/application/" + fxmlAdi)
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saatBaslat() {

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {

                                String tarih = LocalDate.now().format(
                                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                );

                                String saat = LocalTime.now().format(
                                        DateTimeFormatter.ofPattern("HH:mm:ss")
                                );

                                saatLabel.setText(tarih + "    " + saat);
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

    private void ozetGuncelle() {

        ozetHizmetLabel.setText(seciliHizmet.isEmpty() ? "-" : seciliHizmet);
        ozetSaatLabel.setText(seciliSaat.isEmpty() ? "-" : seciliSaat);
        ozetSureLabel.setText(seciliSure.isEmpty() ? "-" : seciliSure);
        ozetUcretLabel.setText(seciliUcret.isEmpty() ? "-" : seciliUcret);

        if (uzmanCombo.getValue() == null) {
            ozetUzmanLabel.setText("-");
        } else {
            ozetUzmanLabel.setText(uzmanCombo.getValue());
        }

        if (tarihPicker.getValue() == null) {
            seciliTarihLabel.setText("-");
            ozetTarihLabel.setText("-");
        } else {
            String tarih = tarihPicker.getValue().toString();
            seciliTarihLabel.setText(tarih);
            ozetTarihLabel.setText(tarih);
        }
    }

    private void doluSaatleriKontrolEt() {

        saatButonlariniSifirla();

        if (tarihPicker.getValue() == null || uzmanCombo.getValue() == null) {
            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT saat FROM randevular "
                            + "WHERE tarih = ? "
                            + "AND uzman = ? "
                            + "AND durum <> 'İptal'";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tarihPicker.getValue().toString());
            ps.setString(2, uzmanCombo.getValue());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String saat = rs.getString("saat");

                Button buton = saateGoreButonGetir(saat);

                if (buton != null) {

                    buton.setDisable(true);

                    buton.setStyle(
                            "-fx-background-color: #777777;"
                                    + "-fx-text-fill: white;"
                                    + "-fx-strikethrough: true;"
                                    + "-fx-opacity: 0.55;"
                                    + "-fx-cursor: default;"
                    );
                }
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saatButonlariniSifirla() {

        Button[] butonlar = {
                saat0900Button,
                saat1000Button,
                saat1100Button,
                saat1200Button,
                saat1300Button,
                saat1400Button,
                saat1500Button,
                saat1600Button,
                saat1700Button,
                saat1800Button,
                saat1900Button,
                saat2000Button
        };

        for (Button btn : butonlar) {

            btn.setDisable(false);
            btn.setStyle("");
        }
    }

    private Button saateGoreButonGetir(String saat) {

        if (saat == null) {
            return null;
        }

        if (saat.equals("09:00")) return saat0900Button;
        if (saat.equals("10:00")) return saat1000Button;
        if (saat.equals("11:00")) return saat1100Button;
        if (saat.equals("12:00")) return saat1200Button;
        if (saat.equals("13:00")) return saat1300Button;
        if (saat.equals("14:00")) return saat1400Button;
        if (saat.equals("15:00")) return saat1500Button;
        if (saat.equals("16:00")) return saat1600Button;
        if (saat.equals("17:00")) return saat1700Button;
        if (saat.equals("18:00")) return saat1800Button;
        if (saat.equals("19:00")) return saat1900Button;
        if (saat.equals("20:00")) return saat2000Button;

        return null;
    }

    private boolean randevuDoluMu() {

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT COUNT(*) FROM randevular "
                            + "WHERE tarih = ? "
                            + "AND saat = ? "
                            + "AND uzman = ? "
                            + "AND durum <> 'İptal'";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tarihPicker.getValue().toString());
            ps.setString(2, seciliSaat);
            ps.setString(3, uzmanCombo.getValue());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int sayi = rs.getInt(1);

                rs.close();
                ps.close();
                conn.close();

                return sayi > 0;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @FXML
    void ciltBakimiSec(ActionEvent event) {

        seciliHizmet = "Cilt Bakımı";
        seciliSure = "60 Dakika";
        seciliUcret = "4000 TL";

        ozetGuncelle();
    }

    @FXML
    void lazerSec(ActionEvent event) {

        seciliHizmet = "Lazer Epilasyon";
        seciliSure = "45 Dakika";
        seciliUcret = "15000 TL";

        ozetGuncelle();
    }

    @FXML
    void kasKirpikSec(ActionEvent event) {

        seciliHizmet = "Kaş & Kirpik";
        seciliSure = "40 Dakika";
        seciliUcret = "5000 TL";

        ozetGuncelle();
    }

    @FXML
    void protezTirnakSec(ActionEvent event) {

        seciliHizmet = "Protez Tırnak";
        seciliSure = "75 Dakika";
        seciliUcret = "3000 TL";

        ozetGuncelle();
    }

    @FXML
    void bolgeselIncelmeSec(ActionEvent event) {

        seciliHizmet = "Bölgesel İncelme";
        seciliSure = "50 Dakika";
        seciliUcret = "16000 TL";

        ozetGuncelle();
    }

    @FXML
    void mezoterapiSec(ActionEvent event) {

        seciliHizmet = "Mezoterapi";
        seciliSure = "35 Dakika";
        seciliUcret = "12000 TL";

        ozetGuncelle();
    }

    private void saatSec(String saat) {

        seciliSaat = saat;
        seciliSaatLabel.setText(saat);

        ozetGuncelle();
    }

    @FXML void saat0900Sec(ActionEvent event) { saatSec("09:00"); }
    @FXML void saat1000Sec(ActionEvent event) { saatSec("10:00"); }
    @FXML void saat1100Sec(ActionEvent event) { saatSec("11:00"); }
    @FXML void saat1200Sec(ActionEvent event) { saatSec("12:00"); }
    @FXML void saat1300Sec(ActionEvent event) { saatSec("13:00"); }
    @FXML void saat1400Sec(ActionEvent event) { saatSec("14:00"); }
    @FXML void saat1500Sec(ActionEvent event) { saatSec("15:00"); }
    @FXML void saat1600Sec(ActionEvent event) { saatSec("16:00"); }
    @FXML void saat1700Sec(ActionEvent event) { saatSec("17:00"); }
    @FXML void saat1800Sec(ActionEvent event) { saatSec("18:00"); }
    @FXML void saat1900Sec(ActionEvent event) { saatSec("19:00"); }
    @FXML void saat2000Sec(ActionEvent event) { saatSec("20:00"); }

    @FXML
    void randevuOnayla(ActionEvent event) {

        ozetGuncelle();

        if (seciliHizmet.isEmpty()
                || seciliSaat.isEmpty()
                || tarihPicker.getValue() == null
                || uzmanCombo.getValue() == null) {

            alertGoster("Eksik Bilgi", "Lütfen hizmet, uzman, tarih ve saat seçiniz.");
            return;
        }

        if (randevuDoluMu()) {

            alertGoster(
                    "Dolu Saat",
                    "Bu uzman için seçilen tarih ve saat doludur. Lütfen başka bir saat seçiniz."
            );

            doluSaatleriKontrolEt();
            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO randevular "
                  + "(musteri_id, ad_soyad, telefon, hizmet, uzman, tarih, saat, notunuz, durum) "
                  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, KullaniciOturum.musteriId);
            ps.setString(2, KullaniciOturum.adSoyad);
            ps.setString(3, KullaniciOturum.telefon);
            ps.setString(4, seciliHizmet);
            ps.setString(5, uzmanCombo.getValue());
            ps.setString(6, tarihPicker.getValue().toString());
            ps.setString(7, seciliSaat);
            ps.setString(8, "");
            ps.setString(9, "Bekliyor");

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster("Başarılı", "Randevunuz başarıyla oluşturuldu.");

            secimleriSifirla();
            doluSaatleriKontrolEt();

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster("Hata", "Randevu oluşturulamadı: " + e.getMessage());
        }
    }

    @FXML
    void secimleriTemizle(ActionEvent event) {

        secimleriSifirla();
        doluSaatleriKontrolEt();
    }

    private void secimleriSifirla() {

        seciliHizmet = "";
        seciliSaat = "";
        seciliSure = "";
        seciliUcret = "";

        seciliSaatLabel.setText("-");
        seciliTarihLabel.setText("-");

        ozetHizmetLabel.setText("-");
        ozetSaatLabel.setText("-");
        ozetSureLabel.setText("-");
        ozetTarihLabel.setText("-");
        ozetUcretLabel.setText("-");
        ozetUzmanLabel.setText("-");

        tarihPicker.setValue(null);
        uzmanCombo.setValue(null);

        saatButonlariniSifirla();
    }

    @FXML
    void panelimGoster(ActionEvent event) {

        try {
            sayfaAc(event, "KullaniciPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profilBilgileriAc(ActionEvent event) {

        try {
            sayfaAc(event, "ProfilBilgileri.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevuAlAc(ActionEvent event) {

        ozetGuncelle();
    }

    @FXML
    void randevularimiAc(ActionEvent event) {

        try {
            sayfaAc(event, "Randevularim.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajlarGoster(ActionEvent event) {

        try {
            sayfaAc(event, "KullaniciMesajlar.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cikisYap(ActionEvent event) {

        try {
            KullaniciOturum.oturumKapat();
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        uzmanCombo.getItems().addAll(
                "Uzman Hayal",
                "Uzman Aleyna",
                "Uzman Elif",
                "Uzman Zeynep"
        );

        tarihPicker.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ozetGuncelle();
                doluSaatleriKontrolEt();
            }
        });

        uzmanCombo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ozetGuncelle();
                doluSaatleriKontrolEt();
            }
        });

        secimleriSifirla();
        saatBaslat();
    }
}