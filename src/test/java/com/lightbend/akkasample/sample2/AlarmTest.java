/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.lightbend.akkasample.sample2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.akkasample.sample2.App.Alarm;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlarmTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testOnEnableWithCorrectPassword() {
        new JavaTestKit(system) {{
            new Within(duration("3 seconds")) {
                protected void run() {
                    final String correctPassword = "secret123";
                    ActorRef alarm = system.actorOf(Alarm.props(correctPassword), "alarm-correct-password");

                    alarm.tell(new Alarm.Enable(correctPassword), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());

                    expectNoMsg(duration("1 second"));
                }
            };
        }};
    }

    @Test
    public void testOnEnableWithIncorrectPassword() {
        new JavaTestKit(system) {{
            new Within(duration("3 seconds")) {
                protected void run() {
                    final String correctPassword = "secret123";
                    final String wrongPassword = "wrongpassword";
                    ActorRef alarm = system.actorOf(Alarm.props(correctPassword), "alarm-wrong-password");

                    alarm.tell(new Alarm.Enable(wrongPassword), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());

                    expectNoMsg(duration("1 second"));
                }
            };
        }};
    }

    @Test
    public void testOnEnableWithNullPassword() {
        new JavaTestKit(system) {{
            new Within(duration("3 seconds")) {
                protected void run() {
                    final String correctPassword = "secret123";
                    ActorRef alarm = system.actorOf(Alarm.props(correctPassword), "alarm-null-password");

                    alarm.tell(new Alarm.Enable(null), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());

                    expectNoMsg(duration("1 second"));
                }
            };
        }};
    }

    @Test
    public void testOnEnableWithEmptyPassword() {
        new JavaTestKit(system) {{
            new Within(duration("3 seconds")) {
                protected void run() {
                    final String correctPassword = "secret123";
                    ActorRef alarm = system.actorOf(Alarm.props(correctPassword), "alarm-empty-password");

                    alarm.tell(new Alarm.Enable(""), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());

                    expectNoMsg(duration("1 second"));
                }
            };
        }};
    }

    @Test
    public void testBehaviorChangeAfterEnable() {
        new JavaTestKit(system) {{
            new Within(duration("3 seconds")) {
                protected void run() {
                    final String correctPassword = "secret123";
                    ActorRef alarm = system.actorOf(Alarm.props(correctPassword), "alarm-behavior-change");

                    alarm.tell(new Alarm.Activity(), getRef());
                    expectNoMsg(duration("500 millis"));

                    alarm.tell(new Alarm.Enable(correctPassword), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());

                    expectNoMsg(duration("500 millis"));

                    alarm.tell(new Alarm.Disable(correctPassword), getRef());

                    alarm.tell(new Alarm.Activity(), getRef());
                    expectNoMsg(duration("500 millis"));
                }
            };
        }};
    }
}
