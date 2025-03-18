package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.baseRule.BaseRule;
import site.nomoreparties.stellarburgers.pages.HomePage;
import site.nomoreparties.stellarburgers.pages.LoginPage;
import site.nomoreparties.stellarburgers.pages.RegisterPage;
import site.nomoreparties.stellarburgers.user.UserConstructor;
import site.nomoreparties.stellarburgers.user.UserRandom;
import site.nomoreparties.stellarburgers.user.UserSteps;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserRegisterTest extends BaseRule {
    public static final String INVALID_PASSWORD = "123z5";
    private UserConstructor userConstructor;
    private HomePage homePage;
    private RegisterPage registerPage;
    private LoginPage loginPage;
    private UserSteps userSteps;


    @Before
    public void setUp() {
        userConstructor = UserRandom.getUserRandom();
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Register user by valid data, through the \"log in to account\" button")
    public void registerUserByValidData() {
        homePage.clickButtonAccount();
        loginPage.clickRegisterLink();
        registerPage.fillRegisterForm(userConstructor.getName(),
                userConstructor.getEmail(), userConstructor.getPassword());
        boolean isDisplayed = registerPage.buttonRegisterIsDisplayed();
        String text = loginPage.getTextLoginForm();
        assertTrue(isDisplayed);
        assertThat("There was no transition to the login page",
                text, containsString("Вход"));
    }

    @Test
    @DisplayName("Register user by invalid password - 5 symbol")
    public void registerUserNotValidPassword() {
        homePage.clickButtonAccount();
        loginPage.clickRegisterLink();
        registerPage.fillRegisterForm(userConstructor.getName(), userConstructor.getEmail(), INVALID_PASSWORD);
        String text = registerPage.getTextErrorPassword();
        assertThat("Incorrect error about incorrect password ",
                text, containsString("Некорректный пароль"));
    }

    @After
    @Step("Delete user")
    public void userDelete() {
        String accessToken = homePage.getAccessToken();
        if(!Objects.isNull(accessToken)) {
            userSteps.userDelete(StringUtils.substringAfter(accessToken, ""));
        }
    }
}
