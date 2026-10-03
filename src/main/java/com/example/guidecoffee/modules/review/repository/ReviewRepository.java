package com.example.guidecoffee.modules.review.repository;

import com.example.guidecoffee.modules.review.model.ReviewModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewModel, String> {
}
