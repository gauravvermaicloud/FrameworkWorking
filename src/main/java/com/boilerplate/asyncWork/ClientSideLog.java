package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IDiagnostic;
import com.boilerplate.java.entities.ClientSideDiagnostic;
import com.boilerplate.service.implemetations.PingService;

public class ClientSideLog implements IAsyncWorkObserver{

	@Autowired IDiagnostic diagnostic;
	
	public void setDiagnostic(IDiagnostic diagnostic){
		this.diagnostic = diagnostic;
	}
	
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		diagnostic.publishClientSideLog((ClientSideDiagnostic)asyncWorkItem.getPayload());
	}
	
}
