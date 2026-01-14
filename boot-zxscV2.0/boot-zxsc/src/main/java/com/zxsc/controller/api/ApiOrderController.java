package com.zxsc.controller.api;

import com.zxsc.common.Result;
import com.zxsc.entity.Order;
import com.zxsc.entity.User;
import com.zxsc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result<List<Order>> list(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        List<Order> orders = orderService.findByUserId(user.getId());
        return Result.success("查询成功", orders);
    }

    @GetMapping("/detail/{id}")
    public Result<Order> detail(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        Order order = orderService.findById(id);
        if (order != null && order.getUserId().equals(user.getId())) {
            return Result.success("查询成功", order);
        }
        return Result.error("订单不存在");
    }

    @PostMapping("/create")
    public Result<String> create(@RequestParam String cartIds,
                                 @RequestParam String receiverName,
                                 @RequestParam String receiverPhone,
                                 @RequestParam String receiverAddress,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }

        String[] ids = cartIds.split(",");
        List<Long> cartIdList = new ArrayList<>();
        for (String id : ids) {
            cartIdList.add(Long.parseLong(id));
        }

        String address = receiverName + " " + receiverPhone + " " + receiverAddress;

        Order order = orderService.createOrder(user.getId(), cartIdList, address);
        if (order != null) {
            return Result.success("订单创建成功");
        }
        return Result.error("订单创建失败");
    }

    @PostMapping("/pay")
    public Result<String> pay(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (orderService.updateStatus(id,1)) {
            return Result.success("支付成功");
        }
        return Result.error("支付失败");
    }

    @PostMapping("/cancel")
    public Result<String> cancel(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (orderService.updateStatus(id,4)) {
            return Result.success("取消成功");
        }
        return Result.error("取消失败");
    }
}
