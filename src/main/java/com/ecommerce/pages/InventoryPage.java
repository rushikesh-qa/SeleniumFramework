// PACKAGE declaration — this file belongs to pages group
package com.ecommerce.pages;

// IMPORTS — classes we need from Selenium library
// WebDriver = represents the browser window
import org.openqa.selenium.WebDriver;

// By = helps locate HTML elements on webpage
import org.openqa.selenium.By;

// WebElement = represents one HTML element (button, div, text etc.)
import org.openqa.selenium.WebElement;

// Select = special class ONLY for HTML dropdown (<select> tag) elements
import org.openqa.selenium.support.ui.Select;

// List = Java collection — holds multiple items like an array
// Used when page has MULTIPLE elements (e.g. all product names)
import java.util.List;

// PUBLIC CLASS — accessible from any other class in project
// Represents the Inventory/Products page of saucedemo
public class InventoryPage {

    // INSTANCE VARIABLE
    // private = only this class can use it (encapsulation rule)
    // WebDriver = type, driver = variable name holding browser
    private WebDriver driver;

    // =====================================================
    // LOCATORS — tell Selenium WHERE to find elements
    // =====================================================

    // By.className() = finds elements using their CSS class name
    // "inventory_item_name" = class name of each product title on page
    // There are MULTIPLE products so we use findElements (plural) later
    private By productNames = By.className("inventory_item_name");

    // CSS selector with ^ symbol = "starts with"
    // data-test attribute starts with 'add-to-cart' for all Add buttons
    // This matches ALL add to cart buttons on the page at once
    private By addToCartButtons = By.cssSelector("[data-test^='add-to-cart']");

    // Locator for the sort dropdown — class name "product_sort_container"
    private By sortDropdown = By.className("product_sort_container");

    // The orange number badge on cart icon showing item count
    private By cartBadge = By.className("shopping_cart_badge");

    // The cart icon/link in top right corner to navigate to cart page
    private By cartLink = By.className("shopping_cart_link");

    // Hamburger menu button (3 lines) in top left corner
    private By burgerMenu = By.id("react-burger-menu-btn");

    // Logout link inside the hamburger side menu
    private By logoutLink = By.id("logout_sidebar_link");

    // Price labels for each product — used for sort verification
    private By productPrices = By.className("inventory_item_price");

    // =====================================================
    // CONSTRUCTOR
    // Runs when test does: new InventoryPage(getDriver())
    // Receives browser instance from test class
    // =====================================================
    public InventoryPage(WebDriver driver) {
        // Store the received browser instance in our class variable
        this.driver = driver;
    }

    // =====================================================
    // METHODS — actions user can do on inventory page
    // =====================================================

    // METHOD: getAllProductNames
    // Purpose: Returns ALL product name elements visible on page
    // Return type: List<WebElement> = a list holding multiple elements
    // Used to count products or read their names
    public List<WebElement> getAllProductNames() {
        // findElements (PLURAL) = returns ALL matching elements as a List
        // findElement (singular) = returns only FIRST matching element
        return driver.findElements(productNames);
    }

    // METHOD: getProductCount
    // Purpose: Returns how many products are shown on page
    // Return type: int = a whole number
    public int getProductCount() {
        // .size() on a List returns number of items in the list
        // saucedemo shows 6 products — so this returns 6
        return driver.findElements(productNames).size();
    }

    // METHOD: addProductToCart
    // Purpose: Clicks Add to Cart button for a specific product
    // Parameter: String productName = name of product to add
    public void addProductToCart(String productName) {
        // Convert product name to the format used in data-test attribute
        // Example: "Sauce Labs Backpack" → "sauce-labs-backpack"
        // .toLowerCase() = converts to all small letters
        // .replace(" ", "-") = replaces spaces with hyphens
        String safeId = productName.toLowerCase().replace(" ", "-");

        // Build the full CSS selector using the converted product name
        // Example: [data-test='add-to-cart-sauce-labs-backpack']
        driver.findElement(
            By.cssSelector("[data-test='add-to-cart-" + safeId + "']")
        ).click(); // .click() = clicks the Add to Cart button
    }

    // METHOD: sortProductsBy
    // Purpose: Selects an option from the sort dropdown
    // Parameter: String option = the visible text of dropdown option
    public void sortProductsBy(String option) {
        // Select class handles HTML <select> dropdown elements
        // First find the dropdown element, then wrap it with Select class
        Select select = new Select(driver.findElement(sortDropdown));

        // selectByVisibleText() = selects option matching the visible text
        // Example options: "Name (A to Z)", "Price (low to high)"
        select.selectByVisibleText(option);
    }

    // METHOD: getCartBadgeCount
    // Purpose: Returns number shown on cart icon (e.g. "2")
    // Return type: String because getText() returns String
    public String getCartBadgeCount() {
        // getText() reads the visible number on the orange cart badge
        return driver.findElement(cartBadge).getText();
    }

    // METHOD: isCartBadgeDisplayed
    // Purpose: Checks if cart badge is visible (true) or not (false)
    // Return type: boolean
    public boolean isCartBadgeDisplayed() {
        try {
            // isDisplayed() = true if badge is visible on screen
            return driver.findElement(cartBadge).isDisplayed();
        } catch (Exception e) {
            // Badge not found = cart is empty = return false
            return false;
        }
    }

    // METHOD: goToCart
    // Purpose: Clicks the cart icon to navigate to cart page
    public void goToCart() {
        // Clicks the shopping cart icon in top right corner
        driver.findElement(cartLink).click();
    }

    // METHOD: logout
    // Purpose: Opens side menu and clicks logout
    // Two steps: open menu THEN click logout
    public void logout() {
        // Step 1: Click hamburger menu to open side navigation
        driver.findElement(burgerMenu).click();

        // Step 2: Click logout link in the side menu
        // Small pause needed — menu needs time to open
        driver.findElement(logoutLink).click();
    }

    // METHOD: isOnInventoryPage
    // Purpose: Confirms browser is on the inventory/products page
    // Return type: boolean
    public boolean isOnInventoryPage() {
        // getCurrentUrl() returns current browser URL as String
        // .contains("inventory") checks if URL has word "inventory"
        // Returns true if on inventory page, false if not
        return driver.getCurrentUrl().contains("inventory");
    }

    // METHOD: getFirstProductName
    // Purpose: Returns name text of the first product in the list
    // Return type: String
    public String getFirstProductName() {
        // get(0) = gets FIRST item from list (index starts at 0)
        // getText() = reads its visible text
        return driver.findElements(productNames).get(0).getText();
    }

    // METHOD: getLastProductName
    // Purpose: Returns name text of the last product in the list
    // Return type: String
    public String getLastProductName() {
        List<WebElement> names = driver.findElements(productNames);
        // size()-1 = index of last item (list of 6 → index 5)
        return names.get(names.size() - 1).getText();
    }

}

