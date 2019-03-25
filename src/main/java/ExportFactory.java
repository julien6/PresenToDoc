import java.util.List;
import java.util.Map;

public class ExportFactory {

    PDocument pDocument;
    final static ExportFactory INSTANCE = new ExportFactory();

    public ExportFactory create(PDocument pDocument) {
        this.pDocument = pDocument;
        return this;
    }

    public String convertToLatex(String fileName) {

        int nbPage = pDocument.getNbPages();
        Map<Integer, List<PNode>> extractedNodeList = pDocument.getExtractedNodes();

        StringBuilder str = new StringBuilder();

        StringBuilder currentWord = new StringBuilder();
        String sentence;

        str.append("\\documentclass{article}\n" +
                "\n" +
                "\\usepackage[french]{babel}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage[a4paper,left=4cm, right=4cm, top=3.5cm, bottom=3.5cm]{geometry}\n" +
                "\n");

        for (int i = 0; i < nbPage; i++) {
            List<PNode> nodeListPerPage = extractedNodeList.get(i);
            PNodeText olderNode = null;

            for (PNode pNode : nodeListPerPage) {

                if (pNode instanceof PNodeText) {

                    currentWord.setLength(0);
                    sentence = "";

                    //on concatène les mots avec les bons formatages pour chacun
                    for (int k = 0; k < ((PNodeText) pNode).textualContent.size(); k++) {

                        PNodeToken pNodeToken = ((PNodeText) pNode).textualContent.get(k);

                        if (pNodeToken.isItalic()) {
                            currentWord.append("\\textit{");
                        }
                        if (pNodeToken.isBold()) {
                            currentWord.append("\\textbf{");
                        }
                        currentWord.append(pNodeToken.getContent());
                        if (pNodeToken.isBold() && pNodeToken.isItalic()) {
                            currentWord.append("}}");
                        }
                        if (pNodeToken.isBold() ^ pNodeToken.isItalic()) {
                            currentWord.append("}");
                        }
                        if (k != ((PNodeText) pNode).textualContent.size() - 1) {
                            currentWord.append(" ");
                        }
                        sentence = currentWord.toString();
                    }

                    if (olderNode != null) {
                        //si on quitte une liste
                        if ((pNode.getHierachyLevel() < olderNode.getHierachyLevel()) && (pNode.getHierachyLevel() > 2)) {
                            str.append("\\end{itemize}\n");
                        }
                    }

                    if (pNode.getHierachyLevel() == 0) {
                        str.append("\n\\title{").append(sentence).append("}\n" +
                                "\n" +
                                "\\begin{document}\n" +
                                "\n" +
                                "\\maketitle\n" +
                                "\n");
                    }

                    if (pNode.getHierachyLevel() == 1) {
                        str.append("\n\n\\section{").append(sentence).append("}\n");
                    }

                    if (pNode.getHierachyLevel() == 2) {
                        str.append("\n\n\\subsection{").append(sentence).append("}\n");
                    }

                    if (pNode.getHierachyLevel() == 3) {
                        //TODO : voir avec y pour distinguer des paragraphes
                        str.append("\n").append(sentence);
                    }

                    if (pNode.getHierachyLevel() > 3) {
                        //si on rentre dans une liste
                        if ((olderNode == null) || (olderNode.getHierachyLevel() < pNode.getHierachyLevel())) {
                            str.append("\\begin{itemize}\n");
                        }
                        str.append("\t\\item ").append(sentence).append("\n");
                    }

                    olderNode = (PNodeText) pNode;

                } else {

                    PNodeImage pNodeImage = (PNodeImage) pNode;

                    str.append("\n\\begin{figure}[htp]\n" +
                            "    \\centering\n" +
                            "    \\includegraphics[scale=0.5]{")
                            .append(pNodeImage)
                            .append("}\n" +
                                    "\\end{figure}\n" +
                                    "\n");
                }

            }

        }
        return str.toString();
    }

    public PDocument build() {
        return pDocument;
    }

}
