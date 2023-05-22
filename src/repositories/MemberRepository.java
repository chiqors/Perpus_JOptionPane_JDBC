package repositories;

import config.Constant;
import models.Member;
import utils.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    public Member getMemberById(int id) {
        Member member = null;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM members WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String registered_at = resultSet.getString("registered_at");
                member = new Member(id, name, email, phone, registered_at);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return member;
    }

    public int getTotalMembers() {
        int total = 0;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT COUNT(id) AS total FROM members";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return total;
    }

    public List<Member> getPagedMembers(int page, int limit) {
        List<Member> memberList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM members LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            statement.setInt(2, (page - 1) * limit);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String registered_at = resultSet.getString("registered_at");
                memberList.add(new Member(id, name, email, phone, registered_at));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return memberList;
    }

    public Boolean insertMember(Member member) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to add member.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
