Feature: Logging into existing account

As a registered user
I would like to log into my account
So that I can access my listings, view other listings, update my account, and leave reviews

As an admin
I would like to log into my admin account
So that I can access the admin dashboard

Scenario Outline: Registered user logs into account (Normal Flow)

Given a registered user or admin with email <email> and password <password> exists
When the user enters valid credentials and submits the login request
Then the user is logged in successfully

Examples:
| email                 | password |
| "bob123@mcgill.ca"      | "120329"   |
| "Test111@concordia.ca"  | "test123"  |  
| "Joe090@udem.ca"        | "joe381"   |
| "Alice@mcgill.ca"       | "a0192"    |

Scenario Outline: User login fails due to incorrect password (Error Flow)

Given a registered user with <email> exists
When they enter an incorrect password <password>
Then a "Incorrect password" message is displayed
Examples:
| email                | password  |
|"bob123@mcgill.ca"     | "wrongpass" |
| "Test111@concordia.ca" | "testingpa" |                                                                
| "Joe090@udem.ca"       | "12903i"    |
| "Alice@mcgill.ca"      | "incorrect" |


Scenario Outline: User login fails due to unregistered email (Error Flow)

Given no account exists for email <email>
When they attempt to login with <password>
Then a "No account with this email exists" message is displayed
Examples:
| email               | password |
| "notreg@mcgill.ca"    | "test1"    |
| "test@udem.ca"        | "test2"    |
| "wrong@concordia.ca"  | "tes3"     |


