package application;

public class AdminOturum {

    public static int adminId;
    public static String kullaniciAdi;
    public static String email;

    public static void oturumAc(int id, String kullanici, String mail) {
        adminId = id;
        kullaniciAdi = kullanici;
        email = mail;
    }

    public static void oturumKapat() {
        adminId = 0;
        kullaniciAdi = null;
        email = null;
    }
}