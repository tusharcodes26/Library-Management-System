package service;

import dao.MemberDAO;
import java.util.List;
import model.Member;

public class MemberService {

    private MemberDAO memberDAO =
            new MemberDAO();

    public boolean addMember(Member member) {

        if(member.getName().trim().isEmpty()) {

            System.out.println("Name Required");
            return false;
        }

        if(!member.getEmail().contains("@")) {

            System.out.println("Invalid Email");
            return false;
        }

        return memberDAO.addMember(member);
    }

    public boolean updateMember(Member member) {

        return memberDAO.updateMember(member);
    }

    public boolean deleteMember(int memberId) {

        return memberDAO.deleteMember(memberId);
    }

    public List<Member> getAllMembers() {

        return memberDAO.getAllMembers();
    }
}