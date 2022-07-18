package com.app.itsm.core.ticket;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class TicketDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Optional<Long> id;

    private Optional<Integer> status;

    private Optional<Integer> priority;

    private Optional<String> requester;

    private Optional<String> technician;

    private Optional<String> title;

    private Optional<String> description;

    private Optional<String> notes;

    private Optional<Date> createdIn;

    private Optional<Date> modifiedIn;

    private Optional<Date> closedIn;

    public TicketDTO() {
        this.id = Optional.empty();
        this.status = Optional.empty();
        this.priority = Optional.empty();
        this.requester = Optional.empty();
        this.technician = Optional.empty();
        this.title = Optional.empty();
        this.description = Optional.empty();
        this.notes = Optional.empty();
        this.createdIn = Optional.empty();
        this.modifiedIn = Optional.empty();
        this.closedIn = Optional.empty();
    }

    public TicketDTO(Ticket ticket) {
        this.id = Optional.of(ticket.getId());
        this.status = Optional.of(ticket.getStatus());
        this.priority = Optional.of(ticket.getPriority());
        this.requester = Optional.of(ticket.getRequester());
        this.title = Optional.of(ticket.getTitle());
        this.description = Optional.of(ticket.getDescription());
        this.createdIn = Optional.of(ticket.getCreatedIn());
        this.modifiedIn = Optional.of(ticket.getModifiedIn());

        if (ticket.getTechnician() != null) {
            this.technician = Optional.of(ticket.getTechnician());
        }
        if (ticket.getNotes() != null) {
            this.notes = Optional.of(ticket.getNotes());
        }
        if (ticket.getClosedIn() != null) {
            this.closedIn = Optional.of(ticket.getClosedIn());
        }
    }

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }

    public Optional<Integer> getStatus() {
        return status;
    }

    public void setStatus(Optional<Integer> status) {
        this.status = status;
    }

    public Optional<Integer> getPriority() {
        return priority;
    }

    public void setPriority(Optional<Integer> priority) {
        this.priority = priority;
    }

    public Optional<String> getRequester() {
        return requester;
    }

    public void setRequester(Optional<String> requester) {
        this.requester = requester;
    }

    public Optional<String> getTechnician() {
        return technician;
    }

    public void setTechnician(Optional<String> technician) {
        this.technician = technician;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public Optional<String> getNotes() {
        return notes;
    }

    public void setNotes(Optional<String> notes) {
        this.notes = notes;
    }

    public Optional<Date> getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(Optional<Date> createdIn) {
        this.createdIn = createdIn;
    }

    public Optional<Date> getModifiedIn() {
        return modifiedIn;
    }

    public void setModifiedIn(Optional<Date> modifiedIn) {
        this.modifiedIn = modifiedIn;
    }

    public Optional<Date> getClosedIn() {
        return closedIn;
    }

    public void setClosedIn(Optional<Date> closedIn) {
        this.closedIn = closedIn;
    }

}
