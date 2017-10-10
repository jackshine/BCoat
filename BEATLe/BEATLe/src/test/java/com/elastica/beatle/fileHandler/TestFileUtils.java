package com.elastica.beatle.fileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.testng.Reporter;

public class TestFileUtils {
	
	public static void writeDatasourceIntoFile(String sourceID,String filePath) throws Exception{


		File file=null;
		ObjectOutputStream objOutputStream=null;
		try{
			file = new File(FileHandlingUtils.getFileAbsolutePath(filePath));
			objOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objOutputStream.writeObject(sourceID);
			objOutputStream.flush();
			
			Reporter.log("ds write success",true);

		}catch(Exception ex)
		{


		}
		finally{
			file=null;
			objOutputStream=null;

		}


	}
	public static String readDatasourceFromTheFile(String filePath) throws Exception{

		String dsID=null;
		//ObjectInputStream objInputStream=null;

		try{
		
			FileInputStream fin = new FileInputStream(FileHandlingUtils.getFileAbsolutePath(filePath));
			ObjectInputStream objInputStream  = new ObjectInputStream(fin);  
			dsID = (String)objInputStream.readObject();
			Reporter.log("dsID::"+dsID,true);  
			objInputStream.close();

		}catch(Exception ex)
		{
			Reporter.log("exception"+ex,true);  

		}
		finally{

			//objInputStream=null;
		}
		return dsID;


	}

}
