import sun.management.resources.agent_ja;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CenterFill {

    private static final int bitsPerChannel = 6;
    private static final int snapshots = 60;
    private static final String algorithm = "avg";

    private static int w;
    private static int h;

    public static void main(String[] args) {
        // calculate the number of possible colors
        int maxValue = (int) Math.pow(2, bitsPerChannel);
        int totalPixels = (int) Math.pow(maxValue, 3);
        int nextSnap = totalPixels / snapshots;
        int snapshot = 0;
        int channelMask = 1;
        for(int i = 1; i < bitsPerChannel; i++) {
            channelMask <<= 1;
            channelMask++;
        }

        // determine the dimensions needed to fit every color
        double squareWidth = Math.sqrt(totalPixels);
        w = (int) squareWidth;
        h = (int) squareWidth;
        if(squareWidth - Math.round(squareWidth) > 0) {
            w = 1;
            while(w < squareWidth) w *= 2;
            h = totalPixels / w;
        }
        // set the size for Point to calculate adjacents
        Point.w = w;
        Point.h = h;

        System.out.println(bitsPerChannel + " bits per channel");
        System.out.println(maxValue + "^3 = " + totalPixels + " distinct colors");
        System.out.println(w + "x" + h + " px image");

        // generate all the colors
        ArrayList<RGB> colors = new ArrayList<>();
        int r = 0;
        int g = 0;
        int b = 0;
        for(int i = 0; i < totalPixels; i++) {
            colors.add(new RGB(r * 255 / channelMask, g * 255 / channelMask, b * 255 / channelMask));
            if(r++ == channelMask) {
                r = 0;
                if(g++ == channelMask) {
                    g = 0;
                    if(b++ == channelMask)
                        b = 0;
                }
            }
        }

        // algorithm to place each pixel starting from the center and expanding outward
        RGB[][] pixels = new RGB[w][h];
        ArrayList<Point> edge = new ArrayList<>();

        // seed points;
        for(int i = 0; i < 5; i++) {
            Point p = new Point((int)(Math.random() * w), (int)(Math.random() * h));
            pixels[p.x][p.y] = colors.remove((int) (Math.random() * colors.size()));
            edge.addAll(p.getAdjacent());
        }

        while(colors.size() > 0) {
            // pick a random color
            RGB c = colors.remove((int) (Math.random() * colors.size()));
            Point newPoint = edge.get(0);
            int minDifference = Integer.MAX_VALUE;

            if(algorithm == "min") {
                // find the edge point with the most similar neighbor
                for(Point edgePoint: edge) {
                    for(Point adjacent: edgePoint.getAdjacent()) {
                        RGB adjacentColor = pixels[adjacent.x][adjacent.y];
                        if(adjacentColor != null) {
                            int d = c.distanceTo(adjacentColor);
                            if(d < minDifference) {
                                minDifference = d;
                                newPoint = edgePoint;
                            }
                        }
                    }
                }
            } else if(algorithm == "avg") {
                // find the edge point with the most similar average neighbors
                for(Point edgePoint: edge) {
                    int avgDifference = 0;
                    int adjacentPoints = 0;
                    for(Point adjacent: edgePoint.getAdjacent()) {
                        RGB adjacentColor = pixels[adjacent.x][adjacent.y];
                        if(adjacentColor != null) {
                            int d = c.distanceTo(adjacentColor);
                            avgDifference += d;
                            adjacentPoints++;
                        }
                    }
                    avgDifference /= adjacentPoints;
                    if(avgDifference < minDifference) {
                        minDifference = avgDifference;
                        newPoint = edgePoint;
                    }
                }
            }

            // set the pixel
            pixels[newPoint.x][newPoint.y] = c;

            // update the edge list
            edge.remove(newPoint);
            for(Point adjacent: newPoint.getAdjacent()) {
                boolean contains = false;
                for(Point edgePoint: edge)
                    if(adjacent.equals(edgePoint)) contains = true;
                if(!contains && pixels[adjacent.x][adjacent.y] == null)
                    edge.add(adjacent);
            }

            if(nextSnap-- == 0) {
                nextSnap = totalPixels / snapshots;
                takeSnapshot(pixels, snapshot++);
            }
        }
        takeSnapshot(pixels, snapshot);
    }

    private static void takeSnapshot(RGB[][] pixels, int n) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < w; x++) {
            for(int y = 0; y < h; y++) {
                RGB c = pixels[x][y];
                img.setRGB(x, y, c == null ? 0 : c.toInt());
            }
        }

        String filename = "RGB" + String.format("%04d", n) + ".png";
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
            ImageIO.write(img, "png", out);
            System.out.println("Writing " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
