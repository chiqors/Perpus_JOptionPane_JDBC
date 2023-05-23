package services;

import config.Constant;
import models.Transaction;
import repositories.TransactionRepository;
import utils.LoadingDialog;

import javax.swing.*;
import java.util.List;

public class TransactionService {
    private final LoadingDialog loadingDialog;
    private final TransactionRepository transactionRepository;

    public TransactionService() {
        loadingDialog = new LoadingDialog();
        transactionRepository = new TransactionRepository();
    }

    // -----------------------------
    // All Non-Void Methods Below
    // -----------------------------

    public Transaction getTransactionById(int transactionId) {
        loadingDialog.showLoading();
        Transaction transaction = transactionRepository.getTransactionById(transactionId);
        loadingDialog.hideLoading();
        return transaction;
    }

    public Transaction getBorrowedTransactionById(int transactionId) {
        loadingDialog.showLoading();
        Transaction transaction = transactionRepository.getBorrowedTransactionById(transactionId);
        loadingDialog.hideLoading();
        return transaction;
    }

    public List<Transaction> getTransactionListByMemberId(int memberId) {
        loadingDialog.showLoading();
        List<Transaction> transactionList = transactionRepository.getTransactionListByMemberId(memberId);
        loadingDialog.hideLoading();
        return transactionList;
    }

    public Object getPagedTransactionList(int page, int pageSize) {
        loadingDialog.showLoading();

        // get the paged transaction list
        List<Transaction> transactionList = transactionRepository.getPagedTransactions(page, pageSize);
        // get the total number of transactions
        int totalTransactions = transactionRepository.getTotalTransactions();
        // get the total number of pages
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);
        // get the current page
        int currentPage = page;
        // combine all the data into a single object to be returned
        Object[] result = {transactionList, totalPages, currentPage};
        // result: [transactionList, totalPages, currentPage]

        loadingDialog.hideLoading();
        return result;
    }

    public Object getPagedBorrowedTransactionList(int page, int pageSize) {
        loadingDialog.showLoading();

        // get the paged transaction list
        List<Transaction> transactionList = transactionRepository.getPagedBorrowedTransactions(page, pageSize);
        // get the total number of transactions
        int totalTransactions = transactionRepository.getTotalBorrowedTransactions();
        // get the total number of pages
        int totalPages = (int) Math.ceil((double) totalTransactions / pageSize);
        // get the current page
        int currentPage = page;
        // combine all the data into a single object to be returned
        Object[] result = {transactionList, totalPages, currentPage};
        // result: [transactionList, totalPages, currentPage]

        loadingDialog.hideLoading();
        return result;
    }

    // -----------------------------
    // All Void Methods Below
    // -----------------------------

    public void addBorrowTransaction(Transaction transaction) {
        loadingDialog.showLoading();
        boolean statusQuery = transactionRepository.insertBorrowTransaction(transaction);
        loadingDialog.hideLoading();
        if (statusQuery) {
            boolean statusQuery2 = transactionRepository.borrowBooks(transaction);
            if (statusQuery2) {
                JOptionPane.showMessageDialog(null, "Transaksi berhasil ditambahkan.", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Update stock gagal ditambahkan.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Transaksi gagal ditambahkan.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBorrowedToReturnedTransaction(Transaction transaction) {
        loadingDialog.showLoading();
        boolean statusQuery = transactionRepository.updateBorrowedToReturnedTransaction(transaction);
        loadingDialog.hideLoading();
        if (statusQuery) {
            boolean statusQuery2 = transactionRepository.returnBooks(transaction);
            if (statusQuery2) {
                JOptionPane.showMessageDialog(null, "Transaksi berhasil diubah.", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Update stock gagal diubah.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Transaksi gagal diubah.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
