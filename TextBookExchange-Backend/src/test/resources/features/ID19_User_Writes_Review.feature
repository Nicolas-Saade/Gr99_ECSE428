Feature: User writes a review for a seller

As a User of the Textbook Exchange System
I want to write reviews for sellers
So that I can share my experience and help other buyers make informed decisions

Background:
Given a user with email "buyer@mail.com" and username "buyer123" is logged into the system
And the following users exist:
  | email                 | username   |
  | seller001@mail.com    | seller001  |
  | seller002@mail.com    | seller002  |


Scenario: User writes a review for a seller (Normal Flow)

When the user "buyer123" writes a review for the seller "seller001@mail.com" with:
  | rating | message                     |
  | 5      | "Great experience, fast shipping!" |
Then the review should be saved with:
  | reviewId | user_email        | rating | message                            |
  | <UUID>   | buyer@mail.com    | 5      | "Great experience, fast shipping!" |


Scenario: User tries to write multiple reviews for the same seller (Error Flow)

Given the user "buyer123" has already written a review for the seller "seller001@mail.com"
When they attempt to write another review with:
  | rating | message                          |
  | 3      | "Changed my mind, not so great." |
Then the system should display a review error message: "You have already reviewed this seller"


Scenario Outline: User enters an invalid rating when writing a review (Error Flow)

When the user "buyer123" writes a review for the seller "<seller_email>" with:
  | rating          | message                      |
  | <invalidRating> | "<message>"                  |
Then the system should display a review error message: "Invalid rating. Please provide a rating between 1 and 5."
And the review should not be saved

Examples:
  | seller_email         | invalidRating | message                      |
  | seller001@mail.com   | 0             | "Worst seller ever."         |
  | seller002@mail.com   | 6             | "Amazing, deserves 6 stars!" |


Scenario: User writes a review without a message (Normal Flow)

When the user "buyer123" writes a review for the seller "seller001@mail.com" with:
  | rating | message |
  | 4      | ""      |
Then the review should be saved with:
  | reviewId | user_email        | rating | message |
  | <UUID>   | buyer@mail.com    | 4      | ""      |


Scenario: User tries to review a seller who is not in the system (Error Flow)

When the user "buyer123" writes a review for the seller "fakeuser@mail.com" with:
  | rating | message                 | 
  | 4      | "Nice transaction!"     |
Then the system should display a review error message: "Seller not found"


Scenario: User tries to write a review for themselves (Error Flow)

Given the user "buyer123" is logged in
When they attempt to write a review for themselves with:
  | rating | message                |
  | 5      | "I'm the best seller!" |
Then the system should display a review error message: "You cannot review yourself"


Scenario: User successfully updates their review (Normal Flow)

Given the user "buyer123" has already written a review for the seller "seller001@mail.com"
When they update their review with:
  | newRating | newMessage                           |
  | 3         | "Condition was worse than expected." |
Then the review should be updated with:
  | reviewId | user_email        | rating | message                              |
  | <UUID>   | buyer@mail.com    | 3      | "Condition was worse than expected." |


Scenario: User tries to update a review they never wrote (Error Flow)

When the user "buyer123" attempts to update a review for the seller "seller002@mail.com" with:
  | newRating | newMessage                |
  | 5         | "Great experience!"       |
Then the system should display a review error message: "You have not reviewed this seller"



Scenario: Admin deletes a review due to inappropriate content

Given the following review exists:
  | reviewId | user_email        | rating | message                       |
  | <UUID>   | buyer@mail.com    | 1      | "This seller is a scammer!!"  |
When an administrator removes the review with reviewId "<UUID>"
Then the review should be deleted
And the system should log the action: "Review <UUID> was removed due to guideline violation."


