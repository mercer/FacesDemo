package be.cegeka.rsvz.ui;

import be.cegeka.rsvz.LocaleBean;
import be.cegeka.rsvz.faces.i18n.UTF8ResourceBundle;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.ExtendedHtmlUnitDriver;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FirstPageTest {
    private static final int PORT = 9999;
    private static final String CONTEXT = "facesdemo";
    private static final String BASE_URL = "http://localhost:" + PORT + "/" + CONTEXT;
    private static final String[] LOCALES = {"en", "el", "fr", "nl", "en-GB"};

    private GlassFish glassfish;
    private WebDriver driver;

    @Before
    public void startServer() throws Exception {
        GlassFishProperties gfProps = new GlassFishProperties();
        gfProps.setPort("http-listener", PORT);
        glassfish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
        glassfish.start();
        File webRoot = new File(getLocalPath() + "/src/main/webapp");
        File classes = new File(getLocalPath() + "/target/classes");
        ScatteredArchive archive = new ScatteredArchive(CONTEXT, ScatteredArchive.Type.WAR, webRoot);
        archive.addClassPath(classes);
        glassfish.getDeployer().deploy(archive.toURI());

        driver = new ExtendedHtmlUnitDriver();
    }

    @Test
    public void allLocalesShouldBeAvailable() {
        for (String locale : LOCALES) {
            ExtendedHtmlUnitDriver localeDriver = new ExtendedHtmlUnitDriver();
            localeDriver.setHeader("Accept-Language", locale);
            localeDriver.get(BASE_URL);
            WebElement element = localeDriver.findElement(By.id("languageLabel"));
            Locale referenceLocale = LocaleBean.extractLocale(locale);
            ResourceBundle res = UTF8ResourceBundle.getBundle("be.cegeka.rsvz.faces.i18n.messages", referenceLocale);
            Assert.assertEquals(res.getString("language"), element.getText());
            localeDriver.quit();
        }
    }

    private String getLocalPath() throws IOException {
        String canonicalPath = new File(".").getCanonicalPath();
        if (canonicalPath.indexOf("richfaces") > 0) {
            return canonicalPath;
        } else {
            return canonicalPath + "/richfaces";
        }
    }

}
