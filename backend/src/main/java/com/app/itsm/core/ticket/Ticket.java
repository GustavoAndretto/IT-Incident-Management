package com.app.itsm.core.ticket;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true)
  private Long id;

  @Column(nullable = false)
  Integer status;

  @Column(nullable = false)
  Integer priority;

  @Column(nullable = false, length = 128)
  String requester;

  @Column(nullable = true, length = 128)
  String technician;

  @Column(nullable = false, length = 128)
  private String title;

  @Column(nullable = false, length = 2000)
  private String description;

  @Column(nullable = true, length = 2000)
  private String notes;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date createdIn;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Date modifiedIn;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true)
  private Date closedIn;

  public Ticket() {
  }

  public Ticket(Integer status, Integer priority, String requester, String technician, String title, String description,
      String notes) {
    this.status = status;
    this.priority = priority;
    this.requester = requester;
    this.technician = technician;
    this.title = title;
    this.description = description;
    this.notes = notes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public String getRequester() {
    return requester;
  }

  public void setRequester(String requester) {
    this.requester = requester;
  }

  public String getTechnician() {
    return technician;
  }

  public void setTechnician(String technician) {
    this.technician = technician;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Date getCreatedIn() {
    return createdIn;
  }

  public void setCreatedIn(Date createdIn) {
    this.createdIn = createdIn;
  }

  public Date getModifiedIn() {
    return modifiedIn;
  }

  public void setModifiedIn(Date modifiedIn) {
    this.modifiedIn = modifiedIn;
  }

  public Date getClosedIn() {
    return closedIn;
  }

  public void setClosedIn(Date closedIn) {
    this.closedIn = closedIn;
  }

}
