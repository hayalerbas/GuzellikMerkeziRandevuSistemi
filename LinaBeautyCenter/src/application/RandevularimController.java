package application;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RandevularimController {

    @FXML private Button anaSayfaButton, cikisButton, iptalButton, iptalEtButton,
            mesajlarButton, profilMenuButton, randevuAlMenuButton, randevularimMenuButton,
            tamamlananButton, tumRandevularButton, yaklasanButton, yeniRandevuAlButton, yenileButton;

    @FXML private TableView<RandevuModel> randevularTable;

    @FXML private TableColumn<RandevuModel, Integer> idColumn;
    @FXML private TableColumn<RandevuModel, String> hizmetColumn, uzmanColumn, tarihColumn,
            saatColumn, durumColumn;

    @FXML private Label saatlabel, takvimlabel;

    private ObservableList<RandevuModel> liste = FXCollections.observableArrayList();

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

    private void randevulariYukle(String filtre) {
        liste.clear();

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql;

            if ("Tümü".equals(filtre)) {
                sql = "SELECT * FROM randevular WHERE musteri_id = ? ORDER BY tarih DESC, saat DESC";
            } else {
                sql = "SELECT * FROM randevular WHERE musteri_id = ? AND durum = ? ORDER BY tarih DESC, saat DESC";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, KullaniciOturum.musteriId);

            if (!"Tümü".equals(filtre)) {
                ps.setString(2, filtre);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(new RandevuModel(
                        rs.getInt("id"),
                        rs.getString("ad_soyad"),
                        rs.getString("telefon"),
                        rs.getString("hizmet"),
                        rs.getString("uzman"),
                        rs.getString("tarih"),
                        rs.getString("saat"),
                        rs.getString("notunuz"),
                        rs.getString("durum")
                ));
            }

            randevularTable.setItems(liste);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevuIptalEt(ActionEvent event) {
        RandevuModel secili = randevularTable.getSelectionModel().getSelectedItem();

        if (secili == null) {
            return;
        }

        try {
            Connection conn = DatabaseConnection.baglan();

            String sql = "UPDATE randevular SET durum = 'İptal' WHERE id = ? AND musteri_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, secili.getId());
            ps.setInt(2, KullaniciOturum.musteriId);

            ps.executeUpdate();

            ps.close();
            conn.close();

            randevulariYukle("Tümü");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void tumRandevularButton(ActionEvent event) { randevulariYukle("Tümü"); }

    @FXML void yaklasanlariGoster(ActionEvent event) { randevulariYukle("Bekliyor"); }

    @FXML void tamamlananlariGoster(ActionEvent event) { randevulariYukle("Onaylandı"); }

    @FXML void iptalEdilenleriGoster(ActionEvent event) { randevulariYukle("İptal"); }

    @FXML void randevulariYenile(ActionEvent event) { randevulariYukle("Tümü"); }

    @FXML void yeniRandevuAl(ActionEvent event) {
        try { sayfaAc(event, "KullaniciRandevuAl.fxml"); } catch (Exception e) { e.printStackTrace(); }
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

    @FXML void randevularimiAc(ActionEvent event) { randevulariYukle("Tümü"); }

    @FXML void mesajlarGoster(ActionEvent event) {
        try { sayfaAc(event, "KullaniciMesajlar.fxml"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void cikisYap(ActionEvent event) {
        try {
            KullaniciOturum.oturumKapat();
            sayfaAc(event, "AnaSayfa.fxml");
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        hizmetColumn.setCellValueFactory(new PropertyValueFactory<>("hizmet"));
        uzmanColumn.setCellValueFactory(new PropertyValueFactory<>("uzman"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        saatColumn.setCellValueFactory(new PropertyValueFactory<>("saat"));
        durumColumn.setCellValueFactory(new PropertyValueFactory<>("durum"));

        saatTarihBaslat();
        randevulariYukle("Tümü");
    }
}