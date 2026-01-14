package com.zxsc.controller.api;

import com.zxsc.common.Result;
import com.zxsc.entity.User;
import com.zxsc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
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
    public Result<String> register(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String name,
                                   @RequestParam String phone,
                                   @RequestParam String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);

        if (userService.register(user)) {
            return Result.success("注册成功");
        }
        return Result.error("用户名已存在");
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return Result.success("退出成功");
    }

    @PostMapping("/updatePassword")
    public Result<String> updatePassword(@RequestParam String oldPassword,
                                         @RequestParam String newPassword,
                                         HttpSession session) {
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

    @PostMapping("/forgotPassword")
    public Result<String> forgotPassword(@RequestParam String username, @RequestParam String phone, @RequestParam String verifyCode, @RequestParam String newPassword) {
        // 验证验证码
        if (!"123456".equals(verifyCode)) {
            return Result.error("验证码错误");
        }
        
        // 验证用户是否存在且手机号正确
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (!user.getPhone().equals(phone)) {
            return Result.error("手机号不正确");
        }
        
        // 修改密码
        if (userService.updatePasswordByPhone(user.getId(), newPassword)) {
            return Result.success("密码修改成功");
        }
        return Result.error("修改密码失败");
    }
}
