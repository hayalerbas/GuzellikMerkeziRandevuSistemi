package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.Stage;

public class CiltTestiController {

    @FXML private Button geriButton;
    @FXML private Button leriButton;

    @FXML private Button secenek1Button;
    @FXML private Button secenek2Button;
    @FXML private Button secenek3Button;
    @FXML private Button secenek4Button;

    @FXML private Label soruLabel;
    @FXML private Label soruSayisiLabel;
    @FXML private Label sonucLabel;

    private int aktifSoru = 0;
    private int secilenCevap = 0;

    private int yagliPuan = 0;
    private int kuruPuan = 0;
    private int karmaPuan = 0;
    private int hassasPuan = 0;

    private String[] sorular = {
            "Cildiniz gün içinde nasıl hissedilir?",
            "Sıklıkla sivilce ya da siyah nokta çıkar mı?",
            "Cildiniz temizledikten sonra nasıl hissedilir?",
            "Güneşlenince cildiniz nasıl tepki verir?",
            "Cildinizde gün sonunda makyaj durumu nasıl olur?"
    };

    private String[][] secenekler = {
            {"Parlak ve yağlı", "Kurumuş ve gergin", "Ne çok yağlı ne çok kuru", "Hassas ve kızarık"},
            {"Evet", "Hayır", "Nadiren", "Sık sık çıkar"},
            {"Çok gerilir", "Yumuşak ve dengeli", "Hemen yağlanır", "Kızarır ve hassaslaşır"},
            {"Hemen kızarır", "Hafif yanar sonra bronzlaşır", "Direkt bronzlaşır", "Lekelenme olur"},
            {"Akar / parlar", "Pul pul olur", "Yerinde kalır", "Kızarıklık olur"}
    };

