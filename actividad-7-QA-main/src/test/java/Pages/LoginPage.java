package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }


    //selectors

    By username = By.name("user-name");
    By password = By.name("password");
    By login_btn = By.name("login-button");

    By mensaje_usuario_no_valido = By.cssSelector("h3[data-test='error']");



/*********************************************************************************/
    public void enter_username(String usernameval){
        try { Thread.sleep(2000); } catch (Exception e) {}
        driver.findElement(username).sendKeys(usernameval);
    }

    public void enterpassword(String passwordval){
        try { Thread.sleep(2000); } catch (Exception e) {}
        driver.findElement(password).sendKeys(passwordval);
    }

    public void clickLogin_btn(){
        try { Thread.sleep(2000); } catch (Exception e) {}
        driver.findElement(login_btn).click();
    }

   /// //////////////////////////////////////////////////////
// mensajes
    public String mensaje_usuario_erroneo(){

        return driver.findElement(mensaje_usuario_no_valido).getText();
    }
    /// ///////////////////////////////////////////////////////////
     //iniciar secion
    public void login(String usernameval, String passwordval){
        this.enter_username(usernameval);
        this.enterpassword(passwordval);
        this.clickLogin_btn();
    }




/*********************************************************************************/






}