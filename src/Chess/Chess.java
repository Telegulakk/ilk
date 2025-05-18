package Chess;

import Chess.Taslar.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Chess.FinalRenkler.*;
import static Chess.OyunDurumu.*;

public class Chess {
    JFrame frame = new JFrame();
    JPanel boardPanel = new JPanel();
    JButton[][] kareler = new JButton[8][8];
    Tas[][] tahta = new Tas[8][8]; // Oyunun mantıksal durumu
    JLayeredPane layeredPane = new JLayeredPane();
    CircularHoleLabel sahTehditGostergesi = null;

    int i1 = -1; // Seçilen ilk karenin satır index'i
    int k1 = -1; // Seçilen ilk karenin sütun index'i
    // sah konumlari
    int beyazSahI = 7;
    int beyazSahK = 4;
    int siyahSahI = 0;
    int siyahSahK = 4;

    public Chess() {
        // assagıdaki ikisi yerine şunu ekleyebilirsin : frame.add(layeredPane, BorderLayout.CENTER); // frame'e boardPanel yerine layeredPane'i ekle
        frame.setContentPane(layeredPane); // frame'in içerik bölmesi layeredPane oluyor
        layeredPane.setLayout(null);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        boardPanel.setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                JButton tile = new JButton();
                kareler[i][k] = tile;
                boardPanel.add(tile);

                if ((i + k) % 2 == 1) {
                    tile.setBackground(DARK_SQUARE_COLOR);
                } else {
                    tile.setBackground(LIGHT_SQUARE_COLOR);
                }

                tile.setBorderPainted(false); // Kenarlıkları kaldır
                tile.setFocusable(false);     // Odaklanma efektini kaldır // basma çizgisi falan gidiyor
                tile.setFont(new Font("SansSerif", Font.BOLD, 50)); // Daha okunaklı bir font ve boyut
                tile.setActionCommand(i + "," + k); // Koordinat bilgisini ekle
                kareyeMouseListenerEkle(tile, i, k);
            }
        }

        // TAŞLARI EKLE
        TasYerlestirme.baslangicTahtasiniOlustur(tahta, kareler, WHITE_PIECE_COLOR, BLACK_PIECE_COLOR);

        // kare boyutu x 8 -> borad ve layeredi bu boyuta getirdik. kareden tahtaya tümevarım
        tahtaBoyutlandirmasi();

        // Diğer ayarlar (pack'ten sonra yapmak daha iyi olabilir)
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // Boyut sabit kalsın
        frame.setVisible(true); // Çerçeveyi görünür yap
    }

    public void hesaplama(int i1, int k1, int i2, int k2) {
        Tas hareketEdenTas = tahta[i1][k1];
        boolean isCastling = hareketEdenTas.getIsim().equals("Ş") && Math.abs(k2 - k1) == 2;

        if (i1 == -1 || hareketEdenTas == null) { // Seçili bir taş yoksa veya boşsa çık
            return;
        }

        if (!hareketEdenTas.getRenk().equals(aktifOyuncu)) {  //  Sırası gelen oyuncu değilse bitir ve ses cikar
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        // hamle sonunda tehtid altinda kalan sahin konum bilgileri. hala tehtidde mi diye incelemek icin
        int tehtiddeOlanSahI;
        int tehtiddeOlanSahK;
        if (aktifOyuncu.equals("B")) {
            tehtiddeOlanSahI = beyazSahI;
            tehtiddeOlanSahK = beyazSahK;
        } else {
            tehtiddeOlanSahI = siyahSahI;
            tehtiddeOlanSahK = siyahSahK;
        }

        // YANDAN GEÇME KURALI
        if (hareketEdenTas instanceof Piyon &&
                Math.abs(i2 - i1) == 1 &&
                Math.abs(k2 - k1) == 1 &&
                tahta[i2][k2] == null &&
                EnPassantDurumu.aktif &&
                i1 == EnPassantDurumu.hedefSatir &&
                k2 == EnPassantDurumu.hedefSutun) {
            HareketYardimcisi.yandanYeme(layeredPane, hareketEdenTas, tahta, kareler, i1, i2, k1, k2, beyazSahI, beyazSahK, siyahSahI, siyahSahK, sahTehditGostergesi);

        } else if (hareketEdenTas.hamleGecerliMi(i1, k1, i2, k2, tahta) && HareketYardimcisi.velevKiOynadik(i1, k1, i2, k2, tehtiddeOlanSahI, tehtiddeOlanSahK, tahta)) {
            // YANDAN GECME ZAMANLAMASI AYARI // 2 adim atan piyonun yaninda rakip piyon var mi
            if (hareketEdenTas instanceof Piyon && Math.abs(i2 - i1) == 2) {
                EnPassantDurumu.ayarla(i2, k2); // bu piyonun koordinatını kaydet
            } else {
                EnPassantDurumu.sifirla(); // her hamle sonrası sıfırla. ÇÜNKÜ YANDAN ALINMA O AN YAPILIR
            }

            // TERFİ
            if (hareketEdenTas instanceof Piyon && (i2 == 7 || i2 == 0)) {
                HareketYardimcisi.terfiPenceresiGoster(layeredPane, kareler, tahta, i1, k1, i2, k2, hareketEdenTas);
                // ROK
            } else if (isCastling) {
                HareketYardimcisi.isCastling(kareler, tahta, i1, k1, k2, hareketEdenTas);
                // NORMAL HAMLE
            } else {
                HareketYardimcisi.normalHamle(kareler, tahta, i1, k1, i2, k2, hareketEdenTas);
            }

            // OYNANAN SAH İSE YERİNİ GUNCELLE
            if (hareketEdenTas instanceof Sah) {
                if (hareketEdenTas.getRenk().equals("B")) {
                    beyazSahI = i2;
                    beyazSahK = k2;
                } else {
                    siyahSahI = i2;
                    siyahSahK = k2;
                }
            }

            // SONRAKİ HAMLENİN OYUNCUSU RENGİ
            aktifOyuncu = (aktifOyuncu.equals("B") ? "S" : "B"); // Oyuncu DEĞİŞTİ!
            String rakipRenk = aktifOyuncu; // Sırası gelen oyuncunun rengi

            // HAMLE SONUCU RAKİP ŞAH MAT VEYA PAT OLDU MU İÇİN LAZIM
            int rakipSahI, rakipSahK;
            if (rakipRenk.equals("B")) {
                rakipSahI = beyazSahI;
                rakipSahK = beyazSahK;
            } else {
                rakipSahI = siyahSahI;
                rakipSahK = siyahSahK;
            }

            // anlik oynanmis olan renk
            String aktifRenk = (aktifOyuncu.equals("B") ? "S" : "B");
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
                    gameOver = true;
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
                    gameOver = true;
                }
            }
        } else {
            Toolkit.getDefaultToolkit().beep(); // Geçersiz hamle sesi
        }
    }

    private void kareyeMouseListenerEkle(JButton tile, int i, int k) {
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (terfiVarMi) {
                    return; // Terfi sırasında tıklamaları yok say
                }
                String[] coords = ((JButton) e.getSource()).getActionCommand().split(",");
                int i2 = Integer.parseInt(coords[0]);
                int k2 = Integer.parseInt(coords[1]);

                if (!gameOver) {
                    if (i1 == -1) { // İlk Tıklama: Taş Seçimi
                        if (tahta[i2][k2] != null && tahta[i2][k2].getRenk().equals(aktifOyuncu)) { // sırası gelen oyuncunun taşı
                            i1 = i2;
                            k1 = k2;
                            kareler[i1][k1].setBackground(SELECTED_SQUARE_COLOR); // Seçimi görselleştir
                        }
                    } else { // İkinci Tıklama: Hedef Kare / Hamle
                        // Önce eski seçimi temizle
                        if ((i1 + k1) % 2 == 1) kareler[i1][k1].setBackground(DARK_SQUARE_COLOR);
                        else kareler[i1][k1].setBackground(LIGHT_SQUARE_COLOR);

                        if (i1 == i2 && k1 == k2) { // Aynı kareye tıklandıysa seçimi iptal et
                            i1 = -1; // Seçimi sıfırla
                            k1 = -1;
                            //return;
                        } else {
                            hesaplama(i1, k1, i2, k2); // Hamleyi yapmayı dene
                            layeredPane.revalidate();
                            layeredPane.repaint();
                            i1 = -1; // Seçimi sıfırla
                            k1 = -1;
                        }
                    }
                }

            }
        });
    }

    private void tahtaBoyutlandirmasi(){
        // özetle kare boyutu belileyip 8le çarpıp borad ve layeredi da bu boyuta getirdik. kareden tahtaya tümevarım
        int tahtaPikselBoyutu = 8 * 90;
        Dimension boardPrefSize = new Dimension(tahtaPikselBoyutu, tahtaPikselBoyutu);
        boardPanel.setPreferredSize(boardPrefSize); // boardPanel'e tercih ettiği boyutu söyle
        layeredPane.setPreferredSize(boardPrefSize); // layeredPane de aynı boyutu tercih etsin
        boardPanel.setBounds(0, 0, tahtaPikselBoyutu, tahtaPikselBoyutu);
        layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        frame.pack(); // framein icindekilere göre frame boyutu ayarlama
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Chess()); // GUI'yi EDT üzerinde başlat
    }
}
    /*
     !!!!!!!!!!!!!!!!!!! İSTEĞE BAĞLI !!!!!!!!!!!!!!!!!!!!!!
    tıklanan taşın nerelere gidebildiğini gösteren noktacıklar
    son tıklanan taş ve ayrılan yer
    geriye veya ileri sarma
    süre
    oynan hamle text bilgisi
    new game
    */