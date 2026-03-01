package tests;

import utils.TestGenerator;

public class TestGeneratorRunner {

    public static void main(String[] args) {

        // ── Example 1 — Generate Cart Test ─────────
        TestGenerator.generateTestToFile(

            // Plain English description
            "Test the shopping cart functionality:\n"
            + "1. Login with valid credentials\n"
            + "2. Verify products page loads\n"
            + "3. Get the first product name\n"
            + "4. Add first product to cart\n"
            + "5. Verify cart count shows 1\n"
            + "6. Click cart icon\n"
            + "7. Verify we navigated to cart page",

            // Class name for the file
            "CartTest"
        );
    }
}
