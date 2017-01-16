import java.io.*;

/**
 * Created by himon on 16-12-12.
 */
public class TWReader {
    private Page page;

    public TWReader() {
    }

    public  void print_Page(Page page){
        System.out.println("New record:");
        System.out.println("version: " + page.getVersion());
        System.out.println("url: " + page.getUrl());
        System.out.println("date: "+page.getDate());
        System.out.println("ip: " + page.getIp());
        System.out.println("unzip-length: "+page.getUnzip_length());
        System.out.println("length: "+page.getLength());
        System.out.println("body: \n"+page.getBody());
        System.out.println();
    }


    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = null;
        if ((byte_1.length >0) &&(byte_2.length > 0)){
            byte_3 = new byte[byte_1.length+byte_2.length];
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
            System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        }else if ((byte_1.length >0) &&(byte_2.length == 0)){
            byte_3 = byte_1;
        }else if ((byte_2.length >0) &&(byte_1.length == 0)){
            byte_3 = byte_2;
        }

        return byte_3;
    }

    public String byte2string(byte[] line){
        return new String(line);
    }

    /**
     * 自定义函数:以字节流读每行内容,对于body部分,由于不需要他的实际内容,我们暂时只是把这行内容记为:数组 body*,
     * return:次行内容的string形式.
     */
    public String readLine(BufferedInputStream bis){
        int batch_size = 100;
        byte[] line = new byte[batch_size];
        byte[] temp = new byte[batch_size];
        byte[] body = {'b','o','d','y'};
        int t = 0; //记录每行读到的字节数,最后用来判断return.
        try {
            // 一次读一个字节
            int tempbyte;
            //判断换行和是否结尾.
            while (((tempbyte = bis.read()) != -1)&& (tempbyte != '\n')){
                if (t < batch_size){
                    line[t++] = (byte)tempbyte;
                }else{
//                    line = body;
                        return byte2string(line);
                }
            }
            if ((t ==0)&&(tempbyte == '\n')){
                t++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t>0?byte2string(line):null;
    }

    public byte[] readLine_byte(BufferedInputStream bis){
        int batch_size = 100;
        byte[] line = new byte[batch_size];
        byte[] temp = new byte[batch_size];
        byte[] body = {'b','o','d','y'};
        int t = 0; //记录每行读到的字节数,最后用来判断return.
        try {
            // 一次读一个字节
            int tempbyte;
            //判断换行和是否结尾.
            while (((tempbyte = bis.read()) != -1)&& (tempbyte != '\n')){
                if (t < batch_size){
                    line[t++] = (byte)tempbyte;
                }else{
//                    line = body;
                    return line;
                }
            }
            if ((t ==0)&&(tempbyte == '\n')){
                t++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t>0?line:null;
    }

    public void test_read(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            System.out.println(reader.readLine()); //version
            System.out.println(reader.readLine()); //url
            System.out.println(reader.readLine()); //data
            System.out.println(reader.readLine()); //ip
            System.out.println(reader.readLine()); //unzip_length
            System.out.println(reader.readLine()); //length

            System.out.println(reader.readLine()); //空行
            char[] tempChar = new char[100];
            if (reader.read(tempChar,0,100)!=0)
                System.out.println(tempChar);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Page nextPage(String fileName) {

        Page page = new Page();
        File file = new File(fileName);
        InputStream in = null;
        BufferedInputStream bis = null;

        try {
            String str_line;
            in = new FileInputStream(file);
            bis =new BufferedInputStream(in);

            if((str_line = readLine(bis)) != null){

                if (str_line.startsWith("version: ")) {
//                    System.out.println("version#"+str_line);
                    page.setVersion(str_line.substring("version: ".length()));
                }

                if ((str_line = readLine(bis)) != null){
                    if (str_line.startsWith("url: "))
//                        System.out.println("url#" + str_line);
                        page.setUrl(str_line.substring("url: ".length()));
                }

                if ((str_line = readLine(bis)) != null){
                    if (str_line.startsWith("date: "))
//                        System.out.println("date#" + str_line);
                        page.setDate(str_line.substring("data: ".length()));
                }

                if ((str_line = readLine(bis)) != null){
                    if (str_line.startsWith("ip: "))
//                        System.out.println("ip#" + str_line);
                        page.setIp(str_line.substring("ip: ".length()));
                }

                if ((str_line = readLine(bis)) != null){
                    if (str_line.startsWith("unzip-length: ")){
//                        System.out.println("unzip-length#"+str_line);
                        String temp = str_line.substring("unzip-length: ".length());
//                        System.out.println("temp#"+temp);
                        int unzip_length = Integer.parseInt(temp.trim());
                        page.setUnzip_length(unzip_length);
                    }
                }

                if ((str_line = readLine(bis))!=null){
//                    System.out.println("length#" + str_line);
                    if (str_line.startsWith("length: ")){
                        String temp = str_line.substring("length: ".length());
                        int length = Integer.parseInt(temp.trim());
                        page.setLength(length);
                    }
                }
                readLine(bis);  //空行!
//                System.out.println("page length:" + page.length);

                byte[] body = new byte[page.length+1];
                if (bis.read(body, 0, page.length) != -1){
//                    System.out.println(new String(body));
                    byte[] output = ZLibUtils.decompress(body);
//                    System.out.println(byte2string(output));
                    page.setBody(byte2string(output));
                }
//                break;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return page.version.isEmpty()?null:page;
    }

    public Page nextPage(BufferedInputStream bis) {
        page = new Page();
        try {
            String str_line;
            while ((str_line = readLine(bis)) != null){

                if (!str_line.startsWith("version: "))
                    continue;
                page.setVersion(str_line.substring("version: ".length()));


                if ((str_line = readLine(bis)) != null){
                    if (!str_line.startsWith("url: "))
                        continue;
                    page.setUrl(str_line.substring("url: ".length()));
                }else {
                    return null;
                }

                if ((str_line = readLine(bis)) != null){
                    if (!str_line.startsWith("date: "))
                        continue;
                    page.setDate(str_line.substring("data: ".length()));
                }else {
                    return null;
                }

                if ((str_line = readLine(bis)) != null){
                    if (!str_line.startsWith("ip: "))
                        continue;
                    page.setIp(str_line.substring("ip: ".length()));
                }else {
                    return null;
                }

                if ((str_line = readLine(bis)) != null){
                    if (!str_line.startsWith("unzip-length: "))
                        continue;
                    String temp = str_line.substring("unzip-length: ".length());
                    int unzip_length = Integer.parseInt(temp.trim());
                    page.setUnzip_length(unzip_length);
                }else {
                    return null;
                }

                if ((str_line = readLine(bis))!=null){
                    if (!str_line.startsWith("length: "))
                        continue;
                    String temp = str_line.substring("length: ".length());
                    int length = Integer.parseInt(temp.trim());
                    page.setLength(length);

                }else {
                    return null;
                }

                if (readLine(bis) == null){     //还有一个空行!
                    return null;
                }

                byte[] body = new byte[page.length+1];
                if (bis.read(body, 0, page.length) != -1){
                    byte[] output = ZLibUtils.decompress(body);
                    page.setBody(byte2string(output));
                }
//                System.out.println("load one record success!");
                break;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return (page.version == null)?null:page;
    }

    /**
     * 修建byte[]
     * */
    public byte[] trim_byte(byte[] B){
        int i = 0;
        for (byte b:B){
            if (b != 0)
                i++;
        }
        byte[] trimed_byte = new byte[i];
        System.arraycopy(B, 0, trimed_byte, 0, i);
        return trimed_byte;
    }


    public  void test(String fileName){
        File file = new File(fileName);
        try {

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));

            byte[] body1 = new byte[12068+1]; //length +1,最后换行符也读进来
            if (bis.read(body1, 0, 12068) != -1){

                System.out.println("compressed body1:");
                System.out.println(byte2string(body1));
                byte[] output = ZLibUtils.decompress(body1);
                System.out.println("decompressed body1:");
                System.out.println(byte2string(output));

            }

            System.out.println("new record:");
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));

            byte[] body2 = new byte[2884+1]; //length +1,最后换行符也读进来
            if (bis.read(body2, 0, 2884) != -1){

                System.out.println("compressed body2:");
                System.out.println(byte2string(body2));
                byte[] output = ZLibUtils.decompress(body2);
                System.out.println("decompressed body2:");
                System.out.println(byte2string(output));

            }

            System.out.println("new record:");
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));
            System.out.println(readLine(bis));


        }catch (Exception e){
            e.printStackTrace();
        }

    }

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
//
//        byte[] test = new byte[20];
//        test[0] = 'a';
//        test[1] = 'b';
//        test[2] = 'c';
//        System.out.println("length:" + test.length);
//        System.out.println(twr.byte2string(test));
//        byte[] trimed = twr.trim_byte(test);
//        System.out.println(trimed.length);
//        System.out.println(twr.byte2string(trimed));

//        twr.test(fileName);



//        Page page;
//        while ( (page = twr.nextRecord2(fileName)) != null){
//
////            twr.print_Page(page);
//        }

//        twr.test_read(fileName);
//        try {
//            File file = new File(fileName);
//            InputStream in = null;
//            BufferedInputStream bis = null;
//            in = new FileInputStream(file);
//            bis =new BufferedInputStream(in);
//            Page page = new Page();
//            while ((page = twr.nextPage(fileName,page)) != null){
//
////                twr.print_Page(page);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        try {
//
//            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
//            byte[] line = twr.readLine(bis);
//            String line_str = new String(line);
//            System.out.println("line:"+line_str);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println(9%5);

    }

}
