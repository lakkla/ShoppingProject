package com.zxsc.controller;

import com.zxsc.common.Result;
import com.zxsc.entity.Cart;
import com.zxsc.entity.User;
import com.zxsc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> carts = cartService.findByUserId(user.getId());
        model.addAttribute("carts", carts);
        return "cart/list";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result<String> add(@RequestParam Long productId, @RequestParam Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (cartService.addToCart(user.getId(), productId, quantity)) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result<String> update(@RequestParam Long id, @RequestParam Integer quantity) {
        if (cartService.updateQuantity(id, quantity)) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    
    @PostMapping("/delete")
    @ResponseBody
    public Result<String> delete(@RequestParam Long id) {
        if (cartService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @PostMapping("/clear")
    @ResponseBody
    public Result<String> clear(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (cartService.clearCart(user.getId())) {
            return Result.success("清空成功");
        }
        return Result.error("清空失败");
    }
}
