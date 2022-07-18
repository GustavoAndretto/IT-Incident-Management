package com.app.itsm.core.user.enums;

public enum UserRole {

    USER(0, "ROLE_USER"),
    TECHNICIAN(1, "ROLE_TECHNICIAN"),
    ADMIN(2, "ROLE_ADMIN");

    private Integer id;

    private String name;

    private UserRole(Integer id, String name) {
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

    public static UserRole fromId(Integer id) {
		if(id == null) {
			throw new NullPointerException("'id' is null");
		}
		
		for(UserRole x : UserRole.values()) {
			if(id.equals(x.getId())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Invalid role");
	}

}
