package Chess;

import Chess.Taslar.*;

import javax.swing.*;
import java.awt.*;

public class TasYerlestirme {

    public static void baslangicTahtasiniOlustur(Tas[][] tahta, JButton[][] kareler, Color beyazRenk, Color siyahRenk) {

        for (int i = 0; i < 8; i++) {
            tasKoy(tahta, kareler, 6, i, new Piyon("B"), beyazRenk);
            tasKoy(tahta, kareler, 1, i, new Piyon("S"), siyahRenk);
        }

        tasKoy(tahta, kareler, 0, 0, new Kale("S"), siyahRenk);
        tasKoy(tahta, kareler, 0, 7, new Kale("S"), siyahRenk);
        tasKoy(tahta, kareler, 7, 0, new Kale("B"), beyazRenk);
        tasKoy(tahta, kareler, 7, 7, new Kale("B"), beyazRenk);

        tasKoy(tahta, kareler, 0, 1, new At("S"), siyahRenk);
        tasKoy(tahta, kareler, 0, 6, new At("S"), siyahRenk);
        tasKoy(tahta, kareler, 7, 1, new At("B"), beyazRenk);
        tasKoy(tahta, kareler, 7, 6, new At("B"), beyazRenk);

        tasKoy(tahta, kareler, 0, 2, new Fil("S"), siyahRenk);
        tasKoy(tahta, kareler, 0, 5, new Fil("S"), siyahRenk);
        tasKoy(tahta, kareler, 7, 2, new Fil("B"), beyazRenk);
        tasKoy(tahta, kareler, 7, 5, new Fil("B"), beyazRenk);

        tasKoy(tahta, kareler, 0, 3, new Vezir("S"), siyahRenk);
        tasKoy(tahta, kareler, 7, 3, new Vezir("B"), beyazRenk);

        tasKoy(tahta, kareler, 7, 4, new Sah("B"), beyazRenk);
        tasKoy(tahta, kareler, 0, 4, new Sah("S"), siyahRenk);
    }

    private static void tasKoy(Tas[][] tahta, JButton[][] kareler, int i, int k, Tas tas, Color renk) {
        tahta[i][k] = tas;
        kareler[i][k].setText(tas.getIsim());
        kareler[i][k].setForeground(renk);
    }
}
