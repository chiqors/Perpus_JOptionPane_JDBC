package features.manage_books;

import config.Constant;
import models.Book;
import utils.DB_Connection;
import utils.LoadingDialog;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Search_Book {
    private final LoadingDialog loadingDialog;

    public Search_Book() {
        loadingDialog = new LoadingDialog();
        String bookName = JOptionPane.showInputDialog(null, "Masukkan nama buku yang ingin dicari:", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

        if (bookName != null) {
            loadingDialog.showLoading();
            searchBookByName(bookName);
        } else {
            new Book_Management_Menu();
        }
    }

    private void searchBookByName(String bookName) {
        List<Book> searchResults = new ArrayList<>();

        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String query = "SELECT * FROM books WHERE LOWER(name) LIKE LOWER(?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + bookName + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String author = resultSet.getString("author");
                        String published = resultSet.getString("published");
                        int stock = resultSet.getInt("stock");

                        Book book = new Book(id, name, author, published, stock);
                        searchResults.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            loadingDialog.hideLoading();
            JOptionPane.showMessageDialog(null, "Failed to search for books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadingDialog.hideLoading();
        displaySearchResults(searchResults);
    }

    private void displaySearchResults(List<Book> searchResults) {
        StringBuilder bookData = new StringBuilder();

        for (Book book : searchResults) {
            bookData.append("ID: ").append(book.getId()).append("\n");
            bookData.append("Nama Buku: ").append(book.getName()).append("\n");
            bookData.append("Penulis: ").append(book.getAuthor()).append("\n");
            bookData.append("Tahun Terbit: ").append(book.getPublished()).append("\n");
            bookData.append("Stok: ").append(book.getStock()).append("\n");
        }

        int choice = 0;

        do {
            String title = "Cari Buku\n\n";
            String menu = JOptionPane.showInputDialog(null, title + bookData + "\n0. Kembali", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            if (menu == null) {
                choice = 0;
                break;
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);

        if (choice == 0) {
            new Book_Management_Menu();
        }
    }
}
