package com.autonomy.abc.endtoend;

import com.autonomy.abc.config.HostedTestBase;
import com.autonomy.abc.config.TestConfig;
import com.autonomy.abc.selenium.config.ApplicationType;
import com.autonomy.abc.selenium.find.FindPage;
import com.autonomy.abc.selenium.find.Service;
import com.autonomy.abc.selenium.menu.NavBarTabId;
import com.autonomy.abc.selenium.page.analytics.AnalyticsPage;
import com.autonomy.abc.selenium.page.analytics.Term;
import com.autonomy.abc.selenium.page.keywords.CreateNewKeywordsPage;
import com.autonomy.abc.selenium.page.keywords.KeywordsPage;
import com.autonomy.abc.selenium.page.promotions.CreateNewPromotionsPage;
import com.autonomy.abc.selenium.page.promotions.PromotionsPage;
import com.autonomy.abc.selenium.page.search.SearchPage;
import com.autonomy.abc.selenium.promotions.PromotionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.autonomy.abc.framework.ABCAssert.assertThat;
import static com.autonomy.abc.framework.ABCAssert.verifyThat;
import static com.autonomy.abc.matchers.ElementMatchers.hasClass;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.lift.Matchers.displayed;

//CSA-1590
public class AnalyticsToFindITCase extends HostedTestBase {
    private PromotionService promotionService;

    public AnalyticsToFindITCase(TestConfig config, String browser, ApplicationType type, Platform platform) {
        super(config, browser, type, platform);
    }

    @Before
    public void setUp(){
        promotionService = getApplication().createPromotionService(getElementFactory());

        PromotionsPage promotions = getElementFactory().getPromotionsPage();
        browserHandles = promotions.createAndListWindowHandles();
        getDriver().switchTo().window(browserHandles.get(1));
        getDriver().get(config.getFindUrl());
        getDriver().manage().window().maximize();
        find = getElementFactory().getFindPage();
        service = find.getService();
        getDriver().switchTo().window(browserHandles.get(0));
    }

    private FindPage find;
    private Service service;
    private List<String> browserHandles;

    @Test
    public void testPromotionToFind() throws InterruptedException {
        body.getSideNavBar().switchPage(NavBarTabId.ANALYTICS);

        AnalyticsPage analyticsPage = getElementFactory().getAnalyticsPage();
        body = getBody();

        Term zeroSearch = analyticsPage.getMostPopularZeroSearchTerm();
        Term nonZero = analyticsPage.getMostPopularNonZeroSearchTerm();

        if(nonZero == null){
            nonZero = new Term("replacement",0);
        }

        String searchTerm = nonZero.getTerm();
        String trigger = "Trigger";
        String synonym = zeroSearch.getTerm();

        promotionService.deleteAll();

        body = getBody();

        body.getTopNavBar().search(searchTerm);
        SearchPage searchPage = getElementFactory().getSearchPage();
        List<String> createdPromotions = searchPage.createAMultiDocumentPromotion(3);
        CreateNewPromotionsPage createNewPromotionsPage = getElementFactory().getCreateNewPromotionsPage();
        createNewPromotionsPage.addSpotlightPromotion("Spotlight", trigger);
        getElementFactory().getSearchPage(); //Wait for search page

        body.getSideNavBar().switchPage(NavBarTabId.KEYWORDS);
        KeywordsPage keywordsPage = getElementFactory().getKeywordsPage();
        keywordsPage.createNewKeywordsButton().click();
        CreateNewKeywordsPage createNewKeywordsPage = getElementFactory().getCreateNewKeywordsPage();
        createNewKeywordsPage.createSynonymGroup(trigger + " " + synonym, "English");

        getElementFactory().getSearchPage();

        getDriver().switchTo().window(browserHandles.get(1));
        find.search(trigger);

        List<String> triggerResults = service.getResultTitles();

        find.search(synonym);

        List<String> findPromotions = service.getPromotionsTitles();

        verifyThat(findPromotions.size(), not(0));
        verifyThat(findPromotions, containsInAnyOrder(createdPromotions.toArray()));
        verifyThat(service.getResultTitles(), contains(triggerResults.toArray()));

        for(WebElement promotion : service.getPromotions()){
            promotionShownCorrectly(promotion);
        }
    }

    private void promotionShownCorrectly (WebElement promotion) {
        assertThat(promotion, hasClass("promoted-document"));
        assertThat(promotion.findElement(By.className("promoted-label")).getText(),containsString("Promoted"));
        assertThat(promotion.findElement(By.className("icon-star")), displayed());
    }

    @After
    public void tearDown(){
        getDriver().switchTo().window(browserHandles.get(0));

        promotionService.deleteAll();

        body.getSideNavBar().switchPage(NavBarTabId.KEYWORDS);
        KeywordsPage keywordsPage = getElementFactory().getKeywordsPage();
        keywordsPage.deleteKeywords();
    }

}