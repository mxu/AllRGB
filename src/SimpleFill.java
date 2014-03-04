import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SimpleFill {

    public static void main(String[] args) {
        int bitsPerChannel = 7;
        int maxValue = (int) Math.pow(2, bitsPerChannel);
        int totalPixels = (int) Math.pow(maxValue, 3);
        double squareWidth = Math.sqrt(totalPixels);
        int w = (int) squareWidth;
        int h = (int) squareWidth;
        if(squareWidth - Math.round(squareWidth) > 0) {
            w = 1;
            while(w < squareWidth) w *= 2;
            h = totalPixels / w;
        }
        int channelMask = 1;
        for(int i = 1; i < bitsPerChannel; i++) {
            channelMask <<= 1;
            channelMask++;
        }
        System.out.println(bitsPerChannel + " bits per channel");
        System.out.println(maxValue + "^3 = " + totalPixels + " distinct colors");
        System.out.println(w + "x" + h + " px image");

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x = 0;
        int y = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for(int i = 0; i < totalPixels; i++) {
            img.setRGB(x, y, (((r * 255 / channelMask) << 8 | (g * 255 / channelMask)) << 8) | (b * 255 / channelMask));
            x++;
            if(x == w) {
                x = 0;
                y++;
            }
            if(r++ == channelMask) {
                r = 0;
                if(g++ == channelMask) {
                    g = 0;
                    if(b++ == channelMask)
                        b = 0;
                }
            }
        }

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream("RGB.png"))) {
            ImageIO.write(img, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}