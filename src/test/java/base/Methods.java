package base;

import com.thoughtworks.gauge.Step;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.util.List;
import java.util.Random;

import static WebAutomationBase.helper.Constant.*;

public class Methods extends BaseSteps {

    @Step("Click join button")
    public void joinbt() {
        WebElement element = webDriver.findElement(By.xpath(JOIN_BUTTON));
        element.click();

    }

    @Step("Check if element exists")
    public WebElement getElementWithKeyIfExistss() {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = webDriver.findElement(By.xpath(PHONE_NUMBER_CODE));
                logger.info( " element is found.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + PHONE_NUMBER_CODE + "' doesn't exist.");
        return null;
    }

    @Step("Click phone code")
    public void phoneCode() {
        WebElement element = webDriver.findElement(By.xpath(PHONE_NUMBER_CODE));
        element.click();

    }

    @Step("Select phone code")
    public void phoneCodeSelect() {
        WebElement element = webDriver.findElement(By.xpath(PHONE_NUMBER_SELECT));
        element.click();

    }

    @Step("Write random Int value to element")
    public void writeRandomIntValueToElement() {
        WebElement element = webDriver.findElement(By.xpath(PHONE_NUMBER));
        element.sendKeys(randomNumber(9));
    }


    @Step("Click currency button")
    public void currencybt() {
        WebElement element = webDriver.findElement(By.xpath(CURRENCY));
        element.click();
    }

    @Step("Find element by clear and send keys  random email")
    public void RandomMail() {
        Long timestamp = getTimestamp();
        WebElement webElement = webDriver.findElement(By.xpath(EMAIL));
        webElement.clear();
        webElement.sendKeys("test" + timestamp + "@testinium.com");

    }

    @Step({"Write random Alpha value to element starting with <text>"})
    public void writeRandomAlphaValueToElement(String startingText) {
        String value = RandomStringUtils.randomAlphabetic(5);
        webDriver.findElement(By.xpath(FIRST_NAME)).sendKeys(startingText+ value);
        logger.info("The text was written to the field as: " + startingText + value);
    }

    @Step({"Write random Alpha value to element Last Name starting with <text>"})
    public void writeRandomAlphaValueToElementLastName(String startingText) {
        String value = RandomStringUtils.randomAlphabetic(5);
        webDriver.findElement(By.xpath(LAST_NAME)).sendKeys(startingText+ value);
        logger.info("The text was written to the field as: " + startingText + value);
    }

    @Step("Write Birth Day Int value to element")
    public void writeIntValueToElement() {
        WebElement element = webDriver.findElement(By.xpath(DATE_OF_BIRTH));
        element.sendKeys("08.12.1992");
    }

    @Step("Write Password value to element")
    public void writePasswordToElement() {
        WebElement element = webDriver.findElement(By.xpath(PASSWORD));
        element.sendKeys("1q2w3e4r5t");
    }


}