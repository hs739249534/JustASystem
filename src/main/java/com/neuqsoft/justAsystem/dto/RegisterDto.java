package com.neuqsoft.justAsystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="注册DTO",description="用户注册")
public class RegisterDto {
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("手机号")
    private Long phoneNum;
    @ApiModelProperty("姓名")
    private String name;
}
