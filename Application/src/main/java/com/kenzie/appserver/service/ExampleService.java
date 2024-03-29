package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.service.model.Example;

import com.kenzie.capstone.service.client.ReviewLambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    private ExampleRepository exampleRepository;
    private ReviewLambdaServiceClient reviewLambdaServiceClient;

    public ExampleService(ExampleRepository exampleRepository, ReviewLambdaServiceClient reviewLambdaServiceClient) {
        this.exampleRepository = exampleRepository;
        this.reviewLambdaServiceClient = reviewLambdaServiceClient;
    }

    public Example findById(String id) {

        // Example getting data from the lambda
        ExampleData dataFromLambda = reviewLambdaServiceClient.getExampleData(id);

        // Example getting data from the local repository
        Example dataFromDynamo = exampleRepository
                .findById(id)
                .map(example -> new Example(example.getId(), example.getName()))
                .orElse(null);

        return dataFromDynamo;
    }

    public Example addNewExample(String name) {
        // Example sending data to the lambda
        ExampleData dataFromLambda = reviewLambdaServiceClient.setExampleData(name);

        // Example sending data to the local repository
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(dataFromLambda.getId());
        exampleRecord.setName(dataFromLambda.getData());
        exampleRepository.save(exampleRecord);

        Example example = new Example(dataFromLambda.getId(), name);
        return example;
    }
}
