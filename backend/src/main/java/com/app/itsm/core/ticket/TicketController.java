package com.app.itsm.core.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

  @Autowired
  TicketService ticketService;

  @PostMapping("/create")
  public TicketDTO create(@RequestBody TicketDTO ticketDTO) {
    return ticketService.create(ticketDTO);
  }

  @PutMapping("/update")
  public TicketDTO update(@RequestBody TicketDTO ticketDTO) {
    return ticketService.update(ticketDTO);
  }

  @GetMapping("/list")
  public List<Ticket> ticketList() {
    return ticketService.listAll();
  }

  @DeleteMapping("/delete")
  public void delete(@RequestParam Long id) {
    ticketService.delete(id);
  }

}
