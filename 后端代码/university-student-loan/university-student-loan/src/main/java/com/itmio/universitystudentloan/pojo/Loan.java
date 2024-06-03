package com.itmio.universitystudentloan.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Loan {
    @NotNull
    private Integer id;
    @NotNull
    private Integer loanType;
    @NotNull
    private Integer loanLevel;
    @NotEmpty
    private String loanMaterial;
    @NotNull
    private Integer createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @NotNull
    private Integer state;
    @NotEmpty
    private String content;
}
