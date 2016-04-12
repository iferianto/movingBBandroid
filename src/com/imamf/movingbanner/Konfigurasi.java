package com.imamf.movingbanner;

/**
 * create by imam ferianto
 * <iferianto@yahoo.com>
 */

public class Konfigurasi {

	public static final String MYTAG="movingBanner";
	public static final Boolean ENABLE_DEBUG=true;
		
	public static final String REGISTRATION_URL="http://10.0.3.2/banner/backend/index.php?r=regandroid&key=secret";
	
	public static final int REGISTRATION_TIMEOUT = 3 * 1000;
	public static final int WAIT_TIMEOUT = 30 * 1000;
	public static final int ZBAR_SCANNER_REQUEST = 0;
	public static final int ZBAR_QR_SCANNER_REQUEST = 1;
	public static final int ALERT_DIALOG1 = 0;
	public static final long LOCATION_REFRESH_TIME = 1; //minute
	public static final float LOCATION_REFRESH_DISTANCE = 10; //meter
	
	
}
