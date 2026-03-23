// PACKAGE declaration
package com.ecommerce.tests;

// Import page classes
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.InventoryPage;
import com.ecommerce.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

// 10 Product/Inventory page test scenarios
// Extends BaseTest — Chrome opens/closes automatically
public class ProductTest extends BaseTest {

    // =====================================================
    // PRIVATE HELPER METHOD — login
    // Logs in before every product test
    // =====================================================
    private InventoryPage login() {

        // Login with valid credentials
        new LoginPage(getDriver())
            .login("standard_user", "secret_sauce");

        // Wait for inventory page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return InventoryPage object ready to use
        return new InventoryPage(getDriver());
    }

    // =====================================================
    // TEST CASE 1 — Verify Product Count
    // =====================================================
    @Test(description = "Inventory page should show 6 products")
    public void testProductCount() {

        InventoryPage inventoryPage = login();

        // saucedemo always shows exactly 6 products
        // getProductCount() returns size of product list
        Assert.assertEquals(
            inventoryPage.getProductCount(),
            6,
            "Inventory page should display exactly 6 products"
        );
    }

    // =====================================================
    // TEST CASE 2 — Verify Page Title
    // =====================================================
    @Test(description = "Inventory page title should be correct")
    public void testInventoryPageTitle() {

        login();

        // getTitle() returns browser tab title
        // saucedemo tab title is "Swag Labs"
        Assert.assertEquals(
            getDriver().getTitle(),
            "Swag Labs",
            "Browser title should be Swag Labs"
        );
    }

    // =====================================================
    // TEST CASE 3 — Sort Products A to Z
    // =====================================================
    @Test(description = "Sort A to Z should work correctly")
    public void testSortAtoZ() {

        InventoryPage inventoryPage = login();

        // Select "Name (A to Z)" from sort dropdown
        inventoryPage.sortProductsBy("Name (A to Z)");

        // Wait for sort to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // First product alphabetically is "Sauce Labs Backpack"
        Assert.assertEquals(
            inventoryPage.getFirstProductName(),
            "Sauce Labs Backpack",
            "First product A to Z should be Sauce Labs Backpack"
        );
    }

    // =====================================================
    // TEST CASE 4 — Sort Products Z to A
    // =====================================================
    @Test(description = "Sort Z to A should work correctly")
    public void testSortZtoA() {

        InventoryPage inventoryPage = login();

        // Select "Name (Z to A)" from sort dropdown
        inventoryPage.sortProductsBy("Name (Z to A)");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Last alphabetically appears first in Z to A sort
        // "Test.allTheThings() T-Shirt (Red)" is last alphabetically
        Assert.assertEquals(
            inventoryPage.getFirstProductName(),
            "Test.allTheThings() T-Shirt (Red)",
            "First product Z to A should be T-Shirt"
        );
    }

    // =====================================================
    // TEST CASE 5 — Sort Price Low to High
    // =====================================================
    @Test(description = "Sort price low to high should work")
    public void testSortPriceLowToHigh() {

        InventoryPage inventoryPage = login();

        // Select price ascending sort
        inventoryPage.sortProductsBy("Price (low to high)");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cheapest product is "Sauce Labs Onesie" at $7.99
        Assert.assertEquals(
            inventoryPage.getFirstProductName(),
            "Sauce Labs Onesie",
            "Cheapest product should appear first"
        );
    }

    // =====================================================
    // TEST CASE 6 — Sort Price High to Low
    // =====================================================
    @Test(description = "Sort price high to low should work")
    public void testSortPriceHighToLow() {

        InventoryPage inventoryPage = login();

        // Select price descending sort
        inventoryPage.sortProductsBy("Price (high to low)");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Most expensive is "Sauce Labs Fleece Jacket" at $49.99
        Assert.assertEquals(
            inventoryPage.getFirstProductName(),
            "Sauce Labs Fleece Jacket",
            "Most expensive product should appear first"
        );
    }

    // =====================================================
    // TEST CASE 7 — Add Product Shows Cart Badge
    // =====================================================
    @Test(description = "Adding product should show cart badge")
    public void testCartBadgeAppearsAfterAdd() {

        InventoryPage inventoryPage = login();

        // Before adding — badge should NOT be visible
        Assert.assertFalse(
            inventoryPage.isCartBadgeDisplayed(),
            "Cart badge should not show before adding product"
        );

        // Add one product
        inventoryPage.addProductToCart("sauce-labs-backpack");

        // After adding — badge SHOULD be visible showing "1"
        Assert.assertTrue(
            inventoryPage.isCartBadgeDisplayed(),
            "Cart badge should appear after adding product"
        );

        Assert.assertEquals(
            inventoryPage.getCartBadgeCount(),
            "1",
            "Cart badge should show count 1"
        );
    }

    // =====================================================
    // TEST CASE 8 — Inventory Page URL is Correct
    // =====================================================
    @Test(description = "After login URL should contain inventory")
    public void testInventoryPageURL() {

        login();

        // After successful login URL should contain "inventory"
        Assert.assertTrue(
            inventoryPage_isCorrect(),
            "URL should contain inventory after login"
        );
    }

    // Private helper — checks current URL contains inventory
    private boolean inventoryPage_isCorrect() {
        return getDriver().getCurrentUrl().contains("inventory");
    }

    // =====================================================
    // TEST CASE 9 — Multiple Products Add to Cart
    // =====================================================
    @Test(description = "Adding 3 products shows badge count 3")
    public void testAddThreeProductsToCart() {

        InventoryPage inventoryPage = login();

        // Add 3 different products
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");
        inventoryPage.addProductToCart("sauce-labs-bolt-t-shirt");

        // Badge should show "3"
        Assert.assertEquals(
            inventoryPage.getCartBadgeCount(),
            "3",
            "Cart badge should show 3 after adding 3 products"
        );
    }

    // =====================================================
    // TEST CASE 10 — Navigate to Cart from Inventory
    // =====================================================
    @Test(description = "Cart icon should navigate to cart page")
    public void testNavigateToCartFromInventory() {

        InventoryPage inventoryPage = login();

        // Add product first
        inventoryPage.addProductToCart("sauce-labs-backpack");

        // Click cart icon
        inventoryPage.goToCart();

        // Wait for cart page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // URL should now contain "cart"
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("cart"),
            "Should navigate to cart page after clicking cart icon"
        );
    }
}
