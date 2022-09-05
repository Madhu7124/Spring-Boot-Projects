package com.simplilearn.projects.adminportal.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplilearn.projects.adminportal.modal.Category;
import com.simplilearn.projects.adminportal.modal.Product;
import com.simplilearn.projects.adminportal.modal.Purchase;
import com.simplilearn.projects.adminportal.modal.User;
import com.simplilearn.projects.adminportal.payload.requests.ProductRequest;
import com.simplilearn.projects.adminportal.payload.requests.PurchaseRequest;
import com.simplilearn.projects.adminportal.payload.response.MessageResponse;
import com.simplilearn.projects.adminportal.repository.CategoryRepository;
import com.simplilearn.projects.adminportal.repository.ProductRepository;
import com.simplilearn.projects.adminportal.repository.PurchaseRepository;
import com.simplilearn.projects.adminportal.repository.UserRepository;
import com.simplilearn.projects.adminportal.security.jwt.JwtUtils;
import com.simplilearn.projects.adminportal.security.services.UserDetailsImpl;

@RestController
@Validated
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	PurchaseRepository purchaseRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/users")
	public ResponseEntity<?> findAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@GetMapping("/users/{user_id}")
	public ResponseEntity<?> findUserById(@PathVariable("user_id") Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		} else {
			return ResponseEntity.ok(new MessageResponse("Unable to find User with ID : " + userId));
		}

	}

	@PostMapping("/isvaliduser")
	public ResponseEntity<?> isValidUser(@RequestParam("email") String email) {
		boolean isValid = userRepository.existsByEmail(email);
		if (isValid) {
			return ResponseEntity.ok(new MessageResponse("User is valid!"));
		} else {
			return ResponseEntity.ok(new MessageResponse("User is not valid!"));
		}
	}

	@PutMapping("/changepassword")
	public ResponseEntity<?> isValidUser(@RequestParam("email") String email,
			@RequestParam("oldpassword") String oldPassword, @RequestParam("newpassword") String newPassword) {
		User user = userRepository.findByEmail(email);
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oldPassword));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		if (Objects.nonNull(userDetails) && Objects.nonNull(userDetails.getUsername())) {
			user.setPassword(encoder.encode(newPassword));
			User updatedUser = userRepository.save(user);
			if (Objects.nonNull(updatedUser)) {
				ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
				return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
						.body(new MessageResponse("Password updated successfully. Please login with new password"));
			}
			return ResponseEntity.ok(new MessageResponse("Unable to change password"));
		} else {
			return ResponseEntity.ok(new MessageResponse("User is not valid!"));
		}
	}

	@GetMapping("/products")
	public ResponseEntity<?> findAllProducts() {
		return ResponseEntity.ok(productRepository.findAll());
	}

	@GetMapping("/categories")
	public ResponseEntity<?> findAllCategories() {
		return ResponseEntity.ok(categoryRepository.findAll());
	}

	@GetMapping("/findbyproductid/{product_id}")
	public ResponseEntity<?> findByProductId(@PathVariable("product_id") Long productId) {
		Optional<Product> product = productRepository.findById(productId);
		if (!product.isEmpty()) {
			return ResponseEntity.ok(product.get());
		} else {
			return ResponseEntity.ok(new MessageResponse("Unable to find Product by Id : " + productId));
		}
	}

	@GetMapping("/findbycategoryid/{category_id}")
	public ResponseEntity<?> findByCategoryId(@PathVariable("category_id") Long categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (!category.isEmpty()) {
			return ResponseEntity.ok(category.get());
		} else {
			return ResponseEntity.ok(new MessageResponse("Unable to find Category by Id : " + categoryId));
		}
	}

	@PostMapping("/saveorupdateproduct")
	public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductRequest productRequestVO) {
		if (Objects.nonNull(productRequestVO)) {
			Category category = null;
			if (Objects.nonNull(productRequestVO.getCategoryid())) {
				Optional<Category> categoryObj = categoryRepository.findById(productRequestVO.getCategoryid());
				if (categoryObj.isPresent()) {
					category = categoryObj.get();
				}
			}

			if (Objects.isNull(category) && (Objects.nonNull(productRequestVO.getCategoryname())
					&& Objects.nonNull(productRequestVO.getCategorytype()))) {
				category = categoryRepository.findByNameAndType(productRequestVO.getCategoryname(),
						productRequestVO.getCategorytype());
			}

			if (Objects.nonNull(category)) {
				category.setName(productRequestVO.getCategoryname());
				category.setDescription(productRequestVO.getCategorydesc());
				category.setType(productRequestVO.getCategorytype());
			} else {
				category = new Category(productRequestVO.getCategoryname(), productRequestVO.getCategorytype(),
						productRequestVO.getCategorydesc());
			}

			category = categoryRepository.save(category);

			if (Objects.nonNull(category)) {
				Product product = null;
				if (Objects.nonNull(productRequestVO.getId())) {
					Optional<Product> productObj = productRepository.findById(productRequestVO.getId());
					if (productObj.isPresent()) {
						product = productObj.get();
					}
				}

				if (Objects.nonNull(product)) {
					product.setName(productRequestVO.getName());
					product.setDescription(productRequestVO.getDescription());
					product.setCategoryId(category.getId());
				} else {
					if (Objects.nonNull(productRequestVO.getName())) {
						product = productRepository.findByName(productRequestVO.getName());
						if (Objects.isNull(product)) {
							product = new Product();
						}
						product.setName(productRequestVO.getName());
						product.setDescription(productRequestVO.getDescription());
						product.setCategoryId(category.getId());
					} else {
						return ResponseEntity.ok(new MessageResponse("Unable to save Product - Product name invalid!"));
					}

				}

				product = productRepository.saveAndFlush(product);

				if (Objects.nonNull(product.getId())) {
					product.setCategory(category);
					return ResponseEntity.ok(product);
				} else {
					return ResponseEntity.ok(new MessageResponse("Unable to save Product - Product details Invalid!"));
				}
			} else {
				return ResponseEntity.ok(new MessageResponse("Unable to save Product - Category details Invalid!"));
			}
		}
		return ResponseEntity.ok(new MessageResponse("Unable to save Product"));
	}

	@PostMapping("/saveorupdatepurchase")
	public ResponseEntity<?> savePurchase(@Valid @RequestBody PurchaseRequest purchaseRequestVO) {
		if (Objects.nonNull(purchaseRequestVO)) {
			Purchase purchase = null;
			if (Objects.nonNull(purchaseRequestVO.getId())) {
				Optional<Purchase> purchaseObj = purchaseRepository.findById(purchaseRequestVO.getId());
				if (purchaseObj.isPresent()) {
					purchase = purchaseObj.get();
				}
			}

			if (Objects.nonNull(purchase)) {
				purchase.setCategoryId(purchaseRequestVO.getCategoryid());
				purchase.setProductId(purchaseRequestVO.getProductid());
				purchase.setPurchaseDate(purchaseRequestVO.getPurchasedate());
			} else {
				purchase = new Purchase();
				purchase.setCategoryId(purchaseRequestVO.getCategoryid());
				purchase.setProductId(purchaseRequestVO.getProductid());
				purchase.setPurchaseDate(purchaseRequestVO.getPurchasedate());
			}

			purchase = purchaseRepository.saveAndFlush(purchase);

			if (Objects.nonNull(purchase.getId())) {
				return ResponseEntity.ok(purchase);
			} else {
				return ResponseEntity
						.ok(new MessageResponse("Unable to create purchase order - purchase details invalid!"));
			}
		}
		return ResponseEntity.ok(new MessageResponse("Unable to create purchase order"));
	}
	
	@GetMapping("/purchaseorders")
	public ResponseEntity<?> findAllPurchases() {
		return ResponseEntity.ok(purchaseRepository.findAll());
	}
	
	@GetMapping("/purchasebycategory/{category_id}")
	public ResponseEntity<?> findPurchaseByCategoryId(@PathVariable("category_id") Long categoryId) {
		List<Purchase> purchaseList = purchaseRepository.findByCategoryId(categoryId);
		if(!purchaseList.isEmpty()) {
			return ResponseEntity.ok(purchaseList);
		}
		return ResponseEntity.ok(new MessageResponse("Unable to find purchase order with category id : "+categoryId));
	}
	
	@PostMapping("/purchasebydate")
	public ResponseEntity<?> findPurchaseByDate(@RequestParam("purchasedate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date purchaseDate) {
		List<Purchase> purchaseList = purchaseRepository.findByPurchaseDate(purchaseDate);
		if(!purchaseList.isEmpty()) {
			return ResponseEntity.ok(purchaseList);
		}
		return ResponseEntity.ok(new MessageResponse("Unable to find purchase order with purchase date : "+purchaseDate));
	}

}
