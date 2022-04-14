
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;



public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, NullPointerException{

        //////Создание промежуточного exel файла
        Workbook midWb = new HSSFWorkbook();
        Sheet midSheet = midWb.createSheet();
        FileOutputStream fos = new FileOutputStream("C:/Rest/ParsingMid2New.xls" );//изменить имя файла
//        CellStyle style = midWb.createCellStyle();
//        style.setFillBackgroundColor(IndexedColors.RED.getIndex());
//        style.setFillPattern(FillPatternType.BIG_SPOTS);
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

        try {
            webDriver.get("https://kaspi.kz/merchantcabinet/");
        }catch (NoSuchElementException exception){
            webDriver.navigate().refresh();
            Thread.sleep(2000);

        }

        Thread.sleep(5000);
        webDriver.findElement(By.id("email")).sendKeys("zakaz@invask.kz");
        webDriver.findElement(By.id("password")).sendKeys("!ZakaZ2022" + Keys.ENTER);
        Thread.sleep(5000);
        try{webDriver.findElement(By.id("main-nav-offers")).click();}
        catch (NoSuchElementException elementException){
            webDriver.navigate().refresh();
        }
        Thread.sleep(18000);
        /////////////////////////////////размер списка товар(откуда брать?)
        int productsListSize = 24;
        int prodCounter = 0;
        int prodCounterTotal = 0;

        /////////////////////////////////

        for (int i = 0; i < productsListSize; i++) {
            Thread.sleep(5000);
            Document doc = Jsoup.parse(webDriver.getPageSource());
            //////добавление первых 10 sku в exel файл
            Elements skus = doc.getElementsByAttributeValue("title", "Артикул в системе продавца");
            int skusListLenght = skus.size();
            ArrayList skuList = new ArrayList();
            for (Element element : skus) {
                skuList.add(element.text());
            }
            System.out.println(skuList);/////////////тест
            ////////////добавление в файл наших артикулов
            for (int j = 0; j < skusListLenght; j++) {
                midSheet.createRow(prodCounterTotal + 1 + j).createCell(0).setCellValue((String) skuList.get(j));
            }
            System.out.println(prodCounter);
            Elements products = doc.getElementsByClass("offer-managment__product-cell-link");
            for (Element prod : products) {


                webDriver.get(prod.attr("href"));
                Thread.sleep(10000);
                try{
                    Document documentTest = Jsoup.parse(webDriver.getPageSource());
                    Elements priceTest = documentTest.getElementsByClass("sellers-table__price-cell-text");
                    String priceTest2 = priceTest.first().text();
                    System.out.println(priceTest2);
                }catch (NullPointerException exception){
                    System.out.println("problems during loading page");
                    webDriver.navigate().refresh();
                    Thread.sleep(20000);
                    try{
                        Document documentTest = Jsoup.parse(webDriver.getPageSource());
                        Elements priceTest = documentTest.getElementsByClass("sellers-table__price-cell-text");
                        String priceTest2 = priceTest.first().text();
                        System.out.println(priceTest2);
                    }catch (NullPointerException e){
                        System.out.println("problems during loading page");
                        //webDriver.navigate().refresh();
                        Thread.sleep(20000);
                    }}
                /////закрытие окна выбора города////
//                WebElement btnClose = webDriver.findElement(By.xpath("//*[@id=\"dialogService\"]/div/div[1]/div[2]/i"));
//                btnClose.click(); Thread.sleep(1000);
                //////////////////////////////////
                ///создание названия и цены
                Document document2 = Jsoup.parse(webDriver.getPageSource());
                String name = document2.getElementsByClass("item__heading").text();
                midSheet.getRow(prodCounter + 1).createCell(1).setCellValue(name);
                Elements els = document2.getElementsByClass("sellers-table__price-cell-text");
                Element els2 = els.first();


                ////////////////первая цена////////////
                String priceLowest = els2.text().replaceAll("₸", "");
                int priceLowestNumber = Integer.parseInt(priceLowest.replace(" ",""));
                midSheet.getRow(prodCounter + 1).createCell(2).setCellValue(priceLowestNumber);
                ///////////имя продавца///////////
                Elements cellersKaspi = document2.getElementsByClass("sellers-table__cell");
                String firstSeller = cellersKaspi.first().text();

                try{
                    int index = firstSeller.indexOf("(");

                String firstSellerout = firstSeller.substring(0,index);
                firstSellerout = firstSellerout.replace(" ","");
                midSheet.getRow(prodCounter+1).createCell(3).setCellValue(firstSellerout);
//                if (!firstSellerout.equals("MusicPark")){
//                    midSheet.getRow(prodCounter+1).getCell(3).setCellStyle(style);
//                }
                }
                catch (StringIndexOutOfBoundsException exception){
                    firstSeller = firstSeller.replace(" ","");
                    midSheet.getRow(prodCounter+1).createCell(3).setCellValue(firstSeller);
//                    if (!firstSeller.equals("MusicPark")){
//                        midSheet.getRow(prodCounter+1).getCell(3).setCellStyle(style);}
                }

                ////////////////вторая цена////////////
                List<WebElement>prices = webDriver.findElements(By.className("sellers-table__price-cell-text"));
                String secondPrice;
                int secondPriceNumber;
                try{secondPrice = prices.get(2).getText();
                    secondPrice = secondPrice.replaceAll("₸","");
                    secondPriceNumber = Integer.parseInt(secondPrice.replace(" ",""));
                    midSheet.getRow(prodCounter+1).createCell(4).setCellValue(secondPriceNumber);

                }catch (IndexOutOfBoundsException e){
                    secondPriceNumber = 0;
                    midSheet.getRow(prodCounter+1).createCell(4).setCellValue(secondPriceNumber);
                }



                prodCounter++;
                System.out.println(prodCounter + " " + name + " " + priceLowestNumber + " " + firstSeller + " " +secondPriceNumber);
            }
            prodCounterTotal = prodCounterTotal + skusListLenght;
            System.out.println("тотал " + prodCounterTotal);



            try{ try {



                    webDriver.get("https://kaspi.kz/merchantcabinet/#/orders/tabs");
                    Thread.sleep(15000);
                    webDriver.findElement(By.id("main-nav-offers")).click();
                    Thread.sleep(5000);

                }catch (NoSuchElementException elementException){
                    //webDriver.get("https://kaspi.kz/merchantcabinet/#/orders/tabs");
                    webDriver.navigate().refresh();
                    Thread.sleep(10000);
                    webDriver.findElement(By.id("main-nav-offers")).click();
                    Thread.sleep(10000);
                }}catch (NoSuchElementException exception){
                webDriver.get("https://kaspi.kz/merchantcabinet/#/orders/tabs");
               // webDriver.navigate().refresh();
                Thread.sleep(10000);
                webDriver.findElement(By.id("main-nav-offers")).click();
                Thread.sleep(10000);

            }
//                finally {
//                    webDriver.get("https://kaspi.kz/merchantcabinet/#/orders/tabs");
//                    webDriver.findElement(By.id("main-nav-offers")).click();
//                    Thread.sleep(15000);
//                }







               ////////////переход по страницам товаров////////////
                Actions move = new Actions(webDriver);
                int p = 0;

                while (p != prodCounterTotal & p < prodCounterTotal) {
                    List<WebElement> btnNext = webDriver.findElements(By.className("gwt-Image"));

                    move.moveToElement(btnNext.get(3));
                    Thread.sleep(1000);
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    Thread.sleep(1000);
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    Thread.sleep(1000);
                    try{btnNext.get(3).click();}
                    catch (ElementNotInteractableException exception){
                        Thread.sleep(10000);
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                        Thread.sleep(5000);
                        btnNext.get(3).click();

                    }
                    Thread.sleep(8000);
                    p = p + 10;
                    System.out.println("p= " + p);
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



            }
            midWb.write(fos);
            webDriver.close();
            fos.close();

        }
    }
