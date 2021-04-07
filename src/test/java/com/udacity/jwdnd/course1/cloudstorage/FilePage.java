package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FilePage {

    @Getter
    @FindBy(id="fileUpload")
    private WebElement chooseFilebutton;

    @Getter
    @FindBy(id="submit-file-button")
    private WebElement submitFileButton;

    @Getter
    @FindBy(xpath = "//*[@id='fileTable']/tbody/tr/td[1]/a[2]")
    private WebElement deleteFileButton;

    @Getter
    @FindBy(id="file-success-msg")
    private WebElement fileCreatedSuccessMsg;

    @Getter
    @FindBy(id="file-delete-msg")
    private WebElement fileDeletedSuccessMsg;

    @Getter
    @FindBy(id="file-error-msg")
    private WebElement fileErrorMsg;

    public FilePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }
}
