package com.testinium.test.automation.tests;


import com.testinium.test.automation.Bean;
import com.testinium.test.automation.Utils;
import com.testinium.test.automation.abstracts.TestAbstracts;
import jdk.jshell.execution.Util;
import jxl.write.WriteException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class MainPageTest extends TestAbstracts implements Runnable {

    public MainPageTest(){
        super(MainPageTest.class);
    }

    public boolean start() throws InterruptedException, WriteException, IOException {
        this.logger.info("Test Başlatıldı");
        gotoMainPage();
        if(validMainPage()){
            if(mouseHoverCiltBakim())
                if(gotoChildLink()) if(selectFilter())if(selectProductAndExcelWrite()){
                    //sepete ekleyince giriş sayfasına gidilmiyor.
                    return true;
                }
        }
        return false;
    }

    public boolean selectProductAndExcelWrite() throws WriteException, IOException {
        List<WebElement> facetLinks = Bean.webDriver.findElements(By.cssSelector(".product-list-wrapper app-custom-product-grid-item"));
        if(facetLinks.size()<0) {
            logger.error("Seçilen filtre için ürün bulunamadı");
            return false;
        }
        WebElement product= facetLinks.get(0);
        String  productName  =product.findElement(By.cssSelector(" .product-cards .infos .cx-product-name .title")).getAttribute("innerHTML");
        String  productCost  =product.findElement(By.cssSelector(" .product-cards .infos .product-price")).findElements(By.cssSelector("span")).stream().map(x->x.getAttribute("innerHTML")).reduce("",String::concat).toString();
        Utils.exelWrite(productName,productCost);
        logger.info("Excele yazdırıldı");
        WebElement elenmt= product.findElement(By.cssSelector("button.add-to-basket"));
        elenmt.submit();
        logger.info("sepete eklendi");
        LoginPageTest loginPageTest=new LoginPageTest();
        loginPageTest.gotoPage();
        loginPageTest.setUsername(productName);
        loginPageTest.setpassword(productCost);
        return true;
    }

    public boolean selectFilter() throws InterruptedException {
        List<WebElement> facetLinks = Bean.webDriver.findElements(By.cssSelector("app-custom-facet-list a.facet-value-link"));
        int randomLinkIndex1= 3;


        WebElement elem1=facetLinks.get(randomLinkIndex1);

        String selectedLink= elem1.getAttribute("href");
        String filter1Name=elem1.findElement(By.cssSelector(".facet-value-content span.label")).getAttribute("innerHTML");
        Bean.webDriver.navigate().to(selectedLink);
        if(!Bean.webDriver.getCurrentUrl().equals(Utils.encodeUri(selectedLink))){
            logger.error("Seçilen Linke Gidilemedi. Link:"+selectedLink);
            return false;
        }
        else{
            logger.info( filter1Name+" filtresi aktif edildi");
        }
        facetLinks = Bean.webDriver.findElements(By.cssSelector("app-custom-facet-list a.facet-value-link"));
        int randomLinkIndex2=  2;
        WebElement elem2=facetLinks.get(randomLinkIndex2);
        selectedLink= elem2.getAttribute("href");
        String filter2Name=elem2.findElement(By.cssSelector(".facet-value-content span.label")).getAttribute("innerHTML");
        Bean.webDriver.navigate().to(selectedLink);
        if(!Bean.webDriver.getCurrentUrl().equals(Utils.encodeUri(selectedLink))){
            logger.error("Seçilen Linke Gidilemedi. Link:"+selectedLink);
            return false;
        }
        else{
            logger.info( filter2Name+" filtresi aktif edildi");
        }

        return selectedFilterControl(new String[]{
                filter1Name,
                filter2Name
        });
    }

    public boolean selectedFilterControl(String[] filterNames){
        List<WebElement> facetLinks = Bean.webDriver.findElements(By.cssSelector(".filter-container li.ng-star-inserted"));
        AtomicInteger counter= new AtomicInteger();
        facetLinks.stream().forEach(x->{
            boolean flag=false;
            for(String valueLink :filterNames){
               flag =flag || (x.getAttribute("innerHTML").contains(valueLink));
            }
            if(!flag){
               logger.warn("Tıklanmayan bir filtre var. Filtre:"+x.getText());
            }
            else{
                counter.addAndGet(1);
            }
        });
        return filterNames.length== counter.get();
    }

    public void gotoMainPage(){
        Bean.webDriver.get(Bean.BASE_URI);
        this.logger.info("Anasayfaya gidildi");
    }
    public boolean validMainPage(){
        boolean flag=Bean.webDriver.getCurrentUrl().equals(Utils.encodeUri(Bean.BASE_URI));
        if(!flag) logger.error("Anasayfaya Gidilemedi");
        return flag;
    }

    public boolean gotoChildLink(){
        WebElement mainMenu = Bean.webDriver.findElement(By.cssSelector("li.category-502"));
        List<WebElement> childLinks=  mainMenu.findElements(By.tagName("a"));
        if(childLinks.size()>0){
           int randomLinkIndex=  2;
            WebElement selectedMenu= childLinks.get(randomLinkIndex);
            String selectedLink= selectedMenu.getAttribute("href");
            while (selectedMenu.getText().isEmpty()){
                 randomLinkIndex= 5;
                 selectedMenu= childLinks.get(randomLinkIndex);
                 selectedLink= selectedMenu.getAttribute("href");
            }
            logger.info(selectedMenu.getText()+"'in linkine gidiliyor. Link: "+selectedLink);
            Bean.webDriver.navigate().to(selectedLink);
            if(!Bean.webDriver.getCurrentUrl().equals(Utils.encodeUri(selectedLink))){
                logger.error("Seçilen Linke Gidilemedi.");
                return false;
            }
        }
        else { logger.error("Menüde gidilecek link bulunamadı"); return false;}
        return true;

    }


    public boolean mouseHoverCiltBakim(){
        boolean flag=false;
        try{
            Actions actions = new Actions(Bean.webDriver);
            WebElement mainMenu = Bean.webDriver.findElement(By.cssSelector("li.category-502"));
            actions.moveToElement(mainMenu).perform();
            flag=true;
            this.logger.info("Cilt Bakım Menüsü Açıldı.");
        }catch (Exception ex){
            this.logger.error("Cilt Bakım Menüsü Mouse Hover Eventi Çalıştırılamadı");
            flag=false;
        }
        return flag;
    }




    public static void main(String[] args) {
        MainPageTest mainPageTest=new MainPageTest();
        mainPageTest.run();
    }

    @Override
    public void run() {
        try {
            start();
            Bean.webDriver.quit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (WriteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
