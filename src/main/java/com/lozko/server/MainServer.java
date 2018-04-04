package com.lozko.server;

import com.hazelcast.core.Hazelcast;
import java.io.Serializable;

public class MainServer {

  public static void main(String[] args) {
    Hazelcast.newHazelcastInstance();
  }
}
