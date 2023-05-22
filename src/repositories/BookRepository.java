package repositories;

import config.Constant;
import models.Book;
import utils.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public Book getBookById(int id) {
        Book book = null;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String published = resultSet.getString("published");
                int stock = resultSet.getInt("stock");

                book = new Book(id, name, author, published, stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return book;
    }

    public List<Book> getPagedBooks(int page, int limit) {
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM books LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            statement.setInt(2, (page - 1) * limit);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String published = resultSet.getString("published");
                int stock = resultSet.getInt("stock");

                Book book = new Book(id, name, author, published, stock);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return bookList;
    }

    public int getTotalBooks() {
        int count = 0;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT COUNT(id) FROM books";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return count;
    }

    public List<Book> searchBooksByName(String bookName) {
        List<Book> searchResults = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM books WHERE LOWER(name) LIKE LOWER(?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + bookName + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String published = resultSet.getString("published");
                int stock = resultSet.getInt("stock");

                Book book = new Book(id, name, author, published, stock);
                searchResults.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to search for books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return searchResults;
    }

    public boolean insertBook(Book book) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "INSERT INTO books (name, author, published, stock) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublished());
            statement.setInt(4, book.getStock());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to add book.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean updateBook(Book book) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "UPDATE books SET name = ?, author = ?, published = ?, stock = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublished());
            statement.setInt(4, book.getStock());
            statement.setInt(5, book.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to update book.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean deleteBook(int id) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to delete book.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
