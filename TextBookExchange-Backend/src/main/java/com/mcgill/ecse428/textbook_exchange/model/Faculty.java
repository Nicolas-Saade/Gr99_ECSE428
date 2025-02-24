/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
// line 52 "../../../../../../model.ump"
// line 138 "../../../../../../model.ump"
@Entity
public class Faculty
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Faculty Attributes
  @Id
  private String departmentName;

  //Faculty Associations
  @ManyToOne
  @JoinColumn(name = "institution_name")
  private Institution institution;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Faculty()
  {
  }

  public Faculty(String aDepartmentName, Institution aInstitution)
  {
    departmentName = aDepartmentName;
    if (!setInstitution(aInstitution))
    {
      throw new RuntimeException("Unable to create Faculty due to aInstitution. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDepartmentName(String aDepartmentName)
  {
    boolean wasSet = false;
    departmentName = aDepartmentName;
    wasSet = true;
    return wasSet;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }
  /* Code from template association_GetOne */
  public Institution getInstitution()
  {
    return institution;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setInstitution(Institution aNewInstitution)
  {
    boolean wasSet = false;
    if (aNewInstitution != null)
    {
      institution = aNewInstitution;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    institution = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "departmentName" + ":" + getDepartmentName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "institution = "+(getInstitution()!=null?Integer.toHexString(System.identityHashCode(getInstitution())):"null");
  }
}