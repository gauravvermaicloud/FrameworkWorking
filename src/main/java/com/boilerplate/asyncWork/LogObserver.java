package com.boilerplate.asyncWork;

import com.boilerplate.framework.Logger;
import com.boilerplate.jobs.QueueReaderJob;

/**
 * This class is used to log an item.
 * @author gaurav
 *
 */
public class LogObserver implements IAsyncWorkObserver{

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(LogObserver.class);
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem){
		if(logger.isDebugEnabled()){
			logger.logDebug("LogObserver", "observe", ""
					,asyncWorkItem ==null ?"Null": asyncWorkItem.toJSON());
		}
	}

}
