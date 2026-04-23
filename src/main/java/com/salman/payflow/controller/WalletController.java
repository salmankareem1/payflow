package com.salman.payflow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salman.payflow.dto.ApiResponse;
import com.salman.payflow.dto.WalletResponse;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.service.WalletService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class WalletController {
	private final WalletService walletService;
	//private final WalletResponse walletResponse;
	
	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	private WalletResponse mapToWalletResponse(Wallet wallet) {
	    return new WalletResponse( wallet.getId(), wallet.getUserId(), wallet.getBalance(), wallet.getCurrency(), wallet.getVersion());
	}
	
	
@PostMapping("/wallet")
public ApiResponse<WalletResponse> createWallet(@RequestBody Wallet wallet) {
     Wallet savedWallet= walletService.createWallet(wallet);
	
	return new ApiResponse<>("Wallet created successfully",mapToWalletResponse(savedWallet) ) ;
}

@GetMapping("/wallets")
public ApiResponse<List<WalletResponse>> getAllWallets() {
	List<WalletResponse> wallets=walletService.getAllWallets().stream().map(this::mapToWalletResponse).collect((Collectors.toList()));
	return new ApiResponse<>("Wallets fetched successfully",wallets) ;
}

@GetMapping("/wallet/{id}")
public ApiResponse<WalletResponse> getWalletById(@PathVariable Long id) {
	Wallet wallet =walletService.getWalletById(id);
	return new ApiResponse<>("Wallet fetched successfully",mapToWalletResponse(wallet));
}

@PutMapping("/wallet/{id}")
public ApiResponse<WalletResponse> updateWalletById(@PathVariable Long id,@Valid @RequestBody Wallet walletDetails ) {
		Wallet updatedWallet=walletService.updateWallet(id, walletDetails);
		return new ApiResponse<>("wallet updated successfully",mapToWalletResponse(updatedWallet));
}

@DeleteMapping("/wallet/{id}")
public ApiResponse<String> deleteWalletById(@PathVariable Long id) {
	walletService.deleteWallet(id);
	return new ApiResponse<>("Wallet deleted successfully", "Deleted wallet id: " + id);
}

}
