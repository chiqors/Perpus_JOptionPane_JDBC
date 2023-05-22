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
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String sql = "SELECT * FROM members";
            PreparedStatement statement = connection.prepareStatement(sql);
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
        }

        return memberList;
    }

    public Member getMemberById(int id) {
        Member member = null;

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM members WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        String registered_at = resultSet.getString("registered_at");
                        member = new Member(id, name, email, phone, registered_at);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to load book data.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        return member;
    }

    public List<Member> getPagedMembers(int page, int limit) {
        List<Member> memberList = new ArrayList<>();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "SELECT * FROM members LIMIT ? OFFSET ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, limit);
                statement.setInt(2, (page - 1) * limit);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        String registered_at = resultSet.getString("registered_at");
                        memberList.add(new Member(id, name, email, phone, registered_at));
                    }
                }
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
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, member.getName());
                statement.setString(2, member.getEmail());
                statement.setString(3, member.getPhone());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to add member.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public Boolean updateMember(Member member) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "UPDATE members SET name = ?, email = ?, phone = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, member.getName());
                statement.setString(2, member.getEmail());
                statement.setString(3, member.getPhone());
                statement.setInt(4, member.getId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to update member.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public Boolean deleteMember(int id) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            String query = "DELETE FROM members WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            JOptionPane.showMessageDialog(null, "Failed to delete member.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
