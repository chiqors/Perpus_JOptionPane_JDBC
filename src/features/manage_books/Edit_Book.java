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

public class Edit_Book {
    public Edit_Book() {
        // show loading state
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.showLoading();

        // load list of books from the database
        List<Book> bookList = loadData();

        // hide loading state
        loadingDialog.hideLoading();

        if (bookList == null) {
            // Handle the case where loading failed
            JOptionPane.showMessageDialog(null, "Failed to load the books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            new Book_Management_Menu();
            return;
        }

        boolean continueEditing = true; // Flag to control the loop

        while (continueEditing) {
            String title = "Ubah Buku\n\n";
            String content = "List Buku\n\n";

            // display list of books
            for (int i = 0; i < bookList.size(); i++) {
                content += bookList.get(i).getId() + ". " + bookList.get(i).getName() + "\n";
            }
            content += "\n";

            String askId = "Masukkan ID buku yang ingin diubah";
            String askName = "Masukkan nama buku";

            String id = JOptionPane.showInputDialog(null, title + content + askId, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Book Management Menu
            if (id == null) {
                continueEditing = false;
                break;
            }

            // search book by Id
            int bookIndex = -1;
            try {
                bookIndex = Integer.parseInt(id) - 1;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Masukkan ID yang valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (bookIndex < 0 || bookIndex >= bookList.size()) {
                JOptionPane.showMessageDialog(null, "ID buku tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // now bookList.get(bookIndex) gives you the book with the specified ID
            String bookName = JOptionPane.showInputDialog(null, title + content + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to beginning of the loop
            if (bookName == null) {
                continue;
            }

            // show loading state
            loadingDialog.showLoading();

            // edit book
            doEditBook(bookName, bookList.get(bookIndex).getId());

            // hide loading state
            loadingDialog.hideLoading();

            // ask if the user wants to continue editing
            int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin mengubah buku lain?", Constant.APP_NAME, JOptionPane.YES_NO_OPTION);
            continueEditing = (choice == JOptionPane.YES_OPTION);
        }

        // if the user doesn't want to continue editing, then return to Book Management Menu
        new Book_Management_Menu();
    }

    private List<Book> loadData() {
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DB_Connection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM books");
             ResultSet resultSet = statement.executeQuery()) {

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
            return null;
        }

        return bookList;
    }

    public void doEditBook(String bookName, int bookId) {
        try (Connection connection = DB_Connection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE books SET name = ? WHERE id = ?")) {

            statement.setString(1, bookName);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to edit the book.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
