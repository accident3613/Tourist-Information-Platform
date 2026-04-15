package com.example.kastools.service;

import com.example.kastools.entity.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> getTicketsBySiteId(Long siteId);

    Ticket getTicketById(Long ticketId);

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsBySiteIdAll(Long siteId);

    Ticket saveTicket(Ticket ticket);

    void toggleTicketStatus(Long id);
}
