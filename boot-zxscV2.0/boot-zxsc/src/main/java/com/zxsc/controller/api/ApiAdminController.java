package com.zxsc.controller.api;

import com.zxsc.common.Result;
import com.zxsc.entity.*;
import com.zxsc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ApiAdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/user/list")
    public Result<List<User>> userList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        List<User> users = userService.findAll();
        return Result.success("查询成功", users);
    }
    
    @PostMapping("/user/update")
    public Result<String> userUpdate(@RequestBody User updateUser, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (userService.update(updateUser)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/user/delete")
    public Result<String> userDelete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (userService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @PostMapping("/user/resetPassword")
    public Result<String> userResetPassword(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (userService.updatePassword(id, null, "123456")) {
            return Result.success("密码已重置为123456");
        }
        return Result.error("重置失败");
    }
    
    @GetMapping("/product/list")
    public Result<List<Product>> productList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        List<Product> products = productService.findAll();
        return Result.success("查询成功", products);
    }
    
    @PostMapping("/product/save")
    public Result<String> productSave(@RequestBody Product product, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (productService.save(product)) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/product/delete")
    public Result<String> productDelete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (productService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/category/list")
    public Result<List<Category>> categoryList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        List<Category> categories = categoryService.findAll();
        return Result.success("查询成功", categories);
    }
    
    @PostMapping("/category/save")
    public Result<String> categorySave(@RequestBody Category category, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (categoryService.save(category)) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/category/delete")
    public Result<String> categoryDelete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (categoryService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/order/list")
    public Result<List<Order>> orderList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        List<Order> orders = orderService.findAll();
        return Result.success("查询成功", orders);
    }
    
    @PostMapping("/order/updateStatus")
    public Result<String> orderUpdateStatus(@RequestBody Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        if (orderService.updateStatus(order.getId(), order.getStatus())) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
