package org.example;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import com.github.javafaker.Faker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
public class Main {
    WebDriver driver;
    WebDriverWait wait;
// To be able to Generate names, numbers , etc..
    Faker faker = new Faker();
String username = "admin";
String password ="Admin123";
String firstName = faker.name().firstName();
String familyName = faker.name().lastName();
// open the website
    @BeforeTest
    public void SetUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\pc\\Downloads\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().window().maximize();
        driver.get("https://o2.openmrs.org/openmrs/login.htm");
    }
// this test to login to the website
    @Test
    public void loginIn() throws InterruptedException {
        Thread.sleep(1000);

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(500);
        driver.findElement(By.id("Registration Desk")).click();
        driver.findElement(By.xpath("//*[@id=\"loginButton\"]")).click();

        System.out.println("Successfully logged in!");
    }
// This test is used to add patient to the list and insert data of patients by using a generated names, gender and numbers
    @Test(dependsOnMethods = "loginIn")
    public void addPatient() throws InterruptedException {

        WebElement registerPatient = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension")));
        registerPatient.click();
        Thread.sleep(2000);
        System.out.println("Start to create a patient");

        driver.findElement(By.name("givenName")).sendKeys(firstName);
        driver.findElement(By.name("familyName")).sendKeys(familyName);
        driver.findElement(By.id("next-button")).click();
        Thread.sleep(2000);
        System.out.println("patient Name Done");
        // Generate a random gender (Male or Female)
        String[] genders = {"Male", "Female"};
        String randomGender = genders[new Random().nextInt(genders.length)];
        System.out.println("Generated Gender: " + randomGender);
        // Select Gender
        Select selectGender = new Select(driver.findElement(By.name("gender")));
        selectGender.selectByVisibleText(randomGender);
       // driver.findElement(By.xpath("//*[@id=\"gender-field\"]/option[1]")).click();
        driver.findElement(By.id("next-button")).click();
        Thread.sleep(2000);
        System.out.println("Gender Done");
        Date fakeDate = faker.date().birthday(1, 80);
       LocalDate birthdate = fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("Generated Birthdate: " + birthdate);
        driver.findElement(By.name("birthdateDay")).sendKeys(String.valueOf(birthdate.getDayOfMonth()));
        driver.findElement(By.name("birthdateYear")).sendKeys(String.valueOf(birthdate.getYear()));
       WebElement month = driver.findElement(By.id("birthdateMonth-field"));
        Select selectMonth = new Select(month);
        selectMonth.selectByVisibleText(birthdate.getMonth().name().substring(0, 1) + birthdate.getMonth().name().substring(1).toLowerCase());
        //Thread.sleep(10000); birthdate.getMonth().toString()
        driver.findElement(By.id("next-button")).click();

        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"cityVillage\"]")).sendKeys(faker.address().cityName());
        //WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cityVillage")));
        //addressField.sendKeys("Cairo");
        System.out.println("City Name : " + faker.address().cityName());
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.name("phoneNumber")).sendKeys(faker.phoneNumber().cellPhone());
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("submit")).click();

        Thread.sleep(10000);


    }
// Add request appointment for a department in the hospital
@Test(dependsOnMethods = "loginIn")
 public void requestAppointment() throws InterruptedException {
           wait = new WebDriverWait(driver, Duration.ofSeconds(10));
           Thread.sleep(5000);
           driver.findElement(By.xpath("//*[@id=\"appointmentschedulingui.requestAppointmentDashboardLink\"]/div/div[2]")).click();
           Thread.sleep(3000);
           System.out.println("Start process");


           driver.findElement(By.id("min-time-frame-value")).sendKeys("1");
    // SELECT TIME FRAME
           Select selectTimeMin = new Select(driver.findElement(By.id("min-time-frame-units")));
           selectTimeMin.selectByVisibleText("Day(s)");
           driver.findElement(By.id("max-time-frame-value")).sendKeys("5");
    // SELECT TIME FRAME
           Select selectTimeMax = new Select(driver.findElement(By.id("max-time-frame-units")));
           selectTimeMax.selectByVisibleText("Day(s)");
           driver.findElement(By.id("notes")).sendKeys("I feel not good since weekend");
    driver.findElement(By.id("appointment-type")).sendKeys("General Medicine");

    Thread.sleep(4000);
    //driver.findElement(By.id("typeahead-3-8989-option-0")).click();
   // Thread.sleep(3000);
    driver.findElement(By.id("save-button")).click();
           System.out.println("REQUEST DONE");
           Thread.sleep(4000);
        }

