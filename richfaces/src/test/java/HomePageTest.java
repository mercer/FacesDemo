import be.cegeka.rsvz.LocaleBean;
import be.cegeka.rsvz.ui.BaseTest;
import be.cegeka.rsvz.ui.HomePage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.ExtendedHtmlUnitDriver;

import java.util.Locale;
import java.util.ResourceBundle;

public class HomePageTest extends BaseTest {
    private static final String[] BROWSER_LOCALES = {"en", "el", "fr", "en-GB", "nl", "nl-BE"};

    @Test
    public void localesShouldBeAvailable() {
        for (String locale : BROWSER_LOCALES) {
            ExtendedHtmlUnitDriver localeDriver = new ExtendedHtmlUnitDriver();
            localeDriver.setHeader("Accept-Language", locale);
            localeDriver.get(BaseTest.BASE_URL);
            WebElement element = localeDriver.findElement(By.id("languageLabel"));
            Locale referenceLocale = LocaleBean.extractLocale(locale);
            ResourceBundle res = ValidationMessages.getBundle("be.cegeka.rsvz.faces.i18n.messages", referenceLocale);
            Assert.assertEquals(res.getString("language"), element.getText());
            localeDriver.quit();
        }
    }

    @Test
    public void unknownLocaleShouldFallBackToEn() {
        ExtendedHtmlUnitDriver localeDriver = new ExtendedHtmlUnitDriver();
        localeDriver.setHeader("Accept-Language", "ro");
        localeDriver.get(BaseTest.BASE_URL);
        WebElement element = localeDriver.findElement(By.id("languageLabel"));
        Locale referenceLocale = LocaleBean.extractLocale("en");
        ResourceBundle res = ValidationMessages.getBundle("be.cegeka.rsvz.faces.i18n.messages", referenceLocale);
        Assert.assertEquals(res.getString("language"), element.getText());
        localeDriver.quit();
    }

    @Test
    public void localeChangeShouldChangeTheLocale() throws Exception {
        getDriver().get(BaseTest.BASE_URL);
        Assert.assertTrue(new HomePage(getDriver()).changeLanguage());
    }

}
