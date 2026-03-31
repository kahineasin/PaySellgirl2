package org.sellgirl.sellgirlPayWeb.controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.TestModel001;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sellgirlPayWeb.configuration.PFConfigMapper;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.product.model.ResourceType;
//import com.sellgirl.sellgirlPayWeb.product.model.resourceChapCreate;
import com.sellgirl.sellgirlPayWeb.product.model.resourceCreate;
import com.sellgirl.sellgirlPayWeb.product.service.ResourceService;
import com.sellgirl.sellgirlPayWeb.projHelper.ProjHelper;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sgHelperExport.SGExcelHelper;
import com.sellgirl.sgJavaMvcHelper.HtmlHelper;
import com.sellgirl.sgJavaMvcHelper.PFGrid;

import junit.framework.TestCase;

import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGLine;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGMySqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecuteBase;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.sql.SqlUpdateItem;
import com.sellgirl.sgJavaHelper.time.SGWaiter;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

@SuppressWarnings("unused")
public class UncheckImportResource extends TestCase {
	public static void initPFHelper() {
		//PFDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		SGDataHelper.SetConfigMapper(new PFConfigMapper());		
		new SGDataHelper(new PFAppConfig());
	}

	private boolean clear=true;
	private boolean printBug=true;
	private boolean printProgress=true;
	private int maxLen=45110;//暂时的阀值
	
