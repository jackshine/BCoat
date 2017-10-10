package com.elastica.driver;

/**
 * Driver Type
 * @author Eldo Rajan
 *
 */
public enum DriverType {
	Local("local"),
	Grid("grid"),
	Cloud("cloud");

	/**
	 * get driver type
	 * @param driverType
	 * @return
	 */
	public static DriverType getDriverType(final String driverType) {
		DriverType dType=null;
		try{
			if (driverType.equalsIgnoreCase("local")) {
				dType=DriverType.Local;
			} else if (driverType.equalsIgnoreCase("grid")) {
				dType=DriverType.Grid;
			} else if (driverType.equalsIgnoreCase("cloud")) {
				dType=DriverType.Cloud;
			} else {
				dType=DriverType.Local;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return dType;
	}

	private String driverType;
	
	DriverType(final String type) {
		this.driverType = type;
	}

	/**
	 * get driver type
	 * @return
	 */
	public String getDriverType() {
		return this.driverType;
	}

}
