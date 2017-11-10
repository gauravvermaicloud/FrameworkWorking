package asyncWorkObservers;

import java.io.Serializable;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;
import com.boilerplate.framework.RequestThreadLocal;

public class ObserveUserOfThreadObserver implements IAsyncWorkObserver,Serializable
{

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		// TODO Auto-generated method stub
		DispatchObject dispatchObject = (DispatchObject)asyncWorkItem.getPayload();
		dispatchObject.setUserName(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		
	}

}
