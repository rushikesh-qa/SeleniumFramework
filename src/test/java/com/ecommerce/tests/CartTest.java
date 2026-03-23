// PACKAGE declaration � this file belongs to tests group
package com.ecommerce.tests;

// Import our page classes created earlier
// LoginPage � needed to login before testing cart
import com.ecommerce.pages.LoginPage;

// InventoryPage � needed to add products to cart
import com.ecommerce.pages.InventoryPage;

// CartPage � has all cart page actions and locators
import com.ecommerce.pages.CartPage;

// BaseTest � provides browser setup and teardown
import com.ecommerce.utils.BaseTest;

// Assert � verifies expected vs actual results
import org.testng.Assert;

// Test annotation � marks method as test case
import org.testng.annotations.Test;

// PUBLIC CLASS � extends BaseTest
// Inherits setUp() � Chrome opens before each test
// Inherits tearDown() � Chrome closes after each test
public class CartTest extends BaseTest {

    // =====================================================
    // PRIVATE HELPER METHOD � loginAndGoToInventory
    // Purpose: Logs in before every cart test
    // private � only used inside this class
    // void � returns nothing
    // Avoids repeating login code in every test
    // =====================================================
    private void loginAndGoToInventory() {

        // Create LoginPage and perform login
        // standard_user is valid working user
        new LoginPage(getDriver()).login(
            "standard_user", "secret_sauce"
        );
    }

    // =====================================================
    // TEST CASE 1 � Add Single Item to Cart
    // =====================================================

    @Test(description = "Add one product to cart and verify badge count")
    public void testAddSingleItemToCart() {

        // Login first � cart requires being logged in
        loginAndGoToInventory();

        // Create InventoryPage to interact with products
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add Sauce Labs Backpack to cart
        // This clicks the Add to Cart button for this product
        inventoryPage.addProductToCart("sauce-labs-backpack");

        // ASSERTION � cart badge should show "1"
        // getCartBadgeCount() reads orange number on cart icon
        Assert.assertEquals(
            inventoryPage.getCartBadgeCount(),
            "1",
            "Cart badge should show 1 after adding one item"
        );
    }

    // =====================================================
    // TEST CASE 2 � Add Multiple Items to Cart
    // =====================================================

    @Test(description = "Add multiple products and verify badge count")
    public void testAddMultipleItemsToCart() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add three different products one by one
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");
        inventoryPage.addProductToCart("sauce-labs-bolt-t-shirt");

