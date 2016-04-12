package com.imamf.movingbanner;
/**
 * create by imam ferianto
 * <iferianto@yahoo.com>
 */

import java.util.HashMap;

public interface AsyncTaskCompleteListener {


	void onTaskCompleted(String result,Boolean error,HashMap<String, String> postparam);

}
