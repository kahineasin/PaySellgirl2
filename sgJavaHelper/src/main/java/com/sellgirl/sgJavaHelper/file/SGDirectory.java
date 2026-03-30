package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGFunc;

/*
 * 代替C#里的Directory类，就相当于“文件夹”
 */
public class SGDirectory {
	public static Boolean Exists(String dirPath) {
		File file = new File(dirPath);
		return file.exists();
	}

	/**
	 * 确保目录存在.
	 * 
	 * @param dirPath
	 */
	public static void EnsureExists(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 确保文件的所在文件夹存在.
	 * 
	 * @param filePath
	 */
	public static void EnsureFilePath(String filePath) {
		// PFDirectory.EnsureExists(filePath);

		String pathWithoutFileName = filePath.replace(SGPath.GetFileName(filePath), "");// 如果目录不存在先生成目录,否则会报错--wxj20180713

		SGDirectory.EnsureExists(pathWithoutFileName);

	}

	public static void DeleteFile(String filePath) {
		if (SGDirectory.Exists(filePath)) {
			(new File(filePath)).delete();
		}
	}    
	public static void cleanDirectory(String directoryPath) {
//        String directoryPath = directoryField.getText();
        if (directoryPath.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "请先选择目录", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
//            JOptionPane.showMessageDialog(this, "选择的路径不是有效目录", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 确认对话框
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "确定要删除 " + directoryPath + " 目录下的所有文件吗？此操作不可恢复！",
//                "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
//        if (confirm != JOptionPane.YES_OPTION) {
//            return;
//        }
        
        // 执行删除操作
        try {
            int deletedCount = deleteAllFilesInDirectory(directory);
//            logArea.append("成功删除了 " + deletedCount + " 个文件\n");
//            JOptionPane.showMessageDialog(this, "删除完成，共删除 " + deletedCount + " 个文件");
        } catch (Exception e) {
//            logArea.append("删除过程中发生错误: " + e.getMessage() + "\n");
//            JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), 
//                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static int deleteAllFilesInDirectory(File directory) {
        int count = 0;
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归删除子目录
                    count += deleteAllFilesInDirectory(file);
                    // 删除空目录
                    if (file.delete()) {
//                        logArea.append("已删除目录: " + file.getAbsolutePath() + "\n");
                    } else {
//                        logArea.append("无法删除目录: " + file.getAbsolutePath() + "\n");
                    }
                } else {
                    // 删除文件
                    if (file.delete()) {
//                        logArea.append("已删除文件: " + file.getAbsolutePath() + "\n");
                        count++;
                    } else {
//                        logArea.append("无法删除文件: " + file.getAbsolutePath() + "\n");
                    }
                }
            }
        }
        
        return count;
    }

	/**
	 * 转绝对路径.
	 * 
	 * @param p
	 * @return
	 */
	public static String ToAbsolutePath(String p) {
		Path path = Paths.get(p).toAbsolutePath();
		return path.toString();

	}

	/**
	 * 把全路径修正为绝对路径的形式(为了避免后续使用时的不便) 如: 1. usr/local/app/app.jar ->
	 * /usr/local/app/app.jar 2. ./usr/local/app/app.jar -> /usr/local/app/app.jar
	 * (一般不考虑这种情况,因为参数p认为是全路径)
	 * 
	 * @param p
	 * @return
	 */
	public static String FullPathToAbsolutePath(String p) {

		String absolutePath = "";
		if ('/' == p.charAt(0)) {// 第1位是斜扛的话,是绝对路径,如 /xx/xx
			absolutePath = p;
		} else if (p.indexOf(":") > -1) {// 带盘符的也是绝对路径,如 D:\xx\xx
			absolutePath = p;
		} else if (p.indexOf("." + File.separatorChar) == 0) {// 当前目录的情况,如 ./ .\\ 之类的
			absolutePath = File.separatorChar + p.substring(2);
		} else {
			absolutePath = File.separatorChar + p;
		}
		;
		return absolutePath;
	}

	/**
	 * 其实用处不大，如果前面一步先从手机复制到电脑的话，全部文件的lastModified都是当天了
	 * @param sourceFile
	 * @param oldTime
	 * @throws IOException
	 */
	@Deprecated
	public static void deleteOldFile(File sourceFile,SGDate oldTime) throws IOException {
		int mb=9;
		long size=1024l*1024l*9;
		if(sourceFile.isFile()) {
			if(oldTime.compareTo(new SGDate(sourceFile.lastModified()))>0) {
				sourceFile.delete();
			}
		}else if(sourceFile.isDirectory()) {
			for(File f:sourceFile.listFiles()) {
				deleteOldFile(f,oldTime);
//				if(f.isFile()) {
////					SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);
//
//					if(size<f.length()) {
////						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);//这样保存不到扩展名
//						String fileName=f.getName();
////						int i=fileName.lastIndexOf(".");
//						fileName=fileName.replace('.','_');
//						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,fileName).toString(),mb);	
//					}else {
//						SGDirectory.EnsureExists(chunkFileFolder);
//						File dstFile = new File(Paths.get(chunkFileFolder,f.getName()).toString());
//						SGPath.copyFile(f, dstFile);
////						java.nio.file.Files.copy(null, null)
//					}
//				}else if(f.isDirectory()) {
//					SGFileSplit.doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString());
//				}
			}
		}
	}
	

	/**
	 * 深度遍历文件夹中的文件.
	 * 
	 * 使用方法:
		SGDirectory.eachFile(
			new File(srcImgPath), 
			dstImgPath, File.separatorChar, 
			(file,path,obj)->{
				SGPath.copyFile(file, new File(path));
			}, 
			(filder,path,obj)->{
				SGDirectory.EnsureExists(path);
			}
		);
	 * 
	 * @param sourceFolder
	 * @param dstFolder
	 * @param sp
	 * @param fileAction
	 * @param folderAction
	 */
	public static void eachFile(File sourceFolder,
			String dstFolder,char sp,
			SGAction<File,String,Object> fileAction,
			SGAction<File,String,Object> folderAction
			) {		
//        mkdirs(channel, dstFolder);
        File[] files = sourceFolder.listFiles();
        if (files != null) {
            for (File file : files) {
            	String dstPath= dstFolder + sp + file.getName();
                if (file.isFile()) {
                	fileAction.go(file,dstPath, files);
//                    System.out.println("上传: " + file.getName());
//                    try (FileInputStream fis = new FileInputStream(file)) {
//                        channel.put(fis, dstFolder + sp + file.getName());
//                    }
                }else if(file.isDirectory()){
                	folderAction.go(file, dstPath, files);
                	SGDirectory.eachFile(file,dstPath,sp,fileAction,folderAction);
                }
            }
        }
	}
}
