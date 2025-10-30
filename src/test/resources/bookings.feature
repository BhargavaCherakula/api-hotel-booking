Feature:Validate the complete Booking lifecycle — Creation, Update, Retrieval, and Deletion

  Background:
    Given A valid admin user retrieves the authentication token successfully

  @Creation @Positive
  Scenario Outline: Hotel Room Booking creation with various booking test data
    Given user creates a room booking with following details
      | <firstName> | <lastName> | <emailID> | <phoneNumber> | <checkin> | <checkout> | <roomType> |
    Then user should book the room successfully with status as 201

    Examples:
      | firstName          | lastName                       | emailID                  | phoneNumber | checkin    | checkout   | roomType |
      | John               | Smith                          | john.smith@outlook.com   | 09876543210 | 2025-11-10 | 2025-11-15 | Single   |
      | bbbbbbbbbbbbbbbbbb | pppppppppppppppppppppppppppppp | priya.patel@live.com     | 09876501234 | 2025-12-01 | 2025-12-05 | Double   |
      | Ahm                | pppppppppppppppppppppppppppppp | ahmed.khan@gmail.com     | 09876009876 | 2025-11-20 | 2025-11-25 | Suite    |
      | bbbbbbbbbbbbbbbbbb | Gon                            | maria.gonzalez@yahoo.com | 09876123456 | 2025-12-15 | 2025-12-20 | Single   |

  @Creation @Negative
  Scenario Outline: Hotel Room Booking creation with invalid or edge-case booking data
    Given user creates a room booking with following details
      | <firstName> | <lastName> | <emailID> | <phoneNumber> | <checkin> | <checkout> | <roomType> |
    Then user should receive an appropriate error message with status as 400
    And user should receive an error response "<error>"
    Examples:
      | firstName           | lastName                        | emailID                | phoneNumber            | checkin    | checkout   | roomType | error                               |
      |                     | test                            | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | Firstname should not be blank       |
      | test                |                                 | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | Lastname should not be blank        |
      | ch                  | one                             | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | size must be between 3 and 18       |
      | David               | aa                              | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Single   | size must be between 3 and 30       |
      | aaaaaaaaaaaaaaaaaaa | test                            | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Double   | size must be between 3 and 18       |
      | David               | ppppppppppppppppppppppppppppppp | test@gmail.com         | 09876543210            | 2025-11-10 | 2025-11-15 | Double   | size must be between 3 and 30       |
      | Maria               | Gonzalez                        | #$%#$%#$%#$%#%$        | 0001234ABCD            | 2025-12-15 | 2025-12-20 | Double   | must be a well-formed email address |
      | Cherry              | Lee                             | cherry.lee@gmail.com   | 09878987890            |            | 2025-12-31 | Double   | must not be null                    |
      | Cherry              | Lee                             | cherry.lee@gmail.com   | 09878987890            | 2025-01-01 |            | Suite    | must not be null                    |
      | Cherry              | Lee                             | cherry.lee@gmail.com   | 09878987890            | 2025-01-01 | 02-01-2025 | Suite    | Failed to create booking            |
      | John                | Smith                           | john.smith@outlook.com | 76543210               | 2025-11-10 | 2025-11-15 | Suite    | size must be between 11 and 21      |
      | John                | Smith                           | john.smith@outlook.com | 2389890987673876567891 | 2025-11-10 | 2025-11-15 | Suite    | size must be between 11 and 21      |

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
  Scenario Outline: Perform hotel room enquiry by check-in and check-out dates and validate room available details
    Given user searches for available rooms between the specified check-in and check-out dates
      | <check-in> | <check-out> |
    Then user should receives a response with status code 200
    Examples:
      | check-in   | check-out  |
      | 2025-12-01 | 2025-12-31 |
      | 2026-01-01 | 2026-01-10 |

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
    Given A valid admin user retrieves the authentication token successfully
    Then user creates a room booking with following details
      | FirstName | LastName | EmailID              | PhoneNumber | Check-in   | Check-Out  | roomType |
      | bhargava  | che      | Bhar.che@outlook.com | 9876543210  | 2025-12-10 | 2025-12-15 | Single   |
    Then user should book the room successfully with status as 201
    When the user retrieves the booking ID from the booking confirmation response
    #------Room Confirmation Validation in Summary & Report-----#
    And the admin verifies that the updated user details are correctly reflected in the room summary
      | FirstName | LastName | EmailID              | PhoneNumber | Check-in   | Check-Out  | roomType |
      | bhargava  | che      | Bhar.che@outlook.com | 9876543210  | 2025-12-10 | 2025-12-15 | Single   |
    Then the admin confirms that the same user details are updated in the booking report:
      | FirstName | LastName | EmailID              | PhoneNumber | Check-in   | Check-Out  | roomType |
      | bhargava  | che      | Bhar.che@outlook.com | 9876543210  | 2025-12-10 | 2025-12-15 | Single   |
    #------------------- Updating ------------------------------#
    And the user updates the booking using the retrieved booking ID with the following details
      | FirstName | LastName | EmailID              | PhoneNumber | Check-in   | Check-Out  | roomType |
      | Bhargava  | Che      | Bhar.che@outlook.com | 9876543210  | 2025-12-10 | 2025-12-15 | Single   |
    Then the booking should be updated successfully with status code 200
    #------------------- Deletion & Cross Verify  ----------------#
    When the user deletes the booking using the booking ID
    Then the user searches for the deleted booking and should receive the following error response
      | statusCode | message                      |
      | 404        | Failed to fetch booking: 404 |