	/**
	 * 从爬取资源导入mysql
	 */
	public void testImportResource() {
		  SGSpeedCounter speed=null;
		  SGWaiter waiter=null;
		  int total=0;
		
		int err=0;
		StringBuilder deny=new StringBuilder(); 
//		initresource();
		try {
//			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderProdJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();
//			ISGJdbc lrJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();

//			String resourcePath="D:\\cache\\html1\\resource_data\\提取示例";
//			String resourcePath="D:\\cache\\html1\\1";
//			String resourcePath="D:\\cache\\html1\\电子书资源成品\\电子书资源成品\\电子书资源成品\\电子书资源成品";
			//String resourcePath="D:\\cache\\html1\\resource_data\\bugData";
			
//			String resourcePath="D:\\cache\\html1\\20260324mix2\\1-2500预览图\\1-2500预览图";
//			String resourcePath="D:\\cache\\html1\\20260324mix2\\漫画\\漫画预览图";
			String resourcePath="D:\\cache\\html1\\20260324mix2\\图片\\P预览图";
			
//			String outImgPath="D:\\cache\\html1\\resourceImg\\cover";
//			String outImgPath2="D:\\cache\\html1\\resourceImg\\content";
			ResourceType resourceType=ResourceType.image;
			String outImgPath="D:\\cache\\html1\\resourceImg\\"+resourceType;
			//String outImgPath2="D:\\cache\\html1\\resourceImg\\content";

//			String excelPath="D:\\cache\\html1\\20260324mix2\\视频信息(1).csv";
			String excelPath="D:\\cache\\html1\\20260324mix2\\"+resourceType+".xlsx";
			
			Workbook wb1 = SGExcelHelper.create(new FileInputStream(new File(excelPath)));

//			ResourceType resourceType=ResourceType.movie;
			
			SGDirectory.EnsureExists(outImgPath);
			//SGDirectory.EnsureExists(outImgPath2);
			File root=new File(resourcePath);
	        File[] files = new File(resourcePath).listFiles();
	        SGSqlInsertCollection insert=null;
	        SGSqlInsertCollection insert2=null;

        	ArrayList<File> covers=new ArrayList<File>();
			try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc)) {
				dstExec.AutoCloseConn(false);

				ResourceService service=new ResourceService();
//				service.setResourceType(resourceType);
				
				if(clear) {
					dstExec.TruncateTable(service.getTableName(resourceType));
					//dstExec.TruncateTable("sg_resource_chap");
				}


				  if(printProgress) {
					  speed=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
					  total=files.length;
					  waiter=new SGWaiter(2000);
				  }

				List<Map<String, Object>> list1=SGExcelHelper.ExcelToDictList(wb1);
				int idx=-1;
//		        for(File i:files) {
			    for(Map<String,Object> row:list1) {
		        	idx++;
//		        	String title=SGDataHelper.ReadFileToString(Paths.get(i.getAbsolutePath(), "title.txt").toString());
//		        	Map<String,Object> row=list1.get(idx);
		        	String title=SGDataHelper.ObjectToString(row.get("文件名")) ;
//		        	String autor=SGDataHelper.ReadFileToString(Paths.get(i.getAbsolutePath(), "autor.txt").toString());
		        	String autor=this.getAuthor(title);
//		        	String autor=SGDataHelper.ObjectToString(row.get("链接")) ;
//		        	String autor=SGDataHelper.ObjectToString(row.get("链接")) ;
//		        	String autor=SGDataHelper.ObjectToString(row.get("链接")) ;
		        	
//		        	List<String> chap=SGDataHelper.ReadFileToLines(Paths.get(i.getAbsolutePath(), "contents.txt").toString());
//					File coverImg=new File(Paths.get(i.getAbsolutePath(), "cover.jpg").toUri());
//		        	
//		        	if(SGDataHelper.StringIsNullOrWhiteSpace(title)) {
//		        		if(printBug) {
//		        		System.out.println("resource title none, folder:"+i.getName());
//		        		}
//		        		deny.append(i.getName()+"\r\n");
//		        		continue;
//		        	}
		        	
		        	int j=0;
		        	String tmpCover="";
		        	covers.clear();
		        	boolean hasAnyCover=false;
		        	
		        	//如果图片名连续固定
//		        	while(true) {
//		        		j++;
////			        	File coverI=new File(Paths.get(resourcePath,title,j+".jpg").toUri());
//			        	File coverI=new File(Paths.get(resourcePath,title,title+"-"+j+".jpg").toUri());
//		        		if(coverI.exists()) {
//		        			if(!hasAnyCover) {hasAnyCover=true;}
//		        			covers.add(coverI);
//		        			tmpCover+=j+".jpg"+",";
////		        			tmpCover+="resourceImg/cover/"+coverI.getName();
////							SGPath.copyFile(
////									coverI,
////			        			new File(Paths.get(outImgPath, resourceId+".jpg").toUri())
////			        			);
//		        		}else {
//		        			break;
//		        		}
//		        	}
		        	

		        	//如果图片名不连续
		        	j=0;
		        	File coverFolder=new File(Paths.get(resourcePath,title).toUri());
		        	for(File coverI:coverFolder.listFiles()) {
		        		j++;
//			        	File coverI=new File(Paths.get(resourcePath,title,j+".jpg").toUri());
//			        	File coverI=new File(Paths.get(resourcePath,title,title+"-"+j+".jpg").toUri());
		        		if(coverI.exists()) {
		        			if(!hasAnyCover) {hasAnyCover=true;}
		        			covers.add(coverI);
		        			tmpCover+=j+".jpg"+",";
//		        			tmpCover+="resourceImg/cover/"+coverI.getName();
//							SGPath.copyFile(
//									coverI,
//			        			new File(Paths.get(outImgPath, resourceId+".jpg").toUri())
//			        			);
		        		}else {
		        			break;
		        		}
		        	}
		        	if(hasAnyCover) {
		        		tmpCover=tmpCover.substring(0,tmpCover.length()-1);
		        	}else {
//		        		int aa=1;
		        		if(printBug) {
		        		System.out.println("resource cover error, "+resourceType+":"+title);
		        		}
		        	}

//					String c=new String(new char[] {SGDataHelper.getFirstLetter(title.charAt(0))});
					String c=ProjHelper.getFirstLetter(title);
					
					resourceCreate model=new resourceCreate();
					model.setResource_name(title);
					model.setResource_author(autor);
//					model.setLetter(c);
//					model.setCover(coverImg.exists()?"id":"");
					model.setCover(tmpCover);
					model.setNetdisk(SGDataHelper.ObjectToString(row.get("链接")));
					model.setExtractCode(SGDataHelper.ObjectToString(row.get("提取码")));
					model.setUnlockPassword(SGDataHelper.ObjectToString(row.get("解压密码")));
					if(ResourceType.image==resourceType) {
//						model.setResource_name(SGDataHelper.ObjectToString(row.get("标题")));
						model.setDuration(SGDataHelper.ObjectToInt0(row.get("图片数量")));
						Double size=SGDataHelper.ObjectToDouble0(row.get("总大小(字节)"));
						model.setSize(SGDataHelper.ObjectToInt0(size/1024));
					}else if(ResourceType.comic==resourceType) {
						model.setResource_name(SGDataHelper.ObjectToString(row.get("标题")));
						model.setDuration(SGDataHelper.ObjectToInt0(row.get("图片数量")));
						Double size=SGDataHelper.ObjectToDouble0(row.get("总大小(字节)"));
						model.setSize(SGDataHelper.ObjectToInt0(size/1024));
					}else {
						model.setDuration(SGDataHelper.ObjectToInt0(row.get("时长(秒)")));
						Double size=SGDataHelper.ObjectToDouble0(row.get("大小(字节)"));
						model.setSize(SGDataHelper.ObjectToInt0(size/1024));
					}
					model.setCreate_date(SGDate.Now());
					if(null==insert) {
						insert=dstExec.getInsertCollection();					
						insert.InitItemByModel(model);
					}else {
						insert.UpdateModelValueAutoConvert(model);
					}

					SGSqlCommandString sql=new SGSqlCommandString(
							SGDataHelper.FormatString(
									"insert into {2} ({0}) values ({1})",
									insert.ToKeysSql(),
									insert.ToValuesSql(),
									service.getTableName(resourceType)
									)
							);
					int r=dstExec.ExecuteSqlInt(sql, null, false);
					if(1>r) {
						err++;
		        		continue;
					}
//					System.out.println("id:"+r);
//					System.out.println("id2:"+dstExec.GetLastInsertedId());
					long resourceId=dstExec.GetLastInsertedId();
					if(hasAnyCover) {
						j=0;
						SGDirectory.EnsureExists(Paths.get(outImgPath, ""+resourceId).toString());
						for(File k:covers) {
							j++;
							SGPath.copyFile(
								k,
		        			new File(Paths.get(outImgPath, ""+resourceId,""+j+".jpg").toUri())
		        			);
						}
					}


//					//封面，如果路径是用id,那么cover字段留空可以了，否则要反写id到cover,更麻烦
//					if(coverImg.exists()) {
//						SGPath.copyFile(
//		        			coverImg,
//		        			new File(Paths.get(outImgPath, resourceId+".jpg").toUri())
//		        			);
//					}
					



			        
			        if(printProgress&&waiter.isOK()) {
					System.out.println(speed.getEnSpeed(total,com.sellgirl.sgJavaHelper.SGDate.Now()));
			        }
		        }
				
				
				dstExec.close();
			} catch (Exception e) {
				e.printStackTrace();
				err++;
			}
			System.out.println("total err: "+err);
			if(0<deny.length()) {System.out.println("deny: "+deny);}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(printProgress) {
			System.out.println(speed.getEnSpeed(total,com.sellgirl.sgJavaHelper.SGDate.Now()));
		}
	}
	
	public void testGenerateSmallImg() {
		int size=80;
		ResourceType resourceType=ResourceType.movie;
		String srcImgPath="D:\\cache\\html1\\resourceImg\\"+resourceType;
		String dstImgPath="D:\\cache\\html1\\resourceImg\\"+resourceType+size;
		SGDirectory.eachFile(
			new File(srcImgPath), 
			dstImgPath, File.separatorChar, 
			(file,path,obj)->{
				try {
					//SGPath.copyFile(file, new File(path));
////					PFLine.FitHeightAndCenterHorizontally()
//					//SGDataHelper.backgroundImg(null, null, null, null, null, null)
					
					//方法1. 共26秒
//					int backWidth = 60; // 1920;
//					int backHeight = 60;// 1080;
//					Image image = ImageIO.read(file);
//					//
//					String tmpImgPath = SGDataHelper.backgroundImg(
//							new Dimension(backWidth, backHeight),
//							image,
//							null,
//							new PFLine(new PFPoint(0, 0), new PFPoint(100, 100)).IsPercent(), 
//							Color.RED,
//							false);
//					File file2 = new File(tmpImgPath);
////					FileInputStream inputStream = new FileInputStream(file2);
////					byte[] bytes = new byte[inputStream.available()];
////					inputStream.read(bytes, 0, inputStream.available());
////					inputStream.close();
//					SGPath.copyFile(file2, new File(path));
//					file2.delete();

					//方法2. 20秒 (推荐)
					int backW = size; // 1920;
					int backH = size;// 1080;
					SGLine imgLine=SGLine.FitHeightAndCenterHorizontally();
					SGLine backLine=new SGLine(new PFPoint(0, 0), new PFPoint(backW, backH));
					
					SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
					SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
					BufferedImage paintBi = null;
					Image image = ImageIO.read(file);
					paintBi = SGDataHelper.backgroundImgInBuffer(
							canvasRef, ctx1Ref, null, 
							new Dimension(backW, backH), image, null,
							backLine,imgLine, 
							//Color.RED
							null
							, false);
					File file2 = new File(path);
//					ImageIO.write(paintBi, "jpg", file2);
					ImageIO.write(paintBi, SGPath.getFileExtension(path), file2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}, 
			(filder,path,obj)->{
				SGDirectory.EnsureExists(path);
			}
		);
	}
	/**
	 * ok
	 * excel的netdisk和extradeCode列有更新
	 * @throws Exception 
	 */
	public void testUpdateResource() throws Exception {

//		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();//homePad
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();//ubuntu
		ResourceType resourceType=ResourceType.movie;
		String excelPath="D:\\cache\\html1\\20260324mix2\\最新表格整合\\"+resourceType+".xlsx";
		
		SGSpeedCounter[] speed=new SGSpeedCounter[]{null};
		int[] startCnt=new int[]{0};
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(50000));

			ResourceService service=new ResourceService();			
			Workbook wb1 = SGExcelHelper.create(new FileInputStream(new File(excelPath)));
			List<Map<String, Object>> list1=SGExcelHelper.ExcelToDictList(wb1);
			
			int[] idx= new int[]{0};
			int insertCnt=list1.size();//1000000;
			
			//comic名字可能重复,文件名没导入数据库,刚好顺序一样,用id吧,需要先核对excel顺序
			boolean b =
					myResource.doUpdateList(
							Arrays.asList(new String[]{
									"resource_id"
//									"resource_name"
									,
									"netdisk","extract_code"}),
							service.getTableName(resourceType),
							new String[]{
									"resource_id"
//									"resource_name"									
							},
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String, Object> row=list1.get(idx[0]);
								Map<String,Object> map=new HashMap<>();
//								map.put("resource_name", row.get("文件名"));
								map.put("resource_id", idx[0]+1);
								map.put("netdisk", row.get("链接"));
								map.put("extract_code", row.get("提取码"));
								idx[0]++;
								return map;
							},
							null,
							a -> {
								// 测试速度
								if(null==speed[0]){
									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
									startCnt[0]=a;
								}
								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Thread.sleep(2000);//写日志的时间
		
//		//-------------------------
//		  SGSpeedCounter speed=null;
//		  SGWaiter waiter=null;
//		  int total=0;
//		
//		int err=0;
//		StringBuilder deny=new StringBuilder(); 
////		initresource();
//		try {
////			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderProdJdbc();
//			ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();
////			ISGJdbc lrJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
//
////			String resourcePath="D:\\cache\\html1\\resource_data\\提取示例";
////			String resourcePath="D:\\cache\\html1\\1";
////			String resourcePath="D:\\cache\\html1\\电子书资源成品\\电子书资源成品\\电子书资源成品\\电子书资源成品";
//			//String resourcePath="D:\\cache\\html1\\resource_data\\bugData";
//			
////			String resourcePath="D:\\cache\\html1\\20260324mix2\\1-2500预览图\\1-2500预览图";
////			String resourcePath="D:\\cache\\html1\\20260324mix2\\漫画\\漫画预览图";
//			String resourcePath="D:\\cache\\html1\\20260324mix2\\图片\\P预览图";
//			
////			String outImgPath="D:\\cache\\html1\\resourceImg\\cover";
////			String outImgPath2="D:\\cache\\html1\\resourceImg\\content";
//			ResourceType resourceType=ResourceType.image;
//			String outImgPath="D:\\cache\\html1\\resourceImg\\"+resourceType;
//			//String outImgPath2="D:\\cache\\html1\\resourceImg\\content";
//
////			String excelPath="D:\\cache\\html1\\20260324mix2\\视频信息(1).csv";
//			String excelPath="D:\\cache\\html1\\20260324mix2\\"+resourceType+".xlsx";
//			
//			Workbook wb1 = SGExcelHelper.create(new FileInputStream(new File(excelPath)));
//
////			ResourceType resourceType=ResourceType.movie;
//			
//			SGDirectory.EnsureExists(outImgPath);
//			//SGDirectory.EnsureExists(outImgPath2);
//			File root=new File(resourcePath);
//	        File[] files = new File(resourcePath).listFiles();
//	        SGSqlInsertCollection insert=null;
//	        SGSqlInsertCollection insert2=null;
//
//	        //ArrayList<File> covers=new ArrayList<File>();
//			try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc)) {
//				dstExec.AutoCloseConn(false);
//
//				ResourceService service=new ResourceService();
////				service.setResourceType(resourceType);
//				
//				dstExec.HugeUpdateReader(null, null, excelPath, null, null, null)
//				
//				dstExec.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//				err++;
//			}
//			System.out.println("total err: "+err);
//			if(0<deny.length()) {System.out.println("deny: "+deny);}
//
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(printProgress) {
//			System.out.println(speed.getEnSpeed(total,com.sellgirl.sgJavaHelper.SGDate.Now()));
//		}
	}
    String[] uploaderNames = new String[] {
                           "深海里的鱼", "云中漫步", "星空下的猫", "风一样的男子", "雨夜带刀",
                           "书虫小七", "影视收藏家", "资源搬运工", "分享快乐", "逍遥客",
                           "追风少年", "静听花开", "月下独酌", "雪落无声", "飞鸟与鱼",
                           "青衫故人", "白衣卿相", "梦里不知身是客", "此间少年", "南风知我意",
                           "北冥有鱼", "东篱把酒", "西窗烛", "南山南", "北海北",
                           "江湖故人", "红尘客栈", "天涯过客", "孤舟蓑笠翁", "独钓寒江雪"
    };
	private String getAuthor(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) - hash) + str.charAt(i);
            hash |= 0;
        }
        int idx= Math.abs(hash) % uploaderNames.length;
		return uploaderNames[idx];
	}
	/**
	 * 按书名删除
	 */
	public void testDeleteByNames() {
//		String[] names=new String[] {"二马","今日简史","人间失格","人间烟火"};
		String[] names=new String[] {"复活"};
//		int[] ids=new int[names.length];
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();
		int total=0;
		try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc)) {
			dstExec.AutoCloseConn(false);
			int idx=0;
			for(String i:names) {
	
				SGSqlCommandString sql=new SGSqlCommandString(
						SGDataHelper.FormatString(
								"delete from sg_resource where resource_id={0}",
								i)
						);
				Long r=SGDataHelper.ObjectToLong(dstExec.QuerySingleValue("select resource_id from sg_resource where resource_name='"+i+"' limit 1"));
				if(null!=r) {
					doDeleteByIds(new long[] {r});
				}
//				ids[idx]=r;
				idx++;
			}
			dstExec.close();
//			doDeleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据id删除主表子表（常用于重新导入 有报错的异常数据）
	 */
	public void testDeleteByIds() {
		long[] ids=new long[] {188,224,232,239};
		doDeleteByIds(ids);
	}
	public void doDeleteByIds(long[] ids) {
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();
		int total=0;
		try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc)) {
			dstExec.AutoCloseConn(false);
			for(long i:ids) {
	
				SGSqlCommandString sql=new SGSqlCommandString(
						SGDataHelper.FormatString(
								"delete from sg_resource where resource_id={0}",
								i)
						);
				int r=dstExec.ExecuteSqlInt(sql, null, false);
				total+=r;
				SGSqlCommandString sql2=new SGSqlCommandString(
						SGDataHelper.FormatString(
								"delete from sg_resource_chap where resource_id={0}",
								i)
						);
				int r2=dstExec.ExecuteSqlInt(sql2, null, false);
				total+=r2;
			}
			dstExec.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(printProgress) {
			System.out.println("effected rows:"+total);
		}
	}
	


	public void testCreateShopTable() {

		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();
		try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc)) {
			dstExec.AutoCloseConn(false);
			boolean b=dstExec.OpenConn();
			System.out.println("conn:"+b);
			if(b) {
				String text=SGDataHelper.ReadFileToString2(new File("D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\sql\\sgshop.sql"));
				SGSqlCommandString sql=new SGSqlCommandString(text.split(";"));
				int r=dstExec.ExecuteSqlInt(sql, null, false);
				
				System.out.println("create table:"+r);
			}
			
			dstExec.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//ok
	public void testBulkCopy() {
		ResourceType resourceType=ResourceType.comic;
		ResourceService service=new ResourceService();
//		service.setResourceType(resourceType);
		  final SGWaiter waiter=new SGWaiter(2000);

		initPFHelper();
		// bulk到ClickHouse
		ISGJdbc srcJdbc = JdbcHelperTest.GetSgShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();
		ISqlExecute srcExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dstTableName=service.getTableName(resourceType);
		// 使用NString前
		ResultSet srcDr = srcExec.GetHugeDataReader("select * from "+dstTableName);
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

//		//dstExec.Delete("test1", null);
//		dstExec.HugeDelete(dstTableName, where -> {
////  		where.Add("data_date",PFDate.Now().ToCalendar());
//		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
//		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
//	});
		//dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null, null);//这样可以，但50000一批时会卡住
		dstExec.HugeBulkReader(null, srcDr,dstTableName, null, 
//		null,null		
		a->{
			if(waiter.isOK()) {
				System.out.println(SGDate.Now().toString()+"----"+a);
			}
		}, a->{
			boolean b=false;
			return b;
		}
		);

	}

	public void testBulkChap() {
		
		initPFHelper();
		
		ISGJdbc srcJdbc = JdbcHelperTest.GetSgShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();
		ISqlExecute srcExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dstTableName="sg_resource_chap";
		// 使用NString前
		ResultSet srcDr = srcExec.GetHugeDataReader("select * from sg_resource_chap");
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

//		//dstExec.Delete("test1", null);
//		dstExec.HugeDelete(dstTableName, where -> {
////  		where.Add("data_date",PFDate.Now().ToCalendar());
//		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
//		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
//	});
		//dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null, null);//这样可以，但50000一批时会卡住
		dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

	}
    public void testUpdatePs() throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
        TimeZone.setDefault(timeZone);//怀疑这个时区会影响jdbc的初始化,事实证明不会

		//IPFJdbc dstJdbc=JdbcHelperTest.GetMySqlTest2Jdbc();
		//IPFJdbc dstJdbc=JdbcHelperTest.GetDayJdbc();
		ISGJdbc dstJdbc=JdbcHelperTest.GetSgShopJdbc();
        ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		//ISqlExecute dstExec = PFSqlExecute.Init(JdbcHelperTest.GetDayJdbc());
		//ISqlExecute dstExec = PFSqlExecute.Init(JdbcHelperTest.GetDayJdbc2());
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//怀疑这个时区会影响jdbc的初始化
        List<String> fieldNames=new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("col1");
        fieldNames.add("col2");
        fieldNames.add("col3");
//        ResultSetMetaData dstMd = dstExec.GetMetaData("test_tb_07", fieldNames);
		ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose("test_tb_07", fieldNames);
        SGSqlInsertCollection insert=dstExec.getInsertCollection(dstMd);
        insert.Set("id",8);
        insert.Set("col1",1662717600000L);
        insert.Set("col2",1662717600000L);
        insert.Set("col3",1662717600000L);
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//ok
        PreparedStatement ps = dstExec.GetPs("test_tb_07", fieldNames, true);//事实证明,获得ps时的时区,会影响后面插入时间的时区
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        for (int i = 0; i < dstMd.getColumnCount(); i++) {
            int mdIdx = i + 1;
            String colName = dstMd.getColumnLabel(mdIdx);
            SqlUpdateItem dstInsertI = insert.get(colName);
            dstExec.updatePs(ps, dstMd, mdIdx, dstInsertI);
        }
        ps.addBatch();
        ps.executeBatch();
        ps.clearBatch();
//        LOGGER.info("插入完成");
    }
