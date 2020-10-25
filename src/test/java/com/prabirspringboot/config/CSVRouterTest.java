package com.prabirspringboot.config;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CSVRouterTest extends CamelTestSupport {

	public void setUp() throws Exception {
		deleteDirectory("test/inbox");
		deleteDirectory("test/outbox");
		super.setUp();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("file://test/inbox").to("file://test/outbox");
			}

		};

	}

	@Test
	public void testMoveFile() throws InterruptedException {

		template.sendBodyAndHeader("file://test/inbox", "Hello World", Exchange.FILE_NAME, "hello.txt");
		Thread.sleep(2000);
		File target = new File("test/outbox/hello.txt");
		assertTrue("File Not Moved", target.exists());
		String content = context.getTypeConverter().convertTo(String.class, target);

		assertEquals("Hello World", content);
		

	}

}
