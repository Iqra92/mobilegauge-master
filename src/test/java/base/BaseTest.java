package base;

import WebAutomationBase.helper.FileResourcesUtils;
import WebAutomationBase.model.ElementInfo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public static AppiumDriver webDriver;
    ChromeOptions options = new ChromeOptions();
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DEFAULT_DIRECTORY_PATH = "json";
    ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();


    @BeforeScenario
    public static void setUp() throws MalformedURLException, Exception {
        System.out.println("*****************Test*****************");
        String selectPlatform = "android";
        String BaseUrl = "https://m-test.texsportbet.com/";
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (StringUtils.isEmpty(System.getenv("key")))
        {
            System.out.println("LOCAL");
            if ("android".equalsIgnoreCase(selectPlatform)) {
                System.out.println("android");
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
                capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                webDriver = new AndroidDriver(url,capabilities);
                webDriver.get(BaseUrl);
                Thread.sleep(5000);

        }else if ("ios".equalsIgnoreCase(selectPlatform)) {
                //capabilities = new DesiredCapabilities();
                capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.4.1");
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 7");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.UDID, "00008030-000475A92E39802E");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                webDriver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                webDriver.get(BaseUrl);

                for (Cookie cookie : webDriver.manage().getCookies()) {
                    System.out.println("Cooooookie " + cookie);
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }   System.out.println("key cookie value is " + webDriver.manage().getCookieNamed("key"));
                webDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);

            }

        } else {

            String cookieValue = System.getenv("cookie_value");
            System.out.println("cookie value from testinium is " + cookieValue);
            System.out.println("TESTINIUM");
            if ("ANDROID".equals(System.getenv("platform"))) {
                ChromeOptions options = new ChromeOptions();
                String versionOfDevice = System.getenv("version");
                capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability("rotatable", true);
                options.addArguments("test-type");
                options.addArguments("ignore-certificate-errors");
                options.addArguments("disable-translate");
                options.addArguments("--dns-prefetch-disable");
                options.addArguments("--disable-notifications");
                //options.setExperimentalOption("w3c",false);
                //options.addArguments("--host-resolver-rules=MAP *.useinsider.* 127.0.0.1");
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                //    capabilities.setCapability("appPackage", "com.android.chrome");
                //  capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
                capabilities.setCapability("unicodeKeyboard", true);
                capabilities.setCapability("resetKeyboard", true);
                capabilities.setCapability("noReset", true);
                webDriver = new AndroidDriver<>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
                webDriver.get(BaseUrl);
                //String versionOfDevice = System.getenv("version");
                System.out.println("Version of running device is " + versionOfDevice);
                // driver.manage().deleteAllCookies();

                }
             else {
                System.out.println("IOS - Mobile Web");
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability("xcodeOrgId", "PMLH8MF4G9");
                capabilities.setCapability("xcodeSigningId", "iPhone Developer");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.SAFARI);
                capabilities.setCapability("cleanSession", true);
                capabilities.setCapability("ensureCleanSession", true);
                capabilities.setCapability("technologyPreview", true);
                webDriver = new IOSDriver<WebElement>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
                webDriver.get(BaseUrl);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                }
                for (Cookie cookie : webDriver.manage().getCookies()) {
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }

            webDriver.navigate().refresh();

            webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);


                   }
            }
        }

//    @AfterScenario
//    public void tearDown() throws Exception {
//        webDriver.quit();
//    }

    public void initMap(File[] fileList) {
        Type elementType = new TypeToken<List<ElementInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList = null;
        for (File file : fileList) {
            try {
                elementInfoList = gson
                        .fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream()
                        .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {
                logger.warn("{} not found", e);
            }
        }
    }

//    public File[] getFileList() {
      /*  System.out.println("*****************check*****************");

        File fileList = new File("D:\\Iqra Project\\mobilewebgauge-master\\src\\test\\resources\\json\\elementValues");
//        File fileList = new File("D:\\Iqra Project\\mobilewebgauge-master\\src\\test\\resources\\json\\elementValues");
        File[] fileList2 = new File(
                this.getClass().getResource(DEFAULT_DIRECTORY_PATH).getPath())
                .listFiles(pathname ->  pathname.getName().startsWith("elementValues"));
//        System.out.println("*****************check2*****************"+fileList2.getClass().getName());

        if (fileList2 == null) {
//            System.out.println("*****************check2*****************"+fileList2.toPath().toString());

            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                    DEFAULT_DIRECTORY_PATH);
            throw new NullPointerException();

        }*/
//        System.out.println("*****************check3*****************"+fileList.list().length+"");

//        return readJsonFile();
//    }
    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }
    public  File[] readJsonFile() {

        try {
            FileResourcesUtils fileHelper = new FileResourcesUtils();
            // files from src/main/resources/json
            List<File> result = fileHelper.getAllFilesFromResource("json");
            for (File file : result) {
                System.out.println("file : " + file);
//                app.printFile(file);
            }
         return    result.toArray(new File[result.size()]);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;

        }
    }
}

