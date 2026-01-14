package com.zxsc.controller;

import com.zxsc.common.Result;
import com.zxsc.entity.Category;
import com.zxsc.entity.Order;
import com.zxsc.entity.Product;
import com.zxsc.entity.User;
import com.zxsc.service.CategoryService;
import com.zxsc.service.OrderService;
import com.zxsc.service.ProductService;
import com.zxsc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/index")
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        return "admin/index";
    }
    
    @GetMapping("/user/list")
    public String userList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/user-list";
    }
    
    @GetMapping("/user/edit")
    public String userEdit(@RequestParam Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        User editUser = userService.findById(id);
        model.addAttribute("user", editUser);
        return "admin/user-edit";
    }
    
    @PostMapping("/user/update")
    @ResponseBody
    public Result<String> updateUser(@RequestBody User user) {
        if (userService.update(user)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/user/resetPassword")
    @ResponseBody
    public Result<String> resetPassword(@RequestParam Long id) {
        if (userService.updatePassword(id, "", "123456")) {
            return Result.success("密码已重置为123456");
        }
        return Result.error("重置失败");
    }
    
    @PostMapping("/user/delete")
    @ResponseBody
    public Result<String> deleteUser(@RequestParam Long id) {
        if (userService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/product/list")
    public String productList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin/product-list";
    }
    
    @GetMapping("/product/edit")
    public String productEdit(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/product-edit";
    }
    
    @PostMapping("/product/save")
    @ResponseBody
    public Result<String> saveProduct(@RequestBody Product product) {
        if (productService.save(product)) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/product/delete")
    @ResponseBody
    public Result<String> deleteProduct(@RequestParam Long id) {
        if (productService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/category/list")
    public String categoryList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/category-list";
    }
    
    @PostMapping("/category/save")
    @ResponseBody
    public Result<String> saveCategory(@RequestBody Category category) {
        if (categoryService.save(category)) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/category/delete")
    @ResponseBody
    public Result<String> deleteCategory(@RequestParam Long id) {
        if (categoryService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @GetMapping("/order/list")
    public String orderList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "admin/order-list";
    }
    
    @PostMapping("/order/updateStatus")
    @ResponseBody
    public Result<String> updateOrderStatus(@RequestParam Long id, @RequestParam Integer status) {
        if (orderService.updateStatus(id, status)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
