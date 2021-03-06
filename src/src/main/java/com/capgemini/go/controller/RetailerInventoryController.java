package com.capgemini.go.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.go.service.RetailerInventoryService;
import com.capgemini.go.bean.RetailerInventoryBean;
import com.capgemini.go.dto.RetailerInventoryDTO;
import com.capgemini.go.exception.RetailerInventoryException;

@RestController
@RequestMapping("/getRetailerInventory")
public class RetailerInventoryController {
	
	@Autowired
	private RetailerInventoryService retailerInventoryService;
		
	@ResponseBody
	@GetMapping("/getDeliveryTimeReport")
	public List<RetailerInventoryBean> getDeliveryTimeReport(@RequestParam String retailerId, @RequestParam int reportType)
	{
		
		List<RetailerInventoryBean> result = null;
		switch (reportType) {
		case 1: {
			try {
				result = retailerInventoryService.getItemWiseDeliveryTimeReport(retailerId);
			} catch (RetailerInventoryException error) {
				error.printStackTrace();
				System.out.println("Delivery Time Report - " + error.getMessage());
			}
			break;
		}
		case 2: {
			try {
				result = retailerInventoryService.getCategoryWiseDeliveryTimeReport(retailerId);
			} catch (RetailerInventoryException error) {
				error.printStackTrace();
				System.out.println("Delivery Time Report - " + error.getMessage());
			}
			break;
		}
		default: {		
			System.out.println("Delivery Time Report - " + "Invalid Argument Recieved");
		}
	}
	
		return result;
		
		}
	@ResponseBody
	@GetMapping("/ShelfTimeReport")
	public List<RetailerInventoryBean> getShelfTimeReport(@RequestParam String retailerId, @RequestParam int reportType) {
		// logger.info("getShelfTimeReport - " + "Request for Shelf Time Report Received");	
		Calendar dateSelection = Calendar.getInstance();
		List<RetailerInventoryBean> result = null;
		switch (reportType) {
		case 1: {
			try {
				result = retailerInventoryService.getMonthlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
				error.printStackTrace();
				System.out.println("Shelf Time Report - " + error.getMessage());
			}
			break;
		}

		case 2: {
			try {
				result = retailerInventoryService.getQuarterlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
					error.printStackTrace();
					System.out.println("Shelf Time Report - " + error.getMessage());
			}
			break;
		}

		case 3: {
			try {
				result = retailerInventoryService.getYearlyShelfTimeReport(retailerId, dateSelection);
			} catch (RetailerInventoryException error) {
				// logger.error("getShelfTimeReport - " + error.getMessage());
				error.printStackTrace();
				System.out.println("Shelf Time Report - " + error.getMessage());
			}
			break;
		}
		default: {
			// logger.error("getShelfTimeReport - " + "Invalid Argument Received");
			System.out.println("Shelf Time Report - " + "Invalid Argument Recieved");
		}
		}
		return result;

	}
	
	@ResponseBody
	@GetMapping("/getProductRecieveTime")
	public String getUpdateProductRecieveTimeStamp(@RequestBody RetailerInventoryDTO retailerInventoryDTO)
	{
		String status="Product Timestamp updated";
		try {
			retailerInventoryService.updateProductRecieveTimeStamp(retailerInventoryDTO);
		}catch (RetailerInventoryException error) {
			error.printStackTrace();
			System.out.println("Product Recieve Time - " + error.getMessage());
		}
		return status;
		
	}
	
	@ResponseBody
	@GetMapping("/getProductSaleTime")
	public String getUpdateProductSaleTimeStamp(@RequestBody RetailerInventoryDTO retailerInventoryDTO)
	{
		String status="Product Timestamp updated";
		try {
			retailerInventoryService.updateProductSaleTimeStamp(retailerInventoryDTO);
		}catch (RetailerInventoryException error) {
			error.printStackTrace();
			System.out.println("Product Sale Time - " + error.getMessage());
		}
		return status;
		
	}
	
	@ResponseBody
	@GetMapping("/RetailerList")
	public List<RetailerInventoryDTO> getRetailerList () {
		List<RetailerInventoryDTO> result = null;
		try {
			result = this.retailerInventoryService.getListOfRetailers();

		} catch (Exception error) {
			error.printStackTrace();
			System.out.println("Retailer List - " + error.getMessage());
		}
		return result;
	}
	

	@GetMapping("/RetailerInventoryById")
	public List<RetailerInventoryDTO> getRetailerInventoryById (@RequestParam String retailerId) {
		
		List<RetailerInventoryDTO> result = null;
		try {
			result = this.retailerInventoryService.getInventoryById(retailerId);
		} catch (Exception error) {
			error.printStackTrace();
			System.out.println("Retailer List By Invenory By Id - " + error.getMessage());
		}
		return result;
	}

}
	
	
	
	


