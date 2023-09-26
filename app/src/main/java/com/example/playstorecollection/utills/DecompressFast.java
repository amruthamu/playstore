package com.example.playstorecollection.utills;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DecompressFast {



    private String _zipFile;
    private String _location;

    public DecompressFast(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }



    public void unzip() {
        try {
            File directory = new File(_location);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory and its parent directories if they don't exist
            }

            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    String filePath = _location + ze.getName();
                    File file = new File(filePath);

                    // Ensure parent directories exist
                    File parentDir = file.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    try (FileOutputStream fout = new FileOutputStream(file);
                         BufferedOutputStream bufout = new BufferedOutputStream(fout)) {

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = zin.read(buffer)) != -1) {
                            bufout.write(buffer, 0, read);
                        }
                    } catch (IOException e) {
                        Log.e("Decompress", "Error while unzipping: " + e.getMessage());
                    }
                }
                zin.closeEntry();
            }
            zin.close();

            Log.d("Unzip", "Unzipping complete. Path: " + _location);
        } catch (Exception e) {
            Log.e("Decompress", "Error during unzip: " + e.getMessage());
            Log.d("Unzip", "Unzipping failed");
        }
    }



    public void unzipNew(String zipFilePath, String destinationFolderPath) {
        try {
            File destinationFolder = new File(destinationFolderPath);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            byte[] buffer = new byte[1024];
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                File entryFile = new File(destinationFolder, entryName);
                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int count;
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, count);
                    }
                    bos.flush();
                    bos.close();
                    fos.close();
                }
                zipInputStream.closeEntry();
            }

            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while((ze = zis.getNextEntry()) != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                FileOutputStream fout = new FileOutputStream(path + filename);

                // reading and writing
                while((count = zis.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    fout.write(bytes);
                    baos.reset();
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }


}
