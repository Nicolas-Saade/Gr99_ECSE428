/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

// line 17 "../../../../../../model.ump"
// line 113 "../../../../../../model.ump"
@Entity
@Table(name = "user_account") // Didn't use "User" because it's a reserved SQL keyword
public class User extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //-----------------------

  //User Associations
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Listing> listings;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aEmail, String aUsername, String aPassword, String aPhoneNumber, Cart aCart)
  {
    super(aEmail, aUsername, aPassword, aPhoneNumber);
    reviews = new ArrayList<Review>();
    listings = new ArrayList<Listing>();
    if (!setCart(aCart))
    {
      throw new RuntimeException("Unable to create User due to aCart. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }
  protected User() {}

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Cart getCart()
  {
    return cart;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCart(Cart aNewCart)
  {
    boolean wasSet = false;
    if (aNewCart != null)
    {
      cart = aNewCart;
      cart.setUser(this);
      wasSet = true;
    }
    return wasSet;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public List<Listing> getListings() {
    return listings;
  }

  public void delete()
  {
    if (cart != null) {
      Cart cartToDelete = cart;
      cart = null;
      cartToDelete.setUser(null);
    }
    for (Review review : reviews) {
      review.delete();
    }
    reviews.clear();
    for (Listing listing : listings) {
      listing.delete();
    }
    listings.clear();
    super.delete();
  }

}