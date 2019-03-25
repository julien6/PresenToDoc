import org.w3c.dom.*;

public abstract class PNode {

    private double x;
    private double y;
    private String id;
    private Node node;
    private int hierachyLevel = 0;

    public Node getNode() {
        return node;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    public PNode(double x, double y, String id, Element node) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.node = node;
    }

    public boolean isClose(PNode pNode, double delta) {
        return (x <= (pNode.x + delta) && x >= (pNode.x - delta))
                && (y <= (pNode.y + delta) && y >= (pNode.y - delta));
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public abstract String toString();

    public int getHierachyLevel() {
        return hierachyLevel;
    }

    public void setHierachyLevel(int hierachyLevel) {
        this.hierachyLevel = hierachyLevel;
    }

}
