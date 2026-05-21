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

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class KullaniciGirisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label hataLabel;

    @FXML
    private TextField kullaniciAdiField;

    @FXML
    private PasswordField sifreField;

    @FXML
    void KullaniciGirisYap(ActionEvent event) {

        String kullaniciAdi =
                kullaniciAdiField.getText();

        String sifre =
                sifreField.getText();

        if (kullaniciAdi.isEmpty()
                || sifre.isEmpty()) {

            hataLabel.setText(
                    "Lütfen tüm alanları doldurun.");

            return;
        }

        try {

            Connection conn =
                    DatabaseConnection.baglan();

            String sql =
                    "SELECT * FROM musteriler "
                    + "WHERE kullanici_adi = ? "
                    + "AND sifre = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, kullaniciAdi);

            ps.setString(2, sifre);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                KullaniciOturum.oturumAc(
                        rs.getInt("id"),
                        rs.getString("ad_soyad"),
                        rs.getString("email"),
                        rs.getString("telefon"));

                Parent root =
                        FXMLLoader.load(
                                getClass().getResource(
                                        "/application/KullaniciPanel.fxml"));

                Stage stage =
                        (Stage) ((Node) event.getSource())
                                .getScene()
                                .getWindow();

                stage.setScene(new Scene(root));

                stage.show();

            } else {

                hataLabel.setText(
                        "Kullanıcı adı veya şifre hatalı.");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

            hataLabel.setText(
                    "Giriş yapılırken hata oluştu.");
        }
    }

    @FXML
    void kayitOlAc(ActionEvent event) {

        try {

            Parent root =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/application/KullaniciKayitpopup.fxml"));

            Stage popup =
                    new Stage();

            popup.setScene(new Scene(root));

            popup.setResizable(false);

            popup.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    void openAnaSayfa(ActionEvent event) {

        try {

            Parent root =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/application/AnaSayfa.fxml"));

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @FXML
    void sifreDegistir(ActionEvent event) {

        try {

            Parent root =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/application/SifreDegistir.fxml"));

            Stage stage =
                    (Stage) ((Node) event.getSource())
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

        assert hataLabel != null;
        assert kullaniciAdiField != null;
        assert sifreField != null;
    }
}