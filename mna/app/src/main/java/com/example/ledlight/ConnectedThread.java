package com.example.ledlight;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectedThread extends Thread{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = mmInStream.available();
                if(bytes != 0) {
                    buffer = new byte[1024];
                    SystemClock.sleep(100);
                    bytes = mmInStream.available();
                    bytes = mmInStream.read(buffer, 0, bytes);
                    mHandler.obtainMessage(Bluetooth.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
        }
    }

    public void write(String input) {
        byte[] bytes = input.getBytes();
        try {
            mmOutStream.write(bytes);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) { }


//        Queue<byte[]> bufferQueue = new LinkedList<byte[]>();
//        int bytes;
//        while (true) {
//            try {
//                bufferQueue.offer(new byte[1024]);
//                bytes = mmInStream.read(bufferQueue.peek());
//                BluetoothService.this.mHandler.obtainMessage(BluetoothPlugin.MESSAGE_READ, bytes, -1, bufferQueue.poll()).sendToTarget();
//            } catch (IOException e) {
//                Log.e(TAG, "disconnected", e);
//                connectionLost();
//                break;
//            }
//        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
