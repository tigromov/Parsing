import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, NullPointerException {
        int time = 1000;
        int counter = 0;
        int li = 2;

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://kaspi.kz/shop/search/?text=music%20park&q=:allMerchants:Musicpark");


       ///close Choose your City
        WebElement btnClose = webDriver.findElement(By.xpath("//*[@id=\"dialogService\"]/div/div[1]/div[2]/i"));
        btnClose.click(); Thread.sleep(time);
        ///



        for (int i = 1; i < 30; i++) {
           webDriver.get("https://kaspi.kz/shop/search/?text=music%20park&q=:allMerchants:Musicpark&page=" + i +"");
           Thread.sleep(time + 3000);
          //  if (counter>3)li = 7;
            //WebElement btn = webDriver.findElement(By.xpath("//*[@id=\"page\"]/div[3]/div/div/div[2]/div[2]/div[2]/li[" + li + "]"));
           // btn.click();
           // Thread.sleep(time+3000);

            Document documentGlobal = Jsoup.parse(webDriver.getPageSource());
            Elements wrappers = documentGlobal.getElementsByClass("item-card__image-wrapper");


            for (Element wr : wrappers) {

                counter++;
                webDriver.get(wr.attr("href"));
                Thread.sleep(time);
                Document document2 = Jsoup.parse(webDriver.getPageSource());
                Elements els = document2.getElementsByClass("sellers-table__price-cell-text");
                Element els2 = els.first();
                String name = document2.getElementsByClass("item__heading").text();
                String sku = document2.getElementsByClass("item__sku").text().substring(13);

                try{

                System.out.println(counter + " " + name + " " + els2.text() + " артикул каспи: " + sku);
                Thread.sleep(time);} catch (NullPointerException e) {
                    System.out.println(counter + " " + name + " Нет актуальных предложений по данному товару");
                }

            }


           // webDriver.getPageSource();

        }

        webDriver.close();

      //  Elements els = document.getElementsByClass("sellers-table__price-cell-text");
//        Element els2 = els.first();
//        System.out.println(els2.text());
        webDriver.close();





    }
}
