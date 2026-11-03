@Dummy
Feature: Dummy API Automation

  @GetOneEmployeeDetails
  Scenario Outline: <TestCase>
    When Get One Employee Details
    Then Validate status code <statusCode> and message using "<TestCase>"
    Examples:
      | TestCase              | statusCode |
      | GetOneEmployeeDetails | 200        |


  @GetAllEmployeeDetails
  Scenario Outline: <TestCase>
    When Get All Employee Details
    Then Validate status code <statusCode> and message using "<TestCase>"
    Examples:
      | TestCase              | statusCode |
      | GetAllEmployeeDetails | 200        |