    private void sayfaAc(ActionEvent event, String fxmlAdi) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/" + fxmlAdi));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void initialize() {
        sonucLabel.setVisible(false);
        soruyuGoster();
    }

    private void soruyuGoster() {
        soruSayisiLabel.setText((aktifSoru + 1) + " / " + sorular.length);

        soruLabel.setVisible(true);
        sonucLabel.setVisible(false);

        soruLabel.setText(sorular[aktifSoru]);

        secenek1Button.setText(secenekler[aktifSoru][0]);
        secenek2Button.setText(secenekler[aktifSoru][1]);
        secenek3Button.setText(secenekler[aktifSoru][2]);
        secenek4Button.setText(secenekler[aktifSoru][3]);

        secenekleriGoster();
        secenekStilSifirla();

        secilenCevap = 0;

        geriButton.setVisible(aktifSoru != 0);
        leriButton.setVisible(true);

        if (aktifSoru == sorular.length - 1) {
            leriButton.setText("Sonuç");
        } else {
            leriButton.setText("İleri");
        }
    }

    @FXML
    void geriGit(ActionEvent event) {
        if (aktifSoru > 0) {
            aktifSoru--;
            soruyuGoster();
        }
    }

    @FXML
    void ileriGit(ActionEvent event) {
        if (secilenCevap == 0) {
            soruLabel.setText("Lütfen bir seçenek seçiniz.");
            return;
        }

        puanEkle();

        if (aktifSoru < sorular.length - 1) {
            aktifSoru++;
            soruyuGoster();
        } else {
            sonucuGoster();
        }
    }

    @FXML
    void openAnaSayfa(ActionEvent event) throws IOException {
        sayfaAc(event, "AnaSayfa.fxml");
    }

    @FXML
    void secenek1Sec(ActionEvent event) {
        secenekSec(1, secenek1Button);
    }

    @FXML
    void secenek2Sec(ActionEvent event) {
        secenekSec(2, secenek2Button);
    }

    @FXML
    void secenek3Sec(ActionEvent event) {
        secenekSec(3, secenek3Button);
    }

    @FXML
    void secenek4Sec(ActionEvent event) {
        secenekSec(4, secenek4Button);
    }

    private void secenekSec(int cevapNo, Button secilenButton) {
        secilenCevap = cevapNo;
        secenekStilSifirla();
        secilenButton.setStyle(seciliStyle());
    }

    private void puanEkle() {
        if (aktifSoru == 0) {
            if (secilenCevap == 1) yagliPuan++;
            if (secilenCevap == 2) kuruPuan++;
            if (secilenCevap == 3) karmaPuan++;
            if (secilenCevap == 4) hassasPuan++;
        }

        if (aktifSoru == 1) {
            if (secilenCevap == 1) yagliPuan++;
            if (secilenCevap == 2) karmaPuan++;
            if (secilenCevap == 3) karmaPuan++;
            if (secilenCevap == 4) yagliPuan++;
        }

        if (aktifSoru == 2) {
            if (secilenCevap == 1) kuruPuan++;
            if (secilenCevap == 2) karmaPuan++;
            if (secilenCevap == 3) yagliPuan++;
            if (secilenCevap == 4) hassasPuan++;
        }

        if (aktifSoru == 3) {
            if (secilenCevap == 1) hassasPuan++;
            if (secilenCevap == 2) karmaPuan++;
            if (secilenCevap == 3) yagliPuan++;
            if (secilenCevap == 4) hassasPuan++;
        }

        if (aktifSoru == 4) {
            if (secilenCevap == 1) yagliPuan++;
            if (secilenCevap == 2) kuruPuan++;
            if (secilenCevap == 3) karmaPuan++;
            if (secilenCevap == 4) hassasPuan++;
        }
    }

    private void sonucuGoster() {
        soruSayisiLabel.setText("Sonuç");

        soruLabel.setVisible(false);
        sonucLabel.setVisible(true);

        secenek1Button.setVisible(false);
        secenek2Button.setVisible(false);
        secenek3Button.setVisible(false);
        secenek4Button.setVisible(false);

        geriButton.setVisible(false);
        leriButton.setVisible(false);

        if (yagliPuan >= kuruPuan && yagliPuan >= karmaPuan && yagliPuan >= hassasPuan) {
            sonucLabel.setText(
                    "Cilt Tipiniz: Yağlı Cilt\n\n" +
                    "Cildiniz gün içinde parlamaya ve yağlanmaya yatkın görünüyor.\n\n" +
                    "Önerilen bakım:\n" +
                    "• Gözenek arındırıcı bakım\n" +
                    "• Siyah nokta temizliği\n" +
                    "• Sebum dengeleyici bakım"
            );
        } else if (kuruPuan >= yagliPuan && kuruPuan >= karmaPuan && kuruPuan >= hassasPuan) {
            sonucLabel.setText(
                    "Cilt Tipiniz: Kuru Cilt\n\n" +
                    "Cildiniz yoğun neme ihtiyaç duyuyor.\n\n" +
                    "Önerilen bakım:\n" +
                    "• Yoğun nem terapisi\n" +
                    "• Cilt bariyeri güçlendirme\n" +
                    "• Nem bakımı"
            );
        } else if (hassasPuan >= yagliPuan && hassasPuan >= kuruPuan && hassasPuan >= karmaPuan) {
            sonucLabel.setText(
                    "Cilt Tipiniz: Hassas Cilt\n\n" +
                    "Cildiniz kızarıklık ve hassasiyete yatkın görünüyor.\n\n" +
                    "Önerilen bakım:\n" +
                    "• Hassas cilt bakımı\n" +
                    "• Yatıştırıcı bakım\n" +
                    "• Koruyucu nem desteği"
            );
        } else {
            sonucLabel.setText(
                    "Cilt Tipiniz: Karma / Normal Cilt\n\n" +
                    "Cildiniz genel olarak dengeli görünüyor.\n\n" +
                    "Önerilen bakım:\n" +
                    "• Düzenli cilt bakımı\n" +
                    "• Nem desteği\n" +
                    "• Parlaklık kazandıran bakım"
            );
        }
    }

    private void secenekleriGoster() {
        secenek1Button.setVisible(true);
        secenek2Button.setVisible(true);
        secenek3Button.setVisible(true);
        secenek4Button.setVisible(true);
    }

    private void secenekStilSifirla() {
        secenek1Button.setStyle(normalStyle());
        secenek2Button.setStyle(normalStyle());
        secenek3Button.setStyle(normalStyle());
        secenek4Button.setStyle(normalStyle());
    }

    private String normalStyle() {
        return "-fx-background-color: rgba(255,255,255,0.38);" +
                "-fx-background-radius: 22;" +
                "-fx-border-color: rgba(212,162,76,0.45);" +
                "-fx-border-radius: 22;" +
                "-fx-border-width: 1.3;" +
                "-fx-text-fill: #3b2416;" +
                "-fx-font-size: 15px;" +
                "-fx-font-family: Georgia;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;";
    }

    private String seciliStyle() {
        return "-fx-background-color: rgba(255,248,240,0.75);" +
                "-fx-background-radius: 22;" +
                "-fx-border-color: #d4a24c;" +
                "-fx-border-radius: 22;" +
                "-fx-border-width: 2;" +
                "-fx-text-fill: #3b2416;" +
                "-fx-font-size: 15px;" +
                "-fx-font-family: Georgia;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;";
    }
}