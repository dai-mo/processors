package org.dcs.core.data.reader;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.dcs.api.RESTException;
import org.dcs.api.model.ErrorCode;

import com.opencsv.CSVWriter;

public class TableLoader {
	
	
	private final CSVRowIterator csvRowIterator;
	private final CSVRowWriter csvRowWriter;
	
	public TableLoader(InputStream inputStream, String dataSourcePath) throws RESTException {		
		this.csvRowIterator = new CSVRowIterator(inputStream);
		this.csvRowWriter = new CSVRowWriter(dataSourcePath);
	}
	
	public void load() throws RESTException {
	    	   	   
	    try {	    		    	
	      String [] nextLine;
	      	      	      	 	 
	      while ((nextLine = csvRowIterator.nextRow()) != null) {
	      	csvRowWriter.writeNext(nextLine);
	      }

	    } finally {
	      if (csvRowWriter != null) {
					csvRowWriter.close();
				}
	    }
	}

}
