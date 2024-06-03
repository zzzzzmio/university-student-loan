package com.itmio.universitystudentloan.controller;

import com.itmio.universitystudentloan.pojo.Loan;
import com.itmio.universitystudentloan.pojo.PageBean;
import com.itmio.universitystudentloan.pojo.Result;
import com.itmio.universitystudentloan.service.impl.LoanService;
import com.itmio.universitystudentloan.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan")
@CrossOrigin
public class LoanController {
    @Autowired
    private LoanService loanService;

    //贷款申请
    @PostMapping("/apply")
    public Result loanApply(@RequestBody Loan loan){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId= (Integer) map.get("id");
        Loan userLoan=loanService.findById(userId,loan.getLoanType());
        if(userLoan!=null){
            return Result.error("您已申请该类型的资金");
        }
        loanService.loanApply(loan);
        return Result.success();
    }

    //更新贷款内容
    @PutMapping("/update")
    public Result loanUpdate(@RequestBody Loan loan){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId= (Integer) map.get("id");
        Integer id = loan.getId();
        Loan l=loanService.findLoan(id);
        if(l.getLoanType()!=loan.getLoanType()){
            Loan userLoan=loanService.findById(userId,loan.getLoanType());
            if(userLoan!=null){
                return Result.error("您已申请该类型资助金");
            }
        }
        loanService.loanUpdate(loan);
        return Result.success();
    }

    //获取贷款内容
    @GetMapping("/detail")
    public Result<Loan> loanDetail(@RequestParam Integer id){
        Loan loan = loanService.findLoan(id);
        return Result.success(loan);
    }

    //删除贷款
    @DeleteMapping("/delete")
    public Result loanDelete(@RequestParam Integer id){
        loanService.loanDelete(id);
        return Result.success();
    }

    //校方审核通过
    @PutMapping("/agree")
    public Result loanAgree(@RequestParam Integer id){
        loanService.loanCheck(id,2);
        return Result.success();
    }

    //校方审核不通过
    @PutMapping ("/disagree")
    public Result loanDisagree(@RequestParam Integer id){
        loanService.loanCheck(id,3);
        return Result.success();
    }

    //学生用户获取贷款列表
    @GetMapping("/student")
    public Result<List<Loan>> loanStudent(){
        List<Loan> loanList=loanService.loanStudent();
        return Result.success(loanList);
    }

    //校方用户获取待审批的贷款列表
    @GetMapping("/school")
    public Result<PageBean<Loan>> loanSchool(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageBean<Loan> pb = loanService.loanSchool(pageNum,pageSize);
        return Result.success(pb);

    }

}
