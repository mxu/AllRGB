import java.util.ArrayList;

/**
 * Created by Mike on 3/4/14.
 */
public class Point {

    public int x;
    public int y;
    public static int w;
    public static int h;
    private ArrayList<Point> adjacent;

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ArrayList<Point> getAdjacent() {
        if(adjacent == null) {
            adjacent = new ArrayList<>();
            for(int dx = -1; dx <= 1; dx++)
                for(int dy = -1; dy <= 1; dy++)
                    if(x + dx > -1 && x + dx < w)
                        if(y + dy > -1 && y + dy < h)
                            if(dx != 0 || dy != 0)
                                adjacent.add(new Point(x + dx, y + dy));
        }
        return adjacent;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Point that) {
        return this.x == that.x && this.y == that.y;
    }

}
