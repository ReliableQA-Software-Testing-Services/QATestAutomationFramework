/**
 * 
 */
package com.qatest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

//import com.qatest.base.BaseTest;

/**
 * @author Divyang
 *
 */
//public class LoginPage extends BaseTest {
public class LoginPage {
    // 1. Locators using Page Factory
    @FindBy(name = "login")
    private WebElement txtUsername;

    @FindBy(name = "password")
    private WebElement txtPassword;

    @FindBy(xpath = "//input[@value='Sign in']")
    private WebElement btnLogin;

    @FindBy(id = "flash")
    private WebElement errorMessage;

    // 2. Constructor to initialize elements
    public LoginPage(WebDriver driver) {
        // 'this' refers to the current class members
        PageFactory.initElements(driver, this);
    }

    // 3. Actions (Methods)
    public void enterUsername(String user) {
        txtUsername.sendKeys(user);
    }

    public void enterPassword(String pwd) {
        txtPassword.sendKeys(pwd);
    }

    public void clickLogin() {
        btnLogin.click();
    }

    // A Wrapper method for cleaner tests
    public void login(String user, String pwd) {
        enterUsername(user);
        enterPassword(pwd);
        clickLogin();
    }
}
