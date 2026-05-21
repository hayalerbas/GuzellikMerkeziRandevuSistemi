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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class AdminPanelController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Button anaSayfaButton;
    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button randevularButton;
    @FXML private Button randevularButton1;

    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    @FXML private Label toplamCalisanLabel;
    @FXML private Label toplamMusteriLabel;
    @FXML private Label toplamRandevuLabel;

    @FXML private Arc bekleyenArc;
    @FXML private Label bekleyenLabel;
    @FXML private Label bekleyenYuzdeLabel;

    @FXML private Arc tamamlananArc;
    @FXML private Label tamamlananLabel;
    @FXML private Label tamamlananYuzdeLabel;

    @FXML private Arc iptalArc;
    @FXML private Label iptalLabel;
    @FXML private Label iptalYuzdeLabel;
    @FXML private VBox yaklasanRandevularVBox;
    @FXML private VBox sonMesajlarVBox;

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

    private int sayiGetir(String sql) {

        int sonuc = 0;

        try {

            Connection conn = DatabaseConnection.baglan();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sonuc = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sonuc;
    }

    private int yuzdeHesapla(int sayi, int toplam) {

        if (toplam == 0) {
            return 0;
        }

        return (int) Math.round((sayi * 100.0) / toplam);
    }

    private void verileriYukle() {

        int toplamRandevu = sayiGetir("SELECT COUNT(*) FROM randevular");
        int toplamMusteri = sayiGetir("SELECT COUNT(*) FROM musteriler");
        int toplamCalisan = sayiGetir("SELECT COUNT(*) FROM calisanlar");

        int bekleyen = sayiGetir(
                "SELECT COUNT(*) FROM randevular WHERE durum = 'Bekliyor' OR durum = 'Beklemede'"
        );

        int tamamlanan = sayiGetir(
                "SELECT COUNT(*) FROM randevular WHERE durum = 'Onaylandı' OR durum = 'Tamamlandı'"
        );

        int iptal = sayiGetir(
                "SELECT COUNT(*) FROM randevular WHERE durum = 'İptal' OR durum = 'İptal Edildi'"
        );

        toplamRandevuLabel.setText(String.valueOf(toplamRandevu));
        toplamMusteriLabel.setText(String.valueOf(toplamMusteri));
        toplamCalisanLabel.setText(String.valueOf(toplamCalisan));

        bekleyenLabel.setText(String.valueOf(bekleyen));
        tamamlananLabel.setText(String.valueOf(tamamlanan));
        iptalLabel.setText(String.valueOf(iptal));

        int bekleyenYuzde = yuzdeHesapla(bekleyen, toplamRandevu);
        int tamamlananYuzde = yuzdeHesapla(tamamlanan, toplamRandevu);
        int iptalYuzde = yuzdeHesapla(iptal, toplamRandevu);

        bekleyenYuzdeLabel.setText("%" + bekleyenYuzde);
        tamamlananYuzdeLabel.setText("%" + tamamlananYuzde);
        iptalYuzdeLabel.setText("%" + iptalYuzde);

        bekleyenArc.setLength(-(bekleyenYuzde * 3.6));
        tamamlananArc.setLength(-(tamamlananYuzde * 3.6));
        iptalArc.setLength(-(iptalYuzde * 3.6));
    }

    private void yaklasanRandevulariYukle() {

        yaklasanRandevularVBox.getChildren().clear();

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT ad_soyad, hizmet, tarih, saat "
                  + "FROM randevular "
                  + "WHERE durum <> 'İptal' "
                  + "ORDER BY tarih ASC, saat ASC "
                  + "LIMIT 3";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

           
            while (rs.next()) {

                Label label = new Label(
                        rs.getString("ad_soyad")
                        + "  |  "
                        + rs.getString("hizmet")
                        + "  |  "
                        + rs.getString("tarih")
                        + "  "
                        + rs.getString("saat")
                );

                label.setStyle(
                        "-fx-font-size: 20px;"
                      + "-fx-text-fill: #8a5f4d;"
                      + "-fx-font-family: Georgia;"
                );

                yaklasanRandevularVBox.getChildren().add(label);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sonMesajlariYukle() {

        sonMesajlarVBox.getChildren().clear();

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql =
                    "SELECT mu.ad_soyad, m.mesaj "
                  + "FROM mesajlar m "
                  + "JOIN musteriler mu ON m.musteri_id = mu.id "
                  + "ORDER BY m.tarih DESC "
                  + "LIMIT 2";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Label label = new Label(
                        rs.getString("ad_soyad")
                        + " → "
                        + rs.getString("mesaj")
                );

                label.setWrapText(true);

                label.setStyle(
                        "-fx-font-size: 20px;"
                      + "-fx-text-fill: #8a5f4d;"
                      + "-fx-font-family: Georgia;"
                );

                sonMesajlarVBox.getChildren().add(label);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saatTarihBaslat() {

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {

                                LocalDate tarih = LocalDate.now();
                                LocalTime saat = LocalTime.now();

                                takvimlabel.setText(
                                        tarih.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                );

                                saatlabel.setText(
                                        saat.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                );
                            }
                        }
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void dashboardGoster(ActionEvent event) {
        try {
            sayfaAc(event, "AdminPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevulariListele(ActionEvent event) {
        try {
            sayfaAc(event, "RandevuPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void musterileriGoster(ActionEvent event) {
        try {
            sayfaAc(event, "MusteriPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void hizmetleriGoster(ActionEvent event) {
        try {
            sayfaAc(event, "HizmetlerPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void calisanlariGoster(ActionEvent event) {
        try {
            sayfaAc(event, "CalisanlarPanel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajlarGoster(ActionEvent event) {
        try {
            sayfaAc(event, "Adminmesajlar.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cikisYap(ActionEvent event) {
        try {
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
    	yaklasanRandevulariYukle();
    	sonMesajlariYukle();
        saatTarihBaslat();
        verileriYukle();
    }
}