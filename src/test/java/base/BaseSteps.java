package base;

import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;


public class BaseSteps extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseSteps.class);

    private static String SAVED_ATTRIBUTE;

    private Actions actions = new Actions(webDriver);
    private ApiTestingPost apiTestingpost = new ApiTestingPost();

    public static final int DEFAULT_WAIT = 10;

    public static final int MIN_WAIT = 5;

    public static final int MAX_WAIT = 20;
    private static EmailUtils emailUtils;
    private static String authCode;

    public BaseSteps() {
        initMap(readJsonFile());
    }


//    private WebElement findElement(String key) {
//        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
//        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
//        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 5);
//        WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));
//        ((JavascriptExecutor) webDriver).executeScript(
//                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
//                webElement);
//        return webElement;
//    }
//
//    private List<WebElement> findElements(String key) {
//        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
//        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
//        return webDriver.findElements(infoParam);
//    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        return webDriver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }



    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }


    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by) {
        return webDriver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return webDriver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }

    public WebElement getElementExists(WebElement element) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                logger.info(" elementi bulundu.");
                return element;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: doesn't exist.");
        return null;
    }


    @Step("Print page source")
    public void printPageSource() {
        System.out.println(getPageSource());
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait for element then click <key>",
            "Elementi bekle ve sonra tıkla <key>"})
    public void checkElementExistsThenClick(String key) {
        getElementWithKeyIfExists(key);
        clickElement(key);
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
            WebElement element = findElement(key);
            clickElement(element);
            logger.info(key + " elementine tıklandı.");

    }

    @Step({"Click to element <key> with focus",
            "<key> elementine focus ile tıkla"})
    public void clickElementWithFocus(String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile tıklandı.");
    }

    @Step({"Check if element <key> exists",
            "Wait for element to load with key <key>",
            "Element var mı kontrol et <key>",
            "Elementin yüklenmesini bekle <key>"})
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElement(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }


    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        webDriver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Wait for element to load with css <css>",
            "Elementin yüklenmesini bekle css <css>"})
    public void waitElementLoadWithCss(String css) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(By.cssSelector(css)).size() > 0) {
                logger.info(css + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + css + "' doesn't exist.");
    }

    @Step({"Wait for element to load with xpath <xpath>",
            "Elementinin yüklenmesini bekle xpath <xpath>"})
    public void waitElementLoadWithXpath(String xpath) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(By.xpath(xpath)).size() > 0) {
                logger.info(xpath + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + xpath + "' doesn't exist.");
    }

    @Step({"Check if element <key> exists else print message <message>",
            "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(message);
    }

    @Step({"Check if element <key> not exists",
            "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element '" + key + "' still exist.");
    }

    @Step({"Upload file in project <path> to element <key>",
            "Proje içindeki <path> dosyayı <key> elemente upload et"})
    public void uploadFile(String path, String key) {
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyası " + key + " elementine yüklendi.");
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void sendKeys(String text, String key) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
    }

    @Step({"Click with javascript to css <css>",
            "Javascript ile css tıkla <css>"})
    public void javascriptClickerWithCss(String css) {
        Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.cssSelector(css)));
        javaScriptClicker(webDriver, webDriver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " tıklandı.");
    }

    @Step({"Click with javascript to xpath <xpath>",
            "Javascript ile xpath tıkla <xpath>"})
    public void javascriptClickerWithXpath(String xpath) {
        Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.xpath(xpath)));
        javaScriptClicker(webDriver, webDriver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " tıklandı.");
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = webDriver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step({"Send TAB key to element <key>",
            "Elemente TAB keyi yolla <key>"})
    public void sendKeyToElementTAB(String key) {
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yollandı.");
    }

    @Step({"Send DOWN key to element <key>",
            "Elemente DOWN keyi yolla <key>"})
    public void sendKeyToElementDOWN(String key) {
        findElement(key).sendKeys(Keys.DOWN);
        logger.info(key + " elementine DOWN keyi yollandı.");
    }

    @Step({"Send BACKSPACE key to element <key>",
            "Elemente BACKSPACE keyi yolla <key>"})
    public void sendKeyToElementBACKSPACE(String key) {
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandı.");
    }

    @Step({"Send ESCAPE key to element <key>",
            "Elemente ESCAPE keyi yolla <key>"})
    public void sendKeyToElementESCAPE(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yollandı.");
    }

    @Step({"Send ENTER key to element <key>",
            "Elemente ENTER keyi yolla <key>"})
    public void sendKeyToElementENTER(String key) {
        findElement(key).sendKeys(Keys.ENTER);
        logger.info(key + " elementine ENTER keyi yollandı.");
    }

    @Step({"Check if element <key> has attribute <attribute>",
            "<key> elementi <attribute> niteliğine sahip mi"})
    public void checkElementAttributeExists(String key, String attribute) {
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step({"Check if element <key> not have attribute <attribute>",
            "<key> elementi <attribute> niteliğine sahip değil mi"})
    public void checkElementAttributeNotExists(String key, String attribute) {
        WebElement element = findElement(key);

        int loopCount = 0;

        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
            "<key> elementinin <attribute> niteliği <value> değerine sahip mi"})
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't match expected value");
    }

    @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
            "<key> elementinin <attribute> niteliği <value> değerini içeriyor mu"})
    public void checkElementAttributeContains(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't contain expected value");
    }

    @Step({"Write <value> to <attributeName> of element <key>",
            "<value> değerini <attribute> niteliğine <key> elementi için yaz"})
    public void setElementAttribute(String value, String attributeName, String key) {
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    @Step({"Write <value> to <attributeName> of element <key> with Js",
            "<value> değerini <attribute> niteliğine <key> elementi için JS ile yaz"})
    public void setElementAttributeWithJs(String value, String attributeName, String key) {
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')", webElement);
    }

    @Step({"Clear text of element <key>",
            "<key> elementinin text alanını temizle"})
    public void clearInputArea(String key) {
        findElement(key).clear();
    }

    @Step({"Clear text of element <key> with BACKSPACE",
            "<key> elementinin text alanını BACKSPACE ile temizle"})
    public void clearInputAreaWithBackspace(String key) {
        WebElement element = findElement(key);
        element.clear();
        element.sendKeys("a");
        actions.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    @Step({"Save attribute <attribute> value of element <key>",
            "<attribute> niteliğini sakla <key> elementi için"})
    public void saveAttributeValueOfElement(String attribute, String key) {
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    @Step({"Write saved attribute value to element <key>",
            "Kaydedilmiş niteliği <key> elementine yaz"})
    public void writeSavedAttributeToElement(String key) {
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {
        String keytextim = getElementText(key);
        Boolean containsText = getElementText(key).contains(expectedText);
        assertTrue("Expected text is not contained", containsText);
    }

    @Step({"Write random value to element <key>",
            "<key> elementine random değer yaz"})
    public void writeRandomValueToElement(String key) {
        findElement(key).sendKeys(randomString(15));
    }

    @Step({"Write random value to element <key> starting with <text>",
            "<key> elementine <text> değeri ile başlayan random değer yaz"})
    public void writeRandomValueToElement(String key, String startingText) {
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    @Step({"Print element text by css <css>",
            "Elementin text değerini yazdır css <css>"})
    public void printElementText(String css) {
        System.out.println(webDriver.findElement(By.cssSelector(css)).getText());
    }

    @Step({"Write value <string> to element <key> with focus",
            "<string> değerini <key> elementine focus ile yaz"})
    public void sendKeysWithFocus(String text, String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
        logger.info(text + " focus ile yazıldı");
    }

    @Step({"Refresh page",
            "Sayfa yenilenir"})
    public void refreshPage() {
        webDriver.navigate().refresh();
        waitByMilliSeconds(5);
    }

    @Step({"Change page zoom to <value>%",
            "Sayfanın zoom değerini değiştir <value>%"})
    public void chromeZoomOut(String value) {
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    @Step({"Open new tab",
            "Yeni sekme aç"})
    public void chromeOpenNewTab() {
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
    }

    @Step({"Focus on tab number <number>",
            "<number> numaralı sekmeye odaklan"})//Starting from 1
    public void chromeFocusTabWithNumber(int number) {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(number - 1));
    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Step({"Focus on frame with <key>",
            "Frame'e odaklan <key>"})
    public void chromeFocusFrameWithNumber(String key) {
        WebElement webElement = findElement(key);
        webDriver.switchTo().frame(webElement);
    }

    @Step({"Accept Chrome alert popup",
            "Chrome uyarı popup'ını kabul et"})
    public void acceptChromeAlertPopup() {
        webDriver.switchTo().alert().accept();
    }


    //----------------------SONRADAN YAZILANLAR-----------------------------------

    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) webDriver;
    }

    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement webElement = webDriver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 70);
        }
        return webElement;
    }

    @Step({"scroll to the <key> area",
            "<key> alanına kaydır"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
    }


    @Step({"<key> alanına js ile kaydır"})
    public void scrollToElementWithJs(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = webDriver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }


    @Step({"<length> uzunlugunda random bir kelime üret ve <saveKey> olarak sakla"})
    public void createRandomString(int length, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey, randomString(length));
    }


    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan degeri yazdir",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSendTextByKey(String key, String saveKey) throws InterruptedException {
        WebElement element = null;
        int waitVar = 0;
        element = findElement(key);
        while (true) {
            if (element.isDisplayed()) {
                logger.info("WebElement is found at: " + waitVar + " second.");
                element.clear();
                StoreHelper.INSTANCE.getValue(saveKey);
                element.sendKeys(StoreHelper.INSTANCE.getValue(saveKey));

                break;
            } else {
                waitVar = waitVar + 1;
                Thread.sleep(1000);
                if (waitVar == 20) {
                    throw new NullPointerException(String.format("by = %s Web element list not found"));
                } else {
                }
            }
        }
    }

    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomeMail(String key) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElement(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@testinium.com");

    }

    @Step({"<key> li elementi bul, temizle ve rasgele isim değerini yaz",
            "Find element by <key> clear and send keys random isim"})
    public void RandomeName(String key) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElement(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@testinium.com");

    }

    @Step("Rastgele telefon no üret")
    public String rastgelTelNoGelsin() {
        Vector<Integer> array = new Vector<Integer>();
        Random randomGenerator = new Random();
        array.add(new Integer(1 + randomGenerator.nextInt(9)));
        for (int i = 1; i < 9; i++) array.add(randomGenerator.nextInt(10));
        int t1 = 0;
        for (int i = 0; i < 9; i += 2) t1 += array.elementAt(i);
        int t2 = 0;
        for (int i = 1; i < 8; i += 2) t2 += array.elementAt(i);
        int x = ((t1 * 7) - t2) % 10;
        array.add(new Integer(x));
        x = 0;
        for (int i = 0; i < 10; i++) x += array.elementAt(i);
        x = x % 10;
        array.add(new Integer(x));
        String res = "";
        for (int i = 0; i < 10; i++) res = res + Integer.toString(array.elementAt(i));
        return res;
    }

    @Step({"write the phone number to <key> the element",
          "Telefon noyu <key> elementine yaz"})
    public void setRandomTelno(String key) {
        String rastgeleTcno = rastgelTelNoGelsin();
        sendKeys(rastgeleTcno, key);
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> olarak sakla",
            "Find element by <key> and save text <saveKey>"})
    public void saveTextByKey(String key, String saveKey) throws InterruptedException {
        Thread.sleep(1000);
        StoreHelper.INSTANCE.saveValue(saveKey, getElementText(key));
        Thread.sleep(2000);

    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değeri içeriyor mu kontrol et",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKeyContain(String key, String saveKey) {
        Assert.assertTrue(StoreHelper.INSTANCE.getValue(saveKey).contains(getElementText(key)));
    }


    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır ve değişiklik oldugunu dogrula",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKeyNotequal(String key, String saveKey) {
        Assert.assertNotEquals(StoreHelper.INSTANCE.getValue(saveKey), getElementText(key));
    }

    @Step({"<key> li elementi bul, temizle ve <text> değerini yaz",
            "Find element by <key> clear and send keys <text>"})
    public void sendKeysByKey(String key, String text) {

        WebElement webElement = findElement(key);
        webElement.clear();
        webElement.sendKeys(text);
    }

    @Step({"<key> li elementi bul, temizle",
            "Find element by <key> clear "})
    public void sendKeysByKey2(String key) {
        WebElement webElement = findElement(key);
        webElement.clear();
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKey(String key, String saveKey) {
        Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), getElementText(key));
    }

    public String randomName(int stringLength) {
        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {
            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }
        return stringRandom;
    }

    @Step({"<key> elementine random isim yaz"})
    public void writeRandomNameToElement(String key) {
        findElement(key).sendKeys(randomName(3));
    }

    @Step({"<key> li elementin değeri <text> e eşitliğini kontrol et",
            "Find element by <key> and text equals <text>"})
    public void equalsTextByKey(String key, String text) {
        Assert.assertEquals(text, findElement(key).getText());
    }


    @Step({"Elementin yüklenmesini bekle ve tıkla <key>"})
    public WebElement getElementWithKeyIfExists2(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElement(key);
                logger.info(key + " elementi bulundu.");
                actions.moveToElement(findElement(key));
                actions.click();
                actions.build().perform();
                logger.info(key + " elementine focus ile tıklandı.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }

    @Step({"Wait for element then click <key> try",
            "Elementi bekle ve sonra tıkla <key> try"})
    public void checkElementExistsThenClickTryCatch(String key) {
        try {
            clickElement(key);
        } catch (Exception e) {
            logger.info("Tek test şubesi var");
        }

    }

    int waitVar = 0;


    @Step("<key> li elementi bul ve <text> değerini tek tek yaz")
    public void sendKeysValueOfClear(String key, String text) {
        WebElement me = findElement(key);
        me.clear();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            me.sendKeys(String.valueOf(c));
        }
        System.out.println("'" + text + "' texti '" + key + "' elementine yazıldı");

    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz, emaili <savekey> olarak sakla",
    })
    public void RandomeMailAndSave(String key, String saveKey) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElement(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@testinium.com");
        String randomMail = "testotomasyon" + timestamp + "@testinium.com";
        StoreHelper.INSTANCE.saveValue(saveKey, randomMail);

    }

    @Step({"<key> li elementi bul ve varsa dokun",
            "Click element by <key> if exist"})
    public void existTapByKey(String key) {

        WebElement element = null;
        try {
            element = findElement(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            element.click();
        }
    }


    @Step({"Hide keyboard",
            "Klavyeyi kapat"})
    public static void HideKeyboard() {
        webDriver.hideKeyboard();
    }


    @Step("<key> olarak <text> seçersem")
    public void implementation1(String key, String text) throws InterruptedException {
        List<WebElement> comboBoxElement = findElements(key);
        for (int i = 0; i < comboBoxElement.size(); i++) {
            Thread.sleep(3);
            String texts = comboBoxElement.get(i).getText();

            if (texts.contains(text)) {
                comboBoxElement.get(i).click();
            }
        }
        logger.info(key + " comboboxından " + text + " değeri seçildi");


    }

    @Step({"Choose <value> day later from <key>",
            "<key> degerinden <value> gün sonrasını sec"})
    public void chooseValueFromCalendar(String key, int value) throws InterruptedException {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String selected = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) + value);
        //Bu methodun döndürdüğü değerler 1-Pazar 2-Pazartesi ... 6-Cuma 7-Cumartesi şeklindedir.
        List<WebElement> columns = findElements(key);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(selected));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        int i = 0;
        Thread.sleep(1000);
        for (WebElement cell : columns) {
            if (cell.getText().equals(selected)) {
                if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
                    columns.get(i).click();
                } else {
                    columns.get(i + 1).click();
                    break;
                }
            }
            i++;
        }
        logger.info("Date selected successfully");
    }

    @Step("<key> olarak <text> seçersemm")
    public void olarakSeçersemm(String key, String text) {

        List<WebElement> anchors = findElements(key);
        Iterator<WebElement> i = anchors.iterator();
        while (i.hasNext()) {
            WebElement anchor = i.next();
            if (anchor.getText().contains(text)) {
                anchor.click();
                break;
            }
        }

    }

    @Step("<key> olarak <index> indexi seçersem")
    public void olarakIndexiSecersemm(String key, String index) throws InterruptedException {

        List<WebElement> anchors = findElements(key);
        Thread.sleep(4000);
        WebElement anchor = anchors.get(Integer.parseInt(index));
        getElementExists(anchor);
        javascriptclicker(anchor);
    }

    @Step("<key> olarak comboboxdan bir değer seçilir")
    public void comboboxRandom(String key) throws InterruptedException {

        List<WebElement> comboBoxElement = findElements(key);
        Thread.sleep(4000);
        int randomIndex = new Random().nextInt(comboBoxElement.size());
        System.out.println("minan " + comboBoxElement.size());
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", comboBoxElement.get(randomIndex));
        logger.info(key + " comboboxından herhangi bir değer seçildi");


    }

    @Step("Elementi bekle ve tıkla <key>")
    public void ListofElementsLoaded(String key) {
        WebElement element = findElement(key);
        WebDriverWait wait = new WebDriverWait(webDriver, 120);
        wait.until(ExpectedConditions.visibilityOf(element));
        javascriptclicker(element);

    }

    @Step("Elementini bekle <key>")
    public void LoadedofElement(String key) {
        WebElement element = findElement(key);
        WebDriverWait wait = new WebDriverWait(webDriver, 120);
        wait.until(ExpectedConditions.visibilityOf(element));
        logger.info(" elementi bulundu" + key);

    }


    @Step("Tarih olarak günün tarihinden <gun> gün sonrasını seç")
    public void implementation2(String gun) throws InterruptedException {
        String key = "Gun_Seçimi";
        int keyint = Integer.parseInt(gun);

        WebElement calenderButton = webDriver.findElement(By.xpath("//span[@class='k-icon k-i-calendar']"));
        calenderButton.click();
        String todayString = getCurrentDay();
        int todayint = Integer.parseInt(todayString);
        List<WebElement> allWorkingDays = findElements(key);
        Thread.sleep(3000);
        if (todayint + keyint > 30 || todayint == 31 || keyint >= allWorkingDays.size()) {
            javaScriptClicker(webDriver, webDriver.findElement(By.cssSelector(".k-link.k-nav-next")));
        }

        List<WebElement> newallWorkingDays = findElements(key);
        Thread.sleep(3000);
        WebElement clickDayElement = newallWorkingDays.get(keyint);

        javascriptclicker(clickDayElement);


    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", element);
    }


    private String getCurrentDay() {
        //Create a Calendar Object
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //Get Current Day as a number
        int todayInt = calendar.get(Calendar.DAY_OF_MONTH);
        //Integer to String Conversion
        String todayStr = Integer.toString(todayInt);
        return todayStr;
    }

    @Step("<key> elementine javascript ile tıkla")
    public void elementeJSileTikla(String key) {
        WebElement element = findElement(key);
        javascriptclicker(element);
        logger.info(key + " elementine javascript ile tıklandı");
    }


    public void doubleclick(WebElement elementLocator) {
        Actions actions = new Actions(webDriver);
        actions.doubleClick(elementLocator).perform();
    }

    @Step("<key> elementine çift tıkla")
    public void doubleclickElement(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            doubleclick(element);
            logger.info(key + " elementine çift tıklandı.");
        }
    }

    @Step("Sayfadaki <key> elementi <value> değerini içerir")
    public void attributeExistWithValue(String key, String value) {
        WebElement element = findElement(key);
        LoadedofElement(key);
        assertTrue(element.getText().contains(value));
        logger.info(key + "elementi" + value + " değerini içerir");
    }

    @Step("<username> kullanıcısını api üzerinden sil")
    public void ClearUserRequest(String username) {
        RestAssured.baseURI = "https://b2bqa.etasimacilik.com/TestQAService/api/v1";
        apiTestingpost.setUserName(username);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(apiTestingpost).log().all()
                .when().post("/qaservice/cleanCustomerMembership")
                .prettyPeek().then().statusCode(200).extract().response();
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Step("Tarih olarak günün tarihinden <gun> gün sonrasını <BenimTarih> olarak kaydedersem")
    public String calenderrr(String gun, String BenimTarih) {

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTime(currentDate);
        c.add(Calendar.DATE, Integer.parseInt(gun)); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        Date currentDatePlusOne = c.getTime();
        StoreHelper.INSTANCE.saveValue(BenimTarih, dateFormat.format(currentDatePlusOne));
        return BenimTarih;

    }


    @Step("<degisken> değişkenini <key> elementine yaz")
    public void sendKeysVariable(String degisken, String key) {
        if (!key.equals("")) {
            //clearInputArea(key);
            findElement(key).sendKeys(degisken);
            logger.info(key + " elementine " + degisken+ " texti yazıldı.");
        }
    }


    @Step("Eğer <key> element sayfada ise tıkla")
    public void ifElementExistClick(String key) {

        List<WebElement> element = findElements(key);
        if (element.size() != 0) {
            clickElementWithFocus(key);
        }
    }

    @Step("<key> elementine <text> değerini js ile yaz")
    public void elementeJSileYaz(String key, String text) {
        WebElement element = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].value=arguments[1]", element, text);
        logger.info(key + " elementine " + text + " değeri js ile yazıldı.");
    }


    @Step("Scrollu sayfanın sonuna kaydır")
    public void scrolldownBottomofPage() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

    }

    @Step("<key> alanını javascript ile temizle")
    public void clearWithJS(String key) {
        WebElement element = findElement(key);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value ='';", element);

    }

    @Step("<taxNumber> Vergi numarasına sahip kullanıcı için post metodu ile <BenimTalepNumaram> Talep Numarası oluştur")
    public String talepOlusturVeFiyatOnayıVer(String taxNumber, String BenimTalepNumaram) {

        apiTestingpost.setAccountTaxNumber(taxNumber);
        RestAssured.baseURI = "https://b2bqa.etasimacilik.com/TestQAService/api/v1";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(apiTestingpost).log().all().
                        post("/qaservice/createPriceConfirmedQuote").prettyPeek().then().statusCode(200).extract().response();
        String BenimTalepNo = response.path("Data.QuoteNumber");

        logger.info("BenimTalepNumaram olarak " + BenimTalepNo + " nolu talep oluşturuldu");
        StoreHelper.INSTANCE.saveValue(BenimTalepNumaram, BenimTalepNo);
        logger.info(BenimTalepNo + "  BenimTalepNumaram olarak kaydedildi");
        return BenimTalepNumaram;

    }

    @Step("<key> elementleri arasından <text> kayıtlı değişkene tıkla")
    public void clickParticularElement(String key, String text) {

        List<WebElement> anchors = findElements(key);
        Iterator<WebElement> i = anchors.iterator();
        while (i.hasNext()) {
            WebElement anchor = i.next();
            int count = 1;
            if (anchor.getText().contains(StoreHelper.INSTANCE.getValue(text))) {
                scrollToElementToBeVisiblest(anchor);
                String xpath = "//button[@class='btn green add-basket cardBtn'][" + count + "]";
                javascriptClickerWithXpath(xpath);
            }
            count++;
        }
    }

    public void scrollToElementToBeVisiblest(WebElement webElement) {
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
    }

    @Step("Siparis durmunu <key> elementli kartla <kartDurumu> ve <keyToCompare> elementini kullanarak karsilastir")
    public void SiparisDurumuKarsilastir(String key, String kartDurumu, String keyToCompare) throws InterruptedException {
        WebElement webElement1 = findElement(key);
        WebElement webElement = findElement(kartDurumu);
        logger.info(" webelement bulundu");
        String tedarikDurumuKart = webElement.getText();
        logger.info(tedarikDurumuKart + " texti bulundu");
        webElement1.click();
        Thread.sleep(10000);
        logger.info(" webelement1 tıklandı");
        WebElement cardDetail = findElement(keyToCompare);
        logger.info("cardDetail elementi bulundu");
        String tedarikDurumuDetay = cardDetail.getText();
        logger.info(tedarikDurumuDetay + " texti bulundu");
        Assert.assertTrue(tedarikDurumuKart.equals(tedarikDurumuDetay));
        logger.info(tedarikDurumuKart + " textiyle " + tedarikDurumuDetay + " texti karşılaştırıldı.");
    }

    @Step("<IlkKart> in icerdiği <key> elementiyle <keyToCompare> elementini seferlerim icin karsilastir")
    public void SeferlerimTalepNumarasıKarsilastir(String IlkKart, String key, String keyToCompare) throws InterruptedException {
        logger.info(IlkKart + " " + key + " " + keyToCompare + " texti bulundu");
        Thread.sleep(4000);
        WebElement webElement = findElement(key);
        String firstCardTalepNo = webElement.getText();
        logger.info(firstCardTalepNo + " texti bulundu");
        firstCardTalepNo = firstCardTalepNo.substring(9, 19);
        logger.info(firstCardTalepNo + " texti bulundu");
        WebElement ilkKartElement = findElement(IlkKart);
        ilkKartElement.click();
        logger.info(" tıklandı bulundu ");
        Thread.sleep(4000);
        WebElement cardDetail = findElement(keyToCompare);
        String detailPage = cardDetail.getText();
        String compareText = detailPage.substring(14, 24);
        System.out.println(firstCardTalepNo);
        System.out.println(compareText);
        Assert.assertTrue(firstCardTalepNo.equals(compareText));
        logger.info(firstCardTalepNo + " textiyle " + compareText + " texti karşılaştırıldı.");
    }

    @Step("<text> textini <key> elemente tek tek yaz")
    public void sendKeyOneByOne(String text, String key) throws InterruptedException {

        WebElement field = findElement(key);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                findElement(key).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yazıldı.");
        }
    }

    @Step("<key> elementiyle <cardDetail> elemetindeki <keyToCompare> textini karsilastir")
    public void AracTalepleriTalepNumarasıKarsilastir(String key, String cardDetail, String keyToCompare) throws InterruptedException {
        WebElement webelement = findElement(key);
        String cardTalepNo = webelement.getText();
        logger.info(cardTalepNo + " texti bulundu ");
        cardTalepNo = cardTalepNo.substring(32, 44);
        logger.info(cardTalepNo + " texti bulundu ");
        WebElement webelement1 = findElement(cardDetail);
        webelement1.click();
        logger.info(" tıklandı bulundu ");
        Thread.sleep(4000);
        WebElement detail = findElement(keyToCompare);
        String detailPage = detail.getText();
        String compareText = detailPage.substring(15, 28);
        System.out.println(cardTalepNo);
        System.out.println(compareText);
        Assert.assertTrue(compareText.contains(cardTalepNo));
        logger.info(cardTalepNo + " textiyle " + compareText + " texti karşılaştırıldı.");
    }

    @Step("<key> tarihinden 2 gün sonraya al")
    public void tarihAl(String key) {
        List<WebElement> elements = findElements(key);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().equals(tarihSec())) {
                elements.get(i).click();
            }
        }
    }

    public String tarihSec() {
        Calendar now = Calendar.getInstance();
        int tarih = now.get(Calendar.DATE) + 2;
        return String.valueOf(tarih);
    }


    @Step("Comboboxtan <text> seçersem")
    public void ComboboxSelecting(String text) {

        WebElement element = webDriver.findElement(By.xpath("//*[text()='" + text + "']"));
        LoadedofWebElement(element);
        clickElement(element);

    }

    public void LoadedofWebElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver, 120);
        wait.until(ExpectedConditions.visibilityOf(element));
        logger.info(" elementi bulundu  " + element);

    }

