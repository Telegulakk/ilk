package Chess;

import Chess.Taslar.*;

import javax.swing.*;
import java.awt.*;

import static Chess.FinalRenkler.BLACK_PIECE_COLOR;
import static Chess.FinalRenkler.WHITE_PIECE_COLOR;

public class HareketYardimcisi {

    public static void yandanYeme(JLayeredPane layeredPane, Tas hareketEdenTas, Tas[][] tahta, JButton[][] kareler, int i1, int i2, int k1, int k2, int beyazSahI, int beyazSahK,
                                  int siyahSahI, int siyahSahK, CircularHoleLabel sahTehditGostergesi) {
        Color yiyeninRengi = (hareketEdenTas.getRenk().equals("B") ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);

        tahta[i2][k2] = hareketEdenTas;
        tahta[i1][k1] = null;
        tahta[i1][k2] = null;

        kareler[i2][k2].setText(hareketEdenTas.getIsim());
        kareler[i1][k1].setText("");
        kareler[i1][k2].setText("");
        kareler[i2][k2].setForeground(yiyeninRengi);

        EnPassantDurumu.sifirla();

        //sonraki hamlenin oyuncusu
        OyunDurumu.aktifOyuncu = (OyunDurumu.aktifOyuncu.equals("B") ? "S" : "B"); // Oyuncu DEĞİŞTİ!
        String rakipRenk = OyunDurumu.aktifOyuncu; // Sırası gelen oyuncunun rengi

        int rakipSahI, rakipSahK;
        if (rakipRenk.equals("B")) {
            rakipSahI = beyazSahI;
            rakipSahK = beyazSahK;
        } else {
            rakipSahI = siyahSahI;
            rakipSahK = siyahSahK;
        }

        // anlik oynanmis olan renk
        String aktifRenk = (OyunDurumu.aktifOyuncu.equals("B") ? "S" : "B");
        if (HareketYardimcisi.kareTehditAltindaMi(rakipSahI, rakipSahK, aktifRenk, tahta)) { // aktif rengin tersine bakiyoruz metodun içinde
            System.out.println("ŞAH ÇEKİLDİ!"); // Debug

            // Önceki göstergeyi kaldır (varsa)
            if (sahTehditGostergesi != null) {
                layeredPane.remove(sahTehditGostergesi);
            }

            // Yeni göstergeyi DOĞRU konuma ekle
            int x = rakipSahK * 90;
            int y = rakipSahI * 90;
            sahTehditGostergesi = new CircularHoleLabel(Color.RED, null, 90);
            sahTehditGostergesi.setBounds(x, y, 90, 90);
            layeredPane.add(sahTehditGostergesi, JLayeredPane.DRAG_LAYER);
            layeredPane.revalidate();
            layeredPane.repaint();

            // Şah Mat kontrolü (Rakip oyuncunun geçerli hamlesi var mı?)
            if (HareketYardimcisi.sahMatPatKontrol(rakipRenk, tahta, rakipSahI, rakipSahK)) {
                System.out.println("ŞAH MAT");
                OyunDurumu.gameOver = true;
            }

        } else { // Rakip Şah tehdit altında DEĞİL
            // Mevcut göstergeyi kaldır (varsa)
            if (sahTehditGostergesi != null) {
                System.out.println("Tehdit kalktı, gösterge kaldırılıyor."); // Debug
                layeredPane.remove(sahTehditGostergesi);
                sahTehditGostergesi = null;
                layeredPane.revalidate();
                layeredPane.repaint();
            }
            // Pat kontrolü (Rakip oyuncunun geçerli hamlesi var mı?)
            if (HareketYardimcisi.sahMatPatKontrol(rakipRenk, tahta, rakipSahI, rakipSahK)) { // rakip sah
                kareler[siyahSahI][siyahSahK].setBackground(Color.DARK_GRAY);
                kareler[beyazSahI][beyazSahK].setBackground(Color.DARK_GRAY);
                System.out.println("PAT");
                // ... (Pat gösterimi) ...
                OyunDurumu.gameOver = true;
            }
        }
    }

