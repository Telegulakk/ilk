package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area; // Area sınıfını import et
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D; // Rectangle2D'yi de kullanabiliriz

public class CircularHoleLabel extends JLabel {
    private Color fillColor;
    // holeColor'a artık gerek yok, delik şeffaf olacak
    // private Color holeColor;

    public CircularHoleLabel(Color fill, Color hole, int size) { // hole parametresi artık kullanılmayacak
        setPreferredSize(new Dimension(size, size));
        setOpaque(false); // Arka planı şeffaf yap
        fillColor = fill;
        // holeColor = new Color(0, 0, 0, 0); // Buna gerek kalmadı
    }

    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g); // super'i BAŞTA çağırmak genellikle daha iyidir,
        // özellikle JLabel'den kalıtım alıyorsan,
        // ama şeffaf olduğu için büyük fark yaratmayabilir.

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Dış dikdörtgenin Alanını (Area) oluştur
        Area outerRectArea = new Area(new Rectangle2D.Double(0, 0, width, height));

        // 2. İçteki deliğin (Elips) Alanını (Area) oluştur
        int diameter = Math.min(width, height) - 20; // Kenardan biraz boşluk bırakalım
        // Negatif çap hatasını önle:
        if (diameter < 1) diameter = Math.min(width, height); // Çok küçükse tam doldur veya minimum yap
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;
        Area holeEllipseArea = new Area(new Ellipse2D.Double(x, y, diameter, diameter));

        // 3. Dikdörtgen Alanından Elips Alanını ÇIKAR (subtract)
        outerRectArea.subtract(holeEllipseArea); // Dikdörtgenden deliği çıkarıyoruz

        // 4. Kalan Alanı (ortası delik dikdörtgeni) fillColor ile doldur
        g2d.setColor(fillColor);
        g2d.fill(outerRectArea); // Sadece kalan alanı doldur

        g2d.dispose();

        // super.paintComponent(g); // Eğer label'in metin/ikon gibi özelliklerini
        // kullanmıyorsan, super'i sonda çağırmak gereksiz olabilir
        // veya çizimi bozabilir. Başta çağırmak daha güvenli.
    }
}