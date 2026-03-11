package com.sellgirl.sgJavaHelper.antivirus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;

public class SGProcessPcManager implements ISGProcessManager {

	@Override
	public void stopProcess(String pid) {
		SGDataHelper.ShutdownSpringBootForWindowsSystemByPID(pid);
	}

	public List<SGProcess> getProcesses()  {
        List<SGProcess> processIDList = new ArrayList<>();
		try {
			ProcessBuilder builder=new ProcessBuilder("tasklist");
			Process process = builder.start();

			BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

//	        List<String> processIDList = new ArrayList<>();
			while((line=reader.readLine())!=null) {
				//System.out.println(line);
	            String[] parts = line.split("\\s+");
	            if (parts.length > 1) {
	            	if(!parts[0].endsWith(".exe")) {
	            		continue;
	            	}
	            	SGProcess item=new SGProcess();
	            	item.setName(parts[0]);
	            	item.setId(parts[1]);
	            	item.setCpu(parts[2]);
	            	item.setMem(parts[3]);
	            	processIDList.add(item);
	            }
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processIDList;
	}


	public  List<SGProcess>  getBadProcesses() {
		String filePath = getFilePath();
		List<String> lines=SGDataHelper.ReadFileToLines(filePath);
		List<String> names=new ArrayList<String>();
		if(null!=lines) {
			for(String line:lines) {
				names.add(line.split(",")[0]);
			}
		}



//        List<String> processIDList = new ArrayList<>();
        List<SGProcess> processIDList = getProcesses();
        List<SGProcess> badList = new ArrayList<>();
		for(SGProcess item:processIDList) {

        	boolean isGood=false;
        	for(String good:names) {
        		if(good.equals(item.getName())) {
        			isGood=true;
        			break;
        		}
        	}
        	if(!isGood) {
        		badList.add(item);
        	}
		}
		
		return badList;
//		if(badList.isEmpty()) {
//			System.out.println("no bad process");
//		}else {
//			System.out.println("has bad process");
//	        for (SGProcess pid : badList) {
//	            System.out.println(pid.getName()+"---------"+pid.getId()+"---------"
//	            		+pid.getCpu()+"---------"+pid.getMem());
//	//            lines.add(pid.getName()+","+pid.getId()+","
//	//            		+pid.getCpu()+","+pid.getMem());
//	        }
//        }

		//SGDataHelper.WriteLinesToFile(lines, file);
	}
	private String getFilePath() {
		return  Paths
				.get(SGDataHelper.GetBaseDirectoryAbsolutePath(), LocalDataType.System.toString() + "LocalData", "Txt", "goodProcess.txt")
				.toString();
	}
	private String processToString(SGProcess pid) {
		return pid.getName()+","+pid.getId()+","
        		+pid.getCpu()+","+pid.getMem();
	}
	public  void  tagGoodProcess(SGProcess process) {
		String filePath = getFilePath();
		List<String> lines=SGDataHelper.ReadFileToLines(filePath);
		//List<String> names=new ArrayList<String>();
		if(null!=lines) {
			for(String line:lines) {
				String name=line.split(",")[0];
				if(name.equals(process.getName())) {
					return;
				}
				//names.add(name);
			}
		}            
		
		lines.add(processToString(process));
		
		SGDataHelper.WriteLinesToFile(lines, filePath);

	}
}
