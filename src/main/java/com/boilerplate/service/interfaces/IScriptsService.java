package com.boilerplate.service.interfaces;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;

public interface IScriptsService {
	public void createPanEmailPhoneNumberHash() throws UnauthorizedException, NotFoundException, BadRequestException;

	public void createTradelineStatusHash() throws NotFoundException, BadRequestException, UnauthorizedException;

	public void updatePanUpperCase() throws NotFoundException, BadRequestException, UnauthorizedException;

	public void processReportsFromDirectory() throws UnauthorizedException, NotFoundException, BadRequestException;

	public void readEmailAttachment() throws  Exception;
	/**
	 * This method accept the task and push the delete tradeline keys task into queue.
	 * @throws UnauthorizedException The UnauthorizedException
	 * @throws NotFoundException The NotFoundException
	 * @throws BadRequestException The BadRequestException
	 */
	public void deleteTradelinesKey() throws UnauthorizedException, NotFoundException, BadRequestException;

	public void addDatabaseAccount() throws UnauthorizedException, NotFoundException, BadRequestException;
}
