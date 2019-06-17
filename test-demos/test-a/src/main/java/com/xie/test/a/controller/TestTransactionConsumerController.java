package com.xie.test.a.controller;


import com.xie.test.a.buz.ConsumerAServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testConsumer")
@Deprecated
public class TestTransactionConsumerController  {

    @Autowired
    private ConsumerAServiceImpl transactionService;


}