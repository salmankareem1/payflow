package com.salman.payflow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salman.payflow.dto.ApiResponse;
import com.salman.payflow.dto.TransactionResponse;
import com.salman.payflow.dto.TransferRequest;
import com.salman.payflow.model.Transaction;
import com.salman.payflow.service.NotificationService;
import com.salman.payflow.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransactionController {
private final TransactionService transactionService;
private final NotificationService notificationService;
	
public TransactionController(TransactionService transactionService,NotificationService notificationService) {
	this.transactionService=transactionService;
	this.notificationService=notificationService;
}

// 200 OK for a successful transfer
@PostMapping("/transaction")
public ResponseEntity<ApiResponse<String>> transfer(@Valid @RequestBody TransferRequest request) {
	transactionService.transfer(request.getFromWalletId(), request.getToWalletId(), request.getAmount());
	notificationService.sendTransferNotification(request.getFromWalletId(), request.getToWalletId(), request.getAmount());	
	return ResponseEntity.ok(new ApiResponse<>("Transaction completed successfully",null));
	
}

// 200 OK
@GetMapping("/transactions")
public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(){
   List<TransactionResponse> transactions = transactionService.getAllTransactions().stream().map(this::mapToTransactionResponse).collect(Collectors.toList());
   return ResponseEntity.ok(new ApiResponse<>("Transactions fetched successfully",transactions));
}
//200 OK — 404 handled by WalletNotFoundException if wallet doesn't exist
@GetMapping("/wallet/{id}/transactions")
public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByWalletId(@PathVariable Long id) {
	List<TransactionResponse> transactions= transactionService.getTransactionsByWalletId(id).stream().map(this::mapToTransactionResponse).collect(Collectors.toList());
	return ResponseEntity.ok(new ApiResponse<>("Wallet transactions fetched successfully",transactions));
}
 private TransactionResponse mapToTransactionResponse(Transaction transaction) {
	 return new TransactionResponse(
             transaction.getId(),
             transaction.getFromWalletId(),
             transaction.getToWalletId(),
             transaction.getAmount(),
             transaction.getStatus(),
             transaction.getReferenceId()
     );
 }

}
