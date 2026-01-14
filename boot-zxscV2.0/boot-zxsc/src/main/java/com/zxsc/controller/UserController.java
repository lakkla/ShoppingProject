package com.zxsc.controller;

import com.zxsc.common.Result;
import com.zxsc.entity.User;
import com.zxsc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Result<User> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.login(username, password);
        if (user != null) {
            if (user.getStatus() == 0) {
                return Result.error("用户已被禁用");
            }
            session.setAttribute("user", user);
            return Result.success("登录成功", user);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    @ResponseBody
    public Result<String> register(@RequestBody User user) {
        if (userService.register(user)) {
            return Result.success("注册成功");
        }
        return Result.error("用户名已存在");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public Result<String> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (userService.updatePassword(user.getId(), oldPassword, newPassword)) {
            return Result.success("密码修改成功");
        }
        return Result.error("原密码错误");
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<String> update(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("请先登录");
        }
        user.setId(currentUser.getId());
        if (userService.update(user)) {
            session.setAttribute("user", userService.findById(user.getId()));
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @PostMapping("/updateProfile")
    @ResponseBody
    public Result<String> updateProfile(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("请先登录");
        }
        user.setId(currentUser.getId());
        if (userService.updateProfile(user)) {
            session.setAttribute("user", userService.findById(user.getId()));
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
}
