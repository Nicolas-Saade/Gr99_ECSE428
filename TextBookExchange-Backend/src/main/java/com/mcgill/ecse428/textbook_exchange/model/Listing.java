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

  public Listing()
  {
  }

  public Listing(String aBookName, String aISBN, float aPrice, LocalDate aDatePosted, User aUser, Course aCourse)
  {
    bookName = aBookName;
    ISBN = aISBN;
    bookcondition = BookCondition.New;
    listingStatus = ListingStatus.Available;
    price = aPrice;
    datePosted = aDatePosted;
    
    try {
        setUser(aUser);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Cannot create due to null user");
    }
    
    try {
        setCourse(aCourse);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Cannot create due to null course");
    }
    
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Listing(String iSBN2, String bookName2, BookCondition condition, float price2, LocalDate datePosted2) {
    //TODO Auto-generated constructor stub
}

public void setBookName(String aBookName)
  {
    this.bookName = aBookName;
  }

  public void setISBN(String aISBN)
  {
    this.ISBN = aISBN;
  }

  public void setBookcondition(BookCondition aBookcondition)
  {
    this.bookcondition = aBookcondition;
  }

  public void setListingStatus(ListingStatus aListingStatus)
  {
    this.listingStatus = aListingStatus;
  }

  public void setPrice(float aPrice)
  {
    this.price = aPrice;
  }

  public void setDatePosted(LocalDate aDatePosted)
  {
    this.datePosted = aDatePosted;
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

  public String getListingStatusString()
  {
    if (listingStatus == ListingStatus.Available) {
      return "Available";
    } else if (listingStatus == ListingStatus.Unavailable) {
      return "Unavailable";
    } else {
      return null;
    }
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
  public void setUser(User aNewUser)
  {
    this.user = aNewUser;
  }
  /* Code from template association_SetUnidirectionalOne */
  public void setCourse(Course aNewCourse)
  {
    this.course = aNewCourse;
  }

  public void setListingStatus(String status){
    if (status.equals("Available")) {
      this.listingStatus = ListingStatus.Available;
    } else if (status.equals("Unavailable")) {
      this.listingStatus = ListingStatus.Unavailable;
    } else {
      throw new IllegalArgumentException("Invalid status: " + status);
    }
  }

  public void setBookcondition(String condition){
    if (condition.equals("New")) {
      this.bookcondition = BookCondition.New;
    } else if (condition.equals("Used")) {
      this.bookcondition = BookCondition.Used;
    } else {
      throw new IllegalArgumentException("Invalid condition: " + condition);
    }
    }


    public void removeCourse()
    {
       this.course = null;
    }


  public void delete()
  {
  
    this.course.delete();
    this.user.delete();
    
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