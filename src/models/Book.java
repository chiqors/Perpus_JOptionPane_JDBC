package models;

import java.util.ArrayList;

public class Book {
    private int id;
    private String name;
    private String author;
    private String published;
    private int stock;

    public Book(int id, String name, String author, String published, int stock) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published = published;
        this.stock = stock;
    }

    public Book(int id, String name, String author, String published) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published = published;
    }

    @Override
    public String toString() {
        return name + " - " + author + " - " + published + " - " + stock;
    }

    public static ArrayList<Book> parseJSON(String borrowed_books) {
        ArrayList<Book> books = new ArrayList<>();
        String[] booksString = borrowed_books.split("},");
        for (String bookString : booksString) {
            String[] bookStringSplit = bookString.split(",");
            int id = Integer.parseInt(bookStringSplit[0].split(":")[1]);
            String name = bookStringSplit[1].split(":")[1].replace("\"", "");
            String author = bookStringSplit[2].split(":")[1].replace("\"", "");
            String published = bookStringSplit[3].split(":")[1].replace("\"", "");
            books.add(new Book(id, name, author, published));
        }
        return books;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int i) {
        this.stock = i;
    }
}
