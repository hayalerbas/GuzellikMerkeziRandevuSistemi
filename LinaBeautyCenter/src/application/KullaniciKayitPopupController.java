package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class KullaniciKayitPopupController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField adSoyadField;
    @FXML private TextField emailField;
    @FXML private Button kayitButton;
    @FXML private TextField kullaniciAdiField;
    @FXML private PasswordField sifreField;
    @FXML private TextField telefonField;

    @FXML private Label baslikLabel1;
    @FXML private Label baslikLabel11;
    @FXML private Label baslikLabel12;
    @FXML private Label baslikLabel121;
    @FXML private Label baslikLabel1211;
    @FXML private Label baslikLabel12111;
    @FXML private Label baslikLabel121111;

    @FXML
    void kayitOl(ActionEvent event) {

        String adSoyad = adSoyadField.getText().trim();
        String telefon = telefonField.getText().trim();
        String email = emailField.getText().trim();
        String kullaniciAdi = kullaniciAdiField.getText().trim();
        String sifre = sifreField.getText().trim();

        if (adSoyad.isEmpty() || telefon.isEmpty() || email.isEmpty()
                || kullaniciAdi.isEmpty() || sifre.isEmpty()) {

            alertGoster(Alert.AlertType.WARNING, "Eksik Bilgi", "Lütfen tüm alanları doldurun.");
            return;
        }

        if (sifre.length() < 6) {
            alertGoster(Alert.AlertType.WARNING, "Şifre Hatası", "Şifre en az 6 karakter olmalıdır.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "INSERT INTO musteriler "
                    + "(ad_soyad, telefon, email, kullanici_adi, sifre, durum) "
                    + "VALUES (?, ?, ?, ?, ?, 'Aktif')";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, adSoyad);
            ps.setString(2, telefon);
            ps.setString(3, email);
            ps.setString(4, kullaniciAdi);
            ps.setString(5, sifre);

            ps.executeUpdate();

            ps.close();
            conn.close();

            alertGoster(Alert.AlertType.INFORMATION, "Başarılı", "Kayıt başarıyla oluşturuldu.");

            adSoyadField.clear();
            telefonField.clear();
            emailField.clear();
            kullaniciAdiField.clear();
            sifreField.clear();

            Stage stage = (Stage) kayitButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            alertGoster(Alert.AlertType.ERROR, "Kayıt Hatası",
                    "Kayıt oluşturulamadı. Kullanıcı adı veya e-posta daha önce alınmış olabilir.");
        }
    }

    @FXML
    void popupKapat(ActionEvent event) {

        Stage stage = (Stage) kayitButton.getScene().getWindow();
        stage.close();
    }

    private void alertGoster(Alert.AlertType type, String baslik, String mesaj) {

        Alert alert = new Alert(type);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    @FXML
    void initialize() {

        assert adSoyadField != null;
        assert emailField != null;
        assert kayitButton != null;
        assert kullaniciAdiField != null;
        assert sifreField != null;
        assert telefonField != null;
    }
}