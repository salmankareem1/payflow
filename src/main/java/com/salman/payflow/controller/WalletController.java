package com.salman.payflow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salman.payflow.dto.ApiResponse;
import com.salman.payflow.dto.CreateWalletRequest;
import com.salman.payflow.dto.WalletResponse;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class WalletController {
	private final WalletService walletService;
	
	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	private WalletResponse mapToWalletResponse(Wallet wallet) {
	    return new WalletResponse( wallet.getId(), wallet.getUserId(), wallet.getBalance(), wallet.getCurrency(), wallet.getVersion());
	}
	
	 // 201 CREATED — a new resource was created
@PostMapping("/wallet")
public ResponseEntity<ApiResponse<WalletResponse>> createWallet(@Valid @RequestBody CreateWalletRequest createWalletRequest) {
     Wallet savedWallet= walletService.createWallet(createWalletRequest);
	
	return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Wallet created Successfully",mapToWalletResponse(savedWallet)));
}

//200 OK
@GetMapping("/wallets")
public ResponseEntity<ApiResponse<List<WalletResponse>>> getAllWallets() {
	List<WalletResponse> wallets=walletService.getAllWallets().stream().map(this::mapToWalletResponse).collect((Collectors.toList()));
	return ResponseEntity.ok(new ApiResponse<>("Wallets fetched Successfully",wallets));
}

//200 OK — 404 handled automatically by WalletNotFoundException in GlobalExceptionHandler
@GetMapping("/wallet/{id}")
public ResponseEntity<ApiResponse<WalletResponse>> getWalletById(@PathVariable Long id) {
	Wallet wallet =walletService.getWalletById(id);
	return ResponseEntity.ok(new ApiResponse<>("Wallet fetched Successfully",mapToWalletResponse(wallet)));
}

//200 OK
@PutMapping("/wallet/{id}")
public ResponseEntity<ApiResponse<WalletResponse>> updateWalletById(@PathVariable Long id,@Valid @RequestBody Wallet walletDetails ) {
		Wallet updatedWallet=walletService.updateWallet(id, walletDetails);
		return ResponseEntity.ok(new ApiResponse<>("Wallet updated Successfully", mapToWalletResponse(updatedWallet)));
}

// 204 NO CONTENT — successful deletion has no body to return
@DeleteMapping("/wallet/{id}")
public ResponseEntity<Void> deleteWalletById(@PathVariable Long id) {
	walletService.deleteWallet(id);
	return ResponseEntity.noContent().build();
}

}
