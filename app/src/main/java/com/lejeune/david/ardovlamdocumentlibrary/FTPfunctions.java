package com.lejeune.david.ardovlamdocumentlibrary;


import android.content.Context;
import android.os.Environment;
import android.util.Log;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by Lucian on 2/9/2017.
 */

public class FTPfunctions {

    com.lejeune.david.ardovlamdocumentlibrary.MenuActivity.AsyncUpdatesDownloadDL mThreadReference = null;

    private static final String TEMP_FILENAME = "DiscreteSystemsandSignalProcessing.pdf";
    private static final String FOLDER_DATA = "/DocLib/Data/";
    private static final String FOLDER_DATA_USERS = FOLDER_DATA + "Users/";
    private static final String FOLDER_DATA_DOCUMENTS = FOLDER_DATA + "Documents/";
    private static final String FOLDER_DATA_DOCUMENTS_ALL = FOLDER_DATA + "Documents/ALL/";
    private  static  final String FOLDER_DOCUMENTS = "/DocLib/Documents/";

    private static final String TEMP_FOLDERNAME_STORAGE = "DocLib";
    private static final String TEMP_FOLDERNAME_FTP = "DocLib";
    private static final String DOC_FOLDERNAME_STORAGE = TEMP_FOLDERNAME_STORAGE + "/DOCUMENTS";
    private static final String DOC_FOLDERNAME_FTP = TEMP_FOLDERNAME_FTP + "/DOCUMENTS";

    private static final String TAG = "MyFTPClientFunctions";
    public FTPClient mFTPClient = null;

    // Method to connect to FTP server:
    public boolean ftpConnect(String host, String username, String password,
                              int port) {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);

            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(username, password);

				/*
				 * Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                System.out.println("function FTP connect :");
                System.out.println(">Status MyFTPFunctions: " + status);
                return status;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host);
        }

        return false;
    }

    // Method to disconnect from FTP server:

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }

        return false;
    }

    // Method to get current working directory:

    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not get current working directory.");
        }

        return null;
    }

    // Method to change working directory:

    public boolean ftpChangeDirectory(String directory_path) {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {
            Log.d(TAG, "Error: could not change directory to " + directory_path);
        }

        return false;
    }

    // Method to list all files in a directory:
    public String[] ftpPrintFilesList(String dir_path) {
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                    fileList[i] = "File :: " + name;
                    Log.i(TAG, "File : " + name);
                } else {
                    fileList[i] = "Directory :: " + name;
                    Log.i(TAG, "Directory : " + name);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            return fileList;
        }
    }



    public boolean downloadDataFiles(String dir_path) {
        boolean status = false;
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();
                //Log.d("DD", "/David/Documents/" + name);

                if (isFile) {
                    fileList[i] = "File :: " + name;
                    Log.i(TAG, "File : " + name);


                    //System.out.println("function bulk ftpDownload :");
                    //System.out.println(">file : " + name);
                    status = ftpDownload(MyVars.FOLDER_DATA + name, Environment.getExternalStorageDirectory()
                            + MyVars.FOLDER_DATA + name);
                    //System.out.println(">Status bulk ftpDownload: " + status);

                } else {
                    fileList[i] = "Directory :: " + name;
                    Log.i(TAG, "Directory : " + name);
                }

            }

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }
    public boolean downloadCommercialFiles(String dir_path) {
        boolean status = false;
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();
                //Log.d("DD", "/David/Documents/" + name);

                if (isFile) {
                    fileList[i] = "File :: " + name;
                    Log.i(TAG, "File : " + name);


                    System.out.println("function bulk ftpDownload :");
                    System.out.println(">file : " + name);
                    status = ftpDownload(MyVars.FOLDER_COMMERCIAL + name, Environment.getExternalStorageDirectory()
                            + MyVars.FOLDER_COMMERCIAL + name);
                    System.out.println(">Status bulk ftpDownload: " + status);

                } else {
                    fileList[i] = "Directory :: " + name;
                    Log.i(TAG, "Directory : " + name);
                }

            }

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }

    public boolean downloadStatsFiles(String dir_path) {
        boolean status = false;
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();
                //Log.d("DD", "/David/Documents/" + name);

                if (isFile) {
                    fileList[i] = "File :: " + name;
                    Log.i(TAG, "File : " + name);


                    //System.out.println("function bulk ftpDownload :");
                    //System.out.println(">file : " + name);
                    status = ftpDownload(MyVars.FOLDER_STATS + name, Environment.getExternalStorageDirectory()
                            + MyVars.FOLDER_STATS + name);
                    //System.out.println(">Status bulk ftpDownload: " + status);

                } else {
                    fileList[i] = "Directory :: " + name;
                    Log.i(TAG, "Directory : " + name);
                }

            }

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }


    // Method to create new directory:

    public boolean ftpMakeDirectory(String new_dir_path) {
        try {
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not create new directory named "
                    + new_dir_path);
        }

        return false;
    }

    // Method to delete/remove a directory:

    public boolean ftpRemoveDirectory(String dir_path) {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not remove directory named " + dir_path);
        }

        return false;
    }

    // Method to delete a file:

    public boolean ftpRemoveFile(String filePath) {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Method to rename a file:

    public boolean ftpRenameFile(String from, String to) {
        try {
            boolean status = mFTPClient.rename(from, to);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Could not rename file: " + from + " to: " + to);
        }

        return false;
    }

    // Method to download a file from FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: path to the source file in FTP server desFilePath: path to
     * the destination file to be saved in sdcard
     */
    public boolean ftpDownload(String srcFilePath, String desFilePath) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);

            //System.out.println("function ftpDownload :");
            //System.out.println(">srcFilePath : " + srcFilePath);
            //System.out.println(">desFilePath : " + desFilePath);
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();
            //System.out.println(">Status ftpDownload: " + status);



            // this because there is no other check on the validity of the file
            // if file doesn't exist it creates an empty file that needs to be deleted
            if (status == false)
            {
                //File dir = Environment.getExternalStorageDirectory();
                File file = new File(desFilePath);
                boolean deleted = file.delete();
            }


            return status;
        } catch (Exception e) {
            Log.d(TAG, "download failed");
        }

        return status;
    }

    // Method to upload a file to FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: source file path in sdcard desFileName: file name to be
     * stored in FTP server desDirectory: directory path where the file should
     * be upload to
     */
    public boolean ftpUpload(String srcFilePath, String desFileName,
                             String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);

            // change working directory to the destination directory
            // if (ftpChangeDirectory(desDirectory)) {
            //System.out.println("function ftpUpload :");
            //System.out.println(">desFileName : " + desFileName);
            //System.out.println(">srcFileStream : " + srcFileStream);
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            //System.out.println(">Status ftpUpload: " + status);
//

            // }

            srcFileStream.close();

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }

        return status;
    }




}
