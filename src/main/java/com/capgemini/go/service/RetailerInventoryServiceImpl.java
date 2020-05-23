package com.capgemini.go.service;

import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.RollbackException;
import com.capgemini.go.repository.RetailerInventoryRepository;
import com.capgemini.go.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.capgemini.go.bean.RetailerInventoryBean;
import com.capgemini.go.dto.RetailerDTO;
import com.capgemini.go.dto.RetailerInventoryDTO;
import com.capgemini.go.dto.UserDTO;
import com.capgemini.go.exception.RetailerInventoryException;

import com.capgemini.go.utility.GoUtility;
@Service
public class RetailerInventoryServiceImpl implements RetailerInventoryService {

	@Autowired
	private RetailerInventoryRepository retailerInventoryRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	RestTemplate restTemplate;
	
	private String authenticationURL = "http://authentication-service/app/admin/viewAllRetailers";
	
	//Delivery Time Report
	/*******************************************************************************************************
	 * - Function Name : getItemWiseDeliveryTimeReport <br>
	 * - Description : to get Item wise Delivery Time Report <br>
	 * 
	 * @param String retailerId
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryBean> getItemWiseDeliveryTimeReport(String retailerId) throws RetailerInventoryException {
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();
		List<RetailerInventoryDTO> listOfDeliveredItems = retailerInventoryRepository.findAllByretailerId(retailerId);		
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			 for (RetailerInventoryDTO deliveredItem : listOfDeliveredItems) {
				RetailerInventoryBean object = new RetailerInventoryBean ();
				object.setRetailerId(retailerId);
				for (UserDTO user : userList) {
					if (user.getUserId().equals(retailerId)) {
						object.setRetailerName(user.getUserName());
					break;
				}
				}
				object.setRetailerName("vikash");
				object.setProductCategoryNumber(deliveredItem.getProductCategory());
				object.setProductCategoryName(GoUtility.getCategoryName(deliveredItem.getProductCategory()));
				object.setProductUniqueId(deliveredItem.getProductUniqueId());
				object.setDeliveryTimePeriod(GoUtility.calculatePeriod(deliveredItem.getProductDispatchTimestamp(), deliveredItem.getProductRecieveTimestamp()));
				object.setShelfTimePeriod(null);
				result.add(object);
			}		
		} catch (RuntimeException error) {
			throw new RetailerInventoryException ("INTERNAL_RUNTIME_ERROR");
		}
		return result;
	}

	/*******************************************************************************************************
	 * - Function Name : getCategoryWiseDeliveryTimeReport <br>
	 * - Description : to get Category wise Delivery Time Report <br>
	 * 
	 * @param String retailerId
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryBean> getCategoryWiseDeliveryTimeReport(String retailerId) throws RetailerInventoryException{
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();
		List<RetailerInventoryDTO> listOfDeliveredItems = retailerInventoryRepository.findAllByretailerId(retailerId); 
		Map<Integer, List<RetailerInventoryBean>> map = new HashMap<Integer, List<RetailerInventoryBean>>();
		for (int category = 1; category <= 5; category++)
			map.put(category, new ArrayList<RetailerInventoryBean>());	
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			for (RetailerInventoryDTO deliveredItem : listOfDeliveredItems) {
				RetailerInventoryBean object = new RetailerInventoryBean ();
				object.setRetailerId(retailerId);
				for (UserDTO user : userList) {
					if (user.getUserId().equals(retailerId)) {
						object.setRetailerName(user.getUserName());
						break;
					}
				}
				object.setProductCategoryNumber(deliveredItem.getProductCategory());
				object.setProductCategoryName(GoUtility.getCategoryName(deliveredItem.getProductCategory()));
				object.setProductUniqueId(deliveredItem.getProductUniqueId());
				object.setDeliveryTimePeriod(GoUtility.calculatePeriod(deliveredItem.getProductDispatchTimestamp(), deliveredItem.getProductRecieveTimestamp()));
				object.setShelfTimePeriod(null);
				map.get(Integer.valueOf(object.getProductCategoryNumber())).add(object);
			}
			
			for (int category = 1; category <= 5; category++) {
				if (map.get(category).size() != 0) {
					int years = 0, months = 0, days = 0, count = 0;
					for (RetailerInventoryBean item : map.get(category)) {
						years += item.getDeliveryTimePeriod().getYears(); 
						months += item.getDeliveryTimePeriod().getMonths(); 
						days += item.getDeliveryTimePeriod().getDays();
						count ++;
					}
					years /= count;
					months /= count;
					days /= count;
					RetailerInventoryBean object = new RetailerInventoryBean ();
					object.setProductCategoryNumber((byte)category);
					object.setProductCategoryName(GoUtility.getCategoryName(category));
					object.setProductUniqueId("----");
					object.setDeliveryTimePeriod(Period.of(years, months, days));
					result.add(object);
				}
			}
			
		} catch (RuntimeException error) {
			throw new RetailerInventoryException ("INTERNAL_RUNTIME_ERROR");
		}
		return result;
	}

	/*******************************************************************************************************
	 * - Function Name : updateItemReceiveTimestamp <br>
	 * - Description : to update receive timestamp of an item in inventory <br>
	 * 
	 * @return boolean (true: if receive timestamp updated | false: otherwise)
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public boolean updateItemRecieveTimeStamp(RetailerInventoryDTO retailerinventorydto) throws RetailerInventoryException {
		boolean receiveTimestampUpdated = false;

		try {

			RetailerInventoryDTO existingItem = (RetailerInventoryDTO) retailerInventoryRepository.findAll();
			if (existingItem == null) {
			throw new RetailerInventoryException("PRODUCT_NOT_IN_INVENTORY");
			}
			existingItem.setProductRecieveTimestamp(retailerinventorydto.getProductRecieveTimestamp());
	
		} catch (IllegalStateException error) {
			throw new RetailerInventoryException("INAPPROPRIATE_METHOD_INVOCATION");
		} catch (RollbackException error) {
			throw new RetailerInventoryException("FAILURE_COMMIT_CHANGES");
		} 
		receiveTimestampUpdated = true;
		return receiveTimestampUpdated;
		
	}
	
	/*******************************************************************************************************
	 * - Function Name : updateItemSaleTimestamp <br>
	 * - Description : to update sale timestamp of an item in inventory <br>
	 * 
	 * @return boolean (true: if sale timestamp updated | false: otherwise)
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public boolean updateItemSaleTimeStamp(RetailerInventoryDTO retailerinventorydto) throws RetailerInventoryException {
		boolean saleTimestampUpdated = false;

		try {
			RetailerInventoryDTO existingItem = (RetailerInventoryDTO) retailerInventoryRepository.findAll();
			if (existingItem == null) {
				throw new RetailerInventoryException("PRODUCT_NOT_IN_INVENTORY");
			}
			existingItem.setProductSaleTimestamp(retailerinventorydto.getProductSaleTimestamp());
	
		} catch (IllegalStateException error) {
			throw new RetailerInventoryException("INAPPROPRIATE_METHOD_INVOCATION");
		} catch (RollbackException error) {
			throw new RetailerInventoryException("FAILURE_COMMIT_CHANGES");
		}
		saleTimestampUpdated = true;
		return saleTimestampUpdated;
		
	}
   
	//Retailer Management System
	/*******************************************************************************************************
	 * - Function Name : getListOfRetailers <br>
	 * - Description : to get list of retailers in database <br>
	 * 
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryDTO> getListOfRetailers() {
				return (List<RetailerInventoryDTO>) retailerInventoryRepository.findAll();
	}

	@Override
	public List<RetailerInventoryDTO> getInventoryById(String retailerId) {
		
		return retailerInventoryRepository.findAllByretailerId(retailerId);
	}
	
	public boolean deleteItemFromInventory(int retailerId, String productUIN) throws RetailerInventoryException {
	
		boolean itemDeleted = false;
		if(retailerInventoryRepository.findById(productUIN).isPresent())
		{
			retailerInventoryRepository.deleteById(productUIN);
			itemDeleted=true;
		}

		return itemDeleted;
	}
	
	public boolean addItemToInventory(String retailerId, byte productCategory, String productId, String productUIN) throws RetailerInventoryException {
		boolean itemAdded = false;
		Calendar currentSystemTimestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		RetailerInventoryDTO queryArgument = new RetailerInventoryDTO(retailerId, productCategory, productId, productUIN, currentSystemTimestamp, null, null);
		itemAdded = retailerInventoryRepository.save(queryArgument) != null;
		return itemAdded;
	}

	//Shelf Time Report
	/*******************************************************************************************************
	 * - Function Name : getMonthlyShelfTimeReport <br>
	 * - Description : to get Monthly Shelf Time Report <br>
	 * 
	 * @param String   retailerId
	 * @param Calendar dateSelection
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryBean> getMonthlyShelfTimeReport(String retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();

		List<RetailerInventoryDTO> listOfSoldItems =  retailerInventoryRepository.findAllByretailerId(retailerId);
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			
			for (RetailerInventoryDTO soldItem : listOfSoldItems) {
				if (soldItem.getProductSaleTimestamp().get(Calendar.MONTH) == dateSelection.get(Calendar.MONTH)) {
					RetailerInventoryBean object = new RetailerInventoryBean ();
					object.setRetailerId(retailerId);
					object.setRetailerName("plawan");
					for (UserDTO user : userList) {
						if (user.getUserId().equals(retailerId)) {
							object.setRetailerName(user.getUserName());
							break;
						}
					}
					object.setProductCategoryNumber(soldItem.getProductCategory());
					object.setProductCategoryName(GoUtility.getCategoryName(soldItem.getProductCategory()));
					object.setProductUniqueId(soldItem.getProductUniqueId());
					object.setShelfTimePeriod(GoUtility.calculatePeriod(soldItem.getProductRecieveTimestamp(), 
							soldItem.getProductSaleTimestamp()));
					object.setDeliveryTimePeriod(null);
					result.add(object);
				}
			}
		} catch (RuntimeException error) {
			throw new RetailerInventoryException ("INTERNAL_RUNTIME_ERROR");
		}
		return result;
		 
	}

	/*******************************************************************************************************
	 * - Function Name : getQuarterlyShelfTimeReport <br>
	 * - Description : to get Quarterly Shelf Time Report <br>
	 * 
	 * @param String   retailerId
	 * @param Calendar dateSelection
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryBean> getQuarterlyShelfTimeReport(String retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();
		List<RetailerInventoryDTO> listOfSoldItems =  retailerInventoryRepository.findAllByretailerId(retailerId);
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			
			for (RetailerInventoryDTO soldItem : listOfSoldItems) {
				RetailerInventoryBean object = new RetailerInventoryBean ();
				object.setRetailerId(retailerId);
				for (UserDTO user : userList) {
					if (user.getUserId().equals(retailerId)) {
						object.setRetailerName(user.getUserName());
						break;
					}
				}
				object.setProductCategoryNumber(soldItem.getProductCategory());
				object.setProductCategoryName(GoUtility.getCategoryName(soldItem.getProductCategory()));
				object.setProductUniqueId(soldItem.getProductUniqueId());
				object.setShelfTimePeriod(GoUtility.calculatePeriod(soldItem.getProductRecieveTimestamp(), 
						soldItem.getProductSaleTimestamp()));
				object.setDeliveryTimePeriod(null);
				result.add(object);
			}
			
	
		} catch (RuntimeException error) {
			throw new RetailerInventoryException ("INTERNAL_RUNTIME_ERROR");
		}
		return result;
	}

	/*******************************************************************************************************
	 * - Function Name : getYearlyShelfTimeReport <br>
	 * - Description : to get Yearly Shelf Time Report <br>
	 * 
	 * @param String   retailerId
	 * @param Calendar dateSelection
	 * @return List<RetailerInventoryBean>
	 * @throws RetailerInventoryException
	 *******************************************************************************************************/
	@Override
	public List<RetailerInventoryBean> getYearlyShelfTimeReport(String retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean>();
		List<RetailerInventoryDTO> listOfSoldItems = retailerInventoryRepository.findAllByretailerId(retailerId);
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();

			for (RetailerInventoryDTO soldItem : listOfSoldItems) {
				RetailerInventoryBean object = new RetailerInventoryBean();
				object.setRetailerId(retailerId);
				for (UserDTO user : userList) {
					if (user.getUserId().equals(retailerId)) {
						object.setRetailerName(user.getUserName());
						break;
					}
				}
				object.setProductCategoryNumber(soldItem.getProductCategory());
				object.setProductCategoryName(GoUtility.getCategoryName(soldItem.getProductCategory()));
				object.setProductUniqueId(soldItem.getProductUniqueId());
				object.setShelfTimePeriod(GoUtility.calculatePeriod(soldItem.getProductRecieveTimestamp(),
						soldItem.getProductSaleTimestamp()));
				object.setDeliveryTimePeriod(null);
				result.add(object);
			}

		} catch (RuntimeException error) {
			throw new RetailerInventoryException("INTERNAL_RUNTIME_ERROR");
		}
		return result;
	}
	/*******************************************************************************************************
	 * - Function Name : getRetailers <br>
	 * - Description : to get items in a given retailer's DTO <br>
	 * 
	 * @return List<RetailerInventoryDTO>
	 *******************************************************************************************************/
	@Override
	public List<RetailerDTO> getRetailers() {
		List<UserDTO> listUsers = (List<UserDTO>) userRepository.findAll();
		List<RetailerDTO> listRetailers = new ArrayList<>();
		RetailerDTO retailer = restTemplate.getForObject(authenticationURL+((RetailerDTO) listUsers).getUsers(),
				RetailerDTO.class);
		listRetailers.add(retailer);
		return listRetailers;
		
	}
}
