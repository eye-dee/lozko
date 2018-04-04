package com.lozko.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IQueue;
import lombok.val;

public class Server {

  private static final Cluster cluster = new Cluster();

  public static void main(String[] args) {
    Config cfg = new Config();

    val instance = Hazelcast.newHazelcastInstance(cfg);

    final IQueue<Runnable> tasks = instance.getQueue("tasks");

    if (tasks.isEmpty()) {
      cluster.makeTasks(3, instance);
      System.out.println("prepared");
    }

    tasks.poll().run();

    instance.shutdown();
  }

}
