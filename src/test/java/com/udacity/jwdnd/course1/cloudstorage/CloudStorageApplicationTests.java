package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private WebDriverWait driverWait;

	private JavascriptExecutor js;

	private String baseURL;

	private String username;
	private final String password="ggranados";
	private final String firstName = "German";
	private final String lastName = "Granados";

	private String loginURL;
	private String signupURL;
	private String homeURL;


	@BeforeAll
	static void beforeAll() {
		//WebDriverManager.chromedriver().setup();
		WebDriverManager.edgedriver().config().setEdgeDriverVersion("89.0.774.68");
		WebDriverManager.edgedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		//this.driver = new ChromeDriver();
		this.driver = new EdgeDriver();
		this.driverWait = new WebDriverWait(driver, 3);
		this.js = (JavascriptExecutor) driver;
		baseURL = "http://localhost:" + port;
		loginURL = baseURL + "/login";
		signupURL = baseURL + "/signup";
		homeURL = baseURL + "/home";
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	public void signupUserAndLogin(){
		Random random = new Random();
		username = "ggranados" + String.valueOf(random.nextInt(1000));
		driver.get(signupURL);
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, username, password);
		driver.get(loginURL);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@Test
	public void getLoginPage() {
		driver.get(loginURL);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/*Write a test that verifies that an unauthorized user
	can only access the login and signup pages.*/
	@Test
	public void testUnauthenticatedUserAccessHomePage(){
		driver.get(homeURL);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUnauthenticatedUserAccessLogin(){
		driver.get(loginURL);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUnauthenticatedUserAccessSignUp(){
		driver.get(signupURL);
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	/*Write a test that signs up a new user, logs in, verifies
	that the home page is accessible, logs out, and verifies
	that the home page is no longer accessible */
	@Test
	public void testUserSignupLoginAndLogout(){
		signupUserAndLogin();
		assertEquals("Home", driver.getTitle());
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		assertEquals("Login", driver.getTitle());
		driver.get(homeURL);
		assertEquals("Login", driver.getTitle());
	}

	/*Write a test that creates a note, and verifies it is displayed.
	* Write a test that edits an existing note and verifies that the changes are displayed
	* Write a test that deletes a note and verifies that the note is no longer displayed
	* */
	@Test
	public void testCreateNote(){
		signupUserAndLogin();
		NotePage notePage = new NotePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getNoteTabButton()));
		js.executeScript("arguments[0].click();", notePage.getNoteTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getAddNewNoteButton()));
		js.executeScript("arguments[0].click();", notePage.getAddNewNoteButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getCreateNoteButton()));
		notePage.saveNote("title", "description");
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getNoteCreatedSuccessMsg()));
		assertEquals("Note created successfully", notePage.getNoteCreatedSuccessMsg().getText());
	}

	@Test
	public void testEditNote(){
		signupUserAndLogin();
		NotePage notePage = new NotePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getNoteTabButton()));
		js.executeScript("arguments[0].click();", notePage.getNoteTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getAddNewNoteButton()));
		js.executeScript("arguments[0].click();", notePage.getAddNewNoteButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getCreateNoteButton()));
		notePage.saveNote("title", "description");
		this.driverWait.until(ExpectedConditions.visibilityOf(notePage.getNoteCreatedSuccessMsg()));
		assertEquals("Note created successfully", notePage.getNoteCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getEditNoteButton()));
		js.executeScript("arguments[0].click();", notePage.getEditNoteButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getCreateNoteButton()));
		notePage.clearNoteFormFields();
		notePage.saveNote("newTitle", "description");
		this.driverWait.until(ExpectedConditions.visibilityOf(notePage.getNoteCreatedSuccessMsg()));
		assertEquals("Note updated successfully", notePage.getNoteCreatedSuccessMsg().getText());
	}

	@Test
	public void testDeleteNote(){
		signupUserAndLogin();
		NotePage notePage = new NotePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getNoteTabButton()));
		js.executeScript("arguments[0].click();", notePage.getNoteTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getAddNewNoteButton()));
		js.executeScript("arguments[0].click();", notePage.getAddNewNoteButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getCreateNoteButton()));
		notePage.saveNote("title", "description");
		this.driverWait.until(ExpectedConditions.visibilityOf(notePage.getNoteCreatedSuccessMsg()));
		assertEquals("Note created successfully", notePage.getNoteCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(notePage.getDeleteNoteButton()));
		js.executeScript("arguments[0].click();", notePage.getDeleteNoteButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(notePage.getNoteDeletedSuccessMsg()));
		assertEquals("Note deleted successfully", notePage.getNoteDeletedSuccessMsg().getText());
	}

	/*Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	* Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
	* Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	* */
	@Test
	public void testCreateCredential(){
		signupUserAndLogin();
		CredentialPage credentialPage = new CredentialPage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCredentialTabButton()));
		js.executeScript("arguments[0].click();", credentialPage.getCredentialTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getAddNewCredentialButton()));
		js.executeScript("arguments[0].click();", credentialPage.getAddNewCredentialButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCreateCredentialButton()));
		credentialPage.saveCredential("www.google.com", "ggranados", "ggranados");
		this.driverWait.until(ExpectedConditions.visibilityOf(credentialPage.getCredentialCreatedSuccessMsg()));
		assertEquals("Credential created successfully", credentialPage.getCredentialCreatedSuccessMsg().getText());
	}

	@Test
	public void testEditCredential(){
		signupUserAndLogin();
		CredentialPage credentialPage = new CredentialPage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCredentialTabButton()));
		js.executeScript("arguments[0].click();", credentialPage.getCredentialTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getAddNewCredentialButton()));
		js.executeScript("arguments[0].click();", credentialPage.getAddNewCredentialButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCreateCredentialButton()));
		credentialPage.saveCredential("www.google.com", "ggranados", "ggranados");
		this.driverWait.until(ExpectedConditions.visibilityOf(credentialPage.getCredentialCreatedSuccessMsg()));
		assertEquals("Credential created successfully", credentialPage.getCredentialCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getEditCredentialButton()));
		js.executeScript("arguments[0].click();", credentialPage.getEditCredentialButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCreateCredentialButton()));
		credentialPage.clearCredentialFormFields();
		credentialPage.saveCredential("www.facebook.com", "ggranados", "ggranados");
		this.driverWait.until(ExpectedConditions.visibilityOf(credentialPage.getCredentialCreatedSuccessMsg()));
		assertEquals("Credential updated successfully", credentialPage.getCredentialCreatedSuccessMsg().getText());
	}

	@Test
	public void testDeleteCredential(){
		signupUserAndLogin();
		CredentialPage credentialPage = new CredentialPage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCredentialTabButton()));
		js.executeScript("arguments[0].click();", credentialPage.getCredentialTabButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getAddNewCredentialButton()));
		js.executeScript("arguments[0].click();", credentialPage.getAddNewCredentialButton());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getCreateCredentialButton()));
		credentialPage.saveCredential("www.google.com", "ggranados", "ggranados");
		this.driverWait.until(ExpectedConditions.visibilityOf(credentialPage.getCredentialCreatedSuccessMsg()));
		assertEquals("Credential created successfully", credentialPage.getCredentialCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(credentialPage.getDeleteCredentialButton()));
		js.executeScript("arguments[0].click();", credentialPage.getDeleteCredentialButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(credentialPage.getCredentialDeletedSuccessMsg()));
		assertEquals("Credential deleted successfully", credentialPage.getCredentialDeletedSuccessMsg().getText());
	}

	@Test
	public void testCreateFile(){
		signupUserAndLogin();
		FilePage filePage = new FilePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getChooseFilebutton()));
		//filePage.getChooseFilebutton().sendKeys("C:\\Users\\gg7pe\\Desktop\\sandro.jpg");
		filePage.getChooseFilebutton().sendKeys(getClass().getClassLoader().getResource("static/img/sandro.jpg").getPath().substring(1));
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getSubmitFileButton()));
		js.executeScript("arguments[0].click();", filePage.getSubmitFileButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(filePage.getFileCreatedSuccessMsg()));
		assertEquals("File Uploaded successfully", filePage.getFileCreatedSuccessMsg().getText());
	}

	@Test
	public void testDeleteFile(){
		signupUserAndLogin();
		FilePage filePage = new FilePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getChooseFilebutton()));
		filePage.getChooseFilebutton().sendKeys(getClass().getClassLoader().getResource("static/img/sandro.jpg").getPath().substring(1));
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getSubmitFileButton()));
		js.executeScript("arguments[0].click();", filePage.getSubmitFileButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(filePage.getFileCreatedSuccessMsg()));
		assertEquals("File Uploaded successfully", filePage.getFileCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getDeleteFileButton()));
		js.executeScript("arguments[0].click();", filePage.getDeleteFileButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(filePage.getFileDeletedSuccessMsg()));
		assertEquals("File deleted successfully", filePage.getFileDeletedSuccessMsg().getText());
	}

	@Test
	public void testCreateFileAlreadyExists(){
		signupUserAndLogin();
		FilePage filePage = new FilePage(driver);
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getChooseFilebutton()));
		filePage.getChooseFilebutton().sendKeys(getClass().getClassLoader().getResource("static/img/sandro.jpg").getPath().substring(1));
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getSubmitFileButton()));
		js.executeScript("arguments[0].click();", filePage.getSubmitFileButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(filePage.getFileCreatedSuccessMsg()));
		assertEquals("File Uploaded successfully", filePage.getFileCreatedSuccessMsg().getText());
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getChooseFilebutton()));
		filePage.getChooseFilebutton().sendKeys(getClass().getClassLoader().getResource("static/img/sandro.jpg").getPath().substring(1));
		this.driverWait.until(ExpectedConditions.elementToBeClickable(filePage.getSubmitFileButton()));
		js.executeScript("arguments[0].click();", filePage.getSubmitFileButton());
		this.driverWait.until(ExpectedConditions.visibilityOf(filePage.getFileErrorMsg()));
		assertEquals("File name is already used", filePage.getFileErrorMsg().getText());

	}
}
