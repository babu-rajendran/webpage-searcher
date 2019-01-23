package com.wework.coding.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This class represents a unit of work of retrieving a URL's source and validating if the search
 * term is present.
 */
public class URLSearchTask implements Runnable {

    private String url;
    private String searchTerm;

    @Override
    /**
     * {@inheritDoc}
     */
    public void run() {
        try {
            String fullURL = "http://" + url;
            URL urlObject = new URL(fullURL);
            URLConnection urlConnection = urlObject.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String data = readFromInputStream(inputStream);
            boolean searchStatus = false;
            if (data.contains(searchTerm)) {
                searchStatus = true;
            }
            writeToFile(fullURL, searchStatus);
            inputStream.close();
        } catch (MalformedURLException e) {
            System.out.println("The URL given is malformed. Please check this URL again: " + url);
        } catch (IOException e) {
            System.out.println("An exception occurred in opening the connection to URL: " + url);
        }
    }

    /**
     * Creates an instance of {@link URLSearchTask} with the given URL and search term.
     *
     * @param url THe URL to be retrieved
     * @param searchTerm The string to be searched in the source
     */
    public URLSearchTask(String url, String searchTerm) {
        this.url = url;
        this.searchTerm = searchTerm;
    }

    /**
     * Creates a file and writes the search results to it.
     *
     * @param url The URL that was retrieved.
     * @param searchStatus The status of whether the search term was found or not
     */
    private void writeToFile(String url, boolean searchStatus) {
        Path path = Paths.get("./results.txt");
        try {
            Files.write(path, (url + " --> Search term found: " + searchStatus + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioe) {
            System.out.println("An exception occurred in writing the results to file");
        }
    }

    /**
     * Reads content from the given {@link InputStream}
     *
     * @return The content read from the input stream.
     */
    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String readContent;

        while ((readContent = bufferedReader.readLine()) != null) {
            stringBuffer.append(readContent);
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }

}