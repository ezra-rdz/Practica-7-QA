package Tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import Pages.LoginPage;
import Pages.sawg_labs_page;

import org.testng.Assert;
import org.testng.annotations.*;

public class Test_sawg_labs {
    static WebDriver driver;
    static LoginPage login;
    static sawg_labs_page sawgLabsPage;

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

        sawgLabsPage = new sawg_labs_page(driver);
    }

    /// //////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void agregar_elemento_carrito() {
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.agregar_carrito();
        Assert.assertEquals(sawgLabsPage.obtenerCantidadCarrito(), "1");
    }

    /// //////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void eliminar_elemento_carrito() {
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.agregar_carrito();
        sawgLabsPage.eliminar_carrito();
        Assert.assertEquals(sawgLabsPage.obtenerCantidadCarrito(), "0");
    }

    /// ////////////////////////////////////////////////////////////////////// /// //////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void no_agregar_nombre_informacion() {
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.no_agregar_elemento_informacion("","manzo","1234");
        try { Thread.sleep(2000); } catch (Exception e) {}
        String mensajeesperado = "Error: First Name is required";

        Assert.assertEquals(sawgLabsPage.obtenerMensajeError(), mensajeesperado);
    }

    @Test
    public void no_agregar_direccion_informacion() {
        login.login("standard_user", "secret_sauce");

        sawgLabsPage.no_agregar_elemento_informacion("juan","","1234");
        try { Thread.sleep(2000); } catch (Exception e) {}

        String mensajeesperado = "Error: Last Name is required";

        Assert.assertEquals(sawgLabsPage.obtenerMensajeError(), mensajeesperado);
    }

    @Test
    public void no_agregar_cp_informacion() {
        login.login("standard_user", "secret_sauce");

        sawgLabsPage.no_agregar_elemento_informacion("juan","manzo","");
        try { Thread.sleep(2000); } catch (Exception e) {}

        String mensajeesperado = "Error: Postal Code is required";

        Assert.assertEquals(sawgLabsPage.obtenerMensajeError(), mensajeesperado);
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////////////
    // ordena de menor a mayor
    @Test
    public void filtrar_menor_mayor(){
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.orden_menor_mayor();
        String elemento = "Price (low to high)";
        Assert.assertEquals(sawgLabsPage.obtenerFiltroSeleccionado(),elemento);
    }

    @Test
    public void filtrar_z_a(){
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.orden_inverso();
        String elemento = "Name (Z to A)";
        Assert.assertEquals(sawgLabsPage.obtenerFiltroSeleccionado(),elemento);
    }

    @Test
    public void filtrar_mayor_menor(){
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.orden_mayor_menor();
        String elemento = "Price (high to low)";
        Assert.assertEquals(sawgLabsPage.obtenerFiltroSeleccionado(),elemento);
    }

    @Test
    public void cerrar_secion(){
        login.login("standard_user", "secret_sauce");
        sawgLabsPage.cerrar_Secion();
        try { Thread.sleep(5000); } catch (Exception e) {}
        String url_esperada= "https://www.saucedemo.com/";

        Assert.assertEquals(driver.getCurrentUrl(), url_esperada);
    }

    @Test
    public void realizar_compra_exitosa_informacion() {
        // 1. Iniciar sesión
        login.login("standard_user", "secret_sauce");
        try { Thread.sleep(1500); } catch (Exception e) {}

        // 2. Ejecutar todo el flujo de compra automatizado en la página
        sawgLabsPage.realizar_compra_exitosa("Ezra", "Manzo", "21000");

        // 3. Capturar el texto real de la UI y compararlo en el Assert
        String mensajeReal = sawgLabsPage.obtenerMensajeCompraExitosa();
        String mensajeesperado = "Thank you for your order!";

        Assert.assertEquals(mensajeReal, mensajeesperado);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    //cierra navegador
    @AfterMethod
    public static void cerrar_navegador() {
        driver.quit();
    }
}
