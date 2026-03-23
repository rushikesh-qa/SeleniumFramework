// This is the PACKAGE declaration
// It tells Java this file belongs to the 'pages' folder/group
// Like an address of this file inside the project
package com.ecommerce.pages;

// IMPORT statements — bring in external classes we need to use
// Without imports, Java does not know what WebDriver, By, WebElement means

// WebDriver = the main interface that represents a browser window
import org.openqa.selenium.WebDriver;

// By = a class that helps us LOCATE elements on webpage (by id, by css, etc.)
import org.openqa.selenium.By;

// WebElement = represents ONE HTML element on the page (button, input, div etc.)
import org.openqa.selenium.WebElement;

// PUBLIC CLASS — 'public' means any other class in project can use this class
// 'class' keyword defines a new class
// 'LoginPage' is the class name — must match filename LoginPage.java
public class LoginPage {

    // INSTANCE VARIABLE (field)
    // 'private' = only THIS class can access this variable (encapsulation)
    // 'WebDriver' = data type (like int, String but for browser)
    // 'driver' = variable name that holds the browser instance
    private WebDriver driver;

    // =====================================================
    // LOCATORS — By objects that tell Selenium WHERE to find HTML elements
    // 'private' = only this class uses these locators
    // 'By' = data type for locators
    // By.id() = finds element using its HTML id attribute — fastest method
    // =====================================================

    // Locator for username input field — id="user-name" in HTML
    private By usernameField = By.id("user-name");

    // Locator for password input field — id="password" in HTML
    private By passwordField = By.id("password");

    // Locator for Login button — id="login-button" in HTML
    private By loginButton = By.id("login-button");

    // Locator for error message — uses CSS selector instead of id
    // data-test='error' is a custom HTML attribute on the error div
    // cssSelector is used when element has no id but has other attributes
    private By errorMessage = By.cssSelector("[data-test='error']");

    // =====================================================
    // CONSTRUCTOR
    // Constructor = special method that runs automatically when object is created
    // Name must match class name exactly — LoginPage
    // No return type (not even void)
    // Parameter: WebDriver driver = receives browser instance from test class
    // =====================================================
    public LoginPage(WebDriver driver) {
        // 'this.driver' = the class field declared above (line 28)
        // 'driver' = the parameter passed into constructor
        // this keyword differentiates class field from parameter
        this.driver = driver;
    }

    // =====================================================
    // METHODS — actions a user can perform on login page
    // 'public' = test classes can call these methods
    // 'void' = method does not return any value
    // =====================================================

    // METHOD: enterUsername
    // Purpose: Types the username text into the username input field
    // Parameter: String username = the username text to type
    public void enterUsername(String username) {
        // driver.findElement() = searches the webpage for our element
        // returns a WebElement object (the actual input box)
        // .clear() = erases any existing text in the field first
        driver.findElement(usernameField).clear();

        // .sendKeys() = simulates keyboard typing into the field
        // types whatever string is passed as 'username' parameter
        driver.findElement(usernameField).sendKeys(username);
    }

    // METHOD: enterPassword
    // Purpose: Types the password text into the password input field
    // Parameter: String password = the password text to type
    public void enterPassword(String password) {
        // clear() removes any old text before typing new password
        driver.findElement(passwordField).clear();

        // sendKeys() types the password into the password field
        driver.findElement(passwordField).sendKeys(password);
    }

    // METHOD: clickLoginButton
    // Purpose: Clicks the Login button on the page
    public void clickLoginButton() {
        // .click() = simulates a mouse click on the login button
        driver.findElement(loginButton).click();
    }

    // METHOD: login (convenience method)
    // Purpose: Combines all 3 steps — type username, type password, click login
    // This is called a HELPER METHOD — tests call this single method
    // instead of calling 3 separate methods every time
    // Parameters: String username, String password
    public void login(String username, String password) {
        enterUsername(username);   // Step 1: type username
        enterPassword(password);   // Step 2: type password
        clickLoginButton();        // Step 3: click login button
    }

    // METHOD: getErrorMessage
    // Purpose: Returns the text shown in error message box
    // Return type: String (returns text, not void)
    // Used in tests to CHECK what error message says
    public String getErrorMessage() {
        // .getText() = reads the visible text of the HTML element
        // returns it as a String so test can compare it
        return driver.findElement(errorMessage).getText();
    }

    // METHOD: isErrorMessageDisplayed
    // Purpose: Checks if error message is visible on screen
    // Return type: boolean (returns true or false)
    public boolean isErrorMessageDisplayed() {
        try {
            // isDisplayed() = returns true if element is visible
            // returns false if element is hidden
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            // If element is NOT found at all, findElement throws an Exception
            // We catch it and return false = no error message shown
            return false;
        }
    }

    // METHOD: getCurrentURL
    // Purpose: Returns the current URL of browser
    // Return type: String
    // Used in tests to verify page changed after login
    public String getCurrentURL() {
        // getCurrentUrl() = reads the address bar URL of browser
        return driver.getCurrentUrl();
    }

}

