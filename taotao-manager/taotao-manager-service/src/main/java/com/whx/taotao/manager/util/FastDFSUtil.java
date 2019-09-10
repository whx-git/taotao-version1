package com.whx.taotao.manager.util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FastDFSUtil {

	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private StorageClient1 storageClient = null;
	private String conf;

	public FastDFSUtil(String conf) throws Exception {

		if (conf.contains("classpath:")) {
			this.conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
		}
		ClientGlobal.init(this.conf);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileName 文件全路径
	 * @param extName 文件扩展名，不包含（.）
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result =null;
		try {
			result= storageClient.upload_file1(fileName, extName, metas);
		}catch (Exception e){
			ClientGlobal.init(this.conf);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageServer = null;
			storageClient = new StorageClient1(trackerServer, storageServer);
			result= storageClient.upload_file1(fileName, extName, metas);
		}

		return result;
	}
	
	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}
	
	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileContent 文件的内容，字节数组
	 * @param extName 文件扩展名
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {

		String result =null;
		try {
			result= storageClient.upload_file1(fileContent, extName, metas);
		}catch (Exception e){
			ClientGlobal.init(this.conf);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageServer = null;
			storageClient = new StorageClient1(trackerServer, storageServer);
			result= storageClient.upload_file1(fileContent, extName, metas);
		}
		return result;
	}
	
	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}


//	//客户端配置文件
//	public  static String conf_filename = "classpath:fdfs_client.properties";
//	//本地文件，要上传的文件
//	public static String local_filename = "/Users/shijian/Downloads/test.jpeg";
//	public static void main(String[] args) {
//		try {
//			FastDFSUtil fastDFSClient=new FastDFSUtil(conf_filename);
//			String path = fastDFSClient.uploadFile(local_filename, null,
//					null);
//			System.out.println("path = " + path);
//
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
