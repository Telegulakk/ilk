package Chess.Taslar;

import Chess.HareketYardimcisi;
import Chess.Tas;

public class Kale extends Tas {
    public Kale(String renk) {
        super(renk, "K");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        boolean yatayHareket = (i1 == i2 && k1 != k2); // 1. Temel Hareket Kuralı: Hareket tam olarak yatay veya tam olarak dikey mi?
        boolean dikeyHareket = (k1 == k2 && i1 != i2); // Aynı kareye gitme durumu da geçersizdir (k1!=k2 veya i1!=i2 kontrolü bunu sağlar).

        Tas hedefTas = tahta[i2][k2];

        if (!yatayHareket && !dikeyHareket) {
            return false;
        }

        if (HareketYardimcisi.yolEngelliMi(i1, k1, i2, k2, tahta)) {
            return false;
        }

        Tas hareketEdenTas = tahta[i1][k1];
        if (hedefTas == null) {
            return true; // Hedef kare boş, hamle geçerli.
        } else {
            if (!hedefTas.getRenk().equals(this.renk)) {
                return true;
            } else {
                return false; // Kendi taşının üzerine gidemezsin, hamle geçersiz.
            }
        }
    }
}