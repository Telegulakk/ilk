package Chess;

public abstract class Tas {
    protected String renk; // "B" veya "S"
    protected String isim; // "P", "A", "F", "K", "V", "Ş"
    protected boolean hareketEttiMi = false; // HER TAŞ İÇİN AYRI!!! static DEĞİL!


    // Hareket gerçekleştiğinde bu metodu çağırın
    public void setHareketEtti() {
        this.hareketEttiMi = true;
    }

    public boolean getHareketEttiMi() {
        return this.hareketEttiMi;
    }

    public Tas(String renk, String isim) {
        this.renk = renk;
        this.isim = isim;
    }

    public String getIsim() {
        return isim;
    }

    public String getRenk() {
        return renk;
    }

    public abstract boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta);
}