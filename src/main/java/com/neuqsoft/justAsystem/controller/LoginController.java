package com.neuqsoft.justAsystem.controller;

import com.neuqsoft.justAsystem.config.VerificationCode;
import com.neuqsoft.justAsystem.model.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class LoginController {

//    @RequestMapping("/user/login")
//    @ResponseBody
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
//        if (!StringUtils.isEmpty(username) && "123456".equals(password)){
////            return "dashboard";
//            return "redirect:main.html"; // 重定向
//        } else {
//            model.addAttribute("msg", "用户名或密码错误");
//            return "index";
//        }
//    }
    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("尚未登录，请登录！");
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpSession session, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        session.setAttribute("verify_code", text);
        VerificationCode.output(image, resp.getOutputStream());
    }

}
