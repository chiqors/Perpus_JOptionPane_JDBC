package features.manage_books;

import config.Constant;
import utils.DB_Connection;
import utils.LoadingDialog;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Add_Book {
    private final LoadingDialog loadingDialog;

    public Add_Book() {
        loadingDialog = new LoadingDialog();
        int choice;
        do {
            String title = "Tambah Buku\n\n";
            String askName = "Masukkan nama buku";
            String askAuthor = "Masukkan nama pengarang";
            String askPublished = "Masukkan tahun terbit";
            String askStock = "Masukkan jumlah stok";

            // get input from user
            String name = JOptionPane.showInputDialog(null, title + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (name == null) {
                choice = 1;
                break;
            }
            String author = JOptionPane.showInputDialog(null, title + askAuthor, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (author == null) {
                choice = 1;
                break;
            }
            String published = JOptionPane.showInputDialog(null, title + askPublished, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (published == null) {
                choice = 1;
                break;
            }
            String stock = JOptionPane.showInputDialog(null, title + askStock, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (stock == null) {
                choice = 1;
                break;
            }

            loadingDialog.showLoading();
            addBookToDatabase(name, author, published, Integer.parseInt(stock));
            loadingDialog.hideLoading();

            // ask user if they want to add another book
            String AddMore = JOptionPane.showConfirmDialog(null, title + "Tambah buku lagi?", Constant.APP_NAME, JOptionPane.YES_NO_OPTION) + "";
            choice = Integer.parseInt(AddMore);

            switch (choice) {
                case 0:
                case 1:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia! Dikembalikan ke menu awal", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    new Book_Management_Menu();
                    break;
            }
        } while (choice != 1);

        if (choice == 1) {
            new Book_Management_Menu();
        }
    }

    private void addBookToDatabase(String name, String author, String published, int stock) {
        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String query = "INSERT INTO books (name, author, published, stock) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, author);
                statement.setString(3, published);
                statement.setInt(4, stock);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to add the book.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
