package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class IslemlerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/application/" + fxmlAdi));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void popupAc(String baslik, String aciklama) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/application/DetayPopup.fxml"));

            Parent root = loader.load();

            DetayPopupController controller = loader.getController();
            controller.detaylariAyarla(baslik, aciklama);

            Stage stage = new Stage();
            stage.setTitle(baslik);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void detayCiltBakimi(ActionEvent event) {

        popupAc(
                "Cilt Bakımı",
                "• Profesyonel cilt temizliği\n"
                        + "• Siyah nokta bakımı\n"
                        + "• Nem terapisi\n"
                        + "• Cilt yenileme uygulamaları\n\n"
                        + "Süre: 60 Dakika"
        );
    }

    @FXML
    void detayLazerEpilasyon(ActionEvent event) {

        popupAc(
                "Lazer Epilasyon",
                "• Kalıcı lazer epilasyon\n"
                        + "• Hassas cilt uyumu\n"
                        + "• Profesyonel cihaz kullanımı\n\n"
                        + "Süre: 45 Dakika"
        );
    }

    @FXML
    void detayKasKirpik(ActionEvent event) {

        popupAc(
                "Kaş & Kirpik",
                "• Kaş tasarımı\n"
                        + "• Kirpik lifting\n"
                        + "• Laminasyon uygulamaları\n\n"
                        + "Süre: 40 Dakika"
        );
    }

    @FXML
    void detayProtezTirnak(ActionEvent event) {

        popupAc(
                "Protez Tırnak",
                "• Jel protez tırnak\n"
                        + "• Kalıcı bakım\n"
                        + "• Şık ve estetik görünüm\n\n"
                        + "Süre: 75 Dakika"
        );
    }

    @FXML
    void detayBolgeselIncelme(ActionEvent event) {

        popupAc(
                "Bölgesel İncelme",
                "• Sıkılaşma uygulamaları\n"
                        + "• Bölgesel yağ azaltma\n"
                        + "• Vücut şekillendirme\n\n"
                        + "Süre: 50 Dakika"
        );
    }

    @FXML
    void detayMezoterapi(ActionEvent event) {

        popupAc(
                "Mezoterapi",
                "• Cilt yenileme desteği\n"
                        + "• Canlandırıcı bakım\n"
                        + "• Profesyonel uygulama\n\n"
                        + "Süre: 35 Dakika"
        );
    }

    @FXML
    void openAnaSayfa(ActionEvent event) throws IOException {
        sayfaAc(event, "AnaSayfa.fxml");
    }

    @FXML
    void openCiltTesti(ActionEvent event) throws IOException {
        sayfaAc(event, "CiltTesti.fxml");
    }

    @FXML
    void openFiyat(ActionEvent event) throws IOException {
        sayfaAc(event, "Fiyat.fxml");
    }

    @FXML
    void openHakkimizda(ActionEvent event) throws IOException {
        sayfaAc(event, "Hakkimizda.fxml");
    }

    @FXML
    void openIletisim(ActionEvent event) throws IOException {
        sayfaAc(event, "Iletisim.fxml");
    }

    @FXML
    void openIslemler(ActionEvent event) throws IOException {
        sayfaAc(event, "Islemler.fxml");
    }

    @FXML
    void openKampanyalar(ActionEvent event) throws IOException {
        sayfaAc(event, "Kampanyalar.fxml");
    }

    @FXML
    void openRandevu(ActionEvent event) throws IOException {
        sayfaAc(event, "Randevu.fxml");
    }

    @FXML
    void openSSS(ActionEvent event) throws IOException {
        sayfaAc(event, "SSS.fxml");
    }

    @FXML
    void openAdminGiris(ActionEvent event) throws IOException {
        sayfaAc(event, "AdminGiris.fxml");
    }

    @FXML
    void initialize() {

    }
}