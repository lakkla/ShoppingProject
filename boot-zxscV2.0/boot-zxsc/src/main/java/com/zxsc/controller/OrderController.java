package com.zxsc.controller;

import com.zxsc.common.Result;
import com.zxsc.entity.Order;
import com.zxsc.entity.User;
import com.zxsc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.findByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "order/list";
    }
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order/detail";
    }
    
    @PostMapping("/create")
    @ResponseBody
    public Result<Order> create(@RequestParam List<Long> cartIds, @RequestParam String address, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        try {
            Order order = orderService.createOrder(user.getId(), cartIds, address);
            if (order != null) {
                return Result.success("订单创建成功", order);
            }
            return Result.error("订单创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/pay")
    @ResponseBody
    public Result<String> pay(@RequestParam Long id) {
        if (orderService.updateStatus(id, 1)) {
            return Result.success("支付成功");
        }
        return Result.error("支付失败");
    }
    
    @PostMapping("/cancel")
    @ResponseBody
    public Result<String> cancel(@RequestParam Long id) {
        if (orderService.cancelOrder(id)) {
            return Result.success("取消成功");
        }
        return Result.error("取消失败");
    }
}
