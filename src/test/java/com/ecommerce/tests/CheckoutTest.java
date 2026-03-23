// PACKAGE declaration
package com.ecommerce.tests;

// Import all page classes
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

// Extends BaseTest — Chrome opens/closes automatically
public class CheckoutTest extends BaseTest {

    // =====================================================
    // PRIVATE HELPER METHOD
    // Logs in, adds product, navigates to checkout step 1
    // Called before every test in this class
    // =====================================================
    private void loginAndAddProductToCart() {

        // Step 1: Login with valid credentials
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "secret_sauce");

        // Step 2: Wait 3 seconds for inventory page to load
        // Thread.sleep() pauses test execution temporarily
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 3: Click first Add to cart button using XPath
        // XPath finds button by its visible text "Add to cart"
        // [1] means first button found on page
        getDriver().findElement(
            org.openqa.selenium.By.xpath(
                "(//button[text()='Add to cart'])[1]"
            )
        ).click();

        // Step 4: Wait 1 second for cart badge to update
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 5: Click cart icon in top right corner
        // className "shopping_cart_link" is the cart icon
        getDriver().findElement(
            org.openqa.selenium.By.className("shopping_cart_link")
        ).click();

        // Step 6: Wait 1 second for cart page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 7: Check if on cart page then click checkout
        if (getDriver().getCurrentUrl().contains("cart")) {
            // We are on cart page — click checkout button
            getDriver().findElement(
                org.openqa.selenium.By.id("checkout")
            ).click();
        } else {
            // Not on cart page — navigate directly to cart
            getDriver().get(BASE_URL + "cart.html");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Now click checkout
            getDriver().findElement(
                org.openqa.selenium.By.id("checkout")
            ).click();
        }

        // Step 8: Wait for checkout step 1 to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // TEST CASE 1 — Complete Full Checkout Flow
    // =====================================================
    @Test(description = "Complete full checkout flow end to end")
    public void testCompleteCheckoutFlow() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Verify on step 1
        Assert.assertTrue(
            checkoutPage.isOnCheckoutStepOne(),
            "Should be on checkout step one"
        );

        // Fill all fields
        checkoutPage.fillShippingInfo("John", "Doe", "12345");

        // Go to step 2
        checkoutPage.clickContinue();

        // Verify on step 2
        Assert.assertTrue(
            checkoutPage.isOnCheckoutStepTwo(),
            "Should be on checkout step two"
        );

        // Verify total shown
        Assert.assertTrue(
            checkoutPage.getTotalPrice().contains("Total:"),
            "Total price should be shown"
        );

        // Complete order
        checkoutPage.clickFinish();

        // Verify confirmation page
        Assert.assertTrue(
            checkoutPage.isOrderComplete(),
            "Should be on confirmation page"
        );

        // Verify confirmation message
        Assert.assertEquals(
            checkoutPage.getConfirmationHeader(),
            "Thank you for your order!",
            "Should show thank you message"
        );
    }

    // =====================================================
    // TEST CASE 2 — Missing First Name
    // =====================================================
    @Test(description = "Empty first name should show error")
    public void testMissingFirstName() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Empty first name
        checkoutPage.fillShippingInfo("", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.isErrorDisplayed(),
            "Error should show for empty first name"
        );
        Assert.assertTrue(
            checkoutPage.getErrorMessage().contains("First Name"),
            "Error should mention First Name"
        );
    }

    // =====================================================
    // TEST CASE 3 — Missing Last Name
    // =====================================================
    @Test(description = "Empty last name should show error")
    public void testMissingLastName() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Empty last name
        checkoutPage.fillShippingInfo("John", "", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.isErrorDisplayed(),
            "Error should show for empty last name"
        );
        Assert.assertTrue(
            checkoutPage.getErrorMessage().contains("Last Name"),
            "Error should mention Last Name"
        );
    }

    // =====================================================
    // TEST CASE 4 — Missing Postal Code
    // =====================================================
    @Test(description = "Empty postal code should show error")
    public void testMissingPostalCode() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Empty postal code
        checkoutPage.fillShippingInfo("John", "Doe", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.isErrorDisplayed(),
            "Error should show for empty postal code"
        );
        Assert.assertTrue(
            checkoutPage.getErrorMessage().contains("Postal Code"),
            "Error should mention Postal Code"
        );
    }

    // =====================================================
    // TEST CASE 5 — All Fields Empty
    // =====================================================
    @Test(description = "All empty fields should show error")
    public void testAllFieldsEmpty() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // All fields empty
        checkoutPage.fillShippingInfo("", "", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.isErrorDisplayed(),
            "Error should show when all fields empty"
        );
    }

    // =====================================================
    // TEST CASE 6 — Cancel Returns to Cart
    // =====================================================
    @Test(description = "Cancel on step 1 should return to cart")
    public void testCancelOnStepOne() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Click Cancel
        checkoutPage.clickCancel();

        // Should go back to cart
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("cart"),
            "Cancel should return to cart page"
        );
    }

    // =====================================================
    // TEST CASE 7 — Item Total on Overview
    // =====================================================
    @Test(description = "Item total should show correct price")
    public void testItemTotalOnOverview() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Go to step 2
        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Verify item total shown
        Assert.assertTrue(
            checkoutPage.getItemTotal().contains("Item total:"),
            "Item total label should be shown"
        );
    }

    // =====================================================
    // TEST CASE 8 — Tax Shown on Overview
    // =====================================================
    @Test(description = "Tax should be shown on overview page")
    public void testTaxDisplayedOnOverview() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Go to step 2
        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Tax label should show
        Assert.assertTrue(
            checkoutPage.getTaxAmount().contains("Tax:"),
            "Tax should be displayed on overview"
        );
    }

    // =====================================================
    // TEST CASE 9 — Back Home After Order
    // =====================================================
    @Test(description = "Back home returns to products page")
    public void testBackHomeAfterOrder() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // Complete checkout
        checkoutPage.fillShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Wait for step 2 to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkoutPage.clickFinish();

        // Wait for confirmation page
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify on confirmation page
        Assert.assertTrue(
            checkoutPage.isOrderComplete(),
            "Should be on confirmation page"
        );

        // Click Back Home
        checkoutPage.clickBackHome();

        // Wait for inventory page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Should be on inventory page
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            "Should return to products page"
        );
    }

    // =====================================================
    // TEST CASE 10 — Complete Checkout Helper Method
    // =====================================================
    @Test(description = "completeCheckout helper should work")
    public void testCompleteCheckoutHelperMethod() {

        loginAndAddProductToCart();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());

        // completeCheckout does fill + continue + finish in one call
        checkoutPage.completeCheckout("Jane", "Smith", "67890");

        // Wait for confirmation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Order should be complete
        Assert.assertTrue(
            checkoutPage.isOrderComplete(),
            "Order should complete using helper method"
        );

        // Confirmation message should show
        Assert.assertEquals(
            checkoutPage.getConfirmationHeader(),
            "Thank you for your order!",
            "Should show confirmation message"
        );
    }
}