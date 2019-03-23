import java.util.*;

class DuplicateFactory {

    final static DuplicateFactory INSTANCE = new DuplicateFactory();
    private Map<Integer, List<PNode>> extractedNodes;
    private PDocument pDocument;
    private int nbPages;

    DuplicateFactory create(PDocument document) {
        this.pDocument = document;
        extractedNodes = document.getExtractedNodes();
        nbPages = document.getNbPages();
        return this;
    }

    private int computeNbNode(PNode node) {

        int number = 0;

        for (int i = 0; i < nbPages; i++) {

            List<PNode> nodeList = extractedNodes.get(i);

            for (PNode currentNode : nodeList) {
                if (currentNode.isClose(node, 5)) {
                    number++;
                    break;
                }
            }
        }
        return number;
    }

    private Map<Integer, List<PNode>> computeDuplicatedNodes(int minNb) {

        Map<Integer, List<PNode>> duplicatedNodesMap = new HashMap<>();

        for (int i = 0; i < nbPages; i++) {

            List<PNode> extractedNodesList = extractedNodes.get(i);
            List<PNode> duplicatedNodeList = new ArrayList<>();

            for (PNode currentNode : extractedNodesList) {
                if (computeNbNode(currentNode) > minNb) {
                    duplicatedNodeList.add(currentNode);
                }
            }
            duplicatedNodesMap.put(i, duplicatedNodeList);
        }

        return duplicatedNodesMap;
    }

    DuplicateFactory removeIdenticalNodes() {

        Map<Integer, List<PNode>> duplicatedNodesMap = computeDuplicatedNodes((int) Math.round(0.8 * nbPages));

        //for each page
        for (int i = 0; i < nbPages; i++) {

            ArrayList<PNode> pNodeArrayList = (ArrayList<PNode>) duplicatedNodesMap.get(i);

            for (PNode nodeToRemove : pNodeArrayList) {
                extractedNodes.get(i).remove(nodeToRemove);
            }

        }
        return this;
    }

    PDocument build() {
        return this.pDocument;
    }

}
