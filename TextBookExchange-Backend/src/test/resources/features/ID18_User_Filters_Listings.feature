Feature: User filters textbook listings

As a User of the Textbook Exchange System
I want to filter listings using various filters
So that I can refine my search results and find the most relevant books

Background:
Given the following listings exist:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | datePosted  | course   |
  | "Data Structures" | "999-9-9-9999999-0"    | "New"           | "Available"     | "30.00"   | "2025-02-01"  | "COMP 250" |
  | "Calculus I"      | "888-8-8-8888888-0"    | "Used"          | "Available"     | "50.00"   | "2025-01-23"  | "MATH 101" | 
  | "Macroeconomics"  | "777-7-7-7777777-0"    | "New"           | "Available"     | "40.00"   | "2025-01-15"  | "ECON 201" |
  | "Algorithms"      | "666-6-6-6666666-0"    | "Used"          | "Unavailable"   | "60.00"   | "2025-01-10"  | "COMP 350" |


Scenario: User applies a price range filter (Normal Flow)

When the user filters listings with a price range between "30.00" and "50.00"
Then the system should return the following results:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Data Structures" | "999-9-9-9999999-0"    | "New"           | "Available"     | "30.00"   | "COMP 250" |
  | "Macroeconomics"  | "777-7-7-7777777-0"    | "New"           | "Available"     | "40.00"  | "ECON 201" |
  | "Calculus I"      | "888-8-8-8888888-0"    | "Used"          | "Available"     | "50.00"   | "MATH 101" |


Scenario: User filters listings by book condition (Normal Flow)

When the user applies a filter to show only "Used" books
Then the system should return the following results:
  | bookName    | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Calculus I"  | "888-8-8-8888888-0"    | "Used"          | "Available"     | "50.00"   | "MATH 101" |
  | "Algorithms"  | "666-6-6-6666666-0"    | "Used"          | "Unavailable"   | "60.00"   | "COMP 350" |


Scenario: User filters results to show only available listings

When the user applies a filter to show only "Available" listings
Then the system should return the following results:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Data Structures" | "999-9-9-9999999-0"    | "New"           | "Available"     | "30.00"   | "COMP 250" |
  | "Macroeconomics"  | "777-7-7-7777777-0"   | "New"           | "Available"     | "40.00"   | "ECON 201" |
  | "Calculus I"      | "888-8-8-8888888-0"   | "Used"          | "Available"     | "50.00"   | "MATH 101" |


Scenario: User filters listings for a specific course

When the user filters listings for the course "COMP 250"
Then the system should return the following results:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Data Structures" | "999-9-9-9999999-0"   | "New"           | "Available"     | "30.00"   | "COMP 250" |


Scenario: User filters listings by institution (Normal Flow)

When the user applies a filter to show only listings from "McGill University"
Then the system should return the following results:
  | bookName       | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Data Structures"| "999-9-9-9999999-0"    | "New"           | "Available"     | "30.00"   | "COMP 250" |
  | "Algorithms"     | "666-6-6-6666666-0"    | "Used"          | "Unavailable"   | "60.00"   | "COMP 350" |


Scenario: User applies filters that return no results (Error Flow)

When the user applies a filter to show only "New" books with a price below "20.00"
Then the system should display a message: "No listings found matching your filters"


Scenario: User applies multiple filters simultaneously (Normal Flow)

When the user applies the following filters:
  | Filter Type   | Value         |
  | "Price Range"   | "30.00 - 50.00" |
  | "Condition"     | "New"           |
  | "Availability"  | "Available"     |
Then the system should return the following results:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | course   |
  | "Data Structures" | "999-9-9-9999999-0"    | "New"           | "Available"     | "30.00"   | "COMP 250" |
  | "Macroeconomics"  | "777-7-7-7777777-0"    | "New"           | "Available"     | "40.00"   | "ECON 201" |


Scenario Outline: User enters an invalid filter value (Error Flow)

When the user applies an invalid filter <filterType> with value <invalidValue>
Then the system should display an error message: "Invalid filter input"

Examples:
  | filterType    | invalidValue  |
  | "Price Range" | "-50.00"      |
  | "Condition"   | "Almost New"  |
  | "Availability"| "Sold Out"    |
  | "Course Code" | "123ABC"      |