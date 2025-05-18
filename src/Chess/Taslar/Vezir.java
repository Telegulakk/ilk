package Chess.Taslar;

import Chess.HareketYardimcisi;
import Chess.Tas;

public class Vezir extends Tas {
    public Vezir(String renk) {
        super(renk, "V");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        int x = Math.abs(i2 - i1);
        int y = Math.abs(k2 - k1);
        boolean yatayHareket = (i1 == i2 && k1 != k2);
        boolean dikeyHareket = (k1 == k2 && i1 != i2);
        boolean caprazHareket = (x == y);
        Tas hedefTas = tahta[i2][k2];

        if (!caprazHareket && !yatayHareket && !dikeyHareket) {  //fil
            return false;
        }

        if (HareketYardimcisi.yolEngelliMi(i1, k1, i2, k2, tahta)) {
            return false;
        }

        if (hedefTas == null) {
            return true;
        } else {
            if (!hedefTas.getRenk().equals(this.renk)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
