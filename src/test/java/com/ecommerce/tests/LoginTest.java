// PACKAGE declaration — this file belongs to tests group
package com.ecommerce.tests;

// Import our own page classes we created earlier
// LoginPage — has all login page actions and locators
import com.ecommerce.pages.LoginPage;

// InventoryPage — has all products page actions and locators
import com.ecommerce.pages.InventoryPage;

// BaseTest — has setUp() and tearDown() methods
// LoginTest EXTENDS BaseTest to inherit browser setup
import com.ecommerce.utils.BaseTest;

// Assert — TestNG class used to verify expected vs actual results
// If assertion fails, test is marked as FAILED in report
import org.testng.Assert;

// Test annotation — marks a method as a test case
// TestNG finds all @Test methods and runs them automatically
import org.testng.annotations.Test;

// PUBLIC CLASS — accessible from TestNG runner
// EXTENDS BaseTest — inherits setUp() BeforeMethod
//                    and tearDown() AfterMethod
// So Chrome opens before each @Test and closes after automatically
public class LoginTest extends BaseTest {

    // =====================================================
    // TEST CASE 1 — Valid Login
    // =====================================================

    // @Test annotation — tells TestNG this is a test method
    // description — explains what this test verifies
    // shown in HTML report
    @Test(description = "Valid user should login successfully")
    public void testValidLogin() {

        // Create LoginPage object
        // Pass browser instance using getDriver() from BaseTest
        // Now loginPage can interact with login page elements
        LoginPage loginPage = new LoginPage(getDriver());

        // Call login() method — types username, password and clicks login
        // "standard_user" and "secret_sauce" are valid credentials
        loginPage.login("standard_user", "secret_sauce");

        // ASSERTION — verify test passed or failed
        // Assert.assertTrue() — passes if condition is TRUE
        // fails if condition is FALSE
        // After valid login, URL should contain "inventory"
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            // This message shows in report if assertion FAILS
            "After valid login URL should contain inventory"
        );
    }

    // =====================================================
    // TEST CASE 2 — Locked Out User
    // =====================================================

    @Test(description = "Locked out user should see error message")
    public void testLockedOutUser() {

        // Create LoginPage object with browser
        LoginPage loginPage = new LoginPage(getDriver());

        // Try to login with locked_out_user credentials
        // This user is blocked by saucedemo intentionally
        loginPage.login("locked_out_user", "secret_sauce");

        // ASSERTION 1 — error message should be displayed
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Locked out user should see error message"
        );

        // ASSERTION 2 — error message should contain "locked out"
        // Assert.assertTrue with .contains() checks partial text match
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("locked out"),
            "Error message should mention locked out"
        );
    }

    // =====================================================
    // TEST CASE 3 — Empty Username
    // =====================================================

    @Test(description = "Empty username should show error")
    public void testEmptyUsername() {

        LoginPage loginPage = new LoginPage(getDriver());

        // Pass empty string "" as username
        // password is valid but username is blank
        loginPage.login("", "secret_sauce");

        // Error message should appear for empty username
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Empty username should show error message"
        );

        // Error text should say Username is required
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username is required"),
            "Error should say Username is required"
        );
    }

    // =====================================================
    // TEST CASE 4 — Empty Password
    // =====================================================

    @Test(description = "Empty password should show error")
    public void testEmptyPassword() {

        LoginPage loginPage = new LoginPage(getDriver());

        // Pass valid username but empty string "" as password
        loginPage.login("standard_user", "");

        // Error message should appear for empty password
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Empty password should show error message"
        );

        // Error text should say Password is required
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Password is required"),
            "Error should say Password is required"
        );
    }

    // =====================================================
    // TEST CASE 5 — Wrong Password
    // =====================================================

    @Test(description = "Wrong password should show error")
    public void testWrongPassword() {

        LoginPage loginPage = new LoginPage(getDriver());

        // Valid username but completely wrong password
        loginPage.login("standard_user", "wrongpassword123");

        // Should stay on login page — error should appear
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Wrong password should show error message"
        );

        // URL should NOT contain inventory — login failed
        Assert.assertFalse(
            getDriver().getCurrentUrl().contains("inventory"),
            "Wrong password should not redirect to inventory"
        );
    }

    // =====================================================
    // TEST CASE 6 — Wrong Username
    // =====================================================

    @Test(description = "Wrong username should show error")
    public void testWrongUsername() {

        LoginPage loginPage = new LoginPage(getDriver());

        // Non existent username with valid password
        loginPage.login("wrong_user_xyz", "secret_sauce");

        // Error should appear for wrong username
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Wrong username should show error message"
        );
    }

    // =====================================================
    // TEST CASE 7 — Both Fields Empty
    // =====================================================

    @Test(description = "Both empty fields should show error")
    public void testBothFieldsEmpty() {

        LoginPage loginPage = new LoginPage(getDriver());

        // Pass empty string for both username and password
        loginPage.login("", "");

        // Error should appear when both fields are empty
        Assert.assertTrue(
            loginPage.isErrorMessageDisplayed(),
            "Both empty fields should show error"
        );

        // Username error appears first — check for it
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username is required"),
            "Error should mention Username is required first"
        );
    }

    // =====================================================
    // TEST CASE 8 — Problem User Login
    // =====================================================

    @Test(description = "Problem user should login but UI has bugs")
    public void testProblemUserLogin() {

        LoginPage loginPage = new LoginPage(getDriver());

        // problem_user can login but product images are broken
        loginPage.login("problem_user", "secret_sauce");

        // Login should still succeed — URL should have inventory
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            "Problem user should reach inventory page"
        );
    }

    // =====================================================
    // TEST CASE 9 — Successful Logout
    // =====================================================

    @Test(description = "User should be able to logout successfully")
    public void testLogout() {

        // First login with valid credentials
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "secret_sauce");

        // Verify login was successful first
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            "Should be on inventory page before logout"
        );

        // Create InventoryPage object to access logout method
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        // Call logout() — opens menu and clicks logout link
        inventoryPage.logout();

        // After logout URL should be back to base login URL
        // Assert.assertEquals — checks exact match of two values
        // First param = actual value
        // Second param = expected value
        // Third param = message shown if fails
        Assert.assertEquals(
            getDriver().getCurrentUrl(),
            BASE_URL,
            "After logout user should be back on login page"
        );
    }

    // =====================================================
    // TEST CASE 10 — Performance Glitch User
    // =====================================================

    @Test(description = "Performance glitch user login should succeed")
    public void testPerformanceGlitchUser() {

        LoginPage loginPage = new LoginPage(getDriver());

        // performance_glitch_user logs in slowly but should succeed
        loginPage.login("performance_glitch_user", "secret_sauce");

        // Despite slow loading, should reach inventory page
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("inventory"),
            "Performance glitch user should reach inventory page"
        );
    }

}

