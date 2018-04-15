import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ResumableFileDownloaderTest {
	//ResumableFileDownloader resumableFileDownloader = new ResumableFileDownloader();
    String url = "";
    HttpURLConnection httpCon;
    
    @Before
    public void setUp() throws Exception {
        url = "http://vuclip.com";
        httpCon = (HttpURLConnection) new URL(url).openConnection();
    }

	@Test
	public void assertNull_whenInvalidURL(){
		URL url = ResumableFileDownloader.verify("ftp://vuclip.com");
		Assert.assertNull(url);
	}
	
	@Test
	public void testResponseCode() throws MalformedURLException, IOException{	
		Assert.assertEquals(httpCon.getResponseCode(), HttpURLConnection.HTTP_OK);
	}
	
	@Test
	public void testDownloadFile() throws FileNotFoundException, IOException, InterruptedException{
		Assert.assertNotNull(ResumableFileDownloader.downloadFile(httpCon));
	}
}
