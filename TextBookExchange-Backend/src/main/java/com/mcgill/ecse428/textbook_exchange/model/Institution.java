/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// line 48 "../../../../../../model.ump"
// line 133 "../../../../../../model.ump"
@Entity
public class Institution
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Institution Attributes
  @Id
  private String institutionName;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  public Institution() {
  }


  public Institution(String aInstitutionName)
  {
    institutionName = aInstitutionName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setInstitutionName(String aInstitutionName)
  {
    boolean wasSet = false;
    institutionName = aInstitutionName;
    wasSet = true;
    return wasSet;
  }

  public String getInstitutionName()
  {
    return institutionName;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "institutionName" + ":" + getInstitutionName()+ "]";
  }
}