package Chess.Taslar;

import Chess.Tas;

public class Piyon extends Tas {

    public Piyon(String renk) {
        super(renk, "P");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        int yon = this.renk.equals("B") ? -1 : 1; // Beyaz yukarı, Siyah aşağı
        int caprazFarkK = Math.abs(k2 - k1);
        boolean caprazYonDogru = (i2 == i1 + yon);

        // Düz bir kare ileri
        if (k1 == k2 && tahta[i2][k2] == null && i2 == i1 + yon) {
            return true;
        }

        // --- Çapraz Hareketler ---
        if (caprazFarkK == 1 && caprazYonDogru) {
            // 1. Normal Çapraz Alma
            if (tahta[i2][k2] != null && !tahta[i2][k2].getRenk().equals(this.renk)) {
                return true;
            }
        }



        // Başlangıçta iki adım gidebilme
        boolean baslangicSirasi = (this.renk.equals("B") && i1 == 6) || (this.renk.equals("S") && i1 == 1);
        boolean ikiAdimIleri = i2 == i1 + (2 * yon);
        if (baslangicSirasi && ikiAdimIleri && k1 == k2 && tahta[i1 + yon][k1] == null && tahta[i2][k2] == null) {
            return true;
        }

        return false;
    }

}