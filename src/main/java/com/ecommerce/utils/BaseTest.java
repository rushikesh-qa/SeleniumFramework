// PACKAGE declaration
package com.ecommerce.utils;

// WebDriver — main interface representing browser
import org.openqa.selenium.WebDriver;

// ChromeDriver — Chrome specific implementation
import org.openqa.selenium.chrome.ChromeDriver;

// ChromeOptions — configure Chrome browser settings
import org.openqa.selenium.chrome.ChromeOptions;

// WebDriverManager — auto downloads correct ChromeDriver
import io.github.bonigarcia.wdm.WebDriverManager;

// TestNG annotations
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

// Arrays — needed for excludeSwitches option
import java.util.Arrays;

// PUBLIC CLASS — parent class for all test classes
// All test classes extend this class
public class BaseTest {

    // ThreadLocal — gives each thread its own WebDriver
    // protected — child test classes can access this
    protected static ThreadLocal<WebDriver> driver
        = new ThreadLocal<>();

    // Base URL — target website constant
    // static final — shared across all tests, never changes
    protected static final String BASE_URL
        = "https://www.saucedemo.com/";

    // @BeforeMethod — runs before EVERY @Test method
    // Opens Chrome browser fresh for each test
    @BeforeMethod
    public void setUp() {

        // Force download fresh ChromeDriver for Chrome 146
        // clearDriverCache — removes old cached driver files
        // clearResolutionCache — removes version mapping cache
        // browserVersion — specifies exact Chrome version
        WebDriverManager.chromedriver()
            .clearDriverCache()
            .clearResolutionCache()
            .browserVersion("146")
            .setup();

        // ChromeOptions — configure Chrome behavior
        ChromeOptions options = new ChromeOptions();

        // Removes "Chrome is controlled by automation" bar
        options.addArguments(
            "--disable-blink-features=AutomationControlled"
        );

        // Opens browser maximized — all elements visible
        options.addArguments("--start-maximized");

        // Disables Chrome extensions — prevents interference
        options.addArguments("--disable-extensions");

        // Disables GPU rendering — prevents display issues
        options.addArguments("--disable-gpu");

        // Disables sandbox — needed on some Windows systems
        options.addArguments("--no-sandbox");

        // Disables shared memory — prevents Chrome crashes
        options.addArguments("--disable-dev-shm-usage");

        // Fixes WebSocket connection issues in Chrome 146
        options.addArguments("--remote-allow-origins=*");

        // Removes automation switches from Chrome flags
        options.setExperimentalOption(
            "excludeSwitches",
            Arrays.asList("enable-automation")
        );

        // Create new Chrome browser instance with options
        WebDriver webDriver = new ChromeDriver(options);

        // Store driver in ThreadLocal for this thread
        driver.set(webDriver);

        // Navigate to the target website
        getDriver().get(BASE_URL);

        // Wait up to 10 seconds for elements to appear
        getDriver().manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(10)
        );
    }

    // PUBLIC STATIC METHOD — getDriver()
    // Returns the WebDriver for current thread
    // Called by all test classes as getDriver()
    // IMPORTANT: must be public static — NOT private
    public static WebDriver getDriver() {
        // driver.get() returns THIS thread's WebDriver instance
        return driver.get();
    }

    // @AfterMethod — runs after EVERY @Test method
    // Closes browser after each test completes
    @AfterMethod
    public void tearDown() {
        // null check — prevents error if setUp() failed
        if (getDriver() != null) {
            // quit() closes ALL windows and ends WebDriver session
            getDriver().quit();
            // remove() cleans ThreadLocal to prevent memory leak
            driver.remove();
        }
    }
}
