import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResumableFileDownloader {
	
	private static String fileURL = "http://vuclip.com";
    private static String saveDir = "C:/Users/AGOEL6/Downloads";
    private static final int BUFFER_SIZE = 10240;

	public static void main(String[] args) throws IOException, InterruptedException  {
        try {
        	URL url = verify(fileURL);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Range", "bytes=0-1024");
            httpCon.connect();
           
            int responseCode = httpCon.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
            	 System.out.println(downloadFile(httpCon));
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpCon.disconnect();
	   }
        catch(Exception e){
		e.printStackTrace();
	}
}

	public static String downloadFile(HttpURLConnection httpConn)
			throws IOException, FileNotFoundException, InterruptedException {
		String fileName = "";
		String disposition = httpConn.getHeaderField("Content-Disposition");
		
		fileName = extractFileName(disposition);
		
		String contentType = httpConn.getContentType();
		int contentLength = httpConn.getContentLength();

		InputStream inputStream = httpConn.getInputStream();
		String saveFilePath = saveDir + File.separator + fileName;
		File file = new File(saveFilePath);
		FileOutputStream outputStream;
		
		if(file.exists()) {
			outputStream = new FileOutputStream(file, true); //makes the stream append if the file exists
		} else {
			outputStream = new FileOutputStream(file); //creates a new file.
		}
 
		inputStream.skip(file.length());
		
		System.out.println("Content-Type = " + contentType);
		System.out.println("Content-Disposition = " + disposition);
		System.out.println("Content-Length = " + contentLength);
		System.out.println("fileName = " + fileName);
		System.out.println("Start Downloading...");
		System.out.write("\r".getBytes());
		int percentage =10;
		while(percentage <= 100) {
		    String temp =generateStars(percentage);
		    System.out.write(temp.getBytes());
		  //  System.out.print("\b\b\b");
		    percentage = percentage+10;
		    Thread.sleep(500);
		}

		
		int bytesRead = -1;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
		    outputStream.write(buffer, 0, bytesRead);
		}
    
		outputStream.close();
		inputStream.close();
    
		System.out.println("\n File downloaded");
		
		return "Download Completed";
	}

	public static URL verify(String fileURL) {
		if(!fileURL.toLowerCase().startsWith("http://")) {
	        return null;
	    }
	    URL verifyUrl = null;
	
	    try{
	        verifyUrl = new URL(fileURL);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return verifyUrl;
    }
	
	public static String generateStars(int percentage){
	    int startsNum = percentage / 4;
	    StringBuilder builder = new StringBuilder();
	    while(startsNum >= 0)
	    {
	    builder.append("*");
	    startsNum--;
	    }
	    builder.append(percentage+"%");
	    return builder.toString();
	}
	
	private static String extractFileName(String disposition) {
		
		String fileName = "";
		 if (disposition != null) {
             int index = disposition.indexOf("filename=");
             if (index > 0) {
            	// extract file name from header
                 fileName = disposition.substring(index + 10,disposition.length() - 1);
             }
         } else {
             // extract file name from URL
             fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
         }
		return fileName;
	}
}