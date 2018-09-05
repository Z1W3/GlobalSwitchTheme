package cattt.assets.theme.library.utils.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger that wraps j.u.l to make the APIs easier to call.
 * Usage:
 * <pre>
 * Log logger = Log.getLogger("klassName");
 * logger.e("TAG", "Something is wrong!");
 * </pre>
 */
public class Log {

    private Logger logger;

    private Log() {
    }

    public Log(Class cls) {
        logger = Logger.getLogger(cls.getSimpleName());
    }

    public static Log getLogger(Class cls) {
        return new Log(cls);
    }

    public void d(String msg, Object... objects) {
        logger.log(Level.FINE, String.format(msg, objects));
    }

    public void d(String msg, Throwable throwable) {
        logger.log(Level.FINE, msg, throwable);
    }

    public void i(String msg, Object... objects) {
        logger.log(Level.INFO, String.format(msg, objects));
    }

    public void i(String msg, Throwable throwable) {
        logger.log(Level.INFO, msg, throwable);
    }


    public void w(String msg, Object... objects) {
        logger.log(Level.WARNING, String.format(msg, objects));
    }

    public void w(String msg, Throwable throwable) {
        logger.log(Level.WARNING, msg, throwable);
    }

    public void e(String msg, Object... objects) {
        logger.log(Level.SEVERE, String.format(msg, objects));
    }

    public void e(String msg, Throwable throwable) {
        logger.log(Level.SEVERE, msg, throwable);
    }
}
