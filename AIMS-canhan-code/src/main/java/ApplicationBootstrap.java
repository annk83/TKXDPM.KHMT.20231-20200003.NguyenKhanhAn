import controller.impl.CartController;
import dmui.content.PlaceOrderUI;
import repo.impl.SQLProvinceRepo;
import utils.Constant;
import dmui.content.ViewCartUI;
import dmui.toplevel.TopLevelFrame;
import domain.impl.LocalCart;
import repo.impl.NoProxyRepository;
import repo.impl.SimpleConnection;
import utils.IEtc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ApplicationBootstrap {
    private static final String CART_FILE = "cart.bin";
    public static void main(String[] args) throws SQLException {
        try {
            TopLevelFrame topLevelFrame = new TopLevelFrame();
            try {
                if (!Files.exists(Path.of(CART_FILE))) {
                    Files.createFile(Path.of(CART_FILE));
                }
            }
            catch (Exception exception) {
                topLevelFrame.getMessageDisplayer().displayCriticalError("File system given up!");
            }

            LocalCart localCart=null;
            IEtc config = new Constant();
            try(var dbCon = new SimpleConnection("jdbc:mariadb://localhost:3306/aims", "annk", "1", false)) {
                var repo = new NoProxyRepository(dbCon);
                try (var fis = new FileInputStream(CART_FILE)) {
                    localCart = new LocalCart(fis, repo, topLevelFrame.getMessageDisplayer(),config);
                }
                var navigator = topLevelFrame.getContentNavigator();
                navigator.register(new ViewCartUI(new CartController(localCart, config, new SQLProvinceRepo(dbCon)), new Constant()));
                navigator.register(new PlaceOrderUI());
                navigator.changeTo(ViewCartUI.class);
                topLevelFrame.run();
            } catch (SQLException sqlException) {
                topLevelFrame.getMessageDisplayer().displayCriticalError("CANT CONNECT TO DATABASE");
                sqlException.printStackTrace();
            }
            finally {
                if(localCart != null) {
                    try(var fis = new FileOutputStream(CART_FILE)) {
                        localCart.saveToOutputStream(fis);
                    }
                    catch (IOException exception) {
                        topLevelFrame.getMessageDisplayer().displayCriticalError("CANT SAVE CART");
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("error: "+exception.getMessage());
            exception.printStackTrace();
        }
    }
}
