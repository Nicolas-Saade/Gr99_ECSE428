/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// line 22 "../../../../../../model.ump"
// line 118 "../../../../../../model.ump"
@Entity
@Table(name = "review")
public class Review
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  @Column(nullable = false)
  private int rating;
  
  @Column
  private String message;

  //Review Associations
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_email", nullable = false)
  private User user;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String reviewId;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(int aRating, String aMessage, User aUser)
  {
    rating = aRating;
    message = aMessage;
    if (!setUser(aUser))
    {
      throw new RuntimeException("Unable to create Review due to aUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRating(int aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setMessage(String aMessage)
  {
    boolean wasSet = false;
    message = aMessage;
    wasSet = true;
    return wasSet;
  }

  public int getRating()
  {
    return rating;
  }

  public String getMessage()
  {
    return message;
  }
  /* Code from template association_GetOne */
  public User getUser()
  {
    return user;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setUser(User aNewUser)
  {
    boolean wasSet = false;
    if (aNewUser != null)
    {
      user = aNewUser;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    user = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "rating" + ":" + getRating()+ "," +
            "message" + ":" + getMessage()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null");
  }
}