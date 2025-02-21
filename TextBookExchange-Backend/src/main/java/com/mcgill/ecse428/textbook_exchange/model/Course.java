/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.mcgill.ecse428.textbook_exchange.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// line 56 "../../../../../../model.ump"
// line 143 "../../../../../../model.ump"
@Entity
public class Course
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Course Associations
  @ManyToOne
  @JoinColumn(name = "faculty_department_name")
  private Faculty faculty;

  @Id
  private String courseId;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Course(Faculty aFaculty)
  {
    if (!setFaculty(aFaculty))
    {
      throw new RuntimeException("Unable to create Course due to aFaculty. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Faculty getFaculty()
  {
    return faculty;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setFaculty(Faculty aNewFaculty)
  {
    boolean wasSet = false;
    if (aNewFaculty != null)
    {
      faculty = aNewFaculty;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    faculty = null;
  }

}