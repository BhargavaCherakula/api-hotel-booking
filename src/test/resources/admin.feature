Feature: Admin Authentication and Cache Key Retrieval

  @AdminLogin @Positive
  Scenario: Verify admin successfully logs in and receives cache key
    Given the secure admin credentials have been provisioned
    When the admin sends a POST request to the login endpoint
    Then the response status code should be 200
    And the response should contain a non-empty cache key

  @AdminLogin @Negative
  Scenario Outline: Verify admin login fails with invalid credentials
    Given the admin has invalid credentials "<userName>" and "<password>"
    When the admin sends a POST request to the login endpoint
    Then the response status code should be 401
    And the response should contain an error message "Invalid credentials"
    Examples:
      | userName | password   |
      |          | 0987654321 |
      | #%$#!)(  |            |
      |          | password   |
      | admin    |            |