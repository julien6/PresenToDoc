import org.w3c.dom.Element;

public class PNodeImage extends PNode {

    private double height, width;
    private String href;

    public PNodeImage(double x, double y, String id, Element node,
                      double height, double width, String href) {
        super(x, y, id, node);
        this.height = height;
        this.width = width;
        this.href = href;
    }

    @Override
    public String toString() {
        return this.href;
    }
}
