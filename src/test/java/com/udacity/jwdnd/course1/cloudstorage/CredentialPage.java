package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    @Getter
    @FindBy(id="nav-credentials-tab")
    private WebElement credentialTabButton;

    @Getter
    @FindBy(id="add-new-credential")
    private WebElement addNewCredentialButton;

    @Getter
    @FindBy(xpath = "//*[@id='credentialTable']/tbody/tr/td[1]/button")
    private WebElement editCredentialButton;

    @Getter
    @FindBy(xpath = "//*[@id='credentialTable']/tbody/tr/td[1]/a")
    private WebElement deleteCredentialButton;

    @FindBy(id="credential-url")
    private WebElement urlField;

    @FindBy(id="credential-username")
    private WebElement userNameField;

    @FindBy(id="credential-password")
    private WebElement passwordField;

    @Getter
    @FindBy(id="save-new-credential")
    private WebElement createCredentialButton;

    @Getter
    @FindBy(id="credential-success-msg")
    private WebElement credentialCreatedSuccessMsg;

    @Getter
    @FindBy(id="credential-delete-msg")
    private WebElement credentialDeletedSuccessMsg;

    public CredentialPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void saveCredential(String url, String userName, String password) {
        this.urlField.sendKeys(url);
        this.userNameField.sendKeys(userName);
        this.passwordField.sendKeys(password);
        this.createCredentialButton.click();
    }

    public void clearCredentialFormFields(){
        this.urlField.clear();
        this.userNameField.clear();
        this.passwordField.clear();
    }

}
