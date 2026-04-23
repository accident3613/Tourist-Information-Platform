package com.example.kastools.service.impl;

import com.example.kastools.entity.Ticket;
import com.example.kastools.mapper.TicketMapper;
import com.example.kastools.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public List<Ticket> getTicketsBySiteId(Long siteId) {
        return ticketMapper.findBySiteId(siteId);
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketMapper.findById(ticketId);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketMapper.findAll();
    }

    @Override
    public Map<String, Object> getAllTicketsWithPaging(int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * pageSize;
        List<Ticket> tickets = ticketMapper.findAllWithPaging(offset, pageSize);
        int total = ticketMapper.countAll();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        
        result.put("list", tickets);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);
        return result;
    }

    @Override
    public List<Ticket> getTicketsBySiteIdAll(Long siteId) {
        return ticketMapper.findBySiteIdAll(siteId);
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        if (ticket.getStatus() == null) {
            ticket.setStatus(1);
        }
        if (ticket.getId() == null) {
            ticketMapper.insert(ticket);
        } else {
            ticketMapper.update(ticket);
        }
        return ticket;
    }

    @Override
    public void toggleTicketStatus(Long id) {
        ticketMapper.toggleStatus(id);
    }
}
