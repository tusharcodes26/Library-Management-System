package service;

import dao.MemberDAO;
import java.util.List;
import model.Member;

public class MemberService {

    private MemberDAO memberDAO =
            new MemberDAO();

    public boolean addMember(Member member) {
        util.Validator.validateMember(member);
        return memberDAO.addMember(member);
    }

    public boolean updateMember(Member member) {
        if(member.getMemberId() <= 0) {
            throw new util.ValidationException("Invalid Member ID");
        }
        util.Validator.validateMember(member);
        return memberDAO.updateMember(member);
    }

    public boolean deleteMember(int memberId) {

        return memberDAO.deleteMember(memberId);
    }

    public List<Member> getAllMembers() {

        return memberDAO.getAllMembers();
    }
}