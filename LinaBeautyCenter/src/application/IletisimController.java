package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class IletisimController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField adSoyadField;

    @FXML
    private TextField emailField;

    @FXML
    private Button gonderButton;

    @FXML
    private Button instagramButton;

    @FXML
    private TextArea mesajArea;

    @FXML
    private TextField telefonField;

    // SAYFA GEÇİŞ

    private void sayfaAc(ActionEvent event,
                         String fxmlAdi) throws IOException {

        Parent root =
                FXMLLoader.load(
                        getClass().getResource(
                                "/application/" + fxmlAdi));

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));

        stage.show();
    }

    // POPUP

    private void popupGoster(String baslik,
                             String mesaj,
                             boolean basariliMi) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/application/BasariliPopup.fxml"));

            Parent root = loader.load();

            BasariliPopupController controller =
                    loader.getController();

            controller.popupBilgi(
                    baslik,
                    mesaj,
                    basariliMi
            );

            Stage stage = new Stage();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // MESAJ GÖNDER

    @FXML
    void gonderMesaj(ActionEvent event) {

        // BOŞ ALAN KONTROLÜ

        if (adSoyadField.getText().isEmpty()
                || telefonField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || mesajArea.getText().isEmpty()) {

            popupGoster(
                    "Mesaj Gönderilemedi",
                    "Lütfen tüm alanları doldurunuz.",
                    false
            );

            return;
        }

        try {

            Connection conn =
                    DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO iletisim_mesajlari "
                            + "(ad_soyad, telefon, email, mesaj) "
                            + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,
                    adSoyadField.getText());

            ps.setString(2,
                    telefonField.getText());

            ps.setString(3,
                    emailField.getText());

            ps.setString(4,
                    mesajArea.getText());

            ps.executeUpdate();

            ps.close();

            conn.close();

            popupGoster(
                    "Mesajınız İletilmiştir",
                    "En kısa sürede size bilgi verilecektir.",
                    true
            );

            // TEMİZLE

            adSoyadField.clear();

            telefonField.clear();

            emailField.clear();

            mesajArea.clear();

        } catch (Exception e) {

            popupGoster(
                    "Sistem Hatası",
                    "Mesaj gönderilirken hata oluştu.",
                    false
            );

            e.printStackTrace();
        }
    }

    // INSTAGRAM

    @FXML
    void openInstagram(ActionEvent event) {

        try {

            Desktop.getDesktop().browse(
                    new URI(
                            "https://instagram.com/lina_beautycenter_"));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // SAYFALAR

    @FXML
    void openAnaSayfa(ActionEvent event)
            throws IOException {

        sayfaAc(event, "AnaSayfa.fxml");
    }

    @FXML
    void openCiltTesti(ActionEvent event)
            throws IOException {

        sayfaAc(event, "CiltTesti.fxml");
    }

    @FXML
    void openFiyat(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Fiyat.fxml");
    }

    @FXML
    void openHakkimizda(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Hakkimizda.fxml");
    }

    @FXML
    void openIletisim(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Iletisim.fxml");
    }

    @FXML
    void openIslemler(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Islemler.fxml");
    }

    @FXML
    void openKampanyalar(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Kampanyalar.fxml");
    }

    @FXML
    void openRandevu(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Randevu.fxml");
    }

    @FXML
    void openSSS(ActionEvent event)
            throws IOException {

        sayfaAc(event, "SSS.fxml");
    }

    @FXML
    void openAdminGiris(ActionEvent event)
            throws IOException {

        sayfaAc(event, "AdminGiris.fxml");
    }

    @FXML
    void initialize() {

        assert adSoyadField != null;
        assert emailField != null;
        assert gonderButton != null;
        assert instagramButton != null;
        assert mesajArea != null;
        assert telefonField != null;
    }
}