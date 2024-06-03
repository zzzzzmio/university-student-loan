package com.itmio.universitystudentloan.mapper;

import com.itmio.universitystudentloan.pojo.Loan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LoanMapper {
    @Insert("insert into loan(loan_type,loan_level,loan_material,create_user,create_time,update_time,state,content) " +
            "values (#{loanType},#{loanLevel},#{loanMaterial},#{createUser},now(),now(),#{state},#{content})")
    void loanApply(Loan loan);

    @Select("select * from loan where create_user=#{userId} and loan_type=#{loanType}")
    Loan findById(Integer userId, Integer loanType);

    @Select("select * from loan where id=#{id};")
    Loan findLoan(Integer id);

    @Update("update loan set loan_type=#{loanType},loan_level=#{loanLevel},loan_material=#{loanMaterial},update_time=now(),content=#{content} where id=#{id}")
    void loanUpdate(Loan loan);

    @Delete("delete from loan where id=#{id} and create_user=#{userId}")
    void loanDelete(Integer id, Integer userId);

    @Update("update loan set state=#{state},update_time=now() where id=#{id}")
    void loanCheck(Integer id,int state);

    @Select("select * from loan where create_user=#{userId}")
    List<Loan> loanStudent(Integer userId);

    @Select("select * from loan where state=1")
    List<Loan> loanSchool();
}
