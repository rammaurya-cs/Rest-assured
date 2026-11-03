@Dummy
Feature: Dummy API Automation

  Scenario Outline: <TestCase>
    When Get One Employee Details
    Then Validate status code "<statusCode>" and message using "<TestCase>"
    Examples:
      | TestCase              | statusCode |
      | GetOneEmployeeDetails | 200        |