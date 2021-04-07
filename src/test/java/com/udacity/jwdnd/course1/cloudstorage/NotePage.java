package com.udacity.jwdnd.course1.cloudstorage;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @Getter
    @FindBy(id="nav-notes-tab")
    private WebElement noteTabButton;

    @Getter
    @FindBy(id="add-new-note")
    private WebElement addNewNoteButton;

    @Getter
    @FindBy(xpath = "//*[@id='noteTable']/tbody/tr/td[1]/button")
    private WebElement editNoteButton;

    @Getter
    @FindBy(xpath = "//*[@id='noteTable']/tbody/tr/td[1]/a")
    private WebElement deleteNoteButton;

    @FindBy(id="note-title")
    private WebElement titleField;

    @FindBy(id="note-description")
    private WebElement descriptionField;

    @Getter
    @FindBy(id="save-new-note")
    private WebElement createNoteButton;

    @Getter
    @FindBy(id="note-success-msg")
    private WebElement noteCreatedSuccessMsg;

    @Getter
    @FindBy(id="note-delete-msg")
    private WebElement noteDeletedSuccessMsg;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void saveNote(String title, String description) {
        this.titleField.sendKeys(title);
        this.descriptionField.sendKeys(description);
        this.createNoteButton.click();
    }

    public void clearNoteFormFields(){
        this.titleField.clear();
        this.descriptionField.clear();
    }
}
