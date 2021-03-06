Feature: Get current weather data

  Scenario: Verify current weather API for given latitude and longitude
    Given User has "valid" API key
    When User sends latitude as 50 and longitude as 20 to the request
    Then The status code must be 200
    And The country is "PL" and location is "Wieliczka"

  Scenario: Verify current weather API with invalid key
    Given User has "invalid" API key
    When User sends latitude as 50 and longitude as 20 to the request
    Then The status code must be 401
    And The response message contains "Invalid API key"

  Scenario: Verify current weather API with invalid latitude value
    Given User has "valid" API key
    When User sends latitude as 500 and longitude as 20 to the request
    Then The status code must be 400
    And The response message contains "wrong latitude"