// start a visit by searching for a patient and start visit and gain some informations from him and attach a file for him and at last end the visit


    @Test(dependsOnMethods = "loginIn")
    public void visit() throws InterruptedException {
        Thread.sleep(2000);

/*
        WebElement click = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension")));

        click.click();
        Thread.sleep(2000);
// search for a patient to start a visit
        driver.findElement(By.id("patient-search")).sendKeys(firstName + familyName);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"patient-search-results-table\"]/tbody/tr[1]/td[2]")).click();
        Thread.sleep(3000);


 */
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.openmrs.module.coreapps.createVisit"))).click();
        Thread.sleep(2000);

wait.until(ExpectedConditions.elementToBeClickable(By.id("start-visit-with-visittype-confirm"))).click();
        Thread.sleep(20000);
        System.out.println("Start To get informations about patient");
// get some informations from the patient
     wait.until(ExpectedConditions.elementToBeClickable(By.id("referenceapplication.realTime.vitals"))).click();
     Thread.sleep(2000);
     driver.findElement(By.name("w8")).sendKeys("1" +faker.number().digits(2));
     driver.findElement(By.id("next-button")).click();
     System.out.println("Get height");
        Thread.sleep(2000);

        driver.findElement(By.name("w10")).sendKeys(faker.number().digits(2));
        driver.findElement(By.id("next-button")).click();
        String BMI = driver.findElement(By.id("calculated-bmi")).getText();
        System.out.println("Patient's BMI is: " + BMI );
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
        driver.findElement(By.id("next-button")).click();
driver.findElement(By.xpath("//*[@id=\"confirmationQuestion\"]/p[1]/button")).click();
        System.out.println("Data Collected " );

        Thread.sleep(5000);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("referenceapplication.realTime.simpleAdmission"))).click();
        Thread.sleep(4000);


        Select Inpatient = new Select(driver.findElement(By.id("w5")));
        Inpatient.selectByVisibleText("Inpatient Ward");

driver.findElement(By.xpath("//*[@id=\"htmlform\"]/htmlform/input")).click();
        System.out.println("Determine Status " );


        Thread.sleep(4000);

    wait.until(ExpectedConditions.elementToBeClickable(By.id("attachments.attachments.visitActions.default"))).click();
        Thread.sleep(4000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"att-page-main\"]/div[1]/att-file-upload/div[3]/div/div[2]/textarea"))).sendKeys("Patient_Report");

        Thread.sleep(5000);

        // Define the path to the file you want to upload
        String filePath = "C:\\Users\\pc\\Downloads\\cover.pdf"; // Replace with your file path
        File file = new File(filePath);
        System.out.println("BEGAN");
        Thread.sleep(5000);

        // Send the file path to the file input element
        driver.findElement(By.id("visit-documents-dropzone")).sendKeys(file.getAbsolutePath());
        System.out.println("Upload File Done");
        Thread.sleep(3000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"att-page-main\"]/div[1]/att-file-upload/div[3]/div/div[2]/span/button[1]"))).click();
        // Locate and click the upload button
        Thread.sleep(3000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"breadcrumbs\"]/li[2]/a"))).click();
        Thread.sleep(3000);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("referenceapplication.realTime.endVisit"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"end-visit-dialog\"]/div[2]/button[1]"))).click();

        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"visit-details\"]/div[2]/a[1]"))).click();
        System.out.println("End the Visit " );
        Thread.sleep(10000);
   }

    @AfterTest
    public void ending(){
        driver.quit();
    }
}


