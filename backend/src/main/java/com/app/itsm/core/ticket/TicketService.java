package com.app.itsm.core.ticket;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.itsm.core.ticket.enums.TicketStatus;

@Service
public class TicketService {

  @Autowired
  TicketRepository repository;

  public List<Ticket> listAll() {
    return repository.findAll();
  }

  public TicketDTO create(TicketDTO ticketDTO) {
    if (ticketDTO.getRequester().isEmpty()) {
      throw new Error("Requester cannot be null!");
    } else if (ticketDTO.getPriority().isEmpty()) {
      throw new Error("Priority cannot be null!");
    } else if (ticketDTO.getStatus().isEmpty()) {
      throw new Error("Status cannot be null!");
    } else if (ticketDTO.getTitle().isEmpty()) {
      throw new Error("Title cannot be null!");
    } else if (ticketDTO.getDescription().isEmpty()) {
      throw new Error("Description cannot be null!");
    }

    Ticket ticket = new Ticket();

    ticket.setRequester(ticketDTO.getRequester().get());
    ticket.setPriority(ticketDTO.getPriority().get());
    ticket.setStatus(ticketDTO.getStatus().get());
    ticket.setTitle(ticketDTO.getTitle().get());
    ticket.setDescription(ticketDTO.getDescription().get());

    if (ticketDTO.getTechnician().isPresent())
      ticket.setTechnician(ticketDTO.getTechnician().get());

    if (ticketDTO.getNotes().isPresent())
      ticket.setNotes(ticketDTO.getNotes().get());

    return new TicketDTO(repository.save(ticket));
  }

  public TicketDTO update(TicketDTO ticketDTO) {
    if (ticketDTO.getId().isEmpty()) {
      throw new Error("Id is null in TicketDTO");
    }

    Ticket ticket = repository.findById(ticketDTO.getId().get())
        .orElseThrow(() -> new Error(String.format("Ticket not found with id: %d", ticketDTO.getId())));

    if (ticketDTO.getRequester().isPresent())
      ticket.setRequester(ticketDTO.getRequester().get());

    if (ticketDTO.getPriority().isPresent())
      ticket.setPriority(ticketDTO.getPriority().get());

    if (ticketDTO.getStatus().isPresent()) {
      ticket.setStatus(ticketDTO.getStatus().get());

      if (ticketDTO.getStatus().get() == TicketStatus.CLOSED.getId()) {
        ticket.setClosedIn(new Date());
      }
    }

    if (ticketDTO.getTitle().isPresent())
      ticket.setTitle(ticketDTO.getTitle().get());

    if (ticketDTO.getDescription().isPresent())
      ticket.setDescription(ticketDTO.getDescription().get());

    if (ticketDTO.getNotes().isPresent())
      ticket.setNotes(ticketDTO.getNotes().get());

    if (ticketDTO.getTechnician().isPresent())
      ticket.setDescription(ticketDTO.getTechnician().get());

    return new TicketDTO(repository.save(ticket));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }
}
