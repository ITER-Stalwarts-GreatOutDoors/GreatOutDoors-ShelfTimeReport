package com.capgemini.go.dto;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RETAILER_INVENTORY")
public class RetailerInventoryDTO {
	public String retailerId;
	public byte productCategory;
	public String productId;
	@Id
	public String productUniqueId;
	public Calendar productDispatchTimestamp;
	public Calendar productRecieveTimestamp;
	public Calendar productSaleTimestamp;
	
	//Constructors
	public RetailerInventoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RetailerInventoryDTO(String retailerId, byte productCategory, String productId, String productUniqueId,
			Calendar productDispatchTimestamp, Calendar productRecieveTimestamp, Calendar productSaleTimestamp) {
		super();
		this.retailerId = retailerId;
		this.productCategory = productCategory;
		this.productId = productId;
		this.productUniqueId = productUniqueId;
		this.productDispatchTimestamp = productDispatchTimestamp;
		this.productRecieveTimestamp = productRecieveTimestamp;
		this.productSaleTimestamp = productSaleTimestamp;
	}
	
	//Setters and Getters
	public String getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}
	public byte getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(byte productCategory) {
		this.productCategory = productCategory;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductUniqueId() {
		return productUniqueId;
	}
	public void setProductUniqueId(String productUniqueId) {
		this.productUniqueId = productUniqueId;
	}
	public Calendar getProductDispatchTimestamp() {
		return productDispatchTimestamp;
	}
	public void setProductDispatchTimestamp(Calendar productDispatchTimestamp) {
		this.productDispatchTimestamp = productDispatchTimestamp;
	}
	public Calendar getProductRecieveTimestamp() {
		return productRecieveTimestamp;
	}
	public void setProductRecieveTimestamp(Calendar productRecieveTimestamp) {
		this.productRecieveTimestamp = productRecieveTimestamp;
	}
	public Calendar getProductSaleTimestamp() {
		return productSaleTimestamp;
	}
	public void setProductSaleTimestamp(Calendar productSaleTimestamp) {
		this.productSaleTimestamp = productSaleTimestamp;
	}
	
	//To String
	@Override
	public String toString() {
		return "RetailerInventoryDTO [retailerId=" + retailerId + ", productCategory=" + productCategory
				+ ", productId=" + productId + ", productUniqueId=" + productUniqueId + ", productDispatchTimestamp="
				+ productDispatchTimestamp + ", productRecieveTimestamp=" + productRecieveTimestamp
				+ ", productSaleTimestamp=" + productSaleTimestamp + "]";
	}
	
	
	
	
}