    public static void gostergeKoy(JLayeredPane layeredPane, int i, int k, CircularHoleLabel oncekiGosterge) {
        if (oncekiGosterge != null) {
            layeredPane.remove(oncekiGosterge);
        }
        CircularHoleLabel yeniGosterge = new CircularHoleLabel(Color.RED, null, 90);
        yeniGosterge.setBounds(k * 90, i * 90, 90, 90);
        layeredPane.add(yeniGosterge, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();

        // HareketYardimcisi.gostergeKoy(layeredPane,rakipSahI,rakipSahK,sahTehditGostergesi);    bunu kullan
    }

    public static void gostergeKaldir(JLayeredPane layeredPane, CircularHoleLabel mevcut) {
        if (mevcut != null) {
            layeredPane.remove(mevcut);
            mevcut = null;
            layeredPane.revalidate();
            layeredPane.repaint();
        }

        // HareketYardimcisi.gostergeKaldir(layeredPane,sahTehditGostergesi);     bunu kullan
    }

    public static void kareyiGuncelle(JButton[][] kareler, int i, int k, String isim, Color renk) {
        kareler[i][k].setText(isim);
        kareler[i][k].setForeground(renk);
    }

    public static void kareyiTemizle(JButton[][] kareler, int i, int k) {
        kareler[i][k].setText("");
    }

    public static void isCastling(JButton[][] kareler, Tas[][] tahta, int i1, int k1, int k2, Tas hareketEdenTas) {
        int yonK = Integer.signum(k2 - k1); // +1 sağa (kısa), -1 sola (uzun)
        int rookK = (yonK == 1) ? 7 : 0;     // Kalenin başlangıç sütunu
        int rookNewK = k1 + yonK;           // Kalenin yeni sütunu (Şah'ın geçtiği kare)

        Tas kale = tahta[i1][rookK];

        // 1. Mantıksal Tahtayı Güncelle
        tahta[i1][k2] = hareketEdenTas; // Şahı yeni yerine taşı
        tahta[i1][rookNewK] = kale;    // Kaleyi yeni yerine taşı
        tahta[i1][k1] = null;         // Şahın eski yerini boşalt
        tahta[i1][rookK] = null;     // Kalenin eski yerini boşalt

        // 2. GUI'yi Güncelle
        kareyiGuncelle(kareler, i1, k2, hareketEdenTas.getIsim(), hareketEdenTas.getRenk().equals("B") ? Color.white : Color.BLACK);
        kareyiGuncelle(kareler, i1, rookNewK, kale.getIsim(), kale.getRenk().equals("B") ? Color.white : Color.BLACK);
        kareyiTemizle(kareler, i1, k1);
        kareyiTemizle(kareler, i1, rookK);

        // 3. Hareket Durumlarını Güncelle
        hareketEdenTas.setHareketEtti();
        kale.setHareketEtti();
    }

    public static void terfiPenceresiGoster(
            JLayeredPane pane, JButton[][] kareler,
            Tas[][] tahta, int i1, int k1, int i2, int k2, Tas hareketEdenTas) {

        Color renk = (hareketEdenTas.getRenk().equals("B") ? Color.WHITE : Color.BLACK);
        Point ekranNoktasi = kareler[i2][k2].getLocationOnScreen();
        int x = ekranNoktasi.x;
        int y = hareketEdenTas.getRenk().equals("B") ? ekranNoktasi.y : ekranNoktasi.y - 270;

        JDialog pencere = new JDialog((JFrame) SwingUtilities.getWindowAncestor(pane), "Terfi", true);
        pencere.setUndecorated(true);
        pencere.setSize(90, 360);
        pencere.setLayout(new GridLayout(4, 1));
        pencere.setLocation(x, y);

        String[] secenekler = {"V", "K", "A", "F"};
        for (String secenek : secenekler) {
            JButton btn = new JButton(secenek);
            btn.setFont(new Font("SansSerif", Font.BOLD, 50));
            btn.setForeground(Color.MAGENTA);
            btn.setBackground(Color.BLACK);
            btn.addActionListener(e -> {
                switch (secenek) {
                    case "V" -> tahta[i2][k2] = new Vezir(hareketEdenTas.getRenk());
                    case "K" -> tahta[i2][k2] = new Kale(hareketEdenTas.getRenk());
                    case "A" -> tahta[i2][k2] = new At(hareketEdenTas.getRenk());
                    case "F" -> tahta[i2][k2] = new Fil(hareketEdenTas.getRenk());
                }
                tahta[i1][k1] = null;
                kareyiGuncelle(kareler, i2, k2, secenek, renk);
                kareyiTemizle(kareler, i1, k1);
                pencere.dispose();
            });
            pencere.add(btn);
        }

        pencere.setVisible(true); // bloklar
    }

    public static void normalHamle(JButton[][] kareler, Tas[][] tahta, int i1, int k1, int i2, int k2, Tas hareketEdenTas) {
        // NORMAL HAMLEYİ GERÇEKLEŞTİR
        // 1. Mantıksal Tahtayı Güncelle
        tahta[i2][k2] = hareketEdenTas;
        tahta[i1][k1] = null;

        // 2. GUI'yi Güncelle
        kareyiGuncelle(kareler, i2, k2, hareketEdenTas.getIsim(), hareketEdenTas.getRenk().equals("B") ? Color.WHITE : Color.BLACK);
        kareyiTemizle(kareler, i1, k1);

        // 3. Hareket Durumunu Güncelle
        hareketEdenTas.setHareketEtti();
    }

    // sadece fil kale ve vezir icin kullaniyoruz
    public static boolean yolEngelliMi(int i1, int k1, int i2, int k2, Tas[][] tahta) {      // static yaptik ki direkt kullanabilelim
        int deltaI = i2 - i1;
        int deltaK = k2 - k1;

        int yonI = Integer.signum(deltaI);   // Hareket yönlerini belirle (1, -1 veya 0)
        int yonK = Integer.signum(deltaK);
        int mevcutI = i1 + yonI;
        int mevcutK = k1 + yonK;

        while (mevcutI != i2 || mevcutK != k2) {
            if (tahta[mevcutI][mevcutK] != null) {
                return true;
            }
            mevcutI += yonI;
            mevcutK += yonK;
        }
        return false;
    }

    public static boolean kareTehditAltindaMi(int sahKonumuI, int sahKonumuK, String oyuncuRenk, Tas[][] tahta) { //hedef kare sah gibi dusun
        String rakipRenk = oyuncuRenk.equals("B") ? "S" : "B";
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (tahta[i][k] != null && !tahta[i][k].getRenk().equals(rakipRenk) && tahta[i][k].hamleGecerliMi(i, k, sahKonumuI, sahKonumuK, tahta)) { // hedef kareye gidebiliyorlarsa sah da da cekebiliyorlar demektir
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean velevKiOynadik(int mevcutI, int mevcutK, int sonI, int sonK, int sahKonumuI_orijinal, int sahKonumuK_orijinal, Tas[][] tahta) {
        Tas yedekHareketEdenTas = tahta[mevcutI][mevcutK]; // Hareket eden taşı yedekle
        // Hata kontrolü: Başlangıç karesi boşsa veya taş yoksa?
        if (yedekHareketEdenTas == null) {
            System.err.println("velevKiOynadik HATA: Hareket eden taş bulunamadı!");
            return false; // Bu hamle zaten geçersiz olurdu
        }
        Tas yedekHedeftekiTas = tahta[sonI][sonK];      // Hedef karedeki taşı (veya null'u) yedekle

        // Simülasyon: Hamleyi geçici olarak yap
        tahta[sonI][sonK] = yedekHareketEdenTas;
        tahta[mevcutI][mevcutK] = null;

        // --- ŞAH'IN KONTROL EDİLECEK DOĞRU KONUMUNU BELİRLE ---

        String hareketEdenRenk = yedekHareketEdenTas.getRenk();
        String rakipRenk = hareketEdenRenk.equals("B") ? "S" : "B"; // Rakip rengi belirle // DEĞİŞİKLİK
        int kontrolI = sahKonumuI_orijinal;
        int kontrolK = sahKonumuK_orijinal;

        if (yedekHareketEdenTas instanceof Sah) {
            kontrolI = sonI;
            kontrolK = sonK;
        }
        // --- Konum Belirleme Sonu ---

        // Tehdit Kontrolü (Düzeltilmiş kareTehditAltindaMi çağrısı)
        boolean sahTehditte = kareTehditAltindaMi(kontrolI, kontrolK, rakipRenk, tahta); // Rakiplerin tehdidini kontrol et // DEĞİŞİKLİK

        // Tahtayı Eski Haline Getir (Geri Alma)
        tahta[mevcutI][mevcutK] = yedekHareketEdenTas;
        tahta[sonI][sonK] = yedekHedeftekiTas;

        // Sonucu Döndür (Tehdit yoksa hamle geçerlidir -> true)
        return !sahTehditte;
    }

    public static boolean sahMatPatKontrol(String tehtiddeOlanSahRengi, Tas[][] tahta, int tehtiddeOlanSahI, int tehtiddeOlanSahK) {
        for (int i1 = 0; i1 < 8; i1++) {
            for (int k1 = 0; k1 < 8; k1++) {
                Tas mevcutKare = tahta[i1][k1];
                if (mevcutKare != null && mevcutKare.getRenk().equals(tehtiddeOlanSahRengi)) {
                    for (int i2 = 0; i2 < 8; i2++) {
                        for (int k2 = 0; k2 < 8; k2++) {
                            if (mevcutKare.hamleGecerliMi(i1, k1, i2, k2, tahta) && velevKiOynadik(i1, k1, i2, k2, tehtiddeOlanSahI, tehtiddeOlanSahK, tahta)) {
                                return false;   // eger hamle gecerliyse mat veya pat olamaz demektir. metodu bitir
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

