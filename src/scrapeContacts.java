import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class scrapeContacts {

    private static final ArrayList<String> searchStyle = new ArrayList<>();

    private static ArrayList<String>Contacts = new ArrayList<>();
    private static WebDriver driver;
    static List<WebElement> data;
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\neera\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", true);
        chromeOptions.addArguments("user-data-dir=C:\\Users\\neera\\AppData\\Local\\Google\\Chrome\\User Data");
        chromeOptions.addArguments("--start-maximized");
        //check how to use proxy
        chromeOptions.addArguments("--proxy-server=80.48.119.28:8080");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://web.whatsapp.com");
        Thread.sleep(20000);

        //getting the list
         data = driver.findElements(By.className("rx9719la"));
        System.out.println(data.size());

        for(int i =0;i<data.size();i++){
            searchStyle.add(data.get(i).getAttribute("style"));
            if((data.get(i).getAttribute("style").contains("1440px"))){
                System.out.println("Position of the element is "+i);
                ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')["+i+"].scrollIntoView();");
            }

            }



        System.out.println("Starting loop");
        Thread.sleep(3000);
        int j=0;
        while(j<1){
            scrollDown();
            j++;
        }

        for(int i=0;i< Contacts.size();i++){
            System.out.println("Customer"+i+1+" "+Contacts.get(i));
        }

    }

    private static int scrollDown(){
        int count=0;

        int number = 1440;

        int i;
        System.out.println("Data size: "+data.size());
        for ( i = 1440; i <10152 ; i=i+720) {
            System.out.println("i: "+i);

            //scrolling into view
            for (int j = 0; j < data.size(); j++) {
                if ((data.get(j).getAttribute("style").contains(String.valueOf(i)))) {
                    ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')[" + j + "].scrollIntoView();");
                }
            }
            data = driver.findElements(By.className("rx9719la"));


            //displaying the contacts
            for (int j = 0; j < data.size(); j++) {
                if(data.get(j).findElement(By.className("zoWT4")).getText().contains("+91")){
                    System.out.println(data.get(j).findElement(By.className("zoWT4")).getText());
                }
            }


            }

        int k = 10152-i;

        i=i+k;
        System.out.println("Value of i is: "+i);

        data = driver.findElements(By.className("rx9719la"));

        //scrolling into view
        for (int j = 0; j < data.size(); j++) {
            if ((data.get(j).getAttribute("style").contains(String.valueOf(i)))) {
                System.out.println("Position of the element is " + j);
                ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')[" + j + "].scrollIntoView();");
            }
        }

        //displaying the contacts
        for (int j = 0; j < data.size(); j++) {
            if(data.get(j).findElement(By.className("zoWT4")).getText().contains("+91")){
                count++;
                System.out.println(data.get(j).findElement(By.className("zoWT4")).getText());
                Contacts.add(data.get(j).findElement(By.className("zoWT4")).getText());
                System.out.println();
                System.out.println("Total numbers extracted: "+count);
            }
        }

        return i;
    }
}
