package application;

public class KullaniciOturum {

    public static int musteriId;
    public static String adSoyad;
    public static String email;
    public static String telefon;

    public static void oturumAc(int id, String ad, String mail, String tel) {
        musteriId = id;
        adSoyad = ad;
        email = mail;
        telefon = tel;
    }

    public static void oturumKapat() {
        musteriId = 0;
        adSoyad = null;
        email = null;
        telefon = null;
    }
}