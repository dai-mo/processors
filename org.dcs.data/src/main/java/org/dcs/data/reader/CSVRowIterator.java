package org.dcs.data.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dcs.api.RESTException;
import org.dcs.api.model.ErrorCode;

import com.opencsv.CSVReader;

public class CSVRowIterator implements TableRowIterator {
		
	private CSVReader csvReader;
	
	
	public CSVRowIterator(InputStream inputStream) {		
		csvReader = new CSVReader(new InputStreamReader(inputStream));
	}
	
	public String[] nextRow() throws RESTException {
		
		try {
			String[] row = csvReader.readNext();
			if(row == null) {
				csvReader.close();
			}
			return row;
		} catch (IOException ioe1) {
			try {
				csvReader.close();
			} catch (IOException ioe2) {
				throw new RESTException(ErrorCode.DCS104(), ioe2);
			}
			throw new RESTException(ErrorCode.DCS104(),ioe1);
		}
	}

}