//	public boolean addResource(resourceCreate model//,SGRef<String> error
//			) {
//		ISGJdbc dstJdbc=JdbcHelperTest.GetSgShopJdbc();
//		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//			myResource.getInsertCollection()
////			myResource.GetConn().setAutoCommit(false);
////			myResource.SetInsertOption(a->a.setProcessBatch(50000));
////			myResource.GetLastInsertedId()
//			int insertCnt=1;
//			int[] idx= new int[]{0};
//			boolean b =
//					myResource.doInsertList(
//							Arrays.asList(new String[]{
//									"user_name",
//									"pwd",
//									//"email",
//									"invitation_code","create_date"}),
//							"sg_user",
//							(a, b2, c) -> a < insertCnt,
//							(a) -> {
//								Map<String,Object> map=new HashMap<>();
//								map.put("user_name", model.getUserName());
//								map.put("pwd", model.getPwd());
////								map.put("email", "");
//								map.put("invitation_code", model.getInvitationCode());
//								map.put("create_date", SGDate.Now());
//								idx[0]++;
//								return map;
//							},
//							null,
//							a -> {
////								// 测试速度
////								if(null==speed[0]){
////									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
////									startCnt[0]=a;
////								}
////								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
////								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
//							},
//							null);
//			String aa=myResource.GetErrorFullMessage();
//			error.SetValue(aa);
//			return b;
//		} catch (Throwable e) {
//			error.SetValue(e.toString());
//			return false;
//			//throw new RuntimeException(e);
//		}
//	}
	public  void testInitResource() {		

		ISGJdbc jdbc=JdbcHelperTest.GetSgShopJdbc();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
			
			resourceCreate model=new resourceCreate();
			model.setResource_name("aa6");
			model.setResource_author("aa");
			model.setCover("aa");
			model.setCreate_date(SGDate.Now());
			
			SGSqlInsertCollection insert=dstExec.getInsertCollection();
			
			insert.InitItemByModel(model);
			
			SGSqlCommandString sql=new SGSqlCommandString(
					SGDataHelper.FormatString(
							"insert into sg_resource ({0}) values ({1})",
							insert.ToKeysSql(),
							insert.ToValuesSql())
					);
			int r=dstExec.ExecuteSqlInt(sql, null, true);
			System.out.println("id:"+r);
			System.out.println("id2:"+dstExec.GetLastInsertedId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class ChapFileFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith("text");
		}
		
	}
	public static class ContentImgFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jpg");
		}
		
	}
}
