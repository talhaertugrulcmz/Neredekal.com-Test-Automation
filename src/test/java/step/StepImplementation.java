package step;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import method.Methods;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class StepImplementation extends Driver {

    Methods methods;

    public StepImplementation() {
        methods = new Methods();

    }


    // Bekleme Süresi

    @Step("<int> saniye kadar bekle")
    public void waitBySeconds(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Neredekal.com URL Kontrolü

    @Step("Sayfa URL Kontrolü")
    public void UrlKontrol() {

        String ExpectedUrl = "https://www.neredekal.com/";
        String ActualUrl = driver.getCurrentUrl();


        Assertions.assertEquals(ExpectedUrl,ActualUrl);

        logger.info("Dogru URL Adresi ----> " + ActualUrl);

    }



    // Neredekal.com Üye Girişi Yapılması

    @Step("<css> li Uye Giris butonunun üzerinde bekleme <xpath> li Giris Yap butonuna tiklanir")
    public void UyeGirisi(String css, String xpath) throws InterruptedException {

        methods.hoverElement(By.cssSelector(css));
        methods.findElement(By.xpath(xpath)).click();

    }



    // Kullanici Bilgileri Girişi

    @Step("<css1> li kullanici adi ve <text1> gir <css2> li sifre giris <text2> gir <css> li butona tikla")
    public void KullaniciLogins(String css1, String text1, String css2, String text2, String css3) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        methods.findElement(By.cssSelector(css1)).sendKeys(text1);
        logger.info("Kullanici mail adresi girildi : " + text1);
        Thread.sleep(1000);

        methods.findElement(By.cssSelector(css2)).sendKeys(text2);
        logger.info("Kullanici sifresi girildi : " + text2);
        Thread.sleep(1000);


        if (text1.equals("ertugrulcmz@gmail.com") && text2.equals("NeredeTest1!")) {
            logger.info("Kullanici Adi ve Sifresi Dogru");
        } else if (text1 == ("ertugrulcmz@gmail.com") && text2 != ("NeredeTest1!")) {
            logger.info("Kullanici Sifresi Yanlis");
        } else if (text1 != ("ertugrulcmz@gmail.com") && text2 == ("NeredeTest1!")) {
            logger.info("Kullanici Adi Yanlis");
        } else {
            logger.info("Giris Basarisiz Lütfen Tekrar Deneyiniz....");
        }


        methods.findElement(By.cssSelector(css3)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='css-19p82hs']")));
        System.out.println("Kullanici Sisteme Giris Yapti ...");


    }



    // Kullanici Login Kontrolu

    @Step("Kullanici Login Kontrolu")
    public void LoginKontrol() {

        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement LoginKontrol = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div/div[2]")));
        String KullaniciKontrol = LoginKontrol.getText();
        System.out.println(" Kullanici Bilgileri Kontrol Ediliyor......" + " \n" + KullaniciKontrol);


        if (KullaniciKontrol.contains("Talha Ertuğrul Ç.")) {

            logger.info("Kullanici Hesabi Goruntulendi... Dogru Kullanici Basarili Bir Sekilde Giris Yapmistir...");

        } else {

            System.out.println("Kullanici Girisi Basarisiz");
        }

    }



    // Otel Ismi Yazma

    @Step("<css> li alana <text> otel ismi gir ve <xpath1> tikla")
    public void OtelBilgi(String css, String text, String xpath) throws InterruptedException {


        methods.findElement(By.cssSelector(css)).sendKeys(text);
        Thread.sleep(1000);
        methods.findElement(By.xpath(xpath)).click();

    }



    // Tarih Seçimi ve Arama Yapma

    @Step("<xpath> li tarih tablosuna tikla ve secim yap ardindan <css> ara butonuna tikla")
    public void TarihSecimi(String xpath , String css) throws InterruptedException {

        WebElement dateField = driver.findElement(By.xpath(xpath));
        dateField.click();
        logger.info("Tarih Tablosuna Tiklandi...");
        Thread.sleep(1000);

        LocalDate today = LocalDate.now();
        String currentDay = String.valueOf(LocalDate.now().getDayOfMonth());
        LocalDate tomorrow = today.plusDays(1);
        String tomorrowDay = String.valueOf(today.plusDays(1).getDayOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM EEEE", new Locale("tr"));
        String todayStr = today.format(formatter);
        String tomorrowStr = tomorrow.format(formatter);

        WebElement bugunElement = driver.findElement(By.xpath("//div[@class='css-1oykjil']"));
        List<WebElement> columns = bugunElement.findElements(By.tagName("div"));
        System.out.println("Giris Tarihi Bugun ---->  " + todayStr);
        System.out.println("Cikis Tarihi Yarin ---->  " + tomorrowStr);

        for (WebElement cell : columns) {
            if (cell.getText().equals(currentDay)) {
                cell.click();
                Thread.sleep(500);
                break;
            }
        }

        for (WebElement cell : columns) {
            if (cell.getText().equals(tomorrowDay)) {
                cell.click();
                Thread.sleep(2000);
                break;
            }
        }

        logger.info("Giris ve Cıkıs Tarihleri Basarili Bir Sekilde Secildi...");

        methods.findElement(By.cssSelector(css)).click();
        logger.info("Istelenilen Seceneklere Gore Arama Yapildi...");

    }




    // Otel Fiyat Filtreleme ve Kontrolü

    @Step("<xpath1> li butona tikla <xpath2> filtre tikla")
    public void filtreleme(String xpath1,String xpath2) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='css-gjrx0e']")));
        WebElement filtre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='css-1somsfw']")));

        String FiyatFiltre = filtre.getText();;
        System.out.println(FiyatFiltre);


        if (filtre.isDisplayed()){
            logger.info("Filtreleme Ikonu Sayfada Goruntulendi...");
            filtre.click();
        }else {
            logger.info("Filtreleme Ikonu Sayfada Goruntulenemedi !!! ");
        }

        methods.hoverElement(By.xpath(xpath2));
        methods.findElement(By.xpath(xpath2)).click();
        logger.info("Fiyat: Önce En Düşük Secenegi Secildi...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"centerArea\"]/div[1]/div[4]/div[2]/div[4]/div[1]")));
    }


    // Sayfa Kaydırma
    @Step("Sayfa Scroll")
    public void SayfaScrl() {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scroll(0,1200)");
    }



    // Otel Fiyatlarının Düşükten Büyüğe Doğru Sıralandığının Kontrolü

    @Step("Otel Fiyatlarinin Düsükten Büyüge Kontrolu")
    public void OtelFiyat(){

        List<WebElement> OtelFiyatlari = driver.findElements(By.cssSelector("div[class=\"css-1epzwqf\"]"));
        List<String> TümFiyat = new ArrayList<>();


        for (WebElement element : OtelFiyatlari) {

            TümFiyat.add(String.valueOf(element.getText()));
        }

        System.out.println("Suanda " + TümFiyat.size() + " Tane Otel Fiyati Listelenmektedir...");
        System.out.println("Kucukten Buyuge Otellerin Fiyat Listesi ----> " + TümFiyat);

        String EnKücük = Collections.min(TümFiyat);
        String EnBüyük = Collections.max(TümFiyat);
        Integer SonEleman = (TümFiyat.size()-1);


        Collections.sort(TümFiyat);
        System.out.println("En Ucuz Otel Fiyati ----> " + EnKücük + "\n" + " En Pahali Otel Fiyati ----> " + EnBüyük);

        if (TümFiyat.get(0).equals(EnKücük) && TümFiyat.get(SonEleman).equals(EnBüyük)){
            logger.info(" \n " +
                    " <---- Tebrikler  Otel Fiyatlari Kucukten Buyuge Dogru Bir Sekilde Siralanmistir ---->  \n"
                    + "---------------------------------------------------------------------------------------");

        }else {

            logger.info("Maalesef... Otel Fiyatlari Kücükten Büyüge Siralanamadi...");
        }
    }

}
