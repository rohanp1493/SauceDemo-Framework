# Selenium Java Automation Framework
 
> Enterprise-grade test automation framework built from scratch using Java, Selenium WebDriver, and TestNG.
 
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-FF6C37?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
 
---
 
## 🏗️ Framework Architecture
 
```
selenium-java-framework/
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   └── BasePage.java          # WebDriver init, wait utilities, screenshot
│   │   ├── pages/
│   │   │   ├── LoginPage.java         # Login page actions + locators
│   │   │   └── DashboardPage.java     # Dashboard page actions + locators
│   │   └── utils/
│   │       ├── DriverManager.java     # Singleton WebDriver manager
│   │       ├── RetryAnalyzer.java     # TestNG retry on failure
│   │       └── ScreenshotUtil.java    # Screenshot capture utility
│   └── test/java/
│       ├── tests/
│       │   ├── LoginTest.java
│       │   └── DashboardTest.java
│       └── listeners/
│           └── TestListener.java      # Screenshot on failure listener
├── testng.xml                         # TestNG suite configuration
└── pom.xml                            # Maven dependencies
```
 
## ✅ Key Features
 
| Feature | Implementation |
|---|---|
| Design Pattern | Page Object Model (POM) with BasePage |
| Wait Strategy | Centralized explicit waits — no Thread.sleep |
| Failure Handling | Auto-screenshot + RetryAnalyzer (2 retries) |
| Reporting | TestNG HTML reports + ExtentReports |
| Data-Driven | @DataProvider for multi-dataset tests |
| Browser Support | Chrome, Firefox (configurable) |
 
## 🚀 How to Run
 
```bash
# Clone the repo
git clone https://github.com/[patilrohan-qa]/selenium-java-framework.git
 
# Run all tests
mvn clean test
 
# Run specific suite
mvn clean test -DsuiteXmlFile=testng.xml
```
 
## 💡 Design Decisions
 
**Why POM?** Separates UI interaction from test logic. When a locator changes, only the Page class updates — not every test.
 
**Why centralized waits?** Prevents duplication and ensures consistent wait strategy. All waits route through BasePage.waitForElement().
 
**Why RetryAnalyzer?** Finance domain apps occasionally have network blips in staging. Retry handles environment instability without hiding real failures.
