package com.general.rentacalculator;

import com.general.rentacalculator.services.ResultService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ResultServiceTest {

    private ResultService resultService;

    @Before
    public void setUp(){
        resultService = new ResultService();
    }

    @Test
    public void cotaTest_smallerThanFirstLimit(){
        // given
        double baseCalculo = 3000;
        Map<Double,Double> tramos = new HashMap<>();
        tramos.put(6000d,9.0);
        tramos.put(10000d,15.5);
        // actually, infinity
        tramos.put(35000d,50d);

        // assert
//        double result = resultService.rentaResultCalculator(baseCalculo,tramos);

        // then
//        Assert.assertEquals( 270d,result,0.01);

    }


    @Test
    public void cotaTest_biggerThanLastLimit(){
        // given
        double baseCalculo = 63000;
        Map<Double,Double> tramos = new HashMap<>();
        tramos.put(17707.20,12.0);
        tramos.put(33007.20,14.0);
        tramos.put(40000d,16.0);
        // actually, infinity
        tramos.put(50000.20,20.1);

        // assert
//        double result = resultService.calculateCota(baseCalculo,tramos);

        // then
//        Assert.assertEquals( 10008.71,result,0.01);

    }

    @Test
    public void cotaTest_insideLImitsAndUnordered(){
        // given
        double baseCalculo = 23216.46;
        Map<Double,Double> tramos = new HashMap<>();
        //2nd
        tramos.put(20200d,12d);
        //1st
        tramos.put(12450d,9.5);
        //3rd actually, infinity
        tramos.put(35200d,15d);

        // assert
//        double result = resultService.calculateCota(baseCalculo,tramos);

        // then
//        Assert.assertEquals( 2565.22,result,0.01);

    }

}
