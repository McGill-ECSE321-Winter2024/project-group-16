/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

// line 37 "model.ump"
// line 122 "model.ump"

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Payment {
  @EmbeddedId
  private Key key;
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Payment Attributes
  private int confirmationNumber;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Payment(int aConfirmationNumber) {
    confirmationNumber = aConfirmationNumber;
  }
  public Payment(Key key) {
    this.key = key;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setConfirmationNumber(int aConfirmationNumber) {
    boolean wasSet = false;
    confirmationNumber = aConfirmationNumber;
    wasSet = true;
    return wasSet;
  }

  public int getConfirmationNumber() {
    return confirmationNumber;
  }

  public void delete() {}
  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public String toString() {
    return super.toString() + "[" +
      "confirmationNumber" + ":" + getConfirmationNumber() + "]";
  }

  @Embeddable
  public static class Key implements Serializable {
    @ManyToOne
    private Customer customer; // Changed from Person to Customer
    @ManyToOne
    private ScheduledCourse scheduledCourse; // Changed from Event to ScheduledCourse

    public Key() {}

    public Key(Customer customer, ScheduledCourse scheduledCourse) {
      this.customer = customer;
      this.scheduledCourse = scheduledCourse;
    }

    public Customer getCustomer() {
      return customer;
    }

    public void setCustomer(Customer customer) {
      this.customer = customer;
    }

    public ScheduledCourse getScheduledCourse() {
      return scheduledCourse;
    }

    public void setScheduledCourse(ScheduledCourse scheduledCourse) {
      this.scheduledCourse = scheduledCourse;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Key)) {
        return false;
      }
      Key other = (Key) obj;
      return this.getCustomer().getId() == other.getCustomer().getId() &&
        this.getScheduledCourse().getId() == other.getScheduledCourse().getId();
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.getCustomer().getId(), this.getScheduledCourse().getId());
    }
  }
}