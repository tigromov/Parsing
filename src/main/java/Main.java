import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, NullPointerException {

        //////Создание промежуточного exel файла
        Workbook midWb = new XSSFWorkbook();
        Sheet midSheet = midWb.createSheet();
        FileOutputStream fos = new FileOutputStream("C:/Rest/ParsingMid2.xls");//изменить имя файла
//        //////
//        int time = 1000;
//        int counter = 0;

        /////запуск Chrome driver
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver webDriver = new ChromeDriver(options);

//        webDriver.get("https://kaspi.kz/shop/search/?text=music%20park&q=:allMerchants:Musicpark");

///////////////////////////////////регистрация////////////////////////////////////////////////////////////////////

        webDriver.get("https://kaspi.kz/merchantcabinet/");
        Thread.sleep(5000);
        webDriver.findElement(By.id("email")).sendKeys("zakaz@invask.kz");
        webDriver.findElement(By.id("password")).sendKeys("!ZakaZ2022" + Keys.ENTER);
        Thread.sleep(5000);
        webDriver.findElement(By.id("main-nav-offers")).click();
        Thread.sleep(15000);
        /////////////////////////////////размер списка товар(откуда брать?)
        int productsListSize = 1;
        int prodCounter = 0;
        /////////////////////////////////
        for (int i = 0; i < productsListSize; i++) {
            Document doc = Jsoup.parse(webDriver.getPageSource());
            //////добавление первых 10 sku в exel файл
            Elements skus = doc.getElementsByAttributeValue("title", "Артикул в системе продавца");
            int skusListLenght = skus.size();
            ArrayList skuList = new ArrayList();
            for (Element element: skus) {
                skuList.add(element.text());
            }
            System.out.println(skuList);/////////////тест
          ////////////добавление в файл наших артикулов
            for (int j = 0; j < skusListLenght; j++) {
              midSheet.createRow(j+1).createCell(0).setCellValue((String) skuList.get(j));}

            Elements products = doc.getElementsByClass("offer-managment__product-cell-link");
            for (Element prod:products) {


                webDriver.get(prod.attr("href"));
                Thread.sleep(3000);
                /////закрытие окна выбора города////
//                WebElement btnClose = webDriver.findElement(By.xpath("//*[@id=\"dialogService\"]/div/div[1]/div[2]/i"));
//                btnClose.click(); Thread.sleep(1000);
                //////////////////////////////////
                ///создание названия и цены
                Document document2 = Jsoup.parse(webDriver.getPageSource());
                String name = document2.getElementsByClass("item__heading").text();
                midSheet.getRow(prodCounter+1).createCell(1).setCellValue(name);
                Elements els = document2.getElementsByClass("sellers-table__price-cell-text");
                Element els2 = els.first();
                String priceLowest = els2.text();
                midSheet.getRow(prodCounter+1).createCell(2).setCellValue(priceLowest);



                prodCounter++;
            }
            webDriver.get("https://kaspi.kz/merchantcabinet/#/offers");
            Thread.sleep(10000);
            webDriver.findElement(By.id("main-nav-offers")).click();
            Thread.sleep(30000);
            Actions move = new Actions(webDriver);

            //"aria-label=\"Next page\""
///html/body/div[5]/div[3]/div/div[4]/table/tbody/tr/td[4]/img
            List<WebElement> btnNext = webDriver.findElements(By.className("gwt-Image"));
            System.out.println(btnNext);
            move.moveToElement(btnNext.get(3));
            Thread.sleep(1000);
            JavascriptExecutor js = (JavascriptExecutor)webDriver;
            js.executeScript("window.scrollBy(0,250)","");
            btnNext.get(3).click();


        }








/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//       ///close Choose your City
//        WebElement btnClose = webDriver.findElement(By.xpath("//*[@id=\"dialogService\"]/div/div[1]/div[2]/i"));
//        btnClose.click(); Thread.sleep(time);
//        ///
//
//
//
//        for (int i = 1; i < 3; i++) {
//           webDriver.get("https://kaspi.kz/shop/search/?text=music%20park&q=:allMerchants:Musicpark&page=" + i +"");
//           Thread.sleep(time + 3000);
//
//
//            Document documentGlobal = Jsoup.parse(webDriver.getPageSource());
//            Elements wrappers = documentGlobal.getElementsByClass("item-card__image-wrapper");
//
//
//            for (Element wr : wrappers) {
//
//                counter++;
//                webDriver.get(wr.attr("href"));
//                Thread.sleep(time);
//                Document document2 = Jsoup.parse(webDriver.getPageSource());
//                Elements els = document2.getElementsByClass("sellers-table__price-cell-text");
//                Element els2 = els.first();
//                String name = document2.getElementsByClass("item__heading").text();
//                String sku = document2.getElementsByClass("item__sku").text().substring(12);
//                String price = els2.text();
//
//
//                try{
//
//                System.out.println(counter + " " + name + " " + price + " артикул каспи: " + sku);
//                Thread.sleep(time);} catch (NullPointerException e) {
//                    System.out.println(counter + " " + name + " Нет актуальных предложений по данному товару");
//                }
//                midSheet.createRow(counter-1).createCell(0).setCellValue(name);
//                midSheet.getRow(counter-1).createCell(1).setCellValue(price);
//                midSheet.getRow(counter-1).createCell(2).setCellValue(sku);
//
//
//            }
//        }
//
        //webDriver.close();
        midWb.write(fos);
        fos.close();







    }
}
