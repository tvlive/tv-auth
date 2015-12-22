Feature: Authorize API User

  Background:
    Given The users with credentials to use the API:
      | username               | token      | date expaired       |
      | william.mark@tvlive.io | 0123456789 | 10/10/2016 02:00:00 |

  @XXXX
  Scenario: Succesfull Authorization
    When I GET the resource '/authorize/william.mark@tvlive.io/0123456789'
    Then the HTTP response is 'OK'

  @XXXX
  Scenario: The user does not exist
    When I GET the resource '/authorize/some.mark@tvlive.io/0123456789'
    Then the HTTP response is 'NOT FOUND'

  @XXXX
  Scenario: The token does not exist
    When I GET the resource '/authorize/william.mark@tvlive.io/0987654321'
    Then the HTTP response is 'NOT FOUND'

  @XXXX
  Scenario: Unsuccessful creation of credentials due to duplication
    When I POST the resource '/authorize' with:
      | username               | date expired        |
      | william.mark@tvlive.io | 10/10/2016 02:00:00 |
    Then the HTTP response is 'CONFLICT'

  @XXXX
  Scenario: The token expired
    Given The users with credentials to use the API:
      | username                  | token      | date expaired       |
      | william.wallace@tvlive.io | 0123456789 | 10/10/2014 02:00:00 |
    When I GET the resource '/authorize/william.wallace@tvlive.io/0123456789'
    Then the HTTP response is 'NOT FOUND'