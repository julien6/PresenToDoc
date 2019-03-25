import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;


public class PDocument {

    private Document document;
    private final static List<String> NODES = Collections.unmodifiableList(Arrays.asList("TEXT", "IMAGE"));
    private Map<Integer, List<PNode>> extractedNodes;
    private int nbPages;

    PDocument(String documentName) {
        File inputFile = new File(documentName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            this.document = doc;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        nbPages = this.computeNbPages();
        extractedNodes = initializeNodeSet();

    }

    public void setExtractedNodes(Map<Integer, List<PNode>> extractedNodes) {
        this.extractedNodes = extractedNodes;
    }

    Map<Integer, List<PNode>> initializeNodeSet() {
        Map<Integer, List<PNode>> nodeSet = new HashMap<>();

        NodeList pagesTag = document.getElementsByTagName("PAGE");

        //for each page
        for (int i = 0; i < pagesTag.getLength(); i++) {

            //getting the i-th pageTag
            Element pageTag = (Element) pagesTag.item(i);

            ArrayList<PNode> nodeArrayList = new ArrayList<>();

            for (String node : NODES) {
                //getting all nodes in the current page
                NodeList nodeListTag = pageTag.getElementsByTagName(node);

                //for each node in the pageTag
                for (int k = 0; k < nodeListTag.getLength(); k++) {

                    Element currentNode = (Element) nodeListTag.item(k);

                    double x = Double.valueOf(currentNode.getAttribute("x"));
                    double y = Double.valueOf(currentNode.getAttribute("y"));
                    String id = currentNode.getAttribute("id");

                    PNode pNode = null;

                    if (node.equals("TEXT")) {

                        Element tokenEx = (Element) currentNode.getElementsByTagName("TOKEN").item(0);
                        String fontName = tokenEx.getAttribute("font-name");
                        double fontSize = Double.valueOf(tokenEx.getAttribute("font-size"));

                        NodeList tokenList = currentNode.getElementsByTagName("TOKEN");
                        List<PNodeToken> textualContent = new ArrayList<>();

                        for (int j = 0; j < tokenList.getLength(); j++) {
                            Element tokenTag = (Element) tokenList.item(j);
                            boolean bold = tokenTag.getAttribute("bold").equals("yes");
                            boolean italic = tokenTag.getAttribute("italic").equals("yes");
                            String fontColor = tokenTag.getAttribute("font-color");
                            String content = tokenTag.getTextContent();

                            textualContent.add(new PNodeToken(bold, italic, fontColor, content));
                        }

                        pNode = new PNodeText(x, y, id, currentNode, fontName, fontSize, textualContent);

                    } else if (node.equals("IMAGE")) {
                        double height = Double.valueOf(currentNode.getAttribute("height"));
                        double width = Double.valueOf(currentNode.getAttribute("width"));
                        String href = currentNode.getAttribute("href");

                        pNode = new PNodeImage(x, y, id, currentNode,
                                height, width, href);
                    }

                    nodeArrayList.add(pNode);
                }
            }
            nodeSet.put(i, nodeArrayList);
        }

        return nodeSet;
    }

    public Document getDocument() {
        return document;
    }

    private int computeNbPages() {
        NodeList pagesTag = document.getElementsByTagName("PAGE");
        return pagesTag.getLength();
    }

    int getNbPages() {
        return nbPages;
    }

    public String toString() {
        NodeList pagesTag = document.getElementsByTagName("PAGE");

        StringBuilder str = new StringBuilder();

        //for each page
        for (int i = 0; i < pagesTag.getLength(); i++) {

            ArrayList<PNode> pNodeArrayList = (ArrayList<PNode>) extractedNodes.get(i);

            for (PNode pNode : pNodeArrayList) {
                str.append(pNode.toString());
                str.append("\n");
            }

            str.append("\n----------\n");
        }

        return str.toString();
    }

    public void save(String fileName) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            Result output = new StreamResult(new File(fileName));
            Source input = new DOMSource(document);

            transformer.transform(input, output);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, List<PNode>> getExtractedNodes() {
        return extractedNodes;
    }
}
