Feature: User marks a listing as sold

As a User of the Textbook Exchange System
I want to mark my listings as sold
So that I can indicate which books are no longer available for purchase

Background:
Given a user with email "test@example.com" and username "testuser" is logged into the system
And the following listings exist:
  | bookName        | ISBN                 | bookCondition | listingStatus | price   | datePosted  | course   |
  | mybook          | 999-9-9-9999999-0    | New           | Available     | 18.09   | 2025-02-01  | ECSE 428 |
  | mybook2         | 888-8-8-8888888-0    | Used          | Available     | 209.66  | 2025-01-23  | ECSE 429 |


Scenario: User marks their listing as sold (Normal Flow)

When the user "testuser" marks the listing with ISBN "999-9-9-9999999-0" as sold
Then the listing should be updated with:
  | bookName | ISBN              | listingStatus |
  | mybook   | 999-9-9-9999999-0 | Unavailable         |


Scenario: User tries to mark a listing as sold that has already been marked as sold (Error Flow)

Given the listing with ISBN "999-9-9-9999999-0" is already marked as "Sold"
When the user "testuser" attempts to mark the listing with ISBN "999-9-9-9999999-0" as sold again
Then the system should display an error message1 "This listing is already sold"


Scenario: User tries to mark another user’s listing as sold (Access Control)

Given another user "bob123" owns a listing with ISBN "092-5-2-1111111-9"
When the user "testuser" attempts to mark the listing with ISBN "092-5-2-1111111-9" as sold
Then the system should display an error message1 "You are not authorized to mark this listing as sold"


Scenario: User tries to mark a non-existent listing as sold (Error Flow)

When the user "testuser" attempts to mark the listing with ISBN "123-4-5-6789101-1" as sold
Then the system should display an error message1 "Listing not found"


Scenario: User marks multiple listings as sold (Normal Flow)

When the user "testuser" marks the following listings as sold:
  | ISBN              |
  | 999-9-9-9999999-0 |
  | 888-8-8-8888888-0 |
Then the listings should be updated with:
  | bookName | ISBN              | listingStatus |
  | mybook   | 999-9-9-9999999-0 | Unavailable         |
  | mybook2  | 888-8-8-8888888-0 | Unavailable         |


Scenario: System error occurs when marking a listing as sold (Error Handling)

Given the system is experiencing technical issues
When the user "testuser" attempts to mark the listing with ISBN "999-9-9-9999999-0" as sold
Then the system should display an error message1 "An error occurred while updating the listing. Please try again later."
And the listing status should remain unchanged


Scenario: Admin overrides the status of a listing (Admin Privilege)

Given an administrator is logged into the system
When the administrator marks the listing with ISBN "999-9-9-9999999-0" as sold
Then the listing should be updated with:
  | bookName | ISBN              | listingStatus |
  | mybook   | 999-9-9-9999999-0 | Unavailable         |


Scenario: User marks a sold listing back to available (Reversing a Sale)

Given the listing with ISBN "999-9-9-9999999-0" is currently marked as "Sold"
When the user "testuser" attempts to change the status back to "Available" for ISBN "999-9-9-9999999-0"
Then the system should display an error message1 "Cannot change status of a sold listing"


