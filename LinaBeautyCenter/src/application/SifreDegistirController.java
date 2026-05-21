package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class SifreDegistirController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField emailField;
    @FXML private Label hataLabel;
    @FXML private Label hataLabel1;
    @FXML private Button kodButton;

    private String dogrulamaKodu;

    @FXML
    void kodGonder(ActionEvent event) {

        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            hataLabel.setText("Lütfen e-posta adresinizi girin.");
            hataLabel1.setText("");
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM adminler WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                dogrulamaKodu = MailGonderici.kodUret();

                boolean gonderildiMi = MailGonderici.kodGonder(email, dogrulamaKodu);

                if (gonderildiMi) {

                    hataLabel.setText("");
                    hataLabel1.setText("6 haneli kod e-posta adresinize gönderildi.");

                    kodPopupAc(email, dogrulamaKodu);

                } else {
                    hataLabel.setText("Kod gönderilemedi. Mail ayarlarınızı kontrol edin.");
                    hataLabel1.setText("");
                }

            } else {
                hataLabel.setText("Bu e-posta ile kayıtlı admin bulunamadı.");
                hataLabel1.setText("");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            hataLabel.setText("Kod gönderilirken hata oluştu.");
            hataLabel1.setText("");
        }
    }

    private void kodPopupAc(String email, String kod) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/application/KodDogrulamaPopup.fxml"));

            Parent root = loader.load();

            KodDogrulamaPopupController controller = loader.getController();
            controller.bilgileriAl(email, kod, "admin");

            Stage popupStage = new Stage();
            popupStage.setTitle("Kod Doğrulama");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            hataLabel.setText("Kod doğrulama ekranı açılamadı.");
        }
    }

    @FXML
    void openAdminGiris(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/application/AnaSayfa.fxml"));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        hataLabel.setText("");
        hataLabel1.setText("");

        assert emailField != null;
        assert hataLabel != null;
        assert hataLabel1 != null;
        assert kodButton != null;
    }
}