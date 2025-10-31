# API Hotel Booking
The objective of this exercise is to demonstrate API testing and Java proficiency by building a basic API Test Automation framework using Rest-Assured and Cucumber. The project includes a test suite that executes multiple tests against a hotel booking website endpoint and validates their responses.

### Key Highlights:
* Modular BDD architecture (easy to extend and maintain)
* API testing powered by Rest Assured
* Log4j logging for full request/response tracing
* Customised HTML reports via Extent Reports 
* Supports positive, negative, regression & functional tagging
  
### Tech Stack

| Tool               | Purpose                         |
| ------------------ | ------------------------------- |
| **Java 21**        | Core programming language       |
| **Maven 3.9+**     | Build and dependency management |
| **JUnit 4**        | Test execution framework        |
| **Cucumber BDD**   | Scenario-driven testing         |
| **Rest Assured**   | REST API validation             |
| **Hamcrest**       | Assertion and matcher library   |
| **Log4j**          | Logging framework               |
| **Extent Reports** | Rich HTML reporting             |

### Prerequisites
| Tool  | Version            | Command to Verify |
| ----- | ------------------ | ----------------- |
| Java  | 21                 | `java -version`   |
| Maven | 3.9+               | `mvn -v`          |
| Git   | Latest             | `git --version`   |
| IDE   | IntelliJ / Eclipse | —                 |

### Installation Steps
#### 1. Clone the Repository
* git clone https://github.com/BhargavaCherakula/api-hotel-booking.git
#### 2. Navigate to the Project Folder
* cd api-hotel-booking
#### 3. Build the Project
* mvn clean install

### Running the Test Suite
### NOTE:
* **Navigate to and open the Test Pack named 'api-hotel-booking'.**
* **Open the Command Prompt inside the Test Pack folder, then execute the commands below according to the testing flow you wish to perform.**

### To Run The Suite Using GitActions
* #### Click this url path - https://github.com/BhargavaCherakula/api-hotel-booking/actions/workflows/api-tests.yml
* #### Click on 'Run workflow' button under 'Run workflow' dropdown shown in screen
![RunWorkFlowScreen](https://github.com/user-attachments/assets/d078d0a1-2b72-4423-8598-4ce2275e9f1e)

### To Run The Suite Offline
    mvn test
      [OR]
We can 'TestRunner.java' file directly

### Admin Feature:
**Run Complete Feature**
* mvn test -Dcucumber.features="src/test/resources/features/admin.feature" -Dcucumber.filter.tags="@Admin"

**Run Negative Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/admin.feature" -Dcucumber.filter.tags="@Negative"

**Run Positive Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/admin.feature" -Dcucumber.filter.tags="@Positive"

### Bookings Feature:

**Run Complete Feature**
mvn test -Dcucumber.features="src/test/resources/features/admin.feature" -Dcucumber.filter.tags="@Bookings"

**Run Negative Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/bookings.feature" -Dcucumber.filter.tags="@Negative"

**Run Positive Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/bookings.feature" -Dcucumber.filter.tags="@Positive"

**Run Room Enquiry Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/bookings.feature" -Dcucumber.filter.tags="@RoomEnquiry"

**Run Regression Tests**:
* mvn test -Dcucumber.features="src/test/resources/features/bookings.feature" -Dcucumber.filter.tags="@Regression"

### Viewing the Test Report
* After each test run, an HTML report is automatically generated in the folder below path.

**📁 Report Path**
* **C:\Users\<your_profile>\api-hotel-booking\target\extent-reports\ExtentReport.html**

### To View the Report
* Navigate to the above folder
* Double-click ExtentReport.html
* View detailed results

### ExtentReport Report Sample:
### 👉 [Executed Full Extent Report Sample (HTML)] : api-hotel-booking/etc/reports
#### Sample:
#![test results sample](https://github.com/user-attachments/assets/e7521c55-eaeb-42b7-9439-0e652fb3079f)
<img width="1260" height="1704" alt="_C__Users_cbhar_AppData_Local_Temp_51d78191-e5dc-494f-9754-e75d33c92aa0_ExtentReport%20(3) zip aa0_ExtentReport html(mac)" src="https://github.com/user-attachments/assets/49916c52-2355-4bbe-a58d-0b0c1d2fc8d1" />


### ---------- Happy Testing -------------
