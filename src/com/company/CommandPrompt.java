package com.company;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.text.SimpleDateFormat;


public class CommandPrompt {

    public File currentDir = new File(".");
    public String ip ="127.0.0.1";
    public int port = 8899;

    public void exec(String statement)throws IOException {
        String command = "";
        String option = "";
        String msg = "";

        int endIdx = statement.trim().indexOf(' ');
        if (endIdx > 0) {
            command = statement.substring(0, endIdx).trim();
            option = statement.substring(endIdx + 1).trim();
        } else {
            command = statement;
        }

        switch (command.toLowerCase()) {
            case"/dl":
                downloadFile();
            case "cd":
//                changeDir(option);
                break;
            case "dir":
//                listFiles(option);
                break;
            case "freespace":
//                msg = String.format("The free space is %,d bytes.", checkFreespace(option));
//                System.out.println(msg);
                break;
            default:
                msg = String.format("'%s' is not recognized as an command.", command);
                System.out.println(msg);
                break;
        }
    }

    private void downloadFile() throws IOException{
        Socket socket = new Socket(ip, port);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String filename = sendMsg(out);
        receiveFile(in, filename);
        in.close();
        socket.close();
    }

    private String sendMsg(DataOutputStream out) throws IOException {
        System.out.print("Input a file name: ");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        int size = msg.length();
        out.writeInt(size);
        out.write(msg.getBytes());
        scanner.close();
        return msg;
    }

    private void receiveFile(DataInputStream in, String filename) throws IOException {
        byte[] buffer = new byte[1024];
        long count = 0, size;
        int len;
        File file;
        FileOutputStream fout;
        file = new File(filename);
        fout = new FileOutputStream(file);

        size = in.readLong();
        count = 0;

        while (count < size) {
            len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
            count += len;
            fout.write(buffer, 0, len);
        }
        fout.close();
    }

//    private void listFiles(String path) {
//        File dir;
//
//        if (path == null)
//            dir = currentDir;
//        else
//            dir = new File(path);
//
//        if (!dir.exists()) {
//            System.out.println("File / directory does not exist.\n" + dir);
//            return;
//        }
//
//        if (dir.isFile())
//            System.out.println(getInfo(dir));
//        else
//        {
//            File[] fileList = dir.listFiles();
//            String info = "";
//            for (int i = 0; i < fileList.length; i++)
//                info += getInfo(fileList[i]) + "\n";
//            System.out.println(info);
//        }
//    }
//
//    private String getInfo(File f) {
//        Date date = new Date(f.lastModified());
//        String ld = new SimpleDateFormat("MMM dd, yyyy").format(date);
//        if (f.isFile()) {
//            return String.format("%dKB\t%s\t%s", (int) Math.ceil((float) f.length() / 1024), ld, f.getName());
//        } else
//            return String.format("<DIR>\t%s\t%s", ld, f.getName());
//    }
//
//    private long checkFreespace(String path) throws IOException {
//        File dir;
//        if (path == null)
//            dir = currentDir;
//        else if (path.startsWith("/") || path.startsWith("\\") || path.contains(":"))
//            dir = new File(path);
//        else
//            dir = new File(currentDir.getCanonicalPath() + "/" + path);
//
//        return dir.getFreeSpace();
//    }
//
//    private void changeDir(String path) throws IOException {
//        if (path == null) {
//            System.out.println(currentDir.getCanonicalPath());
//            return;
//        }
//
//        File dir;
//        if (path.startsWith("/") || path.startsWith("\\") || path.contains(":"))
//            dir = new File(path);
//        else
//            dir = new File(currentDir.getCanonicalPath() + "/" + path);
//
//        if (!dir.exists() || dir.isFile()) {
//            System.out.println("The system cannot find the path specified.");
//            return;
//        }
//
//        currentDir = dir;
//        System.out.println("cd to "+dir);
//    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String statement;
        CommandPrompt prompt = new CommandPrompt();

        while (true) {
            System.out.print("ip address/>");
            statement = scanner.nextLine().trim();
            if (statement.equalsIgnoreCase("/exit"))
                break;
            prompt.exec(statement);
        }
        scanner.close();
    }

}

