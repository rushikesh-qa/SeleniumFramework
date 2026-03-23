// PACKAGE declaration — this file belongs to pages group
package com.ecommerce.pages;

// WebDriver — represents the browser window
import org.openqa.selenium.WebDriver;

// By — used to locate HTML elements on webpage
import org.openqa.selenium.By;

// WebElement — represents one HTML element on the page
import org.openqa.selenium.WebElement;

// List — holds multiple WebElements
import java.util.List;

// ArrayList — resizable list implementation
import java.util.ArrayList;

// PUBLIC CLASS — represents Shopping Cart page of saucedemo
public class CartPage {

    // INSTANCE VARIABLE
    // private — only this class can access it
    // WebDriver — holds the browser instance
    private WebDriver driver;

    // =====================================================
    // LOCATORS — tell Selenium WHERE to find elements
    // =====================================================

    // Each product row in cart has class "cart_item"
    private By cartItems = By.className("cart_item");

    // All remove buttons — starts-with selector matches all
    private By removeButtons = By.cssSelector("[data-test^='remove']");

    // Checkout button — id="checkout" in HTML
    private By checkoutButton = By.id("checkout");

    // Continue Shopping button — id="continue-shopping" in HTML
    private By continueShoppingButton = By.id("continue-shopping");

    // Product name elements inside cart
    private By cartItemNames = By.className("inventory_item_name");

    // Price of each item in cart
    private By cartItemPrices = By.className("inventory_item_price");

    // Cart page title element
    private By cartTitle = By.className("title");

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // =====================================================
    // METHODS
    // =====================================================

    // METHOD: getCartItemCount
    // Purpose: Returns how many items are currently in cart
    // Return type: int
    public int getCartItemCount() {
        // findElements returns list — .size() counts items
        return driver.findElements(cartItems).size();
    }

    // METHOD: isCartEmpty
    // Purpose: Returns true if cart has no items
    // Return type: boolean
    public boolean isCartEmpty() {
        // If count equals 0, cart is empty — returns true
        return getCartItemCount() == 0;
    }

    // METHOD: removeItem
    // Purpose: Removes a specific product from cart by name
    // Parameter: String productName = exact product name
    // FIX: Re-finds elements fresh each time to avoid stale reference
    public void removeItem(String productName) {

        // Get fresh list of all product names in cart
        List<WebElement> names = driver.findElements(cartItemNames);

        // Get fresh list of all remove buttons in cart
        List<WebElement> buttons = driver.findElements(removeButtons);

        // Loop through names to find the matching product
        for (int i = 0; i < names.size(); i++) {

            // .equals() compares exact String match
            // Check if this item name matches what we want to remove
            if (names.get(i).getText().equals(productName)) {

                // Click remove button at same index
                // name[0] pairs with button[0], name[1] with button[1]
                buttons.get(i).click();

                // Stop loop after clicking correct button
                break;
            }
        }
    }

    // METHOD: removeAllItems
    // Purpose: Removes ALL items from cart
    // FIX: Re-finds buttons after each click
    // because page re-renders after every removal
    // old button list becomes stale after each click
    public void removeAllItems() {

        // Keep looping while remove buttons still exist
        while (!driver.findElements(removeButtons).isEmpty()) {

            // Get FRESH list of buttons in every iteration
            // Always click first button in fresh list
            driver.findElements(removeButtons).get(0).click();
        }
    }

    // METHOD: getCartItemNames
    // Purpose: Returns names of all products in cart as List
    // Return type: List<String>
    public List<String> getCartItemNames() {

        // Get all product name elements
        List<WebElement> nameElements = driver.findElements(cartItemNames);

        // Create empty list to store name texts
        List<String> names = new ArrayList<>();

        // Loop through each element and get its text
        for (WebElement element : nameElements) {
            names.add(element.getText());
        }

        return names;
    }

    // METHOD: proceedToCheckout
    // Purpose: Clicks Checkout button
    public void proceedToCheckout() {
        driver.findElement(checkoutButton).click();
    }

    // METHOD: continueShopping
    // Purpose: Clicks Continue Shopping button
    public void continueShopping() {
        driver.findElement(continueShoppingButton).click();
    }

    // METHOD: getCartTitle
    // Purpose: Returns title text of cart page
    // Expected: "Your Cart"
    public String getCartTitle() {
        return driver.findElement(cartTitle).getText();
    }

    // METHOD: isOnCartPage
    // Purpose: Confirms browser is on cart page
    // Return type: boolean
    public boolean isOnCartPage() {
        return driver.getCurrentUrl().contains("cart");
    }

    // METHOD: getItemPrice
    // Purpose: Returns price of specific product in cart
    // Parameter: String productName = product to find price of
    // Return type: String e.g. "$29.99"
    public String getItemPrice(String productName) {

        // Get fresh lists of names and prices
        List<WebElement> names = driver.findElements(cartItemNames);
        List<WebElement> prices = driver.findElements(cartItemPrices);

        // Loop through names to find matching product
        for (int i = 0; i < names.size(); i++) {

            // Check if name matches
            if (names.get(i).getText().equals(productName)) {

                // Return price at same index position
                return prices.get(i).getText();
            }
        }

        // Return this if product not found in cart
        return "Product not found in cart";
    }

}

