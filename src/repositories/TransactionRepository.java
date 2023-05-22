package repositories;

import config.Constant;
import models.Book;
import models.Transaction;
import org.json.simple.parser.ParseException;
import utils.DBConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public List<Transaction> getAllTransaction() {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                transactionList.add(new Transaction(id, borrowedDate, expectedReturnDate, status, memberId, memberName, borrowedBooks, returnedDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get all books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    public List<Transaction> getPagedTransaction(int page, int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, (page - 1) * limit);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                transactionList.add(new Transaction(id, borrowedDate, expectedReturnDate, status, memberId, memberName, borrowedBooks, returnedDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get all books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    public Transaction getTransactionById(int id) {
        Transaction transaction = null;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                transaction = new Transaction(id, borrowedDate, expectedReturnDate, status, memberId, memberName, borrowedBooks, returnedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get transaction by id.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transaction;
    }

    public List<Transaction> getAllBorrowedTransaction() {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE status = 'borrowed'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                transactionList.add(new Transaction(id, borrowedDate, expectedReturnDate, status, memberId, memberName, borrowedBooks, returnedDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get all books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    public List<Transaction> getPagedBorrowedTransaction(int page, int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE status = 'borrowed' LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, (page - 1) * limit);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                transactionList.add(new Transaction(id, borrowedDate, expectedReturnDate, status, memberId, memberName, borrowedBooks, returnedDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get all books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    private ArrayList<Book> parseBookListFromJson(String json) {
        ArrayList<Book> bookList = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                int id = Integer.parseInt(jsonObject.get("id").toString());
                String name = jsonObject.get("name").toString();
                String author = jsonObject.get("author").toString();
                String published = jsonObject.get("published").toString();

                Book book = new Book(id, name, author, published);
                bookList.add(book);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception here
            JOptionPane.showMessageDialog(null, "Failed to parse book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return bookList;
    }
}
