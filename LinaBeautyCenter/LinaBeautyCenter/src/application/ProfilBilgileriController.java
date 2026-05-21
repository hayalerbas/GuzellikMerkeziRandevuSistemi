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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.util.Duration;

public class ProfilBilgileriController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField adField;
    @FXML private TextField soyadField;
    @FXML private TextField emailField;
    @FXML private TextField telefonField;

    @FXML private PasswordField mevcutSifreField;
    @FXML private PasswordField yeniSifreField;
    @FXML private PasswordField yeniSifreTekrarField;

    @FXML private ComboBox<String> cinsiyetCombo;
    @FXML private DatePicker dogumTarihiPicker;

    @FXML private Button anaSayfaButton;
    @FXML private Button cikisButton;
    @FXML private Button kaydetButton;
    @FXML private Button kaydetButton1;
    @FXML private Button mesajlarButton;
    @FXML private Button profilMenuButton;
    @FXML private Button randevuAlMenuButton;
    @FXML private Button randevularimMenuButton;

    @FXML private Pane kisiselBilgilerPane;

    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

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

    private void profilYukle() {

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM musteriler WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String adSoyad = rs.getString("ad_soyad");

                if (adSoyad != null && adSoyad.contains(" ")) {

                    String[] parca = adSoyad.split(" ", 2);

                    adField.setText(parca[0]);
                    soyadField.setText(parca[1]);

                } else {

                    adField.setText(adSoyad);
                    soyadField.setText("");
                }

                emailField.setText(rs.getString("email"));
                telefonField.setText(rs.getString("telefon"));

                try {
                    cinsiyetCombo.setValue(rs.getString("cinsiyet"));
                } catch (Exception e) {
                    cinsiyetCombo.setValue(null);
                }

                try {
                    String dogumTarihi = rs.getString("dogum_tarihi");

                    if (dogumTarihi != null && !dogumTarihi.isEmpty()) {
                        dogumTarihiPicker.setValue(LocalDate.parse(dogumTarihi));
                    }

                } catch (Exception e) {
                    dogumTarihiPicker.setValue(null);
                }
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    void profilKaydet(ActionEvent event) {

        if (adField.getText().trim().isEmpty()
                || soyadField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()
                || telefonField.getText().trim().isEmpty()) {

            alertGoster("Eksik Bilgi", "Lütfen ad, soyad, e-posta ve telefon alanlarını doldurunuz.");
            return;
        }

        String adSoyad = adField.getText().trim() + " " + soyadField.getText().trim();

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "UPDATE musteriler "
                            + "SET ad_soyad = ?, email = ?, telefon = ? "
                            + "WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, adSoyad);
            ps.setString(2, emailField.getText().trim());
            ps.setString(3, telefonField.getText().trim());
            ps.setInt(4, KullaniciOturum.musteriId);

            ps.executeUpdate();
            ps.close();
            conn.close();

            alertGoster("Başarılı", "Profil bilgileriniz güncellendi.");

        } catch (Exception e) {

            e.printStackTrace();
            alertGoster("Hata", "Profil bilgileri güncellenemedi.");
        }
    }

    @FXML
    void sifreGuncelle(ActionEvent event) {

        if (mevcutSifreField.getText().isEmpty()
                || yeniSifreField.getText().isEmpty()
                || yeniSifreTekrarField.getText().isEmpty()) {

            alertGoster("Eksik Bilgi", "Lütfen tüm şifre alanlarını doldurunuz.");
            return;
        }

        if (!yeniSifreField.getText().equals(yeniSifreTekrarField.getText())) {

            alertGoster("Hata", "Yeni şifreler uyuşmuyor.");
            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String kontrolSql =
                    "SELECT * FROM musteriler "
                            + "WHERE id = ? AND sifre = ?";

            PreparedStatement kontrolPs = conn.prepareStatement(kontrolSql);
            kontrolPs.setInt(1, KullaniciOturum.musteriId);
            kontrolPs.setString(2, mevcutSifreField.getText());

            ResultSet rs = kontrolPs.executeQuery();

            if (rs.next()) {

                String sifreSql =
                        "UPDATE musteriler SET sifre = ? WHERE id = ?";

                PreparedStatement sifrePs = conn.prepareStatement(sifreSql);
                sifrePs.setString(1, yeniSifreField.getText());
                sifrePs.setInt(2, KullaniciOturum.musteriId);

                sifrePs.executeUpdate();
                sifrePs.close();

                alertGoster("Başarılı", "Şifreniz başarıyla güncellendi.");

                mevcutSifreField.clear();
                yeniSifreField.clear();
                yeniSifreTekrarField.clear();

            } else {

                alertGoster("Hata", "Mevcut şifre yanlış.");
            }

            rs.close();
            kontrolPs.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
            alertGoster("Hata", "Şifre güncellenemedi.");
        }
    }
    @FXML
    void hesapSil(ActionEvent event) {

        try {

            Connection conn = DatabaseConnection.baglan();

            String kontrolSql =
                    "SELECT COUNT(*) FROM randevular "
                  + "WHERE musteri_id = ? "
                  + "AND durum <> 'İptal'";

            PreparedStatement kontrolPs =
                    conn.prepareStatement(kontrolSql);

            kontrolPs.setInt(1, KullaniciOturum.musteriId);

            ResultSet rs = kontrolPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {

                alertGoster(
                        "Hesap Silinemez",
                        "Aktif randevunuz olduğu için hesabınızı silemezsiniz."
                );

                rs.close();
                kontrolPs.close();
                conn.close();

                return;
            }

            rs.close();
            kontrolPs.close();

            String silSql =
                    "UPDATE musteriler "
                  + "SET durum = 'Pasif' "
                  + "WHERE id = ?";

            PreparedStatement silPs =
                    conn.prepareStatement(silSql);

            silPs.setInt(1, KullaniciOturum.musteriId);

            silPs.executeUpdate();

            silPs.close();
            conn.close();

            KullaniciOturum.oturumKapat();

            alertGoster(
                    "Hesap Pasif Yapıldı",
                    "Hesabınız başarıyla pasif duruma getirildi."
            );

            sayfaAc(event, "AnaSayfa.fxml");

        } catch (Exception e) {

            e.printStackTrace();

            alertGoster(
                    "Hata",
                    "İşlem sırasında hata oluştu."
            );
        }
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

        profilYukle();
    }

    @FXML
    void randevuAlAc(ActionEvent event) {

        try {
            sayfaAc(event, "KullaniciRandevuAl.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        cinsiyetCombo.getItems().addAll(
                "Kadın",
                "Erkek",
                "Belirtmek istemiyorum"
        );

        saatTarihBaslat();
        profilYukle();
    }
}