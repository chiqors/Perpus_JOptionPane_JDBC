package features.membership;

import config.Constant;
import models.Member;
import utils.DB_Connection;
import utils.LoadingDialog;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class List_Members {
    public List_Members() {
        int choice;
        // load list of members from the database
        List<Member> memberList = loadData();
        do {
            String title = "List Anggota\n\n";

            // display list of members
            String memberData = "";
            for (int i = 0; i < memberList.size(); i++) {
                memberData += (i + 1) + ". " + memberList.get(i) + "\n";
            }

            String menu = JOptionPane.showInputDialog(null, title + memberData + "\n0. Kembali", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to member management menu
            if (menu == null) {
                choice = 0;
                break;
            }

            choice = Integer.parseInt(menu);

            if (choice != 0) {
                // display invalid input message
                JOptionPane.showMessageDialog(null, "Input tidak valid!", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        } while (choice != 0);

        if (choice == 0) {
            new Member_Management_Menu();
        }
    }

    private List<Member> loadData() {
        List<Member> memberList = new ArrayList<>();
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.showLoading(); // Show the loading dialog

        try (Connection connection = DB_Connection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM members";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String registered_at = resultSet.getString("registered_at");
                memberList.add(new Member(name, email, phone, registered_at));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadingDialog.hideLoading(); // Hide the loading dialog after data has been loaded
        return memberList;
    }
}
