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

public class Delete_Book {
    private LoadingDialog loadingDialog;

    public Delete_Book() {
        loadingDialog = new LoadingDialog();
        List<Book> bookList = loadData();
        boolean continueDeleting = true; // Flag to control the loop

        while (continueDeleting) {
            String title = "Hapus Buku\n\n";
            String content = "List Buku\n\n";

            // display list of books
            for (int i = 0; i < bookList.size(); i++) {
                content += bookList.get(i).getId() + ". " + bookList.get(i).getName() + "\n";
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

                loadingDialog.showLoading();
                doDeleteBookById(bookId);
                loadingDialog.hideLoading();
            } else if (method.equals("2")) {
                // Delete by name
                String askName = "Masukkan nama buku yang ingin dihapus";
                String bookName = JOptionPane.showInputDialog(null, title + content + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

                // if cancel button is clicked, then return to the beginning of the loop
                if (bookName == null) {
                    continue;
                }

                loadingDialog.showLoading();
                doDeleteBookByName(bookName);
                loadingDialog.hideLoading();
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
        loadingDialog.showLoading(); // Show the loading dialog

        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM books";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String published = resultSet.getString("published");
                int stock = resultSet.getInt("stock");
                bookList.add(new Book(id, name, author, published, stock));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadingDialog.hideLoading(); // Hide the loading dialog after data has been loaded
        return bookList;
    }

    public void doDeleteBookById(int bookId) {
        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doDeleteBookByName(String bookName) {
        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String sql = "DELETE FROM books WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
