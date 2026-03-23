// PACKAGE declaration — this file belongs to pages group
package com.ecommerce.pages;

// WebDriver — represents the browser window
import org.openqa.selenium.WebDriver;

// By — used to locate HTML elements on webpage
import org.openqa.selenium.By;

// PUBLIC CLASS — accessible from any other class in project
// Represents the entire Checkout flow of saucedemo
// Checkout has 3 steps:
// Step 1 — Fill personal information (name, zip)
// Step 2 — Review order overview and total price
// Step 3 — Order confirmation page
public class CheckoutPage {

    // INSTANCE VARIABLE
    // private — only this class can access it
    // WebDriver — holds the browser instance
    private WebDriver driver;

    // =====================================================
    // LOCATORS FOR STEP 1 — Checkout Information Page
    // URL: /checkout-step-one.html
    // =====================================================

    // First name input field — id="first-name" in HTML
    private By firstNameField = By.id("first-name");

    // Last name input field — id="last-name" in HTML
    private By lastNameField = By.id("last-name");

    // Zip/Postal code input field — id="postal-code" in HTML
    private By postalCodeField = By.id("postal-code");

    // Continue button — submits step 1 form and goes to step 2
    private By continueButton = By.id("continue");

    // Cancel button on step 1 — goes back to cart page
    private By cancelButton = By.id("cancel");

    // Error message shown when required fields are empty
    // Uses CSS selector with data-test attribute
    private By errorMessage = By.cssSelector("[data-test='error']");

    // =====================================================
    // LOCATORS FOR STEP 2 — Checkout Overview Page
    // URL: /checkout-step-two.html
    // =====================================================

    // Item total label — shows subtotal before tax
    // Example text: "Item total: $29.99"
    private By itemTotal = By.className("summary_subtotal_label");

    // Tax label — shows tax amount
    // Example text: "Tax: $2.40"
    private By taxLabel = By.className("summary_tax_label");

    // Total label — shows final price including tax
    // Example text: "Total: $32.39"
    private By totalLabel = By.className("summary_total_label");

    // Finish button — completes the order and goes to step 3
    private By finishButton = By.id("finish");

    // Product names shown in order overview
    private By overviewItemNames = By.className("inventory_item_name");

    // =====================================================
    // LOCATORS FOR STEP 3 — Order Confirmation Page
    // URL: /checkout-complete.html
    // =====================================================

    // Main confirmation heading — "Thank you for your order!"
    private By confirmationHeader = By.className("complete-header");

    // Sub text below heading — delivery time message
    private By confirmationText = By.className("complete-text");

    // Back Home button — returns to products page after order
    private By backHomeButton = By.id("back-to-products");

    // =====================================================
    // CONSTRUCTOR
    // Runs when test does: new CheckoutPage(getDriver())
    // Receives browser instance from test class
    // =====================================================
    public CheckoutPage(WebDriver driver) {
        // Store received browser in class variable
        this.driver = driver;
    }

    // =====================================================
    // STEP 1 METHODS — actions on information page
    // =====================================================

    // METHOD: enterFirstName
    // Purpose: Types first name into first name field
    // Parameter: String firstName = first name to type
    public void enterFirstName(String firstName) {
        // clear() removes any existing text first
        driver.findElement(firstNameField).clear();
        // sendKeys() types the firstName into the field
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    // METHOD: enterLastName
    // Purpose: Types last name into last name field
    // Parameter: String lastName = last name to type
    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).clear();
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    // METHOD: enterPostalCode
    // Purpose: Types zip/postal code into postal code field
    // Parameter: String postalCode = zip code to type
    public void enterPostalCode(String postalCode) {
        driver.findElement(postalCodeField).clear();
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    // METHOD: fillShippingInfo
    // Purpose: Fills ALL 3 fields at once — convenience method
    // Parameters: firstName, lastName, postalCode
    // Tests call this single method instead of 3 separate ones
    public void fillShippingInfo(String firstName,
                                  String lastName,
                                  String postalCode) {
        enterFirstName(firstName);   // fills first name field
        enterLastName(lastName);     // fills last name field
        enterPostalCode(postalCode); // fills postal code field
    }

    // METHOD: clickContinue
    // Purpose: Clicks Continue button to go from step 1 to step 2
    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    // METHOD: clickCancel
    // Purpose: Clicks Cancel button to go back to cart page
    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }

