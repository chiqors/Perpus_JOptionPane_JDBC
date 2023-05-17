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

public class List_Books {
    private final LoadingDialog loadingDialog;

    public List_Books() {
        loadingDialog = new LoadingDialog();
        loadData();
    }

    private void loadData() {
        loadingDialog.showLoading();

        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String query = "SELECT * FROM books";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String author = resultSet.getString("author");
                        String published = resultSet.getString("published");
                        int stock = resultSet.getInt("stock");

                        Book book = new Book(id, name, author, published, stock);
                        bookList.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            loadingDialog.hideLoading();
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadingDialog.hideLoading();
        displayBookList(bookList);
    }

    private void displayBookList(List<Book> bookList) {
        int choice = 0;

        do {
            String title = "Daftar Buku\n\n";
            String bookData = "";
            for (int i = 0; i < bookList.size(); i++) {
                bookData += (i + 1) + ". " + bookList.get(i) + "\n";
            }
            String menu = JOptionPane.showInputDialog(null, title + bookData + "\n0. Kembali", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);

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
