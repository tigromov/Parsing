import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://kaspi.kz/shop/p/luther-lcg-0-chernyi-100388079/?c=750000000");
        Document document = Jsoup.parse(webDriver.getPageSource());

        Elements els = document.getElementsByClass("sellers-table__price-cell-text");
        Element els2 = els.first();
        System.out.println(els2.text());
        webDriver.close();
        //Document doc = Jsoup.connect("https://kaspi.kz/shop/p/akg-k-52-chernyi-4802475/?c=750000000").get();


      //  System.out.println(doc.getElementsByTag("div"));

       // System.out.println(doc.getElementsByTag("body"));


    }
}
