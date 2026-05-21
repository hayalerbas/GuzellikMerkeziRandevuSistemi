package application;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class KullaniciMesajlarController {

    @FXML private HBox adminMesajHBox, musteriMesajHBox;
    @FXML private Label adminMesajLabel, emailLabel, mesajSaatLabel, musteriMesajLabel,
            saatlabel, sohbetAdLabel, sohbetBaslikLabel, sonMesajLabel, takvimlabel;
    @FXML private Button anaSayfaButton, cikisButton, gonderButton, mesajlarButton,
            profilMenuButton, randevuAlMenuButton, randevularimMenuButton,
            silButton, yenileButton;
    @FXML private Pane linaSohbetPane, mesajAnaPane, sohbetListePane, sohbetPane;
    @FXML private ScrollPane mesajScrollPane;
    @FXML private TextArea mesajYazArea;
    @FXML private VBox mesajlarVBox;
    @FXML private Circle okunmadiCircle;

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/" + fxmlAdi));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void saatTarihBaslat() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                takvimlabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                saatlabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void mesajlariYukle() {
        mesajlarVBox.getChildren().clear();

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM mesajlar WHERE musteri_id = ? ORDER BY tarih ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mesaj = rs.getString("mesaj");
                String gonderenTipi = rs.getString("gonderen_tipi");

                mesajBalonuEkle(mesaj, "musteri".equalsIgnoreCase(gonderenTipi));
            }

            rs.close();
            ps.close();
            conn.close();

            mesajScrollPane.setVvalue(1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mesajBalonuEkle(String mesaj, boolean musteriMi) {
        HBox satir = new HBox();
        satir.setPrefWidth(540);
        satir.setPadding(new Insets(5, 20, 5, 20));

        Label mesajLabel = new Label(mesaj);
        mesajLabel.setWrapText(true);
        mesajLabel.setMaxWidth(300);
        mesajLabel.setStyle(
                "-fx-padding: 12;"
                        + "-fx-background-radius: 18;"
                        + "-fx-font-size: 14px;"
                        + "-fx-text-fill: #2d1b12;"
        );

        if (musteriMi) {
            satir.setAlignment(Pos.CENTER_RIGHT);
            mesajLabel.setStyle(mesajLabel.getStyle() + "-fx-background-color: #eef6ea;");
        } else {
            satir.setAlignment(Pos.CENTER_LEFT);
            mesajLabel.setStyle(mesajLabel.getStyle() + "-fx-background-color: #f8efe5;");
        }

        satir.getChildren().add(mesajLabel);
        mesajlarVBox.getChildren().add(satir);
    }

    private void sonMesajiYukle() {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT mesaj, TIME_FORMAT(tarih, '%H:%i') AS saat "
                    + "FROM mesajlar WHERE musteri_id = ? ORDER BY tarih DESC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String mesaj = rs.getString("mesaj");

                if (mesaj.length() > 30) {
                    mesaj = mesaj.substring(0, 30) + "...";
                }

                sonMesajLabel.setText(mesaj);
                mesajSaatLabel.setText(rs.getString("saat"));
            } else {
                sonMesajLabel.setText("Henüz mesaj yok.");
                mesajSaatLabel.setText("-");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mesajGonder(ActionEvent event) {
        String mesaj = mesajYazArea.getText().trim();

        if (mesaj.isEmpty()) {
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "INSERT INTO mesajlar "
                    + "(musteri_id, gonderen_tipi, mesaj, okundu, arsiv) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, KullaniciOturum.musteriId);
            ps.setString(2, "musteri");
            ps.setString(3, mesaj);
            ps.setBoolean(4, false);
            ps.setBoolean(5, false);

            ps.executeUpdate();

            ps.close();
            conn.close();

            mesajYazArea.clear();
            mesajlariYukle();
            sonMesajiYukle();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void mesajlariYenile(ActionEvent event) {
        mesajlariYukle();
        sonMesajiYukle();
    }

    @FXML void mesajiSil(ActionEvent event) {
        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "DELETE FROM mesajlar WHERE musteri_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            ps.executeUpdate();

            ps.close();
            conn.close();

            mesajlarVBox.getChildren().clear();
            sonMesajLabel.setText("Henüz mesaj yok.");
            mesajSaatLabel.setText("-");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void sohbetAc(MouseEvent event) {
        sohbetAdLabel.setText("Lina Beauty Center");
        sohbetBaslikLabel.setText("Lina Beauty Center");
        emailLabel.setText("info@linabeauty.com");

        if (okunmadiCircle != null) {
            okunmadiCircle.setFill(Color.TRANSPARENT);
        }

        mesajlariYukle();
    }

    @FXML void panelimGoster(ActionEvent event) {
        try { sayfaAc(event, "KullaniciPanel.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void profilBilgileriAc(ActionEvent event) {
        try { sayfaAc(event, "ProfilBilgileri.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void randevuAlAc(ActionEvent event) {
        try { sayfaAc(event, "KullaniciRandevuAl.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void randevularimiAc(ActionEvent event) {
        try { sayfaAc(event, "Randevularim.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void mesajlarGoster(ActionEvent event) {
        mesajlariYukle();
        sonMesajiYukle();
    }

    @FXML void cikisYap(ActionEvent event) {
        try {
            KullaniciOturum.oturumKapat();
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {
        sohbetAdLabel.setText("Lina Beauty Center");
        sohbetBaslikLabel.setText("Lina Beauty Center");
        emailLabel.setText("info@linabeauty.com");

        saatTarihBaslat();
        mesajlariYukle();
        sonMesajiYukle();
    }
}