/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

// line 27 "../../../../../../model.ump"
// line 148 "../../../../../../model.ump"
@Entity
public class Listing
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum BookCondition { New, Used }
  public enum ListingStatus { Available, Unavailable }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Listing Attributes
  private String bookName;
  @Id
  private String ISBN;
  @Enumerated(EnumType.STRING)
  private BookCondition bookcondition; // TODO Check the default value for this enum
  @Enumerated(EnumType.STRING)
  private ListingStatus listingStatus; // TODO Check the default value for this enum
  private float price;
  private LocalDate datePosted;

  //Listing Associations
  @ManyToOne
  @JoinColumn(name = "user_email")
  private User user;
  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Listing(String aBookName, String aISBN, float aPrice, LocalDate aDatePosted, User aUser, Course aCourse)
  {
    bookName = aBookName;
    ISBN = aISBN;
    bookcondition = BookCondition.New;
    listingStatus = ListingStatus.Available;
    price = aPrice;
    datePosted = aDatePosted;
    if (!setUser(aUser))
    {
      throw new RuntimeException("Unable to create Listing due to aUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCourse(aCourse))
    {
      throw new RuntimeException("Unable to create Listing due to aCourse. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setBookName(String aBookName)
  {
    boolean wasSet = false;
    bookName = aBookName;
    wasSet = true;
    return wasSet;
  }

  public boolean setISBN(String aISBN)
  {
    boolean wasSet = false;
    ISBN = aISBN;
    wasSet = true;
    return wasSet;
  }

  public boolean setBookcondition(BookCondition aBookcondition)
  {
    boolean wasSet = false;
    bookcondition = aBookcondition;
    wasSet = true;
    return wasSet;
  }

  public boolean setListingStatus(ListingStatus aListingStatus)
  {
    boolean wasSet = false;
    listingStatus = aListingStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(float aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setDatePosted(LocalDate aDatePosted)
  {
    boolean wasSet = false;
    datePosted = aDatePosted;
    wasSet = true;
    return wasSet;
  }

  public String getBookName()
  {
    return bookName;
  }

  public String getISBN()
  {
    return ISBN;
  }

  public BookCondition getBookcondition()
  {
    return bookcondition;
  }

  public ListingStatus getListingStatus()
  {
    return listingStatus;
  }

  public float getPrice()
  {
    return price;
  }

  public LocalDate getDatePosted()
  {
    return datePosted;
  }
  /* Code from template association_GetOne */
  public User getUser()
  {
    return user;
  }
  /* Code from template association_GetOne */
  public Course getCourse()
  {
    return course;
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
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCourse(Course aNewCourse)
  {
    boolean wasSet = false;
    if (aNewCourse != null)
    {
      course = aNewCourse;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    user = null;
    course = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "bookName" + ":" + getBookName()+ "," +
            "ISBN" + ":" + getISBN()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bookcondition" + "=" + (getBookcondition() != null ? !getBookcondition().equals(this)  ? getBookcondition().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "listingStatus" + "=" + (getListingStatus() != null ? !getListingStatus().equals(this)  ? getListingStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "datePosted" + "=" + (getDatePosted() != null ? !getDatePosted().equals(this)  ? getDatePosted().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "course = "+(getCourse()!=null?Integer.toHexString(System.identityHashCode(getCourse())):"null");
  }
}