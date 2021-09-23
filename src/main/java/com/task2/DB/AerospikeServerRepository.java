package com.task2.DB;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AerospikeServerRepository extends CrudRepository<ServerModel, Integer> {
    //List<ServerModel> findByTotalBetween(Double from, Double to);

    //List<ServerModel> findByTotalGreaterThan(Double size);
}
