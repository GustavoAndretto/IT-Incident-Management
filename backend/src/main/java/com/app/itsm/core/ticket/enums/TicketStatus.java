package com.app.itsm.core.ticket.enums;

public enum TicketStatus {
    
    CLOSED(0, "STATUS_CLOSED"),
    OPEN(1, "STATUS_OPEN"),
    ONGOING(2, "STATUS_ONGOING");

    Integer id;

    String name;

    private TicketStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
