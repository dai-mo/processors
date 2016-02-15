package org.dcs.core.data.reader;

import java.io.FileWriter;
import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.dcs.api.RESTException;
import org.dcs.api.model.ErrorCode;

import com.opencsv.CSVWriter;

public class CSVRowWriter {
	
	private final String dataSourcePath;
	private final CSVWriter csvWriter;
	
	public CSVRowWriter(String dataSourcePath) throws RESTException {
		this.dataSourcePath = dataSourcePath;
		try {
			this.csvWriter = new CSVWriter(new FileWriter(dataSourcePath), ',');
		} catch (IOException ioe) {
			throw new RESTException(ErrorCode.DCS105(), ioe);
		}	
	}
	
	public void writeNext(@NotNull String[] row) {
		csvWriter.writeNext(row);
	}
	
	public void close() throws RESTException {
		try {
			csvWriter.close();
		} catch (IOException ioe) {
			throw new RESTException(ErrorCode.DCS105(), ioe);
		}
	}

}
