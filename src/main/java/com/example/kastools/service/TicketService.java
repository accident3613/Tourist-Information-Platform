package com.example.kastools.service;

import com.example.kastools.entity.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService {

    List<Ticket> getTicketsBySiteId(Long siteId);

    Ticket getTicketById(Long ticketId);

    List<Ticket> getAllTickets();

    Map<String, Object> getAllTicketsWithPaging(int page, int pageSize);

    List<Ticket> getTicketsBySiteIdAll(Long siteId);

    Ticket saveTicket(Ticket ticket);

    void toggleTicketStatus(Long id);
}
