package com.boilerplate.service.implemetations;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.EntityExistsException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.Widget;
import com.boilerplate.service.interfaces.IWidgetService;

/**
 * This class is the widget service which provides bulk of the widget 
 * operations.
 * @author gaurav
 *
 */
public class WidgetService implements IWidgetService {
	@Override
	public BoilerplateList<Widget> get(BoilerplateList<BaseEntity> entities)
			throws BadRequestException, ConflictException, NotFoundException,
			UnauthorizedException, ValidationFailedException {
		BoilerplateList<Widget> widgetList = 
				new BoilerplateList<Widget>();
		Widget widget = new Widget();
		widget.setId(((BaseEntity)entities.get(0)).getId());

		widget.setName(UUID.randomUUID().toString());
		widgetList.add(widget);
		return widgetList;
		
	}
}