    // METHOD: isErrorDisplayed
    // Purpose: Checks if validation error message is shown
    // Return type: boolean (true = error shown, false = no error)
    // Used to verify form validation works correctly
    public boolean isErrorDisplayed() {
        try {
            // isDisplayed() returns true if element is visible
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            // Element not found means no error shown
            return false;
        }
    }

    // METHOD: getErrorMessage
    // Purpose: Returns text of the error message
    // Return type: String
    public String getErrorMessage() {
        // getText() reads visible text of error element
        return driver.findElement(errorMessage).getText();
    }

    // =====================================================
    // STEP 2 METHODS — actions on overview page
    // =====================================================

    // METHOD: getItemTotal
    // Purpose: Returns subtotal text before tax
    // Return type: String
    // Example return: "Item total: $29.99"
    public String getItemTotal() {
        return driver.findElement(itemTotal).getText();
    }

    // METHOD: getTaxAmount
    // Purpose: Returns tax amount text
    // Return type: String
    // Example return: "Tax: $2.40"
    public String getTaxAmount() {
        return driver.findElement(taxLabel).getText();
    }

    // METHOD: getTotalPrice
    // Purpose: Returns final total price text including tax
    // Return type: String
    // Example return: "Total: $32.39"
    public String getTotalPrice() {
        return driver.findElement(totalLabel).getText();
    }

    // METHOD: clickFinish
    // Purpose: Clicks Finish button to complete the order
    // Takes user from step 2 to step 3 (confirmation page)
    public void clickFinish() {
        driver.findElement(finishButton).click();
    }

    // METHOD: completeCheckout
    // Purpose: Fills form AND completes entire checkout in one call
    // Convenience method combining step 1 and step 2 actions
    // Parameters: firstName, lastName, postalCode
    public void completeCheckout(String firstName,
                                  String lastName,
                                  String postalCode) {
        fillShippingInfo(firstName, lastName, postalCode); // fill step 1
        clickContinue();  // go to step 2
        clickFinish();    // complete order on step 2
    }

    // =====================================================
    // STEP 3 METHODS — actions on confirmation page
    // =====================================================

    // METHOD: getConfirmationHeader
    // Purpose: Returns the main confirmation heading text
    // Return type: String
    // Expected return: "Thank you for your order!"
    public String getConfirmationHeader() {
        return driver.findElement(confirmationHeader).getText();
    }

    // METHOD: getConfirmationText
    // Purpose: Returns the sub message below confirmation heading
    // Return type: String
    // Expected return: "Your order has been dispatched..."
    public String getConfirmationText() {
        return driver.findElement(confirmationText).getText();
    }

    // METHOD: clickBackHome
    // Purpose: Clicks Back Home button after order completion
    // Takes user back to products/inventory page
    public void clickBackHome() {
        driver.findElement(backHomeButton).click();
    }

    // METHOD: isOrderComplete
    // Purpose: Confirms order was placed successfully
    // Return type: boolean
    public boolean isOrderComplete() {
        // getCurrentUrl() returns current browser URL as String
        // .contains("checkout-complete") checks if on confirmation page
        return driver.getCurrentUrl().contains("checkout-complete");
    }

    // METHOD: isOnCheckoutStepOne
    // Purpose: Confirms browser is on step 1 page
    // Return type: boolean
    public boolean isOnCheckoutStepOne() {
        return driver.getCurrentUrl().contains("checkout-step-one");
    }

    // METHOD: isOnCheckoutStepTwo
    // Purpose: Confirms browser is on step 2 overview page
    // Return type: boolean
    public boolean isOnCheckoutStepTwo() {
        return driver.getCurrentUrl().contains("checkout-step-two");
    }

}