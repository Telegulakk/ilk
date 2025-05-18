package Chess.Taslar;

import Chess.Tas;

public class At extends Chess.Tas {

    public At(String renk) {
        super(renk, "A");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        int x = Math.abs(i2 - i1);
        int y = Math.abs(k2 - k1);
        Tas hedefTas = tahta[i2][k2];

        if (!(x + y == 3 && x != 3 && y != 3)) {
            return false;
        }

        if (hedefTas == null){
            return true;
        }else if (!hedefTas.getRenk().equals(this.renk)){
            return true;
        }else{
            return false;
        }
    }
}
