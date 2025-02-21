/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// line 108 "../../../../../../model.ump"
@Entity
@Table(name = "admin")
public class Admin extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Admin(String aEmail, String aUsername, String aPassword, String aPhoneNumber)
  {
    super(aEmail, aUsername, aPassword, aPhoneNumber);
  }
  protected Admin() {}

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}