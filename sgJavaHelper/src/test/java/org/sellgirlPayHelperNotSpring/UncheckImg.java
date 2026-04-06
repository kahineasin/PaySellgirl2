package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sellgirl.sgJavaHelper.AuthenticatorGenerator;
import com.sellgirl.sgJavaHelper.HostType;
import com.sellgirl.sgJavaHelper.PFEmailListener;
import com.sellgirl.sgJavaHelper.PFEmailManager;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.SGLine;
import com.sellgirl.sgJavaHelper.PFListenNewEmailTask;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckImg extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckImg.class);

//	public static void initPFHelper() {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//		new PFDataHelper(new PFAppConfig());
//	}
//	private String sashaImgPath="D:\\cache\\html1\\cliTestData\\20260406\\img\\C-No.00495\\1.jpg";
	public void testBackgroundImg() {
		try {
//			String srcImgPath="D:\\cache\\html1\\cliTestData\\20260406\\img\\C-No.00495\\1.jpg";
			String srcImgPath="D:\\cache\\html1\\cliTestData\\20260406\\img\\C-No.00495\\1.jpg";
			String dstImgPath="D:\\cache\\html1\\cliTestData\\20260406\\out\\10.webp";
//			int backWidth = 3840; // 1920;
//			int backHeight = 2160;// 1080;

			File infile = new File(srcImgPath);
			SGDirectory.EnsureFilePath(dstImgPath);
//			Image image = ImageIO.read(infile);
//		int backWidth = w;
//		int backHeight = h;

			int backW = 200; // 1920;
			int backH = 200;// 1080;
			SGLine imgLine=SGLine.FitHeightAndCenterHorizontally();
			SGLine backLine=new SGLine(new PFPoint(0, 0), new PFPoint(backW, backH));

			SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
			SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
			BufferedImage paintBi = null;
			BufferedImage image = ImageIO.read(infile);
			paintBi = SGDataHelper.backgroundImgInBuffer(
					canvasRef, ctx1Ref, null, 
					new Dimension(backW, backH), image, null,
					backLine,imgLine, 
					Color.RED, false);

			File outfile = new File(dstImgPath);
			ImageIO.write(paintBi, SGPath.getFileExtension(dstImgPath), outfile);
			
//			System.out.println(tmpImgPath);
//			File file = new File(tmpImgPath);
//			FileInputStream inputStream = new FileInputStream(file);
//			byte[] bytes = new byte[inputStream.available()];
//			inputStream.read(bytes, 0, inputStream.available());
//			inputStream.close();
			// file.delete();
		} catch (Exception e) {
		}
		return;
	}
}
