package com.work2win;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.xml.bind.JAXBContext;

@SpringBootApplication
public class SpringBootCamelApplication extends RouteBuilder{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamelApplication.class, args);
	}
	
	@Override
	public void configure() throws Exception {
		
		//sample quick start camel with spring boot
		
		String fileName = "dataSchema";
		String fileNameContent = "how are you";
		from("file:C:/tmp/LOC1").to("file:C:/tmp/LOC2");
		from("file:C:/tmp/LOC3").filter(header(Exchange.FILE_NAME).startsWith(fileName)).to("file:C:/tmp/LOC4");
		from("file:C:/tmp/LOC5").filter(body().startsWith(fileNameContent)).to("file:C:/tmp/LOC6");
		
		//making some changes inside the file and send it to new location
		from("file:C:/tmp/LOC7").process(x -> {
			String body = x.getIn().getBody(String.class);
			StringBuilder sb = new StringBuilder();
			Arrays.stream(body.split(" ")).forEach(s -> {
				sb.append(s+"-");
			});
			x.getIn().setBody(sb);
		}).to("file:C:/tmp/LOC8");
		
		//based on the type of string move it to different file destinations
		
		from("file:C:/tmp/LOC9").unmarshal().csv().split(body().tokenize(",")).choice()
		.when(body().contains("CLOSED")).to("file:C:/tmp/LOC10?fileName=close.csv")
		.when(body().contains("PENDING")).to("file:C:/tmp/LOC10?fileName=Pending.csv")
		.when(body().contains("INTEREST")).to("file:C:/tmp/LOC10?fileName=Interest.csv");
		
	}

}
