package com.wework.coding.challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebsiteSearcher {

    public static void main(String[] args) throws Exception {
        System.out.println("Path to file : " + args[0]);
        System.out.println("Search term : " + args[1]);
        CustomThreadPool customThreadPool = new CustomThreadPool(
            20); //create 20 threads in CustomThreadPool
        processFile(args[0], args[1], customThreadPool);
    }

    static void processFile(String filePath, String searchTerm, CustomThreadPool threadPool) {
        try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
            String line;
            in.readLine(); // Skip the first line containing headers
            List<String> urlList = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                String url = line
                    .replaceAll("\\d+,\"(.*?)\",\\d+,\\d+,\\d+.\\d+,\\d+.\\d+", "$1");
                urlList.add(url);
                Runnable task = new URLSearchTask(url, searchTerm);
                threadPool.execute(task);
            }
            threadPool.shutdown();

        } catch (FileNotFoundException fnfe) {
            System.out.println("Please check if the file exists in the filesystem");
        } catch (IOException ioe) {
            System.out.println("An error occurred in reading the file");
        } catch (Exception e) {
            System.out.println("An exception occurred");
        }

    }

}

