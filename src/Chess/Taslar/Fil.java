package Chess.Taslar;

import Chess.HareketYardimcisi;
import Chess.Tas;

public class Fil extends Tas {
    public Fil(String renk) {
        super(renk, "F");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        int x = Math.abs(i2 - i1);
        int y = Math.abs(k2 - k1);

        if (!(x == y)) {
            return false;
        }

        // 2. Yol Kontrolü: Başlangıç ve hedef kare arasında başka taş var mı?
        if (HareketYardimcisi.yolEngelliMi(i1, k1, i2, k2, tahta)) {
            return false; // Yolda engel var, hamle geçersiz.
        }

        // 3. Hedef Kare Kontrolü: Hedef karede ne var?
        Tas hedefTas = tahta[i2][k2];
        if (hedefTas == null) {
            return true; // Hedef kare boş, hamle geçerli.
        } else {
            // Hedefte bir taş var. Rakip mi, kendi taşımız mı?
            if (!hedefTas.getRenk().equals(this.renk)) {
                return true; // Rakip taş, hamle geçerli (taş alma).
            } else {
                return false; // Kendi taşının üzerine gidemezsin, hamle geçersiz.
            }
        }
    }
}
