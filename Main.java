import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Main {


    public static void main(String[] args) {

        TWReader twr = new TWReader();
        String fileName = "/home/himon/Jobs/CWP200T/page.dat";
        String testFileName = "/home/himon/Jobs/CWP200T/Test_zlib/output";

        try {

            FileInputStream fis = new FileInputStream((new File(fileName)));
            BufferedInputStream bis = new BufferedInputStream(fis);
            Page page = new Page();

            while ((page = twr.nextPage(bis)) != null){
                twr.print_Page(page);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}




