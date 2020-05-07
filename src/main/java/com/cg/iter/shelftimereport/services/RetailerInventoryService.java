package com.cg.iter.shelftimereport.services;

import java.util.Calendar;
import java.util.List;

import com.cg.iter.shelftimereport.bean.RetailerInventoryBean;
import com.cg.iter.shelftimereport.dto.RetailerInventoryDTO;
import com.cg.iter.shelftimereport.exception.RetailerInventoryException;

public interface RetailerInventoryService {

	public List<RetailerInventoryBean> getMonthlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException;


	public List<RetailerInventoryBean> getQuarterlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException;

	public List<RetailerInventoryBean> getYearlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException;


	public boolean updateProductSaleTimeStamp(RetailerInventoryDTO retailerInventoryDTO) throws RetailerInventoryException;

	public boolean updateProductRecieveTimeStamp(RetailerInventoryDTO retailerinventorydto) throws RetailerInventoryException;
	
}
