package com.neuqsoft.justAsystem.controller;

import com.neuqsoft.justAsystem.dto.RegisterDto;
import com.neuqsoft.justAsystem.dto.UserInfoDto;
import com.neuqsoft.justAsystem.model.RespBean;
import com.neuqsoft.justAsystem.model.User;
import com.neuqsoft.justAsystem.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api(description = "用户操作")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public RespBean userRegister(@RequestBody RegisterDto registerDto) {
        return userService.userRegister(registerDto);
    }

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/getUserInfo")
    public UserDetails getUserInfo(@RequestParam String username) {
        return userService.loadUserByUsername(username);
    }

    @ApiOperation(value = "修改个人信息")
    @PutMapping("/changeUserInfo")
    public RespBean changeUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return userService.changeUserInfo(userInfoDto);
    }

    @ApiOperation(value = "查询全部员工信息")
    @GetMapping("/getAllInfo")
    public List<User> getAllInfo() {
        return userService.getAllInfo();
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/deleteStaff")
    public RespBean deleteStaff(@RequestParam Long id) {
        return userService.deleteStaff(id);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/changePassword")
    public RespBean changePassword(@RequestParam Long id, @RequestParam String password) {
        return userService.changePassword(id, password);
    }
}
