package com.neuqsoft.justAsystem.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoDto {

    @ApiModelProperty("Id")
    private Long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private Long phoneNum;
}
