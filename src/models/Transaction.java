package models;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

public class Transaction {
    private int id;
    private String borrowed_date;
    private String expected_return_date;
    private String status;
    private int member_id;
    private String member_name;
    private ArrayList<Book> borrowed_books;
    private String returned_date;

    @Override
    public String toString() {
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append(i + ". " + borrowedBook.getName() + " | (" + borrowedBook.getQty() + ")").append("\n");
            i++;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedReturnDate = null;
        Date returnedDate = null;
        try {
            expectedReturnDate = formatter.parse(expected_return_date);
            returnedDate = formatter.parse(returned_date);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        if (returnedDate != null) {
            if (returnedDate.after(expectedReturnDate)) {
                status = "Terlambat Mengembalikan";
            } else {
                status = "Tepat Waktu";
            }
        } else {
            status = "Belum Dikembalikan";
        }

        String content = "\n# ID: " + id;
        content += "\n# Tanggal Peminjaman: " + borrowed_date + " - " + expected_return_date;
        if (returned_date != null) {
            content += "\n# Tanggal Pengembalian: " + returned_date;
        }
        content += "\n# Status: " + status;
        content += "\n# Anggota: ID " + member_id + " | " + member_name;
        content += "\n# Buku yang dipinjam:\n" + stringBuilder.toString();
        return content;
    }

    public String showMenuReturn() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append("## "+borrowedBook.getName()).append("\n");
        }
        return "\n# ID: " + id +
                "\n# Tanggal Pinjam: " + borrowed_date +
                "\n# Tanggal Batas Pengembalian: " + expected_return_date +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n\n# Buku yang dipinjam:\n" + stringBuilder.toString();
    }

    public String showMenuTransaction() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append("## "+borrowedBook.getName()).append("\n");
        }
        return "\n# ID: " + id +
                "\n# Tanggal Pinjam/Batas Pengembalian: " + borrowed_date + " - " + expected_return_date +
                "\n# Tanggal Pengembalian: " + returned_date +
                "\n# Status: " + status +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n# Buku yang dipinjam:\n" + stringBuilder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowedDate() {
        return borrowed_date;
    }

    public void setBorrowedDate(String borrowed_date) {
        this.borrowed_date = borrowed_date;
    }

    public String getExpectedReturnDate() {
        return expected_return_date;
    }

    public void setExpectedReturnDate(String expected_return_date) {
        this.expected_return_date = expected_return_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }

    public String getMemberName() {
        return member_name;
    }

    public void setMemberName(String member_name) {
        this.member_name = member_name;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowed_books;
    }

    public void setBorrowedBooks(ArrayList<Book> borrowed_books) {
        this.borrowed_books = borrowed_books;
    }

    public String getReturnedDate() {
        return returned_date;
    }

    public void setReturnedDate(String returned_date) {
        this.returned_date = returned_date;
    }
}
