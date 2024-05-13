package com.project.shopapp.repositories;

import com.project.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//@Repository  -- ko can annotation nay vi da extends JpaRepository --> ngam hieu la mot Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


}
