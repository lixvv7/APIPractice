Feature: Get current weather data

  Scenario: Verify current weather API for given latitude and longitude
    Given User has valid API key
    When User sends latitude as 50 and longitude as 20 to the request
    Then The status code must be 200
    And The country is "PL" and location is "Wieliczka"
