package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import hooks.Hooks;
import runner.Runner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ReportUtils {

    //extent report initialization
    public static ExtentReports extentReportInitialization() {
        GlobalVariables.reportPath.set(System.getProperty("user.dir") + File.separator + "Reports" + File.separator
                + "HTMLReports" + File.separator + "API-Automation-Report.html");

        ThreadLocal<ExtentSparkReporter> extentSparkReporter = new ThreadLocal<>();
        extentSparkReporter.set(new ExtentSparkReporter(GlobalVariables.reportPath.get()));

        Runner.setExtentReports(new ExtentReports());
        Runner.getExtentReports().attachReporter(extentSparkReporter.get());

//        Logutils.infoLog(Runner.class,"=========");
//        Logutils.infoLog(Runner.class," Suite started at" +  new Date());
//        Logutils.infoLog(Runner.class," Suite started at" +  new Date());

        return Runner.extentReports.get();
    }

    public static void extentReportClosure(ExtentReports extentReports) {
        try {
            System.out.println("Suite finished at " + new Date());

            if (Runner.extentReports.get() != null) {
                Runner.extentReports.get().setSystemInfo("Total Scenarios", String.valueOf(Hooks.count.get()));
                Runner.extentReports.get().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openReport() {
        File report = new File(GlobalVariables.reportPath.get());
        if (report.exists() && report.isFile()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(report.toURI());
                    System.out.println("Extent Report opened successfully.");
                } else {
                    System.out.println("Desktop browsing is not supported.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Extent Report does not exist: " + report.getAbsolutePath());
        }
    }

}
