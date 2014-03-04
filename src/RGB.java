/**
 * Created by Mike on 3/4/14.
 */
public class RGB {

    public int R;
    public int G;
    public int B;

    public RGB(int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public int distanceTo(RGB that) {
        return Math.abs(this.R - that.R) + Math.abs(this.G - that.G) + Math.abs(this.B - that.B);
    }

    public int toInt() {
        return (this.R << 8 | this.G) << 8 | this.B;
    }

}
