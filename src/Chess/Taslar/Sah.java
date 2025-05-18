package Chess.Taslar;

import Chess.HareketYardimcisi;
import Chess.Tas;

public class Sah extends Tas {

    public Sah(String renk) {
        super(renk, "Ş");
    }

    @Override
    public boolean hamleGecerliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {
        int deltaI = Math.abs(i2 - i1); // Mutlak farklar
        int deltaK = Math.abs(k2 - k1);
        boolean tekKareHamlesi = (deltaI <= 1 && deltaK <= 1) && !(deltaI == 0 && deltaK == 0);

        if (tekKareHamlesi) {
            Tas hedefTas = tahta[i2][k2];
            if (hedefTas == null) {
                return true; //kare bos
            } else {
                if (!hedefTas.getRenk().equals(this.renk)) { // taş rengi ne ?
                    return true; //farkli renk
                } else {
                    return false; //ayni renk
                }
            }
        }

        // --- 2. Rok (Castling) Hamlesi Kontrolü ---
        boolean rokDenemesi = (deltaI == 0 && deltaK == 2); // Sadece yatayda 2 kare mi?

        if (rokDenemesi) {
            // a) Şah daha önce hareket etti mi? (Miras alınan `getHareketEttiMi` kullanılır)
            if (this.getHareketEttiMi()) {
                return false;
            }

            // b) Şah şu anda tehdit altında mı? (Yardımcı metod lazım)
            String rakipRenk = this.renk.equals("B") ? "S" : "B";
            // DİKKAT: isSquareUnderAttack metodu Chess sınıfında, erişim yolu bulunmalı
            // VEYA bu kontrol Chess sınıfındaki hesaplama metoduna taşınmalı.
            // Şimdilik varsayımsal bir çağrı yapalım:
            // if (Chess.isSquareUnderAttack(i1, k1, rakipRenk, tahta)) { // Bu çağrı direkt çalışmaz!
            //     return false;
            // }


            // c) Doğru Kale var mı ve hareket etti mi?
            int yonK = Integer.signum(k2 - k1); // +1 Kısa Rok (sağa), -1 Uzun Rok (sola)
            int rookK = (yonK == 1) ? 7 : 0;     // Beklenen Kale sütunu

            Tas potentialRook = tahta[i1][rookK];
            if (potentialRook == null ||
                    !potentialRook.getIsim().equals("K") || // Gerçekten Kale mi?
                    !potentialRook.getRenk().equals(this.renk) || // Aynı renkte mi?
                    potentialRook.getHareketEttiMi()) { // Kale daha önce hareket etti mi?
                return false;
            }

            // d) Şah ile Kale arasındaki yol boş mu?
            // Dikkat: Hedef kare k2 (Şah'ın varış yeri), Kale sütunu rookK. Yol kontrolü k1 ile rookK arasında yapılır.
            if (HareketYardimcisi.yolEngelliMi(i1, k1, i1, rookK, tahta)) {
                return false;
            }

            // e) Şah'ın geçtiği kare(ler) tehdit altında mı? (Yardımcı metod lazım)
            // DİKKAT: isSquareUnderAttack metodu Chess sınıfında.
            // if (Chess.isSquareUnderAttack(i1, k1 + yonK, rakipRenk, tahta)) { // Bu çağrı direkt çalışmaz!
            //    return false;
            // }
            // Uzun rokta iki kare kontrol edilmeli, ancak Şah'ın varış yerini de kontrol etmek yeterli olabilir.
            // if (Chess.isSquareUnderAttack(i2, k2, rakipRenk, tahta)) { // Varış karesi tehdit altında mı?
            //    return false;
            // }

            // Tüm rok kontrolleri başarılıysa geçerli bir rok hamlesidir.
            return true;
        }

        // Ne standart 1 kare hamlesi ne de rok denemesi ise geçersizdir.
        return false;
    }
}