import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Mike on 3/3/14.
 */
public class CenterFill {

    private static int w;
    private static int h;

    public static void main(String[] args) {
        int bitsPerChannel = 5;
        int maxValue = (int) Math.pow(2, bitsPerChannel);
        int totalPixels = (int) Math.pow(maxValue, 3);
        double squareWidth = Math.sqrt(totalPixels);
        w = (int) squareWidth;
        h = (int) squareWidth;
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

        getAdjacent(new Point(0, 0));
        getAdjacent(new Point(1, 1));
        getAdjacent(new Point(w - 1, 0));
        ArrayList<Integer> colors = new ArrayList<>();
        int r = 0;
        int g = 0;
        int b = 0;
        for(int i = 0; i < totalPixels; i++) {
            colors.add((((r * 255 / channelMask) << 8 | (g * 255 / channelMask)) << 8) | (b * 255 / channelMask));
            if(r++ == channelMask) {
                r = 0;
                if(g++ == channelMask) {
                    g = 0;
                    if(b++ == channelMask)
                        b = 0;
                }
            }
        }

        int[][] pixels = new int[w][h];
        ArrayList<Point> edge = new ArrayList<>();
        while(colors.size() > 0) {
            int c = colors.get((int)(Math.random() * colors.size()));
            Point p = new Point();
            if(colors.size() == totalPixels) {
                p.x = w / 2;
                p.y = h / 2;
                edge = getAdjacent(p);
            } else {
                Point min = new Point();
                int minD = Integer.MAX_VALUE;
                // for each point in the open list
                for(Point np: edge) {
                    // find minimum difference adjacent color
                    for(Point test: getAdjacent(np)) {
                        // d = color at test - c
                        // if(d < minD) {
                        //  minD = d;
                        //  min = test;
                    }
                }
                p.x = min.x;
                p.y = min.y;
            }
            pixels[p.x][p.y] = c;
        }

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream("RGB.png"))) {
            ImageIO.write(img, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Point> getAdjacent(Point p) {
        ArrayList<Point> result = new ArrayList<>();
        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                if(p.x + dx > -1 && p.x + dx < w) {
                    if(p.y + dy > -1 && p.y + dy < h) {
                        if(dx != 0 || dy != 0) {
                            result.add(new Point(p.x + dx, p.y + dy));
                        }
                    }
                }
            }
        }
        System.out.println(result);
        return result;
    }

}
