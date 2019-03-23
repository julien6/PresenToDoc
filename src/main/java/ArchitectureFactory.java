import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

public class ArchitectureFactory {

    public final static ArchitectureFactory INSTANCE = new ArchitectureFactory();
    private Map<Integer, List<PNode>> extractedNodes;
    private Document document;
    private PDocument pDocument;
    private int nbPages;

    public ArchitectureFactory create(PDocument document) {
        this.pDocument = document;
        this.document = document.getDocument();
        extractedNodes = document.getExtractedNodes();
        nbPages = document.getNbPages();
        return this;
    }

    public Map<Integer, Integer> computeArchitecture() {

        Map<Integer, Integer> architectureMapping = new HashMap<>();

        List<Double> textSizes = new ArrayList<>();

        for (int i = 0; i < nbPages; i++) {

            for (PNode pNode : extractedNodes.get(i)) {
                if (pNode instanceof PNodeText) {
                    if (!textSizes.contains(((PNodeText) pNode).fontSize))
                        textSizes.add(((PNodeText) pNode).fontSize);
                }
            }
        }

        textSizes.forEach(System.out::println);

        return architectureMapping;
    }

    public PDocument build() {
        return this.pDocument;
    }

}
