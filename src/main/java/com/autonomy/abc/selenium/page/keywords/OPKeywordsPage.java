 package com.autonomy.abc.selenium.page.keywords;

import com.autonomy.abc.selenium.keywords.KeywordFilter;
import com.autonomy.abc.selenium.language.LanguageDropdown;
import com.autonomy.abc.selenium.language.OPLanguageDropdown;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OPKeywordsPage extends KeywordsPage {
    public OPKeywordsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void deleteAllBlacklistedTerms() throws InterruptedException {
        filterView(KeywordFilter.BLACKLIST);

        for (final String language : getLanguageList()) {
            loadOrFadeWait();

            selectLanguage(language);

            for (final WebElement blacklisted : findElements(By.cssSelector(".blacklisted-word .remove-keyword"))) {
                scrollIntoView(blacklisted, getDriver());
                blacklisted.click();
                waitForRefreshIconToDisappear();
            }
        }
    }

    @Override
    public void selectLanguage(String language)  {
        if (!getSelectedLanguage().equals(language.toUpperCase())) {
            loadOrFadeWait();
            getParent(selectLanguageButton()).click();
            loadOrFadeWait();
            final WebElement element = findElement(By.cssSelector(".keywords-filters")).findElement(By.xpath(".//a[text()='" + language + "']"));
            // IE doesn't like clicking dropdown elements
            final JavascriptExecutor executor = (JavascriptExecutor)getDriver();
            executor.executeScript("arguments[0].click();", element);
            loadOrFadeWait();
        }
    }

    @Override
    protected LanguageDropdown languageDropdown() {
        return new OPLanguageDropdown(findElement(By.cssSelector(".languages-select-view-container .dropdown:nth-of-type(2)")), getDriver());
    }
}
