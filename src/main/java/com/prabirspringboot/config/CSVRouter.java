package com.prabirspringboot.config;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.springframework.stereotype.Component;

@Component
public class CSVRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		CsvDataFormat csv = new CsvDataFormat();
		csv.setDelimiter(",");
		from("file://in?fileName=data.csv&noop=true&delay=15m").unmarshal().csv().convertBodyTo(List.class)
				.process(new Processor() {

					public void process(Exchange msg) throws Exception {
						int c = 0;

						List<List<String>> data = (List<List<String>>) msg.getIn().getBody();
						for (List<String> line : data) {

							for (String i : line) {

								c = c + Integer.parseInt(i);
							}
						}

						msg.getIn().setBody(c);

					}
				})

				.marshal(csv).to("file://out?fileName=fileadd.done").log("done.").end();
	}
}
