package utils;

import config.ConfigReader;

public class TestGenerator {

    // ───────────────────────────────────────────────
    // generateTest()
    // Takes plain English description
    // Returns ready-to-use Java TestNG test code
    //
    // Example:
    // generateTest("Test that adding item to cart
    //               increases cart count to 1")
    // ───────────────────────────────────────────────
    public static String generateTest(
            String testDescription) {

        // Tell AI about our framework structure
        // So it generates compatible code
        String systemPrompt =
            "You are a Selenium Java test automation expert. "
            + "Generate TestNG test code for a framework "
            + "with these exact classes available:\n\n"

            + "PAGE OBJECTS:\n"
            + "LoginPage methods:\n"
            + "  open(String url)\n"
            + "  login(String username, String password)\n"
            + "  enterUsername(String username)\n"
            + "  enterPassword(String password)\n"
            + "  clickLogin()\n"
            + "  isErrorDisplayed() → boolean\n"
            + "  getErrorMessage() → String\n\n"

            + "InventoryPage methods:\n"
            + "  isLoaded() → boolean\n"
            + "  getPageTitle() → String\n"
            + "  getProductCount() → int\n"
            + "  getFirstProductName() → String\n"
            + "  addFirstItemToCart()\n"
            + "  getCartCount() → String\n"
            + "  clickCart()\n"
            + "  logout()\n\n"

            + "FRAMEWORK RULES:\n"
            + "1. Test class extends BaseTest\n"
            + "2. Use @BeforeMethod to create "
            + "page objects and open URL\n"
            + "3. Get driver from "
            + "DriverFactory.getDriver()\n"
            + "4. Get config from "
            + "ConfigReader.getInstance()\n"
            + "5. Use config.get('app.username') "
            + "and config.get('app.password')\n"
            + "6. Use config.get('base.url') for URL\n"
            + "7. Package name is tests\n"
            + "8. Use Thread.sleep(2000) for waits\n"
            + "9. Use Assert.assertTrue, "
            + "Assert.assertEquals for assertions\n"
            + "10. Add @Epic, @Feature, @Story, "
            + "@Severity Allure annotations\n\n"

            + "IMPORTS NEEDED:\n"
            + "import pages.LoginPage;\n"
            + "import pages.InventoryPage;\n"
            + "import driver.DriverFactory;\n"
            + "import config.ConfigReader;\n"
            + "import org.testng.Assert;\n"
            + "import org.testng.annotations.*;\n"
            + "import io.qameta.allure.*;\n"
            + "import org.openqa.selenium.WebDriver;\n\n"

            + "Generate ONLY the Java code. "
            + "No explanations before or after. "
            + "No markdown code blocks. "
            + "Just pure Java code ready to paste.";

        String userMessage =
            "Generate a complete TestNG test class "
            + "for this scenario:\n\n"
            + testDescription;

        System.out.println(
            "Generating test for: "
            + testDescription);

        String generatedCode =
            AIHelper.ask(systemPrompt, userMessage);

        System.out.println(
            "\n========= GENERATED TEST CODE =========\n"
            + generatedCode
            + "\n=======================================\n");

        return generatedCode;
    }

    // ───────────────────────────────────────────────
    // generateTestToFile()
    // Same as generateTest but also saves to file
    // ───────────────────────────────────────────────
    public static String generateTestToFile(
            String testDescription,
            String className) {

        String code = generateTest(testDescription);

        // Save to file
        String filePath =
            "src/test/java/tests/"
            + className + ".java";

        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get(filePath),
                code.getBytes());
            System.out.println(
                "Test saved to: " + filePath);
        } catch (Exception e) {
            System.out.println(
                "Could not save file: "
                + e.getMessage());
        }

        return code;
    }
}