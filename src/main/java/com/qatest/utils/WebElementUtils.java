/**
 * 
 */
package com.qatest.utils;

import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Divyang
 *
 */
public class WebElementUtils {

	/**
	 * 
	 */
	public WebElementUtils() {
		// TODO Auto-generated constructor stub
	}

	// Example of a robust wait utility
	public WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutInSeconds) throws TimeoutException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	// FluentWait is superior to WebDriverWait because it lets you ignore specific exceptions while polling
	public WebElement fluentWait(WebDriver driver, By locator) {
	    Wait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(30))
	            .pollingEvery(Duration.ofMillis(500))
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);

	    return wait.until(driver2 -> driver.findElement(locator));
	}
}
