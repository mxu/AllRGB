import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SimpleFill {

    public static int w = 128;
    public static int h = 128;
    public static void main(String[] args) {
        Point[] pList = {new Point(0, 0), new Point(1, 1), new Point(0, 1), new Point(1, 0)};
        for(int i = 0; i < pList.length; i++) {
            Point p = pList[i];
            System.out.println(p + ": " + getAdjacent(p));
        }

        RGB a = new RGB(128, 128, 128);
        RGB b = new RGB(255, 255, 255);
        System.out.println(a.distanceTo(b));

        System.out.println(Integer.toHexString(a.toInt()));
        System.out.println(Integer.toHexString(b.toInt()));
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
        return result;
    }
}