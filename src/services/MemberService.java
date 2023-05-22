package services;

import config.Constant;
import models.Member;
import repositories.MemberRepository;
import utils.LoadingDialog;

import javax.swing.*;
import java.util.List;

public class MemberService {
    private final LoadingDialog loadingDialog;
    private final MemberRepository memberRepository;

    public MemberService() {
        loadingDialog = new LoadingDialog();
        memberRepository = new MemberRepository();
    }

    // -----------------------------
    // All Non-Void Methods Below
    // -----------------------------

    public Member getMemberById(int memberId) {
        loadingDialog.showLoading();
        Member member = memberRepository.getMemberById(memberId);
        loadingDialog.hideLoading();
        return member;
    }

    public Object getPagedMemberList(int page, int pageSize) {
        loadingDialog.showLoading();

        // get the paged member list
        List<Member> memberList = memberRepository.getPagedMembers(page, pageSize);
        // get the total number of members
        int totalMembers = memberRepository.getTotalMembers();
        // get the total number of pages
        int totalPages = (int) Math.ceil((double) totalMembers / pageSize);
        // get the current page
        int currentPage = page;
        // combine all the data into a single object to be returned
        Object[] result = {memberList, totalPages, currentPage};
        // result: [memberList, totalPages, currentPage]

        loadingDialog.hideLoading();
        return result;
    }

    // -----------------------------
    // All Void Methods Below
    // -----------------------------

    public void addMember(Member member) {
        loadingDialog.showLoading();
        boolean statusQuery = memberRepository.insertMember(member);
        loadingDialog.hideLoading();
        if (statusQuery) {
            JOptionPane.showMessageDialog(null, "Anggota berhasil ditambahkan.", Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan anggota.", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
}
