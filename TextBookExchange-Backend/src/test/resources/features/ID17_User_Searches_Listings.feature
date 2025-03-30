Feature: User searches for textbook listings

As a User of the Textbook Exchange System
I want to search for listings using various filters
So that I can find the books I need more easily

Background:
Given the following listings exist:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | datePosted  | course   |
  | "Data Structures" | "999-9-9-9999999-0"    | New           | Available     | 30.00   | 2025-02-01  | COMP 250 |
  | "Calculus I"      | "888-8-8-8888888-0"    | Used          | Available     | 50.00   | 2025-01-23  | MATH 101 |
  | "Macroeconomics"  | "777-7-7-7777777-0"    | New           | Available     | 40.00   | 2025-01-15  | ECON 201 |
  | "Algorithms"      | "666-6-6-6666666-0"    | Used          | Unavailable   | 60.00   | 2025-01-10  | COMP 350 |


Scenario: User searches for a book by title (Normal Flow)

When the user searches for listings with the book title <bookName>
Then the system should return the listing associated with the book title <bookName>
Examples:
  | bookName        |
  | "Data Structures" |
  | "Calculus I"      |
  | "Macroeconomics"  |
  | "Algorithms"      |


Scenario: User searches for a book by ISBN (Normal Flow)
  When the user searches for listings with the ISBN <ISBN>
  Then the system should return the listing associated with the ISBN <ISBN>
  Examples:
    | ISBN                      |
    | "999-9-9-9999999-0"       |
    | "888-8-8-8888888-0"       |
    | "777-7-7-7777777-0"       |
    | "666-6-6-6666666-0"       |

Scenario: User searches for books required for a course (Normal Flow)
  When the user searches for listings with the course code <course>
  Then the system should return listings associated with the course code <course>
  Examples: 
    | course     |
    | "COMP 250" |
    | "MATH 101" |
    | "ECON 201" |
    | "COMP 350" |



Scenario: User filters listings by price range (Normal Flow)

When the user filters listings with a price range between 30.00 and 50.00
Then the system should return listings with a price range between 30.00 and 50.00


Scenario: User filters search results to show only available books

When the user applies a filter to show only Available listings
Then the system should return only the Available listings


Scenario: User filters listings by book condition (Normal Flow)

When the user filters listings to show only Used books
Then the system should return only listings with Used books


Scenario: User searches for a book that is not listed (Error Flow)

Given the book title <bookName> does not exist in the system
When the user searches for listings with the book title <bookName>
Then the system should display a message: "No listings found for '<bookName>'"
Examples:
  | bookName        |
  | "Nonexistent Book" |
  | "Unknown Title"   |
  | "Not Listed Book"  |
  | "Fake Book"       |


Scenario: User enters an invalid ISBN format (Error Handling)

Given the user enters an invalid ISBN format <ISBN>
When the user searches for listings with the ISBN <ISBN>
Then the system should display a message: "Listing not found"
Examples:
  | ISBN                      |
  | "999-9-9-9999999"         |
  | "888-8-8-8888888"         |
  | "777-7-7-7777777"         |
  | "666-6-6-666666"          |



Scenario: User submits an empty search query (Error Flow)

When the user submits a search without entering any filters or keywords
Then the system should display a message: "Please enter a search term or select a filter."



