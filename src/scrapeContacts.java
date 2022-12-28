import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class scrapeContacts {

    private static final ArrayList<String> searchStyle = new ArrayList<>();

    private static ArrayList<String> Contacts = new ArrayList<>();
    private static WebDriver driver;
    static List<WebElement> data;

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        //kill chrome
        Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
        System.out.print("Enter the number of elements: ");
        int number_of_elements = sc.nextInt();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\neera\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", true);
        chromeOptions.addArguments("user-data-dir=C:\\Users\\neera\\AppData\\Local\\Google\\Chrome\\User Data");
        chromeOptions.addArguments("--start-maximized");
        //check how to use proxy
        chromeOptions.addArguments("--proxy-server=80.48.119.28:8080");
        driver = new ChromeDriver(chromeOptions);

        driver.get("https://web.whatsapp.com");

        //wait for 20 sec for the website to load
        Thread.sleep(20000);

        //getting the list
        data = driver.findElements(By.className("rx9719la"));
        System.out.println(data.size());

        for (int i = 0; i < data.size(); i++) {
            searchStyle.add(data.get(i).getAttribute("style"));
            if ((data.get(i).getAttribute("style").contains("1080px"))) {
                System.out.println("Position of the first element is " + i);
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).findElement(By.className("zoWT4")).getText().contains("+91")) {
//                    System.out.println(data.get(j).findElement(By.className("zoWT4")).getText());
                        if (!Contacts.contains(data.get(j).findElement(By.className("zoWT4")).getText())) {
                            Contacts.add(data.get(j).findElement(By.className("zoWT4")).getText());

                        }

                    }
                }

                ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')[" + i + "].scrollIntoView();");
            }

        }


        System.out.println("Starting loop after 3 seconds...");
        Thread.sleep(3000);

        //calling the function
        try{
            scrollDown(number_of_elements);
        }catch(Exception e)
        {

        }

        //printing all the contacts
        for (int i = 0; i < Contacts.size(); i++) {
            System.out.println("A" + (i + 1) + " " + Contacts.get(i));
        }
        System.out.println("Total contacts scrapped: " + Contacts.size());

        System.out.print("Do you wish to quit the browser (y/n): ");

        //issue the user_input was skipped by java
        //What is happening is that the call to nextLine() first finishes the line where the user enters the number of students. Why? Because nextInt() reads only one int and does not finish the line.
        //So adding an extra readLine() statement would solve this problem.
        sc.nextLine();
        String user_input = sc.nextLine();
        if (user_input.equals("y")) {
            driver.quit();
        }

        System.out.println("Creating CSV file please wait...");
        createCSV(Contacts);
        createVCF(Contacts);

    }

    private static void scrollDown(int number_of_elements) {

        int i;
//        System.out.println("Data size: "+data.size());

        //starting the loop after 1440px of transition
        for (i = 1080; i < number_of_elements; i = i + 720) {
            System.out.println("i: " + i);

            //scrolling into view
            for (int j = 0; j < data.size(); j++) {
                if ((data.get(j).getAttribute("style").contains(String.valueOf(i)))) {
                    ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')[" + j + "].scrollIntoView();");
                }
            }
            data = driver.findElements(By.className("rx9719la"));


            //adding the contacts
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j).findElement(By.className("zoWT4")).getText().contains("+91")) {
//                    System.out.println(data.get(j).findElement(By.className("zoWT4")).getText());
                    if (!Contacts.contains(data.get(j).findElement(By.className("zoWT4")).getText())) {
                        Contacts.add(data.get(j).findElement(By.className("zoWT4")).getText());

                    }

                }
            }

        }

        int k = number_of_elements - i;

        i = i + k;
        System.out.println("Value of i is: " + i);

        data = driver.findElements(By.className("rx9719la"));

        //scrolling into view
        for (int j = 0; j < data.size(); j++) {
            if ((data.get(j).getAttribute("style").contains(String.valueOf(i)))) {
                System.out.println("Position of the element is " + j);
                ((JavascriptExecutor) driver).executeScript("document.querySelectorAll('.rx9719la')[" + j + "].scrollIntoView();");
            }
        }

        //adding the contacts
        for (int j = 0; j < data.size(); j++) {
            if (data.get(j).findElement(By.className("zoWT4")).getText().contains("+91")) {
//                System.out.println(data.get(j).findElement(By.className("zoWT4")).getText());
//                System.out.println();
//                System.out.println("Total numbers extracted: " + count);
                if (!Contacts.contains(data.get(j).findElement(By.className("zoWT4")).getText())) {
                    Contacts.add(data.get(j).findElement(By.className("zoWT4")).getText());

                }
            }
        }

    }

    private static void createCSV(ArrayList<String> Contacts) throws IOException {
        File file = new File("contacts.csv");
        FileWriter outputFile = new FileWriter(file);

        //using openCSV to create the csv file
        CSVWriter writer = new CSVWriter(outputFile);
        String[] headers = {"Name", "Numbers"};
        writer.writeNext(headers);

        for (int i = 0; i < Contacts.size(); i++) {
            String[] data = {"A " + (i + 1), Contacts.get(i)};
            writer.writeNext(data);
        }

        writer.close();
    }

    private static void createVCF(ArrayList<String> Contacts) throws IOException
    {
        File file = new File("contacts.vcf");

        FileOutputStream fop=new FileOutputStream(file);

        if(file.exists()) {

            int count=0;
            for (String contact:Contacts ) {
                count++;
                String str = "BEGIN:VCARD\n" +
                        "VERSION:4.0\n" +
                        "N:Customer;"+count+";;;\n" +
                        "FN:Customer "+count+"\n" +
                        "ORG:Some Co.\n" +
                        "TITLE:Customer "+count+"\n" +
                        "TEL;TYPE=work,voice;VALUE=uri:tel:"+contact+"\n" +
                        "TEL;TYPE=home,voice;VALUE=uri:tel:"+contact+"\n" +
                        "EMAIL:customer@someemail.com\n" +
                        "REV:20080424T195243Z\n" +
                        "END:VCARD";

                fop.write(str.getBytes());
            }

            System.out.println("Done creating vcf file!!");



            //toRead

//            BufferedReader br = null;
//            String sCurrentLine;
//            br = new BufferedReader(new FileReader("contact.vcf"));
//            while ((sCurrentLine = br.readLine()) != null)
//            {
//                System.out.println(sCurrentLine);
//            }
//            //close the output stream and buffer reader
//            fop.flush();
//            fop.close();
//            System.out.println("The data has been written");
//        } else
//            System.out.println("This file does not exist");
//    }



        }




    }
}
