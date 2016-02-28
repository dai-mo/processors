package org.dcs.data.reader;

import java.io.FileWriter;
import java.io.IOException;

import org.dcs.api.model.ErrorConstants;
import org.dcs.api.service.RESTException;

import com.opencsv.CSVWriter;

public class CSVRowWriter {
	
	private final String dataSourcePath;
	private final CSVWriter csvWriter;
	
	public CSVRowWriter(String dataSourcePath) throws RESTException {
		this.dataSourcePath = dataSourcePath;
		try {
			this.csvWriter = new CSVWriter(new FileWriter(dataSourcePath), ',');
		} catch (IOException ioe) {
			throw new RESTException(ErrorConstants.getErrorResponse("DCS105"), ioe);
		}	
	}
	
	public void writeNext(String[] row) {
		csvWriter.writeNext(row);
	}
	
	public void close() throws RESTException {
		try {
			csvWriter.close();
		} catch (IOException ioe) {
			throw new RESTException(ErrorConstants.getErrorResponse("DCS105"), ioe);
		}
	}

}
