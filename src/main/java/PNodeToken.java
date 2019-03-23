import org.w3c.dom.Element;

public class PNodeToken {

    private boolean bold, italic;
    private String fontColor, content;

    public PNodeToken(boolean bold, boolean italic, String fontColor, String content) {
        this.bold = bold;
        this.italic = italic;
        this.fontColor = fontColor;
        this.content = content;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getContent() {
        return content;
    }
}