@Step("Key li <key> ementler arasından rasgele bir tanesine tıkla")
    public void randomSec(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }


    @Step("Hamburger manüye tıkla")
    public void ClickHamburgerManu() throws InterruptedException {
        Thread.sleep(1000);
        webDriver.findElement(By.id("openMenu")).click();
        Thread.sleep(2000);
    }

    @Step("Kadın kategortisini seç")
    public void Selectcathegory() throws InterruptedException {
        webDriver.findElement(By.linkText("Kadın")).click();
        Thread.sleep(1000);
}
@Step("Arama alanına <key> bilgisini yaz")
public void  searchKeyy(String key) throws InterruptedException {
    Thread.sleep(1000);webDriver.findElement(By.id("div[class='m-search-box']")).click();
    Thread.sleep(1000);
    webDriver.findElement(By.cssSelector("input[name='search']")).sendKeys(key);
    Thread.sleep(2000);

}
@Step("Ürün seç")
    public void selectProduct() throws InterruptedException {
        webDriver.findElement(By.cssSelector("div[class='productList']>div:nth-of-type(2)>div")).click();
    Thread.sleep(2000);
}

@Step("Debug Step")
    public void debug() throws InterruptedException {
        Thread.sleep(1000);
}
    @Step("Connect to gmail with <email> and <password>")
    public static void connectToEmail(String email, String password) {
        try {
            emailUtils = new EmailUtils("imap.gmail.com","imap",email,password);
            authCode = emailUtils.getAuthCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Step("Write verification code to <element>")
    public void testVerificationCode(String element) {
        try {
            findElement(element).sendKeys(authCode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't get verification code from gmail");
        }
    }



//----------------------SONRADAN YAZILANLAR PART 2-----------------

    /*@Step("Tarih olarak günün tarihinden <gun> gün sonrasını <BenimTarih> olarak kaydedersem")
    public String getpasseddatefromCalender(String targetDate, String BenimTarih) {

       // Date currentDate = new Date();
        Calendar calender= Calendar.getInstance();
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(("dd-MMM-yyyy"));
        Date formattedTargetDate;
        try {
            targetDateFormat.setLenient(false);
            formattedTargetDate = targetDateFormat.parse(targetDate);
            calender.setTime(formattedTargetDate);

            int targetDay = calender.get(Calendar.DAY_OF_MONTH);
            int targetMonth = calender.get(Calendar.MONTH);
            int targetYear = calender.get(Calendar.YEAR);

            webDriver.findElement(By.xpath(""));
            String actualDate = webDriver.findElements(By.xpath("")).getText();
            calender.setTime(new SimpleDateFormat("MMM yyyy").parse(actualDate));

            int actualMonth = calender.get(Calendar.MONTH);
            int actualYear = calender.get(Calendar.YEAR);

            while (targetMonth < actualMonth || targetYear < actualYear ) {
                webDriver.findElement(By.xpath(""));
                actualDate = webDriver.findElement(By.xpath("")).getText();
                calender.setTime(new SimpleDateFormat("MMM yyyy").parse(actualDate));

                actualMonth = calender.get(Calendar.MONTH);
                actualYear = calender.get(Calendar.YEAR);
            }

        } catch (ParseException e) {
           throw new Exception("Invalid date is provided, please check Input date");
        }


    }*/

}

