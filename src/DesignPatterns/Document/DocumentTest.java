package DesignPatterns.Document;

import java.util.*;

interface Document{
    String render();
}
class SimpleDocument implements Document{
    String text;
    public SimpleDocument(String text){
        this.text = text;
    }

    public String render() {
        return text;
    }
}
abstract class DocumentDecorator implements Document{
    protected Document inner;
    public DocumentDecorator(Document decorated){
        this.inner = decorated;
    }
}
class NumberedDocument extends DocumentDecorator{
    public NumberedDocument(Document decorated){
        super(decorated);
    }
    public String render() {
        String [] split = inner.render().split("\n");
        StringBuilder sb = new StringBuilder();
        int i=1;
        for(String s : split){
            sb.append(i).append(": ").append(s).append("\n");
            i++;
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
class CountDecorator extends DocumentDecorator{

    public CountDecorator(Document decorated){
        super(decorated);
    }

    @Override
    public String render() {
        return inner.render() +"\nWords: " + inner.render().split("\\s+").length;
    }
}
class RedactedDecorator extends DocumentDecorator{
    List<String> forbidden;
    public RedactedDecorator(Document decorated, List<String> forbidden){
        super(decorated);
        this.forbidden = forbidden;
    }

    @Override
    public String render() {
        String text = inner.render();
        for(String f : forbidden) {
            text=text.replaceAll(f,"*" );
        }
        return text;
    }
}

class DocumentViewer{
    Map<String, Document> documents;
    public DocumentViewer(){
        documents = new HashMap<>();
    }
    void addDocument(String id, String text){
        documents.put(id, new SimpleDocument(text));
    }
    void enableLineNumbers(String id){
        documents.compute(id, (k, d) -> new NumberedDocument(d));
    }
    void enableWordCount(String id){
        documents.compute(id, (k, d) -> new CountDecorator(d));
    }
    void enableRedaction(String id, List<String> forbiddenWords){
        documents.compute(id, (k, d) -> new RedactedDecorator(d, forbiddenWords));
    }
    void display(String id){
        System.out.println("=== Document "+id+" ===");
        System.out.println(documents.get(id).render());
    }
}

public class DocumentTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DocumentViewer viewer = new DocumentViewer();

        int numDocs = Integer.parseInt(sc.nextLine().trim());

        for (int i = 0; i < numDocs; i++) {
            String id = sc.nextLine().trim();
            int lines = Integer.parseInt(sc.nextLine().trim());

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                sb.append(sc.nextLine());
                if (j < lines - 1) sb.append("\n");
            }

            viewer.addDocument(id, sb.toString());
        }

        while (sc.hasNext()) {
            String cmd = sc.next();

            if (cmd.equals("exit")) {
                break;
            } else if (cmd.equals("enableLineNumbers")) {
                String id = sc.next();
                viewer.enableLineNumbers(id);
            } else if (cmd.equals("enableWordCount")) {
                String id = sc.next();
                viewer.enableWordCount(id);
            } else if (cmd.equals("enableRedaction")) {
                String id = sc.next();
                String restOfLine = sc.nextLine().trim();
                List<String> forbidden = new ArrayList<>();
                if (!restOfLine.isEmpty()) {
                    forbidden.addAll(Arrays.asList(restOfLine.split("\\s+")));
                }
                viewer.enableRedaction(id, forbidden);
            } else if (cmd.equals("display")) {
                String id = sc.next();
                viewer.display(id);
            } else {
                sc.nextLine();
            }
        }
    }
}
