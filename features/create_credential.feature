Feature: Create Credentials for an API User

  @XXXX
  Scenario: Successful creation of credentials
    When I POST the resource '/authorize' with:
      | username                   | date expired        |
      | alvaro.vilaplana@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP response is 'CREATED'

  Scenario: Bad request due to invalid credentials
    When I POST the resource '/authorize'
    Then the HTTP response is 'BAD REQUEST'


