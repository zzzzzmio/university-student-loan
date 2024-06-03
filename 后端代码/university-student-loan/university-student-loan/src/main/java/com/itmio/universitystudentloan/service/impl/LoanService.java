package com.itmio.universitystudentloan.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itmio.universitystudentloan.mapper.LoanMapper;
import com.itmio.universitystudentloan.pojo.Loan;
import com.itmio.universitystudentloan.pojo.PageBean;
import com.itmio.universitystudentloan.pojo.Result;
import com.itmio.universitystudentloan.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LoanService implements com.itmio.universitystudentloan.service.LoanService {
    @Autowired
    private LoanMapper loanMapper;

    //申请贷款
    @Override
    public void loanApply(Loan loan) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId= (Integer) map.get("id");
        loan.setCreateUser(userId);
        loan.setState(1);
        loanMapper.loanApply(loan);
    }
    //查询该用户是否已经申请该类型的资金
    @Override
    public Loan findById(Integer userId, Integer loanType) {
        return loanMapper.findById(userId,loanType);
    }

    //根据id查找loan表
    @Override
    public Loan findLoan(Integer id) {
        return loanMapper.findLoan(id);
    }

    //更新贷款内容
    @Override
    public void loanUpdate(Loan loan) {
        loanMapper.loanUpdate(loan);
    }

    //删除
    @Override
    public void loanDelete(Integer id) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        loanMapper.loanDelete(id,userId);
    }

    //审核通过
    @Override
    public void loanCheck(Integer id,int state) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        loanMapper.loanCheck(id,state);
    }

    //学生用户获取贷款列表
    @Override
    public List<Loan> loanStudent() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return loanMapper.loanStudent(userId);
    }

    //校方用户获取待审批的贷款列表
    @Override
    public PageBean<Loan> loanSchool(Integer pageNum, Integer pageSize) {
        //1.创建PageBean对象
        PageBean<Loan> pb = new PageBean<>();
        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum,pageSize);
        List<Loan> loans=loanMapper.loanSchool();
        Page<Loan> p = (Page<Loan>) loans;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }
}
