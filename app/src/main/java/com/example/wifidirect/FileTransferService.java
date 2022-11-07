package com.example.wifidirect;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FileTransferService extends IntentService {

    private static final String TAG = "wifiDemo" + FileTransferService.class.getSimpleName();
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SNED_FILE = "com.example.wifidirect.SEND_FILE";
    public static final String EXTRA_FILE_PATH = "file_url";
    public static final String EXTRA_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRA_GROUP_OWNER_PORT = "go_port";

    public FileTransferService(String name) {
        super(name);
    }

    public FileTransferService() {
        super("FileTransferService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        if (intent.getAction().equals(ACTION_SNED_FILE)) {
            String fileUrl = intent.getExtras().getString(EXTRA_FILE_PATH);
            String host = intent.getExtras().getString(EXTRA_GROUP_OWNER_ADDRESS);
            Socket socket  = new Socket();
            int port = intent.getExtras().getInt(EXTRA_GROUP_OWNER_PORT);

            try {
                Log.d(TAG, "onHandleIntent: open client socket - ");
                socket.bind(null);
                socket.connect(new InetSocketAddress(host, port), SOCKET_TIMEOUT);
                Log.d(TAG, "onHandleIntent: client socket - " + socket.isConnected());
                OutputStream stream = socket.getOutputStream();
                ContentResolver cr = context.getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = cr.openInputStream(Uri.parse(fileUrl));
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "onHandleIntent: " + e.getMessage());
                }
            } catch (IOException e) {
                Log.d(TAG, "onHandleIntent: " + e.getMessage());
            }
        }
    }
}