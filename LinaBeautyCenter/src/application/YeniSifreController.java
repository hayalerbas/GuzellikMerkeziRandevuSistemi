package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;

public class YeniSifreController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Label bilgiLabel;
    @FXML private Pane bilgiPane;

    @FXML private Button cikisButton;

    @FXML private Label hataLabel;
    @FXML private Pane hataPane;

    @FXML private Button sifreGuncelleButton;

    @FXML private Label uyariIcon;

    @FXML private PasswordField yeniSifreField;
    @FXML private PasswordField yeniSifreTekrarField;

    private String email;
    private String kullaniciTuru;

    public void bilgileriAl(String email, String kullaniciTuru) {
        this.email = email;
        this.kullaniciTuru = kullaniciTuru;
    }

    @FXML
    void sifreGuncelle(ActionEvent event) {

        String yeniSifre = yeniSifreField.getText().trim();
        String tekrarSifre = yeniSifreTekrarField.getText().trim();

        hataPane.setVisible(false);
        bilgiPane.setVisible(false);

        if (yeniSifre.isEmpty() || tekrarSifre.isEmpty()) {
            hataPane.setVisible(true);
            hataLabel.setText("Lütfen tüm alanları doldurun.");
            return;
        }

        if (yeniSifre.length() < 6) {
            hataPane.setVisible(true);
            hataLabel.setText("Şifre en az 6 karakter olmalıdır.");
            return;
        }

        if (!yeniSifre.equals(tekrarSifre)) {
            hataPane.setVisible(true);
            hataLabel.setText("Yeni şifreler birbiriyle uyuşmuyor.");
            return;
        }

        String tabloAdi;

        if ("admin".equals(kullaniciTuru)) {
            tabloAdi = "adminler";
        } else {
            tabloAdi = "musteriler";
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql = "UPDATE " + tabloAdi + " SET sifre = ? WHERE email = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, yeniSifre);
            ps.setString(2, email);

            int sonuc = ps.executeUpdate();

            if (sonuc > 0) {

                bilgiPane.setVisible(true);
                bilgiLabel.setText("Şifreniz başarıyla güncellendi.");

                yeniSifreField.clear();
                yeniSifreTekrarField.clear();

            } else {

                hataPane.setVisible(true);
                hataLabel.setText("Bu mail adresine ait kullanıcı bulunamadı.");
            }

            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

            hataPane.setVisible(true);
            hataLabel.setText("Şifre güncellenirken hata oluştu.");
        }
    }

    @FXML
    void popupKapat(ActionEvent event) {

        Stage stage = (Stage) cikisButton
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    void initialize() {

        hataPane.setVisible(false);
        bilgiPane.setVisible(false);

        assert bilgiLabel != null;
        assert bilgiPane != null;
        assert cikisButton != null;
        assert hataLabel != null;
        assert hataPane != null;
        assert sifreGuncelleButton != null;
        assert uyariIcon != null;
        assert yeniSifreField != null;
        assert yeniSifreTekrarField != null;
    }
}