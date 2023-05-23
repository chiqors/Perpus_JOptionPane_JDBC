package repositories;

import config.Constant;
import models.Book;
import models.Transaction;
import org.json.simple.parser.ParseException;
import utils.DBConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.postgresql.util.PGobject;

import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public int getTotalTransactions() {
        int total = 0;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(id) AS total FROM transactions";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get total transaction.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return total;
    }

    public List<Transaction> getPagedTransactions(int page, int limit) {
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

                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setBorrowedDate(borrowedDate);
                transaction.setExpectedReturnDate(expectedReturnDate);
                transaction.setStatus(status);
                transaction.setMemberId(memberId);
                transaction.setMemberName(memberName);
                transaction.setBorrowedBooks(borrowedBooks);
                transaction.setReturnedDate(returnedDate);

                transactionList.add(transaction);
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

                transaction = new Transaction();
                transaction.setId(id);
                transaction.setBorrowedDate(borrowedDate);
                transaction.setExpectedReturnDate(expectedReturnDate);
                transaction.setStatus(status);
                transaction.setMemberId(memberId);
                transaction.setMemberName(memberName);
                transaction.setBorrowedBooks(borrowedBooks);
                transaction.setReturnedDate(returnedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get transaction by id.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transaction;
    }

    public Transaction getBorrowedTransactionById(int id) {
        Transaction transaction = null;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE id = ? AND status = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, "borrowed");
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

                transaction = new Transaction();
                transaction.setId(id);
                transaction.setBorrowedDate(borrowedDate);
                transaction.setExpectedReturnDate(expectedReturnDate);
                transaction.setStatus(status);
                transaction.setMemberId(memberId);
                transaction.setMemberName(memberName);
                transaction.setBorrowedBooks(borrowedBooks);
                transaction.setReturnedDate(returnedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get transaction by id.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transaction;
    }

    public List<Transaction> getTransactionListByMemberId(int memberId) {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE member_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String borrowedDate = resultSet.getString("borrowed_date");
                String expectedReturnDate = resultSet.getString("expected_return_date");
                String status = resultSet.getString("status");
                String memberName = resultSet.getString("member_name");

                // Retrieve the borrowed_books JSON array as a String
                String borrowedBooksJson = resultSet.getString("borrowed_books");
                // Convert the borrowed_books JSON array String to a ArrayList<Book> object
                ArrayList<Book> borrowedBooks = parseBookListFromJson(borrowedBooksJson);

                String returnedDate = resultSet.getString("returned_date");

                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setBorrowedDate(borrowedDate);
                transaction.setExpectedReturnDate(expectedReturnDate);
                transaction.setStatus(status);
                transaction.setMemberId(memberId);
                transaction.setMemberName(memberName);
                transaction.setBorrowedBooks(borrowedBooks);
                transaction.setReturnedDate(returnedDate);

                transactionList.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get transaction by member id.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    public List<Transaction> getPagedBorrowedTransactions(int page, int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM transactions WHERE status = 'borrowed' ORDER BY id ASC LIMIT ? OFFSET ?";
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

                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setBorrowedDate(borrowedDate);
                transaction.setExpectedReturnDate(expectedReturnDate);
                transaction.setStatus(status);
                transaction.setMemberId(memberId);
                transaction.setMemberName(memberName);
                transaction.setBorrowedBooks(borrowedBooks);
                transaction.setReturnedDate(returnedDate);

                transactionList.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get all books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return transactionList;
    }

    public int getTotalBorrowedTransactions() {
        int total = 0;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT COUNT(id) AS total FROM transactions WHERE status = 'borrowed'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to get total transaction.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return total;
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
                int qty = Integer.parseInt(jsonObject.get("qty").toString());

                Book book = new Book();
                book.setId(id);
                book.setName(name);
                book.setAuthor(author);
                book.setPublished(published);
                book.setQty(qty);

                bookList.add(book);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception here
            JOptionPane.showMessageDialog(null, "Failed to parse book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return bookList;
    }

    private String parseBookListToJson(ArrayList<Book> bookList) {
        JSONArray jsonArray = new JSONArray();

        for (Book book : bookList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", book.getId());
            jsonObject.put("name", book.getName());
            jsonObject.put("author", book.getAuthor());
            jsonObject.put("published", book.getPublished());
            jsonObject.put("qty", book.getQty());

            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }

    public Boolean insertBorrowTransaction(Transaction transaction) {
        PGobject borrowedBookJsonObject = new PGobject();
        borrowedBookJsonObject.setType("json");
        Boolean isSuccess = false;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "INSERT INTO transactions (borrowed_date, expected_return_date, status, member_id, member_name, borrowed_books) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            borrowedBookJsonObject.setValue(parseBookListToJson(transaction.getBorrowedBooks()));

            statement.setString(1, transaction.getBorrowedDate());
            statement.setString(2, transaction.getExpectedReturnDate());
            statement.setString(3, transaction.getStatus());
            statement.setInt(4, transaction.getMemberId());
            statement.setString(5, transaction.getMemberName());
            statement.setObject(6, borrowedBookJsonObject);

            isSuccess = statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to insert transaction.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return isSuccess;
    }

    public Boolean borrowBooks(Transaction transaction) {
        Boolean isSuccess = false;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "UPDATE books SET stock = stock - ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (Book book : transaction.getBorrowedBooks()) {
                statement.setInt(1, book.getQty());
                statement.setInt(2, book.getId());
                statement.addBatch();
            }

            isSuccess = statement.executeBatch().length > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to borrow books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return isSuccess;
    }

    public Boolean updateBorrowedToReturnedTransaction(Transaction transaction) {
        Boolean isSuccess = false;
        String returnedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "UPDATE transactions SET status = 'returned', returned_date = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, returnedDate);
            statement.setInt(2, transaction.getId());

            isSuccess = statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to update transaction.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return isSuccess;
    }

    public Boolean returnBooks(Transaction transaction) {
        Boolean isSuccess = false;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "UPDATE books SET stock = stock + ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (Book book : transaction.getBorrowedBooks()) {
                statement.setInt(1, book.getQty());
                statement.setInt(2, book.getId());
                statement.addBatch();
            }

            isSuccess = statement.executeBatch().length > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to return books.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return isSuccess;
    }
}
