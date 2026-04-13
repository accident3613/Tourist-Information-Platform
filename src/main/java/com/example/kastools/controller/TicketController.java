package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Ticket;
import com.example.kastools.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

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
}