        // ASSERTION � badge should show "3"
        Assert.assertEquals(
            inventoryPage.getCartBadgeCount(),
            "3",
            "Cart badge should show 3 after adding three items"
        );
    }

    // =====================================================
    // TEST CASE 3 � Verify Cart Page Opens Correctly
    // =====================================================

    @Test(description = "Cart page should open with correct title")
    public void testCartPageOpens() {

        loginAndGoToInventory();

        // Add one item first so cart is not empty
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addProductToCart("sauce-labs-backpack");

        // Navigate to cart page by clicking cart icon
        inventoryPage.goToCart();

        // Create CartPage object to interact with cart page
        CartPage cartPage = new CartPage(getDriver());

        // ASSERTION 1 � should be on cart page
        Assert.assertTrue(
            cartPage.isOnCartPage(),
            "Should be on cart page after clicking cart icon"
        );

        // ASSERTION 2 � cart page title should say "Your Cart"
        Assert.assertEquals(
            cartPage.getCartTitle(),
            "Your Cart",
            "Cart page title should be Your Cart"
        );
    }

    // =====================================================
    // TEST CASE 4 � Verify Item Name in Cart
    // =====================================================

    @Test(description = "Added product name should appear in cart")
    public void testItemNameInCart() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add Sauce Labs Backpack to cart
        inventoryPage.addProductToCart("sauce-labs-backpack");

        // Navigate to cart page
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // ASSERTION � cart item names list should contain product name
        // getCartItemNames() returns List of all item name strings
        // .contains() checks if specific name exists in that list
        Assert.assertTrue(
            cartPage.getCartItemNames().contains("Sauce Labs Backpack"),
            "Cart should contain Sauce Labs Backpack"
        );
    }

    // =====================================================
    // TEST CASE 5 � Verify Item Count in Cart Page
    // =====================================================

    @Test(description = "Cart item count should match added products")
    public void testCartItemCount() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add two products to cart
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");

        // Navigate to cart
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // ASSERTION � cart should have exactly 2 items
        Assert.assertEquals(
            cartPage.getCartItemCount(),
            2,
            "Cart should contain exactly 2 items"
        );
    }

    // =====================================================
    // TEST CASE 6 � Remove Single Item from Cart
    // =====================================================

 // TEST CASE 6 � Remove Single Item from Cart
    @Test(description = "Remove one item and verify cart updates")
    public void testRemoveSingleItem() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add two products first
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");

        // Navigate to cart
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // FIX: Use exact product name as displayed on cart page
        // Capital letters matter � must match getText() exactly
        // "Sauce Labs Backpack" not "sauce-labs-backpack"
        cartPage.removeItem("Sauce Labs Backpack");

        // Small wait � give page time to update after removal
        try {
            // Thread.sleep pauses execution for 1 second (1000 ms)
            // Gives browser time to re-render page after removal
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // InterruptedException must be caught � Java rule
            e.printStackTrace();
        }

        // ASSERTION 1 � cart should now have only 1 item
        Assert.assertEquals(
            cartPage.getCartItemCount(),
            1,
            "Cart should have 1 item after removing one"
        );

        // ASSERTION 2 � removed item should NOT be in cart anymore
        Assert.assertFalse(
            cartPage.getCartItemNames().contains("Sauce Labs Backpack"),
            "Removed item should not appear in cart"
        );

        // ASSERTION 3 � remaining item should still be in cart
        Assert.assertTrue(
            cartPage.getCartItemNames().contains("Sauce Labs Bike Light"),
            "Remaining item should still be in cart"
        );
    }


    // =====================================================
    // TEST CASE 7 � Remove All Items from Cart
    // =====================================================

    @Test(description = "Remove all items and verify cart is empty")
    public void testRemoveAllItems() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add two products
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");

        // Navigate to cart
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // Remove ALL items using removeAllItems() method
        // This loops through all Remove buttons and clicks each
        cartPage.removeAllItems();

        // ASSERTION 1 � cart should be empty now
        Assert.assertTrue(
            cartPage.isCartEmpty(),
            "Cart should be empty after removing all items"
        );

        // ASSERTION 2 � item count should be 0
        Assert.assertEquals(
            cartPage.getCartItemCount(),
            0,
            "Cart item count should be 0 after removing all"
        );
    }

    // =====================================================
    // TEST CASE 8 � Continue Shopping Button
    // =====================================================

    @Test(description = "Continue shopping should go back to products")
    public void testContinueShopping() {

        loginAndGoToInventory();

        // Add item and go to cart
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // Click Continue Shopping button
        cartPage.continueShopping();

        // ASSERTION � should be back on inventory page
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            "Continue shopping should return to inventory page"
        );
    }

    // =====================================================
    // TEST CASE 9 � Cart Proceeds to Checkout
    // =====================================================

    @Test(description = "Checkout button should go to checkout page")
    public void testProceedToCheckout() {

        loginAndGoToInventory();

        // Add item and go to cart
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // Click Checkout button
        cartPage.proceedToCheckout();

        // ASSERTION � URL should contain checkout-step-one
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("checkout-step-one"),
            "Checkout button should go to checkout step one"
        );
    }

    // =====================================================
    // TEST CASE 10 � Verify Item Price in Cart
    // =====================================================

    @Test(description = "Product price should be correct in cart")
    public void testItemPriceInCart() {

        loginAndGoToInventory();

        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Add Sauce Labs Backpack � known price is $29.99
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());

        // ASSERTION � price should match expected value
        Assert.assertEquals(
            cartPage.getItemPrice("Sauce Labs Backpack"),
            "$29.99",
            "Sauce Labs Backpack price should be $29.99"
        );
    }
}

