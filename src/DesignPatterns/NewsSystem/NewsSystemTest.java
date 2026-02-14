package DesignPatterns.NewsSystem;

import java.time.LocalDateTime;
import java.util.*;

interface Subscriber{
    void update(Article article);
}
interface Publisher{
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notifySubscribers(Article article);
}

class Article {

    private final String category;
    private final String author;
    private final String content;
    private final LocalDateTime timestamp;

    public Article(String category, String author, String content, LocalDateTime timestamp) {
        this.category = category;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp).append("] ").append(author).append(" - ").append(category).append('\n');
        sb.append(content);
        return sb.toString();
    }
}

class Category implements Publisher{
    private String category;
    private Set<Subscriber> subscribers;

    public Category(String category) {
        this.category = category;
        this.subscribers = new HashSet<>();
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(Article article) {
        subscribers.forEach(sub -> sub.update(article));
    }
}

class Author implements Publisher{
    private String name;
    private Set<Subscriber> subscribers;

    public Author(String name) {
        this.name = name;
        this.subscribers = new HashSet<>();
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(Article article) {
        subscribers.forEach(sub -> sub.update(article));
    }
}

class User implements Subscriber{
    private String username;
    private Set<Article> articles;

    public User(String username) {
        this.username = username;
        this.articles = new HashSet<>();
    }
    @Override
    public void update(Article article) {
        articles.add(article);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("News for user: ").append(username).append("\n");
        articles.stream()
                .sorted(Comparator.comparing(Article::getTimestamp))
                .forEach(article -> sb.append(article).append("\n"));
        return sb.toString();
    }
}

class NewsSystem{
    private Map<String, Category> categories;
    private Map<String, Author> authors;
    private Map<String, User> users;
    public NewsSystem(List<String> categoryNames, List<String> authorNames){
        this.categories = new HashMap<>();
        this.authors = new HashMap<>();
        users = new HashMap<>();
        categoryNames.forEach(category -> this.categories.put(category, new Category(category)));
        authorNames.forEach(author -> this.authors.put(author, new Author(author)));
    }

    public void addUser(String username){
        users.putIfAbsent(username, new User(username));
    }

    public void subscribeUserToCategory(String username, String categoryName){
        categories.get(categoryName).subscribe(users.get(username));
    }
    public void unsubscribeUserFromCategory(String username, String categoryName){
        categories.get(categoryName).unsubscribe(users.get(username));
    }
    public void subscribeUserToAuthor(String username, String authorName){
        authors.get(authorName).subscribe(users.get(username));
    }
    public void unsubscribeUserFromAuthor(String username, String authorName){
        authors.get(authorName).unsubscribe(users.get(username));
    }

    public void publishArticle(Article article){
        authors.get(article.getAuthor()).notifySubscribers(article);
        categories.get(article.getCategory()).notifySubscribers(article);
    }
    public void printNewsForUser(String username){
        System.out.print(users.get(username));
    }
}

public class NewsSystemTest {

    public static void main(String[] args) {

        // Hardcoded categories and authors
        List<String> categories = List.of(
                "Technology", "Sports", "Politics", "Health", "Science",
                "Business", "Education", "Culture", "Travel", "Entertainment"
        );

        List<String> authors = List.of(
                "MartinFowler", "JohnDoe", "AliceSmith", "BobBrown", "JaneMiller"
        );

        NewsSystem system = new NewsSystem(categories, authors);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0];

            switch (command) {

                case "ADD_USER":
                    system.addUser(parts[1]);
                    break;

                case "SUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToCategory(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromCategory(p[0], p[1]);
                    break;
                }

                case "SUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToAuthor(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromAuthor(p[0], p[1]);
                    break;
                }

                case "PUBLISH": {
                    // format:
                    // PUBLISH <category> <author> <timestamp> <content>
                    String[] p = parts[1].split("\\s+", 4);
                    Article article = new Article(
                            p[0],
                            p[1],
                            p[3],
                            LocalDateTime.parse(p[2])
                    );
                    system.publishArticle(article);
                    break;
                }

                case "PRINT":
                    system.printNewsForUser(parts[1]);
                    break;
            }
        }
    }
}




