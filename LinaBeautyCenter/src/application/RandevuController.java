package application;

import java.io.IOException;
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

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class RandevuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField adSoyadField;

    @FXML
    private ComboBox<String> hizmetCombo;

    @FXML
    private TextArea notArea;

    @FXML
    private ComboBox<String> saatCombo;

    @FXML
    private DatePicker tarihPicker;

    @FXML
    private TextField telefonField;

    @FXML
    private ComboBox<String> uzmanCombo;

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

    // RANDEVU OLUŞTUR

    @FXML
    void randevuOlustur(ActionEvent event) {

        // BOŞ ALAN KONTROLÜ

        if (adSoyadField.getText().isEmpty()
                || telefonField.getText().isEmpty()
                || hizmetCombo.getValue() == null
                || uzmanCombo.getValue() == null
                || tarihPicker.getValue() == null
                || saatCombo.getValue() == null) {

            popupGoster(
                    "Randevu Oluşturulamadı",
                    "Lütfen tüm alanları doldurunuz.",
                    false
            );

            return;
        }

        // TELEFON KONTROL

        if (telefonField.getText().length() < 10) {

            popupGoster(
                    "Geçersiz Telefon",
                    "Telefon numarası eksik veya hatalı.",
                    false
            );

            return;
        }

        try {

        	Connection conn =
        	        DatabaseConnection.baglan();

            String sql =
                    "INSERT INTO randevular "
                            + "(ad_soyad, telefon, hizmet, uzman, tarih, saat, notunuz) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1,
                    adSoyadField.getText());

            ps.setString(2,
                    telefonField.getText());

            ps.setString(3,
                    hizmetCombo.getValue());

            ps.setString(4,
                    uzmanCombo.getValue());

            ps.setString(5,
                    tarihPicker.getValue().toString());

            ps.setString(6,
                    saatCombo.getValue());

            ps.setString(7,
                    notArea.getText());

            ps.executeUpdate();

            ps.close();

            conn.close();

            popupGoster(
                    "Randevunuz Oluşturuldu",
                    "Randevu talebiniz başarıyla oluşturuldu.",
                    true
            );

            // TEMİZLE

            adSoyadField.clear();

            telefonField.clear();

            notArea.clear();

            hizmetCombo.setValue(null);

            uzmanCombo.setValue(null);

            saatCombo.setValue(null);

            tarihPicker.setValue(null);

        } catch (Exception e) {

            popupGoster(
                    "Sistem Hatası",
                    "Randevu oluşturulurken hata oluştu.",
                    false
            );

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

        hizmetCombo.getItems().addAll(
                "Cilt Bakımı",
                "Lazer Epilasyon",
                "Kaş & Kirpik",
                "Protez Tırnak",
                "Bölgesel İncelme",
                "Mezoterapi"
        );

        uzmanCombo.getItems().addAll(
                "Uzman Hayal",
                "Uzman Aleyna",
                "Uzman Elif",
                "Uzman Yağmur",
                "Uzman Zeynep",
                "Uzman Aylin"
        );

        saatCombo.getItems().addAll(
                "09:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00",
                "18:00",
                "19:00",
                "20:00"
        );
    }
}