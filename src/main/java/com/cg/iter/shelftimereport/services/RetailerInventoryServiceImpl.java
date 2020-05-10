package com.cg.iter.shelftimereport.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.RollbackException;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.iter.shelftimereport.bean.RetailerInventoryBean;

import com.cg.iter.shelftimereport.dto.RetailerInventoryDTO;
import com.cg.iter.shelftimereport.dto.UserDTO;
import com.cg.iter.shelftimereport.exception.ExceptionConstants;
import com.cg.iter.shelftimereport.exception.RetailerInventoryException;
import com.cg.iter.shelftimereport.exception.UserException;
import com.cg.iter.shelftimereport.repository.RetailerInventoryRepository;
import com.cg.iter.shelftimereport.repository.UserRepository;
import com.cg.iter.shelftimereport.utility.GoUtility;

@Service(value = "retailerInventoryService")
public class RetailerInventoryServiceImpl implements RetailerInventoryService {
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// private Logger logger = Logger.getRootLogger();
	@Autowired
	private RetailerInventoryRepository retailerInventoryRepository;

	@Autowired
	private UserRepository userRepository;


	@Override
	public boolean updateProductRecieveTimeStamp(RetailerInventoryDTO retailerinventorydto)
			throws RetailerInventoryException {
		// logger.info("updateProductReceiveTimeStamp - " + "function called");
		boolean receiveTimestampUpdated = false;

		Transaction transaction = null;
		Session session = getSessionFactory().openSession();
		try {
			transaction = session.getTransaction();
			transaction.begin();
			RetailerInventoryDTO existingItem = (RetailerInventoryDTO) retailerInventoryRepository.findAll();
			// session.find(RetailerInventoryDTO.class,
			// queryArguments.getProductUniqueId());
			if (existingItem == null) {
				// logger.debug(ExceptionConstants.PRODUCT_NOT_IN_INVENTORY);
				throw new RetailerInventoryException(
						"updateProductReceiveTimeStamp - " + ExceptionConstants.PRODUCT_NOT_IN_INVENTORY);
			}
			existingItem.setProductRecieveTimestamp(retailerinventorydto.getProductRecieveTimestamp());
			transaction.commit();
		} catch (IllegalStateException error) {
			// logger.error(error.getMessage());
			throw new RetailerInventoryException(
					"updateProductReceiveTimeStamp - " + ExceptionConstants.INAPPROPRIATE_METHOD_INVOCATION);
		} catch (RollbackException error) {
			// logger.error(error.getMessage());
			throw new RetailerInventoryException(
					"updateProductReceiveTimeStamp - " + ExceptionConstants.FAILURE_COMMIT_CHANGES);
		} finally {
			session.close();
		}
		receiveTimestampUpdated = true;
		return receiveTimestampUpdated;

	}

	@Override
	public boolean updateProductSaleTimeStamp(RetailerInventoryDTO retailerinventorydto)
			throws RetailerInventoryException {
		// TODO Auto-generated method stub
		boolean saleTimestampUpdated = false;
		// logger.info("updateProductSaleTimeStamp - " + "function called");
		/*
		 * required arguments in `queryArguments` productUIN, productSaleTime
		 * 
		 * un-required productDispatchTime, productReceiveTime, productCategory,
		 * retailerUserId
		 */
		Transaction transaction = null;
		Session session = getSessionFactory().openSession();
		try {
			transaction = session.getTransaction();
			transaction.begin();
			RetailerInventoryDTO existingItem = (RetailerInventoryDTO) retailerInventoryRepository.findAll();
			// session.find(RetailerInventoryDTO.class,
			// queryArguments.getProductUniqueId());
			if (existingItem == null) {
				// logger.debug(ExceptionConstants.PRODUCT_NOT_IN_INVENTORY);
				throw new RetailerInventoryException(
						"updateProductSaleTimeStamp - " + ExceptionConstants.PRODUCT_NOT_IN_INVENTORY);
			}
			existingItem.setProductSaleTimestamp(retailerinventorydto.getProductSaleTimestamp());
			transaction.commit();
		} catch (IllegalStateException error) {
			// logger.error("updateProductSaleTimeStamp - " + error.getMessage());
			throw new RetailerInventoryException(
					"updateProductSaleTimeStamp - " + ExceptionConstants.INAPPROPRIATE_METHOD_INVOCATION);
		} catch (RollbackException error) {
			// logger.error("updateProductSaleTimeStamp - " + error.getMessage());
			throw new RetailerInventoryException(
					"updateProductSaleTimeStamp - " + ExceptionConstants.FAILURE_COMMIT_CHANGES);
		} finally {
			session.close();
		}
		saleTimestampUpdated = true;
		return saleTimestampUpdated;

	}

	@Override
	public List<RetailerInventoryBean> getMonthlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
     List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();
		
		//RetailerInventoryDTO queryArguments = new RetailerInventoryDTO (retailerId, (byte)0, null, null, null, dateSelection );
		List<RetailerInventoryDTO> listOfSoldItems =  retailerInventoryRepository.findAllByretailerId(retailerId);
				//this.retailerInventoryDao.getSoldItemsDetails(queryArguments);
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			
			for (RetailerInventoryDTO soldItem : listOfSoldItems) {
				if (soldItem.getProductSaleTimestamp().get(Calendar.MONTH) == dateSelection.get(Calendar.MONTH)) {
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
				} else {
					
				}
			}
		} catch (RuntimeException error) {
			//GoLog.getLogger(RetailerInventoryServiceImpl.class).error(error.getMessage());
			throw new RetailerInventoryException ("getMonthlyShelfTimeReport - " + ExceptionConstants.INTERNAL_RUNTIME_ERROR);
		}
		return result;
	}


	@Override
	public List<RetailerInventoryBean> getQuarterlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean> ();
		
		//RetailerInventoryDTO queryArguments = new RetailerInventoryDTO (retailerId, (byte)0, null, null, null, dateSelection );
		List<RetailerInventoryDTO> listOfSoldItems =  retailerInventoryRepository.findAllByretailerId(retailerId);
				//this.retailerInventoryDao.getSoldItemsDetails(queryArguments);
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
			//GoLog.getLogger(RetailerInventoryServiceImpl.class).error(error.getMessage());
			throw new RetailerInventoryException ("getQuarterlyShelfTimeReport - " + ExceptionConstants.INTERNAL_RUNTIME_ERROR);
		}
		return result;
	}
	
	


	@Override
	public List<RetailerInventoryBean> getYearlyShelfTimeReport(int retailerId, Calendar dateSelection)
			throws RetailerInventoryException {
		List<RetailerInventoryBean> result = new ArrayList<RetailerInventoryBean>();

		// RetailerInventoryDTO queryArguments = new RetailerInventoryDTO (retailerId,
		// (byte)0, null, null, null, null, dateSelection);
		List<RetailerInventoryDTO> listOfSoldItems = retailerInventoryRepository.findAllByretailerId(retailerId);
		// this.retailerInventoryDao.getSoldItemsDetails(queryArguments);
		try {
			List<UserDTO> userList = (List<UserDTO>) userRepository.findAll();
			// this.userDao.getUserIdList();

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
			// logger.error(error.getMessage());
			throw new RetailerInventoryException(
					"getYearlyShelfTimeReport - " + ExceptionConstants.INTERNAL_RUNTIME_ERROR);
		}
		return result;
	}

}
