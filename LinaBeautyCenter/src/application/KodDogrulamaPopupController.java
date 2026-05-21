package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;

public class KodDogrulamaPopupController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Button cikisButton;
    @FXML private Label hataLabel;
    @FXML private Pane hataPane;
    @FXML private TextField kodField;
    @FXML private Button koduDogrulaButton;
    @FXML private Label sureLabel;
    @FXML private Pane surePane;
    @FXML private Label uyariIcon;

    private String email;
    private String dogruKod;
    private String kullaniciTuru;

    public void bilgileriAl(String email, String dogruKod, String kullaniciTuru) {
        this.email = email;
        this.dogruKod = dogruKod;
        this.kullaniciTuru = kullaniciTuru;
    }

    @FXML
    void koduDogrula(ActionEvent event) {

        String girilenKod = kodField.getText().trim();

        if (girilenKod.isEmpty()) {
            hataPane.setVisible(true);
            hataLabel.setText("Lütfen doğrulama kodunu giriniz.");
            return;
        }

        if (!girilenKod.equals(dogruKod)) {
            hataPane.setVisible(true);
            hataLabel.setText("Kod hatalı. Lütfen mail adresinizi kontrol ediniz veya tekrar kod talep ediniz.");
            return;
        }

        yeniSifreEkraniAc();
    }

    private void yeniSifreEkraniAc() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/application/YeniSifre.fxml"));

            Parent root = loader.load();

            YeniSifreController controller = loader.getController();
            controller.bilgileriAl(email, kullaniciTuru);

            Stage mevcutStage = (Stage) koduDogrulaButton
                    .getScene()
                    .getWindow();

            mevcutStage.close();

            Stage yeniStage = new Stage();
            yeniStage.setTitle("Yeni Şifre Oluştur");
            yeniStage.setScene(new Scene(root));
            yeniStage.setResizable(false);
            yeniStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            hataPane.setVisible(true);
            hataLabel.setText("Yeni şifre ekranı açılamadı.");
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

        sureLabel.setText("Kodun geçerlilik süresi: 2 dakika");

        assert cikisButton != null;
        assert hataLabel != null;
        assert hataPane != null;
        assert kodField != null;
        assert koduDogrulaButton != null;
        assert sureLabel != null;
        assert surePane != null;
        assert uyariIcon != null;
    }
}