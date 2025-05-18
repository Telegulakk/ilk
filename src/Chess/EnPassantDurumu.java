package Chess;

public class EnPassantDurumu {
    public static boolean aktif = false;
    public static int hedefSatir = -1;
    public static int hedefSutun = -1;

    public static void ayarla(int i, int k) {
        aktif = true;
        hedefSatir = i;
        hedefSutun = k;
    }

    public static void sifirla() {
        aktif = false;
        hedefSatir = -1;
        hedefSutun = -1;
    }
}

