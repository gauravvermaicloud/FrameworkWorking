package asyncWorkObservers;

import java.io.Serializable;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;

public class SubtractNumberAsyncWorkObserver   implements IAsyncWorkObserver,Serializable{
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setSubtractResult(dispatchObject.getNumberOne()- dispatchObject.getNumberTwo());
	}
	
}
