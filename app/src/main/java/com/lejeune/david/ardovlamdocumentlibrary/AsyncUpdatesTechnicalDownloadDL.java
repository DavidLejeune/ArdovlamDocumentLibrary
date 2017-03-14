//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.app.ProgressDialog;
//import android.content.pm.ActivityInfo;
//import android.os.AsyncTask;
//import android.os.Environment;
//
//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPReply;
//
//import java.io.File;
//import java.io.FileOutputStream;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncUpdatesTechnicalDownloadDL  extends AsyncTask<String, String, String> {
//
//    boolean status = false;
//    private FTPfunctions ftpclient = null;
//
//    ProgressDialog pd;
//
//
//
//    long startTime ;
//    long stopTime ;
//    long elapsedTime ;
//    int iCount =0;
//    int progress = 0;
//    String downloadedFile = "" ;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//
//        pd = ProgressDialog.show(MenuActivity.this, "", "Getting Technical Files...",
//                true, false);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        pd.dismiss();
//        for(int i=0; i<100 ;i++) {
//            MyStats.logEntry("UPDATE", "TEC-" + MyVars.updatetecserver);
//        }
//        if (status == true) {
//
//            setUpdatenrTec(cntx);
//            compareUpdateNrsTec();
//        }
//
//    }
//
//
//    @Override
//    protected String doInBackground(String... params) {
//
//
//        ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
//
//
//        status = downloadUpdateFilesTec(MyVars.FOLDER_TECHNICAL);
//        System.out.println(MyVars.FOLDER_TECHNICAL + " status : " + status);
//
//
//        if (status == true) {
//            System.out.println("Download success");
////                    handler.sendEmptyMessage(5);
//
////                setUpdatenrTec(cntx);
////                compareUpdateNrsTec();
//        } else {
//            System.out.println("Download failed");
////                    handler.sendEmptyMessage(-1);
//        }
//
//        ftpDisconnect();
//        return null;
//    }
//
//    public boolean ftpDisconnect() {
//        try {
//            mFTPClient.logout();
//            mFTPClient.disconnect();
//            return true;
//        } catch (Exception e) {
//            System.out.println("Error occurred while disconnecting from ftp server.");
//        }
//
//        return false;
//    }
//
//
//    public boolean downloadUpdateFilesTec(String dir_path) {
//        boolean status = false;
//        String[] fileList = null;
//        int iNot5 = 0;
//        System.out.println("downloadUpdateFileCom in FTPfunctions");
//        System.out.println("dir_path : " + dir_path);
//        try {
//            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
//            int length = ftpFiles.length;
//            System.out.println("length : " + length);
//            fileList = new String[length];
//            for (int i = 0; i < length; i++) {
//                String name = ftpFiles[i].getName();
//                boolean isFile = ftpFiles[i].isFile();
//                //Log.d("DD", "/David/Documents/" + name);
//
//                if (isFile) {
//                    fileList[i] = "File :: " + name;
//
//
//                    System.out.println("looping through the arraylist");
//                    //MenuActivity.AsyncUpdatesDownloadDL.publishProgress();
//                    System.out.println("listUpdateFiles.length : " + listUpdateFilesTec.size());
//                    for (String cu : listUpdateFilesTec) {
//                        //System.out.println("list Update entry : " + cu);
//                        if (cu.equalsIgnoreCase(name)){
//                            //iCount +=1;
//                            System.out.println("iCount = " +iCount);
//                            System.out.println("found this file in update list");
//                            System.out.println(">file : " + name);
//                            System.out.println(">cu   : " + cu);
//                            System.out.println("function bulk ftpDownload updates :");
//                            System.out.println(">file : " + name);
//
//                            downloadedFile = name;
//                            //publishProgress(iCount);
//
//                            status = ftpDownload(MyVars.FOLDER_TECHNICAL + name,
//                                    Environment.getExternalStorageDirectory() + MyVars.FOLDER_TECHNICAL + name);
//
//
//
//
//                            String imgName = name.replace(".pdf", ".PNG");
//                            downloadedFile = imgName;
//                            //publishProgress(iCount);
//                            ftpDownload(MyVars.FOLDER_TECHNICAL + imgName,
//                                    Environment.getExternalStorageDirectory() + MyVars.FOLDER_TECHNICAL + imgName);
//
//
//
//
//
//                            String CurrentString = cu;
//                            String[] separated = CurrentString.split("-");
//                            int iSplit = 0;
//                            for (String split : separated){
//                                iSplit += 1;
//                                System.out.println("split " + iSplit + " : " + split);
//                            }
//                            if(iSplit != 5){
//                                iNot5 +=1;
//                                System.out.println("WARNING iNot5 !!!!!!!!!!!!!!!!!!!!");
//                            }
//
//
//                        }
//                    }
////                    System.out.println("function bulk ftpDownload updates :");
////                    System.out.println(">file : " + name);
////                    status = ftpDownload(FOLDER_DATA_DOCUMENTS_ALL + name,
////                            Environment.getExternalStorageDirectory() + FOLDER_DATA_DOCUMENTS_ALL + name);
//
//
//                    System.out.println(">Status bulk ftpDownload: " + status);
//
//                } else {
//                    fileList[i] = "Directory :: " + name;
//                }
//
//            }
//
//            System.out.println("iNot5 : " + iNot5);
//
//
//            return status;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return status;
//        }
//    }
//
//
//    // Method to connect to FTP server:
//    public boolean ftpConnect(String host, String username, String password,
//                              int port) {
//        try {
//            mFTPClient = new FTPClient();
//            // connecting to the host
//            mFTPClient.connect(host, port);
//
//            // now check the reply code, if positive mean connection success
//            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
//                // login using username & password
//                boolean status = mFTPClient.login(username, password);
//
//				/*
//				 * Set File Transfer Mode
//				 *
//				 * To avoid corruption issue you must specified a correct
//				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
//				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
//				 * transferring text, image, and compressed files.
//				 */
//                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
//                mFTPClient.enterLocalPassiveMode();
//
//                System.out.println("function FTP connect :");
//                System.out.println(">Status MyFTPFunctions: " + status);
//                return status;
//            }
//        } catch (Exception e) {
//            System.out.println("Error: could not connect to host " + host);
//        }
//
//        return false;
//    }
//
//
//    public boolean ftpDownload(String srcFilePath, String desFilePath) {
//        boolean status = false;
//        try {
//            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
//
//            System.out.println("function ftpDownload :");
//            System.out.println(">srcFilePath : " + srcFilePath);
//            System.out.println(">desFilePath : " + desFilePath);
//            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
//            desFileStream.close();
//            System.out.println(">Status ftpDownload: " + status);
//
//
//
//            // this because there is no other check on the validity of the file
//            // if file doesn't exist it creates an empty file that needs to be deleted
//            if (!status)
//            {
//                //File dir = Environment.getExternalStorageDirectory();
//                File file = new File(desFilePath);
//                boolean deleted = file.delete();
//            }
//            else
//            {
//                Boolean isImage = srcFilePath.contains(".PNG");
//                if (!isImage){
//                    iCount +=1;
//                    progress = iCount;
//                    //publishProgress(progress);
//                }
//            }
//
//
//            return status;
//        } catch (Exception e) {
//            System.out.println("download failed");
//        }
//
//        return status;
//    }
//
//
//
//}