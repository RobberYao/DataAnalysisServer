package com.fleety.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipTools {

	public static final String ZIP_FILENAME = ""; // 需要解压缩的文件名
	public static final String ZIP_DIR = ""; // 需要压缩的文件夹
	public static final String UN_ZIP_DIR = ""; // 要解压的文件目录
	public static final int BUFFER = 1024; // 缓存大小

	public static void zipFile(String baseDir, String fileName)
			throws Exception {
		List fileList=getSubFiles(new File(baseDir));  
        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(fileName));  
        ZipEntry ze=null;  
        byte[] buf=new byte[BUFFER];  
        int readLen=0;  
        for(int i = 0; i <fileList.size(); i++) {  
            File f=(File)fileList.get(i);  
            ze=new ZipEntry(getAbsFileName(baseDir, f));  
            ze.setSize(f.length());  
            ze.setTime(f.lastModified());     
            zos.putNextEntry(ze);  
            InputStream is=new BufferedInputStream(new FileInputStream(f));  
            while ((readLen=is.read(buf, 0, BUFFER))!=-1) {  
                zos.write(buf, 0, readLen);  
            }  
            zos.setEncoding("GBK");
            is.close();  
        }  
        zos.close();  
	}

	private static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else
				ret = real.getName() + "/" + ret;
		}
		return ret;
	}

	private static List getSubFiles(File baseDir) {
		List ret = new ArrayList();
		File[] tmp = baseDir.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile())
				ret.add(tmp[i]);
			if (tmp[i].isDirectory())
				ret.addAll(getSubFiles(tmp[i]));
		}
		return ret;
	}

	public static void deleteDirFile(String path) {
		File file = new File(path);
		if (file.isDirectory()) { // 如果是目录，先递归删除
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				deleteDirFile(path + "/" + list[i]); // 先删除目录下的文件
			}
		}
		file.delete();
	}

	public static String newFolder(String dir) {
		java.io.File myFilePath = new java.io.File(dir);
		if (myFilePath.isDirectory()) {
		} else {
			myFilePath.mkdirs();
		}
		return dir;
	}

	public static String getFileNames(String path) {

		File file = new File(path); // get file list where the path has
		File[] array = file.listFiles(); // 获得文件列表
		String pdfNames = "";

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				if (array[i].getName().endsWith(".pdf")) { // 获得pdf文件名称
					pdfNames += array[i].getName().substring(0,
							array[i].getName().length() - 4)
							+ ",";
				}
			}
		}
		if (pdfNames.length() > 0) {
			pdfNames.substring(0, pdfNames.length() - 1);
		}
		return pdfNames;
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("copy file error!");
			e.printStackTrace();
		}
	}

	public static boolean fileExist(String fileNames, String pdfName) {
		boolean flag = false;
		if (!"".equals(fileNames)) {
			String[] nameArr = fileNames.split(",");
			for (int i = 0; i < nameArr.length; i++) {
				if (pdfName.equals(nameArr[i])) { // 如果文件名相同则执行拷贝操作(拷贝到zip目录准备打包)
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public static void deleteFileAndDir(String path) {
		File file = new File(path);
		File[] array = file.listFiles();
		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				array[i].delete();
			} else if (array[i].isDirectory()) {
				deleteDirFile(array[i].getPath());
			}
		}
	}

	public static void main(String args[]){
		try {
//			ZipTools.zipFile("D:\\jason3", "D:\\jason3\\test.zip");
			ZipTools.deleteDirFile("D:\\jason3\\all");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
