package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.ExceededParamException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService iProductService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           //@ModelAttribute("files") List<MultipartFile> files,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
            }
            Product newProduct = iProductService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("product_id") Long productId,
            @Valid @ModelAttribute("files") List<MultipartFile> files) {
        try {
            List<ProductImage> productImageList = new ArrayList<>();
            Product existingProduct = iProductService.getProductById(productId);
            if (files.isEmpty())
                files = new ArrayList<>();
            else if (files.size() > ProductImage.MAX_IMAGES_PER_PRODUCT)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not upload exceeded "
                        + ProductImage.MAX_IMAGES_PER_PRODUCT + " images");
            for(MultipartFile file : files) {
                if (file.getSize() == 0) continue;
                if (file.getSize() > 10*1024*1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File must be less than 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be Image");
                }
                String fileName = storeFile(file);
                ProductImageDTO newProductImageDTO = ProductImageDTO.builder()
                        .productId(existingProduct.getId())
                        .imageUrl(fileName)
                        .build();
                ProductImage newProductImage = iProductService.createProductImage(newProductImageDTO);
                productImageList.add(newProductImage);
            }
            return ResponseEntity.status(HttpStatus.OK).body(productImageList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(String.valueOf(uploadDir), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam int page,
            @RequestParam int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page - 1, limit, // tru 1 vi page bat dau tinh tu 0
                Sort.by("createdAt").descending());
        Page<ProductResponse> productResponsePages = iProductService.getAllProducts(pageRequest);
        int totalPages = productResponsePages.getTotalPages();  // = xai may cong thuc tinh toan
        List<ProductResponse> productResponses = productResponsePages.getContent(); // tra ve list Product cua Product Page
        // page bat dau tu 0
        return ResponseEntity.status(HttpStatus.OK).body(
                ProductListResponse.builder()
                        .products(productResponses)
                        .totalPages(totalPages)
                        .build());
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable("product_id") Long id) {
        try {
            Product existingProduct = iProductService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ProductResponse.fromProduct(existingProduct));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable("product_id") Long id,
                                                @Valid @RequestBody ProductDTO productDTO) {
        try {
            iProductService.updateProduct(id, productDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("updateProduct %d", id));
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("product_id") Long id) {
        try {
            iProductService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted product " + id + " successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        int numberFakeProducts = 1000000;
        for (int i=0; i<numberFakeProducts; i++) {
            String productName = faker.commerce().productName();
            if (iProductService.existsByName(productName)) continue;
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10,90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1,4))
                    .thumbnail("")
                    .build();
            try {
                iProductService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("generate " + numberFakeProducts + " fake products successfully");
    }
}
