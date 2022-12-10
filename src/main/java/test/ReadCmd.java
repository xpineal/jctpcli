package test;

import org.kcr.jctpcli.cnf.FJson;

public class ReadCmd {
   public static void main(String[] args) throws InterruptedException {
       for (int i = 0; i < 1000; i++) {
           var cmd = FJson.readCmdThenReset("/tmp/ctp-trade.json");
           System.out.println(cmd);
           Thread.sleep(7000);
       }
    }
}
