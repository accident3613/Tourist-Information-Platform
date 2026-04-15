package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Ticket;
import com.example.kastools.service.AdminPermissionService;
import com.example.kastools.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AdminPermissionService permissionService;

    @GetMapping("/list")
    public Result list(@RequestParam("siteId") Long siteId) {
        Result result = new Result();
        List<Ticket> tickets = ticketService.getTicketsBySiteId(siteId);
        result.setCode(1);
        result.setData(JSON.toJSONString(tickets));
        return result;
    }

    @GetMapping("/detail")
    public Result detail(@RequestParam("ticketId") Long ticketId) {
        Result result = new Result();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket != null) {
            result.setCode(1);
            result.setData(JSON.toJSONString(ticket));
        } else {
            result.setCode(0);
            result.setData("门票不存在");
        }
        return result;
    }

    @GetMapping("/admin/list")
    public Result adminList(@RequestParam(value = "siteId", required = false) Long siteId, HttpServletRequest request) {
        // 获取当前管理员
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        Result result = new Result();
        List<Ticket> tickets;

        // 运营人员只能查看自己景点的门票
        if (permissionService.isOperator(admin)) {
            if (admin.getSite_id() != null) {
                tickets = ticketService.getTicketsBySiteIdAll(admin.getSite_id());
            } else {
                result.setCode(0);
                result.setData("未分配管理景点");
                return result;
            }
        } else {
            // 超级管理员和管理员可以查看所有或指定景点的门票
            if (siteId != null) {
                tickets = ticketService.getTicketsBySiteIdAll(siteId);
            } else {
                tickets = ticketService.getAllTickets();
            }
        }

        result.setCode(1);
        result.setData(JSON.toJSONString(tickets));
        return result;
    }

    @PostMapping("/admin/save")
    public Result adminSave(@RequestBody Ticket ticket, HttpServletRequest request) {
        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, ticket.getSite_id());
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        Result result = new Result();
        try {
            Ticket saved = ticketService.saveTicket(ticket);
            result.setCode(1);
            result.setData(JSON.toJSONString(saved));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("保存失败: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/admin/toggle/{id}")
    public Result adminToggle(@PathVariable Long id, HttpServletRequest request) {
        // 先获取门票信息
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("门票不存在");
            return result;
        }

        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, ticket.getSite_id());
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        Result result = new Result();
        try {
            ticketService.toggleTicketStatus(id);
            result.setCode(1);
            result.setData("操作成功");
        } catch (Exception e) {
            result.setCode(0);
            result.setData("操作失败: " + e.getMessage());
        }
        return result;
    }
}
