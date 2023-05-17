package features.manage_books;

import config.Constant;
import models.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Delete_Book {
    public Delete_Book() {
        List<Book> bookList = loadData();
        boolean continueDeleting = true; // Flag to control the loop

        while (continueDeleting) {
            String title = "Hapus Buku\n\n";
            String content = "List Buku\n\n";

            // display list of books
            for (int i = 0; i < bookList.size(); i++) {
                content += (i + 1) + ". " + bookList.get(i) + "\n";
            }
            content += "\n";

            String askIdOrName = "Pilih metode penghapusan:\n1. Menghapus berdasarkan ID\n2. Menghapus berdasarkan nama";
            String method = JOptionPane.showInputDialog(null, title + content + askIdOrName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Book Management Menu
            if (method == null) {
                continueDeleting = false;
                break;
            }

            if (method.equals("1")) {
                // Delete by ID
                String askId = "Masukkan ID buku yang ingin dihapus";
                String id = JOptionPane.showInputDialog(null, title + content + askId, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to the beginning of the loop
                if (id == null) {
                    continue;
                }

                int bookId;
                try {
                    bookId = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Masukkan ID yang valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // delete book by id
                doDeleteBookById(bookId);
            } else if (method.equals("2")) {
                // Delete by name
                String askName = "Masukkan nama buku yang ingin dihapus";
                String bookName = JOptionPane.showInputDialog(null, title + content + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to the beginning of the loop
                if (bookName == null) {
                    continue;
                }

                // delete book by name
                doDeleteBookByName(bookName);
            } else {
                JOptionPane.showMessageDialog(null, "Pilihan tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // ask if the user wants to continue deleting
            int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin menghapus buku lain?", Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
            continueDeleting = (choice == JOptionPane.YES_OPTION);
        }

        // return to Book Management Menu
        new Book_Management_Menu();
    }

    private List<Book> loadData() {
        List<Book> bookList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(Constant.BOOKS_FILE)) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (Object bookObj : bookArray) {
                JSONObject bookJson = (JSONObject) bookObj;
                int id = Integer.parseInt(bookJson.get("id").toString());
                String name = (String) bookJson.get("name");
                String author = (String) bookJson.get("author");
                String published = (String) bookJson.get("published");
                int stock = Integer.parseInt(bookJson.get("stock").toString());
                bookList.add(new Book(id, name, author, published, stock));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public void doDeleteBookById(int bookId) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(Constant.BOOKS_FILE)) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (int i = 0; i < bookArray.size(); i++) {
                JSONObject book = (JSONObject) bookArray.get(i);

                if (bookId == ((Long) book.get("id")).intValue()) {
                    bookArray.remove(i);
                    break;
                }
            }

            FileWriter file = new FileWriter(Constant.BOOKS_FILE);
            file.write(bookArray.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doDeleteBookByName(String bookName) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(Constant.BOOKS_FILE)) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (int i = 0; i < bookArray.size(); i++) {
                JSONObject book = (JSONObject) bookArray.get(i);

                if (bookName.equals(book.get("name"))) {
                    bookArray.remove(i);
                    break;
                }
            }

            FileWriter file = new FileWriter(Constant.BOOKS_FILE);
            file.write(bookArray.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}