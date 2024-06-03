package com.itmio.universitystudentloan.service;

import com.itmio.universitystudentloan.pojo.Loan;
import com.itmio.universitystudentloan.pojo.PageBean;

import java.util.List;

public interface LoanService {
    void loanApply(Loan loan);

    Loan findById(Integer userId, Integer loanType);

    Loan findLoan(Integer id);

    void loanUpdate(Loan loan);

    void loanDelete(Integer id);

    void loanCheck(Integer id,int state);

    List<Loan> loanStudent();

    PageBean<Loan> loanSchool(Integer pageNum, Integer pageSize);
}
