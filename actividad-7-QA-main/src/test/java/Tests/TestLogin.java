package Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestLogin {

    static WebDriver driver;
    static LoginPage login;

    // abre navegador
    @BeforeMethod
    public void abrir_navegador() {

        WebDriverManager.chromedriver().setup();

        // 1. Crear configuración para desactivar el aviso de contraseñas de Chrome
        org.openqa.selenium.chrome.ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection");

        // 2. Pasar las opciones al inicializar el ChromeDriver
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");

        login = new LoginPage(driver);
    }

    @Test
    public  void Login_test_valido() {
        login.login("standard_user", "secret_sauce");

        String url_esperada= "https://www.saucedemo.com/inventory.html";

        Assert.assertEquals(driver.getCurrentUrl(), url_esperada);
    }

    @Test
    public  void Login_test_usuario_no_valido() {
        login.login("", "secret_sauce");

        String mensajeesperado = "Epic sadface: Username is required";

        Assert.assertEquals(login.mensaje_usuario_erroneo(), mensajeesperado);
    }

    @Test
    public  void Login_test_contrasena_no_valido() {
        login.login("standard_user", "gato");

        String mensajeesperado = "Epic sadface: Username and password do not match any user in this service";

        Assert.assertEquals(login.mensaje_usuario_erroneo(), mensajeesperado);
    }

    @Test
    public  void Login_test_usuario_bloqueado() {

        login.login("locked_out_user", "secret_sauce");
        String mensajeesperado = "Epic sadface: Sorry, this user has been locked out.";

        Assert.assertEquals(login.mensaje_usuario_erroneo(), mensajeesperado);

    }

    @Test
    public  void Login_test_vacio() {

        login.login("", "");

        String mensajeesperado = "Epic sadface: Username is required";

        Assert.assertEquals(login.mensaje_usuario_erroneo(), mensajeesperado);
    }

    //cierra navegador
    @AfterMethod
    public static void cerrar_navegador() {

        driver.quit();

    }
}