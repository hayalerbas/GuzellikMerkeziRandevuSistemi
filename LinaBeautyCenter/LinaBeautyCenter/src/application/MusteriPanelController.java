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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusteriPanelController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<MusteriModel, Integer> idColumn;
    @FXML private TableColumn<MusteriModel, String> adSoyadColumn;
    @FXML private TableColumn<MusteriModel, String> emailColumn;
    @FXML private TableColumn<MusteriModel, String> telefonColumn;
    @FXML private TableColumn<MusteriModel, String> durumColumn;
    @FXML private TableColumn<MusteriModel, String> kayitTarihiColumn;

    @FXML private TableView<MusteriModel> musteriTable;

    @FXML private Button aktifMusterilerButton;
    @FXML private Button anaSayfaButton;
    @FXML private Button calisanlarButton;
    @FXML private Button cikisButton1;
    @FXML private Button duzenleButton;
    @FXML private Button hizmetlerButton;
    @FXML private Button mesajlarButton;
    @FXML private Button musterilerButton;
    @FXML private Button pasifMusterilerButton;
    @FXML private Button randevularButton1;
    @FXML private Button silbutton;
    @FXML private Button tumMusterilerButton;
    @FXML private Button yeniKayitButton;
    @FXML private Button yenileMusteriButton;

    @FXML private TextField musteriAraField;

    @FXML private Label saatlabel;
    @FXML private Label takvimlabel;

    private ObservableList<MusteriModel> liste =
            FXCollections.observableArrayList();

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

    private void musterileriYukle() {

        liste.clear();

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql = "SELECT * FROM musteriler";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                liste.add(new MusteriModel(
                        rs.getInt("id"),
                        rs.getString("ad_soyad"),
                        rs.getString("email"),
                        rs.getString("telefon"),
                        rs.getString("durum"),
                        rs.getString("kayit_tarihi")
                ));
            }

            musteriTable.setItems(liste);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void aktifMusterileriGoster(ActionEvent event) {

        ObservableList<MusteriModel> filtreliListe =
                FXCollections.observableArrayList();

        for (MusteriModel musteri : liste) {

            if (musteri.getDurum() != null
                    && musteri.getDurum().equalsIgnoreCase("Aktif")) {

                filtreliListe.add(musteri);
            }
        }

        musteriTable.setItems(filtreliListe);
    }

    @FXML
    void pasifMusterileriGoster(ActionEvent event) {

        ObservableList<MusteriModel> filtreliListe =
                FXCollections.observableArrayList();

        for (MusteriModel musteri : liste) {

            if (musteri.getDurum() != null
                    && musteri.getDurum().equalsIgnoreCase("Pasif")) {

                filtreliListe.add(musteri);
            }
        }

        musteriTable.setItems(filtreliListe);
    }

    @FXML
    void tumMusterileriGoster(ActionEvent event) {
        musteriTable.setItems(liste);
    }

    @FXML
    void yenileMusteriler(ActionEvent event) {
        musterileriYukle();
    }

    @FXML
    void silMusteri(ActionEvent event) {

        MusteriModel secili =
                musteriTable.getSelectionModel().getSelectedItem();

        if (secili == null) {
            return;
        }

        try {

            Connection conn = DatabaseConnection.baglan();

            String sql = "DELETE FROM musteriler WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, secili.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

            musterileriYukle();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void yeniKayitAc(ActionEvent event) {

        try {
            sayfaAc(event, "KullaniciKayitPopup.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void duzenleMusteri(ActionEvent event) {

        try {
            sayfaAc(event, "ProfilBilgileri.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        adSoyadColumn.setCellValueFactory(
                new PropertyValueFactory<>("adSoyad"));

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        telefonColumn.setCellValueFactory(
                new PropertyValueFactory<>("telefon"));

        durumColumn.setCellValueFactory(
                new PropertyValueFactory<>("durum"));

        kayitTarihiColumn.setCellValueFactory(
                new PropertyValueFactory<>("kayitTarihi"));

        saatTarihBaslat();
        musterileriYukle();
    }
}