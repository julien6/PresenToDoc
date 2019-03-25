import java.util.*;

public class ArchitectureFactory {

    public final static ArchitectureFactory INSTANCE = new ArchitectureFactory();
    private Map<Integer, List<PNode>> extractedNodes;
    private PDocument pDocument;
    private int nbPages, nbLevel;
    private final static int DEFAULT_VALUE = 500;

    private Map<Integer, Double> deltas;
    private HashMap<Double, Integer> sizeToLevelMapping;
    private List<String> beginCaracters = Collections.unmodifiableList(
            Arrays.asList("\u2022", "\u2023", "\u25E6", "\u2043", "\u204C", "\u204D", "\u2219", "\u00A7", "*", "-"));

    public ArchitectureFactory create(PDocument document, int nbLevel) {
        this.pDocument = document;
        extractedNodes = document.getExtractedNodes();
        nbPages = document.getNbPages();
        sizeToLevelMapping = (HashMap<Double, Integer>) computeArchitecture(nbLevel);
        deltas = computeDeltas();
        this.nbLevel = nbLevel;
        return this;
    }

    private Map<Double, Integer> computeArchitecture(int nbLevel) {

        ArrayList<Double> textSizes = new ArrayList<>();
        HashMap<Double, Integer> inf = new HashMap<>();

        for (int i = 0; i < nbPages; i++) {

            for (PNode pNode : extractedNodes.get(i)) {

                if (pNode instanceof PNodeText) {
                    Double textSize = ((PNodeText) pNode).fontSize;

                    if (!textSizes.contains(textSize))
                        textSizes.add(textSize);
                }
            }
        }

        Collections.sort(textSizes);
        Collections.reverse(textSizes);

        for (int i = 0; i < textSizes.size(); i++) {
            if (i < nbLevel) {
                inf.put(textSizes.get(i), i);
            } else {
                inf.put(textSizes.get(i), nbLevel);
            }
        }

        return inf;
    }

    private Map<Integer, Double> computeDeltas() {

        Map<Integer, Double> _deltas = new HashMap<>();

        for (int i = 0; i < nbPages; i++) {

            ArrayList<PNode> extractedNodeList = (ArrayList<PNode>) extractedNodes.get(i);
            double referenceX = 0;
            boolean firstItem = true;
            double deltaPerPage = DEFAULT_VALUE;

            //pour chaque texte de la i-ème page
            for (PNode pNode : extractedNodeList) {

                if (pNode instanceof PNodeText) {

                    PNodeText pNodeText = (PNodeText) pNode;

                    //si c'est un texte de liste ou élément de liste
                    if (beginCaracters.contains(pNodeText.textualContent.get(0).getContent())) {

                        //on prend le 1er item comme référence
                        if (firstItem) {
                            referenceX = pNodeText.getX();
                            firstItem = false;
                        }

                        //si on voit un décalage par rapport à l'item précèdent, enregistrer le plus petit
                        if (Math.abs(pNodeText.getX() - referenceX) > 0.) {
                            deltaPerPage = Math.min(deltaPerPage, Math.abs(pNodeText.getX() - referenceX));
                        }

                    }
                }
            }
            _deltas.put(i, deltaPerPage == DEFAULT_VALUE ? 0. : deltaPerPage);
        }
        return _deltas;
    }

    public ArchitectureFactory formating() {

        boolean firstRootText = true;

        for (int i = 0; i < nbPages; i++) {

            ArrayList<PNode> extractedNodeList = (ArrayList<PNode>) extractedNodes.get(i);

            double referenceX = 0;
            boolean firstItem = true;

            //pour chaque texte de la i-ème page
            for (int j = 0; j < extractedNodeList.size(); j++) {

                if (extractedNodeList.get(j) instanceof PNodeText) {

                    PNodeText pNodeText = (PNodeText) extractedNodeList.get(j);

                    //on prend le 1er élément de niveau 0 comme titre et les autres en sous titres 1
                    if ((sizeToLevelMapping.get(pNodeText.fontSize) == 0) && firstRootText) {
                        pNodeText.setHierachyLevel(0);
                        firstRootText = false;
                    } else if (sizeToLevelMapping.get(pNodeText.fontSize) == 0) {
                        pNodeText.setHierachyLevel(1);
                    } else {
                        //associe chaque texte à une hierarchie en fonction de la taille du texte
                        pNodeText.setHierachyLevel(sizeToLevelMapping.get(pNodeText.fontSize));
                    }

                    //si c'est un texte
//                    if ((pNodeText.getHierachyLevel() == nbLevel)) {

                    //marquer comme texte simple
                    pNodeText.setElementType(ElementType.SIMPLE_TEXT);

                    //si c'est un texte de liste ou élément de liste
                    if (beginCaracters.contains(pNodeText.textualContent.get(0).getContent())) {

                        pNodeText.getTextualContent().remove(0);

                        //marquer comme élément de liste
                        pNodeText.setElementType(ElementType.LIST_ELEMENT);

                        pNodeText.setHierachyLevel(nbLevel + 1);

                        //on prend le 1er item comme référence
                        if (firstItem) {
                            referenceX = pNodeText.getX();
                            firstItem = false;
                        }

                        //si on voit un décalage par rapport à l'item précèdent, determiner le niveau hierarchique
                        if (Math.abs(pNodeText.getX() - referenceX) > 0.) {
                            extractedNodeList.get(j)
                                    .setHierachyLevel((int) ((extractedNodeList.get(j).getX() - referenceX) / deltas.get(i)) + nbLevel + 1);
                        }
                    }
//                    }
                }
            }
            pDocument.getExtractedNodes().put(i, new ArrayList<>(extractedNodeList));
            extractedNodeList.clear();
        }

        pDocument.setExtractedNodes(textFusion(pDocument.getExtractedNodes(), 24.));

        return this;
    }

    private Map<Integer, List<PNode>> textFusion(Map<Integer, List<PNode>> nodeSet, Double maxInterlineSpace) {

        for (int i = 0; i < nbPages; i++) {

            List<PNode> pNodeList = nodeSet.get(i);

            for (int j = 1; j < pNodeList.size(); j++) {

                if (pNodeList.get(j) instanceof PNodeText) {

                    PNodeText pNodeText = (PNodeText) pNodeList.get(j);
                    PNodeText oNodeText = (PNodeText) pNodeList.get(j - 1);

                    if (Math.abs(pNodeText.getY() - oNodeText.getY()) < maxInterlineSpace) {

                        //on concatène le texte du noeud ancien dans le début du texte du noeud nouveau
                        //on lui donne les attributs spatiaux de l'ancien noeud
                        List<PNodeToken> tmp_list = oNodeText.getTextualContent();
                        tmp_list.addAll(pNodeText.getTextualContent());
                        ((PNodeText) pNodeList.get(j)).setTextualContent(tmp_list);
                        pNodeList.get(j).setX(oNodeText.getX());
                        pNodeList.get(j).setY(oNodeText.getY());

                        //on supprime l'ancien noeud
                        pNodeList.remove(j - 1);
                    }
                }
            }
            nodeSet.put(i, new ArrayList<>(pNodeList));
            pNodeList.clear();
        }
        return nodeSet;
    }

    public PDocument build() {
        return this.pDocument;
    }

}
