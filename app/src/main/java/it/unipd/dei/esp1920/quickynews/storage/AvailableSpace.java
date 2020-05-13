package it.unipd.dei.esp1920.quickynews.storage;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;


public class AvailableSpace {

    private static String TAG="AvailableSpace";
    private static long availableSpace;

    public static long getTotalDiskSpace() {
        File path = Environment.getDataDirectory(); //file with the user data directory
        StatFs stFs = new StatFs(path.getPath()); //Statfs: retrieve overall information about the space on a filesystem.
        availableSpace=stFs.getBlockSizeLong() * stFs.getAvailableBlocksLong(); // [u]=Byte
        Log.i(TAG,"getTotalDisk() called: available space = "+availableSpace/1000000+" MB" );

        return availableSpace;
    }
}
