Feature:Validate the complete Booking lifecycle — Creation, Update, Retrieval, and Deletion

  @Creation @Positive
  Scenario Outline: Hotel Room Booking creation with various booking test data
    Given user creates a room booking with following details
      | FirstName   | LastName   | EmailID   | PhoneNumber   | roomType   | depositPaid   |
      | <firstName> | <lastName> | <emailID> | <phoneNumber> | <roomType> | <depositPaid> |
    Then user should book the room successfully with status as 201

    Examples:
      | firstName         | lastName                      | emailID                 | phoneNumber | roomType | depositPaid |
      # Common valid names
      | John              | Smith                         | john.smith@test.com     | 09876505678 | Double   | false       |
      # Min valid names (3 chars each)
      | Ana               | Lee                           | ana.lee@test.com        | 09876501234 | Single   | false       |
      # Mid-range name lengths
      | Priya             | Patel                         | priya.patel@test.com    | 09876512345 | Suite    | false       |
      # Max valid lengths
      | EighteenCharacte | WordWithThirtyLettersRequire | maria.gonzalez@test.com | 09876523456 | Single   | true        |
      # First Name (Min) - Last Name (Max)
      | Ana               | WordWithThirtyLettersRequire | ana.long@test.com       | 09876502345 | Double   | true        |
      # First Name (Max) - Last Name (Min)
      | EighteenCharacte | Lee                           | eighteen.lee@test.com   | 09876503456 | Suite    | true        |

  @Creation @Negative
  Scenario Outline: Hotel Room Booking creation with invalid or edge-case booking data
    Given user creates a room booking with following details
      | FirstName   | LastName   | EmailID   | PhoneNumber   | Check-in  | Check-Out  | roomType   | depositPaid   |
      | <firstName> | <lastName> | <emailID> | <phoneNumber> | <checkIn> | <checkout> | <roomType> | <depositPaid> |
    Then user should receive an appropriate error message with status as 400
    And user should receive an error response "<error>"
    Examples:
      | firstName           | lastName                        | emailID                | phoneNumber            | checkIn    | checkout   | roomType | error                               | depositPaid |
      |                     | test                            | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | Firstname should not be blank       | false       |
      | test                |                                 | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | Lastname should not be blank        | false       |
      | ch                  | one                             | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | size must be between 3 and 18       | false       |
      | David               | aa                              | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | size must be between 3 and 30       | false       |
      | NineteenLetterNamee | test                            | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Double   | size must be between 3 and 18       | false       |
      | David               | ThirtyOneCharacterLastNameExamm | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Double   | size must be between 3 and 30       | false       |
      | Maria               | Gonzalez                        | #$%#$%#$%#$%#%$        | 0001234ABCD            | 2025-12-15 | 2025-12-20 | Double   | must be a well-formed email address | false       |
      | John                | Smith                           | john.smith@outlook.com | 76543210               | 2025-11-10 | 2025-11-15 | Suite    | size must be between 11 and 21      | true        |
      | John                | Smith                           | john.smith@outlook.com | 2389890987673876567891 | 2025-11-10 | 2025-11-15 | Suite    | size must be between 11 and 21      | true        |

  @RoomEnquiry @Positive
  Scenario Outline: Perform hotel room enquiry by room type and validate available details and features
    Given the user performs a room search by room type as "<type>"
    Then user should receives a response with status code 200
    Examples:
      | type   |
      | Single |
      | Double |
      | Suite  |

  @RoomEnquiry @Positive
  Scenario: Perform hotel room enquiry by check-in and check-out dates and validate room available details
    Given user generates random check-in and check-out dates
    When user sends a request to search available rooms using generated dates
    Then user should receives a response with status code 200

  @RoomEnquiry @Positive
  Scenario Outline: Verify all available rooms and their details
    Given the user sends a request to retrieve all available rooms
    Then user should receives a response with status code 200
    Then the user should see the following room types with correct accessibility, image, description, features, and price information
      | <type> | <accessible> | <image> | <description> | <features> | <price> |
    Examples:
      | type   | accessible | image             | description                                                                                                                                         | features        | price |
      | Single | true       | /images/room1.jpg | Aenean porttitor mauris sit amet lacinia molestie. In posuere accumsan aliquet. Maecenas sit amet nisl massa. Interdum et malesuada fames ac ante.  | TV,WiFi,Safe    | 100   |
      | Double | true       | /images/room2.jpg | Vestibulum sollicitudin, lectus ac mollis consequat, lorem orci ultrices tellus, eleifend euismod tortor dui egestas erat. Phasellus et ipsum nisl. | TV,Radio,Safe   | 150   |
      | Suite  | true       | /images/room3.jpg | Etiam metus metus, fringilla ac sagittis id, consequat vel neque. Nunc commodo quis nisl nec posuere. Etiam at accumsan ex.                         | Radio,WiFi,Safe | 225   |


  @EndToEnd @Positive @Regression
  Scenario: Perform an end-to-end room booking flow and validate all operations
    Given user creates a room booking with following details
      | FirstName | LastName | EmailID              | PhoneNumber | roomType | depositPaid |
      | bhargava  | che      | Bhar.che@outlook.com | 09876543210 | Single   | false       |
    Then user should book the room successfully with status as 201
    When the user retrieves the booking ID from the booking confirmation response
    #------Room Confirmation Validation in Summary & Report-----#
    When the user retrieves the room summary details by room type "Single"
    Then the user confirms that the created or updated booking details appear correctly in the room summary
      | FirstName | LastName | roomType |
      | bhargava  | che      | Single   |
    When the user retrieves the booking report
    Then the user confirms that the same user details are displaying in the booking report:
      | title        |
      | bhargava che |
    #------------------- Updating ------------------------------#
    And the user updates the booking using the retrieved booking ID with the following details
      | FirstName | LastName | roomType | depositPaid |
      | John      | Cherry   | Double   | true        |
    Then user should receives a response with status code 200
    #------Room Confirmation Validation in Summary & Report-----#
    When the user retrieves the booking report
    Then the user confirms that the same user details are displaying in the booking report:
      | title       |
      | John Cherry |
    When the user retrieves the room summary details by room type "Double"
    Then the user confirms that the created or updated booking details appear correctly in the room summary
      | FirstName | LastName | roomType |
      | John      | Cherry   | Double   |
       #------------------- Deletion The Booking  -------------------#
    When the user deletes the booking using the booking ID
    Then user should receives a response with status code 200
     #------------------- Retrieve Deleted Booking ---------------#
    Then the user searches for the deleted booking
    Then user should receive an error status as 404
    And user should receive an error as "Failed to fetch booking"


  @EndToEnd @Positive @Regression
  Scenario: Perform an room booking and deletion flow
    Given user creates a room booking with following details
      | FirstName | LastName | EmailID              | PhoneNumber | roomType | depositPaid |
      | bhargava  | che      | Bhar.che@outlook.com | 09876543210  | Single   | true        |
    Then user should book the room successfully with status as 201
    When the user retrieves the booking ID from the booking confirmation response
    #------------------- Deletion The Booking  -------------------#
    When the user deletes the booking using the booking ID
    Then user should receives a response with status code 200
     #------------------- Retrieve Deleted Booking ---------------#
    Then the user searches for the deleted booking
    Then user should receive an error status as 404
    And user should receive an error as "Failed to fetch booking"
