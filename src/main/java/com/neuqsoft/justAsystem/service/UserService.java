package com.neuqsoft.justAsystem.service;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.neuqsoft.justAsystem.dao.UserDao;
import com.neuqsoft.justAsystem.dto.RegisterDto;
import com.neuqsoft.justAsystem.dto.UserInfoDto;
import com.neuqsoft.justAsystem.model.RespBean;
import com.neuqsoft.justAsystem.model.Role;
import com.neuqsoft.justAsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return user;
    }

    public RespBean userRegister(RegisterDto registerDto) {
        User user = new User();
        try {
            String email=registerDto.getEmail();
            String username=email.substring(0, email.indexOf("@"));
            user.setUsername(username);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setEmail(registerDto.getEmail());
            user.setPassword(registerDto.getPassword());
            user.setPhoneNum(registerDto.getPhoneNum());
            user.setName(registerDto.getName());
            List<Role> rs1 = new ArrayList<>();
            Role r1 = new Role();
            r1.setName("ROLE_user");
            r1.setNameZh("普通用户");
            rs1.add(r1);
            user.setRoles(rs1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDao.save(user);
        return RespBean.ok("注册成功");

    }

    public RespBean changeUserInfo(UserInfoDto userInfoDto) {
        User user = userDao.findById(userInfoDto.getId()).get();
        try {
            user.setName(userInfoDto.getName());
            user.setPhoneNum(userInfoDto.getPhoneNum());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDao.save(user);
        return RespBean.ok("修改成功");

    }

    public List<User> getAllInfo() {
        return (List<User>) userDao.findAll();
    }

    public RespBean deleteStaff(Long id) {
        User user = userDao.findById(id).get();
        try {
            user.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDao.save(user);
        return RespBean.ok("删除成功");
    }

    public RespBean changePassword(Long id, String password) {
        User user = userDao.findById(id).get();
        try {
            user.setPassword(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDao.save(user);
        return RespBean.ok("修改成功");
    }

    public RespBean addManyUserInfo(MultipartFile file) {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExcelReader reader = ExcelUtil.getReader(in, 0);
        List<User> userAuths = reader.readAll(User.class);
        System.out.println("file处理：\n"+userAuths);
        userAuths.forEach(userAuth -> {
//            userAuth.setCreateTime(DateUtil.now());
//            userAuth.setUserId(UUID.fastUUID().toString(true));
            String email=userAuth.getEmail();
            String username=email.substring(0, email.indexOf("@"));
            userAuth.setUsername(username);
            userAuth.setAccountNonExpired(true);
            userAuth.setAccountNonLocked(true);
            userAuth.setCredentialsNonExpired(true);
            userAuth.setEnabled(true);
            List<Role> rs1 = new ArrayList<>();
            Role r1 = new Role();
            r1.setName("ROLE_user");
            r1.setNameZh("普通用户");
            rs1.add(r1);
            userAuth.setRoles(rs1);
        });
        userDao.saveAll(userAuths);
        return RespBean.ok("批量导入成功");
    }

}
