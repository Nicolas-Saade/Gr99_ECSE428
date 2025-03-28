/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

// line 45 "../../../../../../model.ump"
// line 128 "../../../../../../model.ump"
@Entity
public class Cart
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String cartId;

  @OneToOne(mappedBy = "cart")
  private User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
  private List<CartItem> cartItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cart()
  {
    cartItems = new ArrayList<CartItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getCartId() {
    return cartId;
  }

  public void setCartId(String cartId) {
    this.cartId = cartId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  public void delete()
  {
    if (cartItems != null) {
      for (CartItem item : cartItems) {
        item.delete();
      }
      cartItems.clear();
    }
  }

}