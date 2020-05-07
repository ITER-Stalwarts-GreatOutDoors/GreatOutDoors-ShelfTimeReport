package com.cg.iter.shelftimereport.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

 //import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cg.iter.shelftimereport.bean.RetailerInventoryBean;
import com.cg.iter.shelftimereport.dto.RetailerInventoryDTO;
import com.cg.iter.shelftimereport.exception.RetailerInventoryException;
import com.cg.iter.shelftimereport.services.RetailerInventoryService;

@RestController
@RequestMapping("/RetailerInventory")
public class RetailerInventoryController {

	 //private Logger logger = Logger.getRootLogger();

	@Autowired
	private RetailerInventoryService retailerInventoryService;

	@ResponseBody
	@PostMapping("/ShelfTimeReport")
	public String getShelfTimeReport(@RequestParam int retailerId, @RequestParam int reportType) {
		// logger.info("getShelfTimeReport - " + "Request for Shelf Time Report Received");	
		// String retailerId = requestData.get("retailerId").toString();
//		int reportType = Integer.valueOf(requestData.get("reportType").toString());
		String status = "Request for Shelf Time Report Recieved!";
		Calendar dateSelection = Calendar.getInstance();
		List<RetailerInventoryBean> result = null;
		switch (reportType) {
		case 1: {
			try {
				result = this.retailerInventoryService.getMonthlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
				
				error.printStackTrace();
				status = error.getMessage();
			}
			break;
		}

		case 2: {
			try {
				result = this.retailerInventoryService.getQuarterlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
				
					error.printStackTrace();
					status = error.getMessage();
			}
			break;
		}

		case 3: {
			try {
				result = this.retailerInventoryService.getYearlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
				
				error.printStackTrace();
				status = error.getMessage();
			}
			break;
		}
		default: {
			// logger.error("getShelfTimeReport - " + "Invalid Argument Received");
			
			return "Data could not be obtained from database";
		}
		}
		return status;

	}
	@ResponseBody
	@PostMapping("/getProductRecieveTime")
	public String getUpdateProductRecieveTimeStamp(@RequestBody RetailerInventoryDTO retailerInventoryDTO)
	{
		String status="Product Timestamp updated";
		try {
			retailerInventoryService.updateProductRecieveTimeStamp(retailerInventoryDTO);
		}catch (RetailerInventoryException error) {
			error.printStackTrace();
			status = error.getMessage();
		}
		return status;
		
	}
	
	@ResponseBody
	@PostMapping("/getProductSaleTime")
	public String getUpdateProductSaleTimeStamp(@RequestBody RetailerInventoryDTO retailerInventoryDTO)
	{
		String status="Product Timestamp updated";
		try {
			retailerInventoryService.updateProductSaleTimeStamp(retailerInventoryDTO);
		}catch (RetailerInventoryException error) {
			error.printStackTrace();
			status = error.getMessage();
		}
		return status;
		
	}

}