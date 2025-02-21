/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// line 40 "../../../../../../model.ump"
// line 123 "../../../../../../model.ump"
@Entity
public class CartItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CartItem Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String cartItemId;

  //CartItem Associations
  @ManyToOne
  @JoinColumn(name = "listing_id")
  private Listing listing;

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CartItem( Listing aListing, Cart aCart)
  {

    if (!setListing(aListing))
    {
      throw new RuntimeException("Unable to create CartItem due to aListing. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setCart(aCart))
    {
      throw new RuntimeException("Unable to create CartItem due to aCart. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------


  public String getcartItemId()
  {
    return cartItemId;
  }
  /* Code from template association_GetOne */
  public Listing getListing()
  {
    return listing;
  }
  /* Code from template association_GetOne */
  public Cart getCart()
  {
    return cart;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setListing(Listing aNewListing)
  {
    boolean wasSet = false;
    if (aNewListing != null)
    {
      listing = aNewListing;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCart(Cart aNewCart)
  {
    boolean wasSet = false;
    if (aNewCart != null)
    {
      cart = aNewCart;
      cart.getCartItems().add(this);
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    if (cart != null) {
      cart.getCartItems().remove(this);
    }
    listing = null;
    cart = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "cartItemId" + ":" + getcartItemId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "listing = "+(getListing()!=null?Integer.toHexString(System.identityHashCode(getListing())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "cart = "+(getCart()!=null?Integer.toHexString(System.identityHashCode(getCart())):"null");
  }
}