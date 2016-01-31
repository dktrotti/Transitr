package dktrotti.transitr;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Grabs live bus data from a server and saves the .pb file
 *
 * Created by Dominic on 16-01-30.
 */
public class HTTPGrabber implements dktrotti.transitr.Observable {
    private static String connstr = "https://data.edmonton.ca/download/7qed-k2fc/application/octet-stream";
    public static String path = "/data/data/dktrotti.transitr/VehicleLocations.pb";

    private ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Retrieves the .pb file from the server.
     * @return Returns a string containing the result of the operation.
     * @throws IOException Thrown if an error occured during the transmission.
     */
    public void retrieveFile() throws IOException {
        GetTask task = new GetTask();
        task.execute("");
    }

    private class GetTask extends AsyncTask<String, Void, String> {
        Throwable ex;

        protected String doInBackground(String... params) {
            File file = new File(path);
            Integer count = 0;

            Long total = 0L;

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(connstr)
                        .build();
                Response response = client.newCall(request).execute();

                InputStream is = response.body().byteStream();

                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = new FileOutputStream(file);

                byte[] data = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                ex = e;
            }
            return total.toString();
        }

        protected void onPostExecute(String result) {
            if (ex == null) {
                updateObservers();
            } else if (ex instanceof SocketTimeoutException) {
                ex.printStackTrace();
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void updateObservers() {
        for (Observer obs: observers) {
            obs.update(this, null);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}

