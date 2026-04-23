package com.salman.payflow.service;

import java.math.BigDecimal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Async
	public void sendTransferNotification(long fromId,long toId, BigDecimal amount) {
		System.out.println("The notification sarted for transfer");
		System.out.println("The amount is sent from:" +fromId);
		System.out.println("The amount is received to:"+ toId);
		System.out.println("The amount transfered is:"+amount);
		
		try {
			Thread.sleep(3000);
		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		System.out.println("Notification Successful");
		
	}
}
