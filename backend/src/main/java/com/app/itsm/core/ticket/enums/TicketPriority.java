package com.app.itsm.core.ticket.enums;

public enum TicketPriority {
    
    LOW(0, "PRIORITY_LOW"),
    MEDIUM(1, "PRIORITY_MEDIUM"),
    HIGH(2, "PRIORITY_HIGH");

    Integer id;

    String name;

    private TicketPriority(Integer id, String name) {
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
