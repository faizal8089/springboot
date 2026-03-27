package com.faizal.springboot.repository;

import com.faizal.springboot.models.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @Query("select price from ProductItem p where p.id = :id")
    Double findCustom(@Param("id") Long id);
}
