Feature: User updates a listing

As a User of the Textbook Exchange System
I want to update my textbook listings
So that I can modify the price, condition, availability, or other details as needed

Background:
Given For updating listings: a user with email "myuser@mail.com" and username "myusername" is logged into the system
And For updating listings: the following listings exist:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | datePosted  | course   |
  | mybook          | 999-9-9-9999999-0    | New           | Available     | 18.09   | 2025-02-01  | ECSE 428 |
  | mybook2         | 888-8-8-8888888-0    | Used          | Available     | 209.66  | 2025-01-23  | ECSE 429 |


Scenario: User updates the price of a textbook listing (Normal Flow)

When For updating listings: the user "myusername" updates the price of the listing with ISBN "999-9-9-9999999-0" to "25.99"
Then For updating listings: the listing should be updated with:
  | bookName  | ISBN              | price  |
  | mybook    | 999-9-9-9999999-0 | 25.99  |


Scenario Outline: User updates the book condition of a listing

When For updating listings: the user "myusername" updates the book condition of the listing with ISBN "<ISBN>" to "<newCondition>"
Then For updating listings: the listing should be updated with:
  | bookName | ISBN  | bookCondition |
  | <bookName> | <ISBN> | <newCondition> |

Examples:
  | bookName | ISBN              | newCondition |
  | mybook   | 999-9-9-9999999-0 | Used        |
  | mybook2  | 888-8-8-8888888-0 | New         |


Scenario Outline: User updates the availability of a listing

When For updating listings: the user "myusername" updates the listing status of the listing with ISBN "<ISBN>" to "<newStatus>"
Then For updating listings: the listing should be updated with:
  | bookName | ISBN  | listingStatus |
  | <bookName> | <ISBN> | <newStatus> |

Examples:
  | bookName | ISBN              | newStatus    |
  | mybook   | 999-9-9-9999999-0 | Unavailable  |
  | mybook2  | 888-8-8-8888888-0 | Available    |


Scenario: User updates the course for a listing

When For updating listings: the user "myusername" updates the course for the listing with ISBN "999-9-9-9999999-0" to "MATH 202"
Then For updating listings: the listing should be updated with:
  | bookName | ISBN              | course    |
  | mybook   | 999-9-9-9999999-0 | MATH 202  |


Scenario Outline: User enters an invalid price when updating a listing (Error Flow)

When For updating listings: the user "myusername" updates the price of the listing with ISBN "<ISBN>" to "<invalidPrice>"
Then For updating listings: the system should display an error message: "Invalid Price"
And For updating listings: the listing should remain unchanged

Examples:
  | bookName | ISBN              | invalidPrice |
  | mybook   | 999-9-9-9999999-0 | -5.00       |
  | mybook2  | 888-8-8-8888888-0 | 0.00        |


Scenario Outline: User enters an invalid book condition (Error Flow)

When For updating listings: the user "myusername" updates the book condition of the listing with ISBN "<ISBN>" to "<invalidCondition>"
Then For updating listings: the system should display an error message: "Invalid Book Condition"
And For updating listings: the listing should remain unchanged

Examples:
  | bookName | ISBN              | invalidCondition |
  | mybook   | 999-9-9-9999999-0 | Almost New      |
  | mybook2  | 888-8-8-8888888-0 | Slightly Used   |


Scenario: User tries to update a non-existent listing (Error Flow)

When For updating listings: the user "myusername" attempts to update the listing with ISBN "123-4-5-6789101-1"
Then For updating listings: the system should display an error message: "Listing not found"


Scenario Outline: User attempts to update a listing with an invalid course (Error Flow)

When For updating listings: the user "myusername" updates the course for the listing with ISBN "<ISBN>" to "<invalidCourse>"
Then For updating listings: the system should display an error message: "Invalid Course Code"
And For updating listings: the listing should remain unchanged

Examples:
  | bookName | ISBN              | invalidCourse  |
  | mybook   | 999-9-9-9999999-0 | Course101      |
  | mybook2  | 888-8-8-8888888-0 | Intro to Math  |


Scenario Outline: User enters an invalid date when updating a listing

When For updating listings: the user "myusername" updates the date posted for the listing with ISBN "<ISBN>" to "<invalidDate>"
Then For updating listings: the system should display an error message: "Invalid Date Format"
And For updating listings: the listing should remain unchanged

Examples:
  | bookName | ISBN              | invalidDate  |
  | mybook   | 999-9-9-9999999-0 | 32-13-2025   |
  | mybook2  | 888-8-8-8888888-0 | 0000-00-00   |


Scenario: User attempts to update another user's listing

Given For updating listings: another user "otheruser@mail.com" owns a listing with ISBN "092-5-2-1111111-9"
When For updating listings: the user "myusername" attempts to update the price of the listing with ISBN "092-5-2-1111111-9" to "30.00"
Then For updating listings: the system should display an error message: "You are not authorized to update this listing"


