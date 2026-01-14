package com.zxsc.controller.api;

import com.zxsc.common.Result;
import com.zxsc.entity.Cart;
import com.zxsc.entity.User;
import com.zxsc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ApiCartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public Result<List<Cart>> list(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        List<Cart> carts = cartService.findByUserId(user.getId());
        return Result.success("查询成功", carts);
    }

    @PostMapping("/add")
    public Result<String> add(@RequestParam Long productId,
                              @RequestParam Integer quantity,
                              HttpSession session) {
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
    public Result<String> update(@RequestParam Long id,
                                 @RequestParam Integer quantity,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (cartService.updateQuantity(id, quantity)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (cartService.deleteById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @PostMapping("/clear")
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
