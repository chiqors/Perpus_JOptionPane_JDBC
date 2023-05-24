package models;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Book {
    private int id;
    private String name;
    private String author;
    private String published;
    private int stock;
    private int qty;
    @Override
    public String toString() {
        String str = "";
        str += "ID: " + id + "\n";
        str += "Name: " + name + "\n";
        return str;
    }

    public String menuListBooks() {
        String str = "";
        String year = getBookYear();
        str += id + ". " + name + " (" + year + ")\n";
        str += " " + " " + " " + " Oleh: " + author + "\n";
        return str;
    }

    public String getBookYear() {
        // convert published to Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(published);
            sdf = new SimpleDateFormat("yyyy");
            return sdf.format(date);
        } catch (ParseException e) {
            return "Tidak diketahui";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
