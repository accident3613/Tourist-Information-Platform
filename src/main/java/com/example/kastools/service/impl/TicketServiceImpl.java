package com.example.kastools.service.impl;

import com.example.kastools.entity.Ticket;
import com.example.kastools.mapper.TicketMapper;
import com.example.kastools.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
