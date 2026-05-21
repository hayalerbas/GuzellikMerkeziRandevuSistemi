package application;

public class HizmetModel {

    private int id;
    private String hizmetAdi;
    private String kategori;
    private double fiyat;
    private String sure;
    private String durum;

    public HizmetModel(int id, String hizmetAdi, String kategori,
                       double fiyat, String sure, String durum) {
        this.id = id;
        this.hizmetAdi = hizmetAdi;
        this.kategori = kategori;
        this.fiyat = fiyat;
        this.sure = sure;
        this.durum = durum;
    }

    public int getId() { return id; }
    public String getHizmetAdi() { return hizmetAdi; }
    public String getKategori() { return kategori; }
    public double getFiyat() { return fiyat; }
    public String getSure() { return sure; }
    public String getDurum() { return durum; }
}