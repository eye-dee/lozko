package com.lozko.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.hazelcast.core.IQueue;
import com.lozko.PasswordService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;


public class Cluster {

  private final PasswordService passwordService = new PasswordService();

  public HazelcastInstance run(int nodes) {
    Config cfg = new Config();

    val instance = Hazelcast.newHazelcastInstance(cfg);

    for (int i = 1; i < nodes; i++) {
      Hazelcast.newHazelcastInstance(cfg);
    }

    return instance;
  }

  public void makeTasks(int nodes, final HazelcastInstance instance) {
    final IQueue<Runnable> tasks = instance.getQueue("tasks");
    final IAtomicReference<byte[]> password = instance.getAtomicReference("password");
    passwordService.generate();

    password.set(passwordService.getPassword().getBytes());

    for (int i = 0; i < nodes; i++) {
      tasks.add(new Task(RandomUtils.nextBytes(3), 10_000, password.get()));
    }

  }

  @RequiredArgsConstructor
  private static final class Task implements Runnable, Serializable {

    private final byte[] start;
    private final int steps;
    private final byte[] password;


    @Override
    public void run() {
      for (int i = 0; i < steps; i++) {
        if (Arrays.equals(password, start)) {
          System.out.println("password");
          System.out.println("start = " + Arrays.toString(start));
          return;
        }

        if (!makeStep(start)) {
          return;
        }
      }
    }

    private static boolean makeStep(byte[] buffer) {
      val l = buffer.length;
      for (int i = l - 1; i >= 0; i--) {
        if (buffer[i] < 'z') {
          buffer[i]++;
          return true;
        } else {
          buffer[i] = 'a';
        }
      }

      return false;
    }
  }

}
