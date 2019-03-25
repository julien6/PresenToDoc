import org.w3c.dom.Element;

import java.util.List;

public class PNodeText extends PNode {

    String fontName;
    double fontSize;
    List<PNodeToken> textualContent;
    private ElementType elementType;

    public PNodeText(double x, double y, String id, Element node,
                     String fontName, double fontSize, List<PNodeToken> textualContent) {
        super(x, y, id, node);
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.textualContent = textualContent;
    }

    public String getFontName() {
        return fontName;
    }

    public double getFontSize() {
        return fontSize;
    }

    public List<PNodeToken> getTextualContent() {
        return textualContent;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        textualContent.forEach(pNodeToken -> str.append(pNodeToken.getContent() + " "));
        return str.toString();
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setTextualContent(List<PNodeToken> textualContent) {
        this.textualContent = textualContent;
    }

}
