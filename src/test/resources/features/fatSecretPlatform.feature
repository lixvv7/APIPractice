Feature: Get food search details

  Background:
    Given User generates the authorization token

Scenario: Search food details
  When User searches for "Plain French Toast"
  Then Status code must be 200
  And The food ID is "4384" and food type is "Generic"

Scenario: Search food details with incorrect token
  When User searches for "Plain French Toast" with incorrect token
  Then Error code in response should be "13"