package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Member;
import util.DBConnection;

public class MemberDAO {

    // ADD MEMBER
    public boolean addMember(Member member) {

        String sql =
                "INSERT INTO members(name,email,phone) VALUES(?,?,?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());

            return ps.executeUpdate() > 0;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // UPDATE MEMBER
    public boolean updateMember(Member member) {

        String sql =
                "UPDATE members SET name=?,email=?,phone=? WHERE member_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setInt(4, member.getMemberId());

            return ps.executeUpdate() > 0;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // DELETE MEMBER
    public boolean deleteMember(int memberId) {

        String sql = "UPDATE members SET status='Inactive' WHERE member_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);

            return ps.executeUpdate() > 0;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // VIEW MEMBERS
    public List<Member> getAllMembers() {

        List<Member> members = new ArrayList<>();

        String sql = "SELECT * FROM members WHERE status='Active'";